package cl.buildersoft.framework.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.sql.Connection;
import java.sql.Timestamp;
import java.util.Enumeration;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;
import cl.buildersoft.bean.sso.SessionBean;
import cl.buildersoft.bean.sso.SessionDataBean;
import cl.buildersoft.framework.beans.Domain;
import cl.buildersoft.framework.beans.User;
import cl.buildersoft.framework.database.BSBeanUtils;
import cl.buildersoft.framework.database.BSmySQL;
import cl.buildersoft.framework.exception.BSSystemException;

// D:\temp\5\20160304\app-web-master\app-web-master\sso\src\cl\buildersoft\sso\filter
public class BSHttpServlet_ extends HttpServlet {
	private static final Logger LOG = Logger.getLogger(BSHttpServlet_.class.getName());
	private static final long serialVersionUID = 7807647668104655759L;
	private static final String SESSION_COOKIE_NAME = "SESSION_SSO";
	private static final int MAX_AGE = 24 * 60 * 60;
	private static final String ROOT = "/";

	protected Connection getConnection(HttpServletRequest request) {
		BSConnectionFactory cf = new BSConnectionFactory();
		return cf.getConnection(request);
	}

	protected void closeConnection(Connection conn) {
		(new BSmySQL()).closeConnection(conn);
	}

	protected void setApplicationValue(HttpServletRequest request, String name, Object object) {
		request.getServletContext().setAttribute(name, object);
	}

	protected Object getApplicationValue(HttpServletRequest request, String name) {
		return request.getServletContext().getAttribute(name);
	}

	protected void setSessionValue(HttpServletRequest request, String name, Object object) {
		request.getSession(false).setAttribute(name, object);
	}

	protected Object getSessionValue(HttpServletRequest request, String name) {
		return request.getSession(false).getAttribute(name);
	}

	protected String readParameterOrAttribute(HttpServletRequest request, String name) {
		String out = null;
		Object object = request.getAttribute(name);
		if (object != null) {
			out = (String) object;
		} else {
			out = request.getParameter(name);
		}
		return out;
	}

	protected User getCurrentUser(HttpServletRequest request) {
		return (User) request.getSession(false).getAttribute("User");
	}

	protected Domain getCurrentDomain(HttpServletRequest request) {
		return (Domain) request.getSession(false).getAttribute("Domain");
	}

	protected void showParameters(HttpServletRequest request) {
		Enumeration<String> names = request.getParameterNames();
		String name = null;
		while (names.hasMoreElements()) {
			name = (String) names.nextElement();

			LOG.log(Level.CONFIG, "Name={0}", request.getParameter(name));

		}
	}

	protected Boolean bootstrap(Connection conn) {
		Boolean bootstrap = false;
		BSConfig config = new BSConfig();
		bootstrap = config.getBoolean(conn, "BOOTSTRAP");
		bootstrap = bootstrap == null ? false : bootstrap;

		return bootstrap;

	}

	/***************************************/
	// Desde aqui las funciones que sirven para el manejo de sesion.
	/***************************************/
	protected void forward(HttpServletRequest request, HttpServletResponse response, String uri) throws ServletException,
			IOException {
		forward(request, response, uri, true);
	}

	protected void forward(HttpServletRequest request, HttpServletResponse response, String uri, Boolean saveSessionValues)
			throws ServletException, IOException {
		if (saveSessionValues) {
			LOG.log(Level.INFO, "Savind values");
			updateSession(request, response);
		}
		request.getRequestDispatcher(uri).forward(request, response);
	}

	protected void redirect(HttpServletRequest request, HttpServletResponse response, String url) throws ServletException,
			IOException {
		updateSession(request, response);
		response.sendRedirect(url);
	}

	protected HttpSession createSession(HttpServletRequest request, HttpServletResponse response) {
		HttpSession session = request.getSession(true);
		String sessionId = session.getId();
		saveCookieToResponse(response, sessionId);
		request.setAttribute(SESSION_COOKIE_NAME, sessionId);
		BSConnectionFactory cf = new BSConnectionFactory();
		Connection conn = cf.getConnection();
		try {
			saveSessionToDB(conn, session, sessionId);
		} finally {
			cf.closeConnection(conn);
		}
		return session;
	}

	protected void deleteSession(HttpServletRequest request, HttpServletResponse response) {
		HttpSession session = request.getSession(false);
		if (session != null) {
			String sessionId = session.getId();

			saveCookieToResponse(response, sessionId, true);

			deleteSessionOfDB(session, sessionId);
			session.invalidate();
		}
	}

	public void updateSession(HttpServletRequest request, HttpServletResponse response) {
		String cookie = readCookieValue(request);
		if (cookie != null) {
			HttpSession session = request.getSession(false);

			if (session != null) {
				BSConnectionFactory cf = new BSConnectionFactory();
				Connection conn = cf.getConnection();
				try {
					// saveSessionToDB(conn, session, sessionId);
					saveSessionToDB(conn, session, cookie);
				} finally {
					cf.closeConnection(conn);
				}

			}
			saveCookieToResponse(response, cookie);
		}
	}

	public void restoreSession(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String cookie = readCookieValue(request);

		if (cookie == null) {
			redirect(request, response, ROOT);
			// request.getRequestDispatcher("Invaliduser.jsp").forward(request,
			// response);
		} else {
			HttpSession session = request.getSession(true);
			BSConnectionFactory cf = new BSConnectionFactory();
			Connection conn = cf.getConnection();
			try {
				// saveSessionToDB(conn, session, sessionId);
				// saveSessionToDB(conn, session, cookie);
				readSessionDataFromDB(conn, session, cookie);
			} finally {
				cf.closeConnection(conn);
			}

		}

	}

	protected void deleteSessionOfDB(HttpSession session, String sessionId) {
		SessionBean sessionBean = new SessionBean();
		SessionDataBean sessionDataBean = new SessionDataBean();
		BSBeanUtils bu = new BSBeanUtils();

		BSConnectionFactory cf = new BSConnectionFactory();
		Connection conn = cf.getConnection();
		try {
			bu.search(conn, sessionBean, "cSessionId=?", sessionId);

			List<SessionDataBean> dataList = (List<SessionDataBean>) bu.list(conn, sessionDataBean, "cSession=?",
					sessionBean.getId());

			for (SessionDataBean data : dataList) {
				bu.delete(conn, data);
			}
			bu.delete(conn, sessionBean);

		} finally {
			cf.closeConnection(conn);
		}

	}

	protected SessionBean saveSessionToDB(Connection conn, HttpSession session, String sessionId) {
		SessionBean sessionBean = new SessionBean();
		SessionDataBean sessionDataBean = null;
		BSBeanUtils bu = new BSBeanUtils();

		sessionBean.setSessionId(sessionId);
		bu.search(conn, sessionBean, "cSessionId=?", sessionId);
		sessionBean.setLastAccess(new Timestamp(System.currentTimeMillis()));
		bu.save(conn, sessionBean);

		Enumeration<String> names = session.getAttributeNames();
		String name = null;

		while (names.hasMoreElements()) {
			name = names.nextElement();
			sessionDataBean = new SessionDataBean();

			sessionDataBean.setSession(sessionBean.getId());
			bu.search(conn, sessionDataBean, "cSession=? AND cName=?", sessionBean.getId(), name);
			sessionDataBean.setSession(sessionBean.getId());

			sessionDataBean.setName(name);
			sessionDataBean.setData(objectToString(session.getAttribute(name)));

			bu.save(conn, sessionDataBean);
		}
		return sessionBean;
	}

	public String readCookieValue(HttpServletRequest request) {
		Cookie[] cookies = request.getCookies();
		String out = null;

		if (cookies != null) {
			for (Cookie currentCookie : cookies) {
				if (currentCookie.getName().equals(SESSION_COOKIE_NAME)) {
					out = currentCookie.getValue();
					break;
				}
			}
		}

		if (out == null) {
			Object obj = request.getAttribute(SESSION_COOKIE_NAME);
			request.setAttribute(SESSION_COOKIE_NAME, null);
			if (obj != null) {
				out = (String) obj;
			}
		}
		return out;
	}

	public String objectToString(Object obj) throws BSSystemException {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		ObjectOutputStream oos;
		try {
			oos = new ObjectOutputStream(baos);
			oos.writeObject(obj);
		} catch (IOException e) {
			throw new BSSystemException(e);
		}
		byte[] userAsBytes = baos.toByteArray();

		BASE64Encoder encoder = new BASE64Encoder();
		String out = encoder.encodeBuffer(userAsBytes);

		return out;
	}

	private Object stringToObject(String str) throws BSSystemException {
		BASE64Decoder decoder = new BASE64Decoder();
		ObjectInputStream ois = null;
		Object out = null;
		try {
			byte[] objectAsBytes = decoder.decodeBuffer(str);
			ByteArrayInputStream bais = new ByteArrayInputStream(objectAsBytes);
			ois = new ObjectInputStream(bais);
			out = ois.readObject();
		} catch (Exception e) {
			throw new BSSystemException(e);
		}

		return out;
	}

	public Cookie saveCookieToResponse(HttpServletResponse response, String sessionId) {
		return saveCookieToResponse(response, sessionId, false);
	}

	public Cookie saveCookieToResponse(HttpServletResponse response, String sessionId, Boolean delete) {
		Cookie cookie = new Cookie(SESSION_COOKIE_NAME, sessionId);
		cookie.setPath(ROOT);
		cookie.setMaxAge(delete ? 0 : MAX_AGE);
		response.addCookie(cookie);
		return cookie;
	}

	private void readSessionDataFromDB(Connection conn, HttpSession session, String sessionValue) {
		BSBeanUtils bu = new BSBeanUtils();
		SessionBean sessionBean = new SessionBean();
		SessionDataBean sessionDataBean = new SessionDataBean();

		bu.search(conn, sessionBean, "cSessionId=?", sessionValue);

		List<SessionDataBean> objectList = (List<SessionDataBean>) bu.list(conn, sessionDataBean, "cSession=?",
				sessionBean.getId());
		Object obj = null;
		for (SessionDataBean record : objectList) {
			obj = stringToObject(record.getData());
			session.setAttribute(record.getName(), obj);
		}
	}
}

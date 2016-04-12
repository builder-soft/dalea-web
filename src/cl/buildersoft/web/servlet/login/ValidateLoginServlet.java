package cl.buildersoft.web.servlet.login;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import cl.buildersoft.framework.beans.Domain;
import cl.buildersoft.framework.beans.DomainAttribute;
import cl.buildersoft.framework.beans.Rol;
import cl.buildersoft.framework.beans.User;
import cl.buildersoft.framework.business.services.EventLogService;
import cl.buildersoft.framework.business.services.ServiceFactory;
import cl.buildersoft.framework.database.BSBeanUtils;
import cl.buildersoft.framework.database.BSmySQL;
import cl.buildersoft.framework.exception.BSDataBaseException;
import cl.buildersoft.framework.exception.BSUserException;
import cl.buildersoft.framework.services.BSUserService;
import cl.buildersoft.framework.services.impl.BSUserServiceImpl;
import cl.buildersoft.framework.util.BSConfig;
import cl.buildersoft.framework.util.BSConnectionFactory;
import cl.buildersoft.framework.util.BSDateTimeUtil;
import cl.buildersoft.framework.util.BSUtils;
import cl.buildersoft.framework.web.servlet.BSHttpServlet_;

/**
 * Servlet implementation class ValidateServlet
 */

@WebServlet(urlPatterns = "/login/ValidateLoginServlet")
public class ValidateLoginServlet extends BSHttpServlet_ {
	private static final Logger LOG = Logger.getLogger(ValidateLoginServlet.class.getName());
	private static final String NOT_FOUND_JSP = "/WEB-INF/jsp/common/not-found.jsp";
	private static final long serialVersionUID = 3161065592126995826L;

	public ValidateLoginServlet() {
		super();
	}

	protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String mail = request.getParameter("mail");
		String password = request.getParameter("password");
		String page = null;

		// LOG.log(Level.INFO, "Starting ValidateLoginServlet");

		Boolean validData = validInputData(mail, password);

		if (validData) {
			BSUserService userService = new BSUserServiceImpl();
			BSConnectionFactory cf = new BSConnectionFactory();
			Connection connTemp = null;

			User user = null;
			List<Rol> rols = null;
			Connection connBSframework = null;
			Connection connDomain = null;

			connBSframework = cf.getConnection();

			EventLogService eventLog = ServiceFactory.createEventLogService();

			User mayBeUser = userExists(connBSframework, userService, mail);

			if (mayBeUser == null) {
				page = NOT_FOUND_JSP;

				List<Domain> domains = getAllDomains(connBSframework);

				for (Domain domain : domains) {
					connTemp = cf.getConnection(domain.getDatabase());
					eventLog.writeEntry(connTemp, userService.getAnonymousUser().getId(), "SECURITY_LOGIN_FAIL",
							"Alguien intentó acceder con el usuario \"%s\", inexistente.", mail);
					cf.closeConnection(connTemp);
				}
			} else {
				LOG.log(Level.FINE, "Validing user {0}, password {1}", BSUtils.array2ObjectArray(mail, password));
				user = userService.login(connBSframework, mail, password);
				LOG.log(Level.INFO, "User: {0}", user);

				if (user == null && mayBeUser != null) {
					List<Domain> mayBeTheirDomains = getDomains(connBSframework, mayBeUser);
					connTemp = null;
					for (Domain domain : mayBeTheirDomains) {
						connTemp = cf.getConnection(domain.getDatabase());
						eventLog.writeEntry(connTemp, mayBeUser.getId(), "SECURITY_LOGIN_FAIL",
								"El usuario intentó acceder con una clave invalida", null);
						cf.closeConnection(connTemp);
					}
					page = NOT_FOUND_JSP;

				} else {
					List<Domain> domains = null;
					Domain defaultDomain = null;
					Map<String, DomainAttribute> domainAttribute = null;
					if (user != null) {
						domains = getDomains(connBSframework, user);
						if (domains.size() == 0) {
							cf.closeConnection(connDomain);
							cf.closeConnection(connBSframework);
							throw new BSUserException("El usuario '" + user.getMail() + "' no tiene dominios configurados");
						}
						defaultDomain = domains.get(0);

						connDomain = cf.getConnection(defaultDomain.getDatabase());

						rols = userService.getRols(connDomain, user);
						if (rols.size() == 0) {
							String msg = "Usuario no tiene roles configurados";
							eventLog.writeEntry(connDomain, user.getId(), "CONFIG_FAIL", msg, null);
							cf.closeConnection(connDomain);
							cf.closeConnection(connBSframework);
							throw new BSUserException(msg);
						}
					}

					if (passwordExpired(connDomain, user)) {
						request.setAttribute("cId", user.getId());
						page = "/WEB-INF/jsp/login/password-expired2.jsp";

					}
					if (user != null) {
						HttpSession session = createSession(request, response);

//						System.out.println(session.getId());
//						System.out.println(System.currentTimeMillis());
//						System.out.println(session.getAttribute("SESSION_SSO").toString() );
						
						//SessionId
						synchronized (session) {
							session.setAttribute("User", user);
							session.setAttribute("Rol", rols);
							session.setAttribute("Menu", null);
							session.setAttribute("Domains", domains);
							session.setAttribute("Domain", defaultDomain);
							session.setAttribute("DomainAttribute", domainAttribute);
						}
						if (page == null) {
							page = "/servlet/login/GetMenuServlet";

						}

						eventLog.writeEntry(connDomain, user.getId(), "SECURITY_LOGIN_OK",
								"Acceso correcto al sistema, Rol/es: %s.", enumerateRols(rols));
					}
				}
			}
			cf.closeConnection(connDomain);
			cf.closeConnection(connBSframework);
		}

		if (page == null) {
			page = "/";
		}
		forward(request, response, page, false);
	}

	private boolean passwordExpired(Connection conn, User user) {
		BSConfig config = new BSConfig();

		Calendar d1 = BSDateTimeUtil.date2Calendar(user.getLastChangePass());

		LOG.log(Level.FINE, "User: {0} - LastChangePass: {1}",
				BSUtils.array2ObjectArray(user.getMail(), BSDateTimeUtil.calendar2String(d1, "yyyy-MM-dd hh:mm:ss")));

		Integer passChangeDays = config.getInteger(conn, "PASS_CHANGE_DAYS", 90);
		LOG.log(Level.FINE, "PassChangeDays: {0}  ", passChangeDays);

		Long daysDiff = BSDateTimeUtil.dateDiff(d1, Calendar.getInstance());
		LOG.log(Level.FINE, "Days diff {0}, for {1}", BSUtils.array2ObjectArray(daysDiff, user.getMail()));

		Boolean out = daysDiff > passChangeDays;

		LOG.log(Level.FINE, "Out is: {0}", out);

		return out;
	}

	private User userExists(Connection connBSframework, BSUserService us, String mail) {
		return us.search(connBSframework, mail);
	}

	private Boolean validInputData(String mail, String password) {
		mail = "".equals(mail) ? null : mail;
		password = "".equals(password) ? null : password;
		Boolean out = true;
		if (mail == null || password == null) {
			out = false;
		}
		return out;
	}

	private Object enumerateRols(List<Rol> rols) {
		String out = "";

		for (Rol rol : rols) {
			out += rol.getName() + ",";
		}
		out = out.substring(0, out.length() - 1);

		return out;
	}

	public Map<String, DomainAttribute> getDomainAttribute(Connection conn, Domain defaultDomain) {
		BSmySQL mysql = new BSmySQL();
		ResultSet rs = mysql.callSingleSP(conn, "pListDomainAttributes", defaultDomain.getId());

		BSBeanUtils bu = new BSBeanUtils();
		Map<String, DomainAttribute> out = new HashMap<String, DomainAttribute>();

		DomainAttribute domainAttribute = null;
		try {
			while (rs.next()) {
				domainAttribute = new DomainAttribute();
				domainAttribute.setId(rs.getLong("cId"));

				bu.search(conn, domainAttribute);

				out.put(domainAttribute.getKey(), domainAttribute);
			}
		} catch (SQLException e) {
			throw new BSDataBaseException(e);
		} finally {
			mysql.closeSQL(rs);
		}

		return out;
	}

	private List<Domain> getDomains(Connection conn, User user) {
		BSmySQL mysql = new BSmySQL();

		ResultSet rs = mysql.callSingleSP(conn, "pListDomainsForUser", user.getId());

		BSBeanUtils bu = new BSBeanUtils();
		List<Domain> out = new ArrayList<Domain>();
		Domain domain = null;
		try {
			while (rs.next()) {
				domain = new Domain();
				domain.setId(rs.getLong("cId"));
				bu.search(conn, domain);
				out.add(domain);
			}
			mysql.closeSQL(rs);
		} catch (SQLException e) {
			throw new BSDataBaseException(e);
		}

		return out;
	}

	private List<Domain> getAllDomains(Connection conn) {
		BSBeanUtils bu = new BSBeanUtils();
		List<Domain> out = new ArrayList<Domain>();
		Domain domain = new Domain();

		out = (List<Domain>) bu.listAll(conn, domain);

		return out;
	}

}

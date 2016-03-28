package cl.buildersoft.web.servlet.system.changepassword;

import java.io.IOException;
import java.sql.Connection;
import java.util.Calendar;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cl.buildersoft.framework.beans.User;
import cl.buildersoft.framework.business.services.EventLogService;
import cl.buildersoft.framework.business.services.ServiceFactory;
import cl.buildersoft.framework.database.BSBeanUtils;
import cl.buildersoft.framework.exception.BSUserException;
import cl.buildersoft.framework.util.BSConfig;
import cl.buildersoft.framework.util.BSConnectionFactory;
import cl.buildersoft.framework.util.BSDateTimeUtil;
import cl.buildersoft.framework.util.BSSecurity;
import cl.buildersoft.framework.web.servlet.BSHttpServlet_;

@WebServlet("/servlet/system/changepassword/ChangePassword")
public class ChangePassword extends BSHttpServlet_ {
	private static final String UPPER_LETTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
	private static final String LOWER_LETTERS = UPPER_LETTERS.toLowerCase();
	private static final String NUMBERS = "0123456789";
	private static final String ALL_VALID_CHARS = UPPER_LETTERS + LOWER_LETTERS + NUMBERS;

	private static final long serialVersionUID = -7579742240257360732L;

	private static final String PASS_MIN_LEN = "PASS_MIN_LEN";
	private static final String PASS_SPEC_CHR = "PASS_SPEC_CHR";
	private static final String PASS_UPPER_CHR = "PASS_UPPER_CHR";
	private static final String PASS_NUM_CHR = "PASS_NUM_CHR";

	public ChangePassword() {
		super();
	}

	@Override
	protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String newPassword = request.getParameter("NewPassword");
		String commitPassword = request.getParameter("CommitPassword");
		// String goHome = request.getParameter("GoHome");
		Boolean reset = getReset(request);
		String oldPassword = request.getParameter("OldPassword");
		String next = request.getParameter("Next");

		// String currentPassword = null;
		EventLogService eventLog = ServiceFactory.createEventLogService();
		BSConnectionFactory cf = new BSConnectionFactory();
		Connection connDomain = cf.getConnection(request);

		try {
			validatePassword(connDomain, newPassword, commitPassword);
		} catch (RuntimeException e) {
			cf.closeConnection(connDomain);
			throw e;
		}
		Long id = Long.parseLong(request.getParameter("cId"));

		Connection conn = cf.getConnection();

		BSBeanUtils bu = new BSBeanUtils();
		User user = new User();
		user.setId(id);
		bu.search(conn, user);
		String currentPasswordMD5 = user.getPassword();

		newPassword = md5(newPassword);
		if (currentPasswordMD5 != null && !reset) {
			// oldPassword =

			String oldPasswordMD5 = md5(oldPassword);

			if (!currentPasswordMD5.equals(oldPasswordMD5)) {
				throw new BSUserException("La clave actual no corresponde");
			}
			if (currentPasswordMD5.equals(newPassword)) {
				throw new BSUserException("La nueva clave es igual a la anterior");
			}
		}

		user.setPassword(newPassword);
		user.setLastChangePass(BSDateTimeUtil.calendar2Date(Calendar.getInstance()));
		bu.update(conn, user);
		cf.closeConnection(conn);

		eventLog.writeEntry(connDomain, getCurrentUser(request).getId(), "SECURITY_CH_PASS", "Cambio la password de %s",
				user.getMail());
		cf.closeConnection(connDomain);

		// String next = "/servlet/common/LoadTable";
		next = next == null ? "/servlet/Home" : next;
		// if (goHome != null && next == null) {
		// next = "/servlet/Home";
		// }

		response.sendRedirect(next);
		// forward(request, response, next);

	}

	private boolean getReset(HttpServletRequest request) {
		String reset = request.getParameter("Reset");
		Boolean out = null;
		if (reset == null) {
			out = false;
		} else {
			out = reset.equalsIgnoreCase("True");
		}
		return out;
	}

	private void validatePassword(Connection conn, String newPassword, String commitPassword) {
		if (!newPassword.equals(commitPassword)) {
			throw new BSUserException("Las claves no coinciden");
		}
		/**
		 * <code>
		 PASS_MIN_LEN
		 PASS_SPEC_CHR
		 PASS_UPPER_CHR
		 PASS_NUM_CHR
		 
		 </code>
		 */
		BSConfig c = new BSConfig();
		Integer minLen = c.getInteger(conn, PASS_MIN_LEN, Integer.valueOf(6));
		Integer specChar = c.getInteger(conn, PASS_SPEC_CHR, Integer.valueOf(0));
		Integer upperChar = c.getInteger(conn, PASS_UPPER_CHR, Integer.valueOf(0));
		Integer numberChar = c.getInteger(conn, PASS_NUM_CHR, Integer.valueOf(0));

		if (newPassword.length() < minLen) {
			throw new BSUserException("La password es demaciado corta, mínimo " + minLen + " caracter" + (minLen > 1 ? "es" : "")
					+ ".");
		}

		if (countInString(newPassword, NUMBERS) < numberChar) {
			throw new BSUserException("La password debe tener al menos " + numberChar + " número" + (numberChar > 1 ? "s" : "")
					+ ".");
		}
		if (countInString(newPassword, UPPER_LETTERS) < upperChar) {
			throw new BSUserException("La password debe tener al menos " + upperChar + " letra" + (upperChar > 1 ? "s" : "")
					+ " mayuscula" + (upperChar > 1 ? "s" : "") + ".");
		}

		Integer validCharsCount = countInString(newPassword, ALL_VALID_CHARS);
		if (newPassword.length() - validCharsCount < specChar) {
			throw new BSUserException("La password debe tener al menos " + specChar + " caracter" + (specChar > 1 ? "es" : "")
					+ " especial" + (upperChar > 1 ? "es" : "") + ".");
		}
	}

	private Integer countInString(String newPassword, String pattern) {
		Integer out = 0;
		for (char c : newPassword.toCharArray()) {
			out += pattern.indexOf(c) > -1 ? 1 : 0;
		}
		return out;
	}

	private String md5(String password) {
		BSSecurity security = new BSSecurity();
		return security.md5(password);
	}
}

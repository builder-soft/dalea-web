package cl.buildersoft.web.servlet.system.user;

import java.io.IOException;
import java.sql.Connection;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import cl.buildersoft.framework.beans.Domain;
import cl.buildersoft.framework.beans.DomainAttribute;
import cl.buildersoft.framework.business.services.EventLogService;
import cl.buildersoft.framework.business.services.ServiceFactory;
import cl.buildersoft.framework.util.BSConnectionFactory;
import cl.buildersoft.framework.util.BSHttpServlet;
import cl.buildersoft.web.servlet.login.ValidateLoginServlet;

@WebServlet("/servlet/system/user/ChangeDomain")
public class ChangeDomain extends BSHttpServlet {
	private static final long serialVersionUID = -5231577745476555171L;

	protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		Long id = Long.parseLong(request.getParameter("cId"));
		HttpSession session = request.getSession(false);
		Long userId = getCurrentUser(request).getId();
		BSConnectionFactory cf = new BSConnectionFactory();

		Connection connFramework = null;
		Connection connOldDomain = null;
		Connection connNewDomain = null;

		try {
			Domain oldDomain = getCurrentDomain(request);
			connFramework = cf.getConnection();
			connOldDomain = cf.getConnection(request);

			switchDomain(connFramework, session, id);

			Domain newDomain = getCurrentDomain(request);
			connNewDomain = cf.getConnection(request);

			EventLogService eventLog = ServiceFactory.createEventLogService();
			eventLog.writeEntry(connOldDomain, userId, "CHANGE_DOMAIN", "Saltó del dominio \"%s\" al \"%s\".",
					oldDomain.getName(), newDomain.getName());
			eventLog.writeEntry(connNewDomain, userId, "CHANGE_DOMAIN", "Saltó desde el dominio \"%s\".", oldDomain.getName());
		} finally {
			cf.closeConnection(connFramework);
			cf.closeConnection(connOldDomain);
			cf.closeConnection(connNewDomain);
		}
		forward(request, response, "/servlet/login/GetMenuServlet");
	}

	private void switchDomain(Connection conn, HttpSession session, Long id) {
		List<Domain> domains = (List<Domain>) session.getAttribute("Domains");
		for (Domain domain : domains) {
			if (domain.getId().equals(id)) {
				session.setAttribute("Domain", domain);
				session.setAttribute("DomainAttribute", getDomainAttribute(conn, domain));
				break;
			}
		}
	}

	private Map<String, DomainAttribute> getDomainAttribute(Connection conn, Domain defaultDomain) {
		ValidateLoginServlet validateLoginServlet = new ValidateLoginServlet();
		return validateLoginServlet.getDomainAttribute(conn, defaultDomain);
	}

}

<%@page import="cl.buildersoft.framework.web.servlet.BSHttpServlet_"%>
<%@page import="cl.buildersoft.framework.beans.User"%>
<%@page import="java.sql.Connection"%>
<%@page import="cl.buildersoft.framework.util.BSConnectionFactory"%>
<%@page
	import="cl.buildersoft.framework.business.services.ServiceFactory"%>
<%@page
	import="cl.buildersoft.framework.business.services.EventLogService"%>
<%
	EventLogService eventLog = ServiceFactory.createEventLogService();
	BSConnectionFactory cf = new BSConnectionFactory();
	Connection conn = null;
	try {
		conn = cf.getConnection(request);
		User user = (User) request.getSession(false).getAttribute("User");
		eventLog.writeEntry(conn, user.getId(), "SECURITY_LOGOUT", "Salida del sistema", null);

		BSHttpServlet_ hs = new BSHttpServlet_();
		hs.deleteSession(request, response);

	} catch (NullPointerException e) {
	} finally {
		cf.closeConnection(conn);
	}

	response.sendRedirect(getServletContext().getAttribute("TIMECTRL_CONTEXT").toString());
%>
package cl.buildersoft.web.servlet;

import java.io.IOException;
import java.sql.Connection;
import java.util.Calendar;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cl.buildersoft.framework.util.BSDataUtils;
import cl.buildersoft.framework.util.BSDateTimeUtil;
import cl.buildersoft.framework.web.servlet.BSHttpServlet_;

@WebServlet("/servlet/Home")
public class Home extends BSHttpServlet_ {
	private static final Logger LOG = Logger.getLogger(Home.class.getName());
	private static final String DATE_FORMAT = "DateFormat";
	private static final long serialVersionUID = -3485155081742992753L;

	protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		Connection conn = super.getConnection(request);
		Boolean bootstrap = bootstrap(conn);
		String page = bootstrap ? "/WEB-INF/jsp/home/index2.jsp" : "/WEB-INF/jsp/home/index.jsp";

		loadDateFormat(request);
		if (!bootstrap) {
			String currentVersion = getServletContext().getInitParameter("CurrentVersion");
//			String lastRead = getLastRead(getConnection(request));

			request.setAttribute("CurrentVersion", currentVersion);
//			request.setAttribute("LastRead", lastRead);
		}
		closeConnection(conn);
		forward(request, response, page, true);
	}

	private void loadDateFormat(HttpServletRequest request) {
		Object dateFormatObject = super.getApplicationValue(request, DATE_FORMAT);
		if (dateFormatObject == null) {
			super.setApplicationValue(request, DATE_FORMAT, BSDateTimeUtil.getFormatDate(request));
		}
	}

	private String getLastRead(Connection conn) {
		BSDataUtils du = new BSDataUtils();
		String sql = "SELECT MAX(cDate) AS LastRead FROM tAttendanceLog;";
		String out = "Sin lecturas";
		String lastRead = du.queryField(conn, sql, null);
		Calendar date = null;

		if (lastRead != null) {
			date = BSDateTimeUtil.string2Calendar(lastRead, "yyyy-MM-dd hh:mm:ss");
			LOG.log(Level.FINE, "LastRead: {0}", date);
			out = BSDateTimeUtil.calendar2String(date, "dd-MM-yyyy HH:mm:ss");
			LOG.log(Level.FINE, "Out: {0}", out);
		}
		return out;
	}
}

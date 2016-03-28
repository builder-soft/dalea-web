package cl.buildersoft.web.servlet.admin.eventViewer;

import java.io.IOException;
import java.sql.Connection;
import java.util.Calendar;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cl.buildersoft.framework.beans.EventBean;
import cl.buildersoft.framework.beans.EventType;
import cl.buildersoft.framework.beans.User;
import cl.buildersoft.framework.business.services.EventLogService;
import cl.buildersoft.framework.business.services.ServiceFactory;
import cl.buildersoft.framework.database.BSBeanUtils;
import cl.buildersoft.framework.services.BSUserService;
import cl.buildersoft.framework.services.impl.BSUserServiceImpl;
import cl.buildersoft.framework.util.BSConnectionFactory;
import cl.buildersoft.framework.util.BSDateTimeUtil;
import cl.buildersoft.framework.web.servlet.BSHttpServlet_;

/**
 * Servlet implementation class EventViewerMain
 */
@WebServlet("/servlet/admin/eventViewer/EventViewerMain")
public class EventViewerMain extends BSHttpServlet_ {
	private static final long serialVersionUID = "/servlet/admin/eventViewer/EventViewerMain".hashCode();

	protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		BSConnectionFactory cf = new BSConnectionFactory();
		Connection conn = cf.getConnection(request);
		Connection connBS = cf.getConnection();
		BSBeanUtils bu = new BSBeanUtils();
		List<EventBean> eventList = null;
		BSUserService us = new BSUserServiceImpl();

		Calendar startDate = getCalendarField(request, "StartDate");
		Calendar endDate = getCalendarField(request, "EndDate");
		Long eventType = getLongField(request, "EventType");
		Long userId = getLongField(request, "User");
		String dateFormat = BSDateTimeUtil.getFormatDate(request);

		Boolean allIsNull = (startDate == null && endDate == null && eventType == null && userId == null);
		if (!allIsNull) {
			eventList = executeQuery(conn, startDate, endDate, eventType, userId);
		}

		List<EventType> eventTypeList = (List<EventType>) bu.listAll(conn, new EventType() ,"cName" );
		List<User> userList = us.listUsersByDomain(getCurrentDomain(request));

		if (startDate == null) {
			startDate = Calendar.getInstance();
		}
		if (endDate == null) {
			endDate = Calendar.getInstance();
		}
		 

		request.setAttribute("EventTypeList", eventTypeList);
		request.setAttribute("UserList", userList);
		request.setAttribute("EventList", eventList);
		request.setAttribute("StartDate", startDate);
		request.setAttribute("EndDate", endDate);
		request.setAttribute("EventType", eventType);
		request.setAttribute("UserId", userId);
		request.setAttribute("DateFormat", dateFormat);

		cf.closeConnection(conn);
		cf.closeConnection(connBS);

		forward(request, response, "/WEB-INF/jsp/admin/event-viewer/event-viewer-main.jsp");
	}

	private List<EventBean> executeQuery(Connection conn, Calendar startDate, Calendar endDate, Long eventType, Long userId) {
		EventLogService eventService = ServiceFactory.createEventLogService();
		List<EventBean> out = null;

		if (eventType == null && userId == null) {
			out = eventService.list(conn, startDate, endDate);
		}

		if (eventType != null && userId != null) {
			out = eventService.list(conn, startDate, endDate, eventType, userId);
		}

		if (eventType != null && userId == null) {
			out = eventService.listByEventType(conn, startDate, endDate, eventType);
		}

		if (eventType == null && userId != null) {
			out = eventService.listByUser(conn, startDate, endDate, userId);
		}

		return out;
	}

	private Calendar getCalendarField(HttpServletRequest request, String fieldName) {
		Calendar out = null;
		String dateString = request.getParameter(fieldName);
		if (dateString != null) {
			if (BSDateTimeUtil.isValidDate(dateString, BSDateTimeUtil.getFormatDate(request))) {
				out = BSDateTimeUtil.string2Calendar(request, dateString);
			}
		}
		return out;
	}

	private Long getLongField(HttpServletRequest request, String fieldName) {
		Long out = null;
		String longAsString = request.getParameter(fieldName);
		if (longAsString != null) {
			try {
				out = Long.parseLong(longAsString);
			} catch (NumberFormatException e) {
				out = null;
			}
		}
		return out;
	}

}

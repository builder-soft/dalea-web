package cl.buildersoft.web.servlet.common.crud;

import java.io.IOException;
import java.sql.Connection;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import cl.buildersoft.framework.beans.Domain;
import cl.buildersoft.framework.beans.LogInfoBean;
import cl.buildersoft.framework.business.services.EventLogService;
import cl.buildersoft.framework.business.services.ServiceFactory;
import cl.buildersoft.framework.database.BSmySQL;
import cl.buildersoft.framework.type.Semaphore;
import cl.buildersoft.framework.util.crud.BSField;
import cl.buildersoft.framework.util.crud.BSTableConfig;
import cl.buildersoft.framework.web.servlet.BSHttpServlet_;

public abstract class HttpServletCRUD_WillBeDelete extends BSHttpServlet_ {
	private static final long serialVersionUID = 713819586332712332L;

	protected abstract BSTableConfig getBSTableConfig(HttpServletRequest request);

	public abstract Semaphore setSemaphore(Connection conn, Object[] values);

//	public abstract String getBusinessClass();
	// public abstract void writeEventLog(Connection conn, String action,
	// HttpServletRequest request, BSTableConfig table);
	public abstract void configEventLog(BSTableConfig table, Long userId);

	public HttpServletCRUD_WillBeDelete() {
		super();
	}

	protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		BSTableConfig table = getBSTableConfig(request);

		String uri = request.getRequestURI().substring(request.getContextPath().length());

		table.setUri(uri);

		HttpSession session = request.getSession(false);
		synchronized (session) {
			session.setAttribute("BSTable", table);
//			session.setAttribute("BusinessClass", getBusinessClass());
		}

		forward(request, response, "/servlet/common/LoadTable");

	}

	protected BSTableConfig initTable(HttpServletRequest request, String tableName) {
		return initTable(request, tableName, null);
	}

	protected BSTableConfig initTable(HttpServletRequest request, String tableName, HttpServletCRUD_WillBeDelete servlet) {
		return initTable(request, null, tableName, servlet);
	}

	protected BSTableConfig initTable(HttpServletRequest request, String database, String tableName, HttpServletCRUD_WillBeDelete servlet) {
		String databaseName = null;
		if (database == null) {
			Domain domain = (Domain) request.getSession(false).getAttribute("Domain");
			databaseName = domain.getDatabase();
		} else {
			databaseName = database;
		}

		BSTableConfig table = new BSTableConfig(databaseName, tableName);
		BSmySQL mysql = new BSmySQL();
		Connection conn = getConnection(request);
		table.configFields(conn, mysql);
		mysql.closeConnection(conn);

		if (servlet != null) {
			request.setAttribute("ServletManager", servlet);
		}
		return table;
	}

	protected void hideFields(BSTableConfig table, String... hideFields) {
		for (String fieldName : hideFields) {
			table.getField(fieldName).setShowInTable(false);
		}
	}
	
	
}

package cl.buildersoft.web.servlet.common.crud;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cl.buildersoft.framework.beans.LogInfoBean;
import cl.buildersoft.framework.business.services.EventLogService;
import cl.buildersoft.framework.business.services.ServiceFactory;
import cl.buildersoft.framework.database.BSmySQL;
import cl.buildersoft.framework.exception.BSDataBaseException;
import cl.buildersoft.framework.exception.BSProgrammerException;
import cl.buildersoft.framework.util.BSUtils;
import cl.buildersoft.framework.util.crud.BSField;
import cl.buildersoft.framework.util.crud.BSTableConfig;
import cl.buildersoft.framework.web.servlet.BSHttpServlet_;

public class AbstractServletUtil extends BSHttpServlet_ {
	private static final Logger LOG = Logger.getLogger(AbstractServletUtil.class.getName());
	private static final long serialVersionUID = -34792656017725168L;

	@Deprecated
	protected String getFieldsNamesWithCommas(BSField[] fields) {
		String out = "";
		if (fields.length == 0) {
			out = "*";
		} else {
			for (BSField field : fields) {
				out += field.getName() + ",";
			}
			out = out.substring(0, out.length() - 1);
		}
		return out;
	}

	protected String getCommas(BSField[] fields) {
		String out = "";

		for (BSField field : fields) {
			// for (int i = 0; i < fields.length; i++) {
			if (!field.isReadonly()) {
				out += "?,";
			}
		}
		out = out.substring(0, out.length() - 1);
		return out;
	}

	protected String unSplit(BSField[] tableFields, String s) {
		/**
		 * <code>
		String out = "";
		for (BSField f : tableFields) {
			if (!f.isReadonly()) {
				out += f.getName() + s;
			}
		}
		out = out.substring(0, out.length() - 1);
		return out;
		</code>
		 */
		return BSUtils.unSplitField(tableFields, s);
	}

	protected List<Object> array2List(Object... prms) {
		List<Object> out = new ArrayList<Object>();

		for (Object o : prms) {
			out.add(o);
		}

		return out;
	}

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		LOG.log(Level.WARNING, "Method doGet of class AbstractServletUtil is deprecated, will be delete.");
		this.doPost(request, response);
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String message = "Method doPost of class AbstractServletUtil is deprecated, will be delete.";
		LOG.log(Level.WARNING, message);
		throw new BSProgrammerException(message);

	}

	protected void fillTableWithRecord(Connection conn, BSTableConfig table, Long id) {
		String sql = getSQL4Search(table, table.getIdField().getName());
		BSmySQL mysql = new BSmySQL();

		ResultSet rs = mysql.queryResultSet(conn, sql, BSUtils.array2List(id));

		resultset2Table(rs, table);

	}

	private String getSQL4Search(BSTableConfig table, String idField) {
		BSField[] fields = table.getFields();
		String sql = "SELECT " + getFieldsNamesWithCommas(fields);
		sql += " FROM " + table.getDatabase() + "." + table.getTableOrViewName();
		sql += " WHERE " + idField + "=?";
		return sql;
	}

	private void resultset2Table(ResultSet rs, BSTableConfig table) {
		BSField[] fields = table.getFields();
		try {
			if (rs.next()) {
				for (BSField f : fields) {
					f.setValue(rs.getObject(f.getName()));
				}
			}
		} catch (SQLException e) {
			throw new BSDataBaseException(e);
		}
	}

	protected void writeEventLog(Connection conn, BSTableConfig table, String action, Long userId) {
		LogInfoBean logInfo = table.getLogInfo(action);
		if (logInfo != null) {
			EventLogService event = ServiceFactory.createEventLogService();

			BSField[] fields = table.getFields();
			String msg = getPercentageSigns(fields);

			event.writeEntry(conn, userId, logInfo.getEventKey(), logInfo.getMessage() + msg, getTableValues(fields));
		}
	}

	private String[] getTableValues(BSField[] fields) {
		String[] out = new String[fields.length];
		Integer i = 0;
		for (BSField f : fields) {
			out[i++] = (f == null || f.getValue()==null? "''" : f.getValue().toString());
		}
		return out;
	}

	private String getPercentageSigns(BSField[] fields) {
		String out = ": ";
		for (BSField f : fields) {
			out += f.getLabel() + ": %s, ";
		}
		out = out.substring(0, out.length() - 2);

		return out;
	}

}

package cl.buildersoft.web.servlet.common.crud;

import java.io.IOException;
import java.sql.Connection;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import cl.buildersoft.framework.database.BSmySQL;
import cl.buildersoft.framework.util.BSUtils;
import cl.buildersoft.framework.util.crud.BSTableConfig;

@WebServlet("/servlet/common/crud/DeleteRecords")
public class DeleteRecords extends AbstractServletUtil {
	private static final long serialVersionUID = -2340853411641380529L;

	protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession(false);
		BSTableConfig table = null;
		synchronized (session) {
			table = (BSTableConfig) session.getAttribute("BSTable");
		}
		BSmySQL mysql = new BSmySQL();
		Connection conn = getConnection(request);

		// String idField = table.getPKField(conn).getName();
		String idField = table.getIdField().getName();
		String[] values = request.getParameterValues(idField);
		Long userId = getCurrentUser(request).getId();

		if (table.getDeleteSP() != null) {
			for (String value : values) {
				Long id = Long.parseLong(value);

				fillTableWithRecord(conn, table, id);

				mysql.callSingleSP(conn, table.getDeleteSP(), id);

				// table.getField(idField).setValue(id);
				writeEventLog(conn, table, "DELETE", userId);
			}

		} else {
			String sql = getSQL4Delete(table, idField);

			for (String value : values) {
				Long id = Long.parseLong(value);

				fillTableWithRecord(conn, table, id);
				mysql.update(conn, sql, BSUtils.array2List(id));

				// table.getField(idField).setValue(id);
				writeEventLog(conn, table, "DELETE", userId);

			}
		}
		closeConnection(conn);

		forward(request, response, "/servlet/common/LoadTable");

	}

	/*
	 * private void fillTableWithRecord(Connection conn, BSTableConfig table,
	 * Long id) { String sql = getSQL4Search(table,
	 * table.getIdField().getName()); BSmySQL mysql = new BSmySQL();
	 * 
	 * ResultSet rs = mysql.queryResultSet(conn, sql, BSUtils.array2List(id));
	 * 
	 * resultset2Table(rs, table);
	 * 
	 * }
	 */

	private String getSQL4Delete(BSTableConfig table, String idField) {
		// BSField[] fields = table.getFields();
		String sql = "DELETE FROM " + table.getDatabase() + ".";
		sql += table.getTableName();
		sql += " WHERE " + idField + "=?";
		return sql;
	}

	/**
	 * <code>
	 
	 private String getSQL4Search(BSTableConfig table, String idField) {
		BSField[] fields = table.getFields();
		String sql = "SELECT " + getFieldsNamesWithCommas(fields);
		sql += " FROM " + table.getDatabase() + "." + table.getTableOrViewName();
		sql += " WHERE " + idField + "=?";
		return sql;
	}
	 
	private void writeEventLog(Connection conn, HttpServletRequest request, BSTableConfig table) {
		String businessClass = (String) request.getSession(false).getAttribute("BusinessClass");

		BSFactory factory = new BSFactory();
		HttpServletCRUD crudManager = (HttpServletCRUD) factory.getInstance(businessClass);

		crudManager.writeEventLog(conn, "DELETE", request, table);

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
</code>
	 */

}

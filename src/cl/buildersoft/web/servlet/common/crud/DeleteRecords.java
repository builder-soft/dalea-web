package cl.buildersoft.web.servlet.common.crud;

import java.io.IOException;
import java.sql.Connection;
import java.util.logging.Level;
import java.util.logging.Logger;

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
	private static final Logger LOG = Logger.getLogger(DeleteRecords.class.getName());
	private static final long serialVersionUID = -2340853411641380529L;

	protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession(false);
		BSTableConfig table = null;
		synchronized (session) {
			table = (BSTableConfig) session.getAttribute("BSTable");
		}
		BSmySQL mysql = new BSmySQL();
		Connection conn = getConnection(request);
		try {
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
		} catch (Exception e) {
			LOG.log(Level.SEVERE, e.getMessage(), e);

		} finally {
			closeConnection(conn);
		}
		forward(request, response, "/servlet/common/LoadTable");

	}

	private String getSQL4Delete(BSTableConfig table, String idField) {
		// BSField[] fields = table.getFields();
		String sql = "DELETE FROM " + table.getDatabase() + ".";
		sql += table.getTableName();
		sql += " WHERE " + idField + "=?";
		return sql;
	}

}

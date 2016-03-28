package cl.buildersoft.web.servlet.common.crud;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import cl.buildersoft.framework.database.BSmySQL;
import cl.buildersoft.framework.exception.BSDataBaseException;
import cl.buildersoft.framework.util.BSConnectionFactory;
import cl.buildersoft.framework.util.BSUtils;
import cl.buildersoft.framework.util.BSWeb;
import cl.buildersoft.framework.util.crud.BSField;
import cl.buildersoft.framework.util.crud.BSTableConfig;

@WebServlet("/servlet/common/InsertRecord")
public class InsertRecord extends AbstractServletUtil {
	private static final long serialVersionUID = 947236230190327847L;

	@Override
	protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession(false);
		BSTableConfig table = null;
		String businessClass = null;

		synchronized (session) {
			table = (BSTableConfig) session.getAttribute("BSTable");
			businessClass = (String) session.getAttribute("BusinessClass");
		}

		String saveSP = table.getSaveSP();

		BSmySQL mysql = new BSmySQL();
		BSConnectionFactory cf = new BSConnectionFactory();
		Connection conn = cf.getConnection(request);
		BSField[] fields = table.deleteId();

		fields = table.getNotReadonly(fields);

		try {
			Long id = null;
			if (saveSP == null) {
				String sql = getSQL(table, fields, request);
				List<Object> params = getValues4Insert(conn, request, fields);
				id = mysql.insert(conn, sql, params);
				request.setAttribute("cId", id);
			} else {
				fields = table.getFields();
				List<Object> params = getValues4Insert(conn, request, fields);
				ResultSet rs = mysql.callSingleSP(conn, saveSP, params);

				if (rs != null) {
					try {
						if (rs.next()) {
							id = rs.getLong(1);
							request.setAttribute("cId", id);
						}
					} catch (SQLException e) {
						throw new BSDataBaseException(e);
					}
				}
			}

			table.getIdField().setValue(id);

			// writeEventLog(conn, businessClass, request, table);
			writeEventLog(conn, table, "INSERT", getCurrentUser(request).getId());

		} finally {
			cf.closeConnection(conn);
		}

		forward(request, response, "/servlet/common/LoadTable");
	}

	private String getSQL(BSTableConfig table, BSField[] fields, HttpServletRequest request) {
		String sql = "INSERT INTO " + table.getDatabase() + "." + table.getTableName();
		sql += "(" + table.unSplitFieldNames(fields, ",") + ") ";
		sql += " VALUES (" + BSUtils.getCommas(fields) + ")";

		return sql;
	}

	private List<Object> getValues4Insert(Connection conn, HttpServletRequest request, BSField[] fields) {
		List<Object> out = new ArrayList<Object>();
		Object value = null;

		for (BSField field : fields) {
			if (!field.isReadonly()) {
				value = BSWeb.value2Object(conn, request, field, true);
				field.setValue(value);
				out.add(value);
			}
		}
		return out;
	}

}

package cl.buildersoft.web.servlet.common.crud;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import cl.buildersoft.framework.database.BSmySQL;
import cl.buildersoft.framework.exception.BSDataBaseException;
import cl.buildersoft.framework.util.BSConnectionFactory;
import cl.buildersoft.framework.util.BSUtils;
import cl.buildersoft.framework.util.crud.BSField;
import cl.buildersoft.framework.util.crud.BSTableConfig;
import cl.buildersoft.framework.web.servlet.BSHttpServlet_;

/**
 * Servlet implementation class EditRecord
 */
@WebServlet("/servlet/common/SearchRecord")
public class SearchRecord extends BSHttpServlet_ {

	private static final long serialVersionUID = -5785656616097922095L;

	public SearchRecord() {
		super();
	}

	protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession(false);
		BSTableConfig table = null;
		synchronized (session) {
			table = (BSTableConfig) session.getAttribute("BSTable");
		}

		String idField = table.getIdField().getName();
		String sql = getSQL4Search(table, idField);
		Long id = Long.parseLong(request.getParameter(idField));

		Connection conn = null;
		BSmySQL mysql = new BSmySQL();
		BSConnectionFactory cf = new BSConnectionFactory();

		conn = cf.getConnection(request);
		ResultSet rs = mysql.queryResultSet(conn, sql, BSUtils.array2List(id));
		resultset2Table(rs, table);
		mysql.closeSQL();
		mysql.closeSQL(rs);

		String page = bootstrap(conn) ? "/WEB-INF/jsp/crud/data-form2.jsp" : "/WEB-INF/jsp/table/data-form.jsp";

		cf.closeConnection(conn);

		request.setAttribute("Action", "Update");
		forward(request, response, page);
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

	private String getSQL4Search(BSTableConfig table, String idField) {
		BSField[] fields = table.getFields();
		String sql = "SELECT " + getFieldsNamesWithCommas(fields);
		sql += " FROM " + table.getDatabase() + "." + table.getTableOrViewName();
		sql += " WHERE " + idField + "=?";
		return sql;
	}

	private String getFieldsNamesWithCommas(BSField[] fields) {
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

}

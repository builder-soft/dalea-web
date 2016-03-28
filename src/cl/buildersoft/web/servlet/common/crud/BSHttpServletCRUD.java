package cl.buildersoft.web.servlet.common.crud;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

import cl.buildersoft.framework.database.BSmySQL;
import cl.buildersoft.framework.exception.BSDataBaseException;
import cl.buildersoft.framework.util.BSUtils;
import cl.buildersoft.framework.util.crud.BSField;
import cl.buildersoft.framework.util.crud.BSTableConfig;
import cl.buildersoft.framework.web.servlet.BSHttpServlet_;

public class BSHttpServletCRUD extends BSHttpServlet_ {
	private static final long serialVersionUID = BSHttpServletCRUD.class.getName().hashCode();

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

}

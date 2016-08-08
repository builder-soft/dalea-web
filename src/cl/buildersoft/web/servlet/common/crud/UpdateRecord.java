package cl.buildersoft.web.servlet.common.crud;

import java.io.IOException;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import cl.buildersoft.framework.database.BSmySQL;
import cl.buildersoft.framework.exception.BSProgrammerException;
import cl.buildersoft.framework.util.BSConnectionFactory;
import cl.buildersoft.framework.util.BSUtils;
import cl.buildersoft.framework.util.BSWeb;
import cl.buildersoft.framework.util.crud.BSField;
import cl.buildersoft.framework.util.crud.BSTableConfig;

@WebServlet("/servlet/common/UpdateRecord")
public class UpdateRecord extends AbstractServletUtil {
	private static final Logger LOG = LogManager.getLogger(UpdateRecord.class);
	private static final long serialVersionUID = 729493572423196326L;

	public UpdateRecord() {
		super();
	}

	protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession(false);
		BSTableConfig table = null;

		synchronized (session) {
			table = (BSTableConfig) session.getAttribute("BSTable");
		}

		// BSField[] fields = table.getFields();
		BSField idField = table.getIdField();
		BSField[] fields = new BSField[0];
		BSField[] fieldsWidthoutId = table.deleteId();
		fieldsWidthoutId = table.getNotReadonly(fieldsWidthoutId);

		Integer index = 0;

		Integer len = showInFormCount(fieldsWidthoutId);
		Integer j = 0;
		fields = new BSField[len];

		for (index = 0; index < fieldsWidthoutId.length; index++) {
			if (fieldsWidthoutId[index].getShowInForm()) {
				System.arraycopy(fieldsWidthoutId, index, fields, j++, 1);
			}
		}

		String sql = getSQL(table, fields, idField);

		List<Object> params = null;

		Connection conn = null;
		BSmySQL mysql = new BSmySQL();
		BSConnectionFactory cf = new BSConnectionFactory();
		try {
			conn = cf.getConnection(request);
			params = getParams(conn, request, fields, idField);

			fillTableWithRecord(conn, table, idField.getValueAsLong());
			writeEventLog(conn, table, "UPDATE", getCurrentUser(request).getId());

			mysql.update(conn, sql, params);
		} catch (Exception e) {
			LOG.error(e);
			throw new BSProgrammerException(e);
		} finally {
			cf.closeConnection(conn);
		}
		forward(request, response, "/servlet/common/LoadTable");
	}

	private Integer showInFormCount(BSField[] fields) {
		Integer out = 0;
		for (BSField field : fields) {
			out += (field.getShowInForm() ? 1 : 0);
		}

		return out;
	}

	private List<Object> getParams(Connection conn, HttpServletRequest request, BSField[] fieldsWidthoutId, BSField idField) {
		List<Object> out = new ArrayList<Object>();

		for (BSField field : fieldsWidthoutId) {
			if (!field.isReadonly() && field.getShowInForm()) {
				LOG.trace(String.format("Processing field %s", field));
				out.add(BSWeb.value2Object(conn, request, field, true));
			}
		}
		Object idValue = BSWeb.value2Object(conn, request, idField, true);
		idField.setValue(idValue);		
		out.add(idValue);

		return out;
	}

	private String getSQL(BSTableConfig table, BSField[] fieldsWidthoutId, BSField idField) {
		String sql = "UPDATE " + table.getDatabase() + "." + table.getTableName();
		sql += " SET " + BSUtils.unSplitField(fieldsWidthoutId, "=?,");
		sql += " WHERE " + idField.getName() + "=?";

		return sql;
	}

}

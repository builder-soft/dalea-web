package cl.buildersoft.web.servlet.common.crud;

import java.io.IOException;
import java.sql.Connection;
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
import cl.buildersoft.framework.util.crud.BSAction;
import cl.buildersoft.framework.util.crud.BSTableConfig;
import cl.buildersoft.framework.web.servlet.BSHttpServlet_;

/**
 * Servlet implementation class SaveRelation
 */
@WebServlet("/servlet/common/SaveRelation")
public class SaveRelation extends BSHttpServlet_ {
	private static final long serialVersionUID = -6105983061875822914L;

	public SaveRelation() {
		super();

	}

	@Override
	protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		Long id = Long.parseLong(request.getParameter("cId"));
		String[] relations = request.getParameterValues("Relation");

		HttpSession session = request.getSession(false);
		BSTableConfig table = null;
		synchronized (session) {
			table = (BSTableConfig) session.getAttribute("BSTable");
		}

		BSmySQL mysql = new BSmySQL();
		BSConnectionFactory cf = new BSConnectionFactory();
		Connection conn = cf.getConnection(request);

		BSAction action = table.getAction(request.getParameter("CodeAction"));

		try {
			mysql.setAutoCommit(conn, false);

			removeRelation(conn, mysql, id, table, action);
			setRelation(conn, mysql, id, relations, action);
			mysql.commit(conn);

		} catch (Exception e) {
			mysql.rollback(conn);
			throw new BSDataBaseException(e);
		} finally {
			mysql.closeSQL();
			cf.closeConnection(conn);
		}
		String uri = table.getUri();
		forward(request, response, uri);
		// request.getRequestDispatcher(uri).forward(request, response);

	}

	private void setRelation(Connection conn, BSmySQL mysql, Long id, String[] relations, BSAction action) {
		String[] natInfo = action.getNatTable();
		List<Object> prms = new ArrayList<Object>();
		String sql = "INSERT INTO " + natInfo[0] + "." + natInfo[1] + " VALUES(?,?)";

		prms.add(id);

		if (relations != null) {
			for (String relation : relations) {
				prms.add(Long.parseLong(relation));

				mysql.update(conn, sql, prms);
				prms.remove(1);
			}
		}
	}

	private void removeRelation(Connection conn, BSmySQL mysql, Long id, BSTableConfig table, BSAction action) {
		String[] natInfo = action.getNatTable();
		String tableName = natInfo[0] + "." + natInfo[1];

		List<Object> prms = new ArrayList<Object>();
		String sql = "DELETE FROM " + tableName + " WHERE " + table2Field(table.getTableName()) + "=?";

		prms.add(id);

		mysql.update(conn, sql, prms);
	}

	private String table2Field(String tableName) {
		return "c" + tableName.substring(1);
	}
}

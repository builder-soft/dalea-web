package cl.buildersoft.web.servlet.common.crud;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import cl.buildersoft.framework.database.BSmySQL;
import cl.buildersoft.framework.util.crud.BSAction;
import cl.buildersoft.framework.util.crud.BSTableConfig;
import cl.buildersoft.framework.web.servlet.BSHttpServlet_;

/**
 * Servlet implementation class NatTable
 */
@WebServlet("/servlet/common/NatTable")
public class NatTable extends BSHttpServlet_ {
	private static final Logger LOG = Logger.getLogger(NatTable.class.getName());
	private static final long serialVersionUID = 8135006580684560874L;

	public NatTable() {
		super();

	}

	protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = null;
		synchronized (request) {
			session = request.getSession(false);
		}

		String code = request.getParameter("CodeAction");
		Long id = Long.parseLong(request.getParameter("cId"));

		BSTableConfig table = (BSTableConfig) session.getAttribute("BSTable");
		BSAction action = table.getAction(code);

		String sql = getSQL(action, table);

		BSmySQL mysql = new BSmySQL();
		Connection conn = getConnection(request);

		ResultSet relation = getRelation(id, sql, mysql, conn);
		request.setAttribute("Relation", relation);

		ResultSet list = getList(action, mysql, conn);

		request.setAttribute("List", list);
		request.setAttribute("Conn", conn);

		String page = null;
		if (bootstrap(conn)) {
			page = "/WEB-INF/jsp/crud/relation-nat2.jsp";
		} else {
			page = "/WEB-INF/jsp/table/relation-nat.jsp";
		}

		forward(request, response, page);
		 
	}

	private ResultSet getList(BSAction action, BSmySQL mysql, Connection conn) {
		String sql;
		String[] natInfo = action.getNatTable();
		sql = "SELECT * FROM " + natInfo[2] + "." + natInfo[3]; // action.getNatTable()[1];
		ResultSet list = mysql.queryResultSet(conn, sql, null);
		return list;
	}

	private ResultSet getRelation(Long id, String sql, BSmySQL mysql, Connection conn) {
		List<Object> prms = new ArrayList<Object>();
		prms.add(id);
		ResultSet relation = mysql.queryResultSet(conn, sql, prms);
		return relation;
	}

	private String getSQL(BSAction action, BSTableConfig table) {
		/**
		 * <code>
SELECT * FROM tUser AS a
LEFT JOIN tR_UserRol AS b ON a.cId = b.cUser
LEFT JOIN tRol AS c ON b.cRol = c.cId; 
		  </code>
		 */
		String natInfo[] = action.getNatTable();
		String sql = "SELECT c.* FROM " + table.getDatabase() + "." + table.getTableName() + " AS a ";
		sql += "LEFT JOIN " + natInfo[0] + "." + natInfo[1] + " AS b ON a.cId = b." + table2Field(table.getTableName()) + " ";
		sql += "LEFT JOIN " + natInfo[2] + "." + natInfo[3] + " AS c ON b." + table2Field(natInfo[3]) + " = c.cId ";
		sql += "WHERE a.cId=? AND c.cId IS NOT NULL";
		LOG.logp(Level.FINE, this.getClass().getName(), "getSQL", "SQL: {0}", sql);

		return sql;
	}

	private String table2Field(String tableName) {
		return "c" + tableName.substring(1);
	}

}

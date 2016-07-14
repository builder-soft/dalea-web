package cl.buildersoft.web.servlet.system.roledef;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import cl.buildersoft.framework.beans.DomainAttribute;
import cl.buildersoft.framework.beans.Menu;
import cl.buildersoft.framework.beans.Rol;
import cl.buildersoft.framework.database.BSBeanUtils;
import cl.buildersoft.framework.database.BSmySQL;
import cl.buildersoft.framework.services.BSMenuService;
import cl.buildersoft.framework.services.impl.BSMenuServiceImpl;
import cl.buildersoft.framework.util.BSConnectionFactory;
import cl.buildersoft.framework.web.servlet.BSHttpServlet_;

/**
 * Servlet implementation class RoleDef
 */
@WebServlet("/servlet/system/roleDef/RoleDef")
public class RoleDef extends BSHttpServlet_ {
	private static final Logger LOG = LogManager.getLogger(RoleDef.class);
	private static final long serialVersionUID = 111140893680994718L;

	public RoleDef() {
		super();
	}

	protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		BSmySQL mysql = new BSmySQL();
		BSConnectionFactory cf = new BSConnectionFactory();
		Connection conn = cf.getConnection(request);
		HttpSession session = request.getSession(false);

		String sql = "SELECT cId, cName FROM tRol";
		ResultSet rolsResultSet = mysql.queryResultSet(conn, sql, null);
		List<Object[]> rolsArray = mysql.resultSet2Matrix(rolsResultSet);

		Long idRolSelected = getRolId(request, rolsArray);
		List<Rol> rols = getRol(conn, idRolSelected);

		BSMenuService menuService = new BSMenuServiceImpl();

		@SuppressWarnings("unchecked")
		Menu fullMenu = menuService.getMenu(conn, (Map<String, DomainAttribute>) session.getAttribute("DomainAttribute"),
				getCurrentUser(request).getAdmin(), null);
		@SuppressWarnings("unchecked")
		Menu rolMenu = menuService.getMenu(conn, (Map<String, DomainAttribute>) session.getAttribute("DomainAttribute"),
				getCurrentUser(request).getAdmin(), rols);

		Boolean bootstrap = bootstrap(conn);
		bootstrap =false;
		
		String page = bootstrap  ? "/WEB-INF/jsp/system/role-def/role-def2.jsp"
				: "/WEB-INF/jsp/system/role-def/role-def.jsp";
		cf.closeConnection(conn);

		LOG.trace(String.format("RolMenu: %s", rolMenu.list().toString()));

		request.setAttribute("Rols", rolsArray);
		request.setAttribute("FullMenu", fullMenu);
		request.setAttribute("RolMenu", rolMenu);
		request.setAttribute("cId", idRolSelected);

		forward(request, response, page);
		// request.getRequestDispatcher("/WEB-INF/jsp/system/role-def/role-def.jsp").forward(request,
		// response);

	}

	private Long getRolId(HttpServletRequest request, List<Object[]> rolsArray) {
		Long idRolLong;

		String idRolString = readFromParameterOrAttribute(request, "cId");

		if (idRolString == null) {
			Object[] row1 = (Object[]) rolsArray.get(0);
			idRolLong = (Long) row1[0];
		} else {
			idRolLong = Long.parseLong(idRolString);
		}
		return idRolLong;
	}

	private String readFromParameterOrAttribute(HttpServletRequest request, String fieldName) {
		String out = null;

		String fieldParameter = request.getParameter(fieldName);
		if (fieldParameter == null) {
			String fieldAttribute = (String) request.getAttribute(fieldName);
			if (fieldAttribute != null) {
				out = fieldAttribute;
			}
		} else {
			out = fieldParameter;
		}
		return out;
	}

	private List<Rol> getRol(Connection conn, Long id) {
		List<Rol> out = new ArrayList<Rol>();
		Rol rol = new Rol();
		BSBeanUtils bu = new BSBeanUtils();
		rol.setId(id);
		bu.search(conn, rol);

		out.add(rol);

		return out;
	}
	/**
	 * <code>
	private List<String[]> resultSet2Matrix(ResultSet rs) {
		List<String[]> out = new ArrayList<String[]>();

		try {
			Integer i = 0;
			ResultSetMetaData metaData = rs.getMetaData();
			Integer colCount = metaData.getColumnCount();
			String[] colNames = new String[colCount];
			for (i = 1; i <= colCount; i++) {
				colNames[i - 1] = metaData.getColumnName(i);
			}

			String[] innerArray = null;
			while (rs.next()) {
				i = 0;
				innerArray = new String[colCount];
				for (String colName : colNames) {
					innerArray[i] = rs.getString(colName);
					i++;
				}
				out.add(innerArray);
			}
			rs.close();
		} catch (Exception e) {
			throw new BSDataBaseException("0300", e.getMessage());
		}

		return out;
	}</code>
	 */

}

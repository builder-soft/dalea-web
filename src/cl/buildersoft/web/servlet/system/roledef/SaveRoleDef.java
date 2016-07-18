package cl.buildersoft.web.servlet.system.roledef;

import java.io.IOException;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import cl.buildersoft.framework.beans.Rol;
import cl.buildersoft.framework.database.BSmySQL;
import cl.buildersoft.framework.util.BSConnectionFactory;
import cl.buildersoft.framework.web.servlet.BSHttpServlet_;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@WebServlet("/servlet/system/roledef/SaveRoleDef")
public class SaveRoleDef extends BSHttpServlet_ {
	private static final Logger LOG = LogManager.getLogger(SaveRoleDef.class);
	private static final long serialVersionUID = -2938011475821935220L;

	public SaveRoleDef() {
		super();

	}

	protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String[] options = request.getParameterValues("Option");
		Long rol = Long.parseLong(request.getParameter("Rol"));

		BSmySQL mysql = new BSmySQL();
		BSConnectionFactory cf = new BSConnectionFactory();
		Connection conn = cf.getConnection(request);
		try {
			mysql.setAutoCommit(conn, false);

			deleteRolDef(conn, mysql, rol);
			if (options != null) {
				saveNewDef(conn, mysql, options, rol);
			}

			conn.commit();
		} catch (Exception e) {
			mysql.rollback(conn);
			throw new ServletException(e);
		} finally {
			mysql.closeSQL();
			cf.closeConnection(conn);
		}

		String nextServlet = "/servlet/system/roleDef/RoleDef";
		request.setAttribute("cId", "" + rol);
		request.setAttribute("NextServlet", "/servlet/system/roleDef/RoleDef");

		HttpSession session = request.getSession(false);
		List<Rol> rols = (List<Rol>) session.getAttribute("Rol");
		for (Rol rolUser : rols) {
			if (rolUser.getId().equals(rol)) {
				nextServlet = "/servlet/login/GetMenuServlet";
				break;
			}
		}

		forward(request, response, nextServlet);

	}

	private void saveNewDef(Connection conn, BSmySQL mysql, String[] options, Long rol) {
		String sql = "INSERT INTO tR_RolOption(cRol, cOption) VALUES(?,?);";
		List<Object> prms = new ArrayList<Object>();
		prms.add(rol);

		for (String option : options) {
			prms.add(Long.parseLong(option));
			mysql.update(conn, sql, prms);
			// printSQL(sql, prms);
			LOG.trace(String.format(sql.replaceAll("[?]", "%s"), rol, option));
			prms.remove(1);
		}
	}

	private void deleteRolDef(Connection conn, BSmySQL mysql, Long rol) {
		String sql = "DELETE FROM tR_RolOption WHERE cRol=?";
		List<Object> prms = new ArrayList<Object>();
		prms.add(rol);
		mysql.update(conn, sql, prms);
	}
}

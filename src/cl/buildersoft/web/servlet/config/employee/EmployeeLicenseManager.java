package cl.buildersoft.web.servlet.config.employee;

import java.sql.Connection;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;

import cl.buildersoft.framework.database.BSmySQL;
import cl.buildersoft.framework.type.Semaphore;
import cl.buildersoft.framework.util.BSConfig;
import cl.buildersoft.framework.util.BSConnectionFactory;
import cl.buildersoft.framework.util.crud.BSAction;
import cl.buildersoft.framework.util.crud.BSActionType;
import cl.buildersoft.framework.util.crud.BSHttpServletCRUD;
import cl.buildersoft.framework.util.crud.BSTableConfig;

/**
 * Servlet implementation class EmployeeManager
 */
@WebServlet("/servlet/config/employee/EmployeeLicenseManager")
public class EmployeeLicenseManager extends BSHttpServletCRUD {
	private static final Logger LOG = Logger.getLogger(EmployeeLicenseManager.class.getName());
	private static final long serialVersionUID = -7665593692157885850L;

	@Override
	protected BSTableConfig getBSTableConfig(HttpServletRequest request) {
		BSTableConfig table = initTable(request, "tEmployee", this);
		table.setSortField(getSortField(request));

		table.setTitle("Licencias de empleados");

		table.getField("cName").setLabel("Nombre");
		table.getField("cPost").setLabel("Cargo");
		table.getField("cArea").setLabel("Area");
		table.getField("cGroup").setLabel("Grupo");
		table.getField("cBoss").setLabel("Superior");
		table.getField("cPrivilege").setLabel("Tipo de usuario");
		// table.getField("cEnabled").setLabel("Activado");
		table.getField("cUsername").setLabel("Nombre Usuario");
		table.getField("cMail").setLabel("Correo electrónico");

		this.hideFields(table, "cMail", "cArea", "cPrivilege", "cBirthDate", "cAddress", "cComuna", "cCountry", "cGenere",
				"cPhone", "cMaritalStatus");
		table.removeField("cEnabled");

		table.setWhere("cEnabled=TRUE");

		table.removeAction("INSERT");
		table.removeAction("EDIT");
		table.removeAction("DELETE");

		BSAction action = new BSAction("LICENSE", BSActionType.Record);
		action.setLabel("Licencias médicas o permisos");
		action.setContext("TIMECTRL_CONTEXT");
		action.setUrl("/servlet/timectrl/employee/LicenseOfEmployee");
		table.addAction(action);

		action = new BSAction("LOAD_LICENSE", BSActionType.Table);
		action.setLabel("Archivo Licencias");
		action.setContext("TIMECTRL_CONTEXT");
		action.setUrl("/servlet/timectrl/employeeLicensing/LoadLicensing");
		table.addAction(action);

		return table;
	}

	private String getSortField(HttpServletRequest request) {
		BSConnectionFactory cf = new BSConnectionFactory();
		BSConfig config = new BSConfig();

		Connection conn = cf.getConnection(request);
		String out = config.getString(conn, "EMPLOYEE_ORDER");
		cf.closeConnection(conn);
		return out;
	}

	@Override
	public Semaphore setSemaphore(Connection conn, Object[] values) {
		LOG.entering(EmployeeLicenseManager.class.getName(), "setSemaphore", values);
		Semaphore out = null;
		Long startTime = null;
		if (values != null) {
			BSmySQL mysql = new BSmySQL();

			String sql = "SELECT count(cId) FROM tR_EmployeeTurn WHERE cEmployee=?";
			startTime = System.currentTimeMillis();
			Integer cant = Integer.parseInt(mysql.queryField(conn, sql, values[0]));
			LOG.log(Level.FINE, "Load semaphore in {0}mm", System.currentTimeMillis() - startTime);
			mysql.closeSQL();
			if (cant == 0) {
				out = Semaphore.RED;
			} else {
				out = Semaphore.GREEN;
			}
		}
		LOG.exiting(EmployeeLicenseManager.class.getName(), "setSemaphore", out);
		return out;
	}

	@Override
	protected void configEventLog(BSTableConfig table, Long userId) {
		// TODO Configurar evento

	}

	
}

package cl.buildersoft.web.servlet.config.employee;
        
import java.sql.Connection;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;

import cl.buildersoft.framework.beans.LogInfoBean;
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
@WebServlet("/servlet/config/employee/EmployeeDetachedManager")
public class EmployeeDetachedManager extends BSHttpServletCRUD {
	private static final Logger LOG = Logger.getLogger(EmployeeDetachedManager.class.getName());
	private static final long serialVersionUID = -7665593692157885850L;

	@Override
	protected BSTableConfig getBSTableConfig(HttpServletRequest request) {
		BSTableConfig table = initTable(request, "tEmployee", this);
		table.setSortField(getSortField(request));

		table.setTitle("Mantención de empleados");

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

		table.setDeleteSP("pIncorporateEmployee");

		table.removeAction("INSERT");
		table.removeAction("EDIT");
		BSAction action = table.getAction("DELETE");
		action.setLabel("Re-incorporar");
		action.setWarningMessage("Esta seguro de reincorporar estos empleados?");

		action = new BSAction("PURGE", BSActionType.Record);
		action.setLabel("Borrado definitivo");
		action.setWarningMessage("¿Esta seguro de eliminar este empleado? Los datos asociados a él se perderán");
		action.setUrl("/servlet/timectrl/employee/PurgeEmployee");
//		table.addAction(action);

		configEventLog(table, getCurrentUser(request).getId());

		table.setWhere("cEnabled=FALSE");

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
		LOG.entering(EmployeeDetachedManager.class.getName(), "setSemaphore", values);
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
		LOG.exiting(EmployeeDetachedManager.class.getName(), "setSemaphore", out);
		return out;
	}

	/**
	 * <code>
	 * 
	 * @Override public void writeEventLog(Connection conn, String action,
	 *           HttpServletRequest request, BSTableConfig table) {
	 *           LOG.log(Level.FINE, "Action={0}", action); EventLogService
	 *           eventLog = ServiceFactory.createEventLogService(); if
	 *           ("DELETE".equals(action)) { eventLog.writeEntry(conn,
	 *           getCurrentUser(request).getId(), "INCORPORATE_EMPL",
	 *           "Reincorpora al empleado '%s'(%s, Id=%s).",
	 *           table.getField("cName").getValue(), table.getField("cRut")
	 *           .getValue(), table.getField("cId").getValue()); } } </code>
	 */
	@Override
	protected void configEventLog(BSTableConfig table, Long userId) {
		LogInfoBean li = new LogInfoBean();
		li.setAction("DELETE");
		li.setEventKey("INCORPORATE_EMPL");
		li.setMessage("Reincorpora al empleado");
		li.setUserId(userId);
		table.addLogInfo(li);

	}

	

}

package cl.buildersoft.web.servlet.config.employee;

import java.sql.Connection;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;

import cl.buildersoft.framework.beans.BSBean;
import cl.buildersoft.framework.beans.LogInfoBean;
import cl.buildersoft.framework.database.BSBeanUtils;
import cl.buildersoft.framework.database.BSmySQL;
import cl.buildersoft.framework.type.Semaphore;
import cl.buildersoft.framework.util.BSConfig;
import cl.buildersoft.framework.util.BSConnectionFactory;
import cl.buildersoft.framework.util.crud.BSAction;
import cl.buildersoft.framework.util.crud.BSActionType;
import cl.buildersoft.framework.util.crud.BSField;
import cl.buildersoft.framework.util.crud.BSHttpServletCRUD;
import cl.buildersoft.framework.util.crud.BSTableConfig;
import cl.buildersoft.timectrl.business.beans.Area;
import cl.buildersoft.timectrl.business.beans.Employee;
import cl.buildersoft.timectrl.business.beans.Group;
import cl.buildersoft.timectrl.business.beans.Post;
import cl.buildersoft.timectrl.business.beans.Privilege;

/**
 * Servlet implementation class EmployeeManager
 */
@WebServlet("/servlet/config/employee/EmployeeManager")
public class EmployeeManager extends BSHttpServletCRUD {
	private static final Logger LOG = Logger.getLogger(EmployeeManager.class.getName());
	private static final long serialVersionUID = -7665593692157885850L;

	@Override
	protected BSTableConfig getBSTableConfig(HttpServletRequest request) {
		BSTableConfig table = initTable(request, "tEmployee", this);
		table.setSortField(getSortField(request));

		table.setTitle("Datos básicos de empleados");

		table.getField("cName").setLabel("Nombre");
		table.getField("cPost").setLabel("Cargo");
		table.getField("cArea").setLabel("Area");
		table.getField("cGroup").setLabel("Grupo");
		table.getField("cBoss").setLabel("Superior");
		table.getField("cPrivilege").setLabel("Tipo de usuario");
		// table.getField("cEnabled").setLabel("Activado");
		table.getField("cUsername").setLabel("Usuario");
		table.getField("cMail").setLabel("Correo electrónico");
		table.getField("cBirthDate").setLabel("Fecha de nacimiento");
		table.getField("cCountry").setLabel("Nacionalidad");
		table.getField("cPhone").setLabel("Teléfono");
		table.getField("cMaritalStatus").setLabel("Estado Civil");

		this.hideFields(table, "cMail", "cArea", "cPrivilege", "cBirthDate", "cAddress", "cComuna", "cCountry", "cGenere",
				"cPhone", "cMaritalStatus");
		
		table.removeField("cEnabled");

		table.setDeleteSP("pDeleteEmployee");
		table.setWhere("cEnabled=TRUE");

		BSAction action = new BSAction("TURNS", BSActionType.Record);
		action.setLabel("Asignación de Turnos");
		action.setUrl("/servlet/timectrl/employee/TurnsOfEmployee");
		// table.addAction(action);

		action = new BSAction("LICENSE", BSActionType.Record);
		action.setLabel("Licencias médicas o permisos");
		action.setUrl("/servlet/timectrl/employee/LicenseOfEmployee");
		// table.addAction(action);

		action = new BSAction("MARK_MODIFY", BSActionType.Record);
		action.setLabel("Administración de Marcas");
		action.setUrl("/servlet/timectrl/employee/MarkAdmin");
		// table.addAction(action);

		action = new BSAction("LOAD_LICENSE", BSActionType.Table);
		action.setLabel("Archivo Licencias");
		action.setUrl("/servlet/timectrl/employeeLicensing/LoadLicensing");
		// table.addAction(action);

		table.getAction("DELETE").setLabel("Desvincular");

		configEventLog(table, getCurrentUser(request).getId());

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
		LOG.entering(EmployeeManager.class.getName(), "setSemaphore", values);
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
		LOG.exiting(EmployeeManager.class.getName(), "setSemaphore", out);
		return out;
	}

	/**
	 * <code>
	@ Override
	public void writeEventLog(Connection conn, String action, HttpServletRequest request, BSTableConfig table) {
		EventLogService eventLog = ServiceFactory.createEventLogService();
		if ("INSERT".equalsIgnoreCase(action)) {
			eventLog.writeEntry(conn, getCurrentUser(request).getId(), "EMPLOYEE_INSERT",
					"Se crea el empleado \"%s\", con Rut %s.", table.getField("cName").getValue(), table.getField("cRut")
							.getValue());

		}
		if ("UPDATE".equalsIgnoreCase(action)) {
			Object[] params = getParams(conn, table);
			eventLog.writeEntry(
					conn,
					getCurrentUser(request).getId(),
					"EMPLOYEE_UPDATE",
					"Se modifica el empleado \"%s\", sus datos fueron:\n- Id: %s\n- Key: %s\n- Rut: %s\n- Cargo: %s\n- Area: %s\n- Tipo usuario: %s\n- Grupo: %s\n- Username: %s\n- Mail: %s\n- Jefatura: %s.",
					params);

		}

		if ("DELETE".equalsIgnoreCase(action)) {
			eventLog.writeEntry(conn, getCurrentUser(request).getId(), "EMPLOYEE_DELETE",
					"Se desvincula el empleado \"%s\", Rut: %s. (Id=%s)", table.getField("cName").getValue(),
					table.getField("cRut").getValue(), table.getField("cId").getValue());

		}

	}
		</code>
	 */

	@Override
	protected void configEventLog(BSTableConfig table, Long userId) {
		LogInfoBean li = new LogInfoBean();
		li.setAction("INSERT");
		li.setEventKey("EMPLOYEE_INSERT");
		li.setMessage("Se crea el empleado");
		li.setUserId(userId);
		table.addLogInfo(li);

		li = new LogInfoBean();
		li.setAction("UPDATE");
		li.setEventKey("EMPLOYEE_UPDATE");
		li.setMessage("Modificacion de empleado");
		li.setUserId(userId);
		table.addLogInfo(li);

		li = new LogInfoBean();
		li.setAction("DELETE");
		li.setEventKey("EMPLOYEE_DELETE");
		li.setMessage("Desvinculacion de empleado");
		li.setUserId(userId);
		table.addLogInfo(li);

	}

	private Object[] getParams(Connection conn, BSTableConfig table) {
		Object[] out = new Object[11];
		out[0] = table.getField("cName").getValue();
		out[1] = table.getField("cId").getValue();
		out[2] = table.getField("cKey").getValue();
		out[3] = table.getField("cRut").getValue();
		out[4] = getPost(conn, table.getField("cPost")); // Cargo
		out[5] = getArea(conn, table.getField("cArea"));// Area
		out[6] = getPrivilege(conn, table.getField("cPrivilege")); // Tipo
																	// Usuario
		out[7] = getGroup(conn, table.getField("cArea")); // Grupo
		out[8] = table.getField("cUsername").getValue();
		out[9] = table.getField("cMail").getValue();
		out[10] = getBoss(conn, table.getField("cArea")); // Superior
		return out;
	}

	private Object getArea(Connection conn, BSField field) {
		Area area = new Area();
		getNameFromEntity(conn, field, area);
		return area.getId() == null ? "Ninguno" : area.getName();
	}

	private Object getGroup(Connection conn, BSField field) {
		Group group = new Group();
		getNameFromEntity(conn, field, group);
		return group.getId() == null ? "Ninguno" : group.getName();
	}

	private String getPost(Connection conn, BSField field) {
		Post post = new Post();
		getNameFromEntity(conn, field, post);
		return post.getId() == null ? "Ninguno" : post.getName();
	}

	private void getNameFromEntity(Connection conn, BSField field, BSBean anyObject) {

		if (field.getValue() != null) {
			BSBeanUtils bu = new BSBeanUtils();
			anyObject.setId(field.getValueAsLong());
			bu.search(conn, anyObject);
		}
	}

	private String getPrivilege(Connection conn, BSField field) {
		Privilege privilege = new Privilege();
		getNameFromEntity(conn, field, privilege);
		return privilege.getId() == null ? "Ninguno" : privilege.getName();

	}

	private String getBoss(Connection conn, BSField field) {
		Employee boss = new Employee();
		getNameFromEntity(conn, field, boss);
		return boss.getId() == null ? "Nadie" : boss.getName();

	}

}

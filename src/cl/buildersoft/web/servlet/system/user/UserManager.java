package cl.buildersoft.web.servlet.system.user;

import java.sql.Connection;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import cl.buildersoft.framework.type.Semaphore;
import cl.buildersoft.framework.beans.Domain;
import cl.buildersoft.framework.beans.User;
import cl.buildersoft.framework.business.services.EventLogService;
import cl.buildersoft.framework.business.services.ServiceFactory;
import cl.buildersoft.framework.util.crud.BSAction;
import cl.buildersoft.framework.util.crud.BSActionType;
import cl.buildersoft.framework.util.crud.BSField;
import cl.buildersoft.framework.util.crud.BSTableConfig;
import cl.buildersoft.framework.web.servlet.HttpServletCRUD;

@WebServlet("/servlet/system/user/UserManager")
public class UserManager extends HttpServletCRUD {
	private static final long serialVersionUID = -3497399350893131897L;

	@Override
	protected BSTableConfig getBSTableConfig(HttpServletRequest request) {
		HttpSession session = request.getSession(false);

		Boolean isAdmin = null;
		User user = null;
		Domain domain = null;
		BSTableConfig table = null;
		synchronized (session) {
			user = (User) session.getAttribute("User");
			domain = (Domain) session.getAttribute("Domain");
			isAdmin = user.getAdmin();
		}

		BSField field = null;
		if (isAdmin) {
			table = new BSTableConfig("bsframework", "tUser", "vUserAdmin");
			table.setSaveSP("bsframework.pSaveUserAdmin");
		} else {
			table = new BSTableConfig(domain.getDatabase(), "tUser", "vUser");
			table.setSaveSP("bsframework.pSaveUser");
		}

		table.setTitle("Usuarios del sistema");
		table.setDeleteSP("pDeleteUser");

		field = new BSField("cId", "ID");
		field.setPK(true);
		table.addField(field);

		field = new BSField("cMail", "Mail");
		field.setTypeHtml("email");
		table.addField(field);

		field = new BSField("cName", "Nombre");
		table.addField(field);

		if (isAdmin) {
			field = new BSField("cAdmin", "Administrador");
			table.addField(field);

			BSAction domainRelation = new BSAction("ROL_DOMAIN", null);
			domainRelation.setNatTable("bsframework", "tR_UserDomain", "bsframework", "tDomain");
			domainRelation.setLabel("Dominios del usuario");
			domainRelation.setContext("DALEA_CONTEXT");
			table.addAction(domainRelation);
		}

		BSAction action = new BSAction("CH_PASS&Reset=", BSActionType.Record);
		action.setLabel("Cambio de clave");
		action.setUrl("/servlet/system/changepassword/SearchPassword");
		action.setContext("DALEA_CONTEXT");
		table.addAction(action);

		BSAction rolRelation = new BSAction("ROL_RELATION", null);

		// Domain domain = (Domain) session.getAttribute("Domain");

		rolRelation.setNatTable(domain.getDatabase(), "tR_UserRol", domain.getDatabase(), "tRol");
		rolRelation.setLabel("Roles de usuario");
		rolRelation.setContext("DALEA_CONTEXT");
		table.addAction(rolRelation);

		return table;
	}

	@Override
	public Semaphore setSemaphore(Connection conn, Object[] values) {
		return null;
	}

	@Override
	public String getBusinessClass() {
		return this.getClass().getName();
	}

	@Override
	public void writeEventLog(Connection conn, String action, HttpServletRequest request, BSTableConfig table) {
		EventLogService event = ServiceFactory.createEventLogService();

		if ("INSERT".equals(action)) {
			event.writeEntry(conn, getCurrentUser(request).getId(), "NEW_USER", "Crea un nuevo usuario %s, mail %s", table
					.getField("cName").getValue(), table.getField("cMail").getValue());
		}
		if ("DELETE".equals(action)) {
			event.writeEntry(conn, getCurrentUser(request).getId(), "DELETE_USER",
					"Borra usuario %s, su Id fué %s\n mail %s\n y perfil %s", table.getField("cName").getValue(), table
							.getField("cId").getValue(), table.getField("cMail").getValue(), Boolean.parseBoolean(table
							.getField("cAdmin").getValue().toString()) ? "Administrador" : "Usuario");
		}
		if ("UPDATE".equals(action)) {
			event.writeEntry(conn, getCurrentUser(request).getId(), "UPDATE_USER",
					"Actualiza usuario, sus datos eran:\n- Id: %s.\n- Nombre: %s\n- Mail: %s\n- Perfil administrador: %s", table
							.getField("cId").getValue(), table.getField("cName").getValue(), table.getField("cMail").getValue(),
					Boolean.parseBoolean(table.getField("cAdmin").getValue().toString()) ? "Si" : "No");
		}
	}

}

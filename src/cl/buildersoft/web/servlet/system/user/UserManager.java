package cl.buildersoft.web.servlet.system.user;

import java.io.Serializable;
import java.sql.Connection;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import cl.buildersoft.framework.beans.Domain;
import cl.buildersoft.framework.beans.LogInfoBean;
import cl.buildersoft.framework.beans.User;
import cl.buildersoft.framework.type.Semaphore;
import cl.buildersoft.framework.util.crud.BSAction;
import cl.buildersoft.framework.util.crud.BSActionType;
import cl.buildersoft.framework.util.crud.BSField;
import cl.buildersoft.framework.util.crud.BSHttpServletCRUD;
import cl.buildersoft.framework.util.crud.BSTableConfig;

@WebServlet("/servlet/system/user/UserManager")
public class UserManager extends BSHttpServletCRUD   {
	private final static Logger LOG = LogManager.getLogger(UserManager.class.getName());
	private static final long serialVersionUID = -3497399350893131897L;

	@Override
	protected BSTableConfig getBSTableConfig(HttpServletRequest request) {
		HttpSession session = request.getSession(false);

		Boolean isAdmin = null;
		User user = null;
		Domain domain = null;
		BSTableConfig table = initTable(request, "bsframework", "tUser", this);
		synchronized (session) {
			user = (User) session.getAttribute("User");
			domain = (Domain) session.getAttribute("Domain");
			isAdmin = user.getAdmin();
		}

		BSField field = null;
		if (isAdmin) {
			table = new BSTableConfig("bsframework", "tUser", "vUserAdmin");
			table.setInsertSP("bsframework.pSaveUserAdmin");
		} else {
//			table = new BSTableConfig(domain.getDatabase(), "tUser", "vUser");
			table = new BSTableConfig("bsframework", "tUser", "vUser");
			table.setInsertSP("bsframework.pSaveUser");
			table.addInsertExtParam(this.getCurrentDomain(request).getId());
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

		configEventLog(table, getCurrentUser(request).getId());

		return table;
	}

	@Override
	public Semaphore setSemaphore(Connection conn, Object[] values) {
		return null;
	}

	@Override
	protected void configEventLog(BSTableConfig table, Long userId) {
		LogInfoBean li = new LogInfoBean();
		li.setAction("INSERT");
		li.setEventKey("NEW_USER");
		li.setMessage("Crea un nuevo usuario");
		li.setUserId(userId);
		table.addLogInfo(li);

		li = new LogInfoBean();
		li.setAction("DELETE");
		li.setEventKey("DELETE_USER");
		li.setMessage("Borra usuario");
		li.setUserId(userId);
		table.addLogInfo(li);

		li = new LogInfoBean();
		li.setAction("UPDATE");
		li.setEventKey("UPDATE_USER");
		li.setMessage("Actualiza usuario");
		li.setUserId(userId);
		table.addLogInfo(li);

	}

	
}

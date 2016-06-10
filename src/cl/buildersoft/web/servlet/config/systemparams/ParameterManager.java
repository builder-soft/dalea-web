package cl.buildersoft.web.servlet.config.systemparams;

import java.sql.Connection;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;

import cl.buildersoft.framework.type.Semaphore;
import cl.buildersoft.framework.util.crud.BSHttpServletCRUD;
import cl.buildersoft.framework.util.crud.BSTableConfig;

@WebServlet("/servlet/config/systemparams/ParameterManager")
public class ParameterManager extends BSHttpServletCRUD {
	private static final long serialVersionUID = 5621476235297476355L;

	public ParameterManager() {
		super();
	}

	@Override
	protected BSTableConfig getBSTableConfig(HttpServletRequest request) {
		BSTableConfig table = initTable(request, "tParameter");

		table.setTitle("Parametros del sistema");

		table.getField("cKey").setLabel("Llave");
		table.getField("cLabel").setLabel("Descripción");
		table.getField("cValue").setLabel("Valor");
		table.getField("cDataType").setLabel("Tipo de dato");

		table.renameAction("INSERT", "ADD_PARAMS");
		table.renameAction("EDIT", "MOD_PARAMS");
		table.renameAction("DELETE", "DEL_PARAMS");

		return table;
	}

	@Override
	public Semaphore setSemaphore(Connection conn, Object[] values) {
		return null;
	}

	@Override
	protected void configEventLog(BSTableConfig table, Long userId) {
		/**
		 * <code>
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
		
		</code>
		 */
	}

	
}

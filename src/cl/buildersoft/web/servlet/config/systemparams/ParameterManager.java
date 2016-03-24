package cl.buildersoft.web.servlet.config.systemparams;

import java.sql.Connection;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;

import cl.buildersoft.framework.type.Semaphore;
import cl.buildersoft.framework.util.crud.BSTableConfig;
import cl.buildersoft.framework.web.servlet.HttpServletCRUD;

@WebServlet("/servlet/config/systemparams/ParameterManager")
public class ParameterManager extends HttpServletCRUD   {
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
	public String getBusinessClass() {
		return this.getClass().getName();
	}

	@Override
	public void writeEventLog(Connection conn, String action, HttpServletRequest request, BSTableConfig table) {
		// TODO Auto-generated method stub

	}
}

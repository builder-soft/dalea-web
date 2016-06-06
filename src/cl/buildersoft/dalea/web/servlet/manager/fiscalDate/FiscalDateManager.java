package cl.buildersoft.dalea.web.servlet.manager.fiscalDate;

import java.sql.Connection;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;

import cl.buildersoft.framework.type.Semaphore;
import cl.buildersoft.framework.util.crud.BSHttpServletCRUD;
import cl.buildersoft.framework.util.crud.BSTableConfig;

@WebServlet("/servlet/timectrl/fiscalDate/FiscalDateManager")
public class FiscalDateManager extends BSHttpServletCRUD {
	private static final long serialVersionUID = 239341883519379947L;

	@Override
	protected BSTableConfig getBSTableConfig(HttpServletRequest request) {
		BSTableConfig table = initTable(request, "tFiscalDate");
		table.setTitle("Tabla de Feriados");

		table.getField("cReason").setLabel("Motivo");
		table.getField("cDate").setLabel("Fecha");

		table.setSortField("cDate");
		configEventLog(table, getCurrentUser(request).getId());

		return table;
	}

	@Override
	public Semaphore setSemaphore(Connection conn, Object[] values) {
		return null;
	}

	@Override
	protected void configEventLog(BSTableConfig table, Long userId) {
		// TODO Auto-generated method stub

	}

	@Override
	protected void preExecuteAction(BSTableConfig table, String action, Long userId) {
		// TODO Auto-generated method stub

	}

	@Override
	protected void postExecuteAction(BSTableConfig table, String action, Long userId) {
		// TODO Auto-generated method stub

	}
}

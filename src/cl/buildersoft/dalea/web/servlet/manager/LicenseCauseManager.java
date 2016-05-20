package cl.buildersoft.dalea.web.servlet.manager;

import java.sql.Connection;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;

import cl.buildersoft.framework.type.Semaphore;
import cl.buildersoft.framework.util.crud.BSHttpServletCRUD;
import cl.buildersoft.framework.util.crud.BSTableConfig;

/**
 * Servlet implementation class LicenseCause
 */
@WebServlet("/servlet/timectrl/licenseCause/LicenseCauseManager")
public class LicenseCauseManager extends BSHttpServletCRUD {
	private static final long serialVersionUID = 5784069118987822401L;

	@Override
	protected BSTableConfig getBSTableConfig(HttpServletRequest request) {
		BSTableConfig table = initTable(request, "tLicenseCause");
		table.setTitle("Tipos de licencias y permisos");

		table.getField("cName").setLabel("Descripción");
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
}

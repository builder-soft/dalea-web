package cl.buildersoft.dalea.web.servlet.manager.enterprise;

import java.sql.Connection;
import java.util.logging.Logger;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;

import cl.buildersoft.framework.type.Semaphore;
import cl.buildersoft.framework.util.crud.BSHttpServletCRUD;
import cl.buildersoft.framework.util.crud.BSTableConfig;

/**
 * Servlet implementation class EmployeeManager
 */
@WebServlet("/servlet/albizia/manager/Enterprise")
public class EnterpriseManager extends BSHttpServletCRUD {
	private static final Logger LOG = Logger.getLogger(EnterpriseManager.class.getName());
	private static final long serialVersionUID = -460775898391545834L;

	@Override
	protected BSTableConfig getBSTableConfig(HttpServletRequest request) {
		BSTableConfig table = initTable(request, "tEnterprise", this);

		table.setTitle("Empresas");

		table.getField("cName").setLabel("Nombre/Razón Social");
		table.getField("cLegalRep").setLabel("Representante Legal");
		table.getField("cCategory").setLabel("Giro");
		table.getField("cAddress").setLabel("Dirección");
		table.getField("cPhone").setLabel("Teléfono");
		table.getField("cRutLegalRep").setLabel("Rut representante Legal");
		table.getField("cMutualFactor").setLabel("Factor Mutual");
		table.getField("cCompensationFund").setLabel("Caja de compensación");

		hideFields(table, "cRutLegalRep", "cMutual", "cMutualFactor", "cCompensationFund");

		// BSAction enterpriseConfig = new BSAction("Configuration",
		// BSActionType.Record);
		// enterpriseConfig.setLabel("Configuracion de empresa");
		// enterpriseConfig.setUrl("/servlet/config/enterprise/EnterpriseConfigServlet");
		// table.addAction(enterpriseConfig);
		return table;
	}

	@Override
	public Semaphore setSemaphore(Connection conn, Object[] values) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected void configEventLog(BSTableConfig table, Long userId) {
		// TODO Auto-generated method stub

	}
}

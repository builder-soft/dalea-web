package cl.buildersoft.web.servlet.system;

import java.sql.Connection;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;

import cl.buildersoft.framework.database.BSmySQL;
import cl.buildersoft.framework.type.Semaphore;
import cl.buildersoft.framework.util.BSConnectionFactory;
import cl.buildersoft.framework.util.crud.BSHttpServletCRUD;
import cl.buildersoft.framework.util.crud.BSTableConfig;

/**
 * Servlet implementation class DomainAttributeManager
 */
@WebServlet("/servlet/system/DomainAttributeManager")
public class DomainAttributeManager extends BSHttpServletCRUD {
	private static final long serialVersionUID = -8184294919790127733L;

	@Override
	protected BSTableConfig getBSTableConfig(HttpServletRequest request) {
		BSTableConfig table = new BSTableConfig("bsframework", "tDomainAttribute");

		BSConnectionFactory cf = new BSConnectionFactory();

		Connection conn = cf.getConnection();
		table.configFields(conn, new BSmySQL());
		cf.closeConnection(conn);

		table.setTitle("Atributos de Dominios");
		table.getField("cId").setLabel("Id");
		table.getField("cDomain").setLabel("Dominio");
		table.getField("cKey").setLabel("Llave");
		table.getField("cName").setLabel("Nombre");
		table.getField("cValue").setLabel("Valor");

		return table;
	}

	@Override
	public Semaphore setSemaphore(Connection conn, Object[] values) {
		return null;
	}

	@Override
	protected void configEventLog(BSTableConfig table, Long userId) {
		// TODO Configurar eventos

	}

	
}

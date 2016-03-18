package cl.buildersoft.dalea;

import java.sql.Connection;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;

import cl.buildersoft.framework.beans.Config;
import cl.buildersoft.framework.database.BSBeanUtils;
import cl.buildersoft.framework.util.BSConnectionFactory;
import cl.buildersoft.framework.util.BSHttpServlet_;

// @ WebServlet("/startup")
public class StartUp extends BSHttpServlet_ {
	private static final String TIMECTRL_CONTEXT = "TIMECTRL_CONTEXT";
	private static final String DALEA_CONTEXT = "DALEA_CONTEXT";
	private static final long serialVersionUID = 1L;

	public StartUp() {
		super();
		System.out.println("Constructor.");

	}

	public void init(ServletConfig config) throws ServletException {
		BSConnectionFactory cf = new BSConnectionFactory();
		Connection conn = cf.getConnection();

		BSBeanUtils bu = new BSBeanUtils();

		Config daleContext = null;
		Config timectrlContext = null;
		try {
			daleContext = (Config) bu.list(conn, new Config(), "cKey=?", DALEA_CONTEXT);
			timectrlContext = (Config) bu.list(conn, new Config(), "cKey=?", TIMECTRL_CONTEXT);
		} finally {
			cf.closeConnection(conn);
		}
		
		config.getServletContext().setAttribute(DALEA_CONTEXT, daleContext);
		config.getServletContext().setAttribute(TIMECTRL_CONTEXT, timectrlContext);
	}

	public void destroy() {
		System.out.println("Destroy");
	}

}

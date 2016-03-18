package cl.buildersoft.dalea;

import java.sql.Connection;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;

import cl.buildersoft.framework.beans.Config;
import cl.buildersoft.framework.database.BSBeanUtils;
import cl.buildersoft.framework.util.BSConnectionFactory;
import cl.buildersoft.framework.util.BSHttpServlet_;
import cl.buildersoft.web.servlet.login.ValidateLoginServlet;

// @ WebServlet("/startup")
public class StartUp extends BSHttpServlet_ {
	private static final Logger LOG = Logger.getLogger(StartUp.class.getName());
	private static final long serialVersionUID = 4859610759900103241L;
	private static final String TIMECTRL_CONTEXT = "TIMECTRL_CONTEXT";
	private static final String DALEA_CONTEXT = "DALEA_CONTEXT";

	public StartUp() {
		super();
		LOG.log(Level.INFO, "Creating {0}", this.getClass().getName());
		
	}

	public void init(ServletConfig config) throws ServletException {
		LOG.log(Level.INFO, "Initializing {0} servlet", this.getClass().getName());
		BSConnectionFactory cf = new BSConnectionFactory();
		Connection conn = cf.getConnection();

		BSBeanUtils bu = new BSBeanUtils();

		Config daleContext = new Config();
		Config timectrlContext = new Config();
		try {
			bu.search(conn, daleContext, "cKey=?", DALEA_CONTEXT);
			bu.search(conn, timectrlContext, "cKey=?", TIMECTRL_CONTEXT);
		} finally {
			cf.closeConnection(conn);
		}

		config.getServletContext().setAttribute(DALEA_CONTEXT, daleContext.getValue());
		config.getServletContext().setAttribute(TIMECTRL_CONTEXT, timectrlContext.getValue());

	}

	public void destroy() {
		LOG.log(Level.INFO, "Destroing {0} servlet", this.getClass().getName());
	}

}

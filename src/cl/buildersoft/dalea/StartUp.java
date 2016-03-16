package cl.buildersoft.dalea;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

// @ WebServlet("/startup")
public class StartUp extends HttpServlet {
	private static final long serialVersionUID = 1L;

	public StartUp() {
		super();
		System.out.println("Constructor.");

	}

	public void init(ServletConfig config) throws ServletException {
		System.out.println("Init: " + config.getInitParameter("ContextList"));
		
	}

	public void destroy() {
		System.out.println("Destroy");
	}

}

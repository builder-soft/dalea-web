package cl.buildersoft.web.servlet.common.crud;

import java.io.IOException;
import java.sql.Connection;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import cl.buildersoft.framework.util.crud.BSField;
import cl.buildersoft.framework.util.crud.BSTableConfig;

@WebServlet("/servlet/common/NewRecord")
public class NewRecord extends AbstractServletUtil {
	private static final long serialVersionUID = -3029640874765422687L;

	public NewRecord() {
		super();
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession(false);
		BSTableConfig table = null;
		synchronized (session) {
			table = (BSTableConfig) session.getAttribute("BSTable");
		}
		BSField[] fields = table.getFields();
		for (BSField f : fields) {
			f.setValue(null);
		}
		request.setAttribute("Action", "Insert");

		Connection conn = getConnection(request);
		String url = bootstrap(conn) ? "/WEB-INF/jsp/crud/data-form2.jsp" : "/WEB-INF/jsp/table/data-form.jsp";
		closeConnection(conn);

		forward(request, response, url);
	}

}

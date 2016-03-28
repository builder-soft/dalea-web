package cl.buildersoft.web.servlet.common.crud;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/servlet/framework/report/DownloadReport")
public class DownloadReport_deprecated extends AbstractServletUtil {
	private static final long serialVersionUID = 5707424258094726391L;

	protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String[] heads = (String[]) request.getAttribute("Heads");
		List<String[]> data = (List<String[]>) request.getAttribute("Data");

		ServletOutputStream output = configHeaderAsFile(response, "download-report.xls");

		for (String head : heads) {
			output.print(head + "\t");
		}
		for (String[] row : data) {
			output.println("");
			for (String cell : row) {
				output.print(cell == null ? "" : cell + "\t");
			}

		}

		output.flush();
		output.close();
	}

}

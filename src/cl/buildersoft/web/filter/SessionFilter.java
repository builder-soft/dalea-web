package cl.buildersoft.web.filter;

import java.io.IOException;

import javax.servlet.DispatcherType;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cl.buildersoft.framework.util.BSHttpServlet_;

@WebFilter(dispatcherTypes = { DispatcherType.REQUEST, DispatcherType.FORWARD }, urlPatterns = { "/servlet/*" })
public class SessionFilter implements Filter {

	// public static String SESSION_COOKIE_NAME = "SessionCookie";

	public SessionFilter() {
		System.out.println("SessionFilterFordward");
	}

	public void destroy() {
		System.out.println("destroy");
	}

	public void init(FilterConfig fConfig) throws ServletException {
		System.out.println("init");
	}

	public void doFilter(ServletRequest rq, ServletResponse rs, FilterChain chain) throws IOException, ServletException {
		System.out.println("SessionFilter");
		HttpServletRequest request = (HttpServletRequest) rq;
		HttpServletResponse response = (HttpServletResponse) rs;

		BSHttpServlet_ su = new BSHttpServlet_();

//		String sessionId = su.readCookieValue(request);
//		try {
//			if (sessionId == null) {
//				request.getRequestDispatcher("Invaliduser.jsp").forward(request, response);
//			} else {
				try {
					su.restoreSession(request, response);
				} catch (Exception e) {
					throw new ServletException(e);
				}

				chain.doFilter(rq, rs);

//			}
//		} catch (Exception e) {
//			throw new ServletException(e);
//		}
	}

}

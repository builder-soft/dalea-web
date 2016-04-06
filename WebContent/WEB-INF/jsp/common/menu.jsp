<%@page import="cl.buildersoft.framework.beans.Option"%>
<%@page import="cl.buildersoft.framework.beans.Submenu"%>
<%@page import="cl.buildersoft.framework.beans.Menu"%>
<%@page import="cl.buildersoft.framework.util.BSWeb"%>
<%@ page import="java.util.List"%>
<ul class="jd_menu" style="z-index: 1">
	<li><a href="${pageContext.request.contextPath}/servlet/Home">Inicio</a></li>
	<%=write_menu_in_menu_jsp(session, request)%>
	<li><a
		href="${pageContext.request.contextPath}/jsp/login/logout.jsp">Salir</a></li>
</ul>


<%!private String write_menu_in_menu_jsp(HttpSession session, HttpServletRequest request) {
		Menu menuUser = (Menu) session.getAttribute("Menu");
		String out = "";
		//Boolean haveMore = null;
		if (menuUser != null) {
			String ctxPath = request.getContextPath();
			List<Submenu> main = menuUser.list();
			Option opt = null;
			String url = null;
			String label = null;
			for (Submenu submenu : main) {
				opt = submenu.getOption();
				out += "<li>";
				out += option2String(opt, ctxPath, true);
				out += writeSubMenu(submenu, ctxPath);
				out += "</li>\n";
			}
		}
		return out;
	}

	private String option2String(Option opt, String contextPath, Boolean isRoot) {
		String out = "";
		String url = opt.getUrl();
		String label = opt.getLabel();
		String urlPath = "";
		String endTag = "";
		String startTag = "";
		String firstCharacter = "?";

		url = url == null ? "" : url;

		if (url.length() > 0) {
			startTag = "<a ";
			if (url.startsWith("/")) {
				url = contextPath + url;
				endTag = "</a>";
			}
			out += startTag;

			if (url.indexOf("?") > -1) {
				firstCharacter = "&";
			}

			out += "href='" + url + firstCharacter + BSWeb.randomString() + "'>";
			endTag = "</a>";
			/**<code>
			} else {
			if (isRoot) {
				out += "";
				endTag = "";
			} else {
				out += "<li>";
				endTag = "</li>";
			}</code>
			 */
		}
		out += label + endTag + "";
		return out;
	}

	private String writeSubMenu(Submenu menu, String contextPath) {
		Option opt = null;
		String url = null;
		String label = null;
		List<Submenu> menuList = menu.list();
		Integer count = menuList.size();
		String out = count > 0 ? "<ul>" : "";

		for (Submenu submenu : menuList) {
			out += "<li>";
			out += option2String(submenu.getOption(), contextPath, false);
			out += writeSubMenu(submenu, contextPath);
			out += "</li>\n";
		}
		out += count > 0 ? "</ul>\n" : "\n";
		return out;
	}%>
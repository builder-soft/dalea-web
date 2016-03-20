<%@page import="cl.buildersoft.framework.util.crud.BSTableConfig"%>
<%@page import="cl.buildersoft.framework.util.crud.BSPaging"%>
<%
	if (paginationRequerid(request)) {
%>

<ul class="pager">
<%=write_first_link_jsp(request)%>
	<%=write_previous_link_jsp(request)%>
	<li><%=write_info_pages_jsp(request)%></li>
	<%=write_next_link_jsp(request)%>
	<%=write_last_link_jsp(request)%>

	<!-- 	
			<li><a href="< % =write_previous_link_jsp(request) % >">Anterior</a></li>
			<li><a href="< % =write_next_link_jsp(request) % >">Siguiente</a></li>
			 -->
</ul>

<%
	}
%>
<!-- 
<div class="row-fluid">
	<div class="span12">
		<div class="pagination pagination-centered">< % =write_pagination_jsp(request) % ></div>
	</div>
</div>


	<script type="text/javascript">
		function keyPressPaging(o, search, path) {
			alert(o + " " + search + " " + path);
			var out = true;
			var key = window.event.keyCode;
			if (key >= 48 && key <= 57) {
				out = true;
			} else {
				if (key == '13') {
					var url = path + "?Page=" + o.value + "&Search=" + search;
					self.location.href = url;
					out = false;
				}
			}
			return out;
		}
	</script>
 -->
<%!private Boolean paginationRequerid(HttpServletRequest request) {
		BSPaging paging = (BSPaging) request.getAttribute("Paging");
		return paging.getRequiresPaging();
	}

	private String write_info_pages_jsp(HttpServletRequest request) {
		String out = "<label>";
		BSPaging paging = (BSPaging) request.getAttribute("Paging");
		out += paging.getCurrentPage() + "/" + paging.getPageCount();
		out += "</label>";
		return out;
	}

	private String write_first_link_jsp(HttpServletRequest request) {
		BSPaging paging = (BSPaging) request.getAttribute("Paging");
		String cssClass = "";
		Integer currentPage = paging.getCurrentPage();

		if (currentPage == 1) {
			cssClass = " disabled";
		}
		return write_link_jsp(request, 1, paging, cssClass, "Primera");
	}

	private String write_last_link_jsp(HttpServletRequest request) {
		BSPaging paging = (BSPaging) request.getAttribute("Paging");
		String cssClass = "";
		Integer currentPage = paging.getCurrentPage();

		if (currentPage == paging.getPageCount()) {
			cssClass = " disabled";
		}
		return write_link_jsp(request, paging.getPageCount(), paging, cssClass, "Última");
	}

	
	private String write_previous_link_jsp(HttpServletRequest request) {
		BSPaging paging = (BSPaging) request.getAttribute("Paging");
		String cssClass = "";
		Integer currentPage = paging.getCurrentPage();

		if (currentPage == 1) {
			cssClass = " disabled";
		}
		return write_link_jsp(request, paging.getCurrentPage() - 1, paging, cssClass, "Anterior");
	}

	private String write_next_link_jsp(HttpServletRequest request) {
		BSPaging paging = (BSPaging) request.getAttribute("Paging");
		String cssClass = "";
		Integer currentPage = paging.getCurrentPage();

		if (currentPage == paging.getPageCount()) {
			cssClass = " disabled";
		}

		return write_link_jsp(request, paging.getCurrentPage() + 1, paging, cssClass, "Siguiente");
	}

	private String write_link_jsp(HttpServletRequest request, Integer page, BSPaging paging, String cssClass, String label) {
		String ctxPath = request.getContextPath();
		BSTableConfig table = (BSTableConfig) request.getSession(false).getAttribute("BSTable");
		String uri = table.getUri();
		String search = paging.getSearchValue(request);
		uri = linkToPage2(ctxPath, page, uri, search);

		String out = "<li class='" + cssClass + "'>";
		if ("disabled".indexOf(cssClass) >= 0) {
			out += "<a href='" + uri + "'>" + label + "</a>";
		} else {
			out += "<a href='#'>" + label + "</a>";
		}

		out += "</li>";
		return out;
	}

	/**
	 private String write_pagination_jsp(HttpServletRequest request) {
	 String ctxPath = request.getContextPath();
	 BSPaging paging = (BSPaging) request.getAttribute("Paging");
	 BSTableConfig table = (BSTableConfig) request.getSession(false).getAttribute("BSTable");

	 String uri = table.getUri();
	 String out = "";
	 //String search = getSearch(request);
	 String search = paging.getSearchValue(request);

	 //		out += "+" + search + "+";

	 if (paging != null && paging.getRequiresPaging()) {
	 Integer currentPage = paging.getCurrentPage();

	 if (paging.getCurrentPage() > 1) {
	 out += linkToPage(ctxPath, 1, "First", uri, search);
	 out += linkToPage(ctxPath, paging.getCurrentPage() - 1, "Prev", uri, search);
	 } else {

	 }

	 out += "<ul>";
	 out += "<input id='CurrentPage' size='2' type='text' onkeypress='return keyPressPaging(this, \"" + search + "\", \""
	 + (ctxPath + uri) + "\");' value='" + currentPage + "'>";
	 out += " de ";
	 out += paging.getPageCount();

	 if (paging.getCurrentPage() < paging.getPageCount()) {
	 out += linkToPage(ctxPath, currentPage + 1, "Next", uri, search);
	 out += linkToPage(ctxPath, paging.getPageCount(), "Last", uri, search);
	 }
	 }
	 return out;
	 }

	 private String linkToPage(String ctxPath, Integer page, String type, String uri, String search) {
	 String out = "<a href='" + ctxPath + uri + "?Page=" + page + "&Search=" + search + "'><img height='15px' src='" + ctxPath
	 + "/img/common/button_page" + type + ".jpg'></a>";

	 return out;
	 }
	 */
	private String linkToPage2(String ctxPath, Integer page, String uri, String search) {
		String out = ctxPath + uri + "?Page=" + page + "&Search=" + search;

		return out;
	}

	/**
	private String getSearch(HttpServletRequest request) {
		String out = (String) request.getAttribute("Search");
		if (out == null) {
			out = "";
		} else {
			out = out.trim();
		}
		return out;
	}*/%>
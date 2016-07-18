<%@page import="cl.buildersoft.framework.util.BSWeb"%>
<%@page import="cl.buildersoft.framework.beans.Submenu"%>
<%@page import="cl.buildersoft.framework.beans.Menu"%>
<%@page import="java.util.List"%>
<%@page import="java.sql.ResultSet"%>

<%@ include file="/WEB-INF/jsp/common/header2.jsp"%>
<%@ include file="/WEB-INF/jsp/common/menu2.jsp"%>
<%
	List<Object[]> rols = (List<Object[]>) request.getAttribute("Rols");
	//Long id = Long.parseLong(request.getParameter("cId"));

	Menu menuAux = (Menu) request.getAttribute("FullMenu");
	List<Submenu> fullMenu = menuAux.list();
	menuAux = (Menu) request.getAttribute("RolMenu");
	List<Submenu> rolMenu = menuAux.list();
%>

<link rel='stylesheet'
	href="${applicationScope['DALEA_CONTEXT']}/treeview2/style.css">
<script type="text/javascript"
	src="${pageContext.request.contextPath}/js/admin/role-def2.js?<%=BSWeb.randomString()%>"></script>

<!-- 
<script type="text/javascript">
	$(document).ready(function() {
		$('#tree1').checkboxTree();
	});
</script>
 -->

<h1 class="cTitle">Definición de Roles.</h1>

 
<form
	action="${pageContext.request.contextPath}/servlet/system/roledef/SaveRoleDef"
	id="frm" method="post">

<!-- 
<form 
action="${applicationScope['TIMECTRL_CONTEXT']}/servlet/ShowParameters" 
id="frm"  method="post">
-->

	<table border="0" width="60%">
		<tr>
			<td class="cLabel">Roles:</td>
			<td><select name="Rol" onchange="javascript:changeSelect('${pageContext.request.contextPath}', this);">
					<%
						for (Object[] row : rols) {
					%>
					<option value="<%=row[0]%>" <%=getSelected(row, request)%>><%=row[1]%></option>
					<%
						}
					%>
			</select></td>
		</tr>
		<tr>
			<td colspan="2">
				<div style="overflow: auto; width: 100%; height: 350px">

					<ul id="tree1" class="treeview">
						<%=write(fullMenu, rolMenu)%>
					</ul>





					<!-- 
<ul class="treeview">
	 <li>
		<input type="checkbox" name="root" id="root">
        <label for="root" class="custom-unchecked">Root</label>
		<ul>
				<li>
					<input type="checkbox" name="tall" id="tall">
					<label for="tall" class="custom-unchecked">Tall Things</label>
					<ul>
						 <li>
							 <input type="checkbox" name="tall-1" id="tall-1">
							 <label for="tall-1" class="custom-unchecked">Buildings</label>
						 </li>
						 <li>
							 <input type="checkbox" name="tall-2" id="tall-2">
							 <label for="tall-2" class="custom-unchecked">Giants</label>
							 <ul>
								 <li>
									 <input type="checkbox" name="tall-2-1" id="tall-2-1">
									 <label for="tall-2-1" class="custom-checked">Andre</label>
								 </li>
								 <li class="last">
									 <input type="checkbox" name="tall-2-2" id="tall-2-2">
									 <label for="tall-2-2" class="custom-unchecked">Paul Bunyan</label>
								 </li>
							 </ul>
						 </li>
						 <li class="last">
							 <input type="checkbox" name="tall-3" id="tall-3">
							 <label for="tall-3" class="custom-unchecked">Two sandwiches</label>
						 </li>
					</ul>
				</li>
				<li class="last">
					<input type="checkbox" name="short" id="short">
					<label for="short" class="custom-unchecked">Short Things</label>
					<ul>
						 <li>
							 <input type="checkbox" name="short-1" id="short-1">
							 <label for="short-1" class="custom-unchecked">Smurfs</label>
						 </li>
						 <li>
							 <input type="checkbox" name="short-2" id="short-2">
							 <label for="short-2" class="custom-unchecked">Mushrooms</label>
							<ul>
								 <li>
									 <input type="checkbox" name="short-x" id="short-x">
									 <label for="short-x" class="custom-unchecked">Smurfs X</label>
								 </li>
								 <li>
									 <input type="checkbox" name="short-y" id="short-y">
									 <label for="short-y" class="custom-unchecked">Mushrooms Y</label>
								 </li>
								 <li class="last">
									 <input type="checkbox" name="short-3" id="short-z">
									 <label for="short-z" class="custom-unchecked">One Sandwich Z</label>
										<ul>
											 <li>
												 <input type="checkbox" name="short-a" id="short-a">
												 <label for="short-a" class="custom-unchecked">Smurfs A</label>
											 </li>
											 <li>
												 <input type="checkbox" name="short-b" id="short-b">
												 <label for="short-b" class="custom-unchecked">Mushrooms B</label>
											 </li>
											 <li class="last">
												 <input type="checkbox" name="short-c" id="short-c">
												 <label for="short-c" class="custom-unchecked">One Sandwich C</label>
											 </li>
										</ul>									 
								 </li>
							</ul>
						 </li>
						 <li class="last">
							 <input type="checkbox" name="short-3" id="short-3">
							 <label for="short-3" class="custom-unchecked">One Sandwich</label>
						 </li>
					</ul>
				</li>
			</ul>
        </li>
		
    </ul>
 -->









				</div>
			</td>
		</tr>
	</table>
	<button type="submit" class="btn btn-primary">Grabar</button>

</form>

<%@ include file="/WEB-INF/jsp/common/footer2.jsp"%>

<script src="${applicationScope['DALEA_CONTEXT']}/treeview2/script2.js"></script>

<%!String idCheckbox = "";
	Integer deep=5;

	private String getSelected(Object[] row, HttpServletRequest request) {
		/** This method is used for search role selected. */
		Long id = (Long) request.getAttribute("cId");
		return ((Long) row[0]).equals(id) ? "selected" : "";
	}

	private String write(List<Submenu> fullMenu, List<Submenu> rolMenu) {
		String out = "";
		Option option = null;
		for (Submenu menu : fullMenu) {
			option = menu.getOption();
			out += deep(0) + "<li>\n" + deep(1) + drowCheckbox(option, rolMenu) + drawLabel(option, rolMenu) + writeSubOption(menu, rolMenu) + deep(0) + "</li>\n";

		}

		return out;
	}

	private String deep(Integer increment) {
		String out = "";
		
		deep += increment;

		for (int i = 0; i <= this.deep; i++) {
			out += "   ";
		}
		return out;
	}


	private String getChecked(Option option, List<Submenu> rolMenu) {
		Boolean isChecked = isChecked(option, rolMenu);
		return isChecked ? "custom-checked" : "custom-unchecked";
	}
	
	private String getChecked4Checkbox(Option option, List<Submenu> rolMenu) {
		Boolean isChecked = isChecked(option, rolMenu);
		return isChecked ? " checked" : "";
	}

	private Boolean isChecked(Option option, List<Submenu> main) {
		Boolean out = Boolean.FALSE;

		for (Submenu sub : main) {
			if (sub.getOption().getId().equals(option.getId())) {
				out = Boolean.TRUE;
				break;
			} else {
				out = isChecked(option, sub.list());
				if (out) {
					break;
				}
			}
		}

		return out;
	}

	private String writeSubOption(Submenu menu, List<Submenu> rolMenu) {
		String out = "";
		List<Submenu> main = menu.list();
		if (main.size() > 0) {
			out += deep(0) + "<ul>\n";
			deep(1);
			Option option = null;
			for (Submenu sub : main) {
				option = sub.getOption();
				out += deep(0) + "<li>\n" + deep(1) + drowCheckbox(option, rolMenu) + drawLabel(option, rolMenu) +  
						writeSubOption(sub, rolMenu) +  deep(-1) + "</li>\n";
			}
			out += deep(-1)+ "</ul>\n";
		}
		return out;
	}
	
	private String drowCheckbox(Option option, List<Submenu> rolMenu) {
		String out = "<input type='checkbox' ";
		out += "value='" + option.getId() + "' ";
		out += "name='Option' id='opt" + option.getId() + "'";
		out += getChecked4Checkbox(option, rolMenu);
		out += ">\n" + deep(0);
		idCheckbox = "opt" + option.getId();
		return out;
	}

	private String drawLabel(Option option, List<Submenu> rolMenu) {
		String out = "<label for='" + idCheckbox + "' class='"+ getChecked(option, rolMenu) + "'>"
				+ option.getLabel() + "</label>\n";
		
/**		
		String out = "<input type='checkbox' ";
		out += "value='" + option.getId() + "' ";
		out += "name='opt" + option.getId() + "' id='opt" + option.getId() + "'";
		//		out += getChecked(option, rolMenu);
		out += ">\n"+deep(0);
		idCheckbox = "opt" + option.getId();
		*/
		return out;
	}
						
	%>

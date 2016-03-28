<%@page import="cl.buildersoft.framework.util.crud.BSField"%>
<%@page import="cl.buildersoft.framework.util.crud.BSTableConfig"%>
<%@page import="java.sql.Connection"%>
<%@page import="cl.buildersoft.framework.database.BSmySQL"%>

<%@page import="java.util.Arrays"%>
<%@page import="java.sql.ResultSetMetaData"%>
<%@page import="java.util.ArrayList"%>

<%@page import="java.sql.ResultSet"%>

<%@ include file="/WEB-INF/jsp/common/header2.jsp"%>
<%@ include file="/WEB-INF/jsp/common/menu2.jsp"%>
<%
	String ctxPath = request.getServletContext().getAttribute("DALEA_CONTEXT").toString();
	ResultSet relation = (ResultSet) request.getAttribute("Relation");
	ResultSet list = (ResultSet) request.getAttribute("List");
	Connection conn = (Connection) request.getAttribute("Conn");

	BSTableConfig table = (BSTableConfig) session.getAttribute("BSTable");
	BSField[] fields = table.getFields();

	BSmySQL mysql = new BSmySQL();
	List<Object[]> listArray = mysql.resultSet2Matrix(list);
	list.close();

	List<Object[]> relationArray = mysql.resultSet2Matrix(relation);
	relation.close();

	new BSmySQL().closeConnection(conn);
%>
<script type="text/javascript"
	src="${applicationScope['DALEA_CONTEXT']}/js/table/relation.js?<%=BSWeb.randomString()%>"></script>

<div class="page-header">
	<h1><%=table.getTitle()%></h1>
</div>

<form
	action="${applicationScope['DALEA_CONTEXT']}/servlet/common/SaveRelation"
	id="frm" method="post">
	<input type="hidden" name="cId"
		value="<%=request.getParameter("cId")%>"> <input type="hidden"
		name="CodeAction" value="<%=request.getParameter("CodeAction")%>">

	<div class="row">
		<div class="col-sm-4 col-sm-offset-1">
			<label for="left">Disponibles</label> <select SIZE="10" id="left"
				style="width: 100%" class="form-control">
				<%
					for (Object[] row : listArray) {
						if (!exists(row, relationArray)) {
				%>
				<option value="<%=row[0]%>"><%=row[1]%></option>
				<%
					}
					}
				%>
			</select>
		</div>

		<div class="col-sm-1 col-sm-offset-1"><br><br><br>
			<button type="button" class="btn btn-success"
				onclick="javascript:moveToRight();" style="width: 100%">-></button>
			<br>
			<button type="button" onclick="javascript:moveToLeft();"
				class="btn btn-success" style="width: 100%"><-</button>
		</div>

		<div class="col-sm-4 col-sm-offset-1">
			<label for="right" align="center">Seleccionados</label><br> <select
				name="Relation" class="form-control" SIZE="10" id="right"
				style="width: 100%">
				<%
					for (Object[] row : relationArray) {
				%>
				<option value="<%=row[0]%>"><%=row[1]%></option>
				<%
					}
				%>
			</select>
		</div>

	</div>


	<!-- 
	<table border="0" width="50%">
		<tr>
			<td style="width: 30%" align="center"><span class="cLabel">Disponibles</span><br>
				<select SIZE="10" id="left" style="width: 100%" class="form-control">
					<%for (Object[] row : listArray) {
				if (!exists(row, relationArray)) {%>
					<option value="<%=row[0]%>"><%=row[1]%></option>
					<%}
			}%>
			</select></td>

			<td style="width: 10%" align="center"><button type="button" class="btn btn-success"
					onclick="javascript:moveToRight();" style="width: 100%">-></button>
				<br> <br> <br>
				<button type="button" onclick="javascript:moveToLeft();" class="btn btn-success"
					style="width: 100%"><-</button></td>

			<td style="width: 30%" align="center"><span class="cLabel"
				align="center">Seleccionados</span><br> <select name="Relation"
				class="form-control" SIZE="10" id="right" style="width: 100%">
					<%for (Object[] row : relationArray) {%>
					<option value="<%=row[0]%>"><%=row[1]%></option>
					<%}%>
			</select></td>
		</tr>
		<tr>
			<td align="center"><button type="button" class="btn btn-primary"
					onclick="javascript:save();">Aceptar</button></td>
			<td>&nbsp;</td>
			<td align="center"><a class="btn btn-link"
				href="<%=ctxPath + table.getUri()%>">Cancelar</a></td>
		</tr>
	</table>
	
	 -->
<button type="button" class="btn btn-primary"
					onclick="javascript:save();">Aceptar</button></td>
			<td>&nbsp;</td>
			<td align="center"><a class="btn btn-link"
				href="<%=ctxPath + table.getUri()%>">Cancelar</a>
</form>
<%@ include file="/WEB-INF/jsp/common/footer2.jsp"%>

<%!private Boolean exists(Object[] element, List<Object[]> list) {
		Boolean out = Boolean.FALSE;
		for (Object[] e : list) {

			if (Arrays.equals(element, e)) {

				out = Boolean.TRUE;
				break;
			}
		}

		return out;
	}

	private String showList(List<Object[]> l) {
		String out = "";

		for (Object[] e : l) {
			out += Arrays.toString(e) + ", ";
		}
		return out;
	}%>
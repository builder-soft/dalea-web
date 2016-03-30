<%@page import="cl.buildersoft.framework.util.crud.BSHttpServletCRUD"%>
<%@page import="cl.buildersoft.framework.dataType.BSDataTypeEnum"%>
<%@page import="cl.buildersoft.framework.dataType.BSDataType"%>
<%@page import="cl.buildersoft.framework.type.Semaphore"%>
<%@page import="cl.buildersoft.framework.util.crud.BSField"%>
<%@page import="cl.buildersoft.framework.util.crud.BSActionType"%>
<%@page import="cl.buildersoft.framework.util.crud.BSAction"%>
<%@page import="cl.buildersoft.framework.util.BSDateTimeUtil"%>
 
 
<%@page import="cl.buildersoft.framework.database.BSmySQL"%>
<%@page import="java.sql.Connection"%>
<%@page import="java.sql.ResultSet"%>
<%
	ResultSet rs = (ResultSet) request.getAttribute("Data");
	Connection conn = (Connection) request.getAttribute("Conn");
	if(conn == null){
		BSConnectionFactory cf = new BSConnectionFactory();
		conn = cf.getConnection(request);
	}
	BSTableConfig table = (BSTableConfig) session.getAttribute("BSTable");
	String script = table.getScript();

	BSAction[] tableActions = table.getActions(BSActionType.Table);
	BSAction[] recordActions = table.getActions(BSActionType.Record);
	BSAction[] multirecordActions = table.getActions(BSActionType.MultiRecord);

	Integer selectorType = getSelectorType(tableActions, recordActions, multirecordActions, request, conn);
%>

<%@ include file="/WEB-INF/jsp/common/header2.jsp"%>
<%@ include file="/WEB-INF/jsp/common/menu2.jsp"%>


<script src="${applicationScope['STATIC_CONTEXT']}/js/table/table.js?<%=Math.random()%>"></script>
<%
	if (script.length() > 0) {
%>
<!-- 
<script
	src="${pageContext.request.contextPath}<%=script%>?<%=Math.random()%>"></script>
	 -->
<%
	}
%>

<div class="page-header">
	<h1>
		<%=table.getTitle()%>
	</h1>
</div>

<%@ include file="/WEB-INF/jsp/common/search2.jsp"%>
<div class="row">
	<form method="post"
		_action="${pageContext.request.contextPath}/servlet/common/crud/DeleteRecords"
		id='frm'>

		<div class="table-responsive">
			<table
				class="table table-striped table-bordered table-hover table-condensed"
				id="MainTable">
				<%
					BSField[] fields = table.getFields();
					String name = null;
					String pkName = null;

					int rowCount = 0;
					Object[] values = null;
					out.println("<thead><tr>");

					if (selectorType > 0) {
						String type = selectorType == 1 ? "radio" : "CHECKBOX";
						out.print("<th class='text-center'>");
						if (selectorType >= 2) {
							out.print("<input id='mainCheck' type='" + type + "' onclick='javascript:swapAllCheck(this);'>");
						}
						out.print("</th>");
					}

					for (BSField field : fields) {
						if (field.getShowInTable()) {
							out.print("<th");

							out.print(getAlign(field));

							out.print(">" + field.getLabel() + "</th>");
						}
						if (field.isPK()) {
							pkName = field.getName();
						}
					}
					out.println("</tr></thead><tbody>");

					while (rs.next()) {
						values = values2Array(rs, pkName, fields);

						out.println(writeValues(values, fields, rowCount, request, selectorType, conn));
						rowCount++;
					}

					rs.close();
				%>
				</tbody>
			</table>
		</div>
		<div class="row">
			<%@ include file="/WEB-INF/jsp/common/pagination2.jsp"%>
		</div>

		<div class="row">
			<div class="btn-group">
				<%
					out.print("<br>");
					out.print("<div id='TableActions' style='float:left;'>");
					for (BSAction action : tableActions) {
						if (BSWeb.canUse(action.getCode(), request, conn)) {
							String id = capitalize(action.getCode());
							out.print("<button class='btn btn-default' type='button' ");
							out.print("id='o" + id + "' ");
							out.print(action.getDisabled() ? "disabled" : "");

							out.print(" onclick='javascript:window.location.href=\"" + request.getServletContext().getAttribute(action.getContext()) + action.getUrl() + "\"'");
							out.print(">" + action.getLabel() + "</button>");
						}

					}
					out.print("</div>");

					out.print("<div id='MultirecordActions' style='float:left;display:none;'>");
					for (BSAction action : multirecordActions) {
						if (BSWeb.canUse(action.getCode(), request, conn)) {
							String id = capitalize(action.getDefaultCode());
							String msg = action.getWarningMessage();
							String method = action.getMethod() != null ? "&Method=" + action.getMethod() : "";
							
							out.print("<button class='btn btn-default' type='button' ");
							out.print("id='o" + id + "' ");
							out.print(action.getDisabled() ? "disabled" : "");

						//	msg =  action.getWarningMessage();
							if(msg==null){
								msg="¿Esta seguro que desea ejecutar esta accion?";
							}
							String js = "";
							if(msg.length()>0){
								js += " onclick='javascript:";
								js += "if(confirm(\""+msg+"\")){";
								js += " doAction(\"" + request.getServletContext().getAttribute(action.getContext()) + action.getUrl() + "\", \"" + action.getCode()
										+ method + "\");";
								js += "}'";
							}else{
								js += " onclick='javascript:doAction(\"" + request.getServletContext().getAttribute(action.getContext()) + action.getUrl() + "\", \"" + action.getCode()
										+ method + "\");'";
							}
//							out.print(js + ":doAction(\"" + ctxPath + action.getUrl() + "\", \"" + action.getCode()
//									+ method + "\");'");
//							out.print(" onclick='javascript:f" + id + "(\""+msg+"\");'");
							out.print(js);
							out.print(">" + action.getLabel() + "</button>");
						}
					}
					out.print("</div>");

					out.print("<div id='RecordActions' style='float:left;display:none;'>");
					for (BSAction action : recordActions) {
						if (BSWeb.canUse(action.getCode(), request, conn)) {
							String id = capitalize(action.getCode());
							String method = action.getMethod() != null ? "&Method=" + action.getMethod() : "";
							out.print("<button class='btn btn-default' type='button' ");
							out.print("id='o" + id + "' ");

							out.print(action.getDisabled() ? "disabled" : "");

							out.print(" onclick='javascript:doAction(\"" + request.getServletContext().getAttribute(action.getContext()) + action.getUrl() + "\", \"" + action.getCode()
									+ method + "\");'");

							out.print(">" + action.getLabel() + "</button>");
						}
					}
					out.print("</div>");
				%>
			</div>
		</div>
	</form>
</div>

<%@ include file="/WEB-INF/jsp/common/footer2.jsp"%>
<%
	new BSmySQL().closeConnection(conn);
	conn=null;
%>

<%!private String writeValues(Object[] values, BSField[] fields, Integer rowCount, HttpServletRequest request,
			Integer selectorType, Connection conn) {
		String out = "";
		Object value = null;
		int i = 1;
		//		String color = rowCount % 2 != 0 ? "cDataTD" : "cDataTD_odd";
		String colorSemaphore = "";

		Object servletObject = request.getAttribute("ServletManager");
		if (servletObject != null) {
			BSHttpServletCRUD servletCRUD = (BSHttpServletCRUD) servletObject;
			Semaphore semaphore = servletCRUD.setSemaphore(conn, values);
			if (semaphore != null) {
				switch (semaphore) {
				case GREEN:
					colorSemaphore = "success";
					break;
				case YELLOW:
					colorSemaphore = "warning";
					break;
				case RED:
					colorSemaphore = "danger";
					break;
				default:
					break;
				}
			}
			out += "<tr class='" + colorSemaphore + "'>";
		}

		if (selectorType > 0) {
			String type = selectorType == 1 ? "radio" : "CHECKBOX";
			out += "<td class='text-center'>"; // class='" + (colorSemaphore.length() > 0 ? colorSemaphore : color) + "'>";

			out += "<input type='" + type + "' ";
			out += "name='cId' ";
			out += "value='" + values[0] + "' ";
			if (selectorType == 1) {
				out += "onclick=\"javascript:$('#RecordActions').show(speed);\" ";
			} else {
				out += "onclick='javascript:swapCheck(this);' ";
			}
			out += ">";

			out += "</td>";
		}
		BSDataType type = null;
		for (BSField field : fields) {
			type = field.getType();

			value = field.isPK() ? values[0] : values[i++];
			if (field.getShowInTable()) {
				out += "<td "; //class='" + color + "'";
				out += getAlign(field);
				out += ">";

				if (field.isFK()) {
					out += getFKValue(field, value);
				} else {
					if (type.isBoolean()  ) {
//					if (type.getDataTypeEnum().equals(BSDataTypeEnum.BOOLEAN)) {
						Boolean b = (Boolean) value;
						if (b.booleanValue() == Boolean.TRUE) {
							out += "Si";
						} else {
							out += "No";
						}
					} else if (type.getDataTypeEnum().equals(BSDataTypeEnum.DOUBLE)) {
						out += BSWeb.formatDouble(request, (Double) value);
					} else if (type.isTime()) {
						out += BSDateTimeUtil.date2String(request, value);
					} else if (type.getDataTypeEnum().equals(BSDataTypeEnum.INTEGER)) {
						out += BSWeb.formatInteger(request, (Integer) value);
					}

					else {
						out += value;
					}

				}
				out += "</td>";
			}
		}

		out += "</tr>";

		return out;
	}

	private String getFKValue(BSField field, Object code) {
		String out = "-";
		if (code != null) {
			Long codeLong = (Long) code;

			List<Object[]> data = field.getFKData();
			Long id = null;
			for (Object[] row : data) {
				id = (Long) row[0];

				if (codeLong.equals(id)) {
					out = (String) row[1];
					break;
				}

			}
		}
		return out;
	}

	private Object[] values2Array(ResultSet rs, String pkName, BSField[] fields) throws Exception {
		String name = null;
		Object value = null;
		int i = pkName == null ? 0 : 1;
		Object[] out = new Object[fields.length];

		for (BSField field : fields) {
			name = field.getName();
			value = rs.getObject(name);

			if (field.getName().equals(pkName)) {
				out[0] = value;
			} else {
				out[i++] = value;
			}

		}
		return out;
	}
	
	private Integer getSelectorType(BSAction[] tableActions, BSAction[] recordActions, BSAction[] multirecordActions,
			HttpServletRequest request, Connection conn) {

		Integer selectorType = 0;

		Boolean haveTableActions = Boolean.FALSE;
		Boolean haveRecordActions = Boolean.FALSE;
		Boolean haveMultirecordActions = Boolean.FALSE;

		for (BSAction action : tableActions) {
			if (BSWeb.canUse(action.getCode(), request, conn)) {
				haveTableActions = Boolean.TRUE;
				break;
			}
		}

		for (BSAction action : multirecordActions) {
			if (BSWeb.canUse(action.getCode(), request, conn)) {
				haveMultirecordActions = Boolean.TRUE;
				break;
			}
		}

		for (BSAction action : recordActions) {
			if (BSWeb.canUse(action.getCode(), request, conn)) {
				haveRecordActions = Boolean.TRUE;
				break;
			}
		}

		selectorType += haveRecordActions ? 1 : 0;
		selectorType += haveMultirecordActions ? 2 : 0;

		return selectorType;
	}	
	
	
	private String getAlign(BSField field) {
		BSDataType type = field.getType();
		String out = " align='left' ";

		if (type.isTime() || type.getDataTypeEnum().equals(BSDataTypeEnum.BOOLEAN) || field.isFK()) {
			out = " align='center' ";
		} else if (type.isNumber()) {
			out = " align='right' ";
		}

		 
		return out;
	}


	private String capitalize(String s) {
		return s.substring(0, 1).toUpperCase() + s.substring(1).toLowerCase();
	}%>

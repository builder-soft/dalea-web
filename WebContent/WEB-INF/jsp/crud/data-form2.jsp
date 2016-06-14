<%@page import="cl.buildersoft.framework.dataType.BSDataTypeFactory"%>
<%@page import="cl.buildersoft.framework.dataType.BSDataType"%>
<%@page import="cl.buildersoft.framework.util.BSDateTimeUtil"%>
<%@page import="cl.buildersoft.framework.dataType.BSDataTypeEnum"%>
<%@page import="cl.buildersoft.framework.util.crud.BSField"%>
<%@page import="cl.buildersoft.framework.util.crud.BSTableConfig"%>
<%@page import="java.sql.ResultSet"%>
<%
	BSTableConfig table = (BSTableConfig) session.getAttribute("BSTable");
	//Connection conn = (Connection) request.getAttribute("Conn");
	String nextServlet = (String) request.getAttribute("Action");
	BSField[] fields = table.getFields();
%>
<%@ include file="/WEB-INF/jsp/common/header2.jsp"%>

<script type="text/javascript">
function onLoadPage(){
	<%String fieldName = null;
			String html = "";
			BSDataType type = null;
			String typeHtml = null;
			Boolean showThis = null;

			for (BSField field : fields) {
				type = field.getType();
				fieldName = field.getName();
				typeHtml = field.getTypeHtml();
				showThis = field.isReadonly();

				if (type.getDataTypeEnum().equals(BSDataTypeEnum.DATE)) {%>
			$("#<%=fieldName%>").datepicker({dateFormat : fixDateFormat(dateFormat)});
		<%}
			}%>

	}

	function sendForm() {
		var msg = null;
<%for (BSField field : fields) {
				type = field.getType();
				fieldName = field.getName();
				typeHtml = field.getTypeHtml();
				showThis = field.isReadonly();
				html = "";

				if (!showThis) {
					if (type.getDataTypeEnum().equals(BSDataTypeEnum.DOUBLE) || type.getDataTypeEnum().equals(BSDataTypeEnum.INTEGER)
							|| type.getDataTypeEnum().equals(BSDataTypeEnum.DATE) || "email".equalsIgnoreCase(typeHtml)) {
						
						if (type.equals(BSDataTypeEnum.DOUBLE)) {
							html = "var " + fieldName + " = formated2double(document.getElementById('" + fieldName
									+ "').value);\n";
						}
						if (type.equals(BSDataTypeEnum.INTEGER)) {
							html = "var " + fieldName + " = formated2integer(document.getElementById('" + fieldName
									+ "').value);\n";
						}
						/**
						if(type.equals(BSDataType.Date)){
							html = "var " + fieldName + " = isDate(document.getElementById('"+fieldName+"').value);\n";
							html += fieldName + " = " + fieldName +"?"+fieldName +":null;\n";
						}
						 */
						if (field.getTypeHtml().equalsIgnoreCase("email")) {
							html = "var " + fieldName + " = isEmail(document.getElementById('" + fieldName + "').value);\n";
							html += fieldName + " = " + fieldName + "?" + fieldName + ":null;\n";
						}

						html += "if (" + fieldName + " == null) {\n";

						html += "   msg = 'El campo \"" + field.getLabel() + "\" no es valido';\n";
						html += "}\n";
					}
				}%>
	
<%=html%>
	
<%}%>
	if (msg != null) {
			alert(msg);
		} else {
<%html = "";
			for (BSField field : fields) {
				type = field.getType();
				fieldName = field.getName();
				showThis = field.isReadonly();

				if (!showThis) {
					if (type.equals(BSDataTypeEnum.DOUBLE) || type.equals(BSDataTypeEnum.INTEGER)) {
						html += "document.getElementById('" + fieldName + "').value = " + fieldName + ";\n";
					}
				}%>

			<%}%>

			<%=html%>
			document.getElementById("editForm").submit();
		}
	}
	function cancelForm() {
		document.getElementById("editForm").action = "${applicationScope['DALEA_CONTEXT']}/servlet/common/LoadTable";
		document.getElementById("editForm").submit();
	}
</script>
<%@ include file="/WEB-INF/jsp/common/menu2.jsp"%>
<div class="page-header">
	<h1 class="">Detalle de información</h1>
</div>

<%
	
	if ("insert".equalsIgnoreCase(nextServlet)) {
	//if ("insert".equalsIgnoreCase(nextServlet) || table.usingView()) {
		nextServlet = "InsertRecord";
	} else {
		nextServlet = "UpdateRecord";
	}
%>

<form
	action="${applicationScope['DALEA_CONTEXT']}/servlet/common/<%=nextServlet%>"
	method="post" id="editForm" class="form-horizontal" role="form">
	
		<%
				Boolean showInForm = null;
				for (BSField field : fields) {
					showInForm = field.getShowInForm();
					if (showInForm) {
			%>
	<div class="form-group">
		<label class="control-label col-sm-2" for="<%=field.getName()%>"><%=field.getLabel()%>:</label>
		<div class="col-sm-10">
			<%=writeHTMLField(field, request)%>
		</div>
	</div>
	<%
		}
		}
	%>
	</div>
</form>
 
<div class="form-group">
	<div class="col-sm-offset-2 col-sm-1">
		<button type="button" onclick="javascript:sendForm()"
			class="btn btn-primary">Aceptar</button>
		&nbsp;&nbsp;&nbsp;
	</div>
	<div class="col-sm-offset-1 col-sm-7">
		<button class="btn btn-link" onclick="javascript:cancelForm()">Cancelar</button>
	</div>
</div>
 

<%@ include file="/WEB-INF/jsp/common/footer2.jsp"%>

<%!private static String NEW = "[Nuevo]";

	private String writeHTMLField(BSField field, HttpServletRequest request) {
		String out = "";
		BSDataType type = field.getType();
		Object value = field.getValue();
		Integer maxlength = 0;
		String name = field.getName();
		String format = "";
		Integer size = 0;
		String afterInput = "";
		Boolean isPk = field.isPK();
		//Boolean isNew = Boolean.FALSE;
		Boolean isReadOnly = isPk ? Boolean.TRUE : field.isReadonly();
		String validationOnBlur = field.getValidationOnBlur() != null ? field.getValidationOnBlur() : "";

		BSDataTypeFactory dtf = new BSDataTypeFactory();

		if (isFK(field)) {
			out += getFKSelect(field);
		} else {
			if (type.getDataTypeEnum().equals(BSDataTypeEnum.BOOLEAN)) {
				out += "<SELECT class='form-control' name='" + name + "' ";
				out += isReadOnly ? " DISABLED " : "";
				out += ">";

				out += writeOptionHTML("true", "Si", value);
				out += writeOptionHTML("false", "No", value);

				out += "</SELECT>";
			} else {
				if (type.getDataTypeEnum().equals(BSDataTypeEnum.STRING)) {
					value = value == null ? "" : value;
					maxlength = field.getLength();
					size = maxlength;
					if (size > 75) {
						size = 75;
					}
				} else if (type.getDataTypeEnum().equals(BSDataTypeEnum.DATE)) {
					maxlength = 10;
					format = BSDateTimeUtil.getFormatDate(request);
					value = BSDateTimeUtil.date2String(value, format);
					size = maxlength;
					afterInput = "(formato: " + format + ")";
					type = dtf.create(BSDataTypeEnum.TEXT);
				} else if (type.getDataTypeEnum().equals(BSDataTypeEnum.TIMESTAMP)) {
					maxlength = 16;
					format = BSDateTimeUtil.getFormatDatetime(request);
					value = BSDateTimeUtil.date2String(value, format);
					size = maxlength;
					afterInput = "(formato: " + format + ")";
					type = dtf.create(BSDataTypeEnum.TEXT);
				} else if (type.getDataTypeEnum().equals(BSDataTypeEnum.DOUBLE)) {
					maxlength = 15;
					value = BSWeb.formatDouble(request, (Double) value);
					size = maxlength;
				} else if (type.getDataTypeEnum().equals(BSDataTypeEnum.INTEGER)) {
					maxlength = 8;
					value = BSWeb.formatInteger(request, (Integer) value);
					size = maxlength;
				} else if (type.getDataTypeEnum().equals(BSDataTypeEnum.LONG)) {
					maxlength = 10;
					if (isPk && value == null) {
						value = NEW;
					} else {
						value = value == null ? "" : BSWeb.formatLong(request, (Long) value);
					}
					size = maxlength;
				}

				out += drawInputText(field.getTypeHtml(), name, maxlength, isReadOnly, value, size, afterInput, validationOnBlur,
						isPk, type, field.getLabel());
			}
		}
		return out;
	}

	private String writeOptionHTML(String option, String display, Object value) {
		String out = "<OPTION value='" + option + "'";
		out += (value != null && value.toString().equals(option) ? " selected" : "");
		out += ">" + display + "</OPTION>";
		return out;
	}

	private Boolean isFK(BSField field) {
		Boolean out = Boolean.FALSE;
		List<Object[]> data = field.getFKData();
		out = data != null;
		return out;
	}

	private String drawInputText(String type, String name, Integer maxlength, Boolean isReadonly, Object value, Integer size,
			String afterInput, String validationOnBlur, Boolean isPk, BSDataType dataType, String label) {
		String out = "";

		if (isPk) {
			out += "<span class='form-control'>" + value + "</span>";
			type = isPk ? "hidden" : type;
		}

		if (dataType.isTime()) {
			out += "$('#" + name + "').datepicker({	dateFormat : fixDateFormat(dateFormat)});\n";
			out += "$('#" + name + "').datepicker('setDate', value);\n";
		} else {
			out += "<input class='form-control' placeholder='" + label + "' type='" + type + "' name='";
			out += name;
			out += "' ";
			out += "id='" + name + "' ";
			out += "maxlength='" + maxlength + "' ";
			out += isReadonly ? "READONLY " : "";
			out += "value='" + (value.equals(NEW) ? "0" : value) + "' ";
			out += "size='" + size + "px' ";

			out += addScript(dataType, type);
		}
		if (!"".equals(validationOnBlur)) {
			out += "onBlur='javascript:" + validationOnBlur + "(this)'";
		}

		out += ">&nbsp;<span class='cLabel'>" + afterInput + "</span>";

		return out;
	}

	private String addScript(BSDataType dataType, String type) {
		String out = "";
		if (dataType.getDataTypeEnum().equals(BSDataTypeEnum.DOUBLE)) {
			out = "onfocus='javascript:doubleFocus(this);' ";
			out += "onblur='javascript:doubleBlur(this);' ";
		} else if (dataType.getDataTypeEnum().equals(BSDataTypeEnum.INTEGER)) {
			out = "onfocus='javascript:integerFocus(this);' ";
			out += "onblur='javascript:integerBlur(this);' ";
		} else if (dataType.getDataTypeEnum().equals(BSDataTypeEnum.DATE)) {
			out += "onblur='javascript:dateBlur(this);' ";
		} else {
			if (type.equalsIgnoreCase("email")) {
				//out += "onblur='javascript:emailBlur(this);' ";
			}
		}
		return out;
	}

	private String getFKSelect(BSField field) {
		String name = field.getName();
		Object value = field.getValue();
		Boolean isNullable = field.getIsNullable();

		String out = "<select class='form-control' name='";
		out += name + "'>";
		if (isNullable) {
			out += "<option value=''>- Ninguno -</option>";
		}
		List<Object[]> data = field.getFKData();
		for (Object[] row : data) {
			out += "<option value='" + row[0] + "' ";
			if (value != null) {
				out += value.equals(row[0]) ? " selected " : "";
			}
			out += ">" + row[1] + "</option>";
		}
		out += "</select>";
		return out;
	}%>

<%@page import="java.util.List"%>
<%@page import="cl.buildersoft.framework.beans.Domain"%>
<%@page import="cl.buildersoft.framework.util.BSWeb"%>
<%@ page language="java"
	contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%
	final String JDMENU_VERSION = "1.2.1";
	final String JQUERY_VERSION = "1.8.2";
%><!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">

<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width, initial-scale=1">

<LINK rel="stylesheet" type="text/css"
	href="${applicationScope['STATIC_CONTEXT']}/css/default.css" />

<title>DALEA T&amp;A - Buildersoft</title>
<script>
	var contextPath = "${pageContext.request.contextPath}";
	var speed = "slow";// "slow" "fast";
	var dateFormat = "${applicationScope.DateFormat}";

	function fixDateFormat(dateFormat) {
		//alert(dateFormat);
		var out = dateFormat;
		if (dateFormat.search('yyyy') > -1) {
			out = dateFormat.replace('yyyy', 'yy');
		} else {
			if (dateFormat.search('yy') > -1) {
				out = dateFormat.replace('yy', 'y');
			}
		}
		// alert(out);
		return out.toLowerCase();
	}
</script>
<script
	src="${applicationScope['DALEA_CONTEXT']}/js/common/framework.js?<%=Math.random() %>"></script>

<script
	src="${applicationScope['STATIC_CONTEXT']}/plugin/jquery/<%=JQUERY_VERSION %>/jquery-<%=JQUERY_VERSION %>.js"></script>
<script
	src="${applicationScope['STATIC_CONTEXT']}/plugin/jquery/<%=JQUERY_VERSION %>/jquery.ui.core.js"></script>
<script
	src="${applicationScope['STATIC_CONTEXT']}/plugin/jquery/<%=JQUERY_VERSION %>/jquery.ui.datepicker.js?<%=Math.random() %>"></script>
<script
	src="${applicationScope['STATIC_CONTEXT']}/plugin/jquery/<%=JQUERY_VERSION %>/jquery.ui.widget.js"></script>
<LINK rel="stylesheet" type="text/css"
	href="${applicationScope['STATIC_CONTEXT']}/plugin/jquery/<%=JQUERY_VERSION %>/jquery.ui.all.css" />
<!-- 
<LINK rel="stylesheet" type="text/css"
	href="${pageContext.request.contextPath}/plugin/jquery/<%=JQUERY_VERSION%>/demos-calendar.css" />
  -->

<!-- 
<script
	src="${pageContext.request.contextPath}/js/common/jdMenu/1.4.1/jquery.bgiframe.js"></script>
<script
	src="${pageContext.request.contextPath}/js/common/jdMenu/1.4.1/jquery.dimensions.js"></script>
<script
	src="${pageContext.request.contextPath}/js/common/jdMenu/1.4.1/jquery.jdMenu.js"></script>
<script
	src="${pageContext.request.contextPath}/js/common/jdMenu/1.4.1/jquery.positionBy.js"></script>

<script
	src="${pageContext.request.contextPath}/js/common/jdMenu/1.4.1/jquery.js"></script>
-->

<script
	src="${applicationScope['STATIC_CONTEXT']}/plugin/jdmenu/<%=JDMENU_VERSION %>/jquery.dimensions.js"></script>
<script
	src="${applicationScope['STATIC_CONTEXT']}/plugin/jdmenu/<%=JDMENU_VERSION %>/jquery.jdMenu.js"></script>
<script
	src="${applicationScope['STATIC_CONTEXT']}/plugin/jdmenu/<%=JDMENU_VERSION %>/jquery.jdMenu.packed.js"></script>
<LINK rel="stylesheet" type="text/css"
	href="${applicationScope['STATIC_CONTEXT']}/plugin/jdmenu/<%=JDMENU_VERSION %>/jdMenu.css" />

<script type="text/javascript">
	$(function() {
		$('ul.jd_menu').jdMenu();
	});
	
	function changeDomain(object){
//		alert(object.value);
		self.location.href="${applicationScope['DALEA_CONTEXT']}/servlet/system/user/ChangeDomain?cId=" + object.value + "&<%=BSWeb.randomString()%>";		
	}
</script>
</head>

<body marginwidth="25px" marginheight="5px"
	onload="javaScript:loadFormat();try{onLoadPage();}catch(e){}">
	<div id="TOOLTIP_CONTAINER"
		style="display: none; position: absolute; top: 0; left: 0; height: 0; width: 0; background-color: #C3C3C2; opacity: 0.5; z-index: 1;"
		align="center"></div>

	<div id="TOOLTIP_CONTENT"
		style="display: none; z-index: 2; position: absolute;" align="center">
		<br /> <br />
		<table border="0"
			style="background-color: white; border: #000000 solid" width="60%"
			height="50%">
			<tr>
				<td align="center" width="10">&nbsp;</td>
				<td>&nbsp;</td>
				<td align="center" width="10"><a class="cLabel"
					style="cursor: pointer" href="javascript:closeTooltip()"
					onmousemove="javascript:window.status='Cerrar'"> (X) </a></td>
			</tr>
			<tr>
				<td align="center" width="10">&nbsp;</td>
				<td height="90%" align="center" valign="top"
					id="TOOLTIP_CELL_CONTAINER"></td>
				<td align="center" width="10">&nbsp;</td>
			</tr>
			<tr>
				<td align="center" width="10">&nbsp;</td>
				<td>&nbsp;</td>
				<td align="center" width="10">&nbsp;</td>
			</tr>
		</table>
	</div>


	<table border="0" style="width: 100%">
		<tr>
			<td align="right"><span class="cLabel">Dominio:</span> <span
				class="cData"> <%
 	Domain currentDomains = (Domain) session.getAttribute("Domain");
 	List<Domain> domains = (List<Domain>) session.getAttribute("Domains");
 	if(domains!=null){
 		if (domains.size() > 1) {
 %><select name="cId" onchange="javascript:changeDomain(this)">
						<%
							for (Domain domain : domains) {
						%>
						<option value="<%=domain.getId()%>"
							<%=currentDomains.getId().equals(domain.getId()) ? "selected" : ""%>><%=domain.getName()%></option>
						<%
							}
						%>
				</select> <%
 		
 		} else {
 %> &nbsp;${sessionScope.Domain.name} <%
 		}
 	}
 %>
			</span>&nbsp;&nbsp;|&nbsp;&nbsp; <span class="cLabel">Usuario:</span><span
				class="cData">&nbsp;${sessionScope.User.name} -
					${sessionScope.User.mail}</span></td>
		<tr>
			<td valign="top">
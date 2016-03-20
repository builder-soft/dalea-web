<%@page import="cl.buildersoft.framework.util.BSWeb"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="es">
<head>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width, initial-scale=1">

<title>DALEA T&amp;A - Buildersoft</title>

<link
	href="${applicationScope['STATIC_CONTEXT']}/bootstrap-334-dist/css/bootstrap.css"
	rel="stylesheet">
<link
	href="${applicationScope['STATIC_CONTEXT']}/plugin/smart-menu/jquery.smartmenus.bootstrap.css"
	rel="stylesheet">
<!--[if lt IE 9]>
      <script src="https://oss.maxcdn.com/html5shiv/3.7.2/html5shiv.min.js"></script>
      <script src="https://oss.maxcdn.com/respond/1.4.2/respond.min.js"></script>
    <![endif]-->
  <link rel="stylesheet" href="${applicationScope['STATIC_CONTEXT']}/plugin/jquery/1.11.3/jquery-ui.css">
 
<script>
	var contextPath = "${applicationScope['TIMECTRL_CONTEXT']}";
	var speed = "fast";// "slow" "fast";
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
	
	function returnTo(url){
		self.location.href = url;
	}
</script>
<script
	src="${applicationScope['DALEA_CONTEXT']}/js/common/framework.js?<%=BSWeb.randomString()%>"></script>

</head>

<body onload="javaScript:try{onLoadPage();}catch(e){}">
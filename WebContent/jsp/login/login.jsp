<%@page import="cl.buildersoft.framework.util.BSHttpServlet_"%>
<%@page import="cl.buildersoft.framework.util.BSWeb"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%
//	request.getSession(false).invalidate();
	BSHttpServlet_ hs = new BSHttpServlet_();
	hs.deleteSession(request, response);
	
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html lang="es">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">

<LINK rel="stylesheet" type="text/css"
	href="${pageContext.request.contextPath}/css/default.css?<%=Math.random()%>" />

<title>Ingreso de usuario</title>
<script>
	function posImage() {return;
		var img = document.getElementById("html5");
		img.style.position= 'absolute';
		img.style.pixelTop = (screen.height-300);
	}
</script>
</head>
<body onload="javascript:posImage()">
	<form
		action="${pageContext.request.contextPath}/login/ValidateLoginServlet?<%=BSWeb.randomString() %>"
		method="post">
		<span class="cLabel">Mail:</span> <input type="text" name="mail" required
			placeholder="Correo personal"><br> <span class="cLabel">Clave:
		</span> <input type="password" required placeholder="Clave de acceso" name="password"><br>

		<button type="submit">Acceder...</button>
	</form>
<!-- 		
	<img src="${pageContext.request.contextPath}/img/html5-icon.png" 
		width="80px" id="html5" alt="Pensando en HTML5 como estandar" />
		 -->
</body>
</html>
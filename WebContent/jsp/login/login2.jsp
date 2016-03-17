<%@page import="cl.buildersoft.framework.util.BSHttpServlet_"%>
<%
	BSHttpServlet_ hs = new BSHttpServlet_();
	hs.deleteSession(request, response);
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<META HTTP-EQUIV="CACHE-CONTROL" CONTENT="NO-CACHE">
<META HTTP-EQUIV="EXPIRES" CONTENT="Mon, 22 Jul 2002 11:12:01 GMT">
<title>DALEA T&amp;A</title>
<link
	href="${pageContext.request.contextPath}/bootstrap-320/css/bootstrap.css"
	rel="stylesheet">

<style type="text/css">
body {
	background-image:
		url('${pageContext.request.contextPath}/img/login/wallpapper4.jpg');
	background-size: 1144px;
	background-repeat: no-repeat;
	background-position: center top;
	background-color: white;
}

.form-signin {
	max-width: 330px;
	padding: 15px;
	margin: 0 auto;
}

.form-signin .form-signin-heading,.form-signin .checkbox {
	margin-bottom: 0px;
}

.form-signin .checkbox {
	font-weight: normal;
}

.form-signin .form-control {
	position: relative;
	font-size: 16px;
	height: auto;
	padding: 10px;
	-webkit-box-sizing: border-box;
	-moz-box-sizing: border-box;
	box-sizing: border-box;
}

.form-signin .form-control:focus {
	z-index: 2;
}

.form-signin input[type="text"] {
	margin-bottom: -1px;
	border-bottom-left-radius: 0;
	border-bottom-right-radius: 0;
}

.form-signin input[type="password"] {
	margin-bottom: 10px;
	border-top-left-radius: 0;
	border-top-right-radius: 0;
}


.login-title {
	color: #555;
	font-size: 18px;
	font-weight: 400;
	display: block;
	background-color: white;
}

.profile-img {
	width: 96px;
	height: 96px;
	margin: 0 auto 0px;
	display: block;
	-moz-border-radius: 50%;
	-webkit-border-radius: 50%;
	border-radius: 50%;
}

.need-help {
	margin-top: 10px;
}

.new-account {
	display: block;
	margin-top: 10px;
}

/**Aqui se configura la posicion del cuadro de login */
.account-wall {
	margin-top: 330px;
	padding: 10px 0px 10px 0px;
	background-color: #f7f7f7;
	-moz-box-shadow: 0px 2px 2px rgba(0, 0, 0, 0.3);
	-webkit-box-shadow: 0px 2px 2px rgba(0, 0, 0, 0.3);
	box-shadow: 0px 2px 2px rgba(0, 0, 0, 0.3);
	margin-left: 50px;
	width: 250px;
}
</style>
</head>
<body>
	<div class="container">
		<div class="row">
			<div class="col-sm-6 col-md-4 col-md-offset-4">
			 <!--  
				<h1 class="text-center login-title">DALEA T&A</h1>
				  -->
				<div class="account-wall">
				  
					<img class="profile-img"
						src="${pageContext.request.contextPath}/img/login/photo.png"
						alt="">
						  
					<form class="form-signin"
						action="${pageContext.request.contextPath}/login/ValidateLoginServlet"
						method="post">
						<input type="text" class="form-control" placeholder="Usuario"
							name="mail" required autofocus> <input type="password"
							name="password" class="form-control"
							placeholder="Clave de acceso" required>
						<button class="btn btn-lg btn-primary btn-block" type="submit">
							Entrar</button>
						<!-- 
                <label class="checkbox pull-left">
                    <input type="checkbox" value="remember-me">
                    Remember me
                </label>
                <a href="#" class="pull-right need-help">Need help? </a><span class="clearfix"></span>
                -->
					</form>
				</div>
				<!-- 
            <a href="#" class="text-center new-account">Create an account </a>
             -->

			</div>
		</div>
	</div>
	<%= getServletContext().getInitParameter("A") %>
</body>
</html>
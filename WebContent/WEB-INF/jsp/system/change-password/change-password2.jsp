<%@ include file="/WEB-INF/jsp/common/header2.jsp"%>
<%@ include file="/WEB-INF/jsp/common/menu2.jsp"%>
<%
	String nextAction = "/servlet/system/changepassword/ChangePassword";
	String cancelAction = "/servlet/system/user/UserManager";
	Boolean passwordIsNull = (Boolean) request.getAttribute("PASS_IS_NULL");

	Object id = request.getParameter("cId");
	if (id == null) {
		id = (Long) request.getAttribute("cId");
		//	nextAction += "?Next=/servlet/Home";
		cancelAction = "/servlet/Home";
	}

	if (passwordIsNull) {
		/**Es cuando la password la cambia el administrador*/
		nextAction += "?Next=" + request.getServletContext().getAttribute("DALEA_CONTEXT")
				+ "/servlet/system/user/UserManager";
	} else {
		nextAction += "?Next=" + request.getServletContext().getAttribute("DALEA_CONTEXT") + "/servlet/Home";
	}

	Integer minLen = (Integer) request.getAttribute("PASS_MIN_LEN");
	Integer specialChars = (Integer) request.getAttribute("PASS_SPEC_CHR");
	Integer upperChars = (Integer) request.getAttribute("PASS_UPPER_CHR");
	Integer numChars = (Integer) request.getAttribute("PASS_NUM_CHR");
%>
<div class="page-header">
	<h1>
		Cambio de clave
		<%=passwordIsNull%></h1>
</div>

<div class="row">
<form action="${applicationScope['DALEA_CONTEXT']}<%=nextAction%>"
	method="post" class="form-horizontal" role="form">
	<input type="hidden" name="cId" value="<%=id%>"> <input
		type="hidden" name="Reset" value="<%=passwordIsNull%>">

	<%
		if (!passwordIsNull) {
	%>

	<div class="form-group">
		<label class="control-label col-sm-2" for="OldPassword">Clave
			anterior: </label>

		<div class="col-sm-10">
			<input type="password" name="OldPassword" id="OldPassword"
				class="form-control" autocomplete="off">
		</div>
	</div>
	<%
		}
	%>
	<div class="form-group">
		<label class="control-label col-sm-2" for="NewPassword">Nueva
			clave:</label>
		<div class="col-sm-10">
			<input type="password" name="NewPassword" id="NewPassword"
				class="form-control" autocomplete="off">
		</div>
	</div>

	<div class="form-group">
		<label class="control-label col-sm-2" for="CommitPassword">Confirme
			clave:</label>
		<div class="col-sm-10">
			<input type="password" name="CommitPassword" id="CommitPassword"
				class="form-control" autocomplete="off">
			</td>
		</div>
	</div>

	<button type="submit" class="btn btn-primary">Confirmar</button>
	<a class="btn btn-link"
		href="${applicationScope['DALEA_CONTEXT']}<%=cancelAction%>">Cancelar</a>

</form>
</div>
<br>
<div class="row well">
	<div class="col-sm-11 col-sm-offset-1 ">
	Considere las siguientes condiciones para definir la clave de acceso:
	</div>
	
	
	<!-- 
	<div class="col-sm-10 col-sm-offset-2">
	Largo mínimo:	<%=minLen %>
	</div>
	<div class="col-sm-10 col-sm-offset-2">
	Caracteres especiales:	<%=specialChars %>
	</div>
	<div class="col-sm-10 col-sm-offset-2">
	Letras mayusculas:	<%=upperChars %>
	</div>
	<div class="col-sm-10 col-sm-offset-2">
	Números:	<%=numChars %>
	</div>
 -->

</div>
	<ul class="list-group">
	<li class="list-group-item">
	Largo mínimo:<%=minLen %></li></ul>


<%@ include file="/WEB-INF/jsp/common/footer2.jsp"%>


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
	
	if(passwordIsNull){ /**Es cuando la password la cambia el administrador*/
		nextAction += "?Next="+request.getServletContext().getAttribute("DALEA_CONTEXT")+"/servlet/system/user/UserManager";
	}else{
		nextAction += "?Next="+request.getServletContext().getAttribute("DALEA_CONTEXT")+"/servlet/Home";
	}
	
%>
<div class="page-header">
	<h1>Cambio de clave <%=passwordIsNull %></h1>
</div>

<form action="${pageContext.request.contextPath}<%=nextAction%>"
	method="post" class="form-horizontal" role="form">
	<input type="hidden" name="cId" value="<%=id%>">
	<input type="hidden" name="Reset" value="<%=passwordIsNull%>">

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
		href="${pageContext.request.contextPath}<%=cancelAction%>">Cancelar</a>
		 
</form>

<%@ include file="/WEB-INF/jsp/common/footer2.jsp"%>


<%@ include file="/WEB-INF/jsp/common/header2.jsp"%>
<%
	Object id = request.getAttribute("cId");
//request.getSession(true);
%>
<div class="container">

	<div class="page-header">
		<h1>Clave de acceso expirada</h1>
	</div>

	<div class="row">
		<div class="col-sm-10 col-sm-offset-1 well">

			<form
				action="${pageContext.request.contextPath}/servlet/system/changepassword/ChangePassword"
				method="post" class="form-horizontal" role="form">
				<input type="hidden" name="cId" value="<%=id%>">
				<input type="hidden" name="_GoHome">
				<input type="hidden" name="Next" value="${pageContext.request.contextPath}/jsp/login/logout.jsp?<%=BSWeb.randomString()%>">

				<div class="form-group">
					<label class="control-label col-sm-2" for="OldPassword">Clave
						anterior: </label>

					<div class="col-sm-10">
						<input type="password" name="OldPassword" id="OldPassword"
							class="form-control" autocomplete="off">
					</div>
				</div>

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

				<button type="submit" class="btn btn-primary">Aceptar</button>
				<a class="btn btn-link" href="${pageContext.request.contextPath}">Cancelar</a>

			</form>


		</div>
	</div>


<%@ include file="/WEB-INF/jsp/common/footer2.jsp"%>
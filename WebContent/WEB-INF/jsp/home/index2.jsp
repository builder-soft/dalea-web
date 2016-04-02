<%@ include file="/WEB-INF/jsp/common/header2.jsp"%>
<%@ include file="/WEB-INF/jsp/common/menu2.jsp"%>
<%
	String loadingIcon = "/img/loading/2.gif";
%>
<script type="text/javascript">
<!--
	var contextPath = "${applicationScope['TIMECTRL_CONTEXT']}";
	function onLoadPage() {
		try {
			loadCurrentVersion();
			
			loadCurrentMarks();
			loadOverdue();
			loadLastRead();
			loadOfflineMch();
			loadEmployeeWORut();
			/**	
			
*/
		} catch (e) {
			//alert(e);
		}
	}

	
//	CurrentMarksLabel
//	OverdueLabel
	
	
	function loadOverdue(){
		getData("LaterCount", "OverdueLabel");
	}
	
	function loadCurrentMarks(){
		getData("CurrentMarks", "CurrentMarksLabel");
	}
	
	function loadCurrentVersion() {
		getData("CurrentVersion", "CurrentVersionLabel", false);
	}

	function loadLastRead() {
		getData("LastRead", "LastReadLabel");
	}

	function loadEmployeeWORut(){
		getData("EmployeeWORut", "EmployeeWORutLabel");
	}
	
	function loadOfflineMch() {
		getData("OfflineMch", "OfflineMchLabel");
	}

	function getData(key, element) {
		getData(key, element, true);
	}
	
	function getData(key, element, sync) {
//		alert(contextPath + '/servlet/ajax/GetIndicator');
		$.ajax({
			type : "GET",
			cache : false,
			url : contextPath + '/servlet/ajax/GetIndicator',
			data : {
				Key : key
			},
			async : sync, /*was true*/
			success : function(data) {
				$('#' + element).text(data);
			},
			error : function(data, textStatus, xhr) {
				$('#' + element).text("Cancelado.");
			}
		});
	}
//-->
</script>

<div class="page-header">
	<h1>Estado del sistema</h1>
</div>

<div class="container-fluid">
	<div class="row">
		<div class="alert alert-info col-sm-4 col-md-offset-1" role="alert">
			Empleados asistentes: <b id="CurrentMarksLabel"><img
				src="${applicationScope['STATIC_CONTEXT']}<%=loadingIcon%>" /></b>
		</div>

		<div class="alert alert-info col-sm-4 col-md-offset-2" role="alert" >
			Atrasos: <b id="OverdueLabel"><img 
				src="${applicationScope['STATIC_CONTEXT']}<%=loadingIcon%>" /></b>
		</div>
	</div>


	<div class="row">
		<div class="alert alert-info col-sm-4 col-md-offset-1" role="alert">
			Última Lectura: <b id="LastReadLabel"><img
				src="${applicationScope['STATIC_CONTEXT']}<%=loadingIcon%>" /></b>
		</div>

		<div class="alert alert-info col-sm-4 col-md-offset-2" role="alert">
			Version Actual: <b id="CurrentVersionLabel"><img
				src="${applicationScope['STATIC_CONTEXT']}<%=loadingIcon%>" /></b>
		</div>
	</div>

	<div class="row">
		<div class="alert alert-info col-sm-4 col-md-offset-1" role="alert">
			Máquinas offline: <b id="OfflineMchLabel"><img
				src="${applicationScope['STATIC_CONTEXT']}<%=loadingIcon%>" /></b>
		</div>
		<div class="alert alert-info col-sm-4 col-md-offset-2" role="alert">
			Empleados sin Rut: <b id="EmployeeWORutLabel"><img
				src="${applicationScope['STATIC_CONTEXT']}<%=loadingIcon%>" /></b>
		</div>
	</div>
</div>
 
<%@ include file="/WEB-INF/jsp/common/footer2.jsp"%>
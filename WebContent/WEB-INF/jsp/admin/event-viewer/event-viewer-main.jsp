<%@page import="cl.buildersoft.framework.beans.EventBean"%>
<%@page import="cl.buildersoft.framework.beans.EventType"%>
<%@page import="java.util.Calendar"%>
<%@page import="cl.buildersoft.framework.util.BSDateTimeUtil"%>
<%@page import="cl.buildersoft.framework.beans.User"%>
<%@ include file="/WEB-INF/jsp/common/header2.jsp"%>
<%@ include file="/WEB-INF/jsp/common/menu2.jsp"%>
<%
	List<EventType> eventTypeList = (List<EventType>) request.getAttribute("EventTypeList");
	List<User> userList = (List<User>) request.getAttribute("UserList");
	List<EventBean> eventList = (List<EventBean>) request.getAttribute("EventList");

	Calendar startDate = (Calendar) request.getAttribute("StartDate");
	Calendar endDate = (Calendar) request.getAttribute("EndDate");

	Long eventTypeId = (Long) request.getAttribute("EventType");
	Long userId = (Long) request.getAttribute("UserId");

	String dateFormat = (String) request.getAttribute("DateFormat");
%>


<div class="page-header">
	<h1>Visor de eventos</h1>
</div>

<script type="text/javascript">
<!--
	function selectDateAtStart(selectedDate){
		$("#DEndDate").datepicker("option", "minDate", selectedDate);
	}
	
	function selectDateAtEnd(selectedDate){
		$("#DStartDate").datepicker("option", "maxDate", selectedDate);
	}
	
	function onLoadPage() {
		$("#DStartDate").datepicker({
			dateFormat : fixDateFormat(dateFormat),
			appendText : " (" + dateFormat.toLowerCase() + ")",
			changeMonth : true,
			numberOfMonths : 1,
			onSelect : selectDateAtStart
		});
		
		$("#DEndDate").datepicker({
			dateFormat : fixDateFormat(dateFormat),
			appendText : " (" + dateFormat.toLowerCase() + ")",
			defaultDate : "1d",
			changeMonth : true,
			numberOfMonths : 1,
			onSelect : selectDateAtEnd
		});

		$("#DStartDate").datepicker("setDate", "<%=BSDateTimeUtil.calendar2String(startDate, dateFormat)%>");
		$("#DEndDate").datepicker("setDate", "<%=BSDateTimeUtil.calendar2String(endDate, dateFormat)%>");
		selectDateAtStart("<%=BSDateTimeUtil.calendar2String(startDate, dateFormat)%>");
		selectDateAtEnd("<%=BSDateTimeUtil.calendar2String(endDate, dateFormat)%>");
	}
//-->
</script>

<form method="post" class="form-horizontal well"
	action="${pageContext.request.contextPath}/servlet/admin/eventViewer/EventViewerMain?<%=BSWeb.randomString() %>"
	id="QueryForm">


	<div class="form-group">
		<label class="control-label col-sm-2" for="DStartDate">Fecha
			inicio:</label>
		<div class="col-sm-4">
			<input type="text" id="DStartDate" Name="StartDate"
				placeholder="Fecha desde">
		</div>
		<label class="control-label col-sm-2" for="DEndDate">Fecha
			termino:</label>
		<div class="col-sm-4 ">
			<input type="text" id="DEndDate" Name="EndDate"
				placeholder="Fecha hasta">
		</div>
	</div>


	<div class="form-group">
			<label class="control-label col-sm-2" for="DEventType">Tipo de evento:</label>
			<div class="col-sm-4 ">
				<select Name="EventType" id="DEventType">
					<option value="">- Todos -</option>
					<%
						for (EventType eventType : eventTypeList) {
					%>
					<option value="<%=eventType.getId()%>" <%=eventType.getId().equals(eventTypeId)?"selected":""%>><%=eventType.getName()%></option>
					<%
						}
					%>
				</select>
			</div>
			<label class="control-label col-sm-2" for="DUser">Usuario:</label>
			<div class="col-sm-4 ">
				<select Name="User" id="DUser">
					<option value="">- Todos -</option>
					<%
						for (User user : userList) {
					%>
					<option value="<%=user.getId()%>" <%= user.getId().equals(userId)?"selected":""%>><%=user.getName()%></option>
					<%
						}
					%>
				</select>
			</div>
		</div>
		<div class="form-group">
			<div class="col-sm-2 col-sm-offset-10">
				<button class='btn btn-default' onclick="$('#QueryForm').submit()">Buscar</button>
			</div>
		</div>
	
</form>


<%
	if (eventList != null) {
%>
<div class="_col-sm-10 _col-sm-offset-1">
<table
	class="table table-striped table-bordered table-hover table-condensed table-responsive">
	<thead>
		<tr>
			<th>Fecha</th>
			<th>Hora</th>
			<th>Usuario</th>
			<th>Evento</th>
			<th>Detalle</th>
		</tr>
	</thead>
	<tbody>
		<%
			if (eventList != null) {
					for (EventBean event : eventList) {
		%>
		<tr>
			<td><%=BSDateTimeUtil.calendar2String(event.getWhen(), "dd-MM-yyyy")%></td>
			<td><%=BSDateTimeUtil.calendar2String(event.getWhen(), "HH:mm:ss")%></td>
			<td><%=event.getUserName()%> (<%=event.getUserMail()%>)</td>
			<td><%=event.getEventTypeName()%></td>
			<td><%=event.getWhat()%></td>
		</tr>
		<%
			}
				}
		%>
		
	</tbody>
</table>
</div>
<%
	}
%>

<%@ include file="/WEB-INF/jsp/common/footer2.jsp"%>


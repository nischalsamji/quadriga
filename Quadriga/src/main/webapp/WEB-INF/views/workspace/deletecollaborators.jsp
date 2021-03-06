<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>

<!-- Content -->
<script>
	$(document).ready(function() {
		activeTable = $('.dataTable').dataTable({
			"bJQueryUI" : true,
			"sPaginationType" : "full_numbers",
			"bAutoWidth" : false
		});
		$("#dlgConfirm").hide();
	});

	function submitClick(id) {
		location.href = '${pageContext.servletContext.contextPath}/auth/workbench/workspace/workspacedetails/${workspaceid}';
	}

	$(function() {

		$("input[name='Back']").button().click(function(event) {
			event.preventDefault();
		});

		$("input[name='deletecollab']").button().click(function(event) {
			if (!$("input[name='wscollabchecked']").is(":checked")) {
				$("#msg").text("Select collaborator to delete");
				event.preventDefault();
				return;
			}
		});

		$("input[name='deletecollab']").button().click(function(event) {
			if ($("input[name='wscollabchecked']").is(":checked")) {
				event.preventDefault();
				$("#dlgConfirm").dialog({
					resizable : false,
					height : 'auto',
					width : 350,
					modal : true,
					buttons : {
						Submit : function() {
							$(this).dialog("close");
							$("#deletewscollabform")[0].submit();
						},
						Cancel : function() {
							$(this).dialog("close");
						}
					}
				});
			}
		});

		$("input[name='selectall']").button().click(function(event) {
			$("input[name='wscollabchecked']").prop("checked", true);
			event.preventDefault();
			return;
		});

		$("input[name='deselectall']").button().click(function(event) {
			$("input[name='wscollabchecked']").prop("checked", false);
			event.preventDefault();
			return;
		});
	});
</script>
<article class="is-page-content">
	<form:form commandName="collaborator" method="POST"
		action="${pageContext.servletContext.contextPath}/auth/workbench/workspace/${workspaceid}/deletecollaborators"
		id="deletewscollabform">
		<c:if test="${not empty collaboratingusers}">
		<h2>Delete workspace collaborators</h2>
		    <h3>Workspace: ${workspacename}</h3>
            <div>${workspacedesc}</div>
            <hr>
			<span class="byline">Select the collaborators to be deleted:</span>
			<c:choose>
				<c:when test="${success=='1'}">
					<div id="msg" title="msg">
						<c:out value="${successmsg}"></c:out>
					</div>
					<br />
				</c:when>
			</c:choose>
			<span class="ui-state-error-text" id="msg" title="msg" style="color: #f00;">
			</span>
			<br />
			<input class="command" type="submit" value='Delete'
				name="deletecollab">
			<input type="button" value="Select All" name="selectall">
			<input type="button" value="DeSelect All" name="deselectall">
			<input type="submit" onClick="submitClick(this.id);"
					value='Cancel' name="Back">
			<table style="width: 100%" class="display dataTable"
				id="wscollablist">
				<thead>
					<tr>
						<th align="center" width="5%">Action</th>
						<th align="center" width="25%">Collaborator</th>
						<th align="center" width="70%">Roles</th>
					</tr>
				</thead>
				<tbody>
					<c:forEach var="workspaceCollaborator" items="${collaboratingusers}">
						<tr>
							<td><input type="checkbox" name="wscollabchecked"
								value="${workspaceCollaborator.collaborator.userObj.userName}"></td>
							<td><font size="3"> <c:out
										value="${workspaceCollaborator.collaborator.userObj.name}"></c:out>
							</font></td>
							<td><font size="3"> <c:forEach var="roles"
										items="${workspaceCollaborator.collaborator.collaboratorRoles}">
										<c:out value="${roles.displayName}"></c:out> ||
		 	</c:forEach>
							</font></td>
						</tr>
					</c:forEach>
				</tbody>
			</table>
			<input type="submit" value='Delete' name="deletecollab">
			<input type="button" value="Select All" name="selectall">
			<input type="button" value="DeSelect All" name="deselectall">
			<input type="submit" onClick="submitClick(this.id);"
					value='Cancel' name="Back">
		</c:if>
		<c:if test="${empty collaboratingusers}">
		<c:choose>
				<c:when test="${success=='1'}">
					<div id="msg" title="msg">
						<c:out value="${successmsg}"></c:out>
					</div>
					<br />
				</c:when>
			</c:choose>
			<span class="byline">You don't have any more collaborators associated to the workspace to delete.</span>
			<ul>
				<li><input type="submit" onClick="submitClick(this.id);"
					value='Okay' name="Back"></li>
			</ul>
		</c:if>
		<div id="dlgConfirm" title="Confirmation">Are you sure?</div>
	</form:form>
</article>





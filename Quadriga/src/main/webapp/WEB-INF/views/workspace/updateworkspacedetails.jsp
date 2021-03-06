<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>

<!-- Content -->
<script>

$(function() {
	$("input[type=submit]").button().click(function(event){
	});
	$("input[type=button]").button().click(function(event){
	});
});

function submitClick(id){
	location.href = "${pageContext.servletContext.contextPath}/auth/workbench/workspace/workspacedetails/${workspace.workspaceId}";
}
</script>
<article class="is-page-content">
	<form:form commandName="workspace" method="POST"
		action="${pageContext.servletContext.contextPath}/auth/workbench/workspace/updateworkspacedetails/${workspace.workspaceId}">
<c:choose>
<c:when test="${success == '0'}">
<header>
	<h2>Update Workspace</h2>
	<span class="byline">Please update the the following
		information:</span>
					<table style="width: 100%">
						<tr>
							<td style="width: 170px">Name:</td>
							<td><form:input path="workspaceName" size="60" id="workspaceName" /></td>
							<td><form:errors path="workspaceName" class="ui-state-error-text"></form:errors></td>
						</tr>
						<tr>
							<td style="vertical-align: top">Description:</td>
							<td><form:textarea path="description" cols="44" rows="6"
									id="description" /></td>
							<td><form:errors path="description" class="ui-state-error-text"></form:errors></td>
						</tr>
						<tr>
							<td><input type="submit" value="Update Workspace"></td>
							<td><input type="button" value="Cancel" 
			                onclick="location.href='${pageContext.servletContext.contextPath}/auth/workbench/workspace/workspacedetails/${workspace.workspaceId}'">
			                </td>
						</tr>
					</table>
				</header>
</c:when>
<c:when test="${success == '1'}">
<span class="byline">Workspace created successfully.</span>
	<ul>
					<li><input type="button" onClick="submitClick(this.workspaceId);"
						value='Okay'></li>
				</ul>>
</c:when>
</c:choose>
	</form:form>
</article>

<!-- /Content -->
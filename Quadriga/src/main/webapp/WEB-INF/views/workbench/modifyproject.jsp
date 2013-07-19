<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>

<!-- Content -->
<script>

$(document).ready(function(){
	  $("#unixName").keyup(function(event)
	  {
		var keyedInput = $("#unixName").val();
		var constant = "";
		if($.trim($("#unixName").val()))
			{
			   constant = $("#unixnameurl").val();
			}
	    $("#UnixURL").text(constant+keyedInput);
	  });
	});
	
	$(function() {
		$("input[type=submit]").button().click(function(event) {
		});
	});
</script>
<style>
.error {
	color: #ff0000;
	font-style: italic;
}
</style>
<header>
	<h2>Modify Project</h2>
	<span class="byline">Please fill in the following information:</span>
</header>

<article class="is-page-content">

	<form:form commandName="project" method="POST"
		action="/auth/workbench/modifyproject/${project.internalid}">
		<table style="width: 100%">
			<tr>
				<td style="width: 170px">Name:</td>
				<td><form:input path="name" size="60" id="name" /></td>
				<td><form:errors path="name" cssClass="error"></form:errors></td>
			</tr>
			<tr>
				<td style="vertical-align: top">Description:</td>
				<td><form:textarea path="description" cols="44" rows="6"
						id="description" /></td>
						<td><form:errors path="description" cssClass="error"></form:errors></td>
			</tr>
			<tr>
				<td>Project Public Access:</td>
				<td><form:select path="projectAccess">
						<form:options />
					</form:select>
					<td><form:errors path="projectAccess" cssClass="error"></form:errors></td>
			</tr>
			<tr>
				<td>Unix name:</td>
				<td><form:input path="unixName" size="60" id="unixName" /></td>
				<td><form:errors path="unixName" cssClass="error"></form:errors></td>
			</tr>
			<tr>
				<td></td>
				<td><div id="UnixURL"></div></td>
			</tr>
			<tr>
			<td><input class="command" type="submit" value="Modify Project"> </td>
			<td><input type="hidden" id="unixnameurl" value=<c:out value="${unixnameurl}"></c:out>/></td>
			</tr>
		</table>
	</form:form>

</article>

<!-- /Content -->
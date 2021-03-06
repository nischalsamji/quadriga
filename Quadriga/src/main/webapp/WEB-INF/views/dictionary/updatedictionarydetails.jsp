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
	location.href = "${pageContext.servletContext.contextPath}/auth/dictionaries/${dictionaryid}";
}
</script>
<style>
.error {
	color: #ff0000;
	font-style: italic;
}
</style>
<article class="is-page-content">
	<form:form commandName="dictionary" method="POST"
		action="${pageContext.servletContext.contextPath}/auth/dictionaries/updatedictionary/${dictionaryid}">
<c:choose>
<c:when test="${success == '0'}">
<header>
	<h2>Update Dictionary</h2>
	<span class="byline">Please update the the following
		information:</span>
					<table style="width: 100%">
						<tr>
							<td style="width: 170px">Name:</td>
							<td><form:input path="dictionaryName" size="60" id="dictionaryName" /></td>
							<td><form:errors path="dictionaryName" cssClass="error"></form:errors></td>
						</tr>
						<tr>
							<td style="vertical-align: top">Description:</td>
							<td><form:textarea path="description" cols="44" rows="6"
									id="description" /></td>
							<td><form:errors path="description" cssClass="error"></form:errors></td>
						</tr>
						<tr>
							<td><input type="submit" value="Update"></td>
						</tr>
					</table>
				</header>
</c:when>
<c:when test="${success == '1'}">
<span class="byline">Dictionary updated successfully.</span>
	<ul>
					<li><input type="button" onClick="submitClick(this.id);"
						value='Okay'></li>
				</ul>>
</c:when>
</c:choose>
	</form:form>
</article>

<!-- /Content -->
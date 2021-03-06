<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="sec"
	uri="http://www.springframework.org/security/tags"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<!-- Content -->

<header>
	<h2>User Management</h2>
	<span class="byline">Manage users and their permissions</span>
</header>

<article class="is-page-content">

	<script>
	<!-- Script for UI validation of user requests -->
		
		$(document).ready(function() {
			activeTable = $('.dataTable').dataTable({
				"bJQueryUI" : true,
				"sPaginationType" : "full_numbers",
				"bAutoWidth" : false
			});
		});

		function jqEnableAll(name, flag) {
			name = name.replace(/\./g,"\\.");
			if (flag == 1) {
				//Allow is selected. Enable user roles check boxes
				$("input." + name).removeAttr("disabled");

				//TODO: Check a default role when allow is selected

			} else {
				//Deny is selected. Uncheck all checkboxes and disable them
				$("input." + name).attr("checked", false);
				$("input." + name).attr("disabled", true);
			}
		}
		
	/* 	function submitClick(id){
			location.href = '${pageContext.servletContext.contextPath}/auth/users/updateroles';
		} */
		
		function submitClick(id) {
			var temp_id = id.replace(/\./g,"\\.");
			//Check if Allow or Deny is selected
			var selectedAccess = $(
					"input[type='radio'][name='" + temp_id + "']:checked").map(
					function() {
						return this.value;
					}).get();
			if (selectedAccess.length == 0) {
				$.alert("Please Approve/Deny the request", "Oops !!!");
				return;
			}

			//If Allow is selected, atleast one role should be selected
			var checkedVals = $('.' + temp_id + ':checkbox:checked').map(function() {
				return this.value;
			}).get();
			if (checkedVals.length == 0 && selectedAccess == 'approve') {
				$.alert("Please select atleast one role for the user",
						"Oops !!!");
				return;
			}

			//Create a path for the user to be passed to the Controller
			var path = id + "-" + selectedAccess + "-" + checkedVals.join("-");
			location.href = '${pageContext.servletContext.contextPath}/auth/users/access/' + path;
		} 
		
	</script>
	
		
    	<sec:authorize
				access="hasAnyRole('ROLE_QUADRIGA_USER_ADMIN')">
	<c:if test="${not empty userRequestsList}">
		<h3>User Requests to access Quadriga</h3>
		<c:forEach var="user" items="${userRequestsList}">
		User Name: <c:out value="${user.userName}"></c:out>
		<form:form commandName="approveAccount" method="POST"
        action="${pageContext.servletContext.contextPath}/auth/users/access/handleRequest">
        <form:input type="hidden" path="username" value="${user.userName}" />
        <form:radiobutton
                onclick="jqEnableAll('',1);"
                path="action" value="approve" />Approve&nbsp;&nbsp;
        <form:checkboxes items="${userRoles}" path="roles" itemLabel="displayName" itemValue="DBid" /> 
        <p>
        <form:radiobutton path="action"
                value="deny"
                onclick="jqEnableAll('',0);" />Deny 
        <br>
        <input type="submit" value="Submit" ></input>
        </form:form>
			
		</c:forEach>
	</c:if>
	
	
	<c:if test="${not empty activeUserList}">
		<h3>Current Active Users</h3>
		<a style="margin-bottom: 15px; " class="btn btn-warning" href='${pageContext.servletContext.contextPath}/auth/users/updateroles'>Update Roles</a>
    
       
		
		<table class="display dataTable" width="100%">
			<thead>
				<tr>
					<th>Username</th>
					<th>Admin</th>
					<th>Standard User</th>
					<th>Restricted User</th>
					<th>Collaborator</th>
					<th>Action</th>
				</tr>
			</thead>
			<tbody>
				<c:forEach var="user" items="${activeUserList}">
					<tr>
						<td width="20%"><font size="3"><c:out
									value="${user.userName}"></c:out></font></td>
						<td class="center">
						<c:set var="flag" value="0" />
						
						<c:forEach items="${user.quadrigaRoles}" var="role">
							
            					<c:if test="${role.name eq ('Quadriga Admin')}">
            						<c:set var="flag" value="1" scope="page"/>  
            					</c:if>
    					</c:forEach>
    					<c:if test="${flag==0}">No</c:if>
    					<c:if test="${flag==1}">Yes</c:if></td>
						<td class="center"><c:set var="flag" value="0" />
						
						<c:forEach items="${user.quadrigaRoles}" var="role">
							
            					<c:if test="${role.name eq ('Quadriga Standard User')}">
            						<c:set var="flag" value="1" scope="page"/>  
            					</c:if>
    					</c:forEach>
    					<c:if test="${flag==0}">No</c:if>
    					<c:if test="${flag==1}">Yes</c:if></td>
						<td class="center"><c:set var="flag" value="0" />
						
						<c:forEach items="${user.quadrigaRoles}" var="role">
							
            					<c:if test="${role.name eq ('Quadriga Restricted User')}">
            						<c:set var="flag" value="1" scope="page"/>  
            					</c:if>
    					</c:forEach>
    					<c:if test="${flag==0}">No</c:if>
    					<c:if test="${flag==1}">Yes</c:if></td>
						<td class="center"><c:set var="flag" value="0" />
						
						<c:forEach items="${user.quadrigaRoles}" var="role">
							
            					<c:if test="${role.name eq ('Quadriga Collaborating User')}">
            						<c:set var="flag" value="1" scope="page"/>  
            					</c:if>
    					</c:forEach>
    					<c:if test="${flag==0}">No</c:if>
    					<c:if test="${flag==1}">Yes</c:if></td>
						<td class="center"><font size="1"> <input
								type="submit"
								onclick="location.href='${pageContext.servletContext.contextPath}/auth/users/deactivate/${user.userName}'"
								value="Deactivate"></font></td>
					</tr>
				</c:forEach>
			</tbody>
			<tfoot>
				<tr>
					<th>Username</th>
					<th>Admin</th>
					<th>Standard User</th>
					<th>Restricted User</th>
					<th>Collaborator</th>
					<th>Action</th>
				</tr>
			</tfoot>
		</table>
	</c:if>

	<br />


	<c:if test="${not empty inactiveUserList}">
		<h3>Deactivated User Accounts</h3>
		<table cellpadding="0" cellspacing="0" border="0"
			class="display dataTable" width="100%">
			<thead>
				<tr>
					<th>Username</th>
					<th>Admin</th>
					<th>Standard User</th>
					<th>Restricted User</th>
					<th>Collaborator</th>
					<th>Action</th>
				</tr>
			</thead>
			<tbody>
				<c:forEach var="user" items="${inactiveUserList}">
					<tr>
						<td width="20%"><font size="3"><c:out
									value="${user.userName}"></c:out></font></td>
						<td class="center">
						<c:set var="flag" value="0" />
						
						<c:forEach items="${user.quadrigaRoles}" var="role">
							
            					<c:if test="${role.name eq ('Quadriga Admin')}">
            						<c:set var="flag" value="1" scope="page"/>  
            					</c:if>
    					</c:forEach>
    					<c:if test="${flag==0}">No</c:if>
    					<c:if test="${flag==1}">Yes</c:if>
    					</td>
						<td class="center">
						<c:set var="flag" value="0" />
						
						<c:forEach items="${user.quadrigaRoles}" var="role">
            					<c:if test="${role.name eq ('Quadriga Standard User')}">
            						<c:set var="flag" value="1" />  
            					</c:if>
    					</c:forEach>
    					<c:if test="${flag==0}">No</c:if>
    					<c:if test="${flag==1}">Yes</c:if></td>
						<td class="center">
						<c:set var="flag" value="0" />
						
						<c:forEach items="${user.quadrigaRoles}" var="role">
            					<c:if test="${role.name eq ('Quadriga Restricted User')}">
            						<c:set var="flag" value="1" />  
            					</c:if>
    					</c:forEach>
    					<c:if test="${flag==0}">No</c:if>
    					<c:if test="${flag==1}">Yes</c:if></td>
						<td class="center"><c:set var="flag" value="0" />
						
						<c:forEach items="${user.quadrigaRoles}" var="role">
            					<c:if test="${role.name eq ('Quadriga Collaborating User')}">
            						<c:set var="flag" value="1" />  
            					</c:if>
    					</c:forEach>
    					<c:if test="${flag==0}">No</c:if>
    					<c:if test="${flag==1}">Yes</c:if> </td>
						<td class="center"><font size="1"> <input
								type="submit"
								onclick="location.href='${pageContext.servletContext.contextPath}/auth/users/activate/${user.userName}'"
								value="Activate"></font></td>
					</tr>
				</c:forEach>
			</tbody>
			<tfoot>
				<tr>
					<th>Username</th>
					<th>Admin</th>
					<th>Standard User</th>
					<th>Restricted User</th>
					<th>Collaborator</th>
					<th>Action</th>
				</tr>
			</tfoot>
		</table>
	</c:if>
		<br />
	<c:if test="${not empty inactiveUserList}">
		<h3>Delete User Accounts</h3>
		<table class="display dataTable" style="width:100%">
			<thead>
				<tr>
					<th>Username</th>
					<th>Admin</th>
					<th>Standard User</th>
					<th>Restricted User</th>
					<th>Collaborator</th>
					<th>Action</th>
				</tr>
			</thead>
			<tbody>
				<c:forEach var="user" items="${inactiveUserList}">
					<tr>
						<td width="20%"><font size="3"><c:out
									value="${user.userName}"></c:out></font></td>
						<td class="center">
						<c:set var="flag" value="0" />
						
						<c:forEach items="${user.quadrigaRoles}" var="role">
							
            					<c:if test="${role.name eq ('Quadriga Admin')}">
            						<c:set var="flag" value="1" scope="page"/>  
            					</c:if>
    					</c:forEach>
    					<c:if test="${flag==0}">No</c:if>
    					<c:if test="${flag==1}">Yes</c:if></td>
						<td class="center"><c:set var="flag" value="0" />
						
						<c:forEach items="${user.quadrigaRoles}" var="role">
							
            					<c:if test="${role.name eq ('Quadriga Standard User')}">
            						<c:set var="flag" value="1" scope="page"/>  
            					</c:if>
    					</c:forEach>
    					<c:if test="${flag==0}">No</c:if>
    					<c:if test="${flag==1}">Yes</c:if></td>
						<td class="center"><c:set var="flag" value="0" />
						
						<c:forEach items="${user.quadrigaRoles}" var="role">
							
            					<c:if test="${role.name eq ('Quadriga Restricted User')}">
            						<c:set var="flag" value="1" scope="page"/>  
            					</c:if>
    					</c:forEach>
    					<c:if test="${flag==0}">No</c:if>
    					<c:if test="${flag==1}">Yes</c:if></td>
						<td class="center"><c:set var="flag" value="0" />
						
						<c:forEach items="${user.quadrigaRoles}" var="role">
							
            					<c:if test="${role.name eq ('Quadriga Collaborating User')}">
            						<c:set var="flag" value="1" scope="page"/>  
            					</c:if>
    					</c:forEach>
    					<c:if test="${flag==0}">No</c:if>
    					<c:if test="${flag==1}">Yes</c:if></td>
						<td class="center"> <input
								type="submit"
								onclick="location.href='${pageContext.servletContext.contextPath}/auth/users/delete/${user.userName}'"
								value="Delete"></font></td>
					</tr>
				</c:forEach>
			</tbody>
			<tfoot>
				<tr>
					<th>Username</th>
					<th>Admin</th>
					<th>Standard User</th>
					<th>Restricted User</th>
					<th>Collaborator</th>
					<th>Action</th>
				</tr>
			</tfoot>
		</table>
	</c:if>

</sec:authorize>
</article>

<!-- /Content -->

<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<link rel="stylesheet"
	href="${pageContext.servletContext.contextPath}/resources/txt-layout/css/style.min.css" />
<script
	src="${pageContext.servletContext.contextPath}/resources/txt-layout/js/jstree.min.js"></script>
<script type="text/javascript">
$(function () {
 $('#dictionarymenu').jstree().on("select_node.jstree", function (e, data) {
	   var i, j = [];
	   for(i = 0, j = data.selected.length; i < j; i++) {
		   $('#dictionarymenu').jstree().toggle_node(data.instance.get_node(data.selected[i]));
		}
     document.location = data.instance.get_node(data.node, true).children('a').attr('href');
 });
});
</script>
<h2 class="major">
	<span>Menu</span>
</h2>
<div id = "dictionarymenu">
<ul>
	<li data-jstree='{"icon":"/quadriga/resources/txt-layout/css/images/minus.png"}'>
	<a href="${pageContext.servletContext.contextPath}/auth/dictionaries/deleteDictionary">
			Delete Dictionary</a>
	</li>
</ul>
</div>

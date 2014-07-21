<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %> 
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE HTML>
<html lang="en-US">
<%@include file="header.jsp" %> 
<script type="text/javascript">
function showReportPage(pageURL){
	$.mobile.showPageLoadingMsg('b','数据加载中',false);
	window.location.href = pageURL;
}
</script>
<body onload="checkMessage('${message}')">
    <div style="position:absolute; left:-9999px;"><a href="#" id="setfoc"></a></div>
    <div data-role="page" id="home">
        <jsp:include page="page_header.jsp" flush="true">
        	<jsp:param name="title" value="当周上报进度"/>
        	<jsp:param name="basePath" value="<%=basePath%>"/>
        </jsp:include>
        <div data-role="content" data-theme="a">
        	<div data-role="fieldcontain" class="department_img_div">
	            <img alt="" src="<%=basePath%>images/img_bg_res_weekly.png" onclick="showReportPage('<%=basePath%>resprocess')" style="cursor: pointer;">
        	</div>
        	<div data-role="fieldcontain" class="department_img_div">
	            <img alt="" src="<%=basePath%>images/img_bg_ped_weekly.png" onclick="showReportPage('<%=basePath%>pedprocess')" style="cursor: pointer;">
        	</div>
        	<div data-role="fieldcontain" class="department_img_div">
	            <img alt="" src="<%=basePath%>images/img_bg_chestSurgery_weekly.png" onclick="showReportPage('<%=basePath%>cheprocess')" style="cursor: pointer;">
        	</div>
        </div>
        <jsp:include page="page_footer.jsp" flush="true">
        	<jsp:param name="backURL" value="dataQuery"/>
        	<jsp:param name="basePath" value="<%=basePath%>"/>
        </jsp:include>
    </div>
</body>
</html>
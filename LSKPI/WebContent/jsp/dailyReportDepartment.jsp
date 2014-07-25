<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %> 
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE HTML>
<html lang="en-US">
<%@include file="header.jsp" %> 
<script type="text/javascript">
function showDailyReportPage(pageURL){
	$.mobile.showPageLoadingMsg('b','数据加载中',false);
	window.location.href = pageURL;
}
</script>
<body onload="checkMessage('${message}')">
    <div style="position:absolute; left:-9999px;"><a href="#" id="setfoc"></a></div>
    <div data-role="page" id="home">
        <jsp:include page="page_header.jsp" flush="true">
        	<jsp:param name="title" value="每日报告"/>
        	<jsp:param name="basePath" value="<%=basePath%>"/>
        </jsp:include>
        <div data-role="content" data-theme="a">
        <%--
        	<div data-role="fieldcontain" class="department_img_div">
	            <img alt="" src="<%=basePath%>images/img_bg_res_daily.png" onclick="showDailyReportPage('<%=basePath%>resdailyreport')" style="cursor: pointer;">
        	</div>
         --%>
        	<div data-role="fieldcontain" class="department_img_div">
	            <img alt="" src="<%=basePath%>images/img_bg_ped_daily.png" onclick="showDailyReportPage('<%=basePath%>peddailyreport')" style="cursor: pointer;">
        	</div>
        	<div data-role="fieldcontain" class="department_img_div">
	            <img alt="" src="<%=basePath%>images/img_bg_chestSurgery_daily.png" onclick="showDailyReportPage('<%=basePath%>chestSurgeryDailyReport')" style="cursor: pointer;">
        	</div>
        </div>
        <jsp:include page="page_footer.jsp" flush="true">
        	<jsp:param name="backURL" value="index"/>
        	<jsp:param name="basePath" value="<%=basePath%>"/>
        </jsp:include>
    </div>
</body>
</html>
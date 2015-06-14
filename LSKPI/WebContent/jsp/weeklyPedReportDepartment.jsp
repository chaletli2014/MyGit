<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %> 
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE HTML>
<html lang="en-US">
<%@include file="header.jsp" %>
<script type="text/javascript">
function showNextPage(pageURL){
    $.mobile.showPageLoadingMsg('b','数据加载中',false);
    window.location.href = pageURL;
}
</script>
<body onload="checkMessage('${message}')">
    <div style="position:absolute; left:-9999px;"><a href="#" id="setfoc"></a></div>
    <div data-role="page" id="home">
        <jsp:include page="page_header.jsp" flush="true">
        	<jsp:param name="title" value="儿科每周报告"/>
        	<jsp:param name="basePath" value="<%=basePath%>"/>
        </jsp:include>
        <div data-role="content" data-theme="a">
        	<div data-role="fieldcontain" class="department_img_div">
	            <img alt="" src="<%=basePath%>images/img_bg_ped_emerging_weekly.png" onclick="showNextPage('<%=basePath%>pedWeeklyreport')" style="cursor: pointer;">
        	</div>
        	<div data-role="fieldcontain" class="department_img_div">
	            <img alt="" src="<%=basePath%>images/img_bg_ped_room_weekly.png" onclick="showNextPage('<%=basePath%>resWeeklyreport')" style="cursor: pointer;">
        	</div>
        </div>
        <div data-role="footer" data-theme="a" style="background:#1C7DBE" data-position="fixed">
		    <div class="ui-grid-a index_footer">
		        <div class="ui-block-a">
		        	<img alt="" src="<%=basePath%>images/footer_back.png" style="cursor: pointer;" onclick="showNextPage('<%=basePath%>weeklyreport')"/>
		        </div>
		        <div class="ui-block-b" style="text-align: right;">
		            <img alt="" src="<%=basePath%>images/footer_logo.png"/>
		        </div>
		    </div>
		</div>
    </div>
</body>
</html>
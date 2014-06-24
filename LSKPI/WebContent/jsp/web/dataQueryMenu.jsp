<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %> 
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE HTML>
<html lang="en-US">
<%@include file="header.jsp" %> 
<script type="text/javascript">
function loadLSData(pageURL){
    $.mobile.showPageLoadingMsg('b','数据加载中',false);
    window.location.href = pageURL;
}
</script>
<body>
    <div style="position:absolute; left:-9999px;"><a href="#" id="setfoc"></a></div>
    <div data-role="page" id="home">
        <jsp:include page="page_header.jsp" flush="true">
        	<jsp:param name="title" value="数据查询"/>
        	<jsp:param name="basePath" value="<%=basePath%>"/>
        </jsp:include>
        <div data-role="content" data-theme="a">
        	<div class="ui-grid-a">
                <div class="ui-block-a">
		        	<div data-role="fieldcontain" class="department_img_div">
			            <img alt="" src="<%=basePath%>images/img_bg_datadownload_w.png" onclick="loadLSData('<%=basePath%>showWebUploadData')" style="cursor: pointer;">
		        	</div>
		        	<div data-role="fieldcontain" class="department_img_div">
			            <img alt="" src="<%=basePath%>images/report_process_w.png" onclick="loadLSData('<%=basePath%>reportProcess')" style="cursor: pointer;">
		        	</div>
		        	<div data-role="fieldcontain" class="department_img_div">
			            <img alt="" src="<%=basePath%>images/img_bg_monthly_query_w.png" onclick="loadLSData('<%=basePath%>monthlyQuery')" style="cursor: pointer;">
		        	</div>
                </div>
                <div class="ui-block-b">
		        	<div data-role="fieldcontain" class="department_img_div">
			            <img alt="" src="<%=basePath%>images/ratio_lastweek_w.png" onclick="loadLSData('<%=basePath%>ratioLastweek')" style="cursor: pointer;">
		        	</div>
		        	<div data-role="fieldcontain" class="department_img_div">
			            <img alt="" src="<%=basePath%>images/img_bg_hosNameQuery_w.png" onclick="loadLSData('<%=basePath%>hospitalQuery')" style="cursor: pointer;">
		        	</div>
		        	<div data-role="fieldcontain" class="department_img_div">
			            <img alt="" src="<%=basePath%>images/img_bg_hosSaleQuery_w.png" onclick="loadLSData('<%=basePath%>hospitalSalesQuery')" style="cursor: pointer;">
		        	</div>
                </div>
	        </div>
        </div>
        <jsp:include page="page_footer.jsp" flush="true">
        	<jsp:param name="backURL" value="index"/>
        	<jsp:param name="basePath" value="<%=basePath%>"/>
        </jsp:include>
    </div>
</body>
</html>
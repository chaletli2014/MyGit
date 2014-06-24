<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %> 
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE HTML>
<html lang="en-US">
<%@include file="header.jsp" %> 
<body onload="checkMessage('${message}')">
    <div style="position:absolute; left:-9999px;"><a href="#" id="setfoc"></a></div>
    <div data-role="page" id="home">
        <jsp:include page="page_header.jsp" flush="true">
        	<jsp:param name="title" value="家庭雾化数据采集"/>
        	<jsp:param name="basePath" value="<%=basePath%>"/>
        </jsp:include>
        <div data-role="content" data-theme="a">
        	<div data-role="fieldcontain" class="department_img_div">
        	   <a onclick="javascript:window.location.href='<%=basePath%>collecthomedata'">每周数据提交</a>
        	   <!-- 
	            <img alt="" src="<%=basePath%>images/img_bg_home_collect_w.png" onclick="javascript:window.location.href='<%=basePath%>collecthomedata'" style="cursor: pointer;">
        	    -->
        	</div>
        	<div data-role="fieldcontain" class="department_img_div">
        	<a onclick="javascript:window.location.href='<%=basePath%>doctormaintenance'">客户列表维护</a>
        	<!-- 
	            <img alt="" src="<%=basePath%>images/img_bg_doctor_w.png" onclick="javascript:window.location.href='<%=basePath%>doctormaintenance'" style="cursor: pointer;">
        	 -->
        	</div>
        </div>
        <jsp:include page="page_footer.jsp" flush="true">
        	<jsp:param name="backURL" value="collectData"/>
        	<jsp:param name="basePath" value="<%=basePath%>"/>
        </jsp:include>
    </div>
</body>
</html>
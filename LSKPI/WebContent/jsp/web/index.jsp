<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %> 
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE HTML>
<html lang="en-US">
<%@include file="header.jsp" %>    
<body onload="checkMessage('${message}')">
    <div style="position:absolute; left:-9999px;"><a href="#" id="setfoc"></a></div>
    <div data-role="page" id="home" data-theme="a">
        <jsp:include page="page_header.jsp" flush="true">
        	<jsp:param name="title" value="销售数据采集及报表系统"/>
        	<jsp:param name="basePath" value="<%=basePath%>"/>
        </jsp:include>
        <div data-role="content" data-theme="a">
            <div class="ui-grid-a ls-grid-img">
                <div class="ui-block-a ls-grid-img-a">
                    <img alt="" src="<%=basePath%>images/img_bg_1_w.png" onclick="javascript:window.location.href='<%=basePath%>collectData'" style="cursor: pointer;">
                </div>
                <div class="ui-block-b">
                   <img alt="" src="<%=basePath%>images/img_bg_2_w.png" onclick="javascript:window.location.href='<%=basePath%>dailyReport'" style="cursor: pointer;">
                </div>
            </div>
            <div class="ui-grid-a ls-grid-img">
                <div class="ui-block-a ls-grid-img-a">
                    <img alt="" src="<%=basePath%>images/img_bg_3_w.png" onclick="javascript:window.location.href='<%=basePath%>weeklyreport'" style="cursor: pointer;">
                </div>
                <div class="ui-block-b">
                    <img alt="" src="<%=basePath%>images/img_bg_4_w.png" onclick="javascript:window.location.href='<%=basePath%>dataQuery'" style="cursor: pointer;">
                </div>
            </div>
        </div>
        <jsp:include page="page_footer.jsp" flush="true">
            <jsp:param value="<%=basePath%>" name="basePath"/>
            <jsp:param value="index" name="currentPage"/>
        </jsp:include>
    </div>
</body>  
</html>  
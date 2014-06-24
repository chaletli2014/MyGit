<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %> 
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE HTML>
<html lang="en-US">
<%@include file="header.jsp" %> 
<body>
    <div style="position:absolute; left:-9999px;"><a href="#" id="setfoc"></a></div>
    <div data-role="page" id="home">
        <div data-role="header" data-position=”fixed”>
            <a href="index" data-role="button" rel="external">返回</a><h1>本周上报情况</h1>
        </div>
        <div data-role="content">
	        <div class="ui-grid-a">
			    <div class="ui-block-a weeklyInput"><strong>医院名称</strong></div>
				<div class="ui-block-b weeklyInput"><strong>本周上报数</strong></div>
			</div>
			<c:forEach items="${weeklyData}" var="dataItem">
			<div class="ui-grid-a">
		        <div class="ui-block-a weeklyInput">${dataItem.hospital}</div>
		        <div class="ui-block-b weeklyInput">${dataItem.num}</div>
		    </div>
			</c:forEach>
        </div>
    </div>
</body>  
</html>  
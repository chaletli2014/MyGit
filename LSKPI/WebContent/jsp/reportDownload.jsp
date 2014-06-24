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
	    <div data-role="fieldcontain">
	        <div data-role="header" data-position=”fixed”>
	            <h1>数据下载</h1>
	        </div>
	        <div data-role="content">
	           <a href="${reportFileURL}">日报下载</a>
	            <button type="submit" onclick="javascript:$.mobile.showPageLoadingMsg('b','文件上传中',false);">提交</button>
	        </div>
	    </div>
    </div>
</body>  
</html>  
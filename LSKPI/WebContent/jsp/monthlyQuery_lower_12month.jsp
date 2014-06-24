<%@page import="com.chalet.lskpi.utils.LsAttributes"%>
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
            <jsp:param name="title" value="销售袋数历史"/>
            <jsp:param name="basePath" value="<%=basePath%>"/>
        </jsp:include>
        <div data-role="content"  data-theme="a">
            <div class="roundCorner" style="padding:4px;">
                <div class="dailyReport_table_Title">${monthlyDataTitle}</div>
                <table class="mobileReport_table">
                    <tr class="mobileReport_table_header">
                        <td width="16%">月份</td>
                        <td width="15%">医院数</td>
                        <td width="15%">上报数</td>
                        <td width="14%">儿科门急诊</td>
                        <td width="14%">儿科病房</td>
                        <td width="14%">呼吸科</td>
                        <td width="14%">其他科室</td>
                        <td width="14%">合计</td>
                    </tr>
                    <c:forEach items="${monthly12Datas}" var="monthly12Data">
	                    <tr class="mobileReport_table_body">
	                        <td>${monthly12Data.dataMonth}</td>
	                        <td><fmt:formatNumber value="${monthly12Data.hosNum}" pattern="#,###"/></td>
	                        <td><fmt:formatNumber value="${monthly12Data.inNum}" pattern="#,###"/></td>
	                        <td><fmt:formatNumber value="${monthly12Data.pedemernum}" pattern="#,###"/></td>
	                        <td><fmt:formatNumber value="${monthly12Data.pedroomnum}" pattern="#,###"/></td>
	                        <td><fmt:formatNumber value="${monthly12Data.resnum}" pattern="#,###"/></td>
	                        <td><fmt:formatNumber value="${monthly12Data.othernum}" pattern="#,###"/></td>
	                        <td><fmt:formatNumber value="${monthly12Data.totalnum}" pattern="#,###"/></td>
	                    </tr>
                    </c:forEach>
                </table>
            </div>
        </div>
        <jsp:include page="page_footer.jsp">
            <jsp:param value="<%=basePath%>" name="basePath"/>
            <jsp:param value="monthlyQuery" name="backURL"/>
        </jsp:include>
    </div>
</body>  
</html>  
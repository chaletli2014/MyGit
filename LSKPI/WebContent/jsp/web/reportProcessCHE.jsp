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
        	<jsp:param name="title" value="胸外科当周上报进度"/>
        	<jsp:param name="basePath" value="<%=basePath%>"/>
        </jsp:include>
        <div data-role="content" data-theme="a">
        	<div class="roundCorner" style="padding:4px;">
        	<div class="report_process_bg_description">每家考评医院每周上报至少3次</div>
	            <table class="mobileReport_table">
	               <tr class="mobileReport_table_header">
                       <td width="15%">负责医院数</td>
                       <td width="15%">有效上报次数</td>
                       <td width="15%">当前上报率</td>
	               </tr>
	               <tr class="mobileReport_table_body">
	                   <td class="report_data_number">${processData.hosNum}</td>
	                   <td class="report_data_number"><fmt:formatNumber value="${processData.validInNum}" pattern="#0"/></td>
	                   <td class="report_data_number"><fmt:formatNumber type="percent" value="${processData.currentInRate}" pattern="#0.00%"/></td>
	               </tr>
	            </table>
	            <c:if test="${'RSM' == currentUser.level}">
		            <table class="mobileReport_table">
		               <tr class="mobileReport_table_header">
	                       <td width="15%">DSM姓名</td>
	                       <td width="15%">负责医院数</td>
	                       <td width="15%">有效上报次数</td>
	                       <td width="15%">当前上报率</td>
		               </tr>
		               <c:forEach items="${processChildData}" var="processData" varStatus="status">
			               <tr class="mobileReport_table_body <c:if test="${status.count%2==0}">mobileReport_tr_even</c:if>">
			                   <td class="report_data_number">${processData.name}</td>
			                   <td class="report_data_number">${processData.hosNum}</td>
			                   <td class="report_data_number"><fmt:formatNumber value="${processData.validInNum}" pattern="#0"/></td>
			                   <td class="report_data_number"><fmt:formatNumber type="percent" value="${processData.currentInRate}" pattern="#0.00%"/></td>
			               </tr>
		               </c:forEach>
		            </table>
	            </c:if>
            </div>
        	<div class="roundCorner" style="padding:4px;">
        	   <div class="report_process_bg_description">背景色为<span class="report_process_bg_span">&nbsp;&nbsp;&nbsp;&nbsp;</span>的医院表示在统计列表中</div>
	            <table class="mobileReport_table">
	               <tr class="mobileReport_table_header">
                       <td width="15%">医院</td>
                       <td width="15%">上报次数</td>
                       <td width="15%">负责销售</td>
	               </tr>
	               <c:forEach items="${processDataDetail}" var="processDataDetailItem" varStatus="status">
	               <tr class="mobileReport_table_body <c:if test="${processDataDetailItem.isAssessed=='1'}">mobileReport_tr_even</c:if>">
	                   <td class="report_data_number">${processDataDetailItem.hospitalName}</td>
	                   <td class="report_data_number"><fmt:formatNumber value="${processDataDetailItem.inNum}" pattern="#0"/></td>
	                   <td class="report_data_number">${processDataDetailItem.saleName}</td>
	               </tr>
	               </c:forEach>
	            </table>
            </div>
        </div>
        <jsp:include page="page_footer.jsp" flush="true">
        	<jsp:param name="backURL" value="reportProcess"/>
        	<jsp:param name="basePath" value="<%=basePath%>"/>
        </jsp:include>
    </div>
</body>
</html>
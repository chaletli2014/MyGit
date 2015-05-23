<%@page import="com.chalet.lskpi.utils.LsAttributes"%>
<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %> 
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE HTML>
<html lang="en-US">
<%@include file="header.jsp" %> 
<script type="text/javascript">
</script>
<body onload="checkMessage('${message}')">
    <div style="position:absolute; left:-9999px;"><a href="#" id="setfoc"></a></div>
    <div data-role="page" id="home">
        <jsp:include page="page_header.jsp" flush="true">
            <jsp:param name="title" value="儿科住院每日报告"/>
            <jsp:param name="basePath" value="<%=basePath%>"/>
        </jsp:include>
        <div data-role="content"  data-theme="a">
            <div class="roundCorner" style="padding:4px;">
                <div class="dailyReport_table_Title">${selfTitle}</div>
                <div class="dailyReport_table_Title">病房雾化</div>
	            <table class="mobileReport_table">
	               <tr class="mobileReport_table_header">
	                   <td width="10%">姓名</td>
                       <td width="10%">医院数</td>
                       <td width="10%">上报率</td>
                       <td width="10%">住院人数</td>
                       <td width="10%">雾化人次</td>
                       <td width="10%">雾化率</td>
                       <td width="10%">天数</td>
                       <td width="10%">平均剂量</td>
                       <td width="10%">雾化博雾人次</td>
                       <td width="10%">博令人次比</td>
	               </tr>
	               <c:forEach items="${mobileDailyReportData}" var="reportData" varStatus="status">
		               <tr class="mobileReport_table_body <c:if test="${status.count%2==0}">mobileReport_tr_even</c:if>">
		                   <td>${reportData.userName}</td>
		                   <td class="report_data_number"><fmt:formatNumber value="${reportData.hosNum}" pattern="#,###"/></td>
		                   <td class="report_data_number"><fmt:formatNumber type="percent" value="${reportData.inRate}" pattern="#0%"/></td>
		                   <td class="report_data_number"><fmt:formatNumber value="${reportData.patNumRoom}" pattern="#,###"/></td>
		                   <td class="report_data_number"><fmt:formatNumber value="${reportData.lsNumRoom}" pattern="#,###"/></td>
		                   <td class="report_data_number"><fmt:formatNumber type="percent" value="${reportData.whRateRoom}" pattern="#0%"/></td>
		                   <td class="report_data_number"><fmt:formatNumber value="${reportData.whdaysRoom}" pattern="#0.00"/></td>
		                   <td class="report_data_number"><fmt:formatNumber value="${reportData.averageDoseRoom}" pattern="#0.00"/></td>
		                   <td class="report_data_number"><fmt:formatNumber value="${reportData.whbwnumRoom}" pattern="#,###"/></td>
		                   <td class="report_data_number"><fmt:formatNumber type="percent" value="${reportData.blRateRoom}" pattern="#0%"/></td>
		               </tr>
	               </c:forEach>
	               <c:if test="${mobileDailyReportParentData!= null }">
		               <tr class="mobileReport_table_body <c:if test="${fn:length(mobileDailyReportData)%2 != 0}">mobileReport_tr_even</c:if>">
		                   <td rowspan="2">${mobileDailyReportParentData.userName}</td>
		                   <td class="report_data_number"><fmt:formatNumber value="${mobileDailyReportParentData.hosNum}" pattern="#,###"/></td>
		                   <td class="report_data_number"><fmt:formatNumber type="percent" value="${mobileDailyReportParentData.inRate}" pattern="#0%"/></td>
		                   <td class="report_data_number"><fmt:formatNumber value="${mobileDailyReportParentData.patNumRoom}" pattern="#,###"/></td>
		                   <td class="report_data_number"><fmt:formatNumber value="${mobileDailyReportParentData.lsNumRoom}" pattern="#,###"/></td>
		                   <td class="report_data_number"><fmt:formatNumber type="percent" value="${mobileDailyReportParentData.whRateRoom}" pattern="#0%"/></td>
		                   <td class="report_data_number"><fmt:formatNumber value="${mobileDailyReportParentData.whdaysRoom}" pattern="#0.00"/></td>
		                   <td class="report_data_number"><fmt:formatNumber value="${mobileDailyReportParentData.averageDoseRoom}" pattern="#0.00"/></td>
		                   <td class="report_data_number"><fmt:formatNumber value="${mobileDailyReportParentData.whbwnumRoom}" pattern="#,###"/></td>
		                   <td class="report_data_number"><fmt:formatNumber type="percent" value="${mobileDailyReportParentData.blRateRoom}" pattern="#0%"/></td>
		               </tr>
	               </c:if>
	            </table>
	            
	            <div class="dailyReport_table_Title">病房家庭雾化</div>
	            <table class="mobileReport_table">
	               <tr class="mobileReport_table_header">
	                   <td width="40%">姓名</td>
                       <td width="15%">赠卖泵数量</td>
                       <td width="15%">带药人数</td>
                       <td width="15%">平均带药天数</td>
                       <td width="15%">总带药支数</td>
	               </tr>
	               <c:forEach items="${mobileDailyReportData}" var="reportData" varStatus="status">
		               <tr class="mobileReport_table_body <c:if test="${status.count%2==0}">mobileReport_tr_even</c:if>">
		                   <td>${reportData.userName}</td>
		                   <td class="report_data_number"><fmt:formatNumber value="${reportData.homeWhRoomNum1}" pattern="#,###"/></td>
		                   <td class="report_data_number"><fmt:formatNumber value="${reportData.homeWhRoomNum2}" pattern="#,###"/></td>
		                   <td class="report_data_number"><fmt:formatNumber value="${reportData.homeWhRoomAverDays}" pattern="#0.0"/></td>
		                   <td class="report_data_number"><fmt:formatNumber value="${reportData.homeWhRoomNum4}" pattern="#,###"/></td>
		               </tr>
	               </c:forEach>
	               <c:if test="${mobileDailyReportParentData!= null }">
		               <tr class="mobileReport_table_body <c:if test="${fn:length(mobileDailyReportData)%2 != 0}">mobileReport_tr_even</c:if>">
		                   <td rowspan="2">${mobileDailyReportParentData.userName}</td>
		                   <td class="report_data_number"><fmt:formatNumber value="${mobileDailyReportParentData.homeWhRoomNum1}" pattern="#,###"/></td>
		                   <td class="report_data_number"><fmt:formatNumber value="${mobileDailyReportParentData.homeWhRoomNum2}" pattern="#,###"/></td>
		                   <td class="report_data_number"><fmt:formatNumber value="${mobileDailyReportParentData.homeWhRoomAverDays}" pattern="#0.0"/></td>
		                   <td class="report_data_number"><fmt:formatNumber value="${mobileDailyReportParentData.homeWhRoomNum4}" pattern="#,###"/></td>
		               </tr>
	               </c:if>
	            </table>
            </div>
            <c:if test="${operatorObj!= null && operatorObj.level=='BM'}">
            <c:forEach items="${mobileDailyReportAllRSMData}" var="rsmReportData">
	            <div class="roundCorner" style="padding: 4px;">
					<div class="dailyReport_table_Title">所属[${rsmReportData[0].regionCenterCN}${titleSuffix}</div>
					
					<div class="dailyReport_table_Title">病房雾化</div>
					<table class="mobileReport_table">
						<tr class="mobileReport_table_header">
							<td width="10%">姓名</td>
	                       	<td width="10%">医院数</td>
	                       	<td width="10%">上报率</td>
	                       	<td width="10%">住院人数</td>
	                       	<td width="10%">雾化人次</td>
	                       	<td width="10%">雾化率</td>
	                       	<td width="10%">天数</td>
	                       	<td width="10%">平均剂量</td>
	                       	<td width="10%">雾化博雾人次</td>
	                       	<td width="10%">博令人次比</td>
						</tr>
						<c:forEach items="${rsmReportData}" var="reportData" varStatus="status">
							<tr	class="mobileReport_table_body <c:if test="${status.count%2==0}">mobileReport_tr_even</c:if>">
								<td>${reportData.userName}</td>
								<td class="report_data_number"><fmt:formatNumber value="${reportData.hosNum}" pattern="#,###"/></td>
			                   	<td class="report_data_number"><fmt:formatNumber type="percent" value="${reportData.inRate}" pattern="#0%"/></td>
			                   	<td class="report_data_number"><fmt:formatNumber value="${reportData.patNumRoom}" pattern="#,###"/></td>
			                   	<td class="report_data_number"><fmt:formatNumber value="${reportData.lsNumRoom}" pattern="#,###"/></td>
			                   	<td class="report_data_number"><fmt:formatNumber type="percent" value="${reportData.whRateRoom}" pattern="#0%"/></td>
			                   	<td class="report_data_number"><fmt:formatNumber value="${reportData.whdaysRoom}" pattern="#0.00"/></td>
			                   	<td class="report_data_number"><fmt:formatNumber value="${reportData.averageDoseRoom}" pattern="#0.00"/></td>
			                   	<td class="report_data_number"><fmt:formatNumber value="${reportData.whbwnumRoom}" pattern="#,###"/></td>
			                   	<td class="report_data_number"><fmt:formatNumber type="percent" value="${reportData.blRateRoom}" pattern="#0%"/></td>
							</tr>
						</c:forEach>
					</table>
					
					<div class="dailyReport_table_Title">病房家庭雾化</div>
		            <table class="mobileReport_table">
		               <tr class="mobileReport_table_header">
		                   <td width="40%">姓名</td>
	                       <td width="15%">赠卖泵数量</td>
	                       <td width="15%">带药人数</td>
	                       <td width="15%">平均带药天数</td>
	                       <td width="15%">总带药支数</td>
		               </tr>
		               <c:forEach items="${rsmReportData}" var="reportData" varStatus="status">
			               <tr class="mobileReport_table_body <c:if test="${status.count%2==0}">mobileReport_tr_even</c:if>">
			                   <td>${reportData.userName}</td>
			                   <td class="report_data_number"><fmt:formatNumber value="${reportData.homeWhRoomNum1}" pattern="#,###"/></td>
			                   <td class="report_data_number"><fmt:formatNumber value="${reportData.homeWhRoomNum2}" pattern="#,###"/></td>
			                   <td class="report_data_number"><fmt:formatNumber value="${reportData.homeWhRoomAverDays}" pattern="#0.0"/></td>
			                   <td class="report_data_number"><fmt:formatNumber value="${reportData.homeWhRoomNum4}" pattern="#,###"/></td>
			               </tr>
		               </c:forEach>
		            </table>
				</div>
            </c:forEach>
            </c:if>
            <c:if test="${mobileDailyReportChildData!= null && fn:length(mobileDailyReportChildData) > 0}">
            <div class="roundCorner" style="padding: 4px;">
				<div class="dailyReport_table_Title">${childTitle}</div>
				
				<div class="dailyReport_table_Title">病房雾化</div>
				<table class="mobileReport_table">
					<tr class="mobileReport_table_header">
						<td width="10%">姓名</td>
                       	<td width="10%">医院数</td>
                       	<td width="10%">上报率</td>
                       	<td width="10%">住院人数</td>
                       	<td width="10%">雾化人次</td>
                       	<td width="10%">雾化率</td>
                       	<td width="10%">天数</td>
                       	<td width="10%">平均剂量</td>
                       	<td width="10%">雾化博雾人次</td>
                       	<td width="10%">博令人次比</td>
					</tr>
					<c:forEach items="${mobileDailyReportChildData}" var="reportData" varStatus="status">
						<tr	class="mobileReport_table_body <c:if test="${status.count%2==0}">mobileReport_tr_even</c:if>">
							<td>${reportData.userName}</td>
							<td class="report_data_number"><fmt:formatNumber value="${reportData.hosNum}" pattern="#,###"/></td>
							<td class="report_data_number"><fmt:formatNumber type="percent" value="${reportData.inRate}" pattern="#0%"/></td>
		                   	<td class="report_data_number"><fmt:formatNumber value="${reportData.patNumRoom}" pattern="#,###"/></td>
		                   	<td class="report_data_number"><fmt:formatNumber value="${reportData.lsNumRoom}" pattern="#,###"/></td>
		                   	<td class="report_data_number"><fmt:formatNumber type="percent" value="${reportData.whRateRoom}" pattern="#0%"/></td>
		                   	<td class="report_data_number"><fmt:formatNumber value="${reportData.whdaysRoom}" pattern="#0.00"/></td>
		                   	<td class="report_data_number"><fmt:formatNumber value="${reportData.averageDoseRoom}" pattern="#0.00"/></td>
		                   	<td class="report_data_number"><fmt:formatNumber value="${reportData.whbwnumRoom}" pattern="#,###"/></td>
		                   	<td class="report_data_number"><fmt:formatNumber type="percent" value="${reportData.blRateRoom}" pattern="#0%"/></td>
						</tr>
					</c:forEach>
				</table>
				
				<div class="dailyReport_table_Title">病房家庭雾化</div>
		            <table class="mobileReport_table">
		               <tr class="mobileReport_table_header">
		                   <td width="40%">姓名</td>
	                       <td width="15%">赠卖泵数量</td>
	                       <td width="15%">带药人数</td>
	                       <td width="15%">平均带药天数</td>
	                       <td width="15%">总带药支数</td>
		               </tr>
		               <c:forEach items="${mobileDailyReportChildData}" var="reportData" varStatus="status">
			               <tr class="mobileReport_table_body <c:if test="${status.count%2==0}">mobileReport_tr_even</c:if>">
			                   <td>${reportData.userName}</td>
			                   <td class="report_data_number"><fmt:formatNumber value="${reportData.homeWhRoomNum1}" pattern="#,###"/></td>
			                   <td class="report_data_number"><fmt:formatNumber value="${reportData.homeWhRoomNum2}" pattern="#,###"/></td>
			                   <td class="report_data_number"><fmt:formatNumber value="${reportData.homeWhRoomAverDays}" pattern="#0.0"/></td>
			                   <td class="report_data_number"><fmt:formatNumber value="${reportData.homeWhRoomNum4}" pattern="#,###"/></td>
			               </tr>
		               </c:forEach>
		            </table>
			</div>
            </c:if>
            <%--
            <c:if test="${operatorObj!= null && (operatorObj.level=='RSM'||operatorObj.level=='RSD'||operatorObj.level=='BM')}">
	            <div class="roundCorner" style="padding:4px;">
	                <div class="dailyReport_table_Title">全国RSM数据排行</div>
	                <table class="mobileReport_table">
	                   <tr class="mobileReport_table_header">
	                       <td width="30%">指标</td>
	                       <td width="35%" colspan="2">Top RSM</td>
	                       <td width="35%" colspan="2">Bottom RSM</td>
	                   </tr>
		               <tr class="mobileReport_table_body">
		                   <td>上报率</td>
		                   <td>${rsmData.topInRateRSMName}</td>
		                   <td class="report_data_number"><fmt:formatNumber type="percent" value="${rsmData.topInRate}" pattern="#0.00%"/></td>
		                   <td>${rsmData.bottomInRateRSMName}</td>
		                   <td class="report_data_number"><fmt:formatNumber type="percent" value="${rsmData.bottomInRate}" pattern="#0.00%"/></td>
		               </tr>
		               <tr class="mobileReport_table_body">
		                   <td >Core医院雾化率</td>
		                   <td>${rsmData.coreTopWhRateRSMName}</td>
		                   <td class="report_data_number"><fmt:formatNumber type="percent" value="${rsmData.coreTopWhRate}" pattern="#0.00%"/></td>
		                   <td>${rsmData.coreBottomWhRateRSMName}</td>
		                   <td class="report_data_number"><fmt:formatNumber type="percent" value="${rsmData.coreBottomWhRate}" pattern="#0.00%"/></td>
		               </tr>
		               <tr class="mobileReport_table_body">
		                   <td >Emerging医院雾化率</td>
		                   <td>${rsmData.topEmergingWhRateRSMName}</td>
		                   <td class="report_data_number"><fmt:formatNumber type="percent" value="${rsmData.topEmergingWhRate}" pattern="#0.00%"/></td>
		                   <td>${rsmData.bottomEmergingWhRateRSMName}</td>
		                   <td class="report_data_number"><fmt:formatNumber type="percent" value="${rsmData.bottomEmergingWhRate}" pattern="#0.00%"/></td>
		               </tr>
		               <tr class="mobileReport_table_body">
		                   <td >平均剂量</td>
		                   <td>${rsmData.topAvRSMName}</td>
		                   <td class="report_data_number"><fmt:formatNumber type="percent" value="${rsmData.topAverageDose}" pattern="#0.00"/></td>
		                   <td>${rsmData.bottomAvRSMName}</td>
		                   <td class="report_data_number"><fmt:formatNumber type="percent" value="${rsmData.bottomAverageDose}" pattern="#0.00"/></td>
		               </tr>
	                </table>
	            </div>
            </c:if>
             --%>
        </div>
        <jsp:include page="page_footer.jsp">
            <jsp:param value="<%=basePath%>" name="basePath"/>
            <jsp:param value="dailyReport" name="backURL"/>
        </jsp:include>
    </div>
</body>  
</html>  
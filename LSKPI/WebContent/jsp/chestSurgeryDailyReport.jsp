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
            <jsp:param name="title" value="胸外科每日报告"/>
            <jsp:param name="basePath" value="<%=basePath%>"/>
        </jsp:include>
        <div data-role="content"  data-theme="a">
            <div class="roundCorner" style="padding:4px;">
                <div class="dailyReport_table_Title">${selfTitle}</div>
	            <table class="mobileReport_table">
	               <tr class="mobileReport_table_header">
                       <td width="20%">姓名</td>
                       <td width="16%">上报率</td>
                       <td width="16%">雾化率</td>
                       <td width="16%">平均剂量</td>
                       <td width="16%">住院人数</td>
                       <td width="16%">雾化人数</td>
                   </tr>
	               <c:forEach items="${mobileDailyReportData}" var="reportData" varStatus="status">
		               <tr class="mobileReport_table_body <c:if test="${status.count%2==0}">mobileReport_tr_even</c:if>">
		                   <td>${reportData.userName}</td>
		                   <td class="report_data_number"><fmt:formatNumber type="percent" value="${reportData.inRate}" pattern="#0%"/></td>
		                   <td class="report_data_number"><fmt:formatNumber type="percent" value="${reportData.whRate}" pattern="#0%"/></td>
		                   <td class="report_data_number"><fmt:formatNumber type="percent" value="${reportData.averageDose}" pattern="#0.00"/></td>
		                   <td class="report_data_number"><fmt:formatNumber value="${reportData.patNum}" pattern="#,###"/></td>
		                   <td class="report_data_number"><fmt:formatNumber value="${reportData.lsNum}" pattern="#,###"/></td>
		               </tr>
	               </c:forEach>
	               <c:if test="${mobileDailyReportParentData!= null }">
                       <tr class="mobileReport_table_body <c:if test="${fn:length(mobileDailyReportData)%2 != 0}">mobileReport_tr_even</c:if>">
		                   <td rowspan="2">${mobileDailyReportParentData.userName}</td>
		                   <td class="report_data_number"><fmt:formatNumber type="percent" value="${mobileDailyReportParentData.inRate}" pattern="#0%"/></td>
		                   <td class="report_data_number"><fmt:formatNumber type="percent" value="${mobileDailyReportParentData.whRate}" pattern="#0%"/></td>
		                   <td class="report_data_number"><fmt:formatNumber type="percent" value="${mobileDailyReportParentData.averageDose}" pattern="#0.00"/></td>
		                   <td class="report_data_number"><fmt:formatNumber value="${mobileDailyReportParentData.patNum}" pattern="#,###"/></td>
		                   <td class="report_data_number"><fmt:formatNumber value="${mobileDailyReportParentData.lsNum}" pattern="#,###"/></td>
		               </tr>
                   </c:if>
	            </table>
            </div>
            <c:if test="${operatorObj!= null && operatorObj.level=='BM'}">
            <c:forEach items="${mobileDailyReportAllRSMData}" var="rsmReportData">
                <div class="roundCorner" style="padding: 4px;">
                <div class="dailyReport_table_Title">所属[${rsmReportData[0].regionCenterCN}${titleSuffix}</div>
                <table class="mobileReport_table">
                    <tr class="mobileReport_table_header">
                        <td width="20%">姓名</td>
                        <td width="16%">上报率</td>
                        <td width="16%">雾化率</td>
                        <td width="16%">平均剂量</td>
                        <td width="16%">住院人数</td>
                        <td width="16%">雾化人数</td>
                    </tr>
                    <c:forEach items="${rsmReportData}" var="reportData" varStatus="status">
                        <tr class="mobileReport_table_body <c:if test="${status.count%2==0}">mobileReport_tr_even</c:if>">
                            <td>${reportData.userName}</td>
                            <td class="report_data_number">
                                <fmt:formatNumber type="percent" value="${reportData.inRate}" pattern="#0%" />
                            </td>
                            <td class="report_data_number">
                                <fmt:formatNumber type="percent" value="${reportData.whRate}" pattern="#0%" />
                            </td>
                            <td class="report_data_number">
                                <fmt:formatNumber type="percent" value="${reportData.averageDose}" pattern="#0.00" />
                            </td>
                            <td class="report_data_number">
                                <fmt:formatNumber value="${reportData.patNum}" pattern="#,###" />
                            </td>
                            <td class="report_data_number">
                                <fmt:formatNumber value="${reportData.lsNum}" pattern="#,###" />
                            </td>
                        </tr>
                    </c:forEach>
                </table>
            </div>
            </c:forEach>
            </c:if>
            <c:if test="${mobileDailyReportChildData!= null && fn:length(mobileDailyReportChildData) > 0}">
	            <div class="roundCorner" style="padding:4px;">
	                <div class="dailyReport_table_Title">${childTitle}</div>
	                <table class="mobileReport_table">
					<tr class="mobileReport_table_header">
						<td width="20%">姓名</td>
						<td width="16%">上报率</td>
						<td width="16%">雾化率</td>
						<td width="16%">平均剂量</td>
						<td width="16%">住院人数</td>
						<td width="16%">雾化人数</td>
					</tr>
					<c:forEach items="${mobileDailyReportChildData}" var="reportData" varStatus="status">
						<tr	class="mobileReport_table_body <c:if test="${status.count%2==0}">mobileReport_tr_even</c:if>">
							<td>${reportData.userName}</td>
							<td class="report_data_number">
								<fmt:formatNumber type="percent" value="${reportData.inRate}" pattern="#0%" />
							</td>
							<td class="report_data_number">
								<fmt:formatNumber type="percent" value="${reportData.whRate}" pattern="#0%" />
							</td>
							<td class="report_data_number">
								<fmt:formatNumber type="percent" value="${reportData.averageDose}" pattern="#0.00" />
							</td>
							<td class="report_data_number">
								<fmt:formatNumber value="${reportData.patNum}" pattern="#,###" />
							</td>
							<td class="report_data_number">
								<fmt:formatNumber value="${reportData.lsNum}" pattern="#,###" />
							</td>
						</tr>
					</c:forEach>
				</table>
	            </div>
            </c:if>
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
		                   <td >雾化率</td>
		                   <td>${rsmData.topWhRateRSMName}</td>
		                   <td class="report_data_number"><fmt:formatNumber type="percent" value="${rsmData.topWhRate}" pattern="#0.00%"/></td>
		                   <td>${rsmData.bottomWhRateRSMName}</td>
		                   <td class="report_data_number"><fmt:formatNumber type="percent" value="${rsmData.bottomWhRate}" pattern="#0.00%"/></td>
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
        </div>
        <jsp:include page="page_footer.jsp">
            <jsp:param value="<%=basePath%>" name="basePath"/>
            <jsp:param value="dailyReport" name="backURL"/>
        </jsp:include>
    </div>
</body>  
</html>  
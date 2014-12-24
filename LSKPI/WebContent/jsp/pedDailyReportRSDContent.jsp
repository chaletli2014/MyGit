<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<c:set var="dailyReportData" value="<%=request.getParameter("dailyReportData") %>"/>

<div class="roundCorner" style="padding: 4px;">
	<div class="dailyReport_table_Title"><%=request.getParameter("title") %></div>
	<table class="mobileReport_table">
		<tr class="mobileReport_table_header">
			<td width="20%">姓名</td>
			<td width="16%">上报率</td>
			<td width="16%">雾化率</td>
			<td width="16%">平均剂量</td>
			<td width="16%">门诊人次</td>
			<td width="16%">雾化人数</td>
		</tr>
		<c:forEach items="${dailyReportData}" var="reportData" varStatus="status">
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
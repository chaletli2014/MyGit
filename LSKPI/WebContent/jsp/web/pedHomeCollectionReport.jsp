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
            <jsp:param name="title" value="儿科家庭雾化周报"/>
            <jsp:param name="basePath" value="<%=basePath%>"/>
        </jsp:include>
        <div data-role="content"  data-theme="a">
            <div class="roundCorner" style="padding:4px;">
                <div class="dailyReport_table_Title">${homeDataTitle}</div>
                <table class="mobileReport_table">
                   <tr class="mobileReport_table_header">
                        <td colspan="3">层级情况</td>
					    <td colspan="3">处方情况</td>
                    </tr>
                    <tr class="mobileReport_table_header">
					    <td width="15%">名称</td>
					    <td width="15%">上报医院数</td>
					    <td width="15%">上报率</td>
					    <td width="20%">卖赠泵数量</td>
					    <td width="20%">平均带药天数</td>
					    <td width="20%">总带药支数</td>
					</tr>
                   <c:forEach items="${homeWeeklyDataList}" var="homeWeeklyData" varStatus="status">
                       <tr class="mobileReport_table_body <c:if test="${status.count%2==0}">mobileReport_tr_even</c:if>">
                          <td class="report_data_number">${homeWeeklyData.userName}</td>
                          <td class="report_data_number" ><fmt:formatNumber value="${homeWeeklyData.inNum}" pattern="#,###"/></td>
                          <td class="report_data_number" ><fmt:formatNumber value="${homeWeeklyData.inRate}" type="percent" pattern="#0%"/></td>
                          <td class="report_data_number" ><fmt:formatNumber value="${homeWeeklyData.homeWhNum1}" pattern="#,###"/></td>
                          <td class="report_data_number" ><fmt:formatNumber value="${homeWeeklyData.averDays}" pattern="#0.00"/></td>
                          <td class="report_data_number" ><fmt:formatNumber value="${homeWeeklyData.homeWhNum4}" pattern="#,###"/></td>
                        </tr>
                   </c:forEach>
                   <tr class="mobileReport_table_body <c:if test="${fn:length(homeWeeklyDataList)%2 != 0}">mobileReport_tr_even</c:if>">
                          <td class="report_data_number">${upperHomeWeeklyData.userName}</td>
                          <td class="report_data_number" ><fmt:formatNumber value="${upperHomeWeeklyData.inNum}" pattern="#,###"/></td>
                          <td class="report_data_number" ><fmt:formatNumber value="${upperHomeWeeklyData.inRate}" type="percent" pattern="#0%"/></td>
                          <td class="report_data_number" ><fmt:formatNumber value="${upperHomeWeeklyData.homeWhNum1}" pattern="#,###"/></td>
                          <td class="report_data_number" ><fmt:formatNumber value="${upperHomeWeeklyData.averDays}" pattern="#0.00"/></td>
                          <td class="report_data_number" ><fmt:formatNumber value="${upperHomeWeeklyData.homeWhNum4}" pattern="#,###"/></td>
                   </tr>
                </table>
            </div>
            <c:if test="${lowerHomeWeeklyDataList!=null && fn:length(lowerHomeWeeklyDataList)>0}">
                <div class="roundCorner" style="padding:4px;">
                <div class="dailyReport_table_Title">${childTitle}</div>
	                <table class="mobileReport_table">
	                   <tr class="mobileReport_table_header">
	                        <td colspan="3">层级情况</td>
	                        <td colspan="3">处方情况</td>
	                    </tr>
	                    <tr class="mobileReport_table_header">
	                        <td width="15%">名称</td>
	                        <td width="15%">上报医院数</td>
						    <td width="15%">上报率</td>
						    <td width="20%">卖赠泵数量</td>
						    <td width="20%">平均带药天数</td>
						    <td width="20%">总带药支数</td>
	                      </tr>
	                   <c:forEach items="${lowerHomeWeeklyDataList}" var="homeWeeklyData" varStatus="status">
	                       <tr class="mobileReport_table_body <c:if test="${status.count%2==0}">mobileReport_tr_even</c:if>">
	                          <td class="report_data_number">${homeWeeklyData.userName}</td>
	                          <td class="report_data_number" ><fmt:formatNumber value="${homeWeeklyData.inNum}" pattern="#,###"/></td>
	                          <td class="report_data_number" ><fmt:formatNumber value="${homeWeeklyData.inRate}" type="percent" pattern="#0%"/></td>
	                          <td class="report_data_number" ><fmt:formatNumber value="${homeWeeklyData.homeWhNum1}" pattern="#,###"/></td>
	                          <td class="report_data_number" ><fmt:formatNumber value="${homeWeeklyData.averDays}" pattern="#0.00"/></td>
	                          <td class="report_data_number" ><fmt:formatNumber value="${homeWeeklyData.homeWhNum4}" pattern="#,###"/></td>
	                        </tr>
	                   </c:forEach>
	                </table>
	            </div>
            </c:if>
            <c:if test="${operatorObj!= null && operatorObj.level=='BM'}">
            <c:forEach items="${allRSMHomeWeeklyData}" var="rsmReportData">
            <div class="roundCorner" style="padding:4px;">
                <div class="dailyReport_table_Title">所属[${rsmReportData[0].regionCenterCN}${titleSuffix}</div>
                <table class="mobileReport_table">
                   <tr class="mobileReport_table_header">
                        <td colspan="3">层级情况</td>
					    <td colspan="3">处方情况</td>
                   </tr>
                   <tr class="mobileReport_table_header">
					    <td width="15%">名称</td>
					    <td width="15%">上报医院数</td>
					    <td width="15%">上报率</td>
					    <td width="20%">卖赠泵数量</td>
					    <td width="20%">平均带药天数</td>
					    <td width="20%">总带药支数</td>
				  </tr>
                   <c:forEach items="${rsmReportData}" var="homeWeeklyData" varStatus="status">
                       <tr class="mobileReport_table_body <c:if test="${status.count%2==0}">mobileReport_tr_even</c:if>">
                          <td class="report_data_number">${homeWeeklyData.userName}</td>
                          <td class="report_data_number" ><fmt:formatNumber value="${homeWeeklyData.inNum}" pattern="#,###"/></td>
                          <td class="report_data_number" ><fmt:formatNumber value="${homeWeeklyData.inRate}" type="percent" pattern="#0%"/></td>
                          <td class="report_data_number" ><fmt:formatNumber value="${homeWeeklyData.homeWhNum1}" pattern="#,###"/></td>
                          <td class="report_data_number" ><fmt:formatNumber value="${homeWeeklyData.averDays}" pattern="#0.00"/></td>
                          <td class="report_data_number" ><fmt:formatNumber value="${homeWeeklyData.homeWhNum4}" pattern="#,###"/></td>
                        </tr>
                   </c:forEach>
                </table>
            	</div>
            </c:forEach>
            </c:if>
        </div>
        <iframe src="${reportFile}" id="iframepage" scrolling="no" width="100%" onload="javascript:dyniframesize('iframepage');"></iframe>
        <jsp:include page="page_footer.jsp">
            <jsp:param value="<%=basePath%>" name="basePath"/>
            <jsp:param value="homeReportDepartment" name="backURL"/>
        </jsp:include>
    </div>
</body>  
</html>  
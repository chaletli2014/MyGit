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
            <jsp:param name="title" value="家庭雾化周报"/>
            <jsp:param name="basePath" value="<%=basePath%>"/>
        </jsp:include>
        <div data-role="content"  data-theme="a">
            <div class="roundCorner" style="padding:4px;">
                <div class="dailyReport_table_Title">${homeDataTitle}</div>
                <table class="mobileReport_table">
                   <tr class="mobileReport_table_header">
                        <td colspan="2">层级情况</td>
				        <td colspan="2">医生情况</td>
					    <td colspan="5">处方情况</td>
                    </tr>
                    <tr class="mobileReport_table_header">
					    <td width="10%">名称</td>
					    <td width="10%">上报率</td>
					    <td width="10%">总目标医生数</td>
					    <td width="10%">上周新增医生数</td>
					    <td width="12%">每周新病人人次</td>
					    <%--
					    <td width="12%">维持期治疗率</td>
					     --%>
					    <td width="12%">维持期使用令舒的人次</td>
					    <%--
					    <td width="12%">维持期令舒比例</td>
					     --%>
					    <td width="12%">家庭雾化疗程达标人次（DOT>=30天）</td>
					</tr>
                   <c:forEach items="${homeWeeklyDataList}" var="homeWeeklyData" varStatus="status">
                       <tr class="mobileReport_table_body <c:if test="${status.count%2==0}">mobileReport_tr_even</c:if>">
                          <td class="report_data_number">${homeWeeklyData.userName}</td>
                          <td class="report_data_number" ><fmt:formatNumber value="${homeWeeklyData.inRate}" type="percent" pattern="#0%"/></td>
                          <td class="report_data_number" ><fmt:formatNumber value="${homeWeeklyData.totalDrNum}" pattern="#,###"/></td>
                          <td class="report_data_number" ><fmt:formatNumber value="${homeWeeklyData.newDrNum}" pattern="#,###"/></td>
                          <td class="report_data_number" ><fmt:formatNumber value="${homeWeeklyData.newWhNum}" pattern="#,###"/></td>
                          <%--
                          <td class="report_data_number" ><fmt:formatNumber value="${homeWeeklyData.cureRate}" type="percent" pattern="#0%"/></td>
                           --%>
                          <td class="report_data_number" ><fmt:formatNumber value="${homeWeeklyData.lsnum}" pattern="#,###"/></td>
                          <%--
                          <td class="report_data_number" ><fmt:formatNumber value="${homeWeeklyData.lsRate}" type="percent" pattern="#0%"/></td>
                           --%>
                          <td class="report_data_number" ><fmt:formatNumber value="${homeWeeklyData.reachRate}" pattern="#,###"/></td>
                        </tr>
                   </c:forEach>
                   <tr class="mobileReport_table_body <c:if test="${fn:length(homeWeeklyDataList)%2 != 0}">mobileReport_tr_even</c:if>">
                          <td class="report_data_number">${upperHomeWeeklyData.userName}</td>
                          <td class="report_data_number" ><fmt:formatNumber value="${upperHomeWeeklyData.inRate}" type="percent" pattern="#0%"/></td>
                          <td class="report_data_number" ><fmt:formatNumber value="${upperHomeWeeklyData.totalDrNum}" pattern="#,###"/></td>
                          <td class="report_data_number" ><fmt:formatNumber value="${upperHomeWeeklyData.newDrNum}" pattern="#,###"/></td>
                          <td class="report_data_number" ><fmt:formatNumber value="${upperHomeWeeklyData.newWhNum}" pattern="#,###"/></td>
                          <%--
                          <td class="report_data_number" ><fmt:formatNumber value="${upperHomeWeeklyData.cureRate}" type="percent" pattern="#0%"/></td>
                           --%>
                          <td class="report_data_number" ><fmt:formatNumber value="${upperHomeWeeklyData.lsnum}" pattern="#,###"/></td>
                          <%--
                          <td class="report_data_number" ><fmt:formatNumber value="${upperHomeWeeklyData.lsRate}" type="percent" pattern="#0%"/></td>
                           --%>
                          <td class="report_data_number" ><fmt:formatNumber value="${upperHomeWeeklyData.reachRate}" pattern="#,###"/></td>
                   </tr>
                </table>
            </div>
            <c:if test="${lowerHomeWeeklyDataList!=null && fn:length(lowerHomeWeeklyDataList)>0}">
                <div class="roundCorner" style="padding:4px;">
                <div class="dailyReport_table_Title">${childTitle}</div>
	                <table class="mobileReport_table">
	                   <tr class="mobileReport_table_header">
	                        <td colspan="2">层级情况</td>
	                        <td colspan="2">医生情况</td>
	                        <td colspan="5">处方情况</td>
	                    </tr>
	                    <tr class="mobileReport_table_header">
	                        <td width="10%">名称</td>
						    <td width="10%">上报率</td>
						    <td width="10%">总目标医生数</td>
						    <td width="10%">上周新增医生数</td>
						    <td width="12%">每周新病人人次</td>
						    <%--
						    <td width="12%">维持期治疗率</td>
						     --%>
						    <td width="12%">维持期使用令舒的人次</td>
						    <%--
						    <td width="12%">维持期令舒比例</td>
						     --%>
						    <td width="12%">家庭雾化疗程达标人次（DOT>=30天）</td>
	                      </tr>
	                   <c:forEach items="${lowerHomeWeeklyDataList}" var="homeWeeklyData" varStatus="status">
	                       <tr class="mobileReport_table_body <c:if test="${status.count%2==0}">mobileReport_tr_even</c:if>">
	                          <td class="report_data_number">${homeWeeklyData.userName}</td>
	                          <td class="report_data_number" ><fmt:formatNumber value="${homeWeeklyData.inRate}" type="percent" pattern="#0%"/></td>
	                          <td class="report_data_number" ><fmt:formatNumber value="${homeWeeklyData.totalDrNum}" pattern="#,###"/></td>
	                          <td class="report_data_number" ><fmt:formatNumber value="${homeWeeklyData.newDrNum}" pattern="#,###"/></td>
	                          <td class="report_data_number" ><fmt:formatNumber value="${homeWeeklyData.newWhNum}" pattern="#,###"/></td>
	                          <%--
	                          <td class="report_data_number" ><fmt:formatNumber value="${homeWeeklyData.cureRate}" type="percent" pattern="#0%"/></td>
	                           --%>
	                          <td class="report_data_number" ><fmt:formatNumber value="${homeWeeklyData.lsnum}" pattern="#,###"/></td>
	                          <%--
	                          <td class="report_data_number" ><fmt:formatNumber value="${homeWeeklyData.lsRate}" type="percent" pattern="#0%"/></td>
	                           --%>
	                          <td class="report_data_number" ><fmt:formatNumber value="${homeWeeklyData.reachRate}" pattern="#,###"/></td>
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
                        <td colspan="2">层级情况</td>
				        <td colspan="2">医生情况</td>
					    <td colspan="5">处方情况</td>
                    </tr>
                    <tr class="mobileReport_table_header">
					    <td width="10%">名称</td>
					    <td width="10%">上报率</td>
					    <td width="10%">总目标医生数</td>
					    <td width="10%">上周新增医生数</td>
					    <td width="12%">每周新病人人次</td>
					    <%--
					    <td width="12%">维持期治疗率</td>
					     --%>
					    <td width="12%">维持期使用令舒的人次</td>
					    <%--
					    <td width="12%">维持期令舒比例</td>
					     --%>
					    <td width="12%">家庭雾化疗程达标人次（DOT>=30天）</td>
					  </tr>
                   <c:forEach items="${rsmReportData}" var="homeWeeklyData" varStatus="status">
                       <tr class="mobileReport_table_body <c:if test="${status.count%2==0}">mobileReport_tr_even</c:if>">
                          <td class="report_data_number">${homeWeeklyData.userName}</td>
                          <td class="report_data_number" ><fmt:formatNumber value="${homeWeeklyData.inRate}" type="percent" pattern="#0%"/></td>
                          <td class="report_data_number" ><fmt:formatNumber value="${homeWeeklyData.totalDrNum}" pattern="#,###"/></td>
                          <td class="report_data_number" ><fmt:formatNumber value="${homeWeeklyData.newDrNum}" pattern="#,###"/></td>
                          <td class="report_data_number" ><fmt:formatNumber value="${homeWeeklyData.newWhNum}" pattern="#,###"/></td>
                          <%--
                          <td class="report_data_number" ><fmt:formatNumber value="${homeWeeklyData.cureRate}" type="percent" pattern="#0%"/></td>
                           --%>
                          <td class="report_data_number" ><fmt:formatNumber value="${homeWeeklyData.lsnum}" pattern="#,###"/></td>
                          <%--
                          <td class="report_data_number" ><fmt:formatNumber value="${homeWeeklyData.lsRate}" type="percent" pattern="#0%"/></td>
                           --%>
                          <td class="report_data_number" ><fmt:formatNumber value="${homeWeeklyData.reachRate}" pattern="#,###"/></td>
                        </tr>
                   </c:forEach>
                </table>
            	</div>
            </c:forEach>
            </c:if>
        </div>
        <iframe src="${reportFile}" class="weeklyReport_iframe"></iframe>
        <jsp:include page="page_footer.jsp">
            <jsp:param value="<%=basePath%>" name="basePath"/>
            <jsp:param value="homeReportDepartment" name="backURL"/>
        </jsp:include>
    </div>
</body>  
</html>  
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
                        <td width="16%">层级情况</td>
				        <td colspan="2">医生情况</td>
					    <td colspan="5">处方情况</td>
                    </tr>
                    <tr class="mobileReport_table_header">
					    <td width="16%">名称</td>
					    <td width="12%">总目标医生数</td>
					    <td width="12%">上周新增医生数</td>
					    <td width="12%">上周家庭雾化新病人次量</td>
					    <td width="12%">持续期治疗率</td>
					    <td width="12%">推荐使用令舒的人次</td>
					    <td width="12%">持续期令舒比例</td>
					    <td width="12%">家庭雾化疗程达标率（DOT>=30天）</td>
					  </tr>
                   <c:forEach items="${homeWeeklyDataList}" var="homeWeeklyData" varStatus="status">
                       <tr class="mobileReport_table_body <c:if test="${status.count%2==0}">mobileReport_tr_even</c:if>">
                          <td class="report_data_number">${homeWeeklyData.userName}</td>
                          <td class="report_data_number" ><fmt:formatNumber value="${homeWeeklyData.totalDrNum}" pattern="#,###"/></td>
                          <td class="report_data_number" ><fmt:formatNumber value="${homeWeeklyData.newDrNum}" pattern="#,###"/></td>
                          <td class="report_data_number" ><fmt:formatNumber value="${homeWeeklyData.newWhNum}" pattern="#,###"/></td>
                          <td class="report_data_number" ><fmt:formatNumber value="${homeWeeklyData.cureRate}" type="percent" pattern="#0%"/></td>
                          <td class="report_data_number" ><fmt:formatNumber value="${homeWeeklyData.lsnum}" pattern="#,###"/></td>
                          <td class="report_data_number" ><fmt:formatNumber value="${homeWeeklyData.lsRate}" type="percent" pattern="#0%"/></td>
                          <td class="report_data_number" ><fmt:formatNumber value="${homeWeeklyData.reachRate}" type="percent" pattern="#0%"/></td>
                        </tr>
                   </c:forEach>
                </table>
            </div>
        </div>
        <jsp:include page="page_footer.jsp">
            <jsp:param value="<%=basePath%>" name="basePath"/>
            <jsp:param value="weeklyreport" name="backURL"/>
        </jsp:include>
    </div>
</body>  
</html>  
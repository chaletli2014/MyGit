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
            <jsp:param name="title" value="上月销售袋数统计"/>
            <jsp:param name="basePath" value="<%=basePath%>"/>
        </jsp:include>
        <div data-role="content"  data-theme="a">
            <div class="roundCorner" style="padding:4px;background:#fff;">
                <div class="dailyReport_table_Title">${selfTitle}</div>
                <table class="mobileReport_table">
                   <tr class="mobileReport_table_header">
                      <td width="15%">姓名</td>
                      <td width="17%">儿科门急诊</td>
                      <td width="17%">儿科病房</td>
                      <td width="17%">呼吸科</td>
                      <td width="17%">其他科室</td>
                      <td width="17%">总袋数</td>
                    </tr>
                   <c:forEach items="${monthlyRatioList}" var="monthlyRatio" varStatus="status">
                       <tr class="mobileReport_table_body <c:if test="${status.count%2==0}">mobileReport_tr_even</c:if>">
                          <td class="report_data_number">
	                          <c:if test="${'DSM' == currentUser.level}">
	                              ${monthlyRatio.dsmName}
	                          </c:if>
	                          <c:if test="${'RSM' == currentUser.level}">
	                              ${monthlyRatio.rsmRegion}
	                          </c:if>
	                          <c:if test="${'RSD' == currentUser.level || 'BM' == currentUser.level}">
	                              ${monthlyRatio.region}
	                          </c:if>
                          </td>
                          <td class="report_data_number" ><fmt:formatNumber value="${monthlyRatio.pedemernum}" pattern="#,###"/></td>
                          <td class="report_data_number" ><fmt:formatNumber value="${monthlyRatio.pedroomnum}" pattern="#,###"/></td>
                          <td class="report_data_number" ><fmt:formatNumber value="${monthlyRatio.resnum}" pattern="#,###"/></td>
                          <td class="report_data_number" ><fmt:formatNumber value="${monthlyRatio.othernum}" pattern="#,###"/></td>
                          <td class="report_data_number" ><fmt:formatNumber value="${monthlyRatio.totalnum}" pattern="#,###"/></td>
                        </tr>
                   </c:forEach>
                   <tr class="mobileReport_table_body <c:if test="${fn:length(monthlyRatioList)%2 != 0}">mobileReport_tr_even</c:if>">
                     <td class="report_data_number">
                        <c:if test="${'DSM' == currentUser.level}">
                              ${superiorMonthlyRatio.rsmRegion}
                          </c:if>
                          <c:if test="${'RSM' == currentUser.level}">
                              ${superiorMonthlyRatio.region}
                          </c:if>
                          <c:if test="${'RSD' == currentUser.level || 'BM' == currentUser.level}">
                                                    	全国
                          </c:if>
                     </td>
                     <td class="report_data_number" ><fmt:formatNumber value="${superiorMonthlyRatio.pedemernum}" pattern="#,###"/></td>
                     <td class="report_data_number" ><fmt:formatNumber value="${superiorMonthlyRatio.pedroomnum}" pattern="#,###"/></td>
                     <td class="report_data_number" ><fmt:formatNumber value="${superiorMonthlyRatio.resnum}" pattern="#,###"/></td>
                     <td class="report_data_number" ><fmt:formatNumber value="${superiorMonthlyRatio.othernum}" pattern="#,###"/></td>
                     <td class="report_data_number" ><fmt:formatNumber value="${superiorMonthlyRatio.totalnum}" pattern="#,###"/></td>
                   </tr>
                </table>
                <table class="mobileReport_table">
                   <tr class="mobileReport_table_header">
                      <td width="15%">姓名</td>
                      <td width="15%">儿科门急诊占比</td>
                      <td width="15%">儿科病房占比</td>
                      <td width="15%">呼吸科占比</td>
                      <td width="15%">其他科室占比</td>
                      <td width="9%">袋数上报率</td>
                      <td width="8%">上报医院数</td>
                      <td width="8%">负责医院数</td>
                    </tr>
                   <c:forEach items="${monthlyRatioList}" var="monthlyRatio" varStatus="status">
                       <tr class="mobileReport_table_body <c:if test="${status.count%2==0}">mobileReport_tr_even</c:if>">
                          <td class="report_data_number">
	                          <c:if test="${'DSM' == currentUser.level}">
	                              ${monthlyRatio.dsmName}
	                          </c:if>
	                          <c:if test="${'RSM' == currentUser.level}">
	                              ${monthlyRatio.rsmRegion}
	                          </c:if>
	                          <c:if test="${'RSD' == currentUser.level || 'BM' == currentUser.level}">
	                              ${monthlyRatio.region}
	                          </c:if>
                          </td>
                          <td class="report_data_number" ><fmt:formatNumber type="percent" value="${monthlyRatio.pedemernumrate}" pattern="#0%"/></td>
                          <td class="report_data_number" ><fmt:formatNumber type="percent" value="${monthlyRatio.pedroomnumrate}" pattern="#0%"/></td>
                          <td class="report_data_number" ><fmt:formatNumber type="percent" value="${monthlyRatio.resnumrate}" pattern="#0%"/></td>
                          <td class="report_data_number" ><fmt:formatNumber type="percent" value="${monthlyRatio.othernumrate}" pattern="#0%"/></td>
                          <td class="report_data_number" ><fmt:formatNumber type="percent" value="${monthlyRatio.innum/monthlyRatio.hosnum}" pattern="#0%"/></td>
                       	  <td class="report_data_number" ><fmt:formatNumber value="${monthlyRatio.innum}" pattern="#,###"/></td>
                       	  <td class="report_data_number" ><fmt:formatNumber value="${monthlyRatio.hosnum}" pattern="#,###"/></td>
                        </tr>
                   </c:forEach>
                   <tr class="mobileReport_table_body <c:if test="${fn:length(monthlyRatioList)%2 != 0}">mobileReport_tr_even</c:if>">
                     <td class="report_data_number">
                        <c:if test="${'DSM' == currentUser.level}">
                              ${superiorMonthlyRatio.rsmRegion}
                          </c:if>
                          <c:if test="${'RSM' == currentUser.level}">
                              ${superiorMonthlyRatio.region}
                          </c:if>
                          <c:if test="${'RSD' == currentUser.level || 'BM' == currentUser.level}">
                                                    	全国
                          </c:if>
                     </td>
                     <td class="report_data_number" ><fmt:formatNumber type="percent" value="${superiorMonthlyRatio.pedemernumrate}" pattern="#0%"/></td>
                     <td class="report_data_number" ><fmt:formatNumber type="percent" value="${superiorMonthlyRatio.pedroomnumrate}" pattern="#0%"/></td>
                     <td class="report_data_number" ><fmt:formatNumber type="percent" value="${superiorMonthlyRatio.resnumrate}" pattern="#0%"/></td>
                     <td class="report_data_number" ><fmt:formatNumber type="percent" value="${superiorMonthlyRatio.othernumrate}" pattern="#0%"/></td>
                     <td class="report_data_number" ><fmt:formatNumber type="percent" value="${superiorMonthlyRatio.innum/superiorMonthlyRatio.hosnum}" pattern="#0%"/></td>
                     <td class="report_data_number" ><fmt:formatNumber value="${superiorMonthlyRatio.innum}" pattern="#,###"/></td>
                     <td class="report_data_number" ><fmt:formatNumber value="${superiorMonthlyRatio.hosnum}" pattern="#,###"/></td>
                   </tr>
                </table>
            </div>
        </div>
        <iframe src="${monthlyReportFile}" id="iframepage" scrolling="no" width="100%" onload="javascript:dyniframesize('iframepage');"></iframe>
        <jsp:include page="page_footer.jsp">
            <jsp:param value="<%=basePath%>" name="basePath"/>
            <jsp:param value="weeklyreport" name="backURL"/>
        </jsp:include>
    </div>
</body>  
</html>  
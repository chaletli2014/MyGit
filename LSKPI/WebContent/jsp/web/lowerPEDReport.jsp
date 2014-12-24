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
            <jsp:param name="title" value="上周数据环比及近期走势"/>
            <jsp:param name="basePath" value="<%=basePath%>"/>
        </jsp:include>
        <div data-role="content"  data-theme="a">
            <div class="roundCorner" style="padding:4px;">
	            <table class="mobileReport_table">
	               <tr class="mobileReport_table_header">
	                   <td width="20%">姓名</td>
                       <td width="16%">上报率</td>
                       <td width="16%">雾化率</td>
                       <td width="16%">平均剂量</td>
                       <td width="16%">门诊人次</td>
                       <td width="16%">雾化人次</td>
	               </tr>
		               <tr class="mobileReport_table_body">
		               		<td rowspan="2">${weeklyRatioData.name}</td>
		                   	<td class="report_data_number">
		                   		<fmt:formatNumber type="percent" value="${weeklyRatioData.inRate}" pattern="#0%"/>
		                   	</td>
		                   	<td class="report_data_number">
		                   		<fmt:formatNumber type="percent" value="${weeklyRatioData.whRate}" pattern="#0%"/>
		                   	</td>
		                   	<td class="report_data_number">
		                   		<fmt:formatNumber type="percent" value="${weeklyRatioData.averageDose}" pattern="#0.00"/>
		                   	</td>
		                   	<td class="report_data_number"><fmt:formatNumber value="${weeklyRatioData.pnum}" pattern="#,###" /></td>
		                   	<td class="report_data_number"><fmt:formatNumber value="${weeklyRatioData.lsnum}" pattern="#,###" /></td>
		               </tr>
		               <tr class="mobileReport_table_body">
		               		<td class="<c:if test="${weeklyRatioData.inRateRatio>0}">ratio_up</c:if><c:if test="${weeklyRatioData.inRateRatio<0}">ratio_down</c:if>" rowspan="2">
		               			<span class="narrow_font">
						    		<c:if test="${weeklyRatioData.inRateRatio>0}">+</c:if>
			               			<c:if test="${weeklyRatioData.inRateRatio<0}">-</c:if>
			               		</span>
		               			<fmt:formatNumber type="percent" value="${weeklyRatioData.inRateRatio<0?-weeklyRatioData.inRateRatio:weeklyRatioData.inRateRatio}" pattern="#0%"/>
		               		</td>
					    	<td class="<c:if test="${weeklyRatioData.whRateRatio>0}">ratio_up</c:if><c:if test="${weeklyRatioData.whRateRatio<0}">ratio_down</c:if>" rowspan="2">
					    		<span class="narrow_font">
						    		<c:if test="${weeklyRatioData.whRateRatio>0}">+</c:if>
			               			<c:if test="${weeklyRatioData.whRateRatio<0}">-</c:if>
			               		</span>
					    		<fmt:formatNumber type="percent" value="${weeklyRatioData.whRateRatio<0?-weeklyRatioData.whRateRatio:weeklyRatioData.whRateRatio}" pattern="#0%"/>
					    	</td>
					    	<td class="<c:if test="${weeklyRatioData.averageDoseRatio>0}">ratio_up</c:if><c:if test="${weeklyRatioData.averageDoseRatio<0}">ratio_down</c:if>" rowspan="2">
					    		<span class="narrow_font">
						    		<c:if test="${weeklyRatioData.averageDoseRatio>0}">+</c:if>
			               			<c:if test="${weeklyRatioData.averageDoseRatio<0}">-</c:if>
			               		</span>
					    		<fmt:formatNumber type="percent" value="${weeklyRatioData.averageDoseRatio<0?-weeklyRatioData.averageDoseRatio:weeklyRatioData.averageDoseRatio}" pattern="#0%"/>
					    	</td>
					   		<td class="<c:if test="${weeklyRatioData.patNumRatio>0}">ratio_up_noimg</c:if><c:if test="${weeklyRatioData.patNumRatio<0}">ratio_down_noimg</c:if>">
					   			<span class="narrow_font">
						    		<c:if test="${weeklyRatioData.patNumRatio>0}">+</c:if>
			               			<c:if test="${weeklyRatioData.patNumRatio<0}">-</c:if>
			               		</span>
					   			<fmt:formatNumber type="percent" value="${weeklyRatioData.patNumRatio<0?-weeklyRatioData.patNumRatio:weeklyRatioData.patNumRatio}" pattern="#0%"/>
					   		</td>
					   		<td class="<c:if test="${weeklyRatioData.lsNumRatio>0}">ratio_up_noimg</c:if><c:if test="${weeklyRatioData.lsNumRatio<0}">ratio_down_noimg</c:if>">
					   			<span class="narrow_font">
						    		<c:if test="${weeklyRatioData.lsNumRatio>0}">+</c:if>
			               			<c:if test="${weeklyRatioData.lsNumRatio<0}">-</c:if>
			               		</span>
					   			<fmt:formatNumber type="percent" value="${weeklyRatioData.lsNumRatio<0?-weeklyRatioData.lsNumRatio:weeklyRatioData.lsNumRatio}" pattern="#0%"/>
					   		</td>
					   </tr>
	            </table>
            </div>
        </div>
        <iframe src="${pedReportFile}" id="iframepage" scrolling="no" width="100%" onload="javascript:dyniframesize('iframepage');"></iframe>
        <jsp:include page="page_footer.jsp">
            <jsp:param value="<%=basePath%>" name="basePath"/>
            <jsp:param value="ratioLastweekPed" name="backURL"/>
        </jsp:include>
    </div>
</body>  
</html>  
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
            <jsp:param name="title" value="医院上周数据环比"/>
            <jsp:param name="basePath" value="<%=basePath%>"/>
        </jsp:include>
        <div data-role="content"  data-theme="a">
            <div class="dailyReport_table_Title">${selectedHospitalName}</div>
            <div class="roundCorner" style="padding:4px;">
                <div class="dailyReport_table_Title">儿科数据环比</div>
	            <table class="mobileReport_table">
	               <tr class="mobileReport_table_header">
                       <td width="20%">上报率</td>
                       <td width="20%">雾化率</td>
                       <td width="20%">平均剂量</td>
                       <td width="20%">门诊人次</td>
                       <td width="20%">雾化人次</td>
	               </tr>
	               <tr class="mobileReport_table_body">
	                   <td class="report_data_number"><fmt:formatNumber type="percent" value="${pedRatioData.inRate}" pattern="#0%"/></td>
	                   <td class="report_data_number"><fmt:formatNumber type="percent" value="${pedRatioData.whRate}" pattern="#0%"/></td>
	                   <td class="report_data_number"><fmt:formatNumber type="percent" value="${pedRatioData.averageDose}" pattern="#0.00"/></td>
	                   <td class="report_data_number">${pedRatioData.pnum}</td>
	                   <td class="report_data_number">${pedRatioData.lsnum}</td>
	               </tr>
	               <tr class="mobileReport_table_body">
	               		<td class="<c:if test="${pedRatioData.inRateRatio>0}">ratio_up</c:if><c:if test="${pedRatioData.inRateRatio<0}">ratio_down</c:if>" rowspan="2">
	               			<span class="narrow_font">
		               			<c:if test="${pedRatioData.inRateRatio>0}">+</c:if>
		               			<c:if test="${pedRatioData.inRateRatio<0}">-</c:if>
	               			</span>
	               			<fmt:formatNumber type="percent" value="${pedRatioData.inRateRatio<0?-pedRatioData.inRateRatio:pedRatioData.inRateRatio}" pattern="#0%"/>
	               		</td>
				    	<td class="<c:if test="${pedRatioData.whRateRatio>0}">ratio_up</c:if><c:if test="${pedRatioData.whRateRatio<0}">ratio_down</c:if>" rowspan="2">
				    		<span class="narrow_font">
					    		<c:if test="${pedRatioData.whRateRatio>0}">+</c:if>
					    		<c:if test="${pedRatioData.whRateRatio<0}">-</c:if>
	               			</span>
				    		<fmt:formatNumber type="percent" value="${pedRatioData.whRateRatio<0?-pedRatioData.whRateRatio:pedRatioData.whRateRatio}" pattern="#0%"/>
				    	</td>
				    	<td class="<c:if test="${pedRatioData.averageDoseRatio>0}">ratio_up</c:if><c:if test="${pedRatioData.averageDoseRatio<0}">ratio_down</c:if>" rowspan="2">
				    		<span class="narrow_font">
					    		<c:if test="${pedRatioData.averageDoseRatio>0}">+</c:if>
					    		<c:if test="${pedRatioData.averageDoseRatio<0}">-</c:if>
	               			</span>
				    		<fmt:formatNumber type="percent" value="${pedRatioData.averageDoseRatio<0?-pedRatioData.averageDoseRatio:pedRatioData.averageDoseRatio}" pattern="#0%"/>
				    	</td>
				   		<td class="<c:if test="${pedRatioData.patNumRatio>0}">ratio_up_noimg</c:if><c:if test="${pedRatioData.patNumRatio<0}">ratio_down_noimg</c:if>">
				   			<fmt:formatNumber type="percent" value="${pedRatioData.patNumRatio<0?-pedRatioData.patNumRatio:pedRatioData.patNumRatio}" pattern="#0%"/>
				   		</td>
				   		<td class="<c:if test="${pedRatioData.lsNumRatio>0}">ratio_up_noimg</c:if><c:if test="${pedRatioData.lsNumRatio<0}">ratio_down_noimg</c:if>">
				   			<fmt:formatNumber type="percent" value="${pedRatioData.lsNumRatio<0?-pedRatioData.lsNumRatio:pedRatioData.lsNumRatio}" pattern="#0%"/>
				   		</td>
				   </tr>
	            </table>
            </div>
            <div class="roundCorner" style="padding:4px;">
                <div class="dailyReport_table_Title">呼吸科数据环比</div>
	            <table class="mobileReport_table">
	               <tr class="mobileReport_table_header">
                       <td width="20%">上报率</td>
                       <td width="20%">雾化率</td>
                       <td width="20%">平均剂量</td>
                       <td width="20%">住院人数</td>
                       <td width="20%">雾化人数</td>
	               </tr>
	               <tr class="mobileReport_table_body">
	                   <td class="report_data_number"><fmt:formatNumber type="percent" value="${resRatioData.inRate}" pattern="#0%"/></td>
	                   <td class="report_data_number"><fmt:formatNumber type="percent" value="${resRatioData.whRate}" pattern="#0%"/></td>
	                   <td class="report_data_number"><fmt:formatNumber type="percent" value="${resRatioData.averageDose}" pattern="#0.00"/></td>
	                   <td class="report_data_number">${resRatioData.pnum}</td>
	                   <td class="report_data_number">${resRatioData.lsnum}</td>
	               </tr>
	               <tr class="mobileReport_table_body">
	               		<td class="<c:if test="${resRatioData.inRateRatio>0}">ratio_up</c:if><c:if test="${resRatioData.inRateRatio<0}">ratio_down</c:if>" rowspan="2">
	               			<span class="narrow_font">
		               			<c:if test="${resRatioData.inRateRatio>0}">+</c:if>
		               			<c:if test="${resRatioData.inRateRatio<0}">-</c:if>
	               			</span>
	               			<fmt:formatNumber type="percent" value="${resRatioData.inRateRatio<0?-resRatioData.inRateRatio:resRatioData.inRateRatio}" pattern="#0%"/>
	               		</td>
				    	<td class="<c:if test="${resRatioData.whRateRatio>0}">ratio_up</c:if><c:if test="${resRatioData.whRateRatio<0}">ratio_down</c:if>" rowspan="2">
				    		<span class="narrow_font">
					    		<c:if test="${resRatioData.whRateRatio>0}">+</c:if>
		               			<c:if test="${resRatioData.whRateRatio<0}">-</c:if>
	               			</span>
				    		<fmt:formatNumber type="percent" value="${resRatioData.whRateRatio<0?-resRatioData.whRateRatio:resRatioData.whRateRatio}" pattern="#0%"/>
				    	</td>
				    	<td class="<c:if test="${resRatioData.averageDoseRatio>0}">ratio_up</c:if><c:if test="${resRatioData.averageDoseRatio<0}">ratio_down</c:if>" rowspan="2">
				    		<span class="narrow_font">
					    		<c:if test="${resRatioData.averageDoseRatio>0}">+</c:if>
		               			<c:if test="${resRatioData.averageDoseRatio<0}">-</c:if>
	               			</span>
				    		<fmt:formatNumber type="percent" value="${resRatioData.averageDoseRatio<0?-resRatioData.averageDoseRatio:resRatioData.averageDoseRatio}" pattern="#0%"/>
				    	</td>
				   		<td class="<c:if test="${resRatioData.patNumRatio>0}">ratio_up_noimg</c:if><c:if test="${resRatioData.patNumRatio<0}">ratio_down_noimg</c:if>">
				   			<fmt:formatNumber type="percent" value="${resRatioData.patNumRatio<0?-resRatioData.patNumRatio:resRatioData.patNumRatio}" pattern="#0%"/>
				   		</td>
				   		<td class="<c:if test="${resRatioData.lsNumRatio>0}">ratio_up_noimg</c:if><c:if test="${resRatioData.lsNumRatio<0}">ratio_down_noimg</c:if>">
				   			<fmt:formatNumber type="percent" value="${resRatioData.lsNumRatio<0?-resRatioData.lsNumRatio:resRatioData.lsNumRatio}" pattern="#0%"/>
				   		</td>
				   </tr>
	            </table>
            </div>
        </div>
        <iframe src="${hospitalReportFile}" class="weeklyReport_iframe"></iframe>
        <jsp:include page="page_footer.jsp">
            <jsp:param value="<%=basePath%>" name="basePath"/>
            <jsp:param value="doHospitalSearch" name="backURL"/>
            <jsp:param value="${hospitalKeyword}" name="hospitalKeyword"/>
        </jsp:include>
    </div>
</body>  
</html>  
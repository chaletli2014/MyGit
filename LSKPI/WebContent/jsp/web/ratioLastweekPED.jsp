<%@page import="com.chalet.lskpi.utils.LsAttributes"%>
<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %> 
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE HTML>
<html lang="en-US">
<%@include file="header.jsp" %> 
<script type="text/javascript">
function submitForm(){
	if(checkForm()){
		$.mobile.showPageLoadingMsg('b','数据提交中',false);
		$('#lowerForm').submit();
	}
}
function checkForm(){
	if( !checkIsNotNull( $("#lowUser") ) ){
		showCustomrizedMessage("请选择一个下级");
		return false;
	}
	return true;
}
</script>
<body onload="checkMessage('${message}')">
    <div style="position:absolute; left:-9999px;"><a href="#" id="setfoc"></a></div>
    <div data-role="page" id="home">
        <jsp:include page="page_header.jsp" flush="true">
            <jsp:param name="title" value="儿科上周数据环比"/>
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
						<td width="16%">门诊人数</td>
						<td width="16%">雾化人次</td>
	               </tr>
	               <c:forEach items="${weeklyRatioData}" var="ratioData" varStatus="status">
		               <tr class="mobileReport_table_body <c:if test="${status.count%2==0}">mobileReport_tr_even</c:if>">
		                   <td rowspan="2">${ratioData.name}</td>
		                   <td class="report_data_number"><fmt:formatNumber type="percent" value="${ratioData.inRate}" pattern="#0%"/></td>
		                   <td class="report_data_number"><fmt:formatNumber type="percent" value="${ratioData.whRate}" pattern="#0%"/></td>
		                   <td class="report_data_number"><fmt:formatNumber type="percent" value="${ratioData.averageDose}" pattern="#0.00"/></td>
		                   <td class="report_data_number"><fmt:formatNumber value="${ratioData.pnum}" pattern="#,###" /></td>
		                   <td class="report_data_number"><fmt:formatNumber value="${ratioData.lsnum}" pattern="#,###" /></td>
		               </tr>
		               <tr class="mobileReport_table_body <c:if test="${status.count%2==0}">mobileReport_tr_even</c:if>">
		               		<td class="<c:if test="${ratioData.inRateRatio>0}">ratio_up</c:if><c:if test="${ratioData.inRateRatio<0}">ratio_down</c:if>">
			               		<span class="narrow_font">
				               		<c:if test="${ratioData.inRateRatio>0}">+</c:if>
			               			<c:if test="${ratioData.inRateRatio<0}">-</c:if>
			               		</span>
		               			<fmt:formatNumber type="percent" value="${ratioData.inRateRatio<0?-ratioData.inRateRatio:ratioData.inRateRatio}" pattern="#0%"/>
		               		</td>
					    	<td class="<c:if test="${ratioData.whRateRatio>0}">ratio_up</c:if><c:if test="${ratioData.whRateRatio<0}">ratio_down</c:if>">
					    		<span class="narrow_font">
						    		<c:if test="${ratioData.whRateRatio>0}">+</c:if>
			               			<c:if test="${ratioData.whRateRatio<0}">-</c:if>
			               		</span>
					    		<fmt:formatNumber type="percent" value="${ratioData.whRateRatio<0?-ratioData.whRateRatio:ratioData.whRateRatio}" pattern="#0%"/>
					    	</td>
					    	<td class="<c:if test="${ratioData.averageDoseRatio>0}">ratio_up</c:if><c:if test="${ratioData.averageDoseRatio<0}">ratio_down</c:if>">
					    		<span class="narrow_font">
						    		<c:if test="${ratioData.averageDoseRatio>0}">+</c:if>
			               			<c:if test="${ratioData.averageDoseRatio<0}">-</c:if>
			               		</span>
					    		<fmt:formatNumber type="percent" value="${ratioData.averageDoseRatio<0?-ratioData.averageDoseRatio:ratioData.averageDoseRatio}" pattern="#0%"/>
					    	</td>
					   		<td class="<c:if test="${ratioData.patNumRatio>0}">ratio_up_noimg</c:if><c:if test="${ratioData.patNumRatio<0}">ratio_down_noimg</c:if>">
					   			<span class="narrow_font">
						    		<c:if test="${ratioData.patNumRatio>0}">+</c:if>
			               			<c:if test="${ratioData.patNumRatio<0}">-</c:if>
			               		</span>
					   			<fmt:formatNumber type="percent" value="${ratioData.patNumRatio<0?-ratioData.patNumRatio:ratioData.patNumRatio}" pattern="#0%"/>
					   		</td>
					   		<td class="<c:if test="${ratioData.lsNumRatio>0}">ratio_up_noimg</c:if><c:if test="${ratioData.lsNumRatio<0}">ratio_down_noimg</c:if>">
					   			<span class="narrow_font">
						    		<c:if test="${ratioData.lsNumRatio>0}">+</c:if>
			               			<c:if test="${ratioData.lsNumRatio<0}">-</c:if>
			               		</span>
					   			<fmt:formatNumber type="percent" value="${ratioData.lsNumRatio<0?-ratioData.lsNumRatio:ratioData.lsNumRatio}" pattern="#0%"/>
					   		</td>
					   </tr>
	               </c:forEach>
	               <c:if test="${countoryRatioData!=null}">
	               	   <tr class="mobileReport_table_body <c:if test="${fn:length(weeklyRatioData)%2 != 0}">mobileReport_tr_even</c:if>">
		                   <td rowspan="2">全国</td>
		                   <td class="report_data_number"><fmt:formatNumber type="percent" value="${countoryRatioData.inRate}" pattern="#0%"/></td>
		                   <td class="report_data_number"><fmt:formatNumber type="percent" value="${countoryRatioData.whRate}" pattern="#0%"/></td>
		                   <td class="report_data_number"><fmt:formatNumber type="percent" value="${countoryRatioData.averageDose}" pattern="#0.00"/></td>
		                   <td class="report_data_number"><fmt:formatNumber value="${countoryRatioData.pnum}" pattern="#,###" /></td>
		                   <td class="report_data_number"><fmt:formatNumber value="${countoryRatioData.lsnum}" pattern="#,###" /></td>
		               </tr>
		               <tr class="mobileReport_table_body <c:if test="${fn:length(weeklyRatioData)%2 != 0}">mobileReport_tr_even</c:if>">
		               		<td class="<c:if test="${countoryRatioData.inRateRatio>0}">ratio_up</c:if><c:if test="${countoryRatioData.inRateRatio<0}">ratio_down</c:if>">
			               		<span class="narrow_font">
				               		<c:if test="${countoryRatioData.inRateRatio>0}">+</c:if>
			               			<c:if test="${countoryRatioData.inRateRatio<0}">-</c:if>
			               		</span>
		               			<fmt:formatNumber type="percent" value="${countoryRatioData.inRateRatio<0?-countoryRatioData.inRateRatio:countoryRatioData.inRateRatio}" pattern="#0%"/>
		               		</td>
					    	<td class="<c:if test="${countoryRatioData.whRateRatio>0}">ratio_up</c:if><c:if test="${countoryRatioData.whRateRatio<0}">ratio_down</c:if>">
					    		<span class="narrow_font">
						    		<c:if test="${countoryRatioData.whRateRatio>0}">+</c:if>
			               			<c:if test="${countoryRatioData.whRateRatio<0}">-</c:if>
			               		</span>
					    		<fmt:formatNumber type="percent" value="${countoryRatioData.whRateRatio<0?-countoryRatioData.whRateRatio:countoryRatioData.whRateRatio}" pattern="#0%"/>
					    	</td>
					    	<td class="<c:if test="${countoryRatioData.averageDoseRatio>0}">ratio_up</c:if><c:if test="${countoryRatioData.averageDoseRatio<0}">ratio_down</c:if>">
					    		<span class="narrow_font">
						    		<c:if test="${countoryRatioData.averageDoseRatio>0}">+</c:if>
			               			<c:if test="${countoryRatioData.averageDoseRatio<0}">-</c:if>
			               		</span>
					    		<fmt:formatNumber type="percent" value="${countoryRatioData.averageDoseRatio<0?-countoryRatioData.averageDoseRatio:countoryRatioData.averageDoseRatio}" pattern="#0%"/>
					    	</td>
					   		<td class="<c:if test="${countoryRatioData.patNumRatio>0}">ratio_up_noimg</c:if><c:if test="${countoryRatioData.patNumRatio<0}">ratio_down_noimg</c:if>">
					   			<span class="narrow_font">
						    		<c:if test="${countoryRatioData.patNumRatio>0}">+</c:if>
			               			<c:if test="${countoryRatioData.patNumRatio<0}">-</c:if>
			               		</span>
					   			<fmt:formatNumber type="percent" value="${countoryRatioData.patNumRatio<0?-countoryRatioData.patNumRatio:countoryRatioData.patNumRatio}" pattern="#0%"/>
					   		</td>
					   		<td class="<c:if test="${countoryRatioData.lsNumRatio>0}">ratio_up_noimg</c:if><c:if test="${countoryRatioData.lsNumRatio<0}">ratio_down_noimg</c:if>">
					   			<span class="narrow_font">
						    		<c:if test="${countoryRatioData.lsNumRatio>0}">+</c:if>
			               			<c:if test="${countoryRatioData.lsNumRatio<0}">-</c:if>
			               		</span>
					   			<fmt:formatNumber type="percent" value="${countoryRatioData.lsNumRatio<0?-countoryRatioData.lsNumRatio:countoryRatioData.lsNumRatio}" pattern="#0%"/>
					   		</td>
					   </tr>
	               </c:if>
	            </table>
            </div>
            <div class="roundCorner" style="padding:4px;">
            	<div class="dailyReport_table_Title">查看下级上周数据环比</div>
           		<form id="lowerForm" action="showLowerPEDReport" method="POST" data-ajax="false" class="validate">
            		<div data-role="fieldcontain">
	                    <label for="lowUser" class="select">下级列表</label>
	                    <select name="lowUser" id="lowUser">
	                        <option value="">--请选择--</option>
		                    <c:forEach var="lowerUser" items="${lowerUsers}">
		                        <option value="${lowerUser.userCode}">${lowerUser.name}</option>
		                    </c:forEach>
		                </select>
                	</div>
           		</form>
                <div style="text-align: center;">
		            <img alt="" src="<%=basePath%>images/button_submit.png" style="cursor: pointer;" onclick="submitForm()"/>
	            </div>
            </div>
        </div>
        <jsp:include page="page_footer.jsp">
            <jsp:param value="<%=basePath%>" name="basePath"/>
            <jsp:param value="ratioLastweek" name="backURL"/>
        </jsp:include>
    </div>
</body>  
</html>  
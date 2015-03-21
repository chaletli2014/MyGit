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
		$.mobile.showPageLoadingMsg('b','数据检索中',false);
		$('#lowerMonthlyForm').submit();
	}
}
function checkForm(){
	if( !checkIsNotNull( $("#lowUser") ) ){
		showCustomrizedMessage("请选择一个下级");
		return false;
	}
	return true;
}
function submitBMForm(){
	if(checkBMForm()){
		$.mobile.showPageLoadingMsg('b','数据检索中',false);
		$('#bmMonthlyForm').submit();
	}
}
function checkBMForm(){
	if( !checkIsNotNull( $("#rsdSelect") ) ){
		showCustomrizedMessage("请选择一个RSD");
		return false;
	}
	return true;
}
$(function(){
	$("#rsdSelect").unbind("change", eDropLangBMChange).bind("change", eDropLangBMChange);
	$("#rsmSelect").unbind("change", eDropFrameBMChange).bind("change", eDropFrameBMChange);
});
</script>
<body onload="checkMessage('${message}')">
    <div style="position:absolute; left:-9999px;"><a href="#" id="setfoc"></a></div>
    <div data-role="page" id="home">
        <jsp:include page="page_header.jsp" flush="true">
            <jsp:param name="title" value="销售袋数月报"/>
            <jsp:param name="basePath" value="<%=basePath%>"/>
        </jsp:include>
        <div data-role="content"  data-theme="a">
            <div class="roundCorner" style="padding:4px;">
                <div class="dailyReport_table_Title">${selfTitle}</div>
	            <table class="mobileReport_table">
	               <tr class="mobileReport_table_header">
				      <td width="15%">姓名</td>
                      <td width="12%">儿科病房</td>
                      <td width="12%">儿科门急诊</td>
                      <td width="12%">家庭雾化</td>
                      <td width="12%">呼吸科病房</td>
                      <td width="12%">呼吸科门急诊</td>
                      <td width="12%">其他科室</td>
                      <td width="13%">总袋数</td>
				    </tr>
	               <c:forEach items="${monthlyRatioList}" var="monthlyRatio" varStatus="status">
		               <tr class="mobileReport_table_body <c:if test="${status.count%2==0}">mobileReport_tr_even</c:if>">
					      <td class="report_data_number" rowspan="2">
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
                          <td class="report_data_number" ><fmt:formatNumber value="${monthlyRatio.pedRoomDrugStoreWh}" pattern="#,###"/></td>
                          <td class="report_data_number" ><fmt:formatNumber value="${monthlyRatio.pedEmerWh}" pattern="#,###"/></td>
                          <td class="report_data_number" ><fmt:formatNumber value="${monthlyRatio.homeWh}" pattern="#,###"/></td>
                          <td class="report_data_number" ><fmt:formatNumber value="${monthlyRatio.resRoom}" pattern="#,###"/></td>
                          <td class="report_data_number" ><fmt:formatNumber value="${monthlyRatio.resClinic}" pattern="#,###"/></td>
                          <td class="report_data_number" ><fmt:formatNumber value="${monthlyRatio.othernum}" pattern="#,###"/></td>
                          <td class="report_data_number" ><fmt:formatNumber value="${monthlyRatio.totalnum}" pattern="#,###"/></td>
					   </tr>
					   <tr class="mobileReport_table_body <c:if test="${status.count%2==0}">mobileReport_tr_even</c:if>">
					      <td class="report_data_number <c:if test="${monthlyRatio.pedRoomDrugStoreWhRatio>0}">ratio_up</c:if><c:if test="${monthlyRatio.pedRoomDrugStoreWhRatio<0}">ratio_down</c:if>" >
					       		<span class="narrow_font">
						    		<c:if test="${monthlyRatio.pedRoomDrugStoreWhRatio>0}">+</c:if>
			               			<c:if test="${monthlyRatio.pedRoomDrugStoreWhRatio<0}">-</c:if>
			               		</span>
					       		<fmt:formatNumber type="percent" value="${monthlyRatio.pedRoomDrugStoreWhRatio<0?-monthlyRatio.pedRoomDrugStoreWhRatio:monthlyRatio.pedRoomDrugStoreWhRatio}" pattern="#0%"/>
					       </td>
					      <td class="report_data_number <c:if test="${monthlyRatio.pedEmerWhRatio>0}">ratio_up</c:if><c:if test="${monthlyRatio.pedEmerWhRatio<0}">ratio_down</c:if>" >
					       		<span class="narrow_font">
						    		<c:if test="${monthlyRatio.pedEmerWhRatio>0}">+</c:if>
			               			<c:if test="${monthlyRatio.pedEmerWhRatio<0}">-</c:if>
			               		</span>
					       		<fmt:formatNumber type="percent" value="${monthlyRatio.pedEmerWhRatio<0?-monthlyRatio.pedEmerWhRatio:monthlyRatio.pedEmerWhRatio}" pattern="#0%"/>
					       </td>
					       <td class="report_data_number <c:if test="${monthlyRatio.homeWhRatio>0}">ratio_up</c:if><c:if test="${monthlyRatio.homeWhRatio<0}">ratio_down</c:if>" >
					       		<span class="narrow_font">
						    		<c:if test="${monthlyRatio.homeWhRatio>0}">+</c:if>
			               			<c:if test="${monthlyRatio.homeWhRatio<0}">-</c:if>
			               		</span>
					       		<fmt:formatNumber type="percent" value="${monthlyRatio.homeWhRatio<0?-monthlyRatio.homeWhRatio:monthlyRatio.homeWhRatio}" pattern="#0%"/>
					       </td>
					      <td class="report_data_number <c:if test="${monthlyRatio.resRoomRatio>0}">ratio_up</c:if><c:if test="${monthlyRatio.resRoomRatio<0}">ratio_down</c:if>" >
					       		<span class="narrow_font">
						    		<c:if test="${monthlyRatio.resRoomRatio>0}">+</c:if>
			               			<c:if test="${monthlyRatio.resRoomRatio<0}">-</c:if>
			               		</span>
					       		<fmt:formatNumber type="percent" value="${monthlyRatio.resRoomRatio<0?-monthlyRatio.resRoomRatio:monthlyRatio.resRoomRatio}" pattern="#0%"/>
					       </td>
					      <td class="report_data_number <c:if test="${monthlyRatio.resClinicRatio>0}">ratio_up</c:if><c:if test="${monthlyRatio.resClinicRatio<0}">ratio_down</c:if>" >
					       		<span class="narrow_font">
						    		<c:if test="${monthlyRatio.resClinicRatio>0}">+</c:if>
			               			<c:if test="${monthlyRatio.resClinicRatio<0}">-</c:if>
			               		</span>
					       		<fmt:formatNumber type="percent" value="${monthlyRatio.resClinicRatio<0?-monthlyRatio.resClinicRatio:monthlyRatio.resClinicRatio}" pattern="#0%"/>
					       </td>
					      <td class="report_data_number <c:if test="${monthlyRatio.othernumratio>0}">ratio_up</c:if><c:if test="${monthlyRatio.othernumratio<0}">ratio_down</c:if>" >
					       		<span class="narrow_font">
						    		<c:if test="${monthlyRatio.othernumratio>0}">+</c:if>
			               			<c:if test="${monthlyRatio.othernumratio<0}">-</c:if>
			               		</span>
					       		<fmt:formatNumber type="percent" value="${monthlyRatio.othernumratio<0?-monthlyRatio.othernumratio:monthlyRatio.othernumratio}" pattern="#0%"/>
					       </td>
					      <td class="report_data_number <c:if test="${monthlyRatio.totalnumratio>0}">ratio_up</c:if><c:if test="${monthlyRatio.totalnumratio<0}">ratio_down</c:if>" >
					       		<span class="narrow_font">
						    		<c:if test="${monthlyRatio.totalnumratio>0}">+</c:if>
			               			<c:if test="${monthlyRatio.totalnumratio<0}">-</c:if>
			               		</span>
					       		<fmt:formatNumber type="percent" value="${monthlyRatio.totalnumratio<0?-monthlyRatio.totalnumratio:monthlyRatio.totalnumratio}" pattern="#0%"/>
					       </td>
					    </tr>
	               </c:forEach>
	               <tr class="mobileReport_table_body <c:if test="${fn:length(monthlyRatioList)%2 != 0}">mobileReport_tr_even</c:if>">
                     <td class="report_data_number" rowspan="2">
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
                     <td class="report_data_number" ><fmt:formatNumber value="${superiorMonthlyRatio.pedRoomDrugStoreWh}" pattern="#,###"/></td>
                     <td class="report_data_number" ><fmt:formatNumber value="${superiorMonthlyRatio.pedEmerWh}" pattern="#,###"/></td>
                     <td class="report_data_number" ><fmt:formatNumber value="${superiorMonthlyRatio.homeWh}" pattern="#,###"/></td>
                     <td class="report_data_number" ><fmt:formatNumber value="${superiorMonthlyRatio.resRoom}" pattern="#,###"/></td>
                     <td class="report_data_number" ><fmt:formatNumber value="${superiorMonthlyRatio.resClinic}" pattern="#,###"/></td>
                     <td class="report_data_number" ><fmt:formatNumber value="${superiorMonthlyRatio.othernum}" pattern="#,###"/></td>
                     <td class="report_data_number" ><fmt:formatNumber value="${superiorMonthlyRatio.totalnum}" pattern="#,###"/></td>
                 </tr>
                 <tr class="mobileReport_table_body <c:if test="${fn:length(monthlyRatioList)%2 != 0}">mobileReport_tr_even</c:if>">
				      <td class="report_data_number <c:if test="${superiorMonthlyRatio.pedRoomDrugStoreWhRatio>0}">ratio_up</c:if><c:if test="${superiorMonthlyRatio.pedRoomDrugStoreWhRatio<0}">ratio_down</c:if>" >
				       		<span class="narrow_font">
					    		<c:if test="${superiorMonthlyRatio.pedRoomDrugStoreWhRatio>0}">+</c:if>
		               			<c:if test="${superiorMonthlyRatio.pedRoomDrugStoreWhRatio<0}">-</c:if>
		               		</span>
				       		<fmt:formatNumber type="percent" value="${superiorMonthlyRatio.pedRoomDrugStoreWhRatio<0?-superiorMonthlyRatio.pedRoomDrugStoreWhRatio:superiorMonthlyRatio.pedRoomDrugStoreWhRatio}" pattern="#0%"/>
				      </td>
				      <td class="report_data_number <c:if test="${superiorMonthlyRatio.pedEmerWhRatio>0}">ratio_up</c:if><c:if test="${superiorMonthlyRatio.pedEmerWhRatio<0}">ratio_down</c:if>" >
				       		<span class="narrow_font">
					    		<c:if test="${superiorMonthlyRatio.pedEmerWhRatio>0}">+</c:if>
		               			<c:if test="${superiorMonthlyRatio.pedEmerWhRatio<0}">-</c:if>
		               		</span>
				       		<fmt:formatNumber type="percent" value="${superiorMonthlyRatio.pedEmerWhRatio<0?-superiorMonthlyRatio.pedEmerWhRatio:superiorMonthlyRatio.pedEmerWhRatio}" pattern="#0%"/>
				      </td>
				      <td class="report_data_number <c:if test="${superiorMonthlyRatio.homeWhRatio>0}">ratio_up</c:if><c:if test="${superiorMonthlyRatio.homeWhRatio<0}">ratio_down</c:if>" >
				       		<span class="narrow_font">
					    		<c:if test="${superiorMonthlyRatio.homeWhRatio>0}">+</c:if>
		               			<c:if test="${superiorMonthlyRatio.homeWhRatio<0}">-</c:if>
		               		</span>
				       		<fmt:formatNumber type="percent" value="${superiorMonthlyRatio.homeWhRatio<0?-superiorMonthlyRatio.homeWhRatio:superiorMonthlyRatio.homeWhRatio}" pattern="#0%"/>
				      </td>
				      <td class="report_data_number <c:if test="${superiorMonthlyRatio.resRoomRatio>0}">ratio_up</c:if><c:if test="${superiorMonthlyRatio.resRoomRatio<0}">ratio_down</c:if>" >
				       		<span class="narrow_font">
					    		<c:if test="${superiorMonthlyRatio.resRoomRatio>0}">+</c:if>
		               			<c:if test="${superiorMonthlyRatio.resRoomRatio<0}">-</c:if>
		               		</span>
				       		<fmt:formatNumber type="percent" value="${superiorMonthlyRatio.resRoomRatio<0?-superiorMonthlyRatio.resRoomRatio:superiorMonthlyRatio.resRoomRatio}" pattern="#0%"/>
				      </td>
				      <td class="report_data_number <c:if test="${superiorMonthlyRatio.resClinicRatio>0}">ratio_up</c:if><c:if test="${superiorMonthlyRatio.resClinicRatio<0}">ratio_down</c:if>" >
				       		<span class="narrow_font">
					    		<c:if test="${superiorMonthlyRatio.resClinicRatio>0}">+</c:if>
		               			<c:if test="${superiorMonthlyRatio.resClinicRatio<0}">-</c:if>
		               		</span>
				       		<fmt:formatNumber type="percent" value="${superiorMonthlyRatio.resClinicRatio<0?-superiorMonthlyRatio.resClinicRatio:superiorMonthlyRatio.resClinicRatio}" pattern="#0%"/>
				     </td>
                     <td class="report_data_number <c:if test="${superiorMonthlyRatio.othernumratio>0}">ratio_up</c:if><c:if test="${superiorMonthlyRatio.othernumratio<0}">ratio_down</c:if>" >
                      		<span class="narrow_font">
					    		<c:if test="${superiorMonthlyRatio.othernumratio>0}">+</c:if>
		               			<c:if test="${superiorMonthlyRatio.othernumratio<0}">-</c:if>
		               		</span>
                      		<fmt:formatNumber type="percent" value="${superiorMonthlyRatio.othernumratio<0?-superiorMonthlyRatio.othernumratio:superiorMonthlyRatio.othernumratio}" pattern="#0%"/>
                      </td>
                     <td class="report_data_number <c:if test="${superiorMonthlyRatio.totalnumratio>0}">ratio_up</c:if><c:if test="${superiorMonthlyRatio.totalnumratio<0}">ratio_down</c:if>" >
                      		<span class="narrow_font">
					    		<c:if test="${superiorMonthlyRatio.totalnumratio>0}">+</c:if>
		               			<c:if test="${superiorMonthlyRatio.totalnumratio<0}">-</c:if>
		               		</span>
                      		<fmt:formatNumber type="percent" value="${superiorMonthlyRatio.totalnumratio<0?-superiorMonthlyRatio.totalnumratio:superiorMonthlyRatio.totalnumratio}" pattern="#0%"/>
                      </td>
                   </tr>
	            </table>
	            <table class="mobileReport_table">
	               <tr class="mobileReport_table_header">
				      <td width="10%">姓名</td>
                      <td width="10%">儿科病房占比</td>
                      <td width="10%">儿科门急诊占比</td>
                      <td width="10%">家庭雾化占比</td>
                      <td width="10%">呼吸科病房占比</td>
                      <td width="10%">呼吸科门急诊占比</td>
                      <td width="10%">其他科室占比</td>
                      <td width="10%">袋数上报率</td>
                      <td width="10%">上报医院数</td>
                      <td width="10%">总医院数</td>
				    </tr>
	               <c:forEach items="${monthlyRatioList}" var="monthlyRatio" varStatus="status">
		               <tr class="mobileReport_table_body <c:if test="${status.count%2==0}">mobileReport_tr_even</c:if>">
					      <td class="report_data_number" rowspan="2">
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
					      <td class="report_data_number" ><fmt:formatNumber type="percent" value="${monthlyRatio.pedRoomDrugStoreWhRate}" pattern="#0%"/></td>
					      <td class="report_data_number" ><fmt:formatNumber type="percent" value="${monthlyRatio.pedEmerWhRate}" pattern="#0%"/></td>
					      <td class="report_data_number" ><fmt:formatNumber type="percent" value="${monthlyRatio.homeWhRate}" pattern="#0%"/></td>
					      <td class="report_data_number" ><fmt:formatNumber type="percent" value="${monthlyRatio.resRoomRate}" pattern="#0%"/></td>
					      <td class="report_data_number" ><fmt:formatNumber type="percent" value="${monthlyRatio.resClinicRate}" pattern="#0%"/></td>
					      <td class="report_data_number" ><fmt:formatNumber type="percent" value="${monthlyRatio.othernumrate}" pattern="#0%"/></td>
					      <td class="report_data_number" ><fmt:formatNumber type="percent" value="${monthlyRatio.inrate}" pattern="#0%"/></td>
					      <td class="report_data_number"><fmt:formatNumber value="${monthlyRatio.innum}" pattern="#,###"/></td>
	                      <td class="report_data_number"><fmt:formatNumber value="${monthlyRatio.hosnum}" pattern="#,###"/></td>
					  </tr>
					  <tr class="mobileReport_table_body <c:if test="${status.count%2==0}">mobileReport_tr_even</c:if>">
					      <td class="report_data_number <c:if test="${monthlyRatio.pedRoomDrugStoreWhRateRatio>0}">ratio_up</c:if><c:if test="${monthlyRatio.pedRoomDrugStoreWhRateRatio<0}">ratio_down</c:if>" >
					       		<span class="narrow_font">
						    		<c:if test="${monthlyRatio.pedRoomDrugStoreWhRateRatio>0}">+</c:if>
			               			<c:if test="${monthlyRatio.pedRoomDrugStoreWhRateRatio<0}">-</c:if>
			               		</span>
					       		<fmt:formatNumber type="percent" value="${monthlyRatio.pedRoomDrugStoreWhRateRatio<0?-monthlyRatio.pedRoomDrugStoreWhRateRatio:monthlyRatio.pedRoomDrugStoreWhRateRatio}" pattern="#0%"/>
					       </td>
					      <td class="report_data_number <c:if test="${monthlyRatio.pedEmerWhRateRatio>0}">ratio_up</c:if><c:if test="${monthlyRatio.pedEmerWhRateRatio<0}">ratio_down</c:if>" >
					       		<span class="narrow_font">
						    		<c:if test="${monthlyRatio.pedEmerWhRateRatio>0}">+</c:if>
			               			<c:if test="${monthlyRatio.pedEmerWhRateRatio<0}">-</c:if>
			               		</span>
					       		<fmt:formatNumber type="percent" value="${monthlyRatio.pedEmerWhRateRatio<0?-monthlyRatio.pedEmerWhRateRatio:monthlyRatio.pedEmerWhRateRatio}" pattern="#0%"/>
					       </td>
					      <td class="report_data_number <c:if test="${monthlyRatio.homeWhRateRatio>0}">ratio_up</c:if><c:if test="${monthlyRatio.homeWhRateRatio<0}">ratio_down</c:if>" >
					       		<span class="narrow_font">
						    		<c:if test="${monthlyRatio.homeWhRateRatio>0}">+</c:if>
			               			<c:if test="${monthlyRatio.homeWhRateRatio<0}">-</c:if>
			               		</span>
					       		<fmt:formatNumber type="percent" value="${monthlyRatio.homeWhRateRatio<0?-monthlyRatio.homeWhRateRatio:monthlyRatio.homeWhRateRatio}" pattern="#0%"/>
					       </td>
					      <td class="report_data_number <c:if test="${monthlyRatio.resRoomRateRatio>0}">ratio_up</c:if><c:if test="${monthlyRatio.resRoomRateRatio<0}">ratio_down</c:if>" >
					       		<span class="narrow_font">
						    		<c:if test="${monthlyRatio.resRoomRateRatio>0}">+</c:if>
			               			<c:if test="${monthlyRatio.resRoomRateRatio<0}">-</c:if>
			               		</span>
					       		<fmt:formatNumber type="percent" value="${monthlyRatio.resRoomRateRatio<0?-monthlyRatio.resRoomRateRatio:monthlyRatio.resRoomRateRatio}" pattern="#0%"/>
					       </td>
					      <td class="report_data_number <c:if test="${monthlyRatio.resClinicRateRatio>0}">ratio_up</c:if><c:if test="${monthlyRatio.resClinicRateRatio<0}">ratio_down</c:if>" >
					       		<span class="narrow_font">
						    		<c:if test="${monthlyRatio.resClinicRateRatio>0}">+</c:if>
			               			<c:if test="${monthlyRatio.resClinicRateRatio<0}">-</c:if>
			               		</span>
					       		<fmt:formatNumber type="percent" value="${monthlyRatio.resClinicRateRatio<0?-monthlyRatio.resClinicRateRatio:monthlyRatio.resClinicRateRatio}" pattern="#0%"/>
					       </td>
					      <td class="report_data_number <c:if test="${monthlyRatio.othernumrateratio>0}">ratio_up</c:if><c:if test="${monthlyRatio.othernumrateratio<0}">ratio_down</c:if>" >
					       		<span class="narrow_font">
						    		<c:if test="${monthlyRatio.othernumrateratio>0}">+</c:if>
			               			<c:if test="${monthlyRatio.othernumrateratio<0}">-</c:if>
			               		</span>
					       		<fmt:formatNumber type="percent" value="${monthlyRatio.othernumrateratio<0?-monthlyRatio.othernumrateratio:monthlyRatio.othernumrateratio}" pattern="#0%"/>
					      </td>
					      <td class="report_data_number <c:if test="${monthlyRatio.inrateratio>0}">ratio_up</c:if><c:if test="${monthlyRatio.inrateratio<0}">ratio_down</c:if>" >
					       		<span class="narrow_font">
						    		<c:if test="${monthlyRatio.inrateratio>0}">+</c:if>
			               			<c:if test="${monthlyRatio.inrateratio<0}">-</c:if>
			               		</span>
					       		<fmt:formatNumber type="percent" value="${monthlyRatio.inrateratio<0?-monthlyRatio.inrateratio:monthlyRatio.inrateratio}" pattern="#0%"/>
					      </td>
					      <td class="report_data_number <c:if test="${monthlyRatio.innumratio>0}">ratio_up</c:if><c:if test="${monthlyRatio.innumratio<0}">ratio_down</c:if>" >
					       		<span class="narrow_font">
						    		<c:if test="${monthlyRatio.innumratio>0}">+</c:if>
			               			<c:if test="${monthlyRatio.innumratio<0}">-</c:if>
			               		</span>
					       		<fmt:formatNumber type="percent" value="${monthlyRatio.innumratio<0?-monthlyRatio.innumratio:monthlyRatio.innumratio}" pattern="#0%"/>
					      </td>
					      <td class="report_data_number <c:if test="${monthlyRatio.hosnumratio>0}">ratio_up</c:if><c:if test="${monthlyRatio.hosnumratio<0}">ratio_down</c:if>" >
					       		<span class="narrow_font">
						    		<c:if test="${monthlyRatio.hosnumratio>0}">+</c:if>
			               			<c:if test="${monthlyRatio.hosnumratio<0}">-</c:if>
			               		</span>
					       		<fmt:formatNumber type="percent" value="${monthlyRatio.hosnumratio<0?-monthlyRatio.hosnumratio:monthlyRatio.hosnumratio}" pattern="#0%"/>
					      </td>
					    </tr>
	               </c:forEach>
	               <tr class="mobileReport_table_body <c:if test="${fn:length(monthlyRatioList)%2 != 0}">mobileReport_tr_even</c:if>">
                     <td class="report_data_number" rowspan="2">
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
				     <td class="report_data_number" ><fmt:formatNumber type="percent" value="${superiorMonthlyRatio.pedRoomDrugStoreWhRate}" pattern="#0%"/></td>
				     <td class="report_data_number" ><fmt:formatNumber type="percent" value="${superiorMonthlyRatio.pedEmerWhRate}" pattern="#0%"/></td>
				     <td class="report_data_number" ><fmt:formatNumber type="percent" value="${superiorMonthlyRatio.homeWhRate}" pattern="#0%"/></td>
				     <td class="report_data_number" ><fmt:formatNumber type="percent" value="${superiorMonthlyRatio.resRoomRate}" pattern="#0%"/></td>
				     <td class="report_data_number" ><fmt:formatNumber type="percent" value="${superiorMonthlyRatio.resClinicRate}" pattern="#0%"/></td>
                     <td class="report_data_number" ><fmt:formatNumber type="percent" value="${superiorMonthlyRatio.othernumrate}" pattern="#0%"/></td>
                     <td class="report_data_number" ><fmt:formatNumber type="percent" value="${superiorMonthlyRatio.inrate}" pattern="#0%"/></td>
				     <td class="report_data_number"><fmt:formatNumber value="${superiorMonthlyRatio.innum}" pattern="#,###"/></td>
	                 <td class="report_data_number"><fmt:formatNumber value="${superiorMonthlyRatio.hosnum}" pattern="#,###"/></td>
				 </tr>
				 <tr class="mobileReport_table_body <c:if test="${fn:length(monthlyRatioList)%2 != 0}">mobileReport_tr_even</c:if>">
			     	<td class="report_data_number <c:if test="${superiorMonthlyRatio.pedRoomDrugStoreWhRateRatio>0}">ratio_up</c:if><c:if test="${superiorMonthlyRatio.pedRoomDrugStoreWhRateRatio<0}">ratio_down</c:if>" >
			       		<span class="narrow_font">
				    		<c:if test="${superiorMonthlyRatio.pedRoomDrugStoreWhRateRatio>0}">+</c:if>
	               			<c:if test="${superiorMonthlyRatio.pedRoomDrugStoreWhRateRatio<0}">-</c:if>
	               		</span>
			       		<fmt:formatNumber type="percent" value="${superiorMonthlyRatio.pedRoomDrugStoreWhRateRatio<0?-superiorMonthlyRatio.pedRoomDrugStoreWhRateRatio:superiorMonthlyRatio.pedRoomDrugStoreWhRateRatio}" pattern="#0%"/>
			       	</td>
			      	<td class="report_data_number <c:if test="${superiorMonthlyRatio.pedEmerWhRateRatio>0}">ratio_up</c:if><c:if test="${superiorMonthlyRatio.pedEmerWhRateRatio<0}">ratio_down</c:if>" >
			       		<span class="narrow_font">
				    		<c:if test="${superiorMonthlyRatio.pedEmerWhRateRatio>0}">+</c:if>
	               			<c:if test="${superiorMonthlyRatio.pedEmerWhRateRatio<0}">-</c:if>
	               		</span>
			       		<fmt:formatNumber type="percent" value="${superiorMonthlyRatio.pedEmerWhRateRatio<0?-superiorMonthlyRatio.pedEmerWhRateRatio:superiorMonthlyRatio.pedEmerWhRateRatio}" pattern="#0%"/>
			       	</td>
			      	<td class="report_data_number <c:if test="${superiorMonthlyRatio.homeWhRateRatio>0}">ratio_up</c:if><c:if test="${superiorMonthlyRatio.homeWhRateRatio<0}">ratio_down</c:if>" >
			       		<span class="narrow_font">
				    		<c:if test="${superiorMonthlyRatio.homeWhRateRatio>0}">+</c:if>
	               			<c:if test="${superiorMonthlyRatio.homeWhRateRatio<0}">-</c:if>
	               		</span>
			       		<fmt:formatNumber type="percent" value="${superiorMonthlyRatio.homeWhRateRatio<0?-superiorMonthlyRatio.homeWhRateRatio:superiorMonthlyRatio.homeWhRateRatio}" pattern="#0%"/>
			       	</td>
			      	<td class="report_data_number <c:if test="${superiorMonthlyRatio.resRoomRateRatio>0}">ratio_up</c:if><c:if test="${superiorMonthlyRatio.resRoomRateRatio<0}">ratio_down</c:if>" >
			       		<span class="narrow_font">
				    		<c:if test="${superiorMonthlyRatio.resRoomRateRatio>0}">+</c:if>
	               			<c:if test="${superiorMonthlyRatio.resRoomRateRatio<0}">-</c:if>
	               		</span>
			       		<fmt:formatNumber type="percent" value="${superiorMonthlyRatio.resRoomRateRatio<0?-superiorMonthlyRatio.resRoomRateRatio:superiorMonthlyRatio.resRoomRateRatio}" pattern="#0%"/>
			       	</td>
			      	<td class="report_data_number <c:if test="${superiorMonthlyRatio.resClinicRateRatio>0}">ratio_up</c:if><c:if test="${superiorMonthlyRatio.resClinicRateRatio<0}">ratio_down</c:if>" >
			       		<span class="narrow_font">
				    		<c:if test="${superiorMonthlyRatio.resClinicRateRatio>0}">+</c:if>
	               			<c:if test="${superiorMonthlyRatio.resClinicRateRatio<0}">-</c:if>
	               		</span>
			       		<fmt:formatNumber type="percent" value="${superiorMonthlyRatio.resClinicRateRatio<0?-superiorMonthlyRatio.resClinicRateRatio:superiorMonthlyRatio.resClinicRateRatio}" pattern="#0%"/>
			       	</td>
                    <td class="report_data_number <c:if test="${superiorMonthlyRatio.othernumrateratio>0}">ratio_up</c:if><c:if test="${superiorMonthlyRatio.othernumrateratio<0}">ratio_down</c:if>" >
                      		<span class="narrow_font">
					    		<c:if test="${superiorMonthlyRatio.othernumrateratio>0}">+</c:if>
		               			<c:if test="${superiorMonthlyRatio.othernumrateratio<0}">-</c:if>
		               		</span>
                      		<fmt:formatNumber type="percent" value="${superiorMonthlyRatio.othernumrateratio<0?-superiorMonthlyRatio.othernumrateratio:superiorMonthlyRatio.othernumrateratio}" pattern="#0%"/>
                     </td>
                     <td class="report_data_number <c:if test="${superiorMonthlyRatio.inrateratio>0}">ratio_up</c:if><c:if test="${superiorMonthlyRatio.inrateratio<0}">ratio_down</c:if>" >
                      		<span class="narrow_font">
					    		<c:if test="${superiorMonthlyRatio.inrateratio>0}">+</c:if>
		               			<c:if test="${superiorMonthlyRatio.inrateratio<0}">-</c:if>
		               		</span>
                      		<fmt:formatNumber type="percent" value="${superiorMonthlyRatio.inrateratio<0?-superiorMonthlyRatio.inrateratio:superiorMonthlyRatio.inrateratio}" pattern="#0%"/>
                     </td>
                     <td class="report_data_number <c:if test="${superiorMonthlyRatio.innumratio>0}">ratio_up</c:if><c:if test="${superiorMonthlyRatio.innumratio<0}">ratio_down</c:if>" >
                      		<span class="narrow_font">
					    		<c:if test="${superiorMonthlyRatio.innumratio>0}">+</c:if>
		               			<c:if test="${superiorMonthlyRatio.innumratio<0}">-</c:if>
		               		</span>
                      		<fmt:formatNumber type="percent" value="${superiorMonthlyRatio.innumratio<0?-superiorMonthlyRatio.innumratio:superiorMonthlyRatio.innumratio}" pattern="#0%"/>
                     </td>
                     <td class="report_data_number <c:if test="${superiorMonthlyRatio.hosnumratio>0}">ratio_up</c:if><c:if test="${superiorMonthlyRatio.hosnumratio<0}">ratio_down</c:if>" >
                      		<span class="narrow_font">
					    		<c:if test="${superiorMonthlyRatio.hosnumratio>0}">+</c:if>
		               			<c:if test="${superiorMonthlyRatio.hosnumratio<0}">-</c:if>
		               		</span>
                      		<fmt:formatNumber type="percent" value="${superiorMonthlyRatio.hosnumratio<0?-superiorMonthlyRatio.hosnumratio:superiorMonthlyRatio.hosnumratio}" pattern="#0%"/>
                     </td>
                   </tr>
	            </table>
            </div>
            <c:if test="${'BM' != currentUser.level }">
            	<div class="roundCorner" style="padding:4px;">
	                <div class="dailyReport_table_Title">${childTitle}</div>
		            <table class="mobileReport_table">
		               <tr class="mobileReport_table_header">
					      <td width="15%">姓名</td>
	                      <td width="12%">儿科病房</td>
	                      <td width="12%">儿科门急诊</td>
	                      <td width="12%">家庭雾化</td>
	                      <td width="12%">呼吸科病房</td>
	                      <td width="12%">呼吸科门急诊</td>
	                      <td width="12%">其他科室</td>
	                      <td width="13%">总袋数</td>
					    </tr>
		               <c:forEach items="${childMonthlyRatioList}" var="childMonthlyRatio" varStatus="status">
			               <tr class="mobileReport_table_body <c:if test="${status.count%2==0}">mobileReport_tr_even</c:if>">
						      <td class="report_data_number" rowspan="2">
						      <c:if test="${'DSM' == currentUser.level}">
							      ${childMonthlyRatio.saleName}
						      </c:if>
						      <c:if test="${'RSM' == currentUser.level}">
							      ${childMonthlyRatio.dsmName}
						      </c:if>
						      <c:if test="${'RSD' == currentUser.level}">
							      ${childMonthlyRatio.rsmRegion}
						      </c:if>
						      </td>
	                          <td class="report_data_number" ><fmt:formatNumber value="${childMonthlyRatio.pedRoomDrugStoreWh}" pattern="#,###"/></td>
	                          <td class="report_data_number" ><fmt:formatNumber value="${childMonthlyRatio.pedEmerWh}" pattern="#,###"/></td>
	                          <td class="report_data_number" ><fmt:formatNumber value="${childMonthlyRatio.homeWh}" pattern="#,###"/></td>
	                          <td class="report_data_number" ><fmt:formatNumber value="${childMonthlyRatio.resRoom}" pattern="#,###"/></td>
	                          <td class="report_data_number" ><fmt:formatNumber value="${childMonthlyRatio.resClinic}" pattern="#,###"/></td>
						      <td class="report_data_number" ><fmt:formatNumber value="${childMonthlyRatio.othernum}" pattern="#,###"/></td>
						      <td class="report_data_number" ><fmt:formatNumber value="${childMonthlyRatio.totalnum}" pattern="#,###"/></td>
						    </tr>
						    <tr class="mobileReport_table_body <c:if test="${status.count%2==0}">mobileReport_tr_even</c:if>">
						      <td class="report_data_number <c:if test="${childMonthlyRatio.pedRoomDrugStoreWhRatio>0}">ratio_up</c:if><c:if test="${childMonthlyRatio.pedRoomDrugStoreWhRatio<0}">ratio_down</c:if>" >
						       		<span class="narrow_font">
							    		<c:if test="${childMonthlyRatio.pedRoomDrugStoreWhRatio>0}">+</c:if>
				               			<c:if test="${childMonthlyRatio.pedRoomDrugStoreWhRatio<0}">-</c:if>
				               		</span>
						       		<fmt:formatNumber type="percent" value="${childMonthlyRatio.pedRoomDrugStoreWhRatio<0?-childMonthlyRatio.pedRoomDrugStoreWhRatio:childMonthlyRatio.pedRoomDrugStoreWhRatio}" pattern="#0%"/>
						       </td>
						      <td class="report_data_number <c:if test="${childMonthlyRatio.pedEmerWhRatio>0}">ratio_up</c:if><c:if test="${childMonthlyRatio.pedEmerWhRatio<0}">ratio_down</c:if>" >
						       		<span class="narrow_font">
							    		<c:if test="${childMonthlyRatio.pedEmerWhRatio>0}">+</c:if>
				               			<c:if test="${childMonthlyRatio.pedEmerWhRatio<0}">-</c:if>
				               		</span>
						       		<fmt:formatNumber type="percent" value="${childMonthlyRatio.pedEmerWhRatio<0?-childMonthlyRatio.pedEmerWhRatio:childMonthlyRatio.pedEmerWhRatio}" pattern="#0%"/>
						       </td>
						      <td class="report_data_number <c:if test="${childMonthlyRatio.homeWhRatio>0}">ratio_up</c:if><c:if test="${childMonthlyRatio.homeWhRatio<0}">ratio_down</c:if>" >
						       		<span class="narrow_font">
							    		<c:if test="${childMonthlyRatio.homeWhRatio>0}">+</c:if>
				               			<c:if test="${childMonthlyRatio.homeWhRatio<0}">-</c:if>
				               		</span>
						       		<fmt:formatNumber type="percent" value="${childMonthlyRatio.homeWhRatio<0?-childMonthlyRatio.homeWhRatio:childMonthlyRatio.homeWhRatio}" pattern="#0%"/>
						       </td>
						      <td class="report_data_number <c:if test="${childMonthlyRatio.resRoomRatio>0}">ratio_up</c:if><c:if test="${childMonthlyRatio.resRoomRatio<0}">ratio_down</c:if>" >
						       		<span class="narrow_font">
							    		<c:if test="${childMonthlyRatio.resRoomRatio>0}">+</c:if>
				               			<c:if test="${childMonthlyRatio.resRoomRatio<0}">-</c:if>
				               		</span>
						       		<fmt:formatNumber type="percent" value="${childMonthlyRatio.resRoomRatio<0?-childMonthlyRatio.resRoomRatio:childMonthlyRatio.resRoomRatio}" pattern="#0%"/>
						       </td>
						      <td class="report_data_number <c:if test="${childMonthlyRatio.resClinicRatio>0}">ratio_up</c:if><c:if test="${childMonthlyRatio.resClinicRatio<0}">ratio_down</c:if>" >
						       		<span class="narrow_font">
							    		<c:if test="${childMonthlyRatio.resClinicRatio>0}">+</c:if>
				               			<c:if test="${childMonthlyRatio.resClinicRatio<0}">-</c:if>
				               		</span>
						       		<fmt:formatNumber type="percent" value="${childMonthlyRatio.resClinicRatio<0?-childMonthlyRatio.resClinicRatio:childMonthlyRatio.resClinicRatio}" pattern="#0%"/>
						       </td>
						      <td class="report_data_number <c:if test="${childMonthlyRatio.othernumratio>0}">ratio_up</c:if><c:if test="${childMonthlyRatio.othernumratio<0}">ratio_down</c:if>" >
						       		<span class="narrow_font">
							    		<c:if test="${childMonthlyRatio.othernumratio>0}">+</c:if>
				               			<c:if test="${childMonthlyRatio.othernumratio<0}">-</c:if>
				               		</span>
						       		<fmt:formatNumber type="percent" value="${childMonthlyRatio.othernumratio<0?-childMonthlyRatio.othernumratio:childMonthlyRatio.othernumratio}" pattern="#0%"/>
						       </td>
			                   <td class="report_data_number <c:if test="${childMonthlyRatio.totalnumratio>0}">ratio_up</c:if><c:if test="${childMonthlyRatio.totalnumratio<0}">ratio_down</c:if>" >
			                      		<span class="narrow_font">
								    		<c:if test="${childMonthlyRatio.totalnumratio>0}">+</c:if>
					               			<c:if test="${childMonthlyRatio.totalnumratio<0}">-</c:if>
					               		</span>
			                      		<fmt:formatNumber type="percent" value="${childMonthlyRatio.totalnumratio<0?-childMonthlyRatio.totalnumratio:childMonthlyRatio.totalnumratio}" pattern="#0%"/>
			                      </td>
						    </tr>
		               </c:forEach>
		            </table>
		            <table class="mobileReport_table">
		               	<tr class="mobileReport_table_header">
	                      <td width="10%">姓名</td>
	                      <td width="10%">儿科病房占比</td>
	                      <td width="10%">儿科门急诊占比</td>
	                      <td width="10%">家庭雾化占比</td>
	                      <td width="10%">呼吸科病房占比</td>
	                      <td width="10%">呼吸科门急诊占比</td>
	                      <td width="10%">其他科室占比</td>
	                      <td width="10%">袋数上报率</td>
	                      <td width="10%">上报医院数</td>
	                      <td width="10%">总医院数</td>
					    </tr>
		               <c:forEach items="${childMonthlyRatioList}" var="childMonthlyRatio" varStatus="status">
			               <tr class="mobileReport_table_body <c:if test="${status.count%2==0}">mobileReport_tr_even</c:if>">
						      <td class="report_data_number" rowspan="2">
						      <c:if test="${'DSM' == currentUser.level}">
							      ${childMonthlyRatio.saleName}
						      </c:if>
						      <c:if test="${'RSM' == currentUser.level}">
							      ${childMonthlyRatio.dsmName}
						      </c:if>
						      <c:if test="${'RSD' == currentUser.level}">
							      ${childMonthlyRatio.rsmRegion}
						      </c:if>
						      </td>
						      <td class="report_data_number" ><fmt:formatNumber type="percent" value="${childMonthlyRatio.pedRoomDrugStoreWhRate}" pattern="#0%"/></td>
						      <td class="report_data_number" ><fmt:formatNumber type="percent" value="${childMonthlyRatio.pedEmerWhRate}" pattern="#0%"/></td>
						      <td class="report_data_number" ><fmt:formatNumber type="percent" value="${childMonthlyRatio.homeWhRate}" pattern="#0%"/></td>
						      <td class="report_data_number" ><fmt:formatNumber type="percent" value="${childMonthlyRatio.resRoomRate}" pattern="#0%"/></td>
						      <td class="report_data_number" ><fmt:formatNumber type="percent" value="${childMonthlyRatio.resClinicRate}" pattern="#0%"/></td>
						      <td class="report_data_number" ><fmt:formatNumber type="percent" value="${childMonthlyRatio.othernumrate}" pattern="#0%"/></td>
						      <td class="report_data_number"><fmt:formatNumber value="${childMonthlyRatio.hosnum}" pattern="#,###"/></td>
				     		  <td class="report_data_number"><fmt:formatNumber value="${childMonthlyRatio.innum}" pattern="#,###"/></td>
						    </tr>
						    <tr class="mobileReport_table_body <c:if test="${status.count%2==0}">mobileReport_tr_even</c:if>">
						      <td class="report_data_number <c:if test="${childMonthlyRatio.pedRoomDrugStoreWhRateRatio>0}">ratio_up</c:if><c:if test="${childMonthlyRatio.pedRoomDrugStoreWhRateRatio<0}">ratio_down</c:if>" >
						       		<span class="narrow_font">
							    		<c:if test="${childMonthlyRatio.pedRoomDrugStoreWhRateRatio>0}">+</c:if>
				               			<c:if test="${childMonthlyRatio.pedRoomDrugStoreWhRateRatio<0}">-</c:if>
				               		</span>
						       		<fmt:formatNumber type="percent" value="${childMonthlyRatio.pedRoomDrugStoreWhRateRatio<0?-childMonthlyRatio.pedRoomDrugStoreWhRateRatio:childMonthlyRatio.pedRoomDrugStoreWhRateRatio}" pattern="#0%"/>
						       </td>
						      <td class="report_data_number <c:if test="${childMonthlyRatio.pedEmerWhRateRatio>0}">ratio_up</c:if><c:if test="${childMonthlyRatio.pedEmerWhRateRatio<0}">ratio_down</c:if>" >
						       		<span class="narrow_font">
							    		<c:if test="${childMonthlyRatio.pedEmerWhRateRatio>0}">+</c:if>
				               			<c:if test="${childMonthlyRatio.pedEmerWhRateRatio<0}">-</c:if>
				               		</span>
						       		<fmt:formatNumber type="percent" value="${childMonthlyRatio.pedEmerWhRateRatio<0?-childMonthlyRatio.pedEmerWhRateRatio:childMonthlyRatio.pedEmerWhRateRatio}" pattern="#0%"/>
						       </td>
						      <td class="report_data_number <c:if test="${childMonthlyRatio.homeWhRateRatio>0}">ratio_up</c:if><c:if test="${childMonthlyRatio.homeWhRateRatio<0}">ratio_down</c:if>" >
						       		<span class="narrow_font">
							    		<c:if test="${childMonthlyRatio.homeWhRateRatio>0}">+</c:if>
				               			<c:if test="${childMonthlyRatio.homeWhRateRatio<0}">-</c:if>
				               		</span>
						       		<fmt:formatNumber type="percent" value="${childMonthlyRatio.homeWhRateRatio<0?-childMonthlyRatio.homeWhRateRatio:childMonthlyRatio.homeWhRateRatio}" pattern="#0%"/>
						       </td>
						      <td class="report_data_number <c:if test="${childMonthlyRatio.resRoomRateRatio>0}">ratio_up</c:if><c:if test="${childMonthlyRatio.resRoomRateRatio<0}">ratio_down</c:if>" >
						       		<span class="narrow_font">
							    		<c:if test="${childMonthlyRatio.resRoomRateRatio>0}">+</c:if>
				               			<c:if test="${childMonthlyRatio.resRoomRateRatio<0}">-</c:if>
				               		</span>
						       		<fmt:formatNumber type="percent" value="${childMonthlyRatio.resRoomRateRatio<0?-childMonthlyRatio.resRoomRateRatio:childMonthlyRatio.resRoomRateRatio}" pattern="#0%"/>
						       </td>
						      <td class="report_data_number <c:if test="${childMonthlyRatio.resClinicRateRatio>0}">ratio_up</c:if><c:if test="${childMonthlyRatio.resClinicRateRatio<0}">ratio_down</c:if>" >
						       		<span class="narrow_font">
							    		<c:if test="${childMonthlyRatio.resClinicRateRatio>0}">+</c:if>
				               			<c:if test="${childMonthlyRatio.resClinicRateRatio<0}">-</c:if>
				               		</span>
						       		<fmt:formatNumber type="percent" value="${childMonthlyRatio.resClinicRateRatio<0?-childMonthlyRatio.resClinicRateRatio:childMonthlyRatio.resClinicRateRatio}" pattern="#0%"/>
						       </td>
						      <td class="report_data_number <c:if test="${childMonthlyRatio.othernumrateratio>0}">ratio_up</c:if><c:if test="${childMonthlyRatio.othernumrateratio<0}">ratio_down</c:if>" >
						       		<span class="narrow_font">
							    		<c:if test="${childMonthlyRatio.othernumrateratio>0}">+</c:if>
				               			<c:if test="${childMonthlyRatio.othernumrateratio<0}">-</c:if>
				               		</span>
						       		<fmt:formatNumber type="percent" value="${childMonthlyRatio.othernumrateratio<0?-childMonthlyRatio.othernumrateratio:childMonthlyRatio.othernumrateratio}" pattern="#0%"/>
						       </td>
						      <td class="report_data_number <c:if test="${childMonthlyRatio.innumratio>0}">ratio_up</c:if><c:if test="${childMonthlyRatio.innumratio<0}">ratio_down</c:if>" >
						       		<span class="narrow_font">
							    		<c:if test="${childMonthlyRatio.innumratio>0}">+</c:if>
				               			<c:if test="${childMonthlyRatio.innumratio<0}">-</c:if>
				               		</span>
						       		<fmt:formatNumber type="percent" value="${childMonthlyRatio.innumratio<0?-childMonthlyRatio.innumratio:childMonthlyRatio.innumratio}" pattern="#0%"/>
						      </td>
						       <td class="report_data_number <c:if test="${childMonthlyRatio.hosnumratio>0}">ratio_up</c:if><c:if test="${childMonthlyRatio.hosnumratio<0}">ratio_down</c:if>" >
						       		<span class="narrow_font">
							    		<c:if test="${childMonthlyRatio.hosnumratio>0}">+</c:if>
				               			<c:if test="${childMonthlyRatio.hosnumratio<0}">-</c:if>
				               		</span>
						       		<fmt:formatNumber type="percent" value="${childMonthlyRatio.hosnumratio<0?-childMonthlyRatio.hosnumratio:childMonthlyRatio.hosnumratio}" pattern="#0%"/>
						      </td>
						    </tr>
		               </c:forEach>
		            </table>
	            </div>
            </c:if>
            <c:if test="${'BM'!=currentUser.level}">
	            <div class="roundCorner" style="padding:4px;">
	                <div class="dailyReport_table_Title">${monthlyDataTitle}</div>
	                <table class="mobileReport_table">
	                    <tr class="mobileReport_table_header">
	                        <td width="8%">月份</td>
	                        <td width="6%">医院数</td>
	                        <td width="6%">上报数</td>
	                      	<td width="12%">儿科病房</td>
	                      	<td width="12%">儿科门急诊</td>
	                      	<td width="12%">家庭雾化</td>
	                      	<td width="12%">呼吸科病房</td>
	                      	<td width="12%">呼吸科门急诊</td>
	                        <td width="10%">其他科室</td>
	                        <td width="10%">合计</td>
	                    </tr>
	                    <c:forEach items="${monthly12Datas}" var="monthly12Data">
		                    <tr class="mobileReport_table_body">
		                        <td>${monthly12Data.dataMonth}</td>
		                        <td><fmt:formatNumber value="${monthly12Data.hosNum}" pattern="#,###"/></td>
		                        <td><fmt:formatNumber value="${monthly12Data.inNum}" pattern="#,###"/></td>
		                        <td><fmt:formatNumber value="${monthly12Data.pedRoomDrugStoreWh}" pattern="#,###"/></td>
		                        <td><fmt:formatNumber value="${monthly12Data.pedEmerWh}" pattern="#,###"/></td>
		                        <td><fmt:formatNumber value="${monthly12Data.homeWh}" pattern="#,###"/></td>
		                        <td><fmt:formatNumber value="${monthly12Data.resRoom}" pattern="#,###"/></td>
		                        <td><fmt:formatNumber value="${monthly12Data.resClinic}" pattern="#,###"/></td>
		                        <td><fmt:formatNumber value="${monthly12Data.othernum}" pattern="#,###"/></td>
		                        <td><fmt:formatNumber value="${monthly12Data.totalnum}" pattern="#,###"/></td>
		                    </tr>
	                    </c:forEach>
	                </table>
	            </div>
            </c:if>
            <c:if test="${'BM'== currentUser.level}">
	            <div class="roundCorner" style="padding:4px;">
	            	<div class="dailyReport_table_Title">查看下级袋数采集情况</div>
            		<form id="bmMonthlyForm" action="showLowerMonthlyData" method="POST" data-ajax="false" class="validate">
	            	<div data-role="fieldcontain">
		                    <label for="rsdSelect" class="select">RSD/全国</label>
		                    <select name="rsdSelect" id="rsdSelect">
		                        <option value="">--请选择--</option>
			                    <c:forEach var="data" items="${rsdUserlist}">
				          			<option value="${data.regionCenter}">${data.regionCenter}</option>  
				          		</c:forEach>
		                        <option value="0">全国</option>
			                </select>
	                </div>
	            	<div data-role="fieldcontain">
		                    <label for="rsmSelect" class="select">RSM</label>
		                    <select name="rsmSelect" id="rsmSelect">
		                        <option value="">--请选择--</option>
		                        <c:forEach var="data" items="${rsmUserlist}">
				          			<option value="${data.region}" parentid="${data.regionCenter}">${data.region}</option>
				          		</c:forEach> 
			                </select>
	                </div>
            		</form>
	                <div style="text-align: center;">
			            <img alt="" src="<%=basePath%>images/button_submit.png" style="cursor: pointer;" onclick="submitBMForm()"/>
		            </div>
	            </div>
            </c:if>
            <c:if test="${'RSD' == currentUser.level || 'RSM' == currentUser.level}">
	            <div class="roundCorner" style="padding:4px;">
	            	<div class="dailyReport_table_Title">查看下级袋数采集情况</div>
            		<form id="lowerMonthlyForm" action="showLowerMonthlyData" method="POST" data-ajax="false" class="validate">
	            		<div data-role="fieldcontain">
		                    <label for="lowUser" class="select">下级列表</label>
		                    <select name="lowUser" id="lowUser">
		                        <option value="">--请选择--</option>
			                    <c:forEach var="lowerUser" items="${lowerUsers}">
			                        <option value="${lowerUser.userCode}">
			                        	<c:if test="${'BM'== currentUser.level}">
				                        	${lowerUser.regionCenter}
			                        	</c:if>
			                        	<c:if test="${'RSD' == currentUser.level}">
				                        	${lowerUser.region}
			                        	</c:if>
			                        	<c:if test="${'RSM' == currentUser.level}">
				                        	${lowerUser.name}
			                        	</c:if>
			                        </option>
			                    </c:forEach>
			                </select>
	                	</div>
            		</form>
	                <div style="text-align: center;">
			            <img alt="" src="<%=basePath%>images/button_submit.png" style="cursor: pointer;" onclick="submitForm()"/>
		            </div>
	            </div>
            </c:if>
        </div>
        <jsp:include page="page_footer.jsp">
            <jsp:param value="<%=basePath%>" name="basePath"/>
            <jsp:param value="dataQuery" name="backURL"/>
        </jsp:include>
    </div>
</body>  
</html>  
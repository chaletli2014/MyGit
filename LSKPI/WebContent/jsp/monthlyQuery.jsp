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
				      <td width="10%">姓名</td>
                      <td width="18%">儿科门急诊袋数</td>
                      <td width="18%">儿科病房袋数</td>
                      <td width="18%">呼吸科袋数</td>
                      <td width="18%">其他科室袋数</td>
                      <td width="18%">总袋数</td>
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
					      <td class="report_data_number" ><fmt:formatNumber value="${monthlyRatio.pedemernum}" pattern="#,###"/></td>
					      <td class="report_data_number" ><fmt:formatNumber value="${monthlyRatio.pedroomnum}" pattern="#,###"/></td>
					      <td class="report_data_number" ><fmt:formatNumber value="${monthlyRatio.resnum}" pattern="#,###"/></td>
					      <td class="report_data_number" ><fmt:formatNumber value="${monthlyRatio.othernum}" pattern="#,###"/></td>
					      <td class="report_data_number" ><fmt:formatNumber value="${monthlyRatio.totalnum}" pattern="#,###"/></td>
					   </tr>
					   <tr class="mobileReport_table_body <c:if test="${status.count%2==0}">mobileReport_tr_even</c:if>">
					      <td class="report_data_number <c:if test="${monthlyRatio.pedemernumratio>0}">ratio_up</c:if><c:if test="${monthlyRatio.pedemernumratio<0}">ratio_down</c:if>" >
					       		<span class="narrow_font">
						    		<c:if test="${monthlyRatio.pedemernumratio>0}">+</c:if>
			               			<c:if test="${monthlyRatio.pedemernumratio<0}">-</c:if>
			               		</span>
					       		<fmt:formatNumber type="percent" value="${monthlyRatio.pedemernumratio<0?-monthlyRatio.pedemernumratio:monthlyRatio.pedemernumratio}" pattern="#0%"/>
					       </td>
					      <td class="report_data_number <c:if test="${monthlyRatio.pedroomnumratio>0}">ratio_up</c:if><c:if test="${monthlyRatio.pedroomnumratio<0}">ratio_down</c:if>" >
					       		<span class="narrow_font">
						    		<c:if test="${monthlyRatio.pedroomnumratio>0}">+</c:if>
			               			<c:if test="${monthlyRatio.pedroomnumratio<0}">-</c:if>
			               		</span>
					       		<fmt:formatNumber type="percent" value="${monthlyRatio.pedroomnumratio<0?-monthlyRatio.pedroomnumratio:monthlyRatio.pedroomnumratio}" pattern="#0%"/>
					       </td>
					      <td class="report_data_number <c:if test="${monthlyRatio.resnumratio>0}">ratio_up</c:if><c:if test="${monthlyRatio.resnumratio<0}">ratio_down</c:if>" >
					       		<span class="narrow_font">
						    		<c:if test="${monthlyRatio.resnumratio>0}">+</c:if>
			               			<c:if test="${monthlyRatio.resnumratio<0}">-</c:if>
			               		</span>
					       		<fmt:formatNumber type="percent" value="${monthlyRatio.resnumratio<0?-monthlyRatio.resnumratio:monthlyRatio.resnumratio}" pattern="#0%"/>
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
                     <td class="report_data_number" ><fmt:formatNumber value="${superiorMonthlyRatio.pedemernum}" pattern="#,###"/></td>
                     <td class="report_data_number" ><fmt:formatNumber value="${superiorMonthlyRatio.pedroomnum}" pattern="#,###"/></td>
                     <td class="report_data_number" ><fmt:formatNumber value="${superiorMonthlyRatio.resnum}" pattern="#,###"/></td>
                     <td class="report_data_number" ><fmt:formatNumber value="${superiorMonthlyRatio.othernum}" pattern="#,###"/></td>
                     <td class="report_data_number" ><fmt:formatNumber value="${superiorMonthlyRatio.totalnum}" pattern="#,###"/></td>
                 </tr>
                 <tr class="mobileReport_table_body <c:if test="${fn:length(monthlyRatioList)%2 != 0}">mobileReport_tr_even</c:if>">
                     <td class="report_data_number <c:if test="${superiorMonthlyRatio.pedemernumratio>0}">ratio_up</c:if><c:if test="${superiorMonthlyRatio.pedemernumratio<0}">ratio_down</c:if>" >
                      		<span class="narrow_font">
					    		<c:if test="${superiorMonthlyRatio.pedemernumratio>0}">+</c:if>
		               			<c:if test="${superiorMonthlyRatio.pedemernumratio<0}">-</c:if>
		               		</span>
                      		<fmt:formatNumber type="percent" value="${superiorMonthlyRatio.pedemernumratio<0?-superiorMonthlyRatio.pedemernumratio:superiorMonthlyRatio.pedemernumratio}" pattern="#0%"/>
                      </td>
                     <td class="report_data_number <c:if test="${superiorMonthlyRatio.pedroomnumratio>0}">ratio_up</c:if><c:if test="${superiorMonthlyRatio.pedroomnumratio<0}">ratio_down</c:if>" >
                      		<span class="narrow_font">
					    		<c:if test="${superiorMonthlyRatio.pedroomnumratio>0}">+</c:if>
		               			<c:if test="${superiorMonthlyRatio.pedroomnumratio<0}">-</c:if>
		               		</span>
                      		<fmt:formatNumber type="percent" value="${superiorMonthlyRatio.pedroomnumratio<0?-superiorMonthlyRatio.pedroomnumratio:superiorMonthlyRatio.pedroomnumratio}" pattern="#0%"/>
                      </td>
                     <td class="report_data_number <c:if test="${superiorMonthlyRatio.resnumratio>0}">ratio_up</c:if><c:if test="${superiorMonthlyRatio.resnumratio<0}">ratio_down</c:if>" >
                      		<span class="narrow_font">
					    		<c:if test="${superiorMonthlyRatio.resnumratio>0}">+</c:if>
		               			<c:if test="${superiorMonthlyRatio.resnumratio<0}">-</c:if>
		               		</span>
                      		<fmt:formatNumber type="percent" value="${superiorMonthlyRatio.resnumratio<0?-superiorMonthlyRatio.resnumratio:superiorMonthlyRatio.resnumratio}" pattern="#0%"/>
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
                      <td width="15%">儿科门急诊占比</td>
                      <td width="15%">儿科病房占比</td>
                      <td width="15%">呼吸科占比</td>
                      <td width="15%">其他科室占比</td>
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
					      <td class="report_data_number" ><fmt:formatNumber type="percent" value="${monthlyRatio.pedemernumrate}" pattern="#0%"/></td>
					      <td class="report_data_number" ><fmt:formatNumber type="percent" value="${monthlyRatio.pedroomnumrate}" pattern="#0%"/></td>
					      <td class="report_data_number" ><fmt:formatNumber type="percent" value="${monthlyRatio.resnumrate}" pattern="#0%"/></td>
					      <td class="report_data_number" ><fmt:formatNumber type="percent" value="${monthlyRatio.othernumrate}" pattern="#0%"/></td>
					      <td class="report_data_number" ><fmt:formatNumber type="percent" value="${monthlyRatio.inrate}" pattern="#0%"/></td>
					      <td class="report_data_number"><fmt:formatNumber value="${monthlyRatio.innum}" pattern="#,###"/></td>
	                      <td class="report_data_number"><fmt:formatNumber value="${monthlyRatio.hosnum}" pattern="#,###"/></td>
					  </tr>
					  <tr class="mobileReport_table_body <c:if test="${status.count%2==0}">mobileReport_tr_even</c:if>">
					      <td class="report_data_number <c:if test="${monthlyRatio.pedemernumrateratio>0}">ratio_up</c:if><c:if test="${monthlyRatio.pedemernumrateratio<0}">ratio_down</c:if>" >
					       		<span class="narrow_font">
						    		<c:if test="${monthlyRatio.pedemernumrateratio>0}">+</c:if>
			               			<c:if test="${monthlyRatio.pedemernumrateratio<0}">-</c:if>
			               		</span>
					       		<fmt:formatNumber type="percent" value="${monthlyRatio.pedemernumrateratio<0?-monthlyRatio.pedemernumrateratio:monthlyRatio.pedemernumrateratio}" pattern="#0%"/>
					       </td>
					      <td class="report_data_number <c:if test="${monthlyRatio.pedroomnumrateratio>0}">ratio_up</c:if><c:if test="${monthlyRatio.pedroomnumrateratio<0}">ratio_down</c:if>" >
					       		<span class="narrow_font">
						    		<c:if test="${monthlyRatio.pedroomnumrateratio>0}">+</c:if>
			               			<c:if test="${monthlyRatio.pedroomnumrateratio<0}">-</c:if>
			               		</span>
					       		<fmt:formatNumber type="percent" value="${monthlyRatio.pedroomnumrateratio<0?-monthlyRatio.pedroomnumrateratio:monthlyRatio.pedroomnumrateratio}" pattern="#0%"/>
					       </td>
					      <td class="report_data_number <c:if test="${monthlyRatio.resnumrateratio>0}">ratio_up</c:if><c:if test="${monthlyRatio.resnumrateratio<0}">ratio_down</c:if>" >
					       		<span class="narrow_font">
						    		<c:if test="${monthlyRatio.resnumrateratio>0}">+</c:if>
			               			<c:if test="${monthlyRatio.resnumrateratio<0}">-</c:if>
			               		</span>
					       		<fmt:formatNumber type="percent" value="${monthlyRatio.resnumrateratio<0?-monthlyRatio.resnumrateratio:monthlyRatio.resnumrateratio}" pattern="#0%"/>
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
                     <td class="report_data_number" ><fmt:formatNumber type="percent" value="${superiorMonthlyRatio.pedemernumrate}" pattern="#0%"/></td>
                     <td class="report_data_number" ><fmt:formatNumber type="percent" value="${superiorMonthlyRatio.pedroomnumrate}" pattern="#0%"/></td>
                     <td class="report_data_number" ><fmt:formatNumber type="percent" value="${superiorMonthlyRatio.resnumrate}" pattern="#0%"/></td>
                     <td class="report_data_number" ><fmt:formatNumber type="percent" value="${superiorMonthlyRatio.othernumrate}" pattern="#0%"/></td>
                     <td class="report_data_number" ><fmt:formatNumber type="percent" value="${superiorMonthlyRatio.inrate}" pattern="#0%"/></td>
				     <td class="report_data_number"><fmt:formatNumber value="${superiorMonthlyRatio.innum}" pattern="#,###"/></td>
	                 <td class="report_data_number"><fmt:formatNumber value="${superiorMonthlyRatio.hosnum}" pattern="#,###"/></td>
				 </tr>
				 <tr class="mobileReport_table_body <c:if test="${fn:length(monthlyRatioList)%2 != 0}">mobileReport_tr_even</c:if>">
                     <td class="report_data_number <c:if test="${superiorMonthlyRatio.pedemernumrateratio>0}">ratio_up</c:if><c:if test="${superiorMonthlyRatio.pedemernumrateratio<0}">ratio_down</c:if>" >
                      		<span class="narrow_font">
					    		<c:if test="${superiorMonthlyRatio.pedemernumrateratio>0}">+</c:if>
		               			<c:if test="${superiorMonthlyRatio.pedemernumrateratio<0}">-</c:if>
		               		</span>
                      		<fmt:formatNumber type="percent" value="${superiorMonthlyRatio.pedemernumrateratio<0?-superiorMonthlyRatio.pedemernumrateratio:superiorMonthlyRatio.pedemernumrateratio}" pattern="#0%"/>
                      </td>
                     <td class="report_data_number <c:if test="${superiorMonthlyRatio.pedroomnumrateratio>0}">ratio_up</c:if><c:if test="${superiorMonthlyRatio.pedroomnumrateratio<0}">ratio_down</c:if>" >
                      		<span class="narrow_font">
					    		<c:if test="${superiorMonthlyRatio.pedroomnumrateratio>0}">+</c:if>
		               			<c:if test="${superiorMonthlyRatio.pedroomnumrateratio<0}">-</c:if>
		               		</span>
                      		<fmt:formatNumber type="percent" value="${superiorMonthlyRatio.pedroomnumrateratio<0?-superiorMonthlyRatio.pedroomnumrateratio:superiorMonthlyRatio.pedroomnumrateratio}" pattern="#0%"/>
                      </td>
                     <td class="report_data_number <c:if test="${superiorMonthlyRatio.resnumrateratio>0}">ratio_up</c:if><c:if test="${superiorMonthlyRatio.resnumrateratio<0}">ratio_down</c:if>" >
                      		<span class="narrow_font">
					    		<c:if test="${superiorMonthlyRatio.resnumrateratio>0}">+</c:if>
		               			<c:if test="${superiorMonthlyRatio.resnumrateratio<0}">-</c:if>
		               		</span>
                      		<fmt:formatNumber type="percent" value="${superiorMonthlyRatio.resnumrateratio<0?-superiorMonthlyRatio.resnumrateratio:superiorMonthlyRatio.resnumrateratio}" pattern="#0%"/>
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
					      <td width="10%">姓名</td>
	                      <td width="18%">儿科门急诊袋数</td>
	                      <td width="18%">儿科病房袋数</td>
	                      <td width="18%">呼吸科袋数</td>
	                      <td width="18%">其他科室袋数</td>
	                      <td width="18%">总袋数</td>
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
						      <td class="report_data_number" ><fmt:formatNumber value="${childMonthlyRatio.pedemernum}" pattern="#,###"/></td>
						      <td class="report_data_number" ><fmt:formatNumber value="${childMonthlyRatio.pedroomnum}" pattern="#,###"/></td>
						      <td class="report_data_number" ><fmt:formatNumber value="${childMonthlyRatio.resnum}" pattern="#,###"/></td>
						      <td class="report_data_number" ><fmt:formatNumber value="${childMonthlyRatio.othernum}" pattern="#,###"/></td>
						      <td class="report_data_number" ><fmt:formatNumber value="${childMonthlyRatio.totalnum}" pattern="#,###"/></td>
						    </tr>
						    <tr class="mobileReport_table_body <c:if test="${status.count%2==0}">mobileReport_tr_even</c:if>">
						      <td class="report_data_number <c:if test="${childMonthlyRatio.pedemernumratio>0}">ratio_up</c:if><c:if test="${childMonthlyRatio.pedemernumratio<0}">ratio_down</c:if>" >
						       		<span class="narrow_font">
							    		<c:if test="${childMonthlyRatio.pedemernumratio>0}">+</c:if>
				               			<c:if test="${childMonthlyRatio.pedemernumratio<0}">-</c:if>
				               		</span>
						       		<fmt:formatNumber type="percent" value="${childMonthlyRatio.pedemernumratio<0?-childMonthlyRatio.pedemernumratio:childMonthlyRatio.pedemernumratio}" pattern="#0%"/>
						       </td>
						      <td class="report_data_number <c:if test="${childMonthlyRatio.pedroomnumratio>0}">ratio_up</c:if><c:if test="${childMonthlyRatio.pedroomnumratio<0}">ratio_down</c:if>" >
						       		<span class="narrow_font">
							    		<c:if test="${childMonthlyRatio.pedroomnumratio>0}">+</c:if>
				               			<c:if test="${childMonthlyRatio.pedroomnumratio<0}">-</c:if>
				               		</span>
						       		<fmt:formatNumber type="percent" value="${childMonthlyRatio.pedroomnumratio<0?-childMonthlyRatio.pedroomnumratio:childMonthlyRatio.pedroomnumratio}" pattern="#0%"/>
						       </td>
						      <td class="report_data_number <c:if test="${childMonthlyRatio.resnumratio>0}">ratio_up</c:if><c:if test="${childMonthlyRatio.resnumratio<0}">ratio_down</c:if>" >
						       		<span class="narrow_font">
							    		<c:if test="${childMonthlyRatio.resnumratio>0}">+</c:if>
				               			<c:if test="${childMonthlyRatio.resnumratio<0}">-</c:if>
				               		</span>
						       		<fmt:formatNumber type="percent" value="${childMonthlyRatio.resnumratio<0?-childMonthlyRatio.resnumratio:childMonthlyRatio.resnumratio}" pattern="#0%"/>
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
	                      <td width="15%">儿科门急诊占比</td>
	                      <td width="15%">儿科病房占比</td>
	                      <td width="15%">呼吸科占比</td>
	                      <td width="15%">其他科室占比</td>
	                      <td width="15%">总医院数</td>
	                      <td width="15%">上报医院数</td>
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
						      <td class="report_data_number" ><fmt:formatNumber type="percent" value="${childMonthlyRatio.pedemernumrate}" pattern="#0%"/></td>
						      <td class="report_data_number" ><fmt:formatNumber type="percent" value="${childMonthlyRatio.pedroomnumrate}" pattern="#0%"/></td>
						      <td class="report_data_number" ><fmt:formatNumber type="percent" value="${childMonthlyRatio.resnumrate}" pattern="#0%"/></td>
						      <td class="report_data_number" ><fmt:formatNumber type="percent" value="${childMonthlyRatio.othernumrate}" pattern="#0%"/></td>
						      <td class="report_data_number"><fmt:formatNumber value="${childMonthlyRatio.hosnum}" pattern="#,###"/></td>
				     		  <td class="report_data_number"><fmt:formatNumber value="${childMonthlyRatio.innum}" pattern="#,###"/></td>
						    </tr>
						    <tr class="mobileReport_table_body <c:if test="${status.count%2==0}">mobileReport_tr_even</c:if>">
						      <td class="report_data_number <c:if test="${childMonthlyRatio.pedemernumrateratio>0}">ratio_up</c:if><c:if test="${childMonthlyRatio.pedemernumrateratio<0}">ratio_down</c:if>" >
						       		<span class="narrow_font">
							    		<c:if test="${childMonthlyRatio.pedemernumrateratio>0}">+</c:if>
				               			<c:if test="${childMonthlyRatio.pedemernumrateratio<0}">-</c:if>
				               		</span>
						       		<fmt:formatNumber type="percent" value="${childMonthlyRatio.pedemernumrateratio<0?-childMonthlyRatio.pedemernumrateratio:childMonthlyRatio.pedemernumrateratio}" pattern="#0%"/>
						       </td>
						      <td class="report_data_number <c:if test="${childMonthlyRatio.pedroomnumrateratio>0}">ratio_up</c:if><c:if test="${childMonthlyRatio.pedroomnumrateratio<0}">ratio_down</c:if>" >
						       		<span class="narrow_font">
							    		<c:if test="${childMonthlyRatio.pedroomnumrateratio>0}">+</c:if>
				               			<c:if test="${childMonthlyRatio.pedroomnumrateratio<0}">-</c:if>
				               		</span>
						       		<fmt:formatNumber type="percent" value="${childMonthlyRatio.pedroomnumrateratio<0?-childMonthlyRatio.pedroomnumrateratio:childMonthlyRatio.pedroomnumrateratio}" pattern="#0%"/>
						       </td>
						      <td class="report_data_number <c:if test="${childMonthlyRatio.resnumrateratio>0}">ratio_up</c:if><c:if test="${childMonthlyRatio.resnumrateratio<0}">ratio_down</c:if>" >
						       		<span class="narrow_font">
							    		<c:if test="${childMonthlyRatio.resnumrateratio>0}">+</c:if>
				               			<c:if test="${childMonthlyRatio.resnumrateratio<0}">-</c:if>
				               		</span>
						       		<fmt:formatNumber type="percent" value="${childMonthlyRatio.resnumrateratio<0?-childMonthlyRatio.resnumrateratio:childMonthlyRatio.resnumrateratio}" pattern="#0%"/>
						       </td>
						      <td class="report_data_number <c:if test="${childMonthlyRatio.othernumrateratio>0}">ratio_up</c:if><c:if test="${childMonthlyRatio.othernumrateratio<0}">ratio_down</c:if>" >
						       		<span class="narrow_font">
							    		<c:if test="${childMonthlyRatio.othernumrateratio>0}">+</c:if>
				               			<c:if test="${childMonthlyRatio.othernumrateratio<0}">-</c:if>
				               		</span>
						       		<fmt:formatNumber type="percent" value="${childMonthlyRatio.othernumrateratio<0?-childMonthlyRatio.othernumrateratio:childMonthlyRatio.othernumrateratio}" pattern="#0%"/>
						       </td>
						       <td class="report_data_number <c:if test="${childMonthlyRatio.hosnumratio>0}">ratio_up</c:if><c:if test="${childMonthlyRatio.hosnumratio<0}">ratio_down</c:if>" >
						       		<span class="narrow_font">
							    		<c:if test="${childMonthlyRatio.hosnumratio>0}">+</c:if>
				               			<c:if test="${childMonthlyRatio.hosnumratio<0}">-</c:if>
				               		</span>
						       		<fmt:formatNumber type="percent" value="${childMonthlyRatio.hosnumratio<0?-childMonthlyRatio.hosnumratio:childMonthlyRatio.hosnumratio}" pattern="#0%"/>
						      </td>
						      <td class="report_data_number <c:if test="${childMonthlyRatio.innumratio>0}">ratio_up</c:if><c:if test="${childMonthlyRatio.innumratio<0}">ratio_down</c:if>" >
						       		<span class="narrow_font">
							    		<c:if test="${childMonthlyRatio.innumratio>0}">+</c:if>
				               			<c:if test="${childMonthlyRatio.innumratio<0}">-</c:if>
				               		</span>
						       		<fmt:formatNumber type="percent" value="${childMonthlyRatio.innumratio<0?-childMonthlyRatio.innumratio:childMonthlyRatio.innumratio}" pattern="#0%"/>
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
	                        <td width="16%">月份</td>
	                        <td width="15%">医院数</td>
	                        <td width="15%">上报数</td>
	                        <td width="14%">儿科门急诊</td>
	                        <td width="14%">儿科病房</td>
	                        <td width="14%">呼吸科</td>
	                        <td width="14%">其他科室</td>
	                        <td width="14%">合计</td>
	                    </tr>
	                    <c:forEach items="${monthly12Datas}" var="monthly12Data">
		                    <tr class="mobileReport_table_body">
		                        <td>${monthly12Data.dataMonth}</td>
		                        <td><fmt:formatNumber value="${monthly12Data.hosNum}" pattern="#,###"/></td>
		                        <td><fmt:formatNumber value="${monthly12Data.inNum}" pattern="#,###"/></td>
		                        <td><fmt:formatNumber value="${monthly12Data.pedemernum}" pattern="#,###"/></td>
		                        <td><fmt:formatNumber value="${monthly12Data.pedroomnum}" pattern="#,###"/></td>
		                        <td><fmt:formatNumber value="${monthly12Data.resnum}" pattern="#,###"/></td>
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
	            	<div data-role="fieldcontain">
	            		<form id="lowerMonthlyForm" action="showLowerMonthlyData" method="POST" data-ajax="false" class="validate">
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
	            		</form>
	                </div>
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
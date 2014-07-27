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
        $('#hospitalSalesQueryForm').submit();
    }
}
function checkForm(){
    if( !checkIsNotNull( $("#hospitalKeyword") ) ){
        showCustomrizedMessage("请填入检索关键字");
        return false;
    }
    return true;
}
function showHospitalRatio(hosCode,orderParam,orderType,order,department){
    $.mobile.showPageLoadingMsg('b','数据检索中',false);
    window.location.href = "<%=basePath%>hospitalRatio?hospitalCode="+hosCode+"&fromSalesQuery=Y&orderParam="+orderParam+"&orderType="+orderType+"&order="+order+"&department="+department;
}
function downloadHosSalesData(orderParam,orderType,order,department){
	$.mobile.showPageLoadingMsg('b','数据检索中',false);
    window.location.href = "<%=basePath%>downloadHosSalesData?orderParam="+orderParam+"&orderType="+orderType+"&order="+order+"&department="+department;
}
</script>
<body onload="checkMessage('${message}')">
    <div style="position:absolute; left:-9999px;"><a href="#" id="setfoc"></a></div>
    <div data-role="page" id="home">
        <jsp:include page="page_header.jsp" flush="true">
        	<jsp:param name="title" value="医院销量检索"/>
        	<jsp:param name="basePath" value="<%=basePath%>"/>
        </jsp:include>
        <div data-role="content" data-theme="a">
        	<div class="roundCorner ls_hos_query">
            <form id="hospitalSalesQueryForm" action="doHospitalSalesQuery" method="POST" data-ajax="false">
	            <div data-role="fieldcontain">
	                <label for="orderParam" class="select">查询参数：</label>
	                <select name="orderParam" id="orderParam">
	                   <option value="pnum" <c:if test="${orderParam=='pnum'}">selected</c:if>>总人数</option>
	                   <option value="lsnum" <c:if test="${orderParam=='lsnum'}">selected</c:if>>雾化令舒人数</option>
	                   <option value="whRate" <c:if test="${orderParam=='whRate'}">selected</c:if>>雾化率</option>
	                   <option value="aveDose" <c:if test="${orderParam=='aveDose'}">selected</c:if>>平均剂量</option>
	                </select>
	            </div>
	            <div data-role="fieldcontain">
	               <label for="orderType" class="select">排序类型：</label>
	                <select name="orderType" id="orderType">
	                   <option value="val" <c:if test="${orderType=='val'}">selected</c:if>>实际值</option>
	                   <option value="ratio" <c:if test="${orderType=='ratio'}">selected</c:if>>增长率</option>
	                </select>
	            </div>
	            <div data-role="fieldcontain">
	               <label for="order" class="select">排列顺序：</label>
	                <select name="order" id="order">
	                   <option value="desc" <c:if test="${order=='desc'}">selected</c:if>>降序</option>
	                   <option value="asc" <c:if test="${order=='asc'}">selected</c:if>>升序</option>
	                </select>
	            </div>
	            <div data-role="fieldcontain">
	               <label for="department" class="select">科室：</label>
	                <select name="department" id="department">
	                   	<option value="1" <c:if test="${department=='1'}">selected</c:if>>呼吸科</option>
                    	<option value="2" <c:if test="${department=='2'}">selected</c:if>>儿科</option>
	                    <option value="3" <c:if test="${department=='3'}">selected</c:if>>胸外科</option>
	                </select>
	            </div>
	            <div data-role="fieldcontain" align="center">
	                <img alt="" src="<%=basePath%>images/button_search.png" style="cursor: pointer;" onclick="submitForm()"/>
	            </div>
            </form>
            </div>
            <c:if test="${hosSalesData!= null && fn:length(hosSalesData) > 0}">
	        	<div class="roundCorner ls_hos_query">
	        	   <div class="dailyReport_table_Title">
	        	      <span>医院销量详细数据</span>
	        	   </div>
	        	   <div class="download_href_div">
        	          <a href="javascript:void(0)" onclick="downloadHosSalesData('${orderParam}','${orderType}','${order}','${department}')" title="点击下载本表格数据">下载数据</a>
                      <c:if test="${hosSalesDataFile != null}">
                        <a href="<%=basePath%>${hosSalesDataFile}" target="_blank">${fn:substringAfter(hosSalesDataFile,'/')}</a>
                      </c:if>
	        	   </div>
	        	   <table class="mobileReport_table">
	        	      <tr class="mobileReport_table_header">
        	              <td width="20%">医院名称</td>
        	              <td width="11%" class="<c:if test="${orderParam=='pnum'&&orderType=='val'}">hos_sales_query_selected_column</c:if>">总人数</td>
        	              <td width="11%" class="<c:if test="${orderParam=='lsnum'&&orderType=='val'}">hos_sales_query_selected_column</c:if>">雾化令舒人数</td>
        	              <td width="8%" class="<c:if test="${orderParam=='whRate'&&orderType=='val'}">hos_sales_query_selected_column</c:if>">雾化率</td>
        	              <td width="8%" class="<c:if test="${orderParam=='aveDose'&&orderType=='val'}">hos_sales_query_selected_column</c:if>">平均剂量</td>
        	              <td width="8%" class="<c:if test="${orderParam=='pnum'&&orderType=='ratio'}">hos_sales_query_selected_column</c:if>">总人数增长率</td>
        	              <td width="8%" class="<c:if test="${orderParam=='lsnum'&&orderType=='ratio'}">hos_sales_query_selected_column</c:if>">雾化令舒人数增长率</td>
        	              <td width="8%" class="<c:if test="${orderParam=='whRate'&&orderType=='ratio'}">hos_sales_query_selected_column</c:if>">雾化率增长率</td>
        	              <td width="8%" class="<c:if test="${orderParam=='aveDose'&&orderType=='ratio'}">hos_sales_query_selected_column</c:if>">平均剂量增长率</td>
        	              <td width="10%">医院详细</td>
	        	      </tr>
	        		  <c:forEach items="${hosSalesData}" var="data" varStatus="status">
		              <tr class="mobileReport_table_body <c:if test="${status.count%2==0}">mobileReport_tr_even</c:if>">
		                  <td align="left">${data.hosName}</td>
		                  <td class="<c:if test="${orderParam=='pnum'&&orderType=='val'}">hos_sales_query_selected_column</c:if>">
		                      <fmt:formatNumber value="${data.pnum}" pattern="#,###"/>
		                  </td>
		                  <td class="<c:if test="${orderParam=='lsnum'&&orderType=='val'}">hos_sales_query_selected_column</c:if>">
		                      <fmt:formatNumber value="${data.lsnum}" pattern="#,###"/>
		                  </td>
		                  <td class="<c:if test="${orderParam=='whRate'&&orderType=='val'}">hos_sales_query_selected_column</c:if>">
		                      <fmt:formatNumber type="percent" value="${data.whRate}" pattern="#0%"/>
		                  </td>
		                  <td class="<c:if test="${orderParam=='aveDose'&&orderType=='val'}">hos_sales_query_selected_column</c:if>">
		                      <fmt:formatNumber type="percent" value="${data.aveDose}" pattern="#0.00"/>
		                  </td>
		                  <td class="<c:if test="${orderParam=='pnum'&&orderType=='ratio'}"> hos_sales_query_selected_column </c:if><c:if test="${data.pnumRatio>0}">ratio_up</c:if><c:if test="${data.pnumRatio<0}">ratio_down</c:if>">
		                      <span class="narrow_font">
                                    <c:if test="${data.pnumRatio>0}">+</c:if>
                              </span>
		                      <fmt:formatNumber type="percent" value="${data.pnumRatio}" pattern="#0%"/>
		                  </td>
		                  <td class="<c:if test="${orderParam=='lsnum'&&orderType=='ratio'}"> hos_sales_query_selected_column </c:if><c:if test="${data.lsnumRatio>0}">ratio_up</c:if><c:if test="${data.lsnumRatio<0}">ratio_down</c:if>">
		                      <span class="narrow_font">
                                    <c:if test="${data.lsnumRatio>0}">+</c:if>
                              </span>
		                      <fmt:formatNumber type="percent" value="${data.lsnumRatio}" pattern="#0%"/>
		                  </td>
		                  <td class="<c:if test="${orderParam=='whRate'&&orderType=='ratio'}"> hos_sales_query_selected_column </c:if><c:if test="${data.whRateRatio>0}">ratio_up</c:if><c:if test="${data.whRateRatio<0}">ratio_down</c:if>">
		                      <span class="narrow_font">
                                    <c:if test="${data.whRateRatio>0}">+</c:if>
                              </span>
		                      <fmt:formatNumber type="percent" value="${data.whRateRatio}" pattern="#0%"/>
		                  </td>
		                  <td class="<c:if test="${orderParam=='aveDose'&&orderType=='ratio'}"> hos_sales_query_selected_column </c:if><c:if test="${data.aveDoseRatio>0}">ratio_up</c:if><c:if test="${data.aveDoseRatio<0}">ratio_down</c:if>">
		                      <span class="narrow_font">
                                    <c:if test="${data.aveDoseRatio>0}">+</c:if>
                              </span>
		                      <fmt:formatNumber type="percent" value="${data.aveDoseRatio}" pattern="#0%"/>
		                  </td>
		                  <td>
		                      <img id="${data.hosCode}" src="<%=basePath%>images/button_next.png" style="cursor: pointer;" onclick="showHospitalRatio(this.id,'${orderParam}','${orderType}','${order}','${department}')"/>
		                  </td>
		              </tr>
	        		  </c:forEach>
        	      </table>
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
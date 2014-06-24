<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %> 
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE HTML>
<html lang="en-US">
<%@include file="header.jsp" %> 
<script type="text/javascript">
function loadData(hospitalCode){
	window.location.href="<%=basePath%>collectmonthlydata?hospitalCode="+encodeURI(hospitalCode);
}
function submitForm(){
    if(checkForm()){
    	$.mobile.showPageLoadingMsg('b','数据提交中',false);
        if( $('.submit_btn') ){
			$('.submit_btn').removeAttr("onclick");
		}
        $('#monthlyDataForm').submit();
    }
}
function checkForm(){
	if( !isDouble($("#pedemernum"),$("#pedroomnum"),$("#resnum"),$("#othernum"))  ){
        return false;
	}
    return true;
}
</script>
<body onload="checkMessage('${message}')">
    <div style="position:absolute; left:-9999px;"><a href="#" id="setfoc"></a></div>
    <div data-role="page" id="home">
        <jsp:include page="page_header.jsp" flush="true">
        	<jsp:param name="title" value="每月数据采集"/>
        	<jsp:param name="basePath" value="<%=basePath%>"/>
        </jsp:include>
        <div data-role="content"  data-theme="a">
        	<div class="roundCorner">
            <form id="monthlyDataForm" action="doCollectMonth" method="POST" data-ajax="false">
            	<input type="hidden" name="dataId" value="${existedData.id}"/>
	        	<input type="hidden" name="hospitalCode" value="${hospitalCode}"/>
	        	<div class="report_process_bg_description">医院名称前的 * 表示该医院在统计范围内</div>
                <div data-role="fieldcontain">
                    <label for="hospital" class="select">医院名称</label>
                    <select name="hospital" id="hospital" onchange="loadData(this.value)">
                        <option value="">--请选择--</option>
	                    <c:forEach var="hospital" items="${hospitals}">
	                        <option value="${hospital.code}" <c:if test="${hospital.code == hospitalCode}">selected</c:if>>${hospital.name}</option>
	                    </c:forEach>
	                </select>
                </div>
                <div>各科室销售袋数</div>
               	<div data-role="fieldcontain" class="formCollection">
                    <label for="pedemernum" id="pedemernum_label">儿科门急诊</label>
                    <input type="number" name="pedemernum" id="pedemernum" value="${existedData.pedemernum}"/>
                </div>
              	<div data-role="fieldcontain">
                   <label for="pedroomnum" id="pedroomnum_label">儿科病房</label>
                   <input type="number" name="pedroomnum" id="pedroomnum" value="${existedData.pedroomnum}"/>
                </div>
                <div data-role="fieldcontain" spellcheck="true">
                    <label for="resnum" id="resnum_label">呼吸科</label>
                    <input type="number" name="resnum" id="resnum" value="${existedData.resnum}"/>
                </div>
                <div data-role="fieldcontain" spellcheck="true">
                    <label for="othernum" id="othernum_label">其他科室</label>
                    <input type="number" name="othernum" id="othernum" value="${existedData.othernum}"/>
                </div>
                <div style="text-align: center;">
                	<a class="submit_btn" href="javascript:void(0)" onclick="submitForm()">
			            <img alt="" src="<%=basePath%>images/button_submit.png" style="cursor: pointer;" />
	            	</a>
	            </div>
            </form>
            </div>
        </div>
        <jsp:include page="page_footer.jsp">
            <jsp:param value="<%=basePath%>" name="basePath"/>
            <jsp:param value="collectData" name="backURL"/>
        </jsp:include>
    </div>
</body>  
</html>  
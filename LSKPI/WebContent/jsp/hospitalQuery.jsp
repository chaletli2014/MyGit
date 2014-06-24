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
        $('#hospitalSearchForm').submit();
    }
}
function checkForm(){
    if( !checkIsNotNull( $("#hospitalKeyword") ) ){
        showCustomrizedMessage("请填入检索关键字");
        return false;
    }
    return true;
}
function showHospitalRatio(hosCode,hospitalKeyword){
	$.mobile.showPageLoadingMsg('b','数据检索中',false);
	window.location.href = "<%=basePath%>hospitalRatio?hospitalCode="+hosCode+"&hospitalKeyword="+hospitalKeyword;
}
</script>
<body onload="checkMessage('${message}')">
    <div style="position:absolute; left:-9999px;"><a href="#" id="setfoc"></a></div>
    <div data-role="page" id="home">
        <jsp:include page="page_header.jsp" flush="true">
        	<jsp:param name="title" value="医院数据检索"/>
        	<jsp:param name="basePath" value="<%=basePath%>"/>
        </jsp:include>
        <div data-role="content"  data-theme="a">
        	<div class="roundCorner" >
            <form id="hospitalSearchForm" action="doHospitalSearch" method="POST" data-ajax="false">
                <div class="ui-grid-a">
	                <div class="ui-block-a">
	                	<div data-role="fieldcontain" >
		                    <input name="hospitalKeyword" id="hospitalKeyword" value="${hospitalKeyword}"/>
		                </div>
	                </div>
	                <div class="ui-block-b">
	                	<div data-role="fieldcontain">
				            <img alt="" src="<%=basePath%>images/button_search.png" style="cursor: pointer;" onclick="submitForm()"/>
		                </div>
	                </div>
	            </div>
            </form>
            </div>
            <c:if test="${searchedHospitals!= null && fn:length(searchedHospitals) > 0}">
	        	<div class="roundCorner" >
	        		<c:forEach items="${searchedHospitals}" var="searchedHospital">
		        		<div class="ui-grid-a">
			                <div class="ui-block-a">
			                	<div data-role="fieldcontain" >
				                    ${searchedHospital.name}
				                </div>
			                </div>
			                <div class="ui-block-b">
			                	<div data-role="fieldcontain">
						            <img id="${searchedHospital.code}" src="<%=basePath%>images/button_next.png" style="cursor: pointer;" onclick="showHospitalRatio(this.id,'${hospitalKeyword}')"/>
				                </div>
			                </div>
			            </div>
	        		</c:forEach>
	        	</div>
            </c:if>
            <c:if test="${hospitalKeyword!=null&&(searchedHospitals==null||fn:length(searchedHospitals)==0)}">
                <div class="roundCorner" >
                                                                    没有检索到符合关键字的医院信息
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
<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %> 
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE HTML>
<html lang="en-US">
<%@include file="header.jsp" %> 
<script type="text/javascript">
function loadData(hospitalName){
	$.mobile.showPageLoadingMsg('b','数据加载中',false);
	window.location.href="<%=basePath%>pediatricsHome?selectedHospital="+encodeURI(hospitalName);
}
function submitForm(){
    if(checkForm()){
        $.mobile.showPageLoadingMsg('b','数据提交中',false);
        if( $('.submit_btn') ){
			$('.submit_btn').removeAttr("onclick");
		}
        $('#pediatricsForm').submit();
    }
}
function checkForm(){
    if( !checkIsNotNull( $("#hospital") ) ){
        showCustomrizedMessage("医院不能为空");
        return false;
    }
    return true;
}
</script>
<body onload="checkMessage('${message}')">
    <div style="position:absolute; left:-9999px;"><a href="#" id="setfoc"></a></div>
    <div data-role="page" id="home">
        <jsp:include page="page_header.jsp" flush="true">
        	<jsp:param name="title" value="儿科每周雾化数据采集"/>
        	<jsp:param name="basePath" value="<%=basePath%>"/>
        </jsp:include>
        <div data-role="content"  data-theme="a">
        	<div class="roundCorner">
        	<div class="report_process_bg_description">标*为Core医院，标**为Emerging医院，所有医院每周只填报一次</div>
            <form id="pediatricsForm" action="collectPediatricsHome" method="POST" data-ajax="false">
            	<input type="hidden" name="dataId" value="${existedData.dataId}"/>
	        	<input type="hidden" name="selectedHospital" value="${selectedHospital}"/>
                <div data-role="fieldcontain">
                    <label for="hospital" class="select">医院名称</label>
                    <select name="hospital" id="hospital" onchange="loadData(this.value)">
                        <option value="">--请选择--</option>
	                    <c:forEach var="hospital" items="${hospitals}">
	                        <option value="${hospital.code}" <c:if test="${hospital.code == selectedHospital}">selected</c:if>>${hospital.name}</option>
	                    </c:forEach>
	                </select>
                </div>
                <div class="form_group_title">门急诊家庭雾化</div>
                <HR style="FILTER: alpha(opacity=100,finishopacity=0,style=1)" width="100%" color=#987cb9 SIZE=3>
                
                <div class="ui-grid-a formCollection">
	                <div class="ui-block-a">
	                	<div data-role="fieldcontain" >
		                    <label for="homeWhEmergingNum1" id="homeWhEmergingNum1_label">赠卖泵数量</label>
		                    <input type="number" name="homeWhEmergingNum1" id="homeWhEmergingNum1" value="${existedData.homeWhEmergingNum1==null?0:existedData.homeWhEmergingNum1}"/>
		                </div>
	                </div>
	                <div class="ui-block-b">
	                	<div data-role="fieldcontain" >
		                    <label for="homeWhEmergingNum2" id="homeWhEmergingNum2_label">带药人数</label>
		                    <input type="number" name="homeWhEmergingNum2" id="homeWhEmergingNum2" value="${existedData.homeWhEmergingNum2==null?0:existedData.homeWhEmergingNum2}"/>
		                </div>
	                </div>
	            </div>
	            <div class="ui-grid-a formCollection">
	                <div class="ui-block-a">
	                	<div data-role="fieldcontain" >
		                    <label for="homeWhEmergingNum3" id="homeWhEmergingNum3_label">平均带药天数</label>
		                    <input type="number" name="homeWhEmergingNum3" id="homeWhEmergingNum3" value="${existedData.homeWhEmergingNum3==null?0:existedData.homeWhEmergingNum3}"/>
		                </div>
	                </div>
	                <div class="ui-block-b">
	                	<div data-role="fieldcontain" >
		                    <label for="homeWhEmergingNum4" id="homeWhEmergingNum4_label">总带药支数</label>
		                    <input type="number" name="homeWhEmergingNum4" id="homeWhEmergingNum4" value="${existedData.homeWhEmergingNum4==null?0:existedData.homeWhEmergingNum4}"/>
		                </div>
	                </div>
	            </div>
	            <div class="form_group_title">住院家庭雾化</div>
                <HR style="FILTER: alpha(opacity=100,finishopacity=0,style=1)" width="100%" color=#987cb9 SIZE=3>
                <div class="ui-grid-a formCollection">
	                <div class="ui-block-a">
	                	<div data-role="fieldcontain" >
		                    <label for="homeWhRoomNum1" id="homewhNum1_label">赠卖泵数量</label>
		                    <input type="number" name="homeWhRoomNum1" id="homeWhRoomNum1" value="${existedData.homeWhRoomNum1==null?0:existedData.homeWhRoomNum1}"/>
		                </div>
	                </div>
	                <div class="ui-block-b">
	                	<div data-role="fieldcontain" >
		                    <label for="homeWhRoomNum2" id="homewhNum2_label">带药人数</label>
		                    <input type="number" name="homeWhRoomNum2" id="homeWhRoomNum2" value="${existedData.homeWhRoomNum2==null?0:existedData.homeWhRoomNum2}"/>
		                </div>
	                </div>
	            </div>
	            <div class="ui-grid-a formCollection">
	                <div class="ui-block-a">
	                	<div data-role="fieldcontain" >
		                    <label for="homeWhRoomNum3" id="homewhNum3_label">平均带药天数</label>
		                    <input type="number" name="homeWhRoomNum3" id="homeWhRoomNum3" value="${existedData.homeWhRoomNum3==null?0:existedData.homeWhRoomNum3}"/>
		                </div>
	                </div>
	                <div class="ui-block-b">
	                	<div data-role="fieldcontain" >
		                    <label for="homeWhRoomNum4" id="homewhNum4_label">总带药支数</label>
		                    <input type="number" name="homeWhRoomNum4" id="homeWhRoomNum4" value="${existedData.homeWhRoomNum4==null?0:existedData.homeWhRoomNum4}"/>
		                </div>
	                </div>
	            </div>	             
                <div style="text-align: center;">
		            <a class="submit_btn" href="javascript:void(0)" onclick="submitForm()">
			            <img alt="" src="<%=basePath%>images/button_submit.png" style="cursor: pointer;" />
	            	</a>
	            </div>
            </form>
            </div>
        </div>
        <%@include file="../popupConfirm.jsp" %>
        <jsp:include page="page_footer.jsp">
            <jsp:param value="<%=basePath%>" name="basePath"/>
            <jsp:param value="collectPediatricsData" name="backURL"/>
        </jsp:include>
    </div>
</body>  
</html>  
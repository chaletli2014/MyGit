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
	window.location.href="<%=basePath%>pediatrics?selectedHospital="+encodeURI(hospitalName);
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
	//$("#hqd"),$("#hbid"),$("#oqd"),$("#obid"),$("#tqd"),$("#tbid")
    if( !checkIsNotNull( $("#hospital") ) ){
        showCustomrizedMessage("医院不能为空");
        return false;
    }
    if( !checkIsNotNull( $("#pnum"),$("#whnum"),$("#lsnum") ) ){
        showCustomrizedMessage("数据不能为空或者字母");
        return false;
    }
	
	if( !isInteger($("#pnum"),$("#whnum"),$("#lsnum"))  ){
        return false;
	}
	
	if( !isLsNumAndPNumValid() ){
        return false;
	}
    /*
    if( !obj1ltobj2("pnum","whnum") ){
        return false;
    }
    
    if( !obj1ltobj2("pnum","lsnum") ){
        return false;
    }
    */
    
    if( !percentValidate($("#hqd"),$("#hbid"),$("#oqd"),$("#obid"),$("#tqd"),$("#tbid")) ){
        return false;
    }
    
    return true;
}
</script>
<body onload="checkMessage('${message}')">
    <div style="position:absolute; left:-9999px;"><a href="#" id="setfoc"></a></div>
    <div data-role="page" id="home">
        <jsp:include page="page_header.jsp" flush="true">
        	<jsp:param name="title" value="儿科每日数据采集"/>
        	<jsp:param name="basePath" value="<%=basePath%>"/>
        </jsp:include>
        <div data-role="content"  data-theme="a">
        	<div class="roundCorner">
        	<div class="report_process_bg_description">医院名称前的 * 表示该医院在考评范围内</div>
            <form id="pediatricsForm" action="collectPediatrics" method="POST" data-ajax="false">
            	<input type="hidden" name="dataId" value="${existedData.dataId}"/>
	        	<input type="hidden" name="selectedHospital" value="${selectedHospital}"/>
                <div data-role="fieldcontain">
                    <label for="hospital" class="select">医院名称</label>
                    <select name="hospital" id="hospital" onchange="loadData(this.value)">
                        <option value="">--请选择--</option>
	                    <c:forEach var="hospital" items="${hospitals}">
	                        <option value="${hospital.name}" <c:if test="${hospital.name == selectedHospital}">selected</c:if>>${hospital.name}</option>
	                    </c:forEach>
	                </select>
                </div>
               	<div data-role="fieldcontain" class="formCollection">
                    <label for="pnum" id="pnum_label">当日门诊人数</label>
                    <input type="number" name="pnum" id="pnum" value="${existedData.pnum==null?0:existedData.pnum}"/>
                </div>
              	<div data-role="fieldcontain">
                   <label for="whnum" id="whnum_label">当日雾化人次</label>
                   <input type="number" name="whnum" id="whnum" value="${existedData.whnum==null?0:existedData.whnum}"/>
                </div>
                <div data-role="fieldcontain" spellcheck="true">
                    <label for="lsnum" id="lsnum_label">当日雾化令舒病人次</label>
                    <input type="number" name="lsnum" id="lsnum" value="${existedData.lsnum==null?0:existedData.lsnum}"/>
                </div>
                <div class="ui-grid-a formCollection">
	                <div class="ui-block-a">
	                	<div data-role="fieldcontain" >
		                    <label for="hqd" id="hqd_label">0.5mg QD(%)</label>
		                    <input type="number" name="hqd" id="hqd" value="${existedData.hqd==null?0:existedData.hqd}"/>
		                </div>
	                </div>
	                <div class="ui-block-b">
	                	<div data-role="fieldcontain">
		                    <label for="hbid" id="hbid_label">0.5mg BID(%)</label>
		                    <input type="number" name="hbid" id="hbid" value="${existedData.hbid==null?0:existedData.hbid}"/>
		                </div>
	                </div>
	            </div>
                <div class="ui-grid-a formCollection">
	                <div class="ui-block-a">
	                	<div data-role="fieldcontain">
		                    <label for="oqd" id="oqd_label">1mg QD(%)</label>
		                    <input type="number" name="oqd" id="oqd" value="${existedData.oqd==null?0:existedData.oqd}"/>
		                </div>
	                </div>
	                <div class="ui-block-b">
	                	<div data-role="fieldcontain">
		                    <label for="obid" id="obid_label">1mg BID(%)</label>
		                    <input type="number" name="obid" id="obid" value="${existedData.obid==null?0:existedData.obid}"/>
		                </div>
	                </div>
	            </div>
                <div class="ui-grid-a formCollection">
	                <div class="ui-block-a">
	                	<div data-role="fieldcontain">
		                    <label for="tqd" id="tqd_label">2mg QD(%)</label>
		                    <input type="number" name="tqd" id="tqd" value="${existedData.tqd==null?0:existedData.tqd}"/>
		                </div>
	                </div>
	                <div class="ui-block-b">
	                	<div data-role="fieldcontain">
		                    <label for="tbid" id="tbid_label">2mg BID(%)</label>
		                    <input type="number" name="tbid" id="tbid" value="${existedData.tbid==null?0:existedData.tbid}"/>
		                </div>
	                </div>
	            </div>
                <div data-role="fieldcontain" class="formCollection">
                    <label for="recipeType" >该医院主要处方方式</label>
                    <select name="recipeType" id="recipeType" >
                        <c:forEach var="recipeType" items="${recipeTypes}">
                            <option value="${recipeType}" <c:if test="${recipeType == existedData.recipeType}">selected</c:if>>一次门急诊处方${recipeType}天的雾化量</option>
                        </c:forEach>
                    </select>
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
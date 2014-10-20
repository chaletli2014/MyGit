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
	window.location.href="<%=basePath%>respirology?selectedHospital="+encodeURI(hospitalName);
}
function submitForm(){
	if(checkForm()){
		$.mobile.showPageLoadingMsg('b','数据提交中',false);
		if( $('.submit_btn') ){
			$('.submit_btn').removeAttr("onclick");
		}
		$('#respirologyForm').submit();
	}
}
function checkForm(){
	//$("#oqd"),$("#tqd"),$("#otid"),$("#tbid"),$("#ttid"),$("#thbid"),$("#fbid")
	if( !checkIsNotNull( $("#hospital") ) ){
		showCustomrizedMessage("医院不能为空");
		return false;
	}
	if( !checkIsNotNull( $("#pnum"),$("#aenum"),$("#whnum"),$("#lsnum") ) ){
		showCustomrizedMessage("数据不能为空或者字母");
		return false;
	}
	
	if( !isInteger($("#pnum"),$("#aenum"),$("#whnum"),$("#lsnum"))  ){
		return false;
	}
	
	if( !isLsNumAndPNumValid() ){
        return false;
	}
	
	if( !numlt9999("whnum") ){
        return false;
	}
	/*
	if( !obj1ltobj2("pnum","aenum") ){
		return false;
	}
	
	if( !obj1ltobj2("pnum","whnum") ){
		return false;
	}
	
	if( !obj1ltobj2("pnum","lsnum") ){
		return false;
	}
	*/
	
	if( !percentValidate($("#oqd"),$("#tqd"),$("#otid"),$("#tbid"),$("#ttid"),$("#thbid"),$("#fbid")) ){
		return false;
	}
	
    if( hasZeroValue($("#pnum"),$("#aenum"),$("#whnum"),$("#lsnum")) ){
        return confirmTypein('','popupConfirm','respirologyForm');
    }
    
	return true;
}
</script>
<body onload="checkMessage('${message}')">
    <div style="position:absolute; left:-9999px;"><a href="#" id="setfoc"></a></div>
    <div data-role="page" id="home">
        <jsp:include page="page_header.jsp" flush="true">
        	<jsp:param name="title" value="呼吸科每日数据采集"/>
        	<jsp:param name="basePath" value="<%=basePath%>"/>
        </jsp:include>
        <div data-role="content" data-theme="a">
        	<div class="roundCorner">
        	<div class="report_process_bg_description">医院名称前的 * 表示该医院在考评范围内</div>
	        <form id="respirologyForm" action="collectRespirology" method="POST" data-ajax="false" class="validate" onsubmit="return checkForm()">
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
	                <label for="pnum" id="pnum_label">当日目标科室病房病人数</label>
	                <input type="number" name="pnum" id="pnum" value="${existedData.pnum==null?0:existedData.pnum}"/>
	            </div>
	            <div data-role="fieldcontain" class="formCollection">
	                <label for="aenum" id="aenum_label">当日病房内AECOPD人数</label>
	                <input type="number" name="aenum" id="aenum"  value="${existedData.aenum==null?0:existedData.aenum}"/>
	            </div>
               	<div data-role="fieldcontain" class="formCollection">
	                <label for="whnum" id="whnum_label">当日雾化人数</label>
	                <input type="number" name="whnum" id="whnum"  value="${existedData.whnum==null?0:existedData.whnum}"/>
	            </div>
               	<div data-role="fieldcontain" class="formCollection">
	                <label for="lsnum" id="lsnum_label">当日雾化令舒病人数</label>
	                <input type="number" name="lsnum" id="lsnum"  value="${existedData.lsnum==null?0:existedData.lsnum}"/>
	            </div>
	            <div class="ui-grid-a formCollection">
	                <div class="ui-block-a">
	                	<div data-role="fieldcontain">
			                <label for="oqd" id="oqd_label">1mg QD(%)</label>
			                <input type="number" name="oqd" id="oqd"  value="${existedData.oqd==null?0:existedData.oqd}"/>
			            </div>
	                </div>
	                <div class="ui-block-b">
	                	<div data-role="fieldcontain">
			                <label for="tqd" id="tqd_label">2mg QD(%)</label>
			                <input type="number" name="tqd" id="tqd"  value="${existedData.tqd==null?0:existedData.tqd}"/>
			            </div>
	                </div>
	            </div>
	            <div class="ui-grid-a formCollection">
	                <div class="ui-block-a">
	                	<div data-role="fieldcontain">
			                <label for="otid" id="otid_label">1mg TID(%)</label>
			                <input type="number" name="otid" id="otid"  value="${existedData.otid==null?0:existedData.otid}"/>
			            </div>
	                </div>
	                <div class="ui-block-b">
	                	<div data-role="fieldcontain">
			                <label for="tbid" id="tbid_label">2mg BID(%)</label>
			                <input type="number" name="tbid" id="tbid"  value="${existedData.tbid==null?0:existedData.tbid}"/>
			            </div>
	                </div>
	            </div>
	            <div class="ui-grid-a formCollection">
	                <div class="ui-block-a">
	                	<div data-role="fieldcontain">
			                <label for="ttid" id="ttid_label">2mg TID(%)</label>
			                <input type="number" name="ttid" id="ttid"  value="${existedData.ttid==null?0:existedData.ttid}"/>
			            </div>
	                </div>
	                <div class="ui-block-b">
	                	<div data-role="fieldcontain">
			                <label for="thbid" id="thbid_label">3mg BID(%)</label>
			                <input type="number" name="thbid" id="thbid"  value="${existedData.thbid==null?0:existedData.thbid}"/>
			            </div>
	                </div>
	            </div>
	            <div class="ui-grid-a formCollection">
	                <div class="ui-block-a">
	                	<div data-role="fieldcontain">
			                <label for="fbid" id="fbid_label">4mg BID(%)</label>
			                <input type="number" name="fbid" id="fbid"  value="${existedData.fbid==null?0:existedData.fbid}"/>
			            </div>
	                </div>
	                <div class="ui-block-b"></div>
	            </div>
	            <%--
	            <div data-role="fieldcontain">
	                <label for="recipeType" >该医院主要处方方式</label>
	                <select name="recipeType" id="recipeType">
	                    <c:forEach var="recipeType" items="${recipeTypes}">
	                        <option value="${recipeType}" <c:if test="${recipeType == existedData.recipeType}">selected</c:if>>一次门急诊处方${recipeType}天的雾化量</option>
	                    </c:forEach>
	                </select>
	            </div>
	             --%>
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
            <jsp:param value="collectData" name="backURL"/>
        </jsp:include>
    </div>
</body>  
</html>  
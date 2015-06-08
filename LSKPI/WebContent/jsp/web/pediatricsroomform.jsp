<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %> 
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE HTML>
<html lang="en-US">
<%@include file="header.jsp" %> 
<script type="text/javascript">
function loadData(hospitaCode){
	$.mobile.showPageLoadingMsg('b','数据加载中',false);
	window.location.href="<%=basePath%>pediatricsRoom?selectedHospital="+encodeURI(hospitaCode);
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
    if( !checkIsNotNull( $("#pnum"),$("#whnum"),$("#lsnum") ) ){
        showCustomrizedMessage("数据不能为空或者字母");
        return false;
    }
	
	if( !isInteger($("#pnum"),$("#whnum"),$("#lsnum"))  ){
        return false;
	}
	/**
    if( !obj1ltobj2("whnum","lsnum") ){
        return false;
    }
     */
    if( !obj1ltobj2("pnum","lsnum") ){
        return false;
    }
	
	if( !isLsNumAndPNumValid() ){
        return false;
	}
	
	if( !numlt9999("whnum") ){
        return false;
	}
    
    if( !percentValidate($("#hqd"),$("#hbid"),$("#oqd"),$("#obid"),$("#tqd"),$("#tbid")) ){
        return false;
    }
    
    if( !percentValidate($("#whdaysRoom1Rate"),$("#whdaysRoom2Rate"),$("#whdaysRoom3Rate")
    		,$("#whdaysRoom4Rate"),$("#whdaysRoom5Rate"),$("#whdaysRoom6Rate"),$("#whdaysRoom7Rate"),$("#whdaysRoom8Rate"),$("#whdaysRoom9Rate"),$("#whdaysRoom10Rate")) ){
        return false;
    }
    
    if( !obj1ltobj2("homeWhRoomNum2","homeWhRoomNum1") ){
        return false;
    }
    
    if( hasZeroValue($("#pnum"),$("#whnum"),$("#lsnum")) ){
        return confirmTypein('','popupConfirm','pediatricsForm');
    }
    return true;
}
</script>
<body onload="checkMessage('${message}')">
    <div style="position:absolute; left:-9999px;"><a href="#" id="setfoc"></a></div>
    <div data-role="page" id="home">
        <jsp:include page="page_header.jsp" flush="true">
        	<jsp:param name="title" value="儿科每周病房雾化数据采集"/>
        	<jsp:param name="basePath" value="<%=basePath%>"/>
        </jsp:include>
        <div data-role="content"  data-theme="a">
        	<div class="roundCorner">
        	<div class="report_process_bg_description">标*为Core医院，标**为Emerging医院，所有医院每周只填报一次</div>
            <form id="pediatricsForm" action="collectPediatricsRoom" method="POST" data-ajax="false">
            	<input type="hidden" name="dataId" value="${existedData.dataId}"/>
	        	<input type="hidden" name="selectedHospital" value="${selectedHospital}"/>
	        	<input type="hidden" name="portNum" value="${existedData.portNum}"/>
                <div data-role="fieldcontain">
                    <label for="hospital" class="select">医院名称</label>
                    <select name="hospital" id="hospital" onchange="loadData(this.value)">
                        <option value="">--请选择--</option>
	                    <c:forEach var="hospital" items="${hospitals}">
	                        <option value="${hospital.code}" <c:if test="${hospital.code == selectedHospital}">selected</c:if>>${hospital.name}</option>
	                    </c:forEach>
	                </select>
                </div>
               	<div data-role="fieldcontain" class="formCollection">
                    <label for="pnum" id="pnum_label">当日住院人数</label>
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
                <c:if test="${hosObj!=null && hosObj.isWHBW=='1'}">
	                <div data-role="fieldcontain" spellcheck="true" id="whbw_div">
	                    <label for="whbwnum" id="whbwnum_label">当日雾化博利康尼人次</label>
	                    <input type="number" name="whbwnum" id="whbwnum" value="${existedData.whbwnum==null?0:existedData.whbwnum}"/>
	                </div>
                </c:if>
                <%--
                <div data-role="fieldcontain">
                    <label for="portNum" id="portNum_label">雾化端口数量</label>
                    <input type="number" id="portNum" value="${existedData.portNum==null?0:existedData.portNum}" readonly="readonly" disabled="disabled"/>
                </div>
                 --%>
                <div class="form_group_title">用药剂量</div>
                <HR style="FILTER: alpha(opacity=100,finishopacity=0,style=1)" width="100%" color=#987cb9 SIZE=3>
                
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
	            
	            <div class="form_group_title">雾化天数</div>
	            <HR style="FILTER: alpha(opacity=100,finishopacity=0,style=1)" width="100%" color=#987cb9 SIZE=3>
	            
	            <div class="ui-grid-a formCollection">
	                <div class="ui-block-a">
	                	<div data-role="fieldcontain" >
		                    <label for="whdaysRoom1Rate" id="whdaysRoom1Rate_label">1天(%)</label>
		                    <input type="number" name="whdaysRoom1Rate" id="whdaysRoom1Rate" value="${existedData.whdaysRoom1Rate==null?0:existedData.whdaysRoom1Rate}"/>
		                </div>
	                </div>
	                <div class="ui-block-b">
	                	<div data-role="fieldcontain" >
		                    <label for="whdaysRoom2Rate" id="whdaysRoom2Rate_label">2天(%)</label>
		                    <input type="number" name="whdaysRoom2Rate" id="whdaysRoom2Rate" value="${existedData.whdaysRoom2Rate==null?0:existedData.whdaysRoom2Rate}"/>
		                </div>
	                </div>
	            </div>
	            <div class="ui-grid-a formCollection">
	                <div class="ui-block-a">
	                	<div data-role="fieldcontain" >
		                    <label for="whdaysRoom3Rate" id="whdaysRoom3Rate_label">3天(%)</label>
		                    <input type="number" name="whdaysRoom3Rate" id="whdaysRoom3Rate" value="${existedData.whdaysRoom3Rate==null?0:existedData.whdaysRoom3Rate}"/>
		                </div>
	                </div>
	                <div class="ui-block-b">
	                	<div data-role="fieldcontain" >
		                    <label for="whdaysRoom4Rate" id="whdaysRoom4Rate_label">4天(%)</label>
		                    <input type="number" name="whdaysRoom4Rate" id="whdaysRoom4Rate" value="${existedData.whdaysRoom4Rate==null?0:existedData.whdaysRoom4Rate}"/>
		                </div>
	                </div>
	            </div>
	            <div class="ui-grid-a formCollection">
	                <div class="ui-block-a">
	                	<div data-role="fieldcontain" >
		                    <label for="whdaysRoom5Rate" id="whdaysRoom5Rate_label">5天(%)</label>
		                    <input type="number" name="whdaysRoom5Rate" id="whdaysRoom5Rate" value="${existedData.whdaysRoom5Rate==null?0:existedData.whdaysRoom5Rate}"/>
		                </div>
	                </div>
	                <div class="ui-block-b">
	                	<div data-role="fieldcontain" >
		                    <label for="whdaysRoom6Rate" id="whdaysRoom6Rate_label">6天(%)</label>
		                    <input type="number" name="whdaysRoom6Rate" id="whdaysRoom6Rate" value="${existedData.whdaysRoom6Rate==null?0:existedData.whdaysRoom6Rate}"/>
		                </div>
	                </div>
	            </div>
	            <div class="ui-grid-a formCollection">
	                <div class="ui-block-a">
	                	<div data-role="fieldcontain" >
		                    <label for="whdaysRoom7Rate" id="whdaysRoom7Rate_label">7天(%)</label>
		                    <input type="number" name="whdaysRoom7Rate" id="whdaysRoom7Rate" value="${existedData.whdaysRoom7Rate==null?0:existedData.whdaysRoom7Rate}"/>
		                </div>
	                </div>
	                <div class="ui-block-b">
	                	<div data-role="fieldcontain" >
		                    <label for="whdaysRoom8Rate" id="whdaysRoom8Rate_label">8天(%)</label>
		                    <input type="number" name="whdaysRoom8Rate" id="whdaysRoom8Rate" value="${existedData.whdaysRoom8Rate==null?0:existedData.whdaysRoom8Rate}"/>
		                </div>
	                </div>
	            </div>
	            <div class="ui-grid-a formCollection">
	                <div class="ui-block-a">
	                	<div data-role="fieldcontain" >
		                    <label for="whdaysRoom9Rate" id="whdaysRoom9Rate_label">9天(%)</label>
		                    <input type="number" name="whdaysRoom9Rate" id="whdaysRoom9Rate" value="${existedData.whdaysRoom9Rate==null?0:existedData.whdaysRoom9Rate}"/>
		                </div>
	                </div>
	                <div class="ui-block-b">
	                	<div data-role="fieldcontain" >
		                    <label for="whdaysRoom10Rate" id="whdaysRoom10Rate_label">10天及以上(%)</label>
		                    <input type="number" name="whdaysRoom10Rate" id="whdaysRoom10Rate" value="${existedData.whdaysRoom10Rate==null?0:existedData.whdaysRoom10Rate}"/>
		                </div>
	                </div>
	            </div>
	            <%--
                <div class="form_group_title">病房家庭雾化</div>
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
	           --%> 
	            <%--
                <div data-role="fieldcontain" class="formCollection">
                    <label for="recipeType" >该医院主要处方方式</label>
                    <select name="recipeType" id="recipeType" >
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
            <jsp:param value="collectPediatricsData" name="backURL"/>
        </jsp:include>
    </div>
</body>  
</html>  
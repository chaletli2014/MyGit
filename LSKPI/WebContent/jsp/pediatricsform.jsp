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
    if( !checkIsNotNull( $("#hospital") ) ){
        showCustomrizedMessage("医院不能为空");
        return false;
    }
    if( !checkIsNotNull( $("#pnum"),$("#whnum"),$("#lsnum") ) ){
        showCustomrizedMessage("数据不能为空或者字母");
        return false;
    }
    
    if( !obj1ltobj2("homeWhEmergingNum2","homeWhEmergingNum1") ){
        return false;
    }
    
    if( !obj1ltobj2("whnum","lsnum") ){
        return false;
    }
    
    if( !obj1ltobj2("pnum","lsnum") ){
        return false;
    }
	
	if( !isInteger($("#pnum"),$("#whnum"),$("#lsnum"))  ){
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
    
    if( !percentValidate($("#whdaysEmerging1Rate"),$("#whdaysEmerging2Rate"),$("#whdaysEmerging3Rate")
    		,$("#whdaysEmerging4Rate"),$("#whdaysEmerging5Rate"),$("#whdaysEmerging6Rate"),$("#whdaysEmerging7Rate")) ){
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
        	<jsp:param name="title" value="儿科每周门急诊雾化数据采集"/>
        	<jsp:param name="basePath" value="<%=basePath%>"/>
        </jsp:include>
        <div data-role="content"  data-theme="a">
        	<div class="roundCorner">
        	<div class="report_process_bg_description">标*为Core医院，每周填报3次；标**为Emerging医院，每周填报1次</div>
            <form id="pediatricsForm" action="collectPediatrics" method="POST" data-ajax="false">
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
                <c:if test="${hosObj!=null && hosObj.isWHBW=='1'}">
	                <div data-role="fieldcontain" spellcheck="true" id="whbw_div">
	                    <label for="whbwnum" id="whbwnum_label">当日雾化博利康尼人次</label>
	                    <input type="number" name="whbwnum" id="whbwnum" value="${existedData.whbwnum==null?0:existedData.whbwnum}"/>
	                </div>
                </c:if>
                <div data-role="fieldcontain">
                    <label for="portNum" id="portNum_label">雾化端口数量</label>
                    <input type="number" id="portNum" value="${existedData.portNum==null?0:existedData.portNum}" readonly="readonly" disabled="disabled"/>
                </div>
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
		                    <label for="whdaysEmerging1Rate" id="whdays1_label">1天(%)</label>
		                    <input type="number" name="whdaysEmerging1Rate" id="whdaysEmerging1Rate" value="${existedData.whdaysEmerging1Rate==null?0:existedData.whdaysEmerging1Rate}"/>
		                </div>
	                </div>
	                <div class="ui-block-b">
	                	<div data-role="fieldcontain" >
		                    <label for="whdaysEmerging2Rate" id="whdays2_label">2天(%)</label>
		                    <input type="number" name="whdaysEmerging2Rate" id="whdaysEmerging2Rate" value="${existedData.whdaysEmerging2Rate==null?0:existedData.whdaysEmerging2Rate}"/>
		                </div>
	                </div>
	            </div>
	            <div class="ui-grid-a formCollection">
	                <div class="ui-block-a">
	                	<div data-role="fieldcontain" >
		                    <label for="whdaysEmerging3Rate" id="whdays3_label">3天(%)</label>
		                    <input type="number" name="whdaysEmerging3Rate" id="whdaysEmerging3Rate" value="${existedData.whdaysEmerging3Rate==null?0:existedData.whdaysEmerging3Rate}"/>
		                </div>
	                </div>
	                <div class="ui-block-b">
	                	<div data-role="fieldcontain" >
		                    <label for="whdaysEmerging4Rate" id="whdays4_label">4天(%)</label>
		                    <input type="number" name="whdaysEmerging4Rate" id="whdaysEmerging4Rate" value="${existedData.whdaysEmerging4Rate==null?0:existedData.whdaysEmerging4Rate}"/>
		                </div>
	                </div>
	            </div>
	            <div class="ui-grid-a formCollection">
	                <div class="ui-block-a">
	                	<div data-role="fieldcontain" >
		                    <label for="whdaysEmerging5Rate" id="whdays5_label">5天(%)</label>
		                    <input type="number" name="whdaysEmerging5Rate" id="whdaysEmerging5Rate" value="${existedData.whdaysEmerging5Rate==null?0:existedData.whdaysEmerging5Rate}"/>
		                </div>
	                </div>
	                <div class="ui-block-b">
	                	<div data-role="fieldcontain" >
		                    <label for="whdaysEmerging6Rate" id="whdays6_label">6天(%)</label>
		                    <input type="number" name="whdaysEmerging6Rate" id="whdaysEmerging6Rate" value="${existedData.whdaysEmerging6Rate==null?0:existedData.whdaysEmerging6Rate}"/>
		                </div>
	                </div>
	            </div>
	            <div class="ui-grid-a formCollection">
	                <div class="ui-block-a">
	                	<div data-role="fieldcontain" >
		                    <label for="whdaysEmerging7Rate" id="whdays7_label">7天及以上(%)</label>
		                    <input type="number" name="whdaysEmerging7Rate" id="whdaysEmerging7Rate" value="${existedData.whdaysEmerging7Rate==null?0:existedData.whdaysEmerging7Rate}"/>
		                </div>
	                </div>
	                <div class="ui-block-b">
	                </div>
	            </div>
	            <%-- 
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
        <%@include file="popupConfirm.jsp" %>
        <jsp:include page="page_footer.jsp">
            <jsp:param value="<%=basePath%>" name="basePath"/>
            <jsp:param value="collectPediatricsData" name="backURL"/>
        </jsp:include>
    </div>
</body>  
</html>  
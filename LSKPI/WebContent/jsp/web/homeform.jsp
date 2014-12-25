<%@page import="java.util.Date"%>
<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %> 
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE HTML>
<html lang="en-US">
<%@include file="header.jsp" %> 
<script type="text/javascript">
function loadData(selectedDoctor){
	$.mobile.showPageLoadingMsg('b','数据加载中',false);
	window.location.href="<%=basePath%>collecthomedata?selectedDoctor="+selectedDoctor;
}
function submitForm(){
	var myDate = new Date();
	var dayInWeekVar = myDate.getDay();
	if( dayInWeekVar>3||dayInWeekVar==0 ){
        submitHomeCollection();
	}else{
		if(checkForm()){
	        submitHomeCollection();
	    }
	}
}
function checkForm(){
	if( !checkIsNotNull( $("#doctor") ) ){
		showCustomrizedMessage("医生名称不能为空");
		return false;
	}
	if( !checkIsNotNull( $("#salenum"),$("#asthmanum"),$("#ltenum"),$("#lsnum"),$("#efnum"),$("#ftnum"),$("#lttnum") ) ){
		showCustomrizedMessage("数据不能为空或者字母");
		return false;
	}
	
	if( !isInteger($("#salenum"),$("#asthmanum"),$("#ltenum"),$("#lsnum"),$("#efnum"),$("#ftnum"),$("#lttnum"))  ){
		return false;
	}
	
	if( !obj1ltobj2("asthmanum","ltenum") ){
		return false;
	}
	
	if( !obj1ltobj2("ltenum","lsnum") ){
		return false;
	}
	
	if( Number($("#efnum").val()) + Number($("#ftnum").val()) + Number($("#lttnum").val()) != Number($("#lsnum").val()) ){
		showCustomrizedMessage("8到15天，15到30天和大于30天的病人数相加应等于维持期病人中推荐使用令舒的人数");
		return false;
	}
	return true;
}
</script>
<%
	int dayInWeek = new Date().getDay();
	boolean noInput = dayInWeek>3||dayInWeek==0;
%>
<body onload="checkMessage('${message}')">
    <div style="position:absolute; left:-9999px;"><a href="#" id="setfoc"></a></div>
    <div data-role="page" id="home">
        <jsp:include page="page_header.jsp" flush="true">
        	<jsp:param name="title" value="家庭雾化数据采集"/>
        	<jsp:param name="basePath" value="<%=basePath%>"/>
        </jsp:include>
        <div data-role="content" data-theme="a">
        	<div data-role="fieldcontain" class="department_img_div"> 
                <img alt="" src="<%=basePath%>images/img_bg_doctor_w.png" onclick="javascript:window.location.href='<%=basePath%>doctormaintenance'" style="cursor: pointer;">
                <br/>
                <img alt="" src="<%=basePath%>images/img_bg_doctor_approval_w.png" onclick="javascript:window.location.href='<%=basePath%>doctorapproval'" style="cursor: pointer;">
            </div>
        	<div class="roundCorner">
	        <form id="homeForm" action="docollecthomedata" method="POST" data-ajax="false" class="validate">
	        	<input type="hidden" name="dataId" value="${existedData.id}"/>
	        	<input type="hidden" name="selectedDoctor" value="${selectedDoctor}"/>
	            <div data-role="fieldcontain">
	                <label for="doctor" class="select">医生名称</label>
	                <select name="doctor" id="doctor" onchange="loadData(this.value)">
                        <option value="">--请选择--</option>
	                    <c:forEach var="doctor" items="${doctors}">
	                        <option value="${doctor.id}" <c:if test="${doctor.id == selectedDoctor}">selected</c:if>>${doctor.nameWithHosName}</option>
	                    </c:forEach>
	                </select>
	            </div>
	            <div data-role="fieldcontain" class="formCollection">
	                <label for="salenum" id="salenum_label">卖/赠泵数量（每周新病人人次）</label>
	                <input type="number" name="salenum" id="salenum" value="${existedData.salenum==null?0:existedData.salenum}" <%if(noInput){%>readonly="readonly" disabled="disabled"<%}%>/>
	            </div>
	            <div data-role="fieldcontain" class="formCollection">
	                <label for="asthmanum" id="asthmanum_label">哮喘*患者人数</label>
	                <input type="number" name="asthmanum" id="asthmanum"  value="${existedData.asthmanum==null?0:existedData.asthmanum}" <%if(noInput){%>readonly="readonly" disabled="disabled"<%}%>/>
	            </div>
               	<div data-role="fieldcontain" class="formCollection">
	                <label for="ltenum" id="ltenum_label">处方≥8天的哮喘维持期病人数</label>
	                <input type="number" name="ltenum" id="ltenum"  value="${existedData.ltenum==null?0:existedData.ltenum}" <%if(noInput){%>readonly="readonly" disabled="disabled"<%}%>/>
	            </div>
               	<div data-role="fieldcontain" class="formCollection">
	                <label for="lsnum" id="lsnum_label">维持期病人中推荐使用令舒的人数</label>
	                <input type="number" name="lsnum" id="lsnum"  value="${existedData.lsnum==null?0:existedData.lsnum}" <%if(noInput){%>readonly="readonly" disabled="disabled"<%}%>/>
	            </div>
	            <div data-role="fieldcontain" class="formCollection">
	            	<label>以下三个病人数之和=维持期病人中推荐使用令舒的人数</label>
	            </div>
               	<div data-role="fieldcontain" class="formCollection">
	                <label for="efnum" id="efnum_label">8≤DOT<15天，病人数</label>
	                <input type="number" name="efnum" id="efnum"  value="${existedData.efnum==null?0:existedData.efnum}" <%if(noInput){%>readonly="readonly" disabled="disabled"<%}%>/>
	            </div>
               	<div data-role="fieldcontain" class="formCollection">
	                <label for="ftnum" id="ftnum_label">15≤DOT<30天，病人数</label>
	                <input type="number" name="ftnum" id="ftnum"  value="${existedData.ftnum==null?0:existedData.ftnum}" <%if(noInput){%>readonly="readonly" disabled="disabled"<%}%>/>
	            </div>
               	<div data-role="fieldcontain" class="formCollection">
	                <label for="lttnum" id="lttnum_label">DOT≥30天，病人数</label>
	                <input type="number" name="lttnum" id="lttnum"  value="${existedData.lttnum==null?0:existedData.lttnum}" <%if(noInput){%>readonly="readonly" disabled="disabled"<%}%>/>
	            </div>
	            
	            <div style="text-align: center;">
	            	<a class="submit_btn" href="javascript:void(0)" onclick="submitForm()">
			            <img alt="" src="<%=basePath%>images/button_submit.png" style="cursor: pointer;"/>
	            	</a>
	            </div>
	        </form>
        	</div>
        	<div data-role="popup" id="popupConfirm" data-inline="true" data-position-to="window" data-theme="b" style="max-width:100%;" class="ui-corner-all">
               <div data-role="header" data-theme="a" class="ui-corner-top">
                     <h1></h1><a href="#" data-rel="back" data-role="button" data-theme="a" data-icon="delete" data-mini="true" data-iconpos="notext" class="ui-btn-right">Close</a>
               </div>
               <div data-role="content" data-theme="d" class="ui-corner-bottom ui-content">
                 <p style="width:100%"></p>
                 <div style="text-align: center;">
                     <a href="javascript:void(0)" id="home_collection_submit" style="display: block;">
                        <img alt="" src="<%=basePath%>images/button_submit_140.png" style="cursor: pointer;" />
                     </a>
                     <a href="javascript:void(0)" id="home_collection_cancel" style="display: block;">
                        <img alt="" src="<%=basePath%>images/button_cancel_140.png" style="cursor: pointer;" />
                     </a>
                 </div>
               </div>
             </div>
        </div>
        <jsp:include page="page_footer.jsp">
            <jsp:param value="<%=basePath%>" name="basePath"/>
            <jsp:param value="collectData" name="backURL"/>
        </jsp:include>
    </div>
</body>  
</html>  
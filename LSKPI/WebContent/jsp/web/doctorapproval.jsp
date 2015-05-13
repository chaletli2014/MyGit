<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %> 
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE HTML>
<html lang="en-US">
<%@include file="header.jsp" %> 
<script type="text/javascript">
function doDeleteDr(drId){
	$("#popupConfirm #delete_dr_submit").removeAttr("onclick");
	
	$.mobile.showPageLoadingMsg('b','数据加载中',false);
    window.location.href = "<%=basePath%>doDeleteDoctor?dataId="+drId;
}
function doRejectDr(drId){
	$.mobile.showPageLoadingMsg('b','数据加载中',false);
    window.location.href = "<%=basePath%>doRejectDoctor?dataId="+drId;
}
function confirmDeleteSingleDr(drId){
	$("#popupConfirm h1").html("系统信息");
    $("#popupConfirm p").html("是否删除所选医生");
    $("#popupConfirm #delete_dr_submit").unbind("click").click(function() { 
        $("#popupConfirm").popup("close"); 
        
        doDeleteDr(drId);
    });
    $("#popupConfirm #delete_dr_cancel").unbind("click").click(function() { $("#popupConfirm").popup("close"); });
    $("#popupConfirm").popup("open");
    
}
function confirmRejectSingleDr(drId){
	doRejectDr(drId);
}
function confirmDeleteDr(){
	var drId =[];
	$.each($(".approvalDr:checked"),function(i,n){
		drId[i]=n.value;
	});
	if( drId == null || drId == '' ){
		showCustomrizedMessage("请选择审批的医生");
		return;
	}
	
    $("#popupConfirm h1").html("系统信息");
    $("#popupConfirm p").html("是否删除所选医生");
    $("#popupConfirm #delete_dr_submit").unbind("click").click(function() { 
        $("#popupConfirm").popup("close");
        
        doDeleteDr(drId);
    });
    $("#popupConfirm #delete_dr_cancel").unbind("click").click(function() { $("#popupConfirm").popup("close"); });
    $("#popupConfirm").popup("open");
}
function confirmRejectDr(){
	var drId =[];
	$.each($(".approvalDr:checked"),function(i,n){
		drId[i]=n.value;
	});
	if( drId == null || drId == '' ){
		showCustomrizedMessage("请选择审批的医生");
		return;
	}
	doRejectDr(drId);
}
function selectAll(){
	if( $("#sg_1") && $("#sg_1").prop("checked") ){
		$.each($(".approvalDr"),function(i,n){
			$(this).prop('checked',true).checkboxradio("refresh");
		});
	}else{
		$.each($(".approvalDr"),function(i,n){
			$(this).prop('checked',false).checkboxradio("refresh");
		});
	}
	$("#sg_1").checkboxradio("refresh");
}
</script>

<style>
input.approvalDr{
	width: 20px;
	height: 20px;
} 
</style>
<body onload="checkMessage('${message}')">
    <div style="position:absolute; left:-9999px;"><a href="#" id="setfoc"></a></div>
    <div data-role="page" id="home">
        <jsp:include page="page_header.jsp" flush="true">
        	<jsp:param name="title" value="家庭雾化删除医生审批"/>
        	<jsp:param name="basePath" value="<%=basePath%>"/>
        </jsp:include>
        <div data-role="content"  data-theme="a">
            <c:if test="${doctors!= null && fn:length(doctors) > 0}">
	        	<div class="roundCorner">
	        		<div class="dailyReport_table_Title">待审批医生列表</div>
	        		<table class="mobileReport_table">
	                    <tr class="mobileReport_table_header">
	                        <td width="20%">
	                        	<input type="checkbox" id="sg_1" name="selectionGroup" onclick="selectAll()"/>
	                        	<label for="sg_1">全选</label>
	                        </td>
	                        <td width="20%">医院</td>
	                        <td width="15%">医生</td>
	                        <td width="15%">删除理由</td>
	                        <td width="10%">操作</td>
	                    </tr>
	                    <c:forEach items="${doctors}" var="doctor" varStatus="status">
	                        <tr class="mobileReport_table_body <c:if test="${status.count%2==0}">mobileReport_tr_even</c:if>" style="height:40px;">
	                            <td>
	                            	<input type="checkbox" name="doctorsToBeDeleted" class="approvalDr" value="${doctor.drId}"/>${status.count}
	                            </td>
	                            <td>${doctor.hospitalName}</td>
	                            <td>${doctor.drName}</td>
	                            <td>${doctor.deleteReason}</td>
	                            <td>
	                            	<c:if test="${currentUser.level=='BM'}">
	                                   <a href="javascript:void(0)" onclick="confirmDeleteSingleDr(${doctor.drId})">
	                                   		<img alt="" src="<%=basePath%>images/button_submit_140.png" style="cursor: pointer;" />
	                                   </a>
			                           <a href="javascript:void(0)" onclick="confirmRejectSingleDr(${doctor.drId})">
						                    <img alt="" src="<%=basePath%>images/button_reject.png" style="cursor: pointer;" />
						               </a>
					               </c:if>
	                            </td>
	                        </tr>
	                    </c:forEach>
	                    <c:if test="${currentUser.level=='BM'}">
		                    <tr>
		                    	<td colspan="5" align="right">
			                    	<a href="javascript:void(0)" onclick="confirmDeleteDr()">
		                           		<img alt="" src="<%=basePath%>images/button_submit_all.png" style="cursor: pointer;" />
			                    	</a>
			                    	<a href="javascript:void(0)" onclick="confirmRejectDr()">
		                            	<img alt="" src="<%=basePath%>images/button_reject_all.png" style="cursor: pointer;" />
			                    	</a>
		                    	</td>
		                   	</tr>
	                    </c:if>
                    </table>
	        	</div>
            </c:if>
            <c:if test="${doctors==null||fn:length(doctors)==0}">
                <div class="roundCorner">
                                                                目前没有删除的医生需要审批
                </div>
            </c:if>
            <div data-role="popup" id="popupConfirm" data-inline="true" data-position-to="window" data-theme="b" style="max-width:100%;" class="ui-corner-all">
			   <div data-role="header" data-theme="a" class="ui-corner-top">
			         <h1></h1><a href="#" data-rel="back" data-role="button" data-theme="a" data-icon="delete" data-mini="true" data-iconpos="notext" class="ui-btn-right">Close</a>
			   </div>
			   <div data-role="content" data-theme="d" class="ui-corner-bottom ui-content">
			     <p style="width:100%"></p>
			     <div style="text-align: center;">
			         <a href="javascript:void(0)" id="delete_dr_submit" style="display: block;">
			         	<img alt="" src="<%=basePath%>images/button_submit_140.png" style="cursor: pointer;" />
			         </a>
			         <a href="javascript:void(0)" id="delete_dr_cancel" style="display: block;">
			         	<img alt="" src="<%=basePath%>images/button_cancel_140.png" style="cursor: pointer;" />
			         </a>
			     </div>
			   </div>
		     </div>
        </div>
        <jsp:include page="page_footer.jsp">
            <jsp:param value="<%=basePath%>" name="basePath"/>
            <jsp:param value="collecthomedata" name="backURL"/>
        </jsp:include>
    </div>
</body>  
</html>  
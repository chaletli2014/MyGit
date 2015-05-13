<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %> 
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE HTML>
<html lang="en-US">
<%@include file="header.jsp" %> 
<script type="text/javascript">
function submitForm(){
	$(".submit_btn").removeAttr("onclick");
    $.mobile.showPageLoadingMsg('b','数据保存中',false);
    $('#doDeleteDoctorForm').submit();
}
</script>
<body onload="checkMessage('${message}')">
    <div data-role="page" id="home">
        <div data-role="content"  data-theme="a">
            <div class="roundCorner" style="width:50%">
            <form id="doDeleteDoctorForm" action="doApprovalDoctor" method="POST" data-ajax="false" style="padding:10px 20px;">
	            <input type="hidden" name="dataId" value="${dataId}"/>
	            <fieldset data-role="controlgroup">
				    <legend>请选择删除的原因:</legend>
				    <input type="radio" name="reason" id="reason-1" value="添加错误" checked="checked">
				    <label for="reason-1">添加错误</label>
				    <input type="radio" name="reason" id="reason-2" value="医生重复">
				    <label for="reason-2">医生重复</label>
				    <input type="radio" name="reason" id="reason-3" value="该医生无潜力">
				    <label for="reason-3">该医生无潜力</label>
				    <input type="radio" name="reason" id="reason-4" value="other">
				    <label for="reason-4">
				                            其他
				        <textarea cols="40" rows="8" name="reason_other" id="reason_other"></textarea>
				    </label>
				</fieldset>
				<div style="text-align: center">
		            <a class="submit_btn" href="javascript:void(0)">
		                <img alt="" src="<%=basePath%>images/button_submit_140.png" style="cursor: pointer;" />
		            </a>
		            <a href="javascript:void(0)" id="edit_dr_cancel" style="display: block;" onclick="javascript:window.location.href='<%=basePath%>doctormaintenance'">
                      <img alt="" src="<%=basePath%>images/button_cancel_140.png" style="cursor: pointer;" />
                    </a>
				</div>
            </form>
            </div>
        </div>
    </div>
</body>  
</html>  
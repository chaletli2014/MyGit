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
        $.mobile.showPageLoadingMsg('b','数据保存中',false);
        $('#addDoctorForm').submit();
    }
}
function checkForm(){
    if( !checkIsNotNull( $("#doctorname") ) ){
        showCustomrizedMessage("请填入医生姓名");
        return false;
    }
    return true;
}
function deletedoctor(dataId,doctorname){
    $.mobile.showPageLoadingMsg('b','数据加载中',false);
    $("#dataId").val(dataId);
    $("#approvalDoctorForm").submit();
}
</script>
<body onload="checkMessage('${message}')">
    <div style="position:absolute; left:-9999px;"><a href="#" id="setfoc"></a></div>
    <div data-role="page" id="home">
        <jsp:include page="page_header.jsp" flush="true">
        	<jsp:param name="title" value="客户列表维护"/>
        	<jsp:param name="basePath" value="<%=basePath%>"/>
        </jsp:include>
        <div data-role="content"  data-theme="a">
	        <c:if test="${currentUser.level=='DSM'||currentUser.level=='REP'}">
	        	<div class="roundCorner">
	        	<div class="dailyReport_table_Title">添加新医生</div>
	            <form id="addDoctorForm" action="doAddDoctor" method="POST" data-ajax="false">
	                <div data-role="fieldcontain">
	                    <label for="hospital" class="select">所在医院</label>
	                    <select name="hospital" id="hospital">
	                        <option value="">--请选择--</option>
	                        <c:forEach var="hospital" items="${hospitals}">
	                            <option value="${hospital.code}" <c:if test="${hospital.code == selectedHospitalCode}">selected</c:if>>${hospital.name}</option>
	                        </c:forEach>
	                    </select>
	                </div>
	                <div data-role="fieldcontain">
	                    <label for="doctorname" class="select">医生姓名</label>
	                    <input name="doctorname" id="doctorname"/>
	                </div>
	                <div style="text-align: center;">
	                    <a class="submit_btn1" href="javascript:void(0)" onclick="checkIfDoctorExsits()">
	                        <img alt="" src="<%=basePath%>images/button_submit.png" style="cursor: pointer;" />
	                    </a>
	                </div>
	            </form>
	            </div>
	        </c:if>
            <c:if test="${existedDoctors!= null && fn:length(existedDoctors) > 0}">
	        	<div class="roundCorner">
	        		<div class="dailyReport_table_Title">已存在医生列表</div>
	        		<table class="mobileReport_table">
	                    <tr class="mobileReport_table_header">
	                        <td width="40%">目标医院名称</td>
	                        <td width="15%">目标医生</td>
	                        <td width="15%">负责销售代表</td>
	                        <td width="10%">操作</td>
	                    </tr>
	                    <c:forEach items="${existedDoctors}" var="doctor" varStatus="status">
	                        <tr class="mobileReport_table_body <c:if test="${status.count%2==0}">mobileReport_tr_even</c:if>">
	                            <td>${doctor.hospitalName}</td>
	                            <td>${doctor.name}</td>
	                            <td>${doctor.salesName}</td>
	                            <td>
	                               <c:if test="${currentUser.level=='DSM'||currentUser.level=='REP'}">
			                           <a href="javascript:void(0)" onclick="showEdit(${doctor.id},'${doctor.name}','${doctor.hospitalName}','${doctor.hospitalCode}')">
						                    <img alt="" src="<%=basePath%>images/button_modify.png" style="cursor: pointer;" />
						                </a>
	                               </c:if>
	                               <c:if test="${currentUser.level=='DSM'||currentUser.level=='BM'}">
                                       <a href="javascript:void(0)" onclick="confirm('',${doctor.id},'${doctor.name}')">
                                         <img alt="" src="<%=basePath%>images/button_delete.png" style="cursor: pointer;" />
                                       </a>
                                   </c:if>
                                   <c:if test="${currentUser.level=='DSM'||currentUser.level=='BM'||currentUser.level=='REP'}">
									   <a href="javascript:void(0)" onclick="showRefer(${doctor.id},'${doctor.name}','${doctor.hospitalName}','${doctor.salesCode}')">
                                         <img alt="" src="<%=basePath%>images/button_refer.png" style="cursor: pointer;" />
                                       </a>
                                   </c:if>
	                            </td>
	                        </tr>
	                    </c:forEach>
                    </table>
	        	</div>
            </c:if>
            <c:if test="${existedDoctors==null||fn:length(existedDoctors)==0}">
                <div class="roundCorner">
                                                                该用户目前未关联任何医生
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
		     <form id="approvalDoctorForm" action="doApprovalDoctor" method="POST" data-ajax="false">
		    	<input type="hidden" id="dataId" name="dataId" value=""/>
		     </form>
		     <form id="relateDoctorForm" action="doRelateDoctor" method="POST" data-ajax="false">
		     	<input type="hidden" id="relatedDoctorName" name="relatedDoctorName" value=""/>
		     	<input type="hidden" id="relatedHospitalCode" name="relatedHospitalCode" value=""/>
		     </form>
		     <%@include file="editdoctor.jsp" %>
		     <%@include file="editRelationship.jsp" %>
        </div>
        <jsp:include page="page_footer.jsp">
            <jsp:param value="<%=basePath%>" name="basePath"/>
            <jsp:param value="collecthomedata" name="backURL"/>
        </jsp:include>
    </div>
</body>  
</html>  
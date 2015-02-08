function confirm(title, dataId, doctorname) {
    if (title == null || title == "") title = "系统信息";
    $("#popupConfirm h1").html(title);
    $("#popupConfirm p").html("是否删除医生: "+doctorname);
    $("#popupConfirm #delete_dr_submit").unbind("click").click(function() { 
        $("#popupConfirm").popup("close"); 
        deletedoctor(dataId,doctorname);
    });
    $("#popupConfirm #delete_dr_cancel").unbind("click").click(function() { $("#popupConfirm").popup("close"); });
    $("#popupConfirm").popup("open");
}
function submitHomeCollection(){
//	var currentDate = new Date();
//	if( currentDate.getDay() > 3 ){
//	    $("#popupConfirm h1").html("系统信息");
//	    $("#popupConfirm p").html("当前为补录时段，<br/>所填数据会覆盖上周数据，<br/>是否确认？");
//	    $("#popupConfirm #home_collection_submit").unbind("click").click(function() { 
//	        $("#popupConfirm").popup("close");
//			$.mobile.showPageLoadingMsg('b','数据提交中',false);
//			if( $('.submit_btn') ){
//				$('.submit_btn').removeAttr("onclick");
//			}
//			$('#homeForm').submit();
//	    });
//	    $("#popupConfirm #home_collection_cancel").unbind("click").click(function() { $("#popupConfirm").popup("close"); });
//	    $("#popupConfirm").popup("open");
//	}else{
		$.mobile.showPageLoadingMsg('b','数据提交中',false);
        if( $('.submit_btn') ){
            $('.submit_btn').removeAttr("onclick");
        }
        $('#homeForm').submit();
//	}
}
function showEdit(dataId,doctorname,hospitalName,hospitalCode) {
    $("#popupEdit #editdoctor_notification").html("");
    $("#popupEdit #doctorname").parent().removeClass("ls-error");
    $("#popupEdit h1").html("医生信息维护");
    $("#popupEdit #edit_dataId").val(dataId);
    $("#popupEdit #edit_doctorname").val(doctorname);
    $("#popupEdit #edit_hospitalcode").val(hospitalCode);
    
    $("#popupEdit #hospitalname").val(hospitalName);
    $("#popupEdit #doctorname").val(doctorname);
    
    $("#popupEdit #edit_dr_submit").unbind("click").click(function() { 
        if( !checkIsNotNull( $("#popupEdit #doctorname") ) ){
            $("#popupEdit #editdoctor_notification").html("请填入医生姓名");
        }else{
            $.mobile.showPageLoadingMsg('b','数据保存中',false);
            $("#popupEdit").popup("close");
            $('#popupEdit #doEditDoctorForm').submit();
        }
    });
    $("#popupEdit #edit_dr_cancel").unbind("click").click(function() { $("#popupEdit").popup("close"); });
    $("#popupEdit").popup("open");
}
function showRefer(doctorId,doctorname,hospitalName,salesCode) {
	$("#popupEditRelationship h1").html("医生关系维护");
	$("#popupEditRelationship #edit_dataId").val(doctorId);
	$("#popupEditRelationship #edit_relatedSales").val(salesCode);
	
	$("#popupEditRelationship #hospitalname").val(hospitalName);
	$("#popupEditRelationship #doctorname").val(doctorname);
	
	$("#popupEditRelationship #edit_dr_submit").unbind("click").click(function() { 
//		if( !checkIsNotNull( $("#popupEditRelationship #relatedSales") ) ){
//			$("#popupEditRelationship #editdoctor_notification").html("请选择一个销售");
//		}else{
		$.mobile.showPageLoadingMsg('b','数据保存中',false);
		$("#popupEditRelationship").popup("close");
		$('#popupEditRelationship #doEditDoctorRelationshipForm').submit();
//		}
	});
	$("#popupEditRelationship #edit_dr_cancel").unbind("click").click(function() { $("#popupEditRelationship").popup("close"); });
	$("#popupEditRelationship").popup("open");
	$("#popupEditRelationship #relatedSales option[value='"+salesCode+"']").attr("selected",true);
	$("#popupEditRelationship #relatedSales").selectmenu('refresh');
}

function checkIfDoctorExsits(){
	if( !checkIsNotNull( $("#doctorname") ) ){
		showCustomrizedMessage("请填入医生姓名");
		return;
	}
	var hospitalCode = $("#hospital").val();
	var doctorName = $("#doctorname").val();
	$.mobile.showPageLoadingMsg('b','医生验证中...',false);
	$.ajax({ 
        type: "get",
        url: "checkIfDoctorExsits", 
        contentType:'application/json',
        dataType:'json',
        data:{
        	hospitalCode:hospitalCode,
        	doctorName:doctorName
        },
        success: function (data) {
        	if (data && data.count>0) {
        		confirmDoctor('','popupConfirm','addDoctorForm',hospitalCode,doctorName);
            }else{
            	$("#addDoctorForm").submit();
            }
        	$.mobile.hidePageLoadingMsg();
         }, 
         error: function (XMLHttpRequest, textStatus, errorThrown) { 
        	 $.mobile.hidePageLoadingMsg();
        	 $('html').simpledialog2({
        	        mode: 'blank',
        	        headerText: '提示',
        	        headerClose: true,
        	        blankContent : 
        	          "<label style='text-align:center;display:block'>添加医生失败</label>" + "<a rel='close' data-role='button' href='#'>关闭</a>"
        	 });
         } 
	});
}

function confirmDoctor(title, popupDivID, formID, hospitalCode, doctorName) {
    if (title == null || title == "") title = "系统信息";
    $("#"+popupDivID+" h1").html(title);
    $("#"+popupDivID+" p").html("该医生姓名已经存在，点击确认，则直接关联该医生，否则新建医生");
    $("#"+popupDivID+" #delete_dr_submit").unbind("click").click(function() { 
        $("#"+popupDivID).popup("close");
        
        $.mobile.showPageLoadingMsg('b','数据提交中',false);
        
        $("#relatedHospitalCode").val(hospitalCode);
        $("#relatedDoctorName").val(doctorName);
        
		if( $('.submit_btn1') ){
			$('.submit_btn1').removeAttr("onclick");
		}
		$("#relateDoctorForm").submit();
    });
    $("#"+popupDivID+" #delete_dr_cancel").unbind("click").click(function() { 
    	$("#"+popupDivID).popup("close");
    	$.mobile.showPageLoadingMsg('b','数据提交中',false);
		if( $('.submit_btn1') ){
			$('.submit_btn1').removeAttr("onclick");
		}
		$("#"+formID).submit();
    });
    $("#"+popupDivID).popup("open");
}
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
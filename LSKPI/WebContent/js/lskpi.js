function checkIsNotNull(obj){
	var argumentLength = arguments.length;
	var invalidCount = 0;
	
	for( var i = 0; i < argumentLength; i++ ){
		if( arguments[i] && arguments[i].val() == "" ){
			arguments[i].parent().addClass("ls-error");
			invalidCount++;
		}
	}
	if( invalidCount > 0 ){
		return false;
	}else{
		return true;
	}
}

function obj1ltobj2(id1, id2){
	if( $("#"+id1) && $("#"+id2) ){
		if( Number($("#"+id1).val()) < Number($("#"+id2).val()) ){
			showCustomrizedMessage($("#"+id1+"_label").text()+"必须大于或者等于"+$("#"+id2+"_label").text());
			$("#"+id1).parent().addClass("ls-error");
			$("#"+id2).parent().addClass("ls-error");
			return false;
		}
	}
	return true;
}

function numlt9999(id){
	if( $("#"+id) ){
		if( Number($("#"+id).val()) > 9999 ){
			showCustomrizedMessage($("#"+id+"_label").text()+"必须小于9999");
			$("#"+id).parent().addClass("ls-error");
			return false;
		}
	}
	return true;
}

function isLsNumAndPNumValid(){
	if( Number($("#lsnum").val()) > Number($("#pnum").val())*1.5 ){
		showCustomrizedMessage("当日雾化令舒病人数据异常，该数值不能超过总人数的1.5倍");
        return false;
	}
	return true;
}

function percentValidate(){
	var argumentLength = arguments.length;
	var invalidCount = 0;
	var percentSum = 0;
	for( var i = 0; i < argumentLength; i++ ){
		if( arguments[i] ){
			if( Number(arguments[i].val()) > 100 ){
				arguments[i].parent().addClass("ls-error");
				invalidCount++;
			}else{
				percentSum = percentSum + Number(arguments[i].val());
			}
		}
	}
	var lsnum = 0;
	if($("#lsnum")){
		lsnum = $("#lsnum").val();
	}
	
	if( invalidCount > 0 ){
		showCustomrizedMessage("百分比录入数据不能大于100");
		return false;
	}else if( percentSum != 100 && lsnum != 0 ){
		showCustomrizedMessage("百分比录入数据之和必须为100");
		return false;
	}else{
		return true;
	}
}

function isDouble(obj){
	var r = /^[1-9]\d*|0$/;
	var argumentLength = arguments.length;
	var invalidCount = 0;
	
	for( var i = 0; i < argumentLength; i++ ){
		if( arguments[i] && !r.test(arguments[i].val())){
			arguments[i].parent().addClass("ls-error");
			invalidCount++;
		}
	}
	if( invalidCount > 0 ){
		showCustomrizedMessage("数值只能为正数或0");
		return false;
	}else{
		return true;
	}
}

function isInteger(obj){
	var r = /^[1-9]\d*$/;
	var argumentLength = arguments.length;
	var invalidCount = 0;
	
	for( var i = 0; i < argumentLength; i++ ){
		if( arguments[i] && !r.test(arguments[i].val()) && arguments[i].val() != 0 ){
			arguments[i].parent().addClass("ls-error");
			invalidCount++;
		}
	}
	if( invalidCount > 0 ){
		showCustomrizedMessage("数值只能为正整数或0");
		return false;
	}else{
		return true;
	}
}

function isNumber(obj){
	var reg = new RegExp("^[0-9]*$");
	var argumentLength = arguments.length;
	var invalidCount = 0;
	
	for( var i = 0; i < argumentLength; i++ ){
		if( arguments[i] && ( arguments[i].val() == '' || !reg.test(arguments[i].val()) ) ){
			arguments[i].parent().addClass("ls-error");
			invalidCount++;
		}
	}
	if( invalidCount > 0 ){
		return false;
	}else{
		return true;
	}
}

function showCustomrizedMessage(message){
	$('html').simpledialog2({
        mode: 'blank',
        headerText: '提示',
        headerClose: true,
        blankContent : 
          "<label style='text-align:center;display:block'>" + message + "</label>" + "<a rel='close' data-role='button' href='#'>关闭</a>"
      });
}

function checkMessage(message){
	if( message != '' ){
		showCustomrizedMessage(message);
	}
}

function checkUploadMessage(messageareaid){
	if( messageareaid != null && messageareaid != '' ){
		$("#"+messageareaid).css("display","block");
	}
}

function webLoginFormCheck(){
	if( $("#web_login_userName") && $("#web_login_userName").val() == '' ){
		alert('用户名不能为空');
		return;
	}
	if( $("#web_login_password") && $("#web_login_password").val() == '' ){
		alert('密码不能为空');
		return;
	}
	$("#webLoginForm").submit();
}

function showweeklypedreport(basePath,reportDate,userTel,userLevel){
    var reportFile = basePath+"weeklyReport/weeklyPEDReport-"+userLevel+"-"+userTel+"-"+reportDate+".html";
    $('html').simpledialog2({
        mode: 'blank',
        headerText: '提示',
        headerClose: true,
        blankContent : 
          "<label style='text-align:center;display:block'>" + message + "</label>" + "<a rel='close' data-role='button' href='#'>关闭</a>"
      });
}

function compareDate(d1, d2) {
	return Date.parse(d1.replace(/-/g, "/")) > Date.parse(d2.replace(/-/g, "/"));
} 

var eDropLangChange = function(){
	var selectedValue = $(this).val();

	$("#dsmSelect").children("span").each(function(){
		$(this).children().clone().replaceAll($(this));
	});

	if(selectedValue != ''){
		$("#dsmSelect").children("option[parentid!='" + selectedValue + "'][value!='']").each(function(){
			$(this).wrap("<span style='display:none'></span>");
		});
	}
};
var eDropFrameChange = function(){
	$("#rsmSelect").val($(this).children("option:selected").attr("parentid"));
};

var eDropLangBMChange = function(){
	var selectedValue = $(this).val();
	
	$("#rsmSelect").children("span").each(function(){
		$(this).children().clone().replaceAll($(this));
	});
	if(selectedValue != ''){
		$("#rsmSelect").children("option[parentid!='" + selectedValue + "'][value!='']").each(function(){
			$(this).wrap("<span style='display:none'></span>");
		});
	}
	
	$("#rsmSelect").selectmenu('enable');
	$("#rsmSelect").val('');
	$("#rsmSelect").selectmenu('refresh', true);
};
var eDropFrameBMChange = function(){
	//$("#rsdSelect").val($(this).children("option:selected").attr("parentid"));
};

var eWebRsdDropLangChange = function(){
	var selectedValue = $(this).val();

	$("#rsmSelect").children("span").each(function(){
		$(this).children().clone().replaceAll($(this));
	});

	if(selectedValue != ''){
		$("#rsmSelect").children("option[parentid!='" + selectedValue + "'][value!='']").each(function(){
			$(this).wrap("<span style='display:none'></span>");
		});
	}
	
	$("#dsmSelect").children("option[parentid!='" + selectedValue + "'][value!='']").each(function(){
		$(this).wrap("<span style='display:none'></span>");
	});
};
var eWebRsmDropFrameChange = function(){
	var selectedValue = $(this).children("option:selected").val();
	$("#dsmSelect").children("span").each(function(){
		$(this).children().clone().replaceAll($(this));
	});
	
	if(selectedValue != ''){
		$("#dsmSelect").children("option[parentid!='" + selectedValue + "'][value!='']").each(function(){
			$(this).wrap("<span style='display:none'></span>");
		});
	}
	
	$("#rsdSelect").val($(this).children("option:selected").attr("parentid"));
};
var eWebDsmDropFrameChange = function(){
	$("#rsmSelect").val($(this).children("option:selected").attr("parentid"));
};

function dyniframesize(iframeID){
	var ifm= document.getElementById(iframeID);
	var subWeb = document.frames ? document.frames[iframeID].document : ifm.contentDocument;
	if(ifm != null && subWeb != null) {
	    ifm.height = subWeb.body.scrollHeight; 
	}
}

function showDownloadFile(divName,fileSelection){
	var fileName = $('#'+fileSelection).find("option:selected").text();
	var filePath = $('#'+fileSelection).val();
	if( filePath != '' ){
		$('#'+divName).html('<a href="'+filePath+'" target="_blank">'+fileName+'</a>');
	}else{
		$('#'+divName).html('');
	}
}

function hasZeroValue(){
	var argumentLength = arguments.length;
	var zeroCount = 0;
	
	for( var i = 0; i < argumentLength; i++ ){
		if( arguments[i] && arguments[i].val() == 0 ){
			zeroCount++;
		}
	}
	if( zeroCount > 0 ){
		return true;
	}else{
		return false;
	}
}

function confirmTypein(title, popupDivID, formID) {
    if (title == null || title == "") title = "系统信息";
    $("#"+popupDivID+" h1").html(title);
    $("#"+popupDivID+" p").html("是否录入为0的数据?");
    $("#"+popupDivID+" #popup_submit").unbind("click").click(function() { 
        $("#"+popupDivID).popup("close");
        
        $.mobile.showPageLoadingMsg('b','数据提交中',false);
		if( $('.submit_btn') ){
			$('.submit_btn').removeAttr("onclick");
		}
		$("#"+formID).submit();
    });
    $("#"+popupDivID+" #popup_cancel").unbind("click").click(function() { 
    	$("#"+popupDivID).popup("close");
    });
    $("#"+popupDivID).popup("open");
}

function checkWHBW(){
	var checkStatus = $("#isWHBW").is(':checked');
	if( checkStatus ){
		showCustomrizedMessage('请注意：如果开启博利康尼数据录入并保存，则不能再取消');
		$("#whbw_div").css("display","block");
	}else{
		$("#whbw_div").css("display","none");
	}
}
<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<!DOCTYPE HTML>
<html lang="en-US">
<%@include file="header_web.jsp"%>
<script type="text/javascript">
	function downloadDailyData(){
		
		if( $("#datepicker") && $("#datepicker").val() == '' || 
				$("#datepicker_end") && $("#datepicker_end").val() == '' ){
			alert('请选择起止日期');
			return false;
		}
		
		if( compareDate($("#datepicker").val(),$("#datepicker_end").val()) ){
			alert('开始日期不能大于截止日期');
			return false;
		}
		
		loading();
		$("#downloadDailyData").submit();
	}
	function downloadHomeData(){
		
		if( $("#home_datepicker") && $("#home_datepicker").val() == '' || 
				$("#home_datepicker_end") && $("#home_datepicker_end").val() == '' ){
			alert('请选择起止日期');
			return false;
		}
		
		if( compareDate($("#home_datepicker").val(),$("#home_datepicker_end").val()) ){
			alert('开始日期不能大于截止日期');
			return false;
		}
		
		loading();
		$("#downloadHomeData").submit();
	}
	function downloadMonthlyData(){
		
		if( ( $("#datepicker_monthly") && $("#datepicker_monthly").val() == '' ) || 
				( $("#datepicker_monthly_end") && $("#datepicker_monthly_end").val() == '' ) ){
			alert('请选择日期');
			return false;
		}
		if( compareDate($("#datepicker_monthly").val(),$("#datepicker_monthly_end").val()) ){
			alert('开始日期不能大于截止日期');
			return false;
		}
		
		loading();
		$("#downloadMonthlyData").submit();
	}
	function downloadMonthlyInRateData(){
		
		if( $("#datepicker_monthlyInRate") && $("#datepicker_monthlyInRate").val() == '' ){
			alert('请选择日期');
			return false;
		}
		loading();
		$("#downloadMonthlyInRateData").submit();
	}
	function downloadMonthlyCollectionData(){
		
		if( $("#datepicker_monthlyCollection") && $("#datepicker_monthlyCollection").val() == '' ){
			alert('请选择日期');
			return false;
		}
		loading();
		$("#downloadMonthlyCollectionData").submit();
	}
	function downloadAllDSMData(){
		
		if( ( $("#datepicker_allDSM") && $("#datepicker_allDSM").val() == '' ) ){
			alert('请选择日期');
			return false;
		}
		loading();
		$("#downloadDailyDSMReport").submit();
	}
	function downloadAllRSMData(){
		
		if( ( $("#datepicker_allRSM") && $("#datepicker_allRSM").val() == '' ) ){
			alert('请选择日期');
			return false;
		}
		loading();
 		$("#downloadDailyRSMReport").submit();
	}
	function downloadWeeklyData(eventtype){
		if( 'download' == eventtype && $("#datepicker_weekly") && $("#datepicker_weekly").val() == '' ){
            alert('请选择日期');
            return false;
        }
		
		if( ( $("#rsdSelect") && $("#rsdSelect").val() == '' )
				&& ( $("#rsmSelect") && $("#rsmSelect").val() == '' ) 
				&& ( $("#dsmSelect") && $("#dsmSelect").val() == '' ) ){
 			alert('请至少选择一个RSD');
			return false;
		}
		loading();
		$("#eventtype").val(eventtype);
 		$("#downloadWeeklyData").submit();
	}
    function refreshPDFWeeklyReport(){
        
        if( ( $("#refreshDate") && $("#refreshDate").val() == '' ) ){
            alert('请选择日期');
            return false;
        }
        loading();
        $("#refreshWeeklyPDFReport").submit();
    }
	$(function(){
	    $("#rsdSelect").unbind("change", eWebRsdDropLangChange).bind("change", eWebRsdDropLangChange);
	    $("#rsmSelect").unbind("change", eWebRsmDropFrameChange).bind("change", eWebRsmDropFrameChange);
		$("#dsmSelect").unbind("change", eWebDsmDropFrameChange).bind("change", eWebDsmDropFrameChange);
	});
</script>
<body onload="checkUploadMessage('${messageareaid}');">
    <div id="upload_loading" class="loading_div" style="display: none;"></div>
	<div id="home">
		<div class="logo_header">
            <img src="<%=basePath%>images/web_logo.png" />
            <label style="float:right;font-weight:bold;background:url(<%=basePath%>images/header_timer.png) no-repeat;padding:4px;position: relative;top:20%;margin-right:10px;">
                <strong id="currentDate"></strong>
            </label>
        </div>
		<div class="downloaddata_input_file">
			<div class="download_title">数据下载</div>
			<c:if test="${operatorObj!=null && operatorObj.level!='REP'}">
				<div class="element_block">
					<div class="element_title">原始数据查询</div>
					<form action="doDownloadDailyData" id="downloadDailyData" method="post" enctype="multipart/form-data" data-ajax="false" accept-charset="UTF-8">
					<input type="hidden" name="fromWeb" value="Y">
					<div data-role="fieldcontain">
							选择日期：<input id="datepicker" type="text" name="chooseDate" class="ls_datepicker" readonly="readonly"/> - <input id="datepicker_end" type="text" name="chooseDate_end" class="ls_datepicker" readonly="readonly"/>
							选择科室：<select name="department">
										<option value="1">呼吸科</option>
										<option value="2">儿科</option>
								</select> 
							<br/>
							<img alt="" src="<%=basePath%>images/button_submit.png" style="cursor: pointer; vertical-align: middle;" onclick="downloadDailyData()" />
						<c:if test="${dataFile != null}">
							<div id="dailyDataFile">
								<a href="<%=basePath%>${dataFile}">${fn:substringAfter(dataFile,'/')}</a>
							</div>
						</c:if>
					</div>
					</form>
				</div>
				<div class="element_block">
					<div class="element_title">家庭雾化数据查询</div>
					<form action="doDownloadHomeData" id="downloadHomeData" method="post" enctype="multipart/form-data" data-ajax="false" accept-charset="UTF-8">
					<input type="hidden" name="fromWeb" value="Y">
					<div data-role="fieldcontain">
							选择日期：<input id="home_datepicker" type="text" name="chooseDate" class="ls_datepicker" readonly="readonly"/> - <input id="home_datepicker_end" type="text" name="chooseDate_end" class="ls_datepicker" readonly="readonly"/>
							<img alt="" src="<%=basePath%>images/button_submit.png" style="cursor: pointer; vertical-align: middle;" onclick="downloadHomeData()" />
						<c:if test="${homeDataFile != null}">
							<div id="homeDataFile">
								<a href="<%=basePath%>${homeDataFile}">${fn:substringAfter(homeDataFile,'/')}</a>
							</div>
						</c:if>
					</div>
					</form>
				</div>
				<div class="element_block">
					<div class="element_title">全国DSM日报查询</div>
					<div>
						<form action="doDownloadDailyDSMReport" id="downloadDailyDSMReport" method="post" enctype="multipart/form-data" data-ajax="false" accept-charset="UTF-8">
							<input type="hidden" name="fromWeb" value="Y">
							选择日期：<input id="datepicker_allDSM" type="text" name="chooseDate" class="ls_dailyReportDatepicker" readonly="readonly"/>
							选择科室：<select name="department">
										<option value="1">呼吸科</option>
										<option value="2">儿科</option>
								</select> 
							<br/>
							<img alt="" src="<%=basePath%>images/button_submit.png" style="cursor: pointer; vertical-align: middle;" onclick="downloadAllDSMData()" />
						</form>
						<c:if test="${dsmDataFile != null && dsmDataFile != ''}">
							<div id="dsmDataFile">
								<a href="${dsmDataFile}">${dsmFileName}</a>
							</div>
						</c:if>
					</div>
				</div>
				<div class="element_block">
					<div class="element_title">全国RSM日报查询</div>
					<div>
						<form action="doDownloadDailyRSMReport" id="downloadDailyRSMReport" method="post" enctype="multipart/form-data" data-ajax="false" accept-charset="UTF-8">
							<input type="hidden" name="fromWeb" value="Y">
							选择日期：<input id="datepicker_allRSM" type="text" name="chooseDate" class="ls_dailyReportDatepicker" readonly="readonly"/>
							选择科室：<select name="department">
										<option value="1">呼吸科</option>
										<option value="2">儿科</option>
								</select> 
							<br/>
							<img alt="" src="<%=basePath%>images/button_submit.png" style="cursor: pointer; vertical-align: middle;" onclick="downloadAllRSMData()" />
						</form>
						<c:if test="${rsmDataFile != null && rsmDataFile != ''}">
							<div id="rsmDataFile">
								<a href="${rsmDataFile}">${rsmFileName}</a>
							</div>
						</c:if>
					</div>
				</div>
				<div class="element_block">
					<div class="element_title">每月袋数数据查询</div>
					<div>
						<form action="doDownloadMonthlyData" id="downloadMonthlyData" method="post" enctype="multipart/form-data" data-ajax="false" accept-charset="UTF-8">
						<input type="hidden" name="fromWeb" value="Y">
							选择日期：<input id="datepicker_monthly" type="text" name="chooseDate_monthly" class="ls_datepicker" readonly="readonly"/> - <input id="datepicker_monthly_end" type="text" name="chooseDate_monthly_end" class="ls_datepicker" readonly="readonly"/>
							<br/>
							<img alt="" src="<%=basePath%>images/button_submit.png" style="cursor: pointer; vertical-align: middle;" onclick="downloadMonthlyData()" />
						</form>
						<c:if test="${monthlyDataFile != null}">
							<div id="monthlyDataFile">
								<a href="<%=basePath%>${monthlyDataFile}">${fn:substringAfter(monthlyDataFile,'/')}</a>
							</div>
						</c:if>
					</div>
				</div>
				<div class="element_block">
					<div class="element_title">周报数据查询</div>
					<div>
						<form action="doDownloadWeeklyData" id="downloadWeeklyData" method="post" enctype="multipart/form-data" data-ajax="false" accept-charset="UTF-8">
							<input type="hidden" name="fromWeb" value="Y">
							<input type="hidden" name="eventtype" id="eventtype" value="">
							<input type="hidden" name="chooseDate_weekly_h" value="${chooseDate_weekly}">
							<span>选择日期：</span>
							<input id="datepicker_weekly" type="text" name="chooseDate_weekly" class="ls_datepicker" readonly="readonly"/>
							<br/>
							<span>RSD/全国：</span>
								<select name="rsdSelect" id="rsdSelect">
				          			<option value="" selected="selected" >---请选择---</option>
					          		<c:forEach var="data" items="${rsdlist}">
					          			<option value="${data.regionCenter}" <c:if test="${selectedRSD==data.regionCenter}">selected="selected"</c:if> >${data.regionCenter}</option>  
					          		</c:forEach>
					          		<option value="0" <c:if test="${selectedRSD=='0'}">selected="selected"</c:if>>全国</option>
					          	</select>
							<span>RSM：</span>
								<select name="rsmSelect" id="rsmSelect">
				          			<option value="" selected="selected" >---请选择---</option>
					          		<c:forEach var="data" items="${rsmlist}">
					          			<option value="${data.region}" parentid="${data.regionCenter}" <c:if test="${selectedRSM==data.region}">selected="selected"</c:if>>${data.region}</option>  
					          		</c:forEach>
					          	</select>
							<span>DSM：</span>
								<select name="dsmSelect" id="dsmSelect">
				          			<option value="" selected="selected" >---请选择---</option>
					          		<c:forEach var="data" items="${dsmlist}">
					          			<option value="${data.telephone}" parentid="${data.region}" <c:if test="${selectedDSM==data.telephone}">selected="selected"</c:if>>${data.name}</option>
					          		</c:forEach>  
					          	</select>
					        <br/>
					        <span>选择科室：</span>
					            <select name="department">
										<option value="1" <c:if test="${department=='1'}">selected="selected"</c:if>>呼吸科</option>
										<option value="2" <c:if test="${department=='2'}">selected="selected"</c:if>>儿科</option>
								</select>
						    <br/>
							<img alt="" src="<%=basePath%>images/button_submit.png" style="cursor: pointer; vertical-align: middle;" onclick="downloadWeeklyData('download')" />
						<c:if test="${reportFiles != null}">
							<div id="weeklyPDFReport">
								<c:forEach items="${reportFiles}" var="reportFile">
									<a href="${reportFile.filePath}" target="_blank">${reportFile.fileName}</a>
								</c:forEach>
							</div>
						</c:if>
						<br/>
					    <span>发送邮箱：</span>
					    <input name="emailto" type="input" style="width:260px;"><span class="report_process_bg_description">(若不填写，则默认发送到当前用户邮箱)</span>
						<img alt="" src="<%=basePath%>images/button_sendEmail.png" style="cursor: pointer; vertical-align: middle;" onclick="downloadWeeklyData('email')" />
						<c:if test="${weeklyReportMessage != null && weeklyReportMessage != ''}">
							<div><span class="upload_failure_title">${weeklyReportMessage}</span></div>
						</c:if>
						</form>
					</div>
				</div>
				<c:if test="${operator!=null && operator=='18501622299'}">
					<div class="element_block">
					     <div class="element_title">PDF周报刷新</div>
		                 <div>
		                    <form action="refreshWeeklyPDFReport" id="refreshWeeklyPDFReport" method="post" enctype="multipart/form-data" data-ajax="false" accept-charset="UTF-8">
		                                                                                    选择日期：<input id="refreshDate" type="text" name="refreshDate" class="ls_dailyReportDatepicker" readonly="readonly"/>
		                        <br/>
		                        <img alt="" src="<%=basePath%>images/button_submit.png" style="cursor: pointer; vertical-align: middle;" onclick="refreshPDFWeeklyReport()" />
		                    </form>
		                    <c:if test="${weeklyPDFRefreshMessage != null && weeklyPDFRefreshMessage != ''}">
		                        <div><span class="upload_success_title">${weeklyPDFRefreshMessage}</span></div>
		                    </c:if>
		                </div>
					</div>
				</c:if>
				<div class="element_block">
					<div class="element_title">每月上报率统计</div>
					<div>
						<form action="doDownloadMonthlyInRateData" id="downloadMonthlyInRateData" method="post" enctype="multipart/form-data" data-ajax="false" accept-charset="UTF-8">
							<input type="hidden" name="fromWeb" value="Y">
							选择日期：<input id="datepicker_monthlyInRate" type="text" name="chooseDate_monthlyInRate" class="ls_datepicker" readonly="readonly"/>
							<br/>
	                        <span>选择级别：</span>
	                        <select name="level">
	                        	<option value="RSM">RSM</option>
	                        	<option value="RSD">RSD</option>
	                        </select>
	                        <br/>
							<img alt="" src="<%=basePath%>images/button_submit.png" style="cursor: pointer; vertical-align: middle;" onclick="downloadMonthlyInRateData()" />
						</form>
						<c:if test="${monthlyInRateDataFile != null}">
							<div id="monthlyInRateDataFile">
								<a href="${monthlyInRateDataFile}">${monthlyInRateDataFileName}</a>
							</div>
						</c:if>
					</div>
				</div>
				<div class="element_block">
					<div class="element_title">每月袋数采集统计表</div>
					<div>
						<form action="doDownloadMonthlyCollectionData" id="downloadMonthlyCollectionData" method="post" enctype="multipart/form-data" data-ajax="false" accept-charset="UTF-8">
							<input type="hidden" name="fromWeb" value="Y">
							选择日期：<input id="datepicker_monthlyCollection" type="text" name="chooseDate_monthlyCollection" class="ls_datepicker" readonly="readonly"/>
							<br/>
							<img alt="" src="<%=basePath%>images/button_submit.png" style="cursor: pointer; vertical-align: middle;" onclick="downloadMonthlyCollectionData()" />
						</form>
						<c:if test="${monthlyCollectionDataFile != null}">
							<div id="monthlyCollectionDataFile">
								<a href="${monthlyCollectionDataFile}">${monthlyCollectionDataFileName}</a>
							</div>
						</c:if>
					</div>
				</div>
			</c:if>
			<div class="element_block">
			 <div class="element_title">KPI资料下载</div>
			     <div>博雾资料</div>
			     <c:if test="${bowuList != null}">
                    <div>
                        <select id="bowu" onchange="showDownloadFile('bowufile',this.id)">
                            <option value="">--请选择下载资料--</option>
                        <c:forEach items="${bowuList}" var="fileObj">
                            <option value="${fileObj.filePath}">${fileObj.fileName}</option>
                        </c:forEach>
                        </select>
                        <span id="bowufile"></span>
                    </div>
                 </c:if>
			     <div>儿科资料</div>
			     <c:if test="${pedList != null}">
                    <div>
                        <select id="ped" onchange="showDownloadFile('pedfile',this.id)">
                            <option value="">--请选择下载资料--</option>
                        <c:forEach items="${pedList}" var="fileObj">
                            <a href="${fileObj.filePath}" target="_blank">${fileObj.fileName}</a>
                            <option value="${fileObj.filePath}">${fileObj.fileName}</option>
                        </c:forEach>
                        </select>
                        <span id="pedfile"></span>
                    </div>
                 </c:if>
			     <div>呼吸科资料</div>
			     <c:if test="${resList != null}">
                    <div>
                        <select id="res" onchange="showDownloadFile('resfile',this.id)">
                            <option value="">--请选择下载资料--</option>
                        <c:forEach items="${resList}" var="fileObj">
                            <a href="${fileObj.filePath}" target="_blank">${fileObj.fileName}</a>
                            <option value="${fileObj.filePath}">${fileObj.fileName}</option>
                        </c:forEach>
                        </select>
                        <span id="resfile"></span>
                    </div>
                 </c:if>
			     <div>外科资料</div>
			     <c:if test="${surgeryList != null}">
                    <div>
                        <select id="surgery" onchange="showDownloadFile('surgeryfile',this.id)">
                            <option value="">--请选择下载资料--</option>
                        <c:forEach items="${surgeryList}" var="fileObj">
                            <a href="${fileObj.filePath}" target="_blank">${fileObj.fileName}</a>
                            <option value="${fileObj.filePath}">${fileObj.fileName}</option>
                        </c:forEach>
                        </select>
                        <span id="surgeryfile"></span>
                    </div>
                 </c:if>
			     
			</div>
		</div>
		<div style="background:#1C7DBE;">
            <div class="index_footer">
                <div style="width:50%;float:left;background:#1C7DBE;height:40px;">
	                <img alt="" src="<%=basePath%>images/footer_back.png" style="cursor: pointer;" onclick="javascript:window.location.href='<%=basePath%>dataQuery'"/>
                </div>
	           <div style="width:50%;float:right;text-align: right;background:#1C7DBE">
	               <img alt="" src="<%=basePath%>images/footer_logo.png"/>
	           </div>
            </div>
        </div>
	</div>
</body>
</html>

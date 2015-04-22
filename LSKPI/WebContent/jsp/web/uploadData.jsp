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
	function downloadWeeklyHomeData(){
       if( $("#weeklyhome_datepicker") && $("#weeklyhome_datepicker").val() == '' ){
           alert('请选择日期');
           return false;
       }
       
       loading();
       $("#downloadWeeklyHomeData").submit();
    }
	function downloadDoctorData(){
        loading();
        $("#downloadDoctorData").submit();
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
    function refreshWeeklyHospitalData(){
        
        if( ( $("#hosData_refreshDate") && $("#hosData_refreshDate").val() == '' ) ){
            alert('请选择日期');
            return false;
        }
        loading();
        $("#refreshWeeklyHospitalData").submit();
    }
	function downloadResMonthData(){
		loading();
		$("#downloadResMonthData").submit();
	}
	function downloadResRe2MonthData(){
		loading();
		$("#downloadResRe2MonthData").submit();
	}
   	function downloadKPIHosData(){
        loading();
        $("#downloadKPIHosData").submit();
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
					<div class="element_title">医院KPI医院列表下载</div>
					<div>
						<form action="doDownloadKPIHosData" id="downloadKPIHosData" method="post" enctype="multipart/form-data" data-ajax="false" accept-charset="UTF-8">
							<input type="hidden" name="fromWeb" value="Y">
							选择科室：<select name="department">
										<option value="2">儿科</option>
										<option value="1">呼吸科</option>
										<option value="3">胸外科</option>
										<option value="4">每月袋数</option>
								</select> 
							<img alt="" src="<%=basePath%>images/button_submit.png" style="cursor: pointer; vertical-align: middle;" onclick="downloadKPIHosData()" />
						</form>
						<c:if test="${kpiHosFile != null}">
							<div id="kpiHosFile">
								<a href="<%=basePath%>${kpiHosFile}">${fn:substringAfter(kpiHosFile,'/')}</a>
							</div>
						</c:if>
					</div>
				</div>
				<div class="element_block">
                    <div class="element_title">家庭雾化医生查询</div>
                    <div>
                        <form action="doDownloadDoctorData" id="downloadDoctorData" method="post" enctype="multipart/form-data" data-ajax="false" accept-charset="UTF-8">
                         <input type="hidden" name="fromWeb" value="Y">
                         <img alt="" src="<%=basePath%>images/button_submit.png" style="cursor: pointer; vertical-align: middle;" onclick="downloadDoctorData()" />
                        </form>
                        <c:if test="${doctorDataFile != null}">
                            <div id="homeDataFile">
                                <a href="<%=basePath%>${doctorDataFile}">${fn:substringAfter(doctorDataFile,'/')}</a>
                            </div>
                        </c:if>
                    </div>
                </div>
				<div class="element_block">
					<div class="element_title">原始数据查询</div>
					<form action="doDownloadDailyData" id="downloadDailyData" method="post" enctype="multipart/form-data" data-ajax="false" accept-charset="UTF-8">
					<input type="hidden" name="fromWeb" value="Y">
					<div data-role="fieldcontain">
							选择日期：<input id="datepicker" type="text" name="chooseDate" class="ls_datepicker" readonly="readonly"/> - <input id="datepicker_end" type="text" name="chooseDate_end" class="ls_datepicker" readonly="readonly"/>
							选择科室：<select name="department">
										<option value="2">儿科</option>
										<option value="1">呼吸科</option>
										<option value="3">胸外科</option>
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
                <div class="element_title">家庭雾化周报查询</div>
	                <div>
	                    <form action="doDownloadWeeklyHomeData" id="downloadWeeklyHomeData" method="post" enctype="multipart/form-data" data-ajax="false" accept-charset="UTF-8">
	                   <input type="hidden" name="fromWeb" value="Y">                                            
	                                                                选择日期：<input id="weeklyhome_datepicker" type="text" name="chooseDate" class="ls_datepicker" readonly="readonly"/>
	                     <img alt="" src="<%=basePath%>images/button_submit.png" style="cursor: pointer; vertical-align: middle;" onclick="downloadWeeklyHomeData()" />
	                    </form>
	                    <c:if test="${weeklyHomeDataFile != null}">
	                        <div id="homeDataFile">
	                            <a href="<%=basePath%>${weeklyHomeDataFile}">${fn:substringAfter(weeklyHomeDataFile,'/')}</a>
	                        </div>
	                    </c:if>
	                </div>
	            </div>
				<div class="element_block">
					<div class="element_title">全国DSM日报查询</div>
					<div>
						<form action="doDownloadDailyDSMReport" id="downloadDailyDSMReport" method="post" enctype="multipart/form-data" data-ajax="false" accept-charset="UTF-8">
							<input type="hidden" name="fromWeb" value="Y">
							选择日期：<input id="datepicker_allDSM" type="text" name="chooseDate" class="ls_dailyReportDatepicker" readonly="readonly"/>
							选择科室：<select name="department">
										<option value="2">儿科</option>
										<option value="1">呼吸科</option>
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
										<option value="2">儿科</option>
										<option value="1">呼吸科</option>
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
										<option value="2" <c:if test="${department=='2'}">selected="selected"</c:if>>儿科</option>
										<option value="1" <c:if test="${department=='1'}">selected="selected"</c:if>>呼吸科</option>
										<option value="3" <c:if test="${department=='3'}">selected="selected"</c:if>>胸外科</option>
										<option value="4" <c:if test="${department=='4'}">selected="selected"</c:if>>家庭雾化</option>
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
					<div class="element_block">
					     <div class="element_title">医院周报中间表数据刷新</div>
		                 <div>
		                    <form action="refreshWeeklyHospitalData" id="refreshWeeklyHospitalData" method="post" enctype="multipart/form-data" data-ajax="false" accept-charset="UTF-8">
		                                                                                    选择日期：<input id="hosData_refreshDate" type="text" name="refreshDate" class="ls_dailyReportDatepicker" readonly="readonly"/>
		                        <br/>
		                        <img alt="" src="<%=basePath%>images/button_submit.png" style="cursor: pointer; vertical-align: middle;" onclick="refreshWeeklyHospitalData()" />
		                    </form>
		                    <c:if test="${weeklyHosRefreshMessage != null && weeklyHosRefreshMessage != ''}">
		                        <div><span class="upload_success_title">${weeklyHosRefreshMessage}</span></div>
		                    </c:if>
		                </div>
					</div>
				</c:if>
				<div class="element_block">
					<div class="element_title">每月汇总数据统计</div>
					<div>
					<form action="doDownloadMonthlyInRateData" id="downloadMonthlyInRateData" method="post" enctype="multipart/form-data" data-ajax="false" accept-charset="UTF-8">
						<input type="hidden" name="fromWeb" value="Y">
						选择日期：<input id="datepicker_monthlyInRate" type="text" name="chooseDate_monthlyInRate" class="ls_datepicker" readonly="readonly"/>
						<br/>
                        <span>选择级别：</span>
                        <select name="level">
                        	<option value="RSD">RSD</option>
                        	<option value="RSM">RSM</option>
                        	<option value="DSM">DSM</option>
                        </select>
                        <br/>
                        <span>选择科室：</span>
                        <select name="department">
                        	<option value="2">儿科</option>
							<option value="1">呼吸科</option>
							<option value="3">胸外科</option>
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
				<div class="element_block">
					<div class="element_title">呼吸科周周报</div>
					<div>
						<form action="doDownloadResMonthData" id="downloadResMonthData" method="post" enctype="multipart/form-data" data-ajax="false" accept-charset="UTF-8">
							<input type="hidden" name="fromWeb" value="Y">
							<input type="hidden" id="isRe2" name="isRe2" value="0"/>
							<span>选择级别：</span>
	                        <select name="level">
	                        	<option value="RSD">RSD</option>
	                        	<option value="RSM">RSM</option>
	                        	<option value="DSM">DSM</option>
	                        </select>
							<img alt="" src="<%=basePath%>images/button_submit.png" style="cursor: pointer; vertical-align: middle;" onclick="downloadResMonthData()" />
						</form>
						<c:if test="${resReportFiles != null}">
	                        <div id="weeklyPDFReport">
	                            <c:forEach items="${resReportFiles}" var="reportFile">
	                                <a href="${reportFile.filePath}" target="_blank">${reportFile.fileName}</a>
	                            </c:forEach>
	                        </div>
	                    </c:if>
					</div>
				</div>
				<div class="element_block">
					<div class="element_title">RE2医院呼吸科周周报</div>
					<div>
						<form action="doDownloadResMonthData" id="downloadResRe2MonthData" method="post" enctype="multipart/form-data" data-ajax="false" accept-charset="UTF-8">
							<input type="hidden" name="fromWeb" value="Y">
							<input type="hidden" id="isRe2" name="isRe2" value="1"/>
							<span>选择级别：</span>
	                        <select name="level">
	                        	<option value="RSD">RSD</option>
	                        	<option value="RSM">RSM</option>
	                        	<option value="DSM">DSM</option>
	                        </select>
                        
							<img alt="" src="<%=basePath%>images/button_submit.png" style="cursor: pointer; vertical-align: middle;" onclick="downloadResRe2MonthData()" />
						</form>
						<c:if test="${re2ReportFiles != null}">
	                        <div id="weeklyPDFReport">
	                            <c:forEach items="${re2ReportFiles}" var="reportFile">
	                                <a href="${reportFile.filePath}" target="_blank">${reportFile.fileName}</a>
	                            </c:forEach>
	                        </div>
	                    </c:if>
					</div>
				</div>
			</c:if>
			<div class="element_block">
			 <div class="element_title">KPI资料下载</div>
			     <c:if test="${allSourceFileList != null}">
			     <c:forEach items="${allSourceFileList}" var="sourceFile">
				     <div>${sourceFile[0].folderName_cn}</div>
	                    <div>
	                        <select id="${sourceFile[0].folderName_en}" onchange="showDownloadFile('${sourceFile[0].folderName_en}file',this.id)">
	                            <option value="">--请选择下载资料--</option>
	                        <c:forEach items="${sourceFile}" var="fileObj">
	                            <option value="${fileObj.filePath}">${fileObj.fileName}</option>
	                        </c:forEach>
	                        </select>
	                        <span id="${sourceFile[0].folderName_en}file"></span>
	                    </div>
			     </c:forEach>
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

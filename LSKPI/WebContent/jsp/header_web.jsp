<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<head>
	<title>Pulmicort KPI</title>
	
    <script type="text/javascript" src="<%=basePath%>js/jquery-1.9.1.min.js"></script>
	    
	<!-- export the jquery ui to use date picker -->
	<link rel="stylesheet" href="<%=basePath%>css/jquery-ui.css" type="text/css"/>
	<script type="text/javascript" src="<%=basePath%>js/jquery-ui-1.10.3.js"></script>
	
	<link rel="stylesheet" type="text/css" href="<%=basePath%>css/jquery.mobile.simpledialog.min.css" />
	<script type="text/javascript" src="<%=basePath%>js/jquery.mobile.simpledialog2.min.js"></script>
	
	<script type="text/javascript" src="<%=basePath%>js/jquery-validate.min.js"></script>
	
	<link rel="stylesheet" href="<%=basePath%>css/lskpi.css" type="text/css"/>
	<script type="text/javascript" src="<%=basePath%>js/lskpi.js?ver=1020"></script>
	
    <LINK href="<%=basePath%>css/user_Login.css" type=text/css rel=stylesheet>
	<script type="text/javascript">
	$(document).ready(function(){
		if( $(".ls_datepicker") ){
			$(".ls_datepicker").datepicker();
			$(".ls_datepicker").datepicker('option', {dateFormat: 'yy-m-d'});
		}
		
		if( $(".ls_dailyReportDatepicker") ){
			$(".ls_dailyReportDatepicker").datepicker();
			$(".ls_dailyReportDatepicker").datepicker('option', {dateFormat: 'mm-dd-yy'});
		}
	});
	
	function loading(){
        var second = 0;
        timer = setInterval(function(){
            if(--second < 1){
            	document.getElementById('upload_loading').style.display = '';
                clearInterval(timer);
            }
        },500);
        document.attachEvent ? document.attachEvent('onreadystatechange',CtrlLoad) : document.onreadystatechange = CtrlLoad;
        function CtrlLoad(){
            if(document.readyState && ('complete' == document.readyState)){
                document.getElementById('upload_loading').style.display = 'none';
                clearInterval(timer);
            }
        }
    }
	</script>
</head>
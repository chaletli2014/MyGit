<div data-role="footer" data-theme="a" data-position="fixed" style="background:#1C7DBE">
    <div class="ui-grid-a index_footer">
        <div class="ui-block-a">
        	<%if( null != request.getParameter("backURL") ){ %>
        	   <%if( null != request.getParameter("fromSalesQuery") && "Y".equalsIgnoreCase(request.getParameter("fromSalesQuery")) ){ %>
	        	<img alt="" src="<%=request.getParameter("basePath")%>images/footer_back.png" style="cursor: pointer;" onclick="javascript:window.location.href='<%=request.getParameter("basePath")+"doHospitalSalesQuery?orderParam="+request.getParameter("orderParam")+"&orderType="+request.getParameter("orderType")+"&order="+request.getParameter("order")+"&department="+request.getParameter("department")%>'"/>
        	   <%}else if( "doHospitalSearch".equalsIgnoreCase(request.getParameter("backURL")) ){ %>
	        	<img alt="" src="<%=request.getParameter("basePath")%>images/footer_back.png" style="cursor: pointer;" onclick="javascript:window.location.href='<%=request.getParameter("basePath")+request.getParameter("backURL")+"?isBack=Y&hospitalKeyword="+request.getParameter("hospitalKeyword")%>'"/>
        	   <%}else{ %>
	        	<img alt="" src="<%=request.getParameter("basePath")%>images/footer_back.png" style="cursor: pointer;" onclick="javascript:window.location.href='<%=request.getParameter("basePath")+request.getParameter("backURL")%>'"/>
        	   <%} %>
        	<%} %>
        	<%if( null != request.getParameter("currentPage") ){ %>
	        	<img alt="" src="<%=request.getParameter("basePath")%>images/footer_back.png" style="cursor: pointer;" onclick="closeCurWin()"/>
        	<%} %>
        </div>
        <div class="ui-block-b" style="text-align: right;">
            <img alt="" src="<%=request.getParameter("basePath")%>images/footer_logo.png"/>
        </div>
    </div>
</div>
<script>
function closeCurWin(){
	parent.window.close();
}
</script>
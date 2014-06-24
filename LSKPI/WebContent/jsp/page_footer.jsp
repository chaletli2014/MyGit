<div data-role="footer" data-theme="a" style="background:#1C7DBE">
    <div class="ui-grid-a index_footer">
        <div class="ui-block-a">
        	<%if( null != request.getParameter("backURL") ){ %>
        	   <%if( "doHospitalSearch".equalsIgnoreCase(request.getParameter("backURL")) ){ %>
	        	<img alt="" src="<%=request.getParameter("basePath")%>images/footer_back.png" style="cursor: pointer;" onclick="javascript:window.location.href='<%=request.getParameter("basePath")+request.getParameter("backURL")+"?isBack=Y&hospitalKeyword="+request.getParameter("hospitalKeyword")%>'"/>
        	   <%}else{ %>
	        	<img alt="" src="<%=request.getParameter("basePath")%>images/footer_back.png" style="cursor: pointer;" onclick="javascript:window.location.href='<%=request.getParameter("basePath")+request.getParameter("backURL")%>'"/>
        	   <%} %>
        	<%} %>
        	<%if( null != request.getParameter("currentPage") ){ %>
	        	<img alt="" src="<%=request.getParameter("basePath")%>images/footer_back.png" style="cursor: pointer;" onclick="javascript:window.location.href='http://www.whatisnew.com.cn/m/close.html'"/>
        	<%} %>
        </div>
        <div class="ui-block-b" style="text-align: right;">
            <img alt="" src="<%=request.getParameter("basePath")%>images/footer_logo.png"/>
        </div>
    </div>
</div>
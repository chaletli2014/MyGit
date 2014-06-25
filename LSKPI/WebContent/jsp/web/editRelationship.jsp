<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<div data-role="popup" id="popupEditRelationship" data-inline="true" data-position-to="window" data-theme="b" style="width:400px;max-width:100%;" class="ui-corner-all">
    <div data-role="header" data-theme="a" class="ui-corner-top">
          <h1></h1><a href="#" data-rel="back" data-role="button" data-theme="a" data-icon="delete" data-mini="true" data-iconpos="notext" class="ui-btn-right">Close</a>
    </div>
    <div data-role="content" data-theme="d" class="ui-corner-bottom ui-content">
        <div class="roundCorner" style="width:90%">
            <form id="doEditDoctorRelationshipForm" action="doEditDoctorRelationship" method="POST" data-ajax="false" style="padding:10px 20px;">
                <input type="hidden" name="dataId" id="edit_dataId" value=""/>
                <input type="hidden" name="edit_relatedSales" id="edit_relatedSales" value=""/>
                
                <label for="hospitalname" class="select">目标医院</label>
                <input name="hospitalname" id="hospitalname" readonly="readonly"/>
                <label for="doctorname" class="select">医生姓名</label>
                <input name="doctorname" id="doctorname" readonly="readonly"/>
                
                <label for="relatedSales" class="select">关联销售<span id="editdoctor_notification" class="error_message"></span></label>
                <select name="relatedSales" id="relatedSales">
                    <option value="">--请选择--</option>
                    <c:forEach var="sales" items="${salesList}">
                       <option value="${sales.userCode}">${sales.name}</option>
                    </c:forEach>
                </select>
                
				<div style="text-align: center;">
	                <a href="javascript:void(0)" id="edit_dr_submit" style="display: block;">
	                  <img alt="" src="<%=basePath%>images/button_submit_140.png" style="cursor: pointer;" />
	                </a>
	                <a href="javascript:void(0)" id="edit_dr_cancel" style="display: block;">
	                  <img alt="" src="<%=basePath%>images/button_cancel_140.png" style="cursor: pointer;" />
	                </a>
				</div>
            </form>
        </div>
    </div>
</div>
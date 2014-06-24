<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<!DOCTYPE HTML>
<%@include file="header_web.jsp"%>
<BODY id=userlogin_body>
	<DIV></DIV>
	<DIV id=user_login>
		<DL>
			<DD id=user_top>
				<UL>
					<LI class=user_top_l></LI>
					<LI class=user_top_c></LI>
					<LI class=user_top_r></LI>
				</UL>
			<DD id=user_main>
				<UL>
					<LI class=user_main_l></LI>
					<LI class=user_main_c>
					   <form action="doLogin" id="webLoginForm" method="post">
							<DIV class=user_main_box>
								<UL>
									<LI class=user_main_text>用户名：</LI>
									<LI class=user_main_input><INPUT class="TxtUserNameCssClass" id="web_login_userName" maxLength="20" name="web_login_userName"></LI>
								</UL>
								<UL>
									<LI class=user_main_text>密 码：</LI>
									<LI class=user_main_input><INPUT class="TxtPasswordCssClass" id="web_login_password" type="password" name="web_login_password"></LI>
								</UL>
							</DIV>
					    </form>
					    <c:if test="${web_login_message != ''}">
							<div style="margin-top:40px;color:red;" id="web_login_message">${web_login_message}</div>
					    </c:if>
					</LI>
					<LI class=user_main_r>
					   <INPUT class="IbtnEnterCssClass" id="IbtnEnter" style="BORDER-TOP-WIDTH: 0px; BORDER-LEFT-WIDTH: 0px; BORDER-BOTTOM-WIDTH: 0px; BORDER-RIGHT-WIDTH: 0px"
						onclick='webLoginFormCheck()' type="image" src="<%=basePath%>css/images/user_botton.gif" name="IbtnEnter">
					</LI>
				</UL>
		</DL>
	</DIV>
	</FORM>
</BODY>
</HTML>

<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"
	contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="/WEB-INF/tld/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/tld/struts-logic.tld" prefix="logic"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<!-- saved from url=(0219)http://login.sdo.com/sdo/Login/LoginFrameFC.php?target=top&appId=4012&pm=2&areaId=1&loginifrmId=iframeLogin&proxyUrl=&returnURL=http%3A%2F%2Fsw.sdo.com%2FLoginCheck.aspx&backUrl=http%3A%2F%2Fsw.sdo.com%2FLoginCheck.aspx -->
<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
			<meta name="description" content="GIS在线用户登录窗口">
				<meta name="keywords"
					content="GIS在线,GIS网络,通行证,密宝,SDO,SNDA,网络游戏,游戏充值">
					<title>CGISER通行证</title>
					<link href="./css/public.css" rel="stylesheet">
						<script src="http://code.jquery.com/jquery-latest.js">
</script>
						<script type="text/javascript">
// 测试引用是否成功
$(document).ready(function(e) {
	$('#serverSelect').change(function() {
		var serverId = $(this).children('option:selected').val()
	});
});
function selectServer() {
	$('#login1').animate( {
		left : "388"
	}, 1000);
}
</script>
						<script>
document.domain = "cgiser.com";
</script>
	</head>
	<body style="">
			<div id="login1" class="publicspr toplogin"
				style="zIndex: 1; left: 0px; position: absolute">
				<div id="jUserLoginedDiv" class="clearfix toplogined"
					style="display: none">
					<div id="jUserIcon" class="loginheader"></div>
					<div class="loginname">
						<h4 id="jUserName"></h4>
						<p id="jUserArea" class="publicspr login-fwq">
							-
						</p>
						<a class="login-out lnk-normal"
							href="javascript:LW201310_Userinfo.userLogout();">注销</a>
					</div>
					<ul class="loginnumber">
						<li>
							<em id="jUserGames" class="cgreen">-</em>元宝
						</li>
						<li>
							<em id="jUserRP">-</em>神龙券
						</li>
						<li>
							<em id="jUserIP">-</em>铜钱
						</li>
						<li>
							<em id="jUserRS">-</em>战斗力
						</li>
					</ul>
				</div>
			</div>
	</body>
</html>

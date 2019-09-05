<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
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
		<meta name="keywords" content="GIS在线,GIS网络,通行证,密宝,SDO,SNDA,网络游戏,游戏充值">
		<title>用户登录窗口 - GIS在线</title>
		<link href="./css/base_login.css" rel="stylesheet" type="text/css">
		<script>
document.domain = "cgiser.com";
</script>
	</head>
	<body>
		<div id="app_sdo_login" class="app_sdo_login sms">
			<h2 class="login_header">
				<span class="header_left"></span>
				<span class="header_bg"> <span class="header_title">GIS通行证</span>
					<span class="get_app_wrap"> <a
						href="http://txz.sdo.com/default.aspx?fromid=P201" target="_blank"
						class="get_app">GIS通行证手机版下载</a> </span> </span>
				<span class="header_right"></span>
			</h2>
			<div class="login_wrap">
				<div class="nav">
					<ul id="nav">
						<li class="back" style="left: 323px; width: 105px;"></li>
						<li class="btn_index first">
							<a href="javascript:void(0)" id="nav_btn_index" hidefocue="true"
								class="">密码登录</a>
						</li>
						<li class="btn_mobile">
							<a href="javascript:void(0)" id="nav_btn_mobile"
								title="手指一划，轻松登录" hidefocue="true" class="">一键登录</a>
						</li>
						<li class="btn_code2d">
							<a href="javascript:void(0)" id="nav_btn_code2d" hidefocue="true"
								class="">二维码</a>
						</li>
						<li class="btn_sms last cur">
							<a href="javascript:void(0)" id="nav_btn_sms" hidefocue="true"
								class="cur">短信登录</a>
						</li>
					</ul>
				</div>
				<div id="login_frame" class="login_frame">

					<form accept-charset="gb2312" target="_top"
						action="https://cas.sdo.com/dplogin" method="post" id="form"
						class="login_form" name="form">

						<!-- 提示 -->
						<span class="form_tips_outer"> <span id="form_tips"
							class="form_tips" style="">&nbsp;</span> </span>
						<!--/提示 -->
						<div class="login_table">
							<!-- 用户信息填写表单 -->
							<div id="tbody_user" class="tbody">
								<!-- 用户名 -->
								<div id="user_name" class="tr">
									<span class="td">
										<div id="user_name_input" class="cell_input with_icon">
											<span class="cell_input_notice">手机/邮箱/个性账号</span>
											<input type="text" id="username" name="username" tabindex="1"
												class="text username" maxlength="50" autocomplete="off">
											<a class="sms_btn sent_btn">获取验证码</a>

											<a href="javascript:void(0)" class="clear_btn"
												style="display: none;">清除</a>
										</div> </span>
								</div>
								<!--/用户名 -->
								<!-- 密码 -->
								<div id="user_pwd" class="tr">
									<span class="td">
										<div id="user_pwd_input" class="cell_input with_icon">
											<span class="cell_input_notice">静态密码/动态密码</span>
											<input type="password" tabindex="2" id="password"
												name="password" class="text password" maxlength="40"
												title="使用动态密码，登录更安全！">
										</div> </span>
								</div>
								<!--/密码 -->
								<!-- 一键登录-状态 -->
								<div id="confirm_code" class="tr">
									<div class="status_suc">
										<span class="confirm_code_outer"> <label for="ptpwd"
												class="input_label" id="ptpwd_label">
												登录确认码：
											</label> <span>12345</span> </span>
									</div>
									<div class="status_notInstalled">
										<p>
											下载链接将以短信的方式发送至您的手机
										</p>
										<a href="http://txz.sdo.com/txz/pushlogindown?fromid=48"
											target="_blank">其他下载方式</a>
									</div>
									<div class="status_bindMobile">
										<div class="step step_1">
											<span class="count">第一步</span>
											<p>
												下载链接将以短信发送至手机
												<a href="http://txz.sdo.com/txz/pushlogindown?fromid=48"
													target="_blank">其他下载方式</a>
											</p>
										</div>
										<div class="step step_2">
											<span class="count">第二步</span>
											<p>
												绑定登录账号，请按
												<a
													href="http://txz.sdo.com/txz/pushlogindown?fromid=48&tag=1"
													target="_blank">引导操作</a>
											</p>
										</div>
									</div>
								</div>
								<!--/一键登录-状态 -->
								<!-- 短信登录-验证码 -->
								<div id="code" class="tr">
									<span class="td">
										<div class="code cell_input with_icon">
											<span class="cell_input_notice">短信验证码</span>
											<input name="checkCode" type="text" id="checkCode"
												class="text checkCode" maxlength="6">
											<a class="sms_btn">获取验证码</a>
										</div> </span>
								</div>
								<!--/短信登录-验证码 -->
								<!-- 自动登录checkbox -->
								<div id="ope" class="ope tr">
									<span class="td"> </span>
								</div>
								<!--/自动登录checkbox -->
							</div>
							<!--/用户名 -->
							<!-- 二维码 -->
							<div id="tbody_code2" class="tbody">
								<div id="code2" class="code2 tr">
									<div class="code2d_notice">
										拍摄二维码，稍等片刻即可登录
										<a href="javascript:void(0)" class="setup_code2d">安装</a>
										<div class="code2d_tip" style="display: none;">
											<div class="code2d_tip_bg"></div>
											<a href="http://txz.sdo.com/txz/?from=login" target="_blank"
												class="code2d_link">下载GIS通行证</a>
										</div>
									</div>
									<img src="qrcode" width="98" height="98">
								</div>
							</div>
							<!--/二维码 -->
							<!-- 短信登录-设置密码 -->
							<div id="tbody_setpass" class="tbody">
								<div id="setpass_user_name" class="tr">
									<span class="td col_1"> <label class="input_label">
											GIS通行证
										</label> </span>
									<span class="td setpass_account">13487665522</span>
								</div>
								<div id="setpass_set_pass" class="tr">
									<span class="td">
										<div class="code cell_input">
											<span class="cell_input_notice">设置密码</span>
											<input name="" type="password" id="" class="text">
										</div> </span>
								</div>
								<div id="setpass_confirm_pass" class="tr">
									<span class="td">
										<div class="code cell_input">
											<span class="cell_input_notice">确认密码</span>
											<input name="" type="password" id="" class="text">
										</div> </span>
								</div>
							</div>
							<!--/短信登录-设置密码 -->
							<!-- 图片验证码 -->
							<div id="tbody_checkcode" class="tbody">
								<div id="checkcode_input" class="tr">
									<span class="td">
										<div id="code_input" class="cell_input">
											<span class="cell_input_notice">输入验证码</span>
											<input type="text" id="img_password" maxlength="6"
												class="text">
										</div> </span>
								</div>
								<div id="checkcode_img" class="tr">
									<span class="td"> <span class="input_label">验证码</span> </span>
									<span class="td checkcode_img"> <img src="code.png"
											id="captchaImg"> <a class="checkcode_change">看不清</a> </span>
								</div>
							</div>
							<!--/图片验证码 -->
							<!-- A8密保 -->
							<div id="tbody_safe_code_a8" class="tbody">
								<div id="safe_code_a8_input" class="tr">
									<span class="td">
										<p class="safeCodeTips">
											请将您的挑战码
											<span class="tzcode">3456</span> 输入密宝，然后依次填写密宝输出的密码
										</p>
										<div class="cell_input">
											<input type="text" name="ptekey" id="ptekey" maxlength="8"
												class="text">
										</div> </span>
								</div>
							</div>
							<!--/A8密保 -->
							<!-- A6密保 -->
							<div id="tbody_safe_code_a6" class="tbody">
								<div id="safe_code_a6_input" class="tr">
									<span class="td">
										<p class="safeCodeTips">
											请依次输入密宝密码的对应位置上的数字
										</p>
										<div class="safeCodeList">
											<ul>
												<li class="cur">
													<label for="" class="safeCodeText">
														第2位
													</label>
													<div class="cell_input">
														<input type="text" name="codekey1" id="codekey1"
															maxlength="1" class="text">
													</div>
												</li>
												<li>
													<label for="" class="safeCodeText">
														第3位
													</label>
													<div class="cell_input">
														<input type="text" name="codekey2" id="codekey2"
															maxlength="1" class="text">
													</div>
												</li>
												<li>
													<label for="" class="safeCodeText">
														第4位
													</label>
													<div class="cell_input">
														<input type="text" name="codekey3" id="codekey3"
															maxlength="1" class="text">
													</div>
												</li>
												<li>
													<label for="" class="safeCodeText">
														第5位
													</label>
													<div class="cell_input">
														<input type="text" name="codekey4" id="codekey4"
															maxlength="1" class="text">
													</div>
												</li>
											</ul>
										</div> </span>
								</div>
							</div>
							<!--/A6密保 -->
							<!-- 安全卡 -->
							<div id="tbody_safe_code_card" class="tbody">
								<div id="safe_code_code_input" class="tr">
									<span class="td">
										<p class="safeCodeTips">
											请依次输入安全卡3个坐标上对应的数字
										</p>
										<div class="safeCodeList">
											<ul>
												<li class="cur">
													<label for="" class="safeCodeText">
														H6
													</label>
													<div class="cell_input">
														<input type="text" name="" id="" maxlength="2"
															class="text">
													</div>
												</li>
												<li>
													<label for="" class="safeCodeText">
														D5
													</label>
													<div class="cell_input">
														<input type="text" name="" id="" maxlength="2"
															class="text">
													</div>
												</li>
												<li>
													<label for="" class="safeCodeText">
														E4
													</label>
													<div class="cell_input">
														<input type="text" name="" id="" maxlength="2"
															class="text">
													</div>
												</li>
											</ul>
										</div>
										<p class="safeCodeTipsB">
											安全卡遗失，需要
											<a
												href="http://ecard.sdo.com/SiteEcard/Ecard/Main.aspx?NowClick=Main"
												target="_blank">帮助</a>？
										</p> </span>
								</div>
							</div>
							<!--/安全卡 -->
							<!-- 表单提交按钮 -->
							<div id="tbody_btn" class="tbody">
								<div id="login_btn" class="tr">
									<span class="td">
										<div class="login_btn">
											<a id="btn_user_login" class="btn_user_login"> <span
												class="btn_user_login_index">登 录</span> <span
												class="btn_user_login_mobile">手机一键登录</span> <span
												class="btn_user_login_setpass">确 定</span> </a>
										</div>
										<div class="cancel_btn">
											<a id=""
												href="http://login.sdo.com/sdo/Login/LoginFrameFC.php?target=top&appId=4012&pm=2&areaId=1&loginifrmId=iframeLogin&proxyUrl=&returnURL=http%3A%2F%2Fsw.sdo.com%2FLoginCheck.aspx&backUrl=http%3A%2F%2Fsw.sdo.com%2FLoginCheck.aspx#">取消</a>
										</div>
										<div class="back_btn">
											<a
												href="http://login.sdo.com/sdo/Login/LoginFrameFC.php?target=top&appId=4012&pm=2&areaId=1&loginifrmId=iframeLogin&proxyUrl=&returnURL=http%3A%2F%2Fsw.sdo.com%2FLoginCheck.aspx&backUrl=http%3A%2F%2Fsw.sdo.com%2FLoginCheck.aspx#">返回</a>
										</div>
										<div class="setpass_link">
											<a
												href="http://login.sdo.com/sdo/Login/LoginFrameFC.php?target=top&appId=4012&pm=2&areaId=1&loginifrmId=iframeLogin&proxyUrl=&returnURL=http%3A%2F%2Fsw.sdo.com%2FLoginCheck.aspx&backUrl=http%3A%2F%2Fsw.sdo.com%2FLoginCheck.aspx#"
												title="可使用短信验证码再次登录，或到SDO网站设置密码">直接进入应用</a>
										</div> </span>
								</div>
							</div>
							<!--/表单提交按钮 -->
						</div>
					</form>
				</div>
				<div class="reg_guide">
					<a href="http://register.sdo.com/?from=201" target="_blank"
						class="reg_btn">注册</a>
					<span class="line">|</span>
					<a href="http://pwd.sdo.com/ptinfo/safecenter/ScIndex/PwdFind.aspx"
						class="btn_forget_pwd" id="btn_forget_pwd" target="_blank">忘记密码</a>
					<span class="line">|</span>
					<a
						href="http://diaocha.sdo.com/vote/15/6555.aspx?ExtendInfo=web_4012"
						class="btn_to_feedback" id="btn_to_feedback" target="_blank">反馈</a>
				</div>
			</div>
			<div class="footer"></div>
		</div>
		<script src="jquery-1.8.2.js">
</script>
		<script>

$.CONFIGS = {
	Login : {
		_SSO : "https://cas.sdo.com/authen/{t}.jsonp",
		QRCode : {
			QRCodeImageUrl : "https://cas.sdo.com/cas/qrcode?appId=4012&areaId=1",
			reqQRCodePollingData : {
				appId : 4012,
				areaId : 1,
				code : 300,
				serviceUrl : 'http://sw.sdo.com/LoginCheck.aspx',
				productId : 2,
				productVersion : '3.1.0',
				authenSource : 2
			}
		}
	},

	App : {
		appId : 4012,
		areaId : 1,
		serviceUrl : "http://sw.sdo.com/LoginCheck.aspx",
		target : "top",
		isSupportAutoLogin : 0,
		autoLoginSaveTime : 14,
		isAutomaticallyLogged : 0,
		UIConfig : {
			CssUrl : '',
			LoginWays : '',
			HiddenTitle : 0,
			ADWords : ''
		}
	}
};

(function() {
	var App = $.CONFIGS.App;
	var L = $.CONFIGS.Login;
	var ADWords = App.UIConfig.ADWords;

	var methods = [
			[ "staticLogin", "inputUserId|password" ],
			[ "dynamicLogin", "guid|loginType|password" ],
			[ "checkCodeLogin", "guid|password" ],//图形验证码登录（包括换图片）
			[ "autoLogin", "" ],
			[ "sendPhoneCheckCode", "inputUserId|type" ],
			[ "phoneCheckCodeLogin", "checkCodeSessionKey|checkCode" ],//手机验证码登陆
			[ "getCodeKey", "" ], [ "codeKeyLogin", "codeKey|" ],
			[ "sendPushMessage", "inputUserId|" ],
			[ "pushMessageLogin", "pushMsgSessionKey|" ],
			[ "checkAccountType", "inputUserId|" ],
			[ "sendPassword", "authKey|" ],
			[ "modifyPassword", "authKey|newPwd" ],
			[ "getPushMessageStatus", "inputUserId|" ],
			[ "ssoLogin", "serviceUrl|" ],
			[ "fcmLogin", "realName|idCard|email" ] ];

	for ( var i = 0; i < methods.length; i++) {
		L[methods[i][0]] = {
			url : L._SSO.replace("{t}", methods[i][0]),
			requestdata : {}
		};
		var fields = methods[i][1].split("|");
		for ( var x = 0; x < fields.length; x++) {
			if (fields[x].length > 0) {
				L[methods[i][0]].requestdata[fields[x]] = '';
			}
		}
		L[methods[i][0]].requestdata["appId"] = App.appId;
		L[methods[i][0]].requestdata["areaId"] = App.areaId;
		L[methods[i][0]].requestdata["serviceUrl"] = App.serviceUrl;
		L[methods[i][0]].requestdata["productVersion"] = "3.1.0";
		L[methods[i][0]].requestdata["frameType"] = 3;
		L[methods[i][0]].requestdata["locale"] = "zh_CN";
		L[methods[i][0]].requestdata["version"] = 21;
		L[methods[i][0]].requestdata["tag"] = 20;
		L[methods[i][0]].requestdata["authenSource"] = 2;
		L[methods[i][0]].requestdata["productId"] = 2;

	}

})();
</script>
		<script src="./js/login.js">
</script>
		<script src="./js/sdo_beacon(1).js">
</script>
		<!--    <script src="../PRes/4in1/js/comment.js"></script>-->

	</body>
</html>

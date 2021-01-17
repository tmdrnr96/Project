<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
	<head>
		<meta charset="UTF-8">
		<title>Sign in to Personal Online Banking</title>
		
		<!-- bank_login_form.css 스타일 시트 참조  -->
		<link rel = "stylesheet" href="${pageContext.request.contextPath}/resources/css/bank_login_form.css">		
		<!-- favicon -->
		<link rel="shortcut icon" href="${pageContext.request.contextPath}/resources/img/bank_fav.png">
		
		<script src="${pageContext.request.contextPath}/resources/js/httpRequest.js"></script>
	
		<style type="text/css">		
		  body{background-image: url(resources/img/bank_building.jpg);
  		  background-size: cover; 
  		  }
  		  
		</style>
		
		
		<script type="text/javascript">
		
		function sign(f) {

			var id = f.id.value.trim();
			var pwd = f.pwd.value.trim();
			
			if(id == ""){
				alert("User ID를 입력해주세요.");
				return;
			}
			
			if(pwd == ""){
				alert("User Password를 입력해주세요.");
				return;
			}
			
			var url = "sign_up.do";
			var param = "id="+id+"&pwd="+encodeURIComponent(pwd);
			
			sendRequest(url, param, result,"post");				
		}
		
		function result() {
			if(xhr.readyState == 4 && xhr.status == 200){
				var data = xhr.responseText;
				var json = eval(data);
				
				if( json[0].param == 'no_id' ){
					alert("아이디가 존재하지 않습니다");
					
				}else if( json[0].param == 'no_pwd'){
					alert("비밀번호가 일치하지 않습니다");
					
				}else if( json[0].param == 'clear' ){
					alert("로그인 성공");
					location.href = "main.do";
				}
			}
		}
		
		//아이디 찾기
		function f_id() {
					//크롬에서는 window지원이 잘 안됨..
					//창크기 고정 필요..							
			var url = 'find_id_form.do';
			var status = "width=510, height=400, left=370, top=190, scrollbars=no, resizable=no";	
			window.open(url, "_blank", status);
		
		}	
		
		//비밀번호 찾기
		 function f_pwd() {
			 var url = 'find_pwd_form.do'; 
			window.open(url, "_blank","width=510, height=400, left=370, top=190, scrollbars=no, resizable=no");
		} 
		
		
		</script>
	</head>
	<body>
		<!-- 로그인, 로그 아웃 서브메뉴를 띄워줄 JSP  -->
		<div class="login" align="center">
			<jsp:include page="../bank_login/session/bank_login_logout.jsp"/>
		</div>
		<div class = "box">
			<h1>EzoBank 로그인</h1>
			<form>
				<div id = "table">
					<table align = "center">
						<tr>						
							<td><input type = "text" class = "log" name = "id" placeholder="User ID" ></td>
						</tr>
						<tr>						
							<td><input class = "log" type = "password" name = "pwd" placeholder="User Password"></td>
						</tr>
						
						<tr>
							<td align = "center">
								<input type = "button" class = "btn_log" value = "로그인" onclick = "sign(this.form);" >					
								<input type = "button" class = "btn_cancel" value = "닫기" onclick = "history.go(-1);" >
							</td>
						</tr>	
					</table>
				</div>					
			</form>
			<div id = "find" align = "center">
					   <a class = "f" href = "javascript:f_id();">아이디 찾기</a>  
					   <a class = "f" href = "javascript:f_pwd();">비밀번호 찾기</a>		
			</div>
			
			<div id = "join" align = "center">
				  <a class = "j" href = "join_form.do;"> EzoBank 회원 가입 </a>	
			</div>
		</div>
		
		<div id = "footer" align="center">
			<%@include file="../footer/bank_footer.jsp"%>
		</div>		
	</body>
</html>
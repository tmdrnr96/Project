<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
	<head>
		<meta charset="UTF-8">
		<title>Insert title here</title>
		<link rel="shortcut icon" href="${pageContext.request.contextPath}/resources/img/bank_fav.png">
		<script src="${pageContext.request.contextPath}/resources/js/httpRequest.js"></script>
		
		<style type="text/css">
		body{
		    background-color:#212121;
		    background-size: cover;
		}
		
		.box{
		  width: 500px;
		  position: absolute;
		  top: 50%;
		  left: 50%;
		  transform: translate(-50%,-50%);
		  background: #212121;
		  text-align: center;
		  color: white;
		}
		
		.box h1{
		 font-weight: 500;
		 text-shadow: 3px 3px 2px #000;
		}
		
		#pwd,#c_pwd{
		  border:0;
		  background: none;
		  display: inline;
		  margin: 5px auto;
		  text-align: center;
		  border: 2px solid #3498db;
		  padding: 14px 10px;
		  width: 240px;
		  outline: none;
		  color: white;
		  border-radius: 24px;
		  transition: 0.25s;
		}
		
		#pwd:focus,#c_pwd:focus{
		  width: 260px;
		  border-color: #2ecc71;
		}
		
		
		.box input[type = "button"]{
		  border:0;
		  background: none;
		  display: inline;
		  margin: 35px auto;
		  text-align: center;
		  border: 2px solid #2ecc71;
		  padding: 14px 40px;
		  outline: none;
		  color: white;
		  border-radius: 24px;
		  transition: 0.25s;
		  cursor: pointer;
		}
		
		.box input[type = "button"]:hover{
		  background: #2ecc71;
		}
		
		</style>
		
		<script type="text/javascript">		
		
		<!-- 비밀번호 찾기에 성공할 시에 비밀번호 변경 -->		
		//비밀번호 수정
		function change(f) {
						
			var ssn = f.ssn.value;
			var pwd = f.pwd.value.trim();
			var c_pwd=f.c_pwd.value.trim();
			
			//유효성체크
			if(pwd == "" || c_pwd == ""){
				alert("사용하실 Password를 입력해주세요.");
				return;
			}
			
			if(pwd != c_pwd){
				alert("비밀번호가 일치하지 않습니다.\n다시확인해주세요.");
				return;
			}
			
			var url = "change_password.do";
			var param = "ssn="+ssn+"&pwd="+encodeURIComponent(pwd);
			
			sendRequest(url, param, result, "post");			
		}
		
		function result() {
			
		if(xhr.readyState == 4 && xhr.status == 200){

			var data = xhr.responseText;
			
			if(data != "yes"){
				alert("Password 변경 실패..\n고객 센터로 연락바랍니다.");
			}else{
				alert("Password 변경 완료!\n");		
			}
			window.close();
			return;
			}
		}
				
		</script>
	</head>
	<body class = "box">
		<h2 align = "center" >비밀번호 변경 </h2>
		<form>
			<input type = "hidden" name = "ssn" value = "${param.ssn}">
			<table align = "center">
				<tr>
					<th>새로운 비밀번호 : </th>
					<td><input type = "password" id = "pwd" name = "pwd"></td>
				</tr>
				<tr>
					<th>비밀번호 확인 : </th>
					<td><input type = "password" id = "c_pwd" name = "c_pwd"></td>
				</tr>
				<tr>
					<td colspan = "2" align = "center">
						<input type= "button" class = "btn" value = "확인" onclick = "change(this.form);">
						<input type= "button" class = "btn" value = "취소" onclick = window.close();>
					</td>
				</tr>
			</table>
		</form>
	</body>
</html>
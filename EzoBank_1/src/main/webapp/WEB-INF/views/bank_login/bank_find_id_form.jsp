<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
	<head>
		<meta charset="UTF-8">
		<!--ID 찾기  -->
		<title>Find_User_ID</title>
		<link rel="shortcut icon" href="https://png.pngtree.com/png-clipart/20190630/original/pngtree-vector-bank-icon-png-image_4174115.jpg">
		
		<link rel = "stylesheet" href="${pageContext.request.contextPath}/resources/css/bank_find_id_form.css">
		
		<!-- Ajax 사용 준비 -->
		<script src="${pageContext.request.contextPath}/resources/js/httpRequest.js"></script>
		
		<script type="text/javascript">
		
		function find_id(f) {
			
			var name = f.name.value.trim();
			var ssn = f.ssn1.value +"-"+ f.ssn2.value;
			
			//유효성 체크			
			//이름
			if(name == ""){
				alert("이름을 입력해주세요.");
				return;
			}
			
			//주민등록번호
			var pattern = /^(?:[0-9]{2}(?:0[1-9]|1[0-2])(?:0[1-9]|[1,2][0-9]|3[0,1]))-[1-4][0-9]{6}$/;			
			if(!pattern.test(ssn)){
				alert("주민번호 13자리를 입력해주세요.");
				return;
			}
			
			var url = "find_id.do";
			var param = "name="+name+"&ssn="+ssn;
			
			sendRequest(url, param, result, "post");			
		}
		
		function result() {
			if(xhr.readyState == 4 && xhr.status == 200){
				var data = xhr.responseText;
				
				//no면 가입 x
				if(data != 'no'){
					alert("회원님의 아이디는 '"+data+"'입니다.");
					window.close();
					
				}else{
					alert("가입되어 있지 않은 정보입니다.\n 회원가입을 진행해 주세요!");
					window.close();
				}
		
			}
		}
		</script>
	</head>
	<body>
		<form class = "box">
				<h1 align = "center">아이디 찾기</h1>
			<table align = "center">
				<tr>
					<th>이름 : </th>
					<td><input type = "text" id = "name" name = "name"></td>
				</tr>
				<tr>
					<th>주민 번호 : </th>
					<td><input type = "text" class = "ssn" id = "ssn1" name = "ssn1" maxlength="6" size = "6"> - <input type = "password" class = "ssn" id = "ssn2" name = "ssn2" maxlength="7" size = "7"></td>
				</tr>
				<tr>
					<td colspan = "2" align = "center">
					<input type = "button" class = "btn" value = "찾기" onclick = "find_id(this.form);">
					<input type = "button" class = "btn" value = "취소" onclick = window.close();>
					</td>			
				</tr>
			</table>
			
		</form>
	</body>
</html>
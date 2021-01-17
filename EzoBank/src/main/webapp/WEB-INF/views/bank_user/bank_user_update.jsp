<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    
    <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
    
<!DOCTYPE html>
<html>
	<head>
		<meta charset="UTF-8">
		<title>Online Banking Modified</title>
		
		<link rel="shortcut icon" href="${pageContext.request.contextPath}/resources/img/bank_fav.png">
	
		<script src="${pageContext.request.contextPath}/resources/js/httpRequest.js"></script>
		<!--주소 찾기 API  -->
		<script src="http://dmaps.daum.net/map_js_init/postcode.v2.js"></script>
		
		<!--bank_join_form.css 스타일 시트 참조  -->
		<link rel = "stylesheet" href="${pageContext.request.contextPath}/resources/css/bank_user_update.css">
		
		<!-- (임시)배경화면 적용이 잘 안됨..-->
		<style type="text/css">		
		  body{background-image: url(resources/img/bank_building.jpg);
  		  background-size: cover; 
  		  }
		</style>

		<script type="text/javascript">
		
		function user_info(f) {

			var pwd = f.pwd.value.trim();
			var c_pwd = f.c_pwd.value.trim();
			var name = f.name.value.trim();
			var ssn = f.ssn1.value + "-" + f.ssn2.value;
			var account = f.account1.value + "-" + f.account2.value + "-" + f.account3.value;
			var tel = f.tel1.value.trim() +"-"+ f.tel2.value.trim() +"-"+ f.tel3.value.trim();
			var email = f.email1.value.trim() +"@"+ f.email2.value.trim();
			var addr = f.roadAddr.value +"/"+f.detailAddr.value.trim();
			var postcode = f.postcode.value;
			var idx = f.idx.value;
			
			//변경 가능한 것들에 대한 유효성 체크			
			//비밀번호
			if(pwd == ""){
				alert("비밀번호를 입력해주세요.");								
				return;
			}
			
			//비밀번호 체크
			if(c_pwd != pwd){
				alert("Password Check를 다시 확인해주세요.");
				return;
			}
			
			//이름
			if(name == ""){
				alert("이름을 입력해주세요.");
				return;
			}
			
			//전화번호
			var pattern = /^\d{3}-\d{3,4}-\d{4}$/;
			if(!pattern.test(tel)){
				alert("전화 번호를 다시 입력해주세요.");
				return;
			}
			
			//이메일
			var pattern = /^[a-z0-9_+.-]+@([a-z0-9-]+\.)+[a-z0-9]{2,4}$/;
			if(!pattern.test(email)){
				alert("이메일 형식이 올바르지 않습니다.");
				return;
			}
			
			//주소
			if(addr == ""){
				alert("주소를 입력해주세요.");
				return;
			}
			
			var param = "idx="+idx+"&pwd="+encodeURIComponent(pwd)+"&name="+name+"&ssn="+ssn+"&account="+account+"&tel="+tel+"&email="+email+"&addr="+addr + "&postcode="+postcode;
			var url = "user_modified.do";
			
			sendRequest(url,param,result,"post");
		}
		
		function result() {
			if(xhr.readyState == 4 && xhr.status == 200){
				var data = xhr.responseText;
				
				if(data != "yes"){
					alert("회원 정보 수정 실패..\n고객센터에 문의하세요.");
					return;
				}
				
				alert("회원 정보 수정 성공!");
				location.href="main.do";
				
			}
		}
		
		/*----------------------- 비밀번호 type 변경 ----------------------- */
		var on_off = 'on';
			
		function pwd_view() {
			
			if(on_off == 'on'){
				pwd.type = 'text';
				c_pwd.type = 'text';			
				on_off = 'off';
				view.value = '끄기';
				
			}else if(on_off == 'off'){
				pwd.type = 'password';
				c_pwd.type = 'password';			
				on_off = 'on';
				view.value = '보기';
			}

		}
	/*----------------------- 비밀번호 type 변경 ----------------------- */		
		/*------------------------- 주소 찾기 API ------------------------- */
		
	    function DaumPostcode_API() {
	        new daum.Postcode({
	            oncomplete: function(data) {

	            	// 참고 항목 변수
	                var extraRoadAddr = ''; 

	                // 참고 항목변수에 추가
	                if(data.bname !== '' && /[동|로|가]$/g.test(data.bname)){
	                   extraRoadAddr += data.bname;
	               		}
	                
	                // 건물명이 있고, 공동주택일 경우 참고 항목변수에 추가
	                if(data.buildingName != '' && data.apartment == 'Y'){
	                	extraRoadAddr += (extraRoadAddr != '' ? ', ' + data.buildingName : data.buildingName);
	                }

	                // 표시할 참고항목이 있을 경우, 괄호까지 추가한 최종 문자열을 만든다.
	                if(extraRoadAddr != ''){
	                    extraRoadAddr = '(' + extraRoadAddr + ')';
	                }
	             
	                // 해당 주소에 대한 정보 저장
	                document.getElementById('postcode').value = data.zonecode;
	                document.getElementById("roadAddr").value = data.roadAddress + extraRoadAddr;
	                document.getElementById("jibunAddr").value = data.jibunAddress + extraRoadAddr;                         

	                // 참고항목 문자열이 있을 경우 해당 필드에 넣는다.
	                if(roadAddr != ''){
	                    document.getElementById("extraAddr").value = extraRoadAddr;
	                } else {
	                    document.getElementById("extraAddr").value = '';
	                }                
	            }
	        }).open();
	    }
	/*------------------------- 주소 찾기 API ------------------------- */
		
		</script>
	
	</head>
	
	<body>
		<!--로그인 없이 이용 불가능  -->
		<c:if test="${ empty sessionScope.user}">
			<script>
				alert("로그인 후 이용하세요");
				location.href="login.do";
			</script>
		</c:if>  
		
		<!--아이디, 주민번호, 계좌번호 수정 불가 이외 수정 가능  -->
		<!-- 수정시 비밀번호를 입력   -->
		<div class="login" align="center">
			<jsp:include page="../bank_login/session/bank_login_logout.jsp"/>
		</div>
		<form class = "box">		
			<h1 align = "center">회원 정보 수정</h1>
			<input type = "hidden" name = "idx" value = ${user.idx}>
			<table  align = "center">
				<tr>
					<th> 아이디 : </th>
					<td><input id = "id" name = "id" value = ${user.id} readonly></td>
				</tr>
				<tr>
					<th> 비밀번호 : </th>
					<td><input type = "password" id = "pwd" name = "pwd"></td>
					<td class = "idpwd" rowspan = "2"><input type = "button" id = "view" name = "view" value = "보기" onclick = "pwd_view();"></td>		
				</tr>
				<tr>
					<th> 비밀번호 확인 : </th>
					<td><input type = "password" id = "c_pwd" name = "c_pwd"></td>
				</tr>				
				<tr>
					<th> 이름 : </th>
					<td><input id = "name" name = "name" value = ${user.name}></td>
				</tr>
				<tr>
					<th> 주민번호 : </th>
					<td><input class = "ssn" name = "ssn1" size = "5" maxlength="6" value=${ssn1} readonly> - <input type = "password" class = "ssn" name = "ssn2" size = "7" maxlength="7" value =${ssn2} readonly></td>
				</tr>
				<tr>
					<th> 계좌번호 : </th>
					<td colspan = "2"><input class = "account" name = "account1" maxlength= "3"size = "3" value =${account1} readonly> - <input class = "account" name = "account2" maxlength= "2" size = "2" value =${account2} readonly> - <input class = "account" name = "account3" maxlength= "6" size = "6" value =${account3} readonly></td>
				</tr>
				<tr>
					<th> 전화번호 : </th>
					<td colspan = "2">(<input class = "tel" name = "tel1" maxlength= "3"size = "3" value =${tel1}>) - <input class = "tel" name = "tel2" maxlength= "4" size = "4" value =${tel2}> - <input class = "tel" name = "tel3" maxlength= "4" size = "4" value =${tel3}></td>
				</tr>		
				<tr>
					<th> 이메일 : </th>
					<td colspan = "2"><input class = "email" name = "email1" maxlength= "9" size = "6" value =${email1}> @ <input class = "email" name = "email2" size = "13" value =${email2}></td>
				</tr>		
				<tr>
					<th rowspan = "3"> Address : </th>
					<!-- 주소 찾기 API --> 

					<td><input type="text" id="postcode" placeholder="Postcode" value=${user.postcode} readonly></td>
					<td><input type="button" id = "find_addr" onclick="DaumPostcode_API();" value="찾기"></td>
					
				</tr>
				<tr>
					<td>						
						<input type="text" name = "roadAddr" id="roadAddr" placeholder="주소" size="30" value = '${addr1}' readonly>
						<input type="hidden" name = "jibunAddr" id="jibunAddr" placeholder="주소"  size="30" value = '${addr1}' readonly>
					</td>
				</tr>
				<tr>
					<td>
						<input type="text" name = "detailAddr" id="detailAddr" placeholder="상세 주소"  value = '${addr2}' size="30">
						<input type="hidden" name = "extraAddr" id="extraAddr" placeholder="참고항목"  size="30">
					</td>
				</tr>

			</table>
			
			<div align = "center">
				<input class = "btn" type = "button" value = "수정" onclick="user_info(this.form);">
				<input class = "btn" type = "button" value = "취소" onclick="history.go(-1);">
			</div>
		</form>	
		
		<div id = "footer" align="center">
			<%@include file="../footer/bank_footer.jsp"%>
		</div>
		
	</body>
</html>
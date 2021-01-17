<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
	
	<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
    
<!DOCTYPE html>
<html>
	<head>
		<meta charset="UTF-8"> 
		
		<title>Online Banking Enrollment</title>
		
		<!-- favicon -->
		<link rel="shortcut icon" href="${pageContext.request.contextPath}/resources/img/bank_fav.png">
		
		<!--bank_join_form.css 스타일 시트 참조  -->
		<link rel = "stylesheet" href="${pageContext.request.contextPath}/resources/css/bank_sign_up_form.css">
		
		<!-- Ajax 사용 준비 -->
		<script src="${pageContext.request.contextPath}/resources/js/httpRequest.js"></script>
		
		<!--주소 찾기 API  -->
		<script src="http://dmaps.daum.net/map_js_init/postcode.v2.js"></script>

		<!-- (임시)배경화면 적용이 잘 안됨..ㅜㅜ  -->
		<style type="text/css">		
		  body{background-image: url(resources/img/bank_building.jpg);
  		  background-size: cover; 
  		  }
  		  
		</style>
		
	
		<script type="text/javascript">	
	
/*------------------------- 주소 찾기 API ------------------------- */
		
    function DaumPostcode_API() {	
        new daum.Postcode({
            oncomplete: function(data) {

            	// 참고 항목 변수 : 주소 입력시 괄호 안에 들어 갈 내용
            	//ex)서울 서대문구 성산로 450-2(신촌동) <- 괄호안에 내용
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
/*------------------------- id 중복 체크 ------------------------- */
		
		/* Ajax 사용(중복 ID Check) */
		var check = "no";
		
		function id_check() {

			var id = document.getElementById("id").value.trim();
			if(id == ""){
				alert("ID를 입력해주세요.");
				return;
			}
						
			var url = "id_check.do";
			var param = "id="+id;
			
			sendRequest(url, param, id_result, "post");
			
		}
		
		function id_result() {
			if(xhr.readyState == 4 && xhr.status == 200){
				var data = xhr.responseText;
	
				if(data == "yes"){
					check = "yes";
					alert("사용 가능한 아이디 입니다.");
				}else{
					check = "no";
					alert("이미 사용 중인 아이디 입니다.");
				}
			}
		}
/*------------------------- id 중복 체크 ------------------------- */

/*------------------------- 주민번호 중복 체크 --------------------   */
	
	/* Ajax 사용(중복 ssn Check) */
	
	var ssn_c = "no"; 
	
	function ssn_check() {
		
	var ssn1 = document.getElementById("ssn1").value.trim();
	var ssn2 = document.getElementById("ssn2").value.trim();
	var ssn = ssn1 + "-" + ssn2;

	if(ssn1 == "" || ssn2 == ""){
		alert("주민번호를 입력해주세요.");
		return;
	}
	
	var url = "ssn_check.do";
	var param = "ssn="+ssn;
	
	sendRequest(url, param, ssn_result, "post");
	
	}
	
	function ssn_result() {
		if(xhr.readyState == 4 && xhr.status == 200){
			var data = xhr.responseText;
			
			if(data == "no"){
				alert("이미 가입이 되어있는 주민번호 입니다.");
				ssn_c= "no";
				return;
			}
			
			if(data == "yes"){
				alert("사용 가능한 주민번호 입니다.");
				ssn_c = "yes";
				return;
			}
		}
	}
	

/*------------------------- 주민번호 중복 체크 --------------------   */
		
/*----------------------- 비밀번호 type 변경 ----------------------- */
	var on_off = 'on';
		
	function pwd_view() {
		//비밀번호 입력시 옆에 있는 버튼을 누를 때마다 type을 변경(password -> text || text -> password) 
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

/*------------------------- 회원가입 ------------------------- */
	
	//회원 정보 추가
	function join(f) {
			//유효성체크
			var id = f.id.value.trim();			
			var pwd = f.pwd.value.trim();
			var c_pwd = f.c_pwd.value.trim();
			var name = f.name.value.trim();
			var ssn = f.ssn1.value.trim() + "-" + f.ssn2.value.trim();
			var tel = f.tel1.value.trim() +"-"+ f.tel2.value.trim() +"-"+ f.tel3.value.trim();
			var email = f.email1.value.trim() +"@"+ f.email2.value.trim();
		 	var postcode = f.postcode.value;
			var addr =  f.roadAddr.value +"/"+f.detailAddr.value.trim();  
			
			//아이디
			if(id == ""){
				alert("아이디를 입력해주세요");
				return;
			}
			
			//아이디 중복체크 여부
			if(check != "yes"){
				alert("아이디 중복체크를 해주세요.");
				return;
			}
			
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
			
			//주민등록번호
			var pattern = /^(?:[0-9]{2}(?:0[1-9]|1[0-2])(?:0[1-9]|[1,2][0-9]|3[0,1]))-[1-4][0-9]{6}$/;			
			if(!pattern.test(ssn)){
				alert("주민번호 13자리를 입력해주세요.");
				return;
			}
			
			//주민번호 중복체크
			if(ssn_c == 'no'){
				alert("주민번호 중복체크를 해주세요.");
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
						
			//약관 동의
			//선택된 라디오 값 받기(선택되면 true, 아니면 false)
			var radio_value = document.getElementById("checkbox").checked;

			if(radio_value == false){
				alert("약관에 동의해주세요.");
				return;
			}
			
			//Ajax 사용(회원 정보 등록)
			var url = "insert.do";
			var param = "id="+id +"&pwd="+encodeURIComponent(pwd)+"&name="+name+"&ssn="+ssn+"&tel="+tel+"&email="+email+"&postcode="+postcode+"&addr="+addr; 
				
			sendRequest(url,param,join_result,"post")
			
		}	
		
		function join_result() {
			if(xhr.readyState == 4 && xhr.status == 200){
				var data = xhr.responseText;
				
			 	if(data != "yes"){
			 		alert("회원가입 실패..\n고객센터에 문의바랍니다.");
					return;				
			 	}
			 	 alert("회원가입 완료!");
				 location.href = "login.do";				
			 }
			 				 	
			}
	
/*------------------------- 회원가입 ------------------------- */				
		</script>		
	</head>
	<body id = "sign_box">	 
		<div class="login" align="center">
			<jsp:include page="../bank_login/session/bank_login_logout.jsp"/>
		</div>
		<form class = "box" id = "joining">
			<h1 align = "center">EzoBank 회원가입</h1>
			<p align = "center">* Please enter your account information.</p>

		<div id = "div_box">
			<table  id = "table" align = "center">
				<tr>
					<th> 아이디 : </th>
					<td><input type = "text" id = "id" name = "id"></td>
					<td class = "idpwd"><input type = "button" id = "c_id" name = "c_id" value = "확인" onclick = "id_check();"></td>
				</tr>
				<tr>
					<th> 비밀번호 : </th>
					<td><input type = "password" id = "pwd" name = "pwd"></td> 
					<td rowspan = "2" class = "idpwd"><input type = "button" id = "view" name = "view" value = "보기" onclick = "pwd_view();"></td>		
				</tr>
				<tr>
					<th> 비밀번호 확인 : </th>
					<td><input type = "password" id = "c_pwd" name = "c_pwd"></td>
				</tr>				
				<tr>
					<th> 이름 : </th>
					<td><input type = "text" id = "name" name = "name"></td>
				</tr>
				<tr>
					<th> 주민번호 : </th>
					<td><input type = "text" class = "ssn" id = "ssn1" name = "ssn1" size = "5" maxlength="6"> - <input type = "password" class = "ssn" id = "ssn2" name = "ssn2" size = "7" maxlength="7"></td>
					<td><input type = "button" id = "c_ssn" name = "c_ssn" value = "확인" onclick = "ssn_check();"></td>
				</tr>
				<tr>
					<th> 전화번호 : </th>
					<td colspan = "2">(<input type = "text" class = "tel" name = "tel1" maxlength= "3"size = "3">) - <input  type = "text" class = "tel" name = "tel2" maxlength= "4" size = "4"> - <input type = "text" class = "tel" name = "tel3" maxlength= "4" size = "4"></td>
				</tr>		
				<tr>
					<th> 이메일 : </th>
					<td colspan = "2"><input type = "text" class = "email" name = "email1" maxlength= "9" size = "6"> @ <input type = "text" class = "email" name = "email2" size = "13"></td>
				</tr>		
				<tr>
					<th rowspan ="3"> 주소 : </th> 
					
				<!-- 주소 찾기 API --> 
					
					<td><input type="text" id="postcode" placeholder="우편번호" size = "20" readonly></td>
					<td><input type="button" id = "find_addr" onclick="DaumPostcode_API();" value="찾기"></td>					
				</tr>
				<tr>
					<td>						
						<input type="text" name = "roadAddr" id="roadAddr" placeholder="주소" size="25" readonly>
						<input type="hidden" name = "jibunAddr" id="jibunAddr" placeholder="주소"  size="25"readonly>
					</td>
				</tr> 
				<tr>
					<td>
						<input type="text" name = "detailAddr" id="detailAddr" placeholder="상세 주소"  size="25">
						<input type="hidden" name = "extraAddr" id="extraAddr" placeholder="참고항목"  size="25">
					</td>
				</tr>
				<tr>
					<td colspan = "3" align = "center">
						<textarea id = "txt" rows="10" cols="65" disabled="disabled" style="resize: none;">${content}</textarea>
					</td>				
				</tr>
				<tr>
					<td align = "right" colspan = "3">
						<input type = "checkbox" id ="checkbox" value = "">동의
						<!-- <input type = "radio" name ="agree" value = "N">DisAgree -->
					</td>
				</tr>
				
				<!-- 계좌번호, 잔액 , 날짜 -->
			</table>
			</div>
			<div align = "center">
				<input class = "btn" type = "button" value = "가입" onclick="join(this.form);">
				<input class = "btn" type = "button" value = "취소" onclick="history.go(-1);">
			</div>
		</form>

			<div id = "footer" align="center">
				<%@include file="../footer/bank_footer.jsp"%>
			</div>
		
	</body>
</html>
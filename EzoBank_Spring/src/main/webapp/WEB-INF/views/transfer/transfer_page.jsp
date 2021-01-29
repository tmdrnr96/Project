<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
    
<!DOCTYPE html>
<html>
	<head>
		<meta charset="UTF-8">
		<%@ include file= "../bank_login/session/bank_login_logout.jsp" %>
		<title>Transfer</title>
		
		<link href="${ pageContext.request.contextPath }/resources/css/ezo_bank.css" rel="stylesheet">
		
		<script type="text/javascript" src="${ pageContext.request.contextPath }/resources/js/httpRequest.js"></script>
		
		<script type="text/javascript">
			function account(n) {
				var idx = n;
				var acc = document.getElementById("acc").value.trim();
				
				
				if(acc==''){
					alert("계좌번호를 입력해 주세요");
					return;
				}
				
				var str = acc;

				var first_str = str.substring(0, 3);
				var second_str = str.substring(3, 5);
				var last_str = str.substring(5, 12);
				
				//계좌번호에 -를 추가하기 위한 코드
				acc = first_str + "-" + second_str + "-" + last_str;
				
				var url = "transfer_account.do";
				var param = "idx="+idx+"&acc="+acc;
				sendRequest(url,param,resultFn,"post");
			}
			
			function resultFn() {
				if(xhr.readyState==4&&xhr.status==200){
					var data = xhr.responseText;
					if(data==''){
						alert("해당 계좌번호가 존재하지 않습니다");
						document.getElementById("acc").value = "";
						return;
					}
					
					var json = eval(data);
					
					if(!confirm(json[0].name+"님에게 이체하시겠습니까?")){
						document.getElementById("acc").value = "";
						return;
					}
					
					var idxMe = json[0].idxMe;
					var idxYou = json[0].idxYou;
					
					location.href="transfer_money_page.do?idxMe="+idxMe+"&idxYou="+idxYou;

				}
			}
			
			/* 버튼 클릭 시 input text필드에 값 이동 */
			function transfer(n) {
				if(n>=0){
					document.getElementById("acc").value += n;
				}
				
				/* 값 초기화 */
				else if(n==-1){
					document.getElementById("acc").value = "";
				}
				
				/* 삭제 */
				else{
					var val = document.getElementById('acc').value;
			        val = val.substr(0,val.length - 1);
			        document.getElementById('acc').value = val;
				}
			}
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
        
		<div align="center" class="num">
			<h1>:::이체:::</h1>
			<p style="color: white">${ vo.name }님 이체하실 계좌번호를 입력해 주세요</p>
			<div>
				<input type="button" value="1" onclick="transfer(1);">
				<input type="button" value="2" onclick="transfer(2);">
				<input type="button" value="3" onclick="transfer(3);">
			</div>
			<div>
				<input type="button" value="4" onclick="transfer(4);">
				<input type="button" value="5" onclick="transfer(5);">
				<input type="button" value="6" onclick="transfer(6);">
			</div>
			<div>
				<input type="button" value="7" onclick="transfer(7);">
				<input type="button" value="8" onclick="transfer(8);">
				<input type="button" value="9" onclick="transfer(9);">
			</div>
			<div>
				<input type="button" value="C" onclick="transfer(-1);">
				<input type="button" value="0" onclick="transfer(0);">
				<input type="button" value="del" onclick="transfer(-10);">
			</div>
			
			
			<br>
			&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
			<span style="color: white; font-size: 11px;">'-'를 붙이지 말고 숫자만 입력해주세요</span>
			&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
			
			<input type="text" id="acc">
			<input type="button" value="확인" onclick="account('${vo.idx}');"><br><br>
			<a href="main.do" style="text-decoration: none;">나가기</a>
			
		</div>
		
		<footer>
			<div id = "footer" align="center">
				<%@include file="../footer/bank_footer.jsp"%>
			</div>
		</footer>
	</body>
</html>
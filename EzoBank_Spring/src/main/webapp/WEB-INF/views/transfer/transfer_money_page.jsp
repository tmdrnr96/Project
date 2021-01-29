<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    
<!DOCTYPE html>
<html>
	<head>
		<meta charset="UTF-8">
		<%@ include file= "../bank_login/session/bank_login_logout.jsp" %>
		<title>Transfer</title>
		
		<link href="${ pageContext.request.contextPath }/resources/css/ezo_bank.css" rel="stylesheet">
		
		<script type="text/javascript" src="${ pageContext.request.contextPath }/resources/js/httpRequest.js"></script>
		
		<script type="text/javascript">
			function deposit(n1,n2) {
				var idxMe = n1;
				var idxYou = n2;
				
				var amount = document.getElementById("amount").value.trim();
				
				/* 금액 유효성 검사 */
				if(amount==''){
					alert("금액을 입력해 주세요");
					return;
				}
				
				if(amount<=0){
					alert("금액을 잘못 입력하셨습니다");
					document.getElementById("amount").value = "";
					return;
				}
				
				if(amount > ${voMe.balance}){
					alert("잔액이 부족합니다");
					document.getElementById("amount").value = "";
					return;
				}
				
				
				if(!confirm(amount+"원을 "+ '${voYou.name}' +"님에게 이체하시겠습니까?")){
					document.getElementById("amount").value = "";
					return;
				}
				
				var url = "transfer_amount.do";
				var param = "idxMe="+idxMe+"&idxYou="+idxYou+"&amount="+amount;
				sendRequest(url,param,resultFn,"post");
			}
			
			function resultFn() {
				if(xhr.readyState==4&&xhr.status==200){
					var data = xhr.responseText;
					
					if(data=='success'){
						alert("이체하였습니다");
						document.getElementById("amount").value = "";
						// 화면 앞쪽으로 돌아가기
						location.href="main.do";
					}
					else{
						alert("이체를 실패하였습니다");
						document.getElementById("amount").value = "";
						return;
					}
					
					
				}
			}
			
			/* 버튼 클릭 시 input text필드에 값 이동 */
			function transfer(n) {
				if(n>=0){
					document.getElementById("amount").value += n;
				}
				
				/* 값 초기화 */
				else if(n==-1){
					document.getElementById("amount").value = "";
				}
				
				/* 삭제 */
				else{
					var val = document.getElementById('amount').value;
			        val = val.substr(0,val.length - 1);
			        document.getElementById('amount').value = val;
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
			<p style="color: white">${ voMe.name }님 ${ voYou.name }께 이체하실 금액을 입력해주세요</p>
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
				
			<input type="text" id="amount">
			<input type="button" value="확인" onclick="deposit('${voMe.idx}','${ voYou.idx }');"><br><br>
			<a href="transfer_page.do?idx=${ voMe.idx }" style="text-decoration: none;">뒤로가기</a>
		</div>
		
		<footer>
			<div id = "footer" align="center">
				<%@include file="../footer/bank_footer.jsp"%>
			</div>
		</footer>
	</body>
</html>
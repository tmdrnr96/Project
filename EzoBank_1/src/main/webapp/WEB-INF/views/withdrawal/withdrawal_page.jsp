<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>    
    
<!DOCTYPE html>
<html>
	<head>
		<meta charset="UTF-8">
		<%@ include file= "../bank_login/session/bank_login_logout.jsp" %>
		<title>Withdrawal</title>
		
		<link href="${ pageContext.request.contextPath }/resources/css/ezo_bank.css" rel="stylesheet">
		
		<script type="text/javascript" src="${ pageContext.request.contextPath }/resources/js/httpRequest.js"></script>
		
		<script type="text/javascript">
			function withdrawal(n) {
				var idx = n;
				var amount = document.getElementById("amount").value.trim();
				
				var account = document.getElementById("account").value;
				var balance = document.getElementById("balance").value;		
				
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
				
				if(amount> ${vo.balance}){
					alert("잔액이 부족합니다");
					document.getElementById("amount").value = "";
					return;
				}
				
				var amt = amount%1000;
				if(amt>0){
					alert("동전은 출금이 불가합니다");
					document.getElementById("amount").value = "";
					return;
				}
				
			
				if(!confirm(amount+"원을 출금하시겠습니까?")){
					document.getElementById("amount").value = "";
					return;
				}
				
				/* 출금 성공여부를 알 수 있는 Ajax */
				var url = "depositandwithdraw.do";
				var param = "idx="+idx+"&amount="+amount+"&check=2"+"&account="+account+"&balance="+balance;
				sendRequest(url,param,resultFn,"post");
			}
			
			function resultFn() {
				if(xhr.readyState==4&&xhr.status==200){
					var data = xhr.responseText;
					
					if(data=='success'){
						alert("출금하였습니다");
						document.getElementById("amount").value = "";
						location.href="main.do";
					}
					else{
						alert("출금을 실패하였습니다");
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
				<h1>:::출금:::</h1>
				<p style="color: white">${ vo.name }님 계좌에서 출금하실 금액을 입력해주세요</p>
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
			
				<input type = "hidden" id = "account" value = "${user.account}">
				<input type = "hidden" id = "balance" value = "${user.balance}">
	
				<input type="text" id="amount">
				<input type="button" value="출금" onclick="withdrawal('${vo.idx}');"><br><br>
				<a href="main.do?idx=${ vo.idx }" style="text-decoration: none;">나가기</a>
			</div>
		
		<footer>
			<div id = "footer" align="center">
				<%@include file="../footer/bank_footer.jsp"%>
			</div>
		</footer>

	</body>
</html>
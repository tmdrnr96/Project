<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>   
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>   

<!DOCTYPE html>
<html>

<head>
<meta charset="UTF-8">
<%@ include file= "../bank_login/session/bank_login_logout.jsp" %>
<title>Insert title here</title>
<script src="${pageContext.request.contextPath}/resources/js/httpRequest.js"></script>

<link href="${pageContext.request.contextPath}/resources/css/view.css?after" rel="stylesheet" />
<link rel="preconnect" href="https://fonts.gstatic.com">
<link rel="preconnect" href="https://fonts.gstatic.com">
<link rel="preconnect" href="https://fonts.gstatic.com">
<link href="https://fonts.googleapis.com/css2?family=Do+Hyeon&display=swap" rel="stylesheet">
<link rel="preconnect" href="https://fonts.gstatic.com">
<link href="https://fonts.googleapis.com/css2?family=Nanum+Gothic:wght@700&display=swap" rel="stylesheet">


<style type="text/css">

body{
	background-image: url(resources/img/bank_building.jpg);
    background-size: cover; 
}


</style>

<script type="text/javascript">

	function detail(f) {
		
		//acc 계좌번호 
		var acc = document.getElementById("account").value;
		
		//get으로  넘길시 account -> null
		//post로 넘기면 account 값이 들어옴..
		f.action="det.do?account="+acc+"&page=${param.page}";
		f.method="post";
		f.submit();
		
	}

</script>
</head>
<body>
<form>

	<div class="box">
	
	
	 	<h1> :::잔액:::</h1>
	 	<p style="color: white">${ vo.name }님 입출금 계좌입니다.</p>
	 	<input type = "hidden" value = "${vo.account}" id="account">
	 	
	 	<div id="content">
	 	
		<p id="con1"> 계좌번호 : ${vo.account }</p>
		
		<p id="con3"> 잔액 : <fmt:formatNumber value="${vo.balance}" type="currency"
		                     currencyCode="KRW"/>원 </p>
		
		
		</div>
		
		<div class = "detail_btn">
		<input id="det" type="button" value="상세정보" onclick="detail(this.form);"> 
		
		</div>
	
		<a href="main.do?idx=${ vo.idx }" style="text-decoration: none;">나가기</a>
	</div>
	
</form>		
	
	<div id = "footer"  align="center">
			<%@include file="../footer/bank_footer.jsp"%>
	</div>
	
</body>
</html>
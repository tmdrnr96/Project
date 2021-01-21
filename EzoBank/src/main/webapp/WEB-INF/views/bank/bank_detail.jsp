<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<!DOCTYPE html>
<html>
<head>
<%@ include file= "../bank_login/session/bank_login_logout.jsp" %>
<meta charset="UTF-8">
<title>상세보기</title>
<link href="${pageContext.request.contextPath}/resources/css/detail.css?after" rel="stylesheet">

	<style type="text/css">
	
	body{
    background-image: url(resources/img/bank_building.jpg);
    background-size: cover;
    height: 1000px;
	}
	
	</style>
	
	<script type="text/javascript">
	
		function find() {
			var term = document.getElementById("term").value;
			location.href="det.do?term="+term+"&account=${param.account}"+"&page=${param.page}";		
		}
	
	</script>
	
</head>
<body>
	
	<div align="center" class="box">
			
			<h1>:::계좌 입∙출금 내역:::</h1>
			
			<div class="choice">
					<h5>조회기간</h5>
			
				<select id="term">
					<option value="0">전체</option>
					<option value="10">1개월</option>
					<option value="20">3개월</option>
				</select>
				
				<input type="button" id = "search_btn" value="검색" onclick="find();">
			</div>
			
			
		<hr style="margin: 10px 0">
		
		<table border="1" align="center" class="content-table">
			<tr>
				<th style="width: 90px;">거래일자</th>
				<th style="width: 50px;">항목</th>
				<th style="width: 80px;">입금</th>
				<th style="width: 80px;">출금</th>
				<th style="width: 90px;">항목잔액</th>
			</tr>
				<c:if test="${ empty list }">
					<td colspan="5">내역 없음</td>
				</c:if>	
								
				<c:if test="${ !empty list }">
					<c:forEach var="vo" items="${ list }">
						<tr class="b_tab">
							<td>${vo.usedate}</td>				
							<td>${vo.content}</td>				
							<td><fmt:formatNumber value="${vo.deposit}" /></td>				
							<td><fmt:formatNumber value="${vo.withdraw}"/></td>	
							<td><fmt:formatNumber value="${vo.balance}"/></td>			
						</tr>			
					</c:forEach>
				</c:if>	
				
			<tr>			
				<td colspan="5" align="center">
					${pageMenu}
				</td>
			</tr>				
		</table>
		
		<div id = "out_btn" align = "center">
			<a href="javascript:window.history.go(-2);" style="text-decoration: none;">나가기</a>
		</div>
		
		<div id = "footer"  align="center">
			<%@include file="../footer/bank_footer.jsp"%>
		</div>
</div>
</body>
</html>
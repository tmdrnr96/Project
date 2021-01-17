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
<%-- <link href="${pageContext.request.contextPath}/resources/css/detail.css?after" rel="stylesheet">
 --%>	
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
			alert(term);
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
			<%-- <caption style="color:white;">:::계좌 상세보기 :::</caption> --%>
		<thead>
			<tr>
				<th>거래일자</th>
				<th style="width: 40px;">항목</th>
				<th>입금</th>
				<th>출금</th>
				<th>항목잔액</th>
			</tr>
		</thead>	
		<tbody>
				<c:if test="${ empty list }">
					<td colspan="6">내역 없음</td>
				</c:if>	
				
				<c:if test="${ !empty list }">
					<c:forEach var="vo" items="${ list }">
						<tr class="b_tab">
							<td style="width: 90px;">${vo.usedate}</td>				
							<td style="width: 10px;">${vo.content}</td>				
							<td><fmt:formatNumber value="${vo.deposit}" /></td>				
							<td><fmt:formatNumber value="${vo.withdraw}"/></td>	
							<td><fmt:formatNumber value="${vo.balance}"/></td>			
						</tr>
							
						
					</c:forEach>
				</c:if>	
		</tbody>
		
		<tfoot>
			<tr>
				<td colspan="6" align="center">
					${pageMenu}
				</td>
			</tr>		
		</tfoot>
		
		</table>
		<div id = "out_btn" align = "center">
			<a href="main.do?idx=${ vo.idx }" style="text-decoration: none;">나가기</a>
		</div>
		
		<div id = "footer"  align="center">
			<%@include file="../footer/bank_footer.jsp"%>
		</div>
</div>
</body>
</html>
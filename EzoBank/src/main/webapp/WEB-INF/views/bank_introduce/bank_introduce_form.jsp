<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
	<head>
		<meta charset="UTF-8">
		<%@ include file= "../bank_login/session/bank_login_logout.jsp" %>
		<link rel = "stylesheet" href="${pageContext.request.contextPath}/resources/css/bank_introduce_form.css">
		<title>EzoBank Introduce</title>
		<style type="text/css">
		body{
		  background-image: url(resources/img/bank_building.jpg);
  		  background-size: cover;
		}
		</style>
	</head>	
	<body id = "introduce_body">	
		<div id = "introduce_box">	
			<div id = "introduce_box1">
				<h1>금융으로 세상을 이롭게 한다.</h1>
				<p>미래를 함께하는 EZO Bnak의 따뜻한 금융이란<br>
				상품, 서비스, 자금운용 등에서 과거와는 다른 방법, 새로운 환경에 맞는 새로운 방식을 추구하여 <br>
				고객과 EZO Bank 그리고 사회의 가치가 함께 커지는 상생의 선순환 구조를 추구하는 은행입니다.		
			</div>
			
			<div id = "introduce_box2">
				<img alt="introduce_img" src="${pageContext.request.contextPath}/resources/img/bank_introduce.png" width="650px" height="250px" >
			</div>
		
			<div id = "introduce_box3">
				<table id = "introduce_table" align = "center" border="1">
					<tr>
						<th width = "150" height="50">금융의 본업</th>
						<th width = "150" height="50">창조적 금융</th>
						<th width = "150" height="50">상생의 선순환 구조</th>
					</tr>
					<tr>
						<td>시대적 흐름에 맞는 금융상품, 서비스를 통해 고객의 목표 달성을 지우너하거나 고객이 맡긴 자금을 잘 
							운영하여 불려주는 것을 의미합니다.</td>
						<td>상품, 서비스 자금운용방식 등에서 과거와는 다른 방법. 새로운 환경에 맞는 새로운 방식을
							찾아야 하는데, 이러한 새로운 방법론을 통틀어  '창조적 금융'이라고 합니다.</td>
						<td>EZO Bank가 더 많은 고객의 성공을 돕고, 지원의 효율적 배분이라는
							금융의 기능을 잘 수행하면, 사회 전체의 가치도 커져 나가면서 상생과 발전을 이루어가는 모습이 상생의 선환 구조입니다. <br>
						</td>
					</tr>		
				</table>
			</div>	
		</div>
		<footer>
			<div id = "footer" align="center">
				<%@include file="../footer/bank_footer.jsp"%>
			</div>
		</footer>
	</body>
</html>
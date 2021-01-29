<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
 
<!DOCTYPE html>
<html>
	<head>
		<meta charset="UTF-8">
		<%@ include file= "../bank_login/session/bank_login_logout.jsp" %>
		<title>Insert title here</title>
		
		<link href="${ pageContext.request.contextPath }/resources/css/board_css.css" rel="stylesheet">
		
		<style type="text/css">
		body{
    		background-image: url(resources/img/bank_building.jpg);
    		background-size: cover;
		}
		</style>
		
		<script type="text/javascript" src="${ pageContext.request.contextPath }/resources/js/httpRequest.js"></script>
		
		<script type="text/javascript">
			
			function del() {
				if(confirm("정말 삭제하시겠습니까?")){
					var url = "notice_del.do";
					var param = "idx=${vo.idx}"; /* param.idx == vo.idx */
					sendRequest(url,param,resultFn,"post");
					
				}
				
			}
			
			function resultFn() {
				if(xhr.readyState == 4 && xhr.status == 200){
					var data = xhr.responseText;
					
					if(data == 'yes'){
						alert("글을 삭제했습니다");
						location.href = "notice_list.do?page=${param.page}";
					}
					
				}
			}
			
			function modify() {
				
				location.href="notice_modify.do?idx=${ param.idx }&page=${param.page}";
			}
		</script>
		
		<style type="text/css">
			.view_table{
				border-collapse: collapse;
				margin: 25px 0;
				font-size: 0.9em;
				position: absolute;
				top: 330px;
				left: 50%;
				transform: translate(-50%,-50%);
				min-width: 400px;
				border-radius: 24px;
				overflow: hidden;
				box-shadow: 0 0 20px rgba(0, 0, 0, 0.15);
				transition: 0.25s;
			}
			
		</style>
	</head>
	
	<body>
		<form name="f" method="post">
			<table class="view_table" border="1" align="center">
				<tr>
					<th width="120" height="25">제목</th>
					<td width="250">${ vo.subject }</td>
				</tr>
				
				<tr>
					<th width="120" height="25">작성일</th>
					<td width="250">${ vo.regdate }</td>
				</tr>
				
				<tr>
					<th width="120">내용</th>
					<td width="150" height="200">
						<c:if test="${ !empty vo.filename }">
							<div><img src="${ pageContext.request.contextPath }/resources/upload/${ vo.filename }" width="200"></div>
						</c:if>
						<pre>${ vo.content }</pre>
					</td>
				</tr>
				
				
				<tr>
					<td colspan="2" align="center">
						<!-- 목록으로 돌아가기 -->
						<img src="${ pageContext.request.contextPath }/resources/img/btn_list.gif" onclick="location.href='notice_list.do?page=${param.page}'">
						
						<c:if test="${sessionScope.user.id eq 'admin'}">
							<!-- 수정  -->
							<img src="${ pageContext.request.contextPath }/resources/img/btn_modify.gif" onclick="modify();">
							
							<!-- 삭제 -->
							<img src="${ pageContext.request.contextPath }/resources/img/btn_delete.gif" onclick="del();">
						</c:if>
					</td>
				</tr>
				
			</table>
		</form>
		<div id = "footer" align="center">
			<%@include file="../footer/bank_footer.jsp"%>
		</div>
	</body>
</html>
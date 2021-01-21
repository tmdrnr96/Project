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
			function reply() {
				location.href = "reply_form.do?idx=${vo.idx}&page=${param.page}";
			}
			
			function del() {
				if(confirm("정말 삭제하시겠습니까?")){
					var pwd = document.getElementById("c_pwd").value.trim();
					
					if(pwd==''){
						alert("비밀번호를 입력하세요");
						return;
					}
					
					if(pwd != ${ vo.pwd }){
						alert("비밀번호가 일치하지 않습니다");
						return;
					}
					
					var url = "del.do";
					var param = "idx=${vo.idx}"; /* param.idx == vo.idx */
					sendRequest(url,param,resultFn,"post");
					
				}
				
			}
			
			function resultFn() {
				if(xhr.readyState == 4 && xhr.status == 200){
					var data = xhr.responseText;
					
					if(data == 'yes'){
						alert("글을 삭제했습니다");
						location.href = "board_list.do?page=${param.page}";
					}
					
				}
			}
			
			function modify() {
				var pwd = document.getElementById("c_pwd").value.trim();
				if(pwd==''){
					alert("비밀번호를 입력하세요");
					return;
				}
				
				if(pwd != ${ vo.pwd }){
					alert("비밀번호가 일치하지 않습니다");
					return;
				}
				
				location.href="board_update.do?idx=${ param.idx }&page=${param.page}";
			}
		</script>
	</head>
	
	<body>
		<form name="f" method="post">
			<table class="view_table" border="1" align="center">
				<tr>
					<th width="120" height="25">제목</th>
					<td width="250">${ vo.subject }</td>
				</tr>
				
				<tr>
					<th width="120" height="25">작성자</th>
					<td width="250">${ vo.name }</td>
				</tr>
				
				<tr>
					<th width="120" height="25">작성일</th>
					<td width="250">${ vo.regdate }</td>
				</tr>
				
				<tr>
					<th width="120">내용</th>
					<td width="250" height="250"><pre>${ vo.content }</pre></td> <!-- pre : enter값 인식하도록 하는 태그 -->
				</tr>
				
				<tr>
					<th width="120">비밀번호</th>
					<td width="250">
						<input type="password" id="c_pwd" style="border:none">
					</td> 
				</tr>
				
				
				<tr>
					<td colspan="2" align="center">
						<!-- 목록으로 돌아가기 -->
						<img src="${ pageContext.request.contextPath }/resources/img/btn_list.gif" onclick="location.href='board_list.do?page=${param.page}'">
						
						<c:if test="${ vo.depth eq 0 }">
							<!-- 댓글 -->
							<img src="${ pageContext.request.contextPath }/resources/img/btn_reply.gif" onclick="reply();"> <!-- input이 아니면 this.form을 사용할 수 없다 -->
						</c:if>
						
						<!-- 수정  -->
						<img src="${ pageContext.request.contextPath }/resources/img/btn_modify.gif" onclick="modify();">
						
						<!-- 삭제 -->
						<img src="${ pageContext.request.contextPath }/resources/img/btn_delete.gif" onclick="del();">
					</td>
				</tr>
				
			</table>
		</form>
		<div id = "footer" align="center">
			<%@include file="../footer/bank_footer.jsp"%>
		</div>
	</body>
</html>
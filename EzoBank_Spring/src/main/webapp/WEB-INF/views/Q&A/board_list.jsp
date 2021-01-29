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
		
	</head>
	
	<body>
		<table class="board_table">
			<tr>
				<td colspan="5" align="center" style="font-size: 25px; font-weight: bold;">
					Q&A
				</td>
			</tr>
			
			<tr>
				<th width="10%">번호</th>
				<th>제목</th>
				<th width="15%">작성자</th>
				<th width="25%">작성일</th>
				<th width="10%">조회수</th>
			</tr>
			
			
			<c:forEach var="vo" items="${ list }">
				<tr>
					<td align="center">${ vo.idx }</td>
					
					<td>
						<!-- 댓글 들여쓰기 -->
						<c:forEach begin="1" end="${ vo.depth }">
							&nbsp;
						</c:forEach>
						
						<!-- 댓글기호  -->
						<c:if test="${ vo.depth ne 0 }">
							ㄴ
						</c:if>
						
						<!-- 삭제된 글은 클릭이 불가 -->
						<c:if test="${ vo.del_info eq -1 }">
							<span style="color:gray;">
								${ vo.subject }
							</span>
						</c:if>
						
						<!-- 삭제되지 않은 글은 클릭 가능 -->
						<c:if test="${ vo.del_info ne -1 }">
							<a href="view.do?idx=${ vo.idx }&page=${empty param.page?1:param.page}" style=" text-decoration:none;">
								${ vo.subject }
							</a>
						</c:if>
					</td>
					
					<td align="center">${ vo.name }</td>
					<td align="center">${ vo.regdate }</td>
					<td align="center">${ vo.readhit }</td>
				</tr>
			</c:forEach>
			
			<!-- 게시판 페이지 갯수 표시 -->
			<tr>
				<td colspan="5" align="center">
					${ pageMenu }
				</td>
			</tr>
			
			<tr>
				<td colspan="5" align="right">
					<img src="${ pageContext.request.contextPath }/resources/img/btn_reg.gif" onclick="location.href='insert_form.do'">
				</td>
			</tr>
			
			<!-- 게시판에 게시글이 1개도 없는 경우 -->
			<c:if test="${ empty list }">
				<tr>
					<td colspan="5" align="center">
						현재 등록된 게시글이 없습니다
					</td>
				</tr>
			</c:if>			
		</table>	
		<div id = "footer" align="center">
			<%@include file="../footer/bank_footer.jsp"%>
		</div>	
	</body>
</html>
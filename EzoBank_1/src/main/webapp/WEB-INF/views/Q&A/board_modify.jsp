<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    
<!DOCTYPE html>
<html>
	<head>
		<meta charset="UTF-8">
		<%@ include file= "../bank_login/session/bank_login_logout.jsp" %>
		<title>Insert title here</title>
		
		<link href="${ pageContext.request.contextPath }/resources/css/board_css.css" rel="stylesheet">
		
		<script type="text/javascript">
			function send_check() {
				var f = document.f;
				
				if(f.subject.value==''){
					alert("제목을 입력하세요");
					return;
				}
				
				if(f.name.value==''){
					alert("작성자를 입력하세요");
					return;
				}
				
				if(f.content.value.trim()==''){
					alert("내용 한 글자 이상 입력해야 합니다");
					return;
				}
				
				if(f.pwd.value.trim()==''){
					alert("비밀번호를 입력하세요");
					return;
				}
				
				if(f.pwd.value.trim()!=${vo.pwd}){
					alert("비밀번호가 틀렸습니다");
					return;
				}
				
				f.action="update.do?idx=${ param.idx }&page=${param.page}";
				f.submit();
				
			}
		</script>
	</head>
	
	<body>
		<table class="view_table">
			<form name="f" method="post">
				<tr>
					<th width="120">제목</th>
					<td>
						<input name="subject" value="${ vo.subject }" style="width:250px;">
					</td>
				</tr>
				
				<tr>
					<th width="120">작성자</th>
					<td>
						<input name="name" value="${ vo.name }" style="width:250px;">
					</td>
				</tr>
				
				<tr>
					<th width="120">내용</th>
					<td>
						<!-- 세로:10줄, 가로:70글자 -- -->
						<pre><textarea name="content" rows="15" cols="40" style="resize: none;">${ vo.content }</textarea></pre>
					</td>
				</tr>
				
				<tr>
					<th width="120">비밀번호</th>
					<td>
						<input type="password" name="pwd" style="width:250px;">
					</td>
				</tr>
				
				<tr>
					<td align="center" colspan="2">
						<img src="${ pageContext.request.contextPath }/resources/img/btn_reg.gif" onclick="send_check();" style="cursor:pointer"> <!-- 이미지 태그에서는 this.form을 사용하지 못한다  -->
						<img src="${ pageContext.request.contextPath }/resources/img/btn_back.gif" onclick="location.href='board_list.do'" style="cursor:pointer">
					</td>
				</tr>
			</form>
			
		</table>
		<div id = "footer" align="center">
			<%@include file="../footer/bank_footer.jsp"%>
		</div>
	</body>
</html>
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
				
				if(f.content.value.trim()==''){
					alert("내용 한 글자 이상 입력해야 합니다");
					return;
				}
				
				
				f.action="notice_update.do?idx=${ param.idx }&page=${param.page}";
				f.submit();
			}
		</script>
		
		<style type="text/css">
			.view_table{
				border-collapse: collapse;
				margin: 25px 0;
				font-size: 0.9em;
				position: absolute;
				top: 300px;
				left: 50%;
				transform: translate(-50%,-50%);
				min-width: 500px;
				border-radius: 24px;
				overflow: hidden;
				box-shadow: 0 0 20px rgba(0, 0, 0, 0.15);
				transition: 0.25s;
			}
		</style>
	</head>
	
	<body>
		<table class="view_table">
			<form name="f" method="post" enctype="multipart/form-data">
				<input type="hidden" name="filename" value="${ vo.filename }">
				<tr>
					<th width="120">제목</th>
					<td>
						<input name="subject" value="${ vo.subject }" style="width:350px;">
					</td>
				</tr>
				
				<tr>
					<th width="120">첨부</th>
					<td><input type="file" name="photo"></td>
				</tr>
				
				<tr>
					<th width="120">내용</th>
					<td>
						<textarea name="content" rows="15" cols="40" style="resize: none; width: 350px;">${ vo.content }</textarea>
					</td>
				</tr>
				
				<tr>
					<td align="center" colspan="2">
						<img src="${ pageContext.request.contextPath }/resources/img/btn_reg.gif" onclick="send_check();" style="cursor:pointer"> <!-- 이미지 태그에서는 this.form을 사용하지 못한다  -->
						<img src="${ pageContext.request.contextPath }/resources/img/btn_back.gif" onclick="location.href='notice_list.do'" style="cursor:pointer">
					</td>
				</tr>
			</form>
			
		</table>
		<div id = "footer" align="center">
			<%@include file="../footer/bank_footer.jsp"%>
		</div>
	</body>
</html>
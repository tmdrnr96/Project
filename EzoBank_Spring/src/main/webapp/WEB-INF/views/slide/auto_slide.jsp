<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
	<head>
		<meta charset="UTF-8">
		<title>Insert title here</title>
		<link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/auto_slied.css">
		<script type="text/javascript">
		
		//슬라이드
		var slideIndex = 1;
		 
		var i;
		
	    var slides = document.getElementsByClassName("mySlides");	 
	    var dots = document.getElementsByClassName("dot");
	    
			function plusSlides() {
				  slideIndex++;				  				 
				 
				  if (slideIndex > slides.length) {slideIndex = 1}
				  
				  for (i = 0; i < slides.length; i++) {	
				      slides[i].style.display = "none";	
				  }
		
				  for (i = 0; i < dots.length; i++) {
				      dots[i].className = dots[i].className.replace(" active", "");
				  }
		
				  slides[slideIndex-1].style.display = "block";
				  dots[slideIndex-1].className += " active";
				  
				}
	    				
			//도트무늬 클릭
			function currentSlide(n) {
			
				slideIndex = n;
				
				if (slideIndex > slides.length) {slideIndex = 1}  
				
				  if (slideIndex < 1) {slideIndex = slides.length}
				 
				  for (i = 0; i < slides.length; i++) {
				      slides[i].style.display = "none";  
				  }
				  for (i = 0; i < dots.length; i++) {
				      dots[i].className = dots[i].className.replace(" active", "");
				  }
				  slides[slideIndex-1].style.display = "block";  
				  dots[slideIndex-1].className += " active";
			}
			
			//2초 간격으로 plusSlides 메서드 실행(자동)
			setInterval("plusSlides()", 2000);
		
		</script>
	</head>
	<body id = slide_body>
		<div id = "slide_box">
			<div class="slideshow-container">
			
				<div class="mySlides" style="display: block;">			
					<a href = "javascript:m_transfer('${user.idx}');">
						<img class="main_slideImg" src="${pageContext.request.contextPath}/resources/img/sample1.jpg"
						 width="250px;" height="150px;">
					</a>
				</div>
				
				<div class="mySlides">	
					<a href = "join_form.do">		
						<img class="main_slideImg" src="${pageContext.request.contextPath}/resources/img/sample2.jpg"
						 width="250px;" height="150px;">
					</a>
				</div>
	
				<div class="mySlides">	
					<a href = "bank_introduce.do">		
						<img class="main_slideImg" src="${pageContext.request.contextPath}/resources/img/sample3.jpg"
						 width="250px;" height="150px;">
					</a>
				</div>				
			</div>
			
			<div id = "dotlist" style="text-align:center">
			  <span class="dot" onclick="currentSlide(1)"></span> 
			  <span class="dot" onclick="currentSlide(2)"></span> 
			  <span class="dot" onclick="currentSlide(3)"></span> 
			</div>
		</div>
	</body>
</html>
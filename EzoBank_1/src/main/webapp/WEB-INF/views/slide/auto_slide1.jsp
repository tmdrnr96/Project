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
	    
	    	//오른쪽 화살표 누를때
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
			
			function minusSlide() {
			  slideIndex--;
			 
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
			
			setInterval("plusSlides()", 2000);
		
		</script>
	</head>
	<body id = slide_body>
		<div id = "slide_box">
			<div class="slideshow-container">
			
				<div class="mySlides" style="display: block;">			
					<a href = "#">
						<img class="main_slideImg" src="${pageContext.request.contextPath}/resources/img/sample1.jpg">
					</a>
				</div>
				
				<div class="mySlides">	
					<a href = "#">		
						<img class="main_slideImg" src="${pageContext.request.contextPath}/resources/img/sample2.jpg">
					</a>
				</div>
	
				<div class="mySlides">	
					<a href = "#">		
						<img class="main_slideImg" src="${pageContext.request.contextPath}/resources/img/sample3.jpg">
					</a>
				</div>
										
				<a class="prev" onclick="minusSlide();" style="font-size:2cm;">❮</a>
				<a class="next"onclick="plusSlides();" style="font-size:2cm;">❯</a>
					
			</div>
			<br>
			
			<div id = "dotlist" style="text-align:center">
			  <span class="dot" onclick="currentSlide(1)"></span> 
			  <span class="dot" onclick="currentSlide(2)"></span> 
			  <span class="dot" onclick="currentSlide(3)"></span> 
			</div>
		</div>
	</body>
</html>
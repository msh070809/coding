<%@page import="org.dms.web.domain.UserVO"%>

<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<%@ taglib uri="http://www.springframework.org/security/tags"
	prefix="sec"%>

<%
String imgURL = "";

if (request.getAttribute("user") != null) {

	UserVO user = (UserVO) request.getAttribute("user");

	if (user.getUser_img() == null) {

		imgURL = (String) request.getContextPath() + "/resources/images/user.png";

	} else {

		imgURL = "/getByteImage/" + user.getUser_id();

	}

}
%>



<!DOCTYPE HTML>



<html>

<head>

<title>CODING</title>

<meta charset="utf-8" />

<meta name="viewport"
	content="width=device-width, initial-scale=1, user-scalable=no" />

<link rel="stylesheet"
	href="<%=request.getContextPath()%>/resources/css/main.css"
	type="text/css" />

<link rel="stylesheet"
	href="<%=request.getContextPath()%>/resources/css/custom_main.css"
	type="text/css" />

<link rel="stylesheet"
	href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.8.2/css/all.min.css" />

<!-- 이게 Font Awesome 5 Free를 사용하게 해주는거 같아요. 이거덕에 사이드바 모양이 보여요! -->



<script>
	function setupTypewriter(t) {

		var HTML = t.innerHTML;

		t.innerHTML = "";

		var cursorPosition = 0,

		tag = "",

		writingTag = false,

		tagOpen = false,

		typeSpeed = 100,

		tempTypeSpeed = 0;

		var type = function() {

			if (writingTag === true) {

				tag += HTML[cursorPosition];

			}

			if (HTML[cursorPosition] === "<") {

				tempTypeSpeed = 0;

				if (tagOpen) {

					tagOpen = false;

					writingTag = true;

				} else {

					tag = "";

					tagOpen = true;

					writingTag = true;

					tag += HTML[cursorPosition];

				}

			}

			if (!writingTag && tagOpen) {

				tag.innerHTML += HTML[cursorPosition];

			}

			if (!writingTag && !tagOpen) {

				if (HTML[cursorPosition] === " ") {

					tempTypeSpeed = 0;

				}

				else {

					tempTypeSpeed = (Math.random() * typeSpeed) + 50;

				}

				t.innerHTML += HTML[cursorPosition];

			}

			if (writingTag === true && HTML[cursorPosition] === ">") {

				tempTypeSpeed = (Math.random() * typeSpeed) + 50;

				writingTag = false;

				if (tagOpen) {

					var newSpan = document.createElement("span");

					t.appendChild(newSpan);

					newSpan.innerHTML = tag;

					tag = newSpan.firstChild;

				}

			}

			cursorPosition += 1;

			if (cursorPosition < HTML.length) {

				setTimeout(type, tempTypeSpeed);

			}

		};

		return {

			type : type

		};

	}

	function typing() {

		var cpptyper = document.getElementById('cpptypo');

		cpptypewriter = setupTypewriter(cpptypo);

		cpptypewriter.type();

	}

	window.onload = setInterval(function() {

		typing();

	}, 4000);

	function function1012() {
		location = "http://localhost:9130/problem/1012"
	}
	function function187() {
		location = "http://localhost:9130/problem/187"
	}
	function function751() {
		location = "http://localhost:9130/problem/751"
	}
	function function214() {
		location = "http://localhost:9130/problem/214"
	}
	function function123() {
		location = "http://localhost:9130/problem/123"
	}
	function function791() {
		location = "http://localhost:9130/problem/791"
	}
	function function428() {
		location = "http://localhost:9130/problem/428"
	}
	function function8() {
		location = "http://localhost:9130/problem/8"
	}
	function function148() {
		location = "http://localhost:9130/problem/148"
	}
</script>

</head>

<body class="is-preload">



	<!-- Wrapper -->

	<div id="wrapper">



		<!-- Main -->

		<div id="main">

			<jsp:include page="header.jsp" flush="true">

				<jsp:param name="imgURL" value="<%=imgURL%>" />

			</jsp:include>

			<h2 style="text-align: center">안녕하세요. 오늘도 화이팅입니다!</h2>

			<div
				style="text-align: center; margin-top: 30px; border-radius: 10px;">

				<div
					style="display: inline-block; width: 30%; border: 1px solid white; border-radius: 10px;">

					<h3>TODAY</h3>

					<div style="background-color: white; color: black">

						<div style="margin-top: 10px; font-size: 25px; display: flex"
							onclick="function1012()">

							<div class="problem_level" id="item_level_2"
								style="background-color: #7BC379; width: 80%; border-radius: 20px;">LEVEL
								2</div>

							<div style="width: 100%">1012. 유기농 배추</div>

						</div>

						<div style="margin-top: 10px; font-size: 25px; display: flex"
							onclick="function187()">

							<div class="problem_level" id="item_level_1"
								style="background-color: #FFCC80; width: 80%; border-radius: 20px;">LEVEL
								1</div>

							<div style="width: 100%">187. 어린 왕자</div>

						</div>

						<div style="margin-top: 10px; font-size: 25px; display: flex"
							onclick="function751()">

							<div class="problem_level" id="item_level_5"
								style="background-color: #8C699B; width: 80%; border-radius: 20px;">LEVEL
								5</div>

							<div style="width: 100%">751. 팀 선발</div>

						</div>

					</div>

				</div>

				<div
					style="display: inline-block; width: 30%; border: 1px solid white; border-radius: 10px;">

					<h3>NEW</h3>

					<div style="background-color: white; color: black">

						<div style="margin-top: 10px; font-size: 25px; display: flex"
							onclick="function214()">

							<div class="problem_level" id="item_level_1"
								style="background-color: #FFCC80; width: 80%; border-radius: 20px;">LEVEL
								1</div>

							<div style="width: 100%">214. 팰린드롬</div>

						</div>

						<div style="margin-top: 10px; font-size: 25px; display: flex"
							onclick="function123()">

							<div class="problem_level" id="item_level_3"
								style="background-color: #79BCC3; width: 80%; border-radius: 20px;">LEVEL
								3</div>

							<div style="width: 100%">123. 감소하는 수</div>

						</div>

						<div style="margin-top: 10px; font-size: 25px; display: flex"
							onclick="function791()">

							<div class="problem_level" id="item_level_4"
								style="background-color: #EA7862; width: 80%; border-radius: 20px;">LEVEL
								4</div>

							<div style="width: 100%">791. 트리의 순회</div>

						</div>

					</div>

				</div>

				<div
					style="display: inline-block; width: 30%; border: 1px solid white; border-radius: 10px;">

					<h3>DIFFICULT</h3>

					<div style="background-color: white; color: black">

						<div style="margin-top: 10px; font-size: 25px; display: flex"
							onclick="function428()">

							<div class="problem_level" id="item_level_5"
								style="background-color: #8C699B; width: 80%; border-radius: 20px;">LEVEL
								5</div>

							<div style="width: 100%">428. 스티커 수집</div>

						</div>

						<div style="margin-top: 10px; font-size: 25px; display: flex"
							onclick="function8()">

							<div class="problem_level" id="item_level_2"
								style="background-color: #EA7862; width: 80%; border-radius: 20px;">LEVEL
								4</div>

							<div style="width: 100%">8. 카드 정렬하기</div>

						</div>

						<div style="margin-top: 10px; font-size: 25px; display: flex"
							onclick="function148()">

							<div class="problem_level" id="item_level_4"
								style="background-color: #EA7862; width: 80%; border-radius: 20px;">LEVEL
								4</div>

							<div style="width: 100%">148. 종이 접기</div>

						</div>

					</div>

				</div>

			</div>

		</div>

	</div>



	<div class="inner">
		<h3 style="text-align: center">프로그래밍 문제를 풀고 온라인으로 채점을 받고 점수를
			확인하세요.</h3>

		<h3 style="text-align: center">어려운 문제는 친구와 함께 공유하며 학습하세요.</h3>

		<div class="typo">

			<span class="blue">print(<span class="red" id="cpptypo">"CBNU
					coding"</span><span class="blue">)</span> </pre>
		</div>

	</div>

	<footer>

		<p>
			CBNU ATW<br>
		<p style="margin: 0;">
			박찬일&emsp;문승현</a>&emsp;정연휘<br>
	</footer>



	<!-- Scripts -->

	<script
		src="${pageContext.request.contextPath}/resources/js/jquery.min.js"></script>

	<script
		src="${pageContext.request.contextPath}/resources/js/browser.min.js"></script>

	<script
		src="${pageContext.request.contextPath}/resources/js/breakpoints.min.js"></script>

	<script src="${pageContext.request.contextPath}/resources/js/util.js"></script>

	<script src="${pageContext.request.contextPath}/resources/js/main.js"></script>

</body>

</html>
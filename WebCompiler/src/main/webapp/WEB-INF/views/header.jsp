<%@page import="org.dms.web.domain.UserVO"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/security/tags"
	prefix="sec"%>
<%
String imgURL = request.getParameter("imgURL");
%>
<header id="header">
	<a class="main_logo" href="/"><img
		src="<%=request.getContextPath()%>/resources/images/main_logo.png"
		alt="메인페이지" /></a> <a class="header_problem" href="/problem"><img
		src="<%=request.getContextPath()%>/resources/images/coding.png"
		alt="문제 페이지" />문제풀기</a> <a class="header_board" href="/board"><img
		src="<%=request.getContextPath()%>/resources/images/header_board.png"
		alt="게시판 페이지" />자유게시판</a>

	<c:if test="${user.user_id == null}">
		<a class="header_signup" href="/join">회원가입</a>
		<a class="header_signin" href="/login">로그인</a>
	</c:if>

	<c:if test="${user.user_id != null}">
		<div class="header_profile" style="cursor: pointer;"
			onClick="location.href='/mypage'">
			<img class="img" src=<%=imgURL%>>
			<div class="name_intro">
				<div class="header_name">
					<a href="/mypage">${user.user_name}</a>
				</div>
				<div class="header_intro">${user.user_introduce}</div>
			</div>
		</div>
	</c:if>

	<c:if test="${user.user_authority == 'admin'}">
		<a class="header_testcase" href="/testcase"><img
			src="/resources/images/testcase.png" />테스트케이스</a>

		<a class="header_problem_insert" href="/problem/insert"><img
			src="/resources/images/problem_insert.png" />문제등록</a>
	</c:if>
</header>


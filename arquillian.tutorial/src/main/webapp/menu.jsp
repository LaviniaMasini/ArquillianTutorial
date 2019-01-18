<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%><!DOCTYPE html>
<html>
<head>
<title>menu</title>
<meta charset="UTF-8">
<link rel="stylesheet" type="text/css" href="css/menu.css" />

<style>
.button {
	font-size: 16px;
	border: none;
	width: 160px;
	height: 50px;
	outline: none;
	color: black;
	background-color: inherit;
	font-family: inherit;
	text-align: left;
}

.button:hover {
	background: #ddd;
}
</style>

</head>
<body>

	<div class="navbar">
		<a id="homeBtn" href="homepage">Home</a>

		<%
			if (session.getAttribute("username") == null) {
		%>
		<a id="loginBtn" style="float: right;" href="login">Login</a>
		<%
			} else {
		%>
		<div class="dropdown" style="float: right;">
			<button id="welcomeBtn" class="dropbtn">
				Welcome
				<%=session.getAttribute("username")%></button>
			<div class="dropdown-content">
				<a id="userInfo" href="update_credentials">User Info</a> <a
					id="logout" href="logout">Logout</a>
			</div>
			<%
				}
			%>
		</div>
	</div>

</body>
</html>
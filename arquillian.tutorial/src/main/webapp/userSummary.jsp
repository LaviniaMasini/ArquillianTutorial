<!DOCTYPE html>
<html>
<head>
<title>User Home</title>
<link rel="stylesheet" type="text/css" href="css/button.css" />
<style>
h3 {
	text-align: center;
}
</style>
</head>
<body>
	<jsp:include page="menu.jsp" />
	<h3 id="welcomeTitle">
		Welcome
		<%=session.getAttribute("username")%></h3>
	<a id="userInfoBtn" href="update_credentials" class="buttonLeft">User
		Info<br>
	</a>
	<a id="logoutBtn" href="logout" class="buttonRight">Logout<br></a>
</body>
</html>

<!DOCTYPE html>
<html>
<head>
<title>Login Page</title>
<link rel="stylesheet" type="text/css" href="css/form.css" />
</head>
<body>

	<jsp:include page="menu.jsp" />

	<div class="form">
		<form name="Login" id="form-1" method="post" action="login">
			<span class="title"> Login </span> 
				<label id="usernameLabel" for="user">Username</label>
				<input id="username" class="input" name="username" placeholder="Username" type="text" required
				title="Please fill out this field"
				oninvalid="setCustomValidity('Please insert valid Username')"
				oninput="setCustomValidity('')" /><br>
				<label id="passwordLabel" for="pass">Password</label>
				<input id="password" class="input" name="password" placeholder="Password" type="password"
				required title="Please fill out this field"
				oninvalid="setCustomValidity('Please insert valid Password')"
				oninput="setCustomValidity('')" /> 
				<a class="newUser" id="newUser"	href="registration">Create your account</a>
				<input class="loginBtn" type="submit" id="confirmBtn" value="Ok" />
			<%
				if (request.getAttribute("errorMessage") != null) {
			%>
			<label class="error" id="error"><%=request.getAttribute("errorMessage")%> </label>
			<%
				}
			%>
		</form>
	</div>


</body>
</html>
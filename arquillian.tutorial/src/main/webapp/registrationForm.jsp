<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html>
<head>
<title>Registration Page</title>
<link rel="stylesheet" type="text/css" href="css/form.css" />
</head>
<body>
	<jsp:include page="menu.jsp" />

	<div class="form">
		<form name="Login" id="form-1" method="post" action="registration">
			<span class="title"> Insert your data </span><br> <label
				id="firstnameLabel" for="firstname">Firstname<abbr
				title="This field is mandatory">*</abbr></label><input id="firstname"
				class="input" name="firstname" placeholder="Firstname" type="text"
				required title="Please fill out this field"
				oninvalid="setCustomValidity('Please insert your Firstname')"
				oninput="setCustomValidity('')" /> <label id="lastnameLabel"
				for="lastname">Lastname<abbr title="This field is mandatory">*</abbr></label>
			<input id="lastname" class="input" name="lastname"
				placeholder="Lastname" type="text" required
				title="Please fill out this field"
				oninvalid="setCustomValidity('Please insert your Lastname')"
				oninput="setCustomValidity('')" /> <label id="addressLabel"
				for="address">Address<abbr title="This field is mandatory">*</abbr></label>
			<input id="address" class="input" name="address"
				placeholder="Address" type="text" required
				title="Please fill out this field"
				oninvalid="setCustomValidity('Please insert your Address')"
				oninput="setCustomValidity('')" /> <label id="emailLabel"
				for="email">Email<abbr title="This field is mandatory">*</abbr></label><input
				id="email" class="input" name="email" placeholder="Email"
				type="email" required title="Please fill out this field"
				oninvalid="setCustomValidity('Please insert valid email')"
				oninput="setCustomValidity('')" /> <label id="usernameLabel"
				for="user">Username<abbr title="This field is mandatory">*</abbr></label><input
				id="username" class="input" name="username" placeholder="Username"
				type="text" required title="Please fill out this field"
				oninvalid="setCustomValidity('Please insert your Username')"
				oninput="setCustomValidity('')" /><label id="passwordLabel"
				for="pass">Password<abbr title="This field is mandatory">*</abbr></label>
			<input id="password" class="input" name="password"
				placeholder="Password" type="password" required
				title="Please fill out this field"
				oninvalid="setCustomValidity('Please insert your Password')"
				oninput="setCustomValidity('')" /> <label
				id="repeatedPasswordLabel" for="repeatedPassword">Repeat
				Password<abbr title="This field is mandatory">*</abbr>
			</label> <input id="repeatedPassword" class="input" name="repeatedPassword"
				placeholder="Repeat Password" type="password" required
				title="Please fill out this field"
				oninvalid="setCustomValidity('Please repeat your Password')"
				oninput="setCustomValidity('')" /> <input type="submit"
				class="loginBtn" id="confirmBtn" value="Ok" />
			<%
				if (request.getAttribute("errorMessage") != null) {
			%>
			<label class="error" id="error"><%=request.getAttribute("errorMessage")%></label>
			<%
				}
			%>
		</form>
	</div>


</body>
</html>
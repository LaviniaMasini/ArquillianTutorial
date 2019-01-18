<!DOCTYPE html>
<html>
<head>
<title>Update your credentials</title>
<link rel="stylesheet" type="text/css" href="css/form.css" />
<style>
h3 {
	text-align: center;
}
</style>
</head>
<body>

	<jsp:include page="menu.jsp" />
	<h3>Update your credentials</h3>
	<div class="form">
		<form name="credential" id="credentials" method="post"
			action="update_credentials">
			<label id="firstnameLabel" for="firstname">Firstname<abbr title="This field is mandatory">*</abbr></label> <input
				id="firstname" class="input" name="firstname" type="text"
				value=<%=request.getAttribute("firstname")%> required title= "Please fill out this field"
				oninvalid="setCustomValidity('Please insert valid Firstname')"
				oninput="setCustomValidity('')"  />
			<label id="lastnameLabel" for="lastname">Lastname<abbr title="This field is mandatory">*</abbr></label><input
				id="lastname" class="input" name="lastname" type="text"
				value=<%=request.getAttribute("lastname")%> required title= "Please fill out this field"
				oninvalid="setCustomValidity('Please insert valid Lastname')"
				oninput="setCustomValidity('')"  />
			<label id="addressLabel" for="address">Address<abbr title="This field is mandatory">*</abbr></label> <input
				id="address" class="input" name="address" type="text"
				value=<%=request.getAttribute("address")%> required title= "Please fill out this field"
				oninvalid="setCustomValidity('Please insert valid Address')"
				oninput="setCustomValidity('')"  />
			 <label
				id="emailLabel" for="email">Email<abbr title="This field is mandatory">*</abbr></label> <input id="email"
				class="input" name="email" type="email"
				value=<%=request.getAttribute("email")%> required title= "Please fill out this field"
				oninvalid="setCustomValidity('Please insert valid Email')"
				oninput="setCustomValidity('')"  /> 
			<input
				class="loginBtn" type="submit" id="confirmBtn" value="Ok" />
		</form>
	</div>
</body>
</html>

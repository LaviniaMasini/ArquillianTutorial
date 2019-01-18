<%@page import="java.time.LocalDate"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<title>Order Review</title>
<link rel="stylesheet" type="text/css" href="css/orderReview.css" />
</head>
<body>

	<jsp:include page="menu.jsp" />
	<h3 class="title">Order Review</h3>
	<div class="container">
		<div class="date">
			<h4><%=LocalDate.now()%></h4>
		</div>
		<hr>
		<div>
			<div>
				<h4>Client Information</h4>
				<p id="firstname"><%=request.getAttribute("firstname")%></p>
				<p id="lastname"><%=request.getAttribute("lastname")%></p>
				<p id="address"><%=request.getAttribute("address")%></p>
				<p id="email"><%=request.getAttribute("email")%></p>
			</div>
		</div>
		<hr>
		<div>
			<table class="table">
				<tr>
					<td id="name"><%=request.getAttribute("name")%></td>
					<td id="description"><%=request.getAttribute("description")%></td>
					<td id="category"><%=request.getAttribute("category")%></td>
					<td id="price"><%=request.getAttribute("price")%>€</td>
				</tr>
			</table>
			<br> <br>
		</div>
	</div>
</body>
</html>

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html>
<head>
<title>Home Page</title>
<link rel="stylesheet" type="text/css" href="css/card.css" />
</head>
<body>
	<jsp:include page="menu.jsp" />
	<%
		if (request.getAttribute("errorMessage") != null) {
	%>
	<label class="error" id="error"><%=request.getAttribute("errorMessage")%>
	</label>
	<%
		}
	%>
	<div class="card-columns">
		<c:forEach var="item" items="${list}">
			<div class="card" style="width: 400px">
				<div class="card-body">
					<h2 class="card-title">${item.name}</h2>
					<h3 class="price">${item.price}€</h3>
					<p class="card-text">${item.description}</p>
					<form id="form-1" action="homepage" method="post">
						<input id="input-1" name="id" type="text" hidden="true"
							value="${item.id}" />
						<button id="ordersBtn">Add to Order</button>
					</form>
				</div>
			</div>
		</c:forEach>
	</div>

</body>
</html>

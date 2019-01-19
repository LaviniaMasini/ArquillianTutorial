package arquillian.tutorial.servlet;

import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

import org.junit.Test;
import org.mockito.ArgumentMatcher;
import org.mockito.Mock;
import org.springframework.test.util.ReflectionTestUtils;

import arquillian.tutorial.entity.Orders;
import arquillian.tutorial.entity.Products;
import arquillian.tutorial.entity.Users;
import arquillian.tutorial.service.OrdersService;
import arquillian.tutorial.service.ProductsService;
import arquillian.tutorial.service.UsersService;

public class HomePageServletTest extends AbstractServletTestHelper {

	@Mock
	private ProductsService productsService;

	@Mock
	private UsersService usersService;

	@Mock
	private OrdersService ordersService;


	@Test
	public void testDoGet() throws ServletException, IOException {
		List<Products> productsList = new ArrayList<>();
		productsList.add(new Products("name", "description", "category", 1.0));
		when(productsService.findAllProducts()).thenReturn(productsList);
		when(request.getRequestDispatcher("index.jsp")).thenReturn(requestDispatcher);
		new HomePageServlet(productsService, usersService, ordersService).doGet(request, response);
		verify(productsService).findAllProducts();
		verify(request).setAttribute("list", productsList);
		verify(requestDispatcher).include(request, response);
	}

	@Test
	public void testDoPostWhenSessionIsNull() throws ServletException, IOException {
		when(request.getSession()).thenReturn(session);
		when(session.getAttribute("username")).thenReturn(null);
		when(request.getRequestDispatcher("loginPage.jsp")).thenReturn(requestDispatcher);
		new HomePageServlet(productsService, usersService, ordersService).doPost(request, response);
		verify(requestDispatcher).include(request, response);
	}

	@Test
	public void testDoPostWhenSessionIsNotNullAndAddIsOk() throws ServletException, IOException {
		String username = "username";
		String password = "password";
		setSession(username, password);
		Products p = setProductAttribute();
		Users u = new Users("firstname", "lastname", "address", "email@email.com", username, password);
		when(usersService.findUserByUsernameAndPassword(username, password)).thenReturn(u);
		when(ordersService.addOrder(argThat(new ArgumentMatcher<Orders>() {
			@Override
			public boolean matches(Orders argument) {
				Orders orders = argument;
				return orders.getOrderDate().equals(LocalDate.now()) && orders.getProduct().equals(p)
						&& orders.getUser().equals(u);
			}
		}))).thenReturn(true);
		when(request.getRequestDispatcher("orderReview.jsp")).thenReturn(requestDispatcher);
		new HomePageServlet(productsService, usersService, ordersService).doPost(request, response);
		verify(productsService).findProductById(1);
		verifySetProductAttributes(request, 1, "name1", "description1", "category1", 1.0);
		verifySetUserAttributes(request, "firstname", "lastname", "address", "email@email.com");
		verify(usersService).findUserByUsernameAndPassword(username, password);
		verify(ordersService).addOrder(any(Orders.class));
		verify(requestDispatcher).forward(request, response);
	}

	@Test
	public void testDoPostWhenSessionIsNotNullAndAddIsNotOk() throws ServletException, IOException {
		String username = "username";
		String password = "password";
		setSession(username, password);
		setProductAttribute();
		Users u = new Users("firstname", "lastname", "address", "email@email.com", username, password);
		when(usersService.findUserByUsernameAndPassword(username, password)).thenReturn(u);
		when(ordersService.addOrder(any(Orders.class))).thenReturn(false);
		when(request.getRequestDispatcher("index.jsp")).thenReturn(requestDispatcher);
		new HomePageServlet(productsService, usersService, ordersService).doPost(request, response);
		verify(productsService).findProductById(1);
		verifySetProductAttributes(request, 1, "name1", "description1", "category1", 1.0);
		verifySetUserAttributes(request, "firstname", "lastname", "address", "email@email.com");
		verify(usersService).findUserByUsernameAndPassword(username, password);
		verify(ordersService).addOrder(any(Orders.class));
		verify(request).setAttribute("errorMessage", "There was an error during the order addition");
		verify(requestDispatcher).forward(request, response);
	}

	private void verifySetProductAttributes(HttpServletRequest request, int id, String name, String description,
			String category, double price) {
		verify(request).setAttribute("id", id);
		verify(request).setAttribute("name", name);
		verify(request).setAttribute("category", category);
		verify(request).setAttribute("description", description);
		verify(request).setAttribute("price", price);
	}

	
	private Products setProductAttribute() {
		Products p = new Products("name1", "description1", "category1", 1.0);
		String idStr = "1";
		when(request.getParameter("id")).thenReturn(idStr);
		ReflectionTestUtils.setField(p, "id", 1);
		int id = Integer.parseInt(idStr);
		when(productsService.findProductById(id)).thenReturn(p);
		return p;
	}

}

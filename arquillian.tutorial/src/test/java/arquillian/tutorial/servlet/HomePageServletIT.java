package arquillian.tutorial.servlet;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.util.ReflectionTestUtils;

import arquillian.tutorial.database.IOrdersDatabase;
import arquillian.tutorial.database.IProductsDatabase;
import arquillian.tutorial.database.IUsersDatabase;
import arquillian.tutorial.entity.Orders;
import arquillian.tutorial.entity.Products;
import arquillian.tutorial.entity.Users;
import arquillian.tutorial.exception.DatabaseException;
import arquillian.tutorial.service.IOrdersService;
import arquillian.tutorial.service.IProductsService;
import arquillian.tutorial.service.IUsersService;
import arquillian.tutorial.service.OrdersService;
import arquillian.tutorial.service.ProductsService;
import arquillian.tutorial.service.UsersService;

public class HomePageServletIT {

	@Mock
	private HttpServletRequest request;

	@Mock
	private HttpServletResponse response;

	@Mock
	private RequestDispatcher requestDispatcher;

	@Mock
	private HttpSession session;

	@Mock
	private IProductsDatabase productsDatabase;

	@Mock
	private IUsersDatabase usersDatabase;

	@Mock
	private IOrdersDatabase ordersDatabase;

	private IUsersService usersService;

	private IProductsService productsService;

	private IOrdersService ordersService;

	@Before
	public void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);
		usersService = new UsersService();
		productsService = new ProductsService();
		ordersService = new OrdersService();
		usersService.setUsersService(usersDatabase);
		productsService.setProductsDatabase(productsDatabase);
		ordersService.setOrdersDatabase(ordersDatabase);
	}

	@Test
	public void testDoGet() throws ServletException, IOException {
		List<Products> productsList = new ArrayList<>();
		productsList.add(new Products("name", "description", "category", 1.0));
		when(productsDatabase.getAllProducts()).thenReturn(productsList);
		when(request.getRequestDispatcher("index.jsp")).thenReturn(requestDispatcher);
		new HomePageServlet(productsService, usersService, ordersService).doGet(request, response);
		verify(productsDatabase).getAllProducts();
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
	public void testDoPostWhenSessionIsNotNullAndAddIsOk() throws DatabaseException, ServletException, IOException {
		String username = "username";
		String password = "password";
		setSession(username, password);
		setProduct("1", "name1", "description1", "category1", 1.0);
		Users u = new Users("firstname", "lastname", "address", "email@email.com", username, password);
		when(usersDatabase.getUserByUsernameAndPassword(username, password)).thenReturn(u);
		when(request.getRequestDispatcher("orderReview.jsp")).thenReturn(requestDispatcher);
		new HomePageServlet(productsService, usersService, ordersService).doPost(request, response);
		verify(productsDatabase).getProductById(1);
		verifySetProductAttributes(request, 1, "name1", "description1", "category1", 1.0);
		verifySetUserAttributes(request, "firstname", "lastname", "address", "email@email.com");
		verify(usersDatabase).getUserByUsernameAndPassword(username, password);
		verify(ordersDatabase).add(any(Orders.class));
		verify(requestDispatcher).forward(request, response);

	}

	@Test
	public void testDoPostWhenSessionIsNotNullAndAddIsNotOk() throws DatabaseException, ServletException, IOException {
		String username = "username";
		String password = "password";
		setSession(username, password);
		setProduct("1", "name1", "description1", "category1", 1.0);
		Users u = new Users("firstname", "lastname", "address", "email@email.com", username, password);
		doThrow(new DatabaseException()).when(ordersDatabase).add(any(Orders.class));
		when(usersDatabase.getUserByUsernameAndPassword(username, password)).thenReturn(u);
		when(request.getRequestDispatcher("index.jsp")).thenReturn(requestDispatcher);
		new HomePageServlet(productsService, usersService, ordersService).doPost(request, response);
		verify(productsDatabase).getProductById(1);
		verifySetProductAttributes(request, 1, "name1", "description1", "category1", 1.0);
		verifySetUserAttributes(request, "firstname", "lastname", "address", "email@email.com");
		verify(usersDatabase).getUserByUsernameAndPassword(username, password);
		verify(ordersDatabase).add(any(Orders.class));
		verify(request).setAttribute("errorMessage", "There was an error during the order addition");
		verify(requestDispatcher).forward(request, response);

	}

	private void setSession(String username, String password) {
		when(request.getSession()).thenReturn(session);
		when(session.getAttribute("username")).thenReturn(username);
		when(session.getAttribute("password")).thenReturn(password);
	}

	private void verifySetProductAttributes(HttpServletRequest request, int id, String name, String description,
			String category, double price) {
		verify(request).setAttribute("id", id);
		verify(request).setAttribute("name", name);
		verify(request).setAttribute("category", category);
		verify(request).setAttribute("description", description);
		verify(request).setAttribute("price", price);
	}

	private void verifySetUserAttributes(HttpServletRequest request, String firstname, String lastname, String address,
			String email) {
		verify(request).setAttribute("firstname", firstname);
		verify(request).setAttribute("lastname", lastname);
		verify(request).setAttribute("address", address);
		verify(request).setAttribute("email", email);

	}

	private void setProduct(String idStr, String name, String description, String category, double price) {
		when(request.getParameter("id")).thenReturn(idStr);
		Products p = new Products(name, description, category, price);
		ReflectionTestUtils.setField(p, "id", 1);
		int id = Integer.parseInt(idStr);
		when(productsDatabase.getProductById(id)).thenReturn(p);
	}

}

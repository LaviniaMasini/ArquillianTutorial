package arquillian.tutorial.servlet;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import arquillian.tutorial.database.IUsersDatabase;
import arquillian.tutorial.entity.Users;
import arquillian.tutorial.exception.DatabaseException;
import arquillian.tutorial.service.UsersService;

public class LoginServletIT {
	
	@Mock
	private IUsersDatabase usersDatabase;
	
	private UsersService usersService;
	
	@Mock
	private HttpServletRequest request;

	@Mock
	private HttpServletResponse response;

	@Mock
	private RequestDispatcher requestDispatcher;

	@Mock
	private HttpSession session;
	
	@Before
	public void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);
		usersService = new UsersService();
		usersService.setUsersService(usersDatabase);

	}

	@Test
	public void testDoGetWithoutSession() throws ServletException, IOException {
		when(request.getSession()).thenReturn(session);
		when(request.getSession().getAttribute("username")).thenReturn(null);
		when(request.getRequestDispatcher("loginPage.jsp")).thenReturn(requestDispatcher);
		new LoginServlet(usersService).doGet(request, response);
		verify(requestDispatcher).forward(request, response);
	}
	
	@Test
	public void testDoGetWithSession() throws ServletException, IOException {
		when(request.getSession()).thenReturn(session);
		when(request.getSession().getAttribute("username")).thenReturn("user");
		when(request.getRequestDispatcher("userSummary.jsp")).thenReturn(requestDispatcher);
		new LoginServlet(usersService).doGet(request, response);
		verify(requestDispatcher).forward(request, response);
	}
	
	@Test
	public void testDoPostWithUser() throws ServletException, IOException, DatabaseException{
		String username = "username1";
		String password = "password1";
		when(request.getParameter("username")).thenReturn(username);
		when(request.getParameter("password")).thenReturn(password);
		when(usersDatabase.getUserByUsernameAndPassword(username, password)).thenReturn(new Users());
		when(request.getSession()).thenReturn(session);
		when(request.getRequestDispatcher("userSummary.jsp")).thenReturn(requestDispatcher);
		new LoginServlet(usersService).doPost(request, response);
		verify(session).setAttribute("username", username);
		verify(session).setAttribute("password", password);
		verify(usersDatabase).getUserByUsernameAndPassword(username, password);
		verify(requestDispatcher).include(request, response);
	}

	
	@Test
	public void testDoPostWithoutUser() throws ServletException, IOException, DatabaseException{
		String username = "username1";
		String password = "password1";
		when(request.getParameter("username")).thenReturn(username);
		when(request.getParameter("password")).thenReturn(password);
		when(usersDatabase.getUserByUsernameAndPassword(username, password)).thenThrow(new DatabaseException());
		when(request.getRequestDispatcher("loginPage.jsp")).thenReturn(requestDispatcher);
		new LoginServlet(usersService).doPost(request, response);
		verify(usersDatabase).getUserByUsernameAndPassword(username, password);
		verify(request).setAttribute("errorMessage", "Invalid username or password");
		verify(requestDispatcher).include(request, response);
	}

}

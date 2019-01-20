package arquillian.tutorial.servlet;

import static org.mockito.Mockito.*;

import java.io.IOException;

import javax.servlet.ServletException;

import org.junit.Test;
import org.mockito.Mock;

import arquillian.tutorial.entity.Users;
import arquillian.tutorial.service.IUsersService;

public class LoginServletTest extends AbstractServletTestHelper {

	@Mock
	private IUsersService usersService;

	@Test
	public void testDoGetWithoutSession() throws ServletException, IOException {
		when(request.getSession()).thenReturn(session);
		when(session.getAttribute("username")).thenReturn(null);
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
	public void testDoPostWithUser() throws ServletException, IOException {
		String username = "username1";
		String password = "password1";
		when(request.getParameter("username")).thenReturn(username);
		when(request.getParameter("password")).thenReturn(password);
		when(usersService.findUserByUsernameAndPassword(username, password)).thenReturn(new Users());
		when(request.getSession()).thenReturn(session);
		when(request.getRequestDispatcher("userSummary.jsp")).thenReturn(requestDispatcher);
		new LoginServlet(usersService).doPost(request, response);
		verify(session).setAttribute("username", username);
		verify(session).setAttribute("password", password);
		verify(usersService).findUserByUsernameAndPassword(username, password);
		verify(requestDispatcher).include(request, response);
	}

	@Test
	public void testDoPostWithoutUser() throws ServletException, IOException {
		String username = "username1";
		String password = "password1";
		when(request.getParameter("username")).thenReturn(username);
		when(request.getParameter("password")).thenReturn(password);
		when(usersService.findUserByUsernameAndPassword(username, password)).thenReturn(null);
		when(request.getRequestDispatcher("loginPage.jsp")).thenReturn(requestDispatcher);
		new LoginServlet(usersService).doPost(request, response);
		verify(usersService).findUserByUsernameAndPassword(username, password);
		verify(request).setAttribute("errorMessage", "Invalid username or password");
		verify(requestDispatcher).include(request, response);
	}

}

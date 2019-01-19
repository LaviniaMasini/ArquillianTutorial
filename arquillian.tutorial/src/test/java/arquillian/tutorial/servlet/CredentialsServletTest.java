package arquillian.tutorial.servlet;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

import java.io.IOException;

import javax.servlet.ServletException;

import org.junit.Test;
import org.mockito.Mock;

import arquillian.tutorial.entity.Users;
import arquillian.tutorial.service.UsersService;

public class CredentialsServletTest extends AbstractServletTestHelper {

	@Mock
	private UsersService usersService;

	@Test
	public void testDoGetWhenSessionIsNotNull() throws ServletException, IOException {
		String username = "username1";
		String password = "password1";
		String firstname = "firstname1";
		String lastname = "lastname1";
		String address = "address1";
		String email = "email@email.com";
		when(request.getRequestDispatcher("userInfo.jsp")).thenReturn(requestDispatcher);
		setSession(username, password);
		Users u = new Users(firstname, lastname, address, email, username, password);
		when(usersService.findUserByUsernameAndPassword(username, password)).thenReturn(u);
		new CredentialsServlet(usersService).doGet(request, response);
		verifySetUserAttributes(request, u.getFirstname(), u.getLastname(), u.getAddress(), u.getEmail());
		verify(requestDispatcher).forward(request, response);

	}

	@Test
	public void testDoGetWhenSessionIsNull() throws ServletException, IOException {
		when(request.getSession()).thenReturn(session);
		when(session.getAttribute("username")).thenReturn(null);
		when(request.getRequestDispatcher("loginPage.jsp")).thenReturn(requestDispatcher);
		new CredentialsServlet(usersService).doGet(request, response);
		verify(requestDispatcher).forward(request, response);

	}

	@Test
	public void testDoPost() throws ServletException, IOException {
		String username = "username";
		String password = "password";
		String firstname = "firstname1";
		String lastname = "lastname1";
		String address = "address1";
		String email = "email@email.com";
		Users u = spy(new Users("firstname", "lastname", "address", "email", username, password));
		setSession(username, password);
		when(request.getParameter("firstname")).thenReturn(firstname);
		when(request.getParameter("lastname")).thenReturn(lastname);
		when(request.getParameter("address")).thenReturn(address);
		when(request.getParameter("email")).thenReturn(email);
		when(request.getRequestDispatcher("userInfo.jsp")).thenReturn(requestDispatcher);
		when(usersService.findUserByUsernameAndPassword(username, password)).thenReturn(u);
		when(usersService.updateUser(u)).thenReturn(true);
		new CredentialsServlet(usersService).doPost(request, response);
		assertUser(firstname, lastname, address, email, u);
		verify(usersService).findUserByUsernameAndPassword(username, password);
		verify(usersService).updateUser(u);
		verifySetUserAttributes(request, firstname, lastname, address, email);
		verify(requestDispatcher).include(request, response);
	}

	private void assertUser(String firstname, String lastname, String address, String email, Users u) {
		assertEquals(u.getFirstname(), firstname);
		assertEquals(u.getLastname(), lastname);
		assertEquals(u.getAddress(), address);
		assertEquals(u.getEmail(), email);
	}

}

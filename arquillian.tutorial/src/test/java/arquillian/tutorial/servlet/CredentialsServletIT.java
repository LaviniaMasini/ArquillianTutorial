package arquillian.tutorial.servlet;

import static org.junit.Assert.*;
import static org.mockito.Mockito.spy;
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
import arquillian.tutorial.service.IUsersService;
import arquillian.tutorial.service.UsersService;

public class CredentialsServletIT {

	@Mock
	private IUsersDatabase usersDatabase;

	private IUsersService usersService;

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
	public void testDoGetWhenSessionIsNotNull() throws ServletException, IOException, DatabaseException {
		String username = "username1";
		String password = "password1";
		String firstname = "firstname1";
		String lastname = "lastname1";
		String address = "address1";
		String email = "email@email.com";
		when(request.getRequestDispatcher("userInfo.jsp")).thenReturn(requestDispatcher);
		setSession(username, password);
		Users u = new Users(firstname, lastname, address, email, username, password);
		when(usersDatabase.getUserByUsernameAndPassword(username, password)).thenReturn(u);
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
	public void testDoPost() throws ServletException, IOException, DatabaseException {
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
		when(usersDatabase.getUserByUsernameAndPassword(username, password)).thenReturn(u);
		new CredentialsServlet(usersService).doPost(request, response);
		assertUser(firstname, lastname, address, email, u);
		verify(usersDatabase).update(u);
		verifySetUserAttributes(request, firstname, lastname, address, email);
		verify(requestDispatcher).include(request, response);
	}

	private void verifySetUserAttributes(HttpServletRequest request, String firstname, String lastname, String address,
			String email) {
		verify(request).setAttribute("firstname", firstname);
		verify(request).setAttribute("lastname", lastname);
		verify(request).setAttribute("address", address);
		verify(request).setAttribute("email", email);

	}
	
	private void setSession(String username, String password) {
		when(request.getSession()).thenReturn(session);
		when(session.getAttribute("username")).thenReturn(username);
		when(session.getAttribute("password")).thenReturn(password);
	}
	
	private void assertUser(String firstname, String lastname, String address, String email, Users u) {
		assertEquals(u.getFirstname(), firstname);
		assertEquals(u.getLastname(), lastname);
		assertEquals(u.getAddress(), address);
		assertEquals(u.getEmail(), email);
	}

}

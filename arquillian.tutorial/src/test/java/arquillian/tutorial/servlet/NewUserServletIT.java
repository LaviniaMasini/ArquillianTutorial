package arquillian.tutorial.servlet;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

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

public class NewUserServletIT {

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
	public void testDoGetWhenUserIsNull() throws ServletException, IOException {
		when(request.getRequestDispatcher("registrationForm.jsp")).thenReturn(requestDispatcher);
		when(request.getSession()).thenReturn(session);
		when(session.getAttribute("username")).thenReturn(null);
		new NewUserServlet(usersService).doGet(request, response);
		verify(requestDispatcher).forward(request, response);
	}

	@Test
	public void testDoGetWhenUserIsNotNull() throws ServletException, IOException {
		when(request.getRequestDispatcher("userSummary.jsp")).thenReturn(requestDispatcher);
		when(request.getSession()).thenReturn(session);
		when(session.getAttribute("username")).thenReturn("username1");
		new NewUserServlet(usersService).doGet(request, response);
		verify(requestDispatcher).forward(request, response);

	}

	@Test
	public void testDoPostwhenPasswordIsDifferentFromRepeatedPassword() throws ServletException, IOException {
		String firstname = "firstname1";
		String lastname = "lastname1";
		String address = "address1";
		String email = "email1";
		String username = "username1";
		String password = "password1";
		String repeatedPassword = "password2";
		setUserParameters(firstname, lastname, address, email, username, password, repeatedPassword);
		when(request.getRequestDispatcher("registrationForm.jsp")).thenReturn(requestDispatcher);
		new NewUserServlet(usersService).doPost(request, response);
		verify(request).setAttribute("errorMessage", "The password and the repeated password must be the same");
		verify(requestDispatcher).forward(request, response);
	}

	@Test
	public void testDoPostWhenUsernameAndPasswordAreAlreadyInDB()
			throws ServletException, IOException, DatabaseException {
		String firstname = "firstname1";
		String lastname = "lastname1";
		String address = "address1";
		String email = "email1";
		String username = "username1";
		String password = "password1";
		String repeatedPassword = "password1";
		setUserParameters(firstname, lastname, address, email, username, password, repeatedPassword);
		when(usersDatabase.getUserByUsernameAndPassword(username, password)).thenReturn(new Users());
		when(request.getRequestDispatcher("registrationForm.jsp")).thenReturn(requestDispatcher);
		new NewUserServlet(usersService).doPost(request, response);
		verify(usersDatabase).getUserByUsernameAndPassword(username, password);
		verify(requestDispatcher).forward(request, response);
		verify(request).setAttribute("errorMessage", "These username and password are already present");
	}
	
	@Test
	public void testDoPostWhenCredentialsAreCorrectAndInsertIsOk() throws ServletException, IOException, DatabaseException {
		String firstname = "firstname1";
		String lastname = "lastname1";
		String address = "address1";
		String email = "email1";
		String username = "username1";
		String password = "password1";
		setUserParameters(firstname, lastname, address, email, username, password, password);
		when(request.getSession()).thenReturn(session);
		when(request.getRequestDispatcher("userSummary.jsp")).thenReturn(requestDispatcher);
		when(usersDatabase.getUserByUsernameAndPassword(username, password)).thenThrow(new DatabaseException());
		new NewUserServlet(usersService).doPost(request, response);
		verify(usersDatabase).add(any(Users.class));
		verify(session).setAttribute("username", username);
		verify(session).setAttribute("password", password);
		verify(requestDispatcher).include(request, response);
	}
	
	@Test
	public void testDoPostWhenCredentialsAreCorrectAndInsertIsNotOk() throws ServletException, IOException, DatabaseException {
		String firstname = "firstname1";
		String lastname = "lastname1";
		String address = "address1";
		String email = "email1";
		String username = "username1";
		String password = "password1";
		setUserParameters(firstname, lastname, address, email, username, password, password);
		when(request.getSession()).thenReturn(session);
		when(request.getRequestDispatcher("registrationForm.jsp")).thenReturn(requestDispatcher);
		when(usersDatabase.getUserByUsernameAndPassword(username, password)).thenThrow(new DatabaseException());
		doThrow(new DatabaseException("Error during user addition...")).when(usersDatabase).add(any(Users.class));
		new NewUserServlet(usersService).doPost(request, response);
		verify(usersDatabase).add(any(Users.class));
		verify(request).setAttribute("errorMessage", "There was a problem during user addition");
		verify(requestDispatcher).forward(request, response);
	}

	private void setUserParameters(String firstname, String lastname, String address, String email, String username,
			String password, String repeatedPassword) {
		when(request.getParameter("firstname")).thenReturn(firstname);
		when(request.getParameter("lastname")).thenReturn(lastname);
		when(request.getParameter("address")).thenReturn(address);
		when(request.getParameter("email")).thenReturn(email);
		when(request.getParameter("username")).thenReturn(username);
		when(request.getParameter("password")).thenReturn(password);
		when(request.getParameter("repeatedPassword")).thenReturn(repeatedPassword);
	}

}

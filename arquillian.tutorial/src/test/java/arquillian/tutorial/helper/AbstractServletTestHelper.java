package arquillian.tutorial.helper;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.mockito.Mock;

public class AbstractServletTestHelper {
	
	@Mock
	protected HttpServletRequest request;

	@Mock
	protected HttpServletResponse response;

	@Mock
	protected RequestDispatcher requestDispatcher;

	@Mock
	protected HttpSession session;

	public AbstractServletTestHelper() {
		super();
	}

	protected void setSession(String username, String password) {
		when(request.getSession()).thenReturn(session);
		when(session.getAttribute("username")).thenReturn(username);
		when(session.getAttribute("password")).thenReturn(password);
	}
	
	protected void verifySetUserAttributes(HttpServletRequest request, String firstname, String lastname, String address,
			String email) {
		verify(request).setAttribute("firstname", firstname);
		verify(request).setAttribute("lastname", lastname);
		verify(request).setAttribute("address", address);
		verify(request).setAttribute("email", email);

	}

}
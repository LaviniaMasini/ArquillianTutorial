package arquillian.tutorial.servlet;

import static org.mockito.Mockito.*;

import java.io.IOException;

import javax.servlet.ServletException;

import org.junit.Test;


public class LogoutServletTest extends AbstractServletTestHelper{

	@Test
	public void testDoGetWhenSessionIsNotNull() throws ServletException, IOException{
		when(request.getSession()).thenReturn(session);
		when(request.getRequestDispatcher("loginPage.jsp")).thenReturn(requestDispatcher);
		new LogoutServlet().doGet(request, response);
		verify(session, times(1)).invalidate();
		verify(requestDispatcher).include(request, response);

	}

	@Test
	public void testDoGetWhenSessionIsNull() throws ServletException, IOException{
		when(request.getSession()).thenReturn(null);
		when(request.getRequestDispatcher("loginPage.jsp")).thenReturn(requestDispatcher);
		new LogoutServlet().doGet(request, response);
		verify(requestDispatcher).include(request, response);

	}

}

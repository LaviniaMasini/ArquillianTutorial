package arquillian.tutorial.servlet;

import java.io.IOException;

import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import arquillian.tutorial.service.UsersService;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {
	private static final String USERNAME_ATTR = "username";

	private static final long serialVersionUID = 1L;

	private final UsersService usersService;

	@Inject
	public LoginServlet(UsersService usersService) {
		super();
		this.usersService = usersService;
	}

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		HttpSession session = request.getSession();
		if (session.getAttribute(USERNAME_ATTR) == null) {
			request.getRequestDispatcher("loginPage.jsp").forward(request, response);
		} else {
			request.getRequestDispatcher("userSummary.jsp").forward(request, response);

		}
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String username = request.getParameter(USERNAME_ATTR);
		String password = request.getParameter("password");
		if (usersService.findUserByUsernameAndPassword(username, password) != null) {
			HttpSession session = request.getSession();
			setSession(username, password, session);
			request.getRequestDispatcher("userSummary.jsp").include(request, response);
		} else {
			request.setAttribute("errorMessage", "Invalid username or password");
			request.getRequestDispatcher("loginPage.jsp").include(request, response);
		}
	}
	
	public void setSession(String username, String password, HttpSession session) {
		session.setAttribute(USERNAME_ATTR, username);
		session.setAttribute("password", password);
	}

}

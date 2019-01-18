package arquillian.tutorial.servlet;

import java.io.IOException;

import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import arquillian.tutorial.entity.Users;
import arquillian.tutorial.service.UsersService;

/**
 * Servlet implementation class NewUserServlet
 */
@WebServlet("/registration")
public class NewUserServlet extends HttpServlet {
	private static final String ERROR_MESSAGE = "errorMessage";

	private static final String REGISTRATION_FORM_JSP = "registrationForm.jsp";

	private static final String USERNAME_ATTR = "username";

	private static final long serialVersionUID = 1L;

	private final UsersService usersService;

	@Inject
	public NewUserServlet(UsersService usersService) {
		super();
		this.usersService = usersService;
	}

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		HttpSession session = request.getSession();
		if (session.getAttribute(USERNAME_ATTR) == null) {
			request.getRequestDispatcher(REGISTRATION_FORM_JSP).forward(request, response);
		} else {
			request.getRequestDispatcher("userSummary.jsp").forward(request, response);
		}

	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String firstname = request.getParameter("firstname");
		String lastname = request.getParameter("lastname");
		String address = request.getParameter("address");
		String email = request.getParameter("email");
		String username = request.getParameter(USERNAME_ATTR);
		String password = request.getParameter("password");
		String repeatedPassword = request.getParameter("repeatedPassword");
		if (!(password.equals(repeatedPassword))) {
			request.setAttribute(ERROR_MESSAGE, "The password and the repeated password must be the same");
			request.getRequestDispatcher(REGISTRATION_FORM_JSP).forward(request, response);
		} else if (usersService.findUserByUsernameAndPassword(username, password) != null) {
			request.setAttribute(ERROR_MESSAGE, "These username and password are already present");
			request.getRequestDispatcher(REGISTRATION_FORM_JSP).forward(request, response);
		} else {
			if(usersService.addUser(new Users(firstname, lastname, address, email, username, password))) {
				HttpSession session = request.getSession();
				session.setAttribute(USERNAME_ATTR, username);
				session.setAttribute("password", password);				
				request.getRequestDispatcher("userSummary.jsp").include(request, response);
			} else {
				request.setAttribute(ERROR_MESSAGE, "There was a problem during user addition");
				request.getRequestDispatcher(REGISTRATION_FORM_JSP).forward(request, response);

			}
		}

	}

}

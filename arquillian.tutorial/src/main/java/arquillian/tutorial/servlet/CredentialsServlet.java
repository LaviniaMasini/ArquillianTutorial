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

@WebServlet("/update_credentials")
public class CredentialsServlet extends HttpServlet {
	private static final String USERNAME_ATTR = "username";

	private static final long serialVersionUID = 1L;

	private final UsersService userService;

	@Inject
	public CredentialsServlet(UsersService usersService) {
		super();
		this.userService = usersService;
	}

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		HttpSession session = request.getSession();
		if (session.getAttribute(USERNAME_ATTR) != null) {
			String username = session.getAttribute(USERNAME_ATTR).toString();
			String password = session.getAttribute("password").toString();
			Users u = userService.findUserByUsernameAndPassword(username, password);
			setUserAttributes(request, u.getFirstname(), u.getLastname(), u.getAddress(), u.getEmail());
			request.getRequestDispatcher("userInfo.jsp").forward(request, response);
		} else {
			request.getRequestDispatcher("loginPage.jsp").forward(request, response);

		}
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		HttpSession session = request.getSession();
		String username = session.getAttribute(USERNAME_ATTR).toString();
		String password = session.getAttribute("password").toString();
		String firstname = request.getParameter("firstname");
		String lastname = request.getParameter("lastname");
		String address = request.getParameter("address");
		String email = request.getParameter("email");
		Users u = userService.findUserByUsernameAndPassword(username, password);
		updateUser(u, firstname, lastname, address, email);
		setUserAttributes(request, firstname, lastname, address, email);
		request.getRequestDispatcher("userInfo.jsp").include(request, response);

	}

	private void updateUser(Users u, String firstname, String lastname, String address, String email) {
		u.setFirstname(firstname);
		u.setLastname(lastname);
		u.setAddress(address);
		u.setEmail(email);
		userService.updateUser(u);
	}

	private static void setUserAttributes(HttpServletRequest request, String firstname, String lastname, String address,
			String email) {
		request.setAttribute("firstname", firstname);
		request.setAttribute("lastname", lastname);
		request.setAttribute("address", address);
		request.setAttribute("email", email);
	}

}

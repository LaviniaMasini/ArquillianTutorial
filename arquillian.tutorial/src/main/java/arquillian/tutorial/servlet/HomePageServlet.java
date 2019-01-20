package arquillian.tutorial.servlet;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import arquillian.tutorial.entity.Orders;
import arquillian.tutorial.entity.Products;
import arquillian.tutorial.entity.Users;
import arquillian.tutorial.service.IOrdersService;
import arquillian.tutorial.service.IProductsService;
import arquillian.tutorial.service.IUsersService;

@WebServlet(urlPatterns = { "/homepage", "" })
public class HomePageServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	private final IProductsService productsService;

	private final IUsersService usersService;

	private final IOrdersService ordersService;

	@Inject
	public HomePageServlet(IProductsService productsService, IUsersService usersService, IOrdersService ordersService) {
		super();
		this.productsService = productsService;
		this.usersService = usersService;
		this.ordersService = ordersService;
	}

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		List<Products> productsList = productsService.findAllProducts();
		request.setAttribute("list", productsList);
		request.getRequestDispatcher("index.jsp").include(request, response);
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		HttpSession session = request.getSession();
		if (session.getAttribute("username") == null) {
			request.getRequestDispatcher("loginPage.jsp").include(request, response);
		} else {
			String idStr = request.getParameter("id");
			int id = Integer.parseInt(idStr);
			Products p = productsService.findProductById(id);
			Users u = usersService.findUserByUsernameAndPassword(session.getAttribute("username").toString(),
					session.getAttribute("password").toString());
			setUserAttributes(request, u.getFirstname(), u.getLastname(), u.getAddress(), u.getEmail());
			setProductAttributes(request, p.getId(), p.getName(), p.getDescription(), p.getCategory(), p.getPrice());
			if (ordersService.addOrder(new Orders(LocalDate.now(), p, u))) {
				request.getRequestDispatcher("orderReview.jsp").forward(request, response);
			} else {
				request.setAttribute("errorMessage", "There was an error during the order addition");
				request.getRequestDispatcher("index.jsp").forward(request, response);
			}
		}
	}

	private static void setProductAttributes(HttpServletRequest request, int id, String name, String description,
			String category, double price) {
		request.setAttribute("id", id);
		request.setAttribute("name", name);
		request.setAttribute("category", category);
		request.setAttribute("description", description);
		request.setAttribute("price", price);
	}

	private static void setUserAttributes(HttpServletRequest request, String firstname, String lastname, String address,
			String email) {
		request.setAttribute("firstname", firstname);
		request.setAttribute("lastname", lastname);
		request.setAttribute("address", address);
		request.setAttribute("email", email);

	}

}

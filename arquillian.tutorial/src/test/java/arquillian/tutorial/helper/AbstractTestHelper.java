package arquillian.tutorial.helper;

import static org.junit.Assert.assertEquals;

import java.time.LocalDate;

import javax.annotation.Resource;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.HeuristicMixedException;
import javax.transaction.HeuristicRollbackException;
import javax.transaction.NotSupportedException;
import javax.transaction.RollbackException;
import javax.transaction.SystemException;
import javax.transaction.UserTransaction;

import arquillian.tutorial.entity.Orders;
import arquillian.tutorial.entity.Products;
import arquillian.tutorial.entity.Users;

public class AbstractTestHelper {

	public AbstractTestHelper() {
		super();
	}

	@PersistenceContext(name = "arquillian.tutorial")
	protected EntityManager entityManager;

	@Resource
	protected UserTransaction userTx;

	protected void assertOrder(Orders resultOrder, Products p, LocalDate date, Users u, int id) {
		assertEquals(id, resultOrder.getId());
		assertEquals(date, resultOrder.getOrderDate());
		assertProduct(p, resultOrder.getProduct().getName(), resultOrder.getProduct().getDescription(),
				resultOrder.getProduct().getCategory(), resultOrder.getProduct().getPrice(),
				resultOrder.getProduct().getId());
		assertUser(u, resultOrder.getUser().getId(), resultOrder.getUser().getFirstname(),
				resultOrder.getUser().getLastname(), resultOrder.getUser().getAddress(),
				resultOrder.getUser().getEmail(), resultOrder.getUser().getUsername(),
				resultOrder.getUser().getPassword());
	}

	protected void assertProduct(Products resultProduct, String name, String description, String category, double price,
			int id) {
		assertEquals(id, resultProduct.getId());
		assertEquals(name, resultProduct.getName());
		assertEquals(description, resultProduct.getDescription());
		assertEquals(category, resultProduct.getCategory());
		assertEquals(price, resultProduct.getPrice(), 0);
	}

	protected void assertUser(Users resultUser, int id, String firstname, String lastname, String address, String email,
			String username, String password) {
		assertEquals(id, resultUser.getId());
		assertEquals(firstname, resultUser.getFirstname());
		assertEquals(lastname, resultUser.getLastname());
		assertEquals(address, resultUser.getAddress());
		assertEquals(email, resultUser.getEmail());
		assertEquals(username, resultUser.getUsername());
		assertEquals(password, resultUser.getPassword());

	}

	protected void removeProduct(Products p) throws NotSupportedException, SystemException, RollbackException,
			HeuristicMixedException, HeuristicRollbackException {
		userTx.begin();
		p = entityManager.merge(p);
		entityManager.remove(p);
		userTx.commit();
	}

}
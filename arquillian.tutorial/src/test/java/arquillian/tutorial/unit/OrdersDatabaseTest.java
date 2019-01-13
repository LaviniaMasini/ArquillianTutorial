package arquillian.tutorial.unit;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.util.ReflectionTestUtils;

import arquillian.tutorial.database.OrdersDatabase;
import arquillian.tutorial.entity.Orders;
import arquillian.tutorial.entity.Products;
import arquillian.tutorial.entity.Users;
import arquillian.tutorial.exception.DatabaseException;
import arquillian.tutorial.helper.AbstractTestHelper;

public class OrdersDatabaseTest extends AbstractTestHelper {

	@Mock
	private EntityManager entityManager;

	@Mock
	private Query query;

	private OrdersDatabase ordersDatabase;

	private List<Orders> ordersList;

	private static final String queryString = "select o from Orders o";

	@Before
	public void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);
		ordersDatabase = new OrdersDatabase();
		ordersDatabase.setEntityManager(entityManager);
		ordersList = new ArrayList<>();
	}

	@Test
	public void testGetAllOrdersWhenDBIsEmpty() {
		when(entityManager.createQuery(queryString)).thenReturn(query);
		when(query.getResultList()).thenReturn(ordersList);
		List<Orders> ordersListResult = ordersDatabase.getAllOrders();
		verify(entityManager).createQuery(queryString);
		verify(query).getResultList();
		assertTrue(ordersListResult.isEmpty());
	}

	@Test
	public void testGetAllOrdersWhenDBContainsTwoOrders() {
		Products p = new Products("name1", "description1", "category1", 1.0);
		Users u = new Users("firstname1", "lastname1", "address1", "email1", "username1", "password1");
		Orders order = new Orders(LocalDate.now(), p, u);
		Products p2 = new Products("name2", "description2", "category2", 1.0);
		Users u2 = new Users("firstname2", "lastname2", "address2", "email2", "username2", "password2");
		Orders order2 = new Orders(LocalDate.now(), p2, u2);
		ordersList.add(order);
		ordersList.add(order2);
		when(entityManager.createQuery(queryString)).thenReturn(query);
		when(query.getResultList()).thenReturn(ordersList);
		List<Orders> ordersListResult = ordersDatabase.getAllOrders();
		verify(entityManager).createQuery(queryString);
		verify(query).getResultList();
		assertEquals(2, ordersListResult.size());

	}

	@Test
	public void testGetOrdersByIdWhenIdIsPresent() {
		Products p = new Products("name1", "description1", "category1", 1.0);
		Users u = new Users("firstname1", "lastname1", "address1", "email1", "username1", "password1");
		Orders order = new Orders(LocalDate.now(), p, u);
		int id = 1;
		ReflectionTestUtils.setField(order, "id", id);
		when(entityManager.find(Orders.class, id)).thenReturn(order);
		Orders resultOrder = ordersDatabase.getOrdersById(1);
		verify(entityManager, times(1)).find(Orders.class, id);
		assertOrder(resultOrder, p, LocalDate.now(), u, id);

	}

	@Test
	public void testGetOrdersByIdWhenIdIsNotPresent() {
		int id = 1;
		when(entityManager.find(Orders.class, id)).thenReturn(null);
		Orders resultOrder = ordersDatabase.getOrdersById(id);
		verify(entityManager, times(1)).find(Orders.class, id);
		assertNull(resultOrder);

	}

	@Test
	public void testAddUOrderWhenOrderIsNotPresent() throws DatabaseException {
		Products p = new Products("name1", "description1", "category1", 1.0);
		Users u = new Users("firstname1", "lastname1", "address1", "email1", "username1", "password1");
		Orders order = new Orders(LocalDate.now(), p, u);
		int id = 1;
		ReflectionTestUtils.setField(p, "id", id);
		ReflectionTestUtils.setField(u, "id", id);
		ordersDatabase.add(order);
		verify(entityManager, times(1)).persist(order);
	}

	@Test(expected = DatabaseException.class)
	public void testAddOrderWhenOrderIsAlreadyPresent() throws DatabaseException {
		Products p = new Products("name1", "description1", "category1", 1.0);
		Users u = new Users("firstname1", "lastname1", "address1", "email1", "username1", "password1");
		Orders order = new Orders(LocalDate.now(), p, u);
		IllegalStateException exc = new IllegalStateException();
		doThrow(exc).when(entityManager).persist(order);
		ordersDatabase.add(order);
	}

	@Test(expected = DatabaseException.class)
	public void testAddOrderWhenProductAndUserAreNotPresent() throws DatabaseException {
		IllegalStateException exc = new IllegalStateException();
		Orders o = new Orders();
		doThrow(exc).when(entityManager).persist(o);
		ordersDatabase.add(o);

	}

	@Test
	public void testRemoveOrderWhenOrderIsPresent() throws DatabaseException {
		Products p = new Products("name1", "description1", "category1", 1.0);
		Users u = new Users("firstname1", "lastname1", "address1", "email1", "username1", "password1");
		Orders order = new Orders(LocalDate.now(), p, u);
		ReflectionTestUtils.setField(order, "id", 1);
		when(entityManager.find(Orders.class, 1)).thenReturn(order);
		ordersDatabase.remove(order);
		verify(entityManager, times(1)).find(Orders.class, 1);
		verify(entityManager, times(1)).remove(order);

	}

	@Test(expected = DatabaseException.class)
	public void testRemoveUOrderWhenOrderIsNotPresent() throws DatabaseException {
		IllegalStateException exc = new IllegalStateException();
		Orders o = new Orders(LocalDate.now(), new Products(), new Users());
		when(entityManager.find(Orders.class, 1)).thenReturn(null);
		doThrow(exc).when(entityManager).remove(null);
		ordersDatabase.remove(o);

	}

	@Test(expected = DatabaseException.class)
	public void testRemoveUOrderWhenOrderIsNull() throws DatabaseException {
		IllegalStateException exc = new IllegalStateException();
		doThrow(exc).when(entityManager).find(Orders.class, null);
		ordersDatabase.remove(null);

	}

}

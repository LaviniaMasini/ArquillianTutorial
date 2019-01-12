package arquillian.tutorial;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.util.ReflectionTestUtils;

import arquillian.tutorial.database.IOrdersDatabase;
import arquillian.tutorial.entity.Orders;
import arquillian.tutorial.entity.Products;
import arquillian.tutorial.entity.Users;
import arquillian.tutorial.exception.DatabaseException;
import arquillian.tutorial.service.OrdersService;

public class OrdersServiceTest extends AbstractTestHelper {

	@Mock
	private IOrdersDatabase ordersDatabase;

	private OrdersService ordersService;

	private List<Orders> ordersList;

	@Before
	public void setup() throws Exception {
		ordersList = new ArrayList<Orders>();
		MockitoAnnotations.initMocks(this);
		ordersService = new OrdersService();
		ordersService.setOrdersDatabase(ordersDatabase);
	}

	@Test
	public void testFindAllOrdersWhenListIsEmpty() {
		when(ordersDatabase.getAllOrders()).thenReturn(ordersList);
		List<Orders> ordersListResult = ordersService.findAllOrders();
		verify(ordersDatabase, times(1)).getAllOrders();
		assertTrue(ordersListResult.isEmpty());
	}

	@Test
	public void testFindAllOrdersWhenListContainsOneOrders() {
		ordersList.add(new Orders(LocalDate.now(), new Products("name1", "description1", "category1", 1.0),
				new Users("firstname1", "lastname1", "address1", "email1", "username1", "password1")));
		ordersList.add(new Orders(LocalDate.now(), new Products("name2", "description2", "category2", 2.0),
				new Users("firstname2", "lastname2", "address2", "email2", "username2", "password2")));
		when(ordersDatabase.getAllOrders()).thenReturn(ordersList);
		List<Orders> ordersListResult = ordersService.findAllOrders();
		verify(ordersDatabase, times(1)).getAllOrders();
		assertEquals(2, ordersListResult.size());

	}

	@Test
	public void testFindOrderByIdWhenOrderIsNotPresent() {
		int id = 1;
		when(ordersDatabase.getOrdersById(id)).thenReturn(null);
		Orders orderResult = ordersService.findOrderById(id);
		verify(ordersDatabase, times(1)).getOrdersById(id);
		assertNull(orderResult);
	}
	
	@Test
	public void testFindOrderByIdWhenOrderIsPresent() {
		int id = 1;
		Products p = new Products("name1", "description1", "category1", 1.0);
		Users u = new Users("firstname1", "lastname1", "address1", "email1", "username1", "password1");
		Orders order = new Orders(LocalDate.now(), p, u);
		ReflectionTestUtils.setField(order, "id", id);
		ReflectionTestUtils.setField(p, "id", id);
		ReflectionTestUtils.setField(u, "id", id);
		when(ordersDatabase.getOrdersById(id)).thenReturn(order);
		Orders orderResult = ordersService.findOrderById(id);
		verify(ordersDatabase, times(1)).getOrdersById(id);
		assertOrder(orderResult, p, LocalDate.now(), u, 1);
	}
	
	@Test
	public void testAddNewOrderWhenOrderIsAdded() throws DatabaseException{
		Products p = new Products("name1", "description1", "category1", 1.0);
		Users u = new Users("firstname1", "lastname1", "address1", "email1", "username1", "password1");
		Orders order = new Orders(LocalDate.now(), p, u);
		boolean orderResult = ordersService.addOrder(order);
		verify(ordersDatabase, times(1)).add(order);
		assertTrue(orderResult);
	}
	
	@Test
	public void testAddNewOrderWhenOrderIsNotAdded() throws DatabaseException {
		Products p = new Products("name1", "description1", "category1", 1.0);
		Users u = new Users("firstname1", "lastname1", "address1", "email1", "username1", "password1");
		Orders order = new Orders(LocalDate.now(), p, u);
		doThrow(new DatabaseException("Error during order addition...")).when(ordersDatabase).add(order);
		boolean resultOrder = ordersService.addOrder(order);
		verify(ordersDatabase, times(1)).add(order);
		assertFalse(resultOrder);
	}
	
	@Test
	public void testRemoveOrderWhenOrderIsRemoved() throws DatabaseException {
		Products p = new Products("name1", "description1", "category1", 1.0);
		Users u = new Users("firstname1", "lastname1", "address1", "email1", "username1", "password1");
		Orders order = new Orders(LocalDate.now(), p, u);
		boolean resultOrder = ordersService.removeOrder(order);
		verify(ordersDatabase, times(1)).remove(order);
		assertTrue(resultOrder);

	}
	
	@Test
	public void testRemoveOrderWhenOrderIsNotRemoved() throws DatabaseException {
		Products p = new Products("name1", "description1", "category1", 1.0);
		Users u = new Users("firstname1", "lastname1", "address1", "email1", "username1", "password1");
		Orders order = new Orders(LocalDate.now(), p, u);
		doThrow(new DatabaseException("Error during order removal...")).when(ordersDatabase).remove(order);
		boolean resultOrder = ordersService.removeOrder(order);
		verify(ordersDatabase, times(1)).remove(order);
		assertFalse(resultOrder);

	}
	
	

}

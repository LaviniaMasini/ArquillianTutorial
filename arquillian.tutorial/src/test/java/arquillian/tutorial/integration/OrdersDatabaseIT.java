package arquillian.tutorial.integration;

import static org.junit.Assert.*;

import java.time.LocalDate;

import javax.ejb.EJB;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.junit.InSequence;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Test;
import org.junit.runner.RunWith;

import arquillian.tutorial.database.IOrdersDatabase;
import arquillian.tutorial.database.OrdersDatabase;
import arquillian.tutorial.entity.Orders;
import arquillian.tutorial.entity.Products;
import arquillian.tutorial.entity.Users;
import arquillian.tutorial.exception.DatabaseException;

@RunWith(Arquillian.class)
public class OrdersDatabaseIT {

	@EJB
	private IOrdersDatabase ordersDatabase;

	@PersistenceContext(name = "arquillian.tutorial")
	private EntityManager entityManager;

	@Deployment
	public static Archive<?> createDeployment() {
		return ShrinkWrap.create(WebArchive.class, "OrdersDatabaseIT.war").addPackage(OrdersDatabase.class.getPackage())
				.addClass(DatabaseException.class).addPackage(Orders.class.getPackage())
				.addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml").addAsResource("scripts/import.sql")
				.addAsResource("test-persistence.xml", "META-INF/persistence.xml");
	}

	@Test
	@InSequence(1)
	public void testAddWhenOrderIsNotPresent() throws DatabaseException {
		Products p = entityManager.find(Products.class, 1);
		Users u = entityManager.find(Users.class, 1);
		Orders o = new Orders(LocalDate.now(), p, u);
		ordersDatabase.add(o);
		assertEquals(1, entityManager.createQuery("select o from Orders o").getResultList().size());
	}

	@Test
	@InSequence(2)
	public void testGetAllOrdersWhenListIsNotEmpty() {
		assertEquals(1, ordersDatabase.getAllOrders().size());
	}

	@Test
	@InSequence(3)
	public void testGetOrdersByIdWhenIdIsPresent() {
		int id = 1;
		assertEquals(id, ordersDatabase.getOrdersById(id).getId());
	}

	@Test
	public void testGetOrdersByIdWhenIdIsNotPresent() {
		int id = -1;
		assertNull(ordersDatabase.getOrdersById(id));

	}

	@Test(expected = DatabaseException.class)
	@InSequence(4)
	public void testAddWhenOrderIsAlreadyPresent() throws DatabaseException {
		Orders o = entityManager.find(Orders.class, 1);
		ordersDatabase.add(o);
	}

	@Test(expected = DatabaseException.class)
	@InSequence(5)
	public void testAddWhenUserAndProductOfAnOrderAreNotPresent() throws DatabaseException {
		Orders o = new Orders();
		ordersDatabase.add(o);
	}

	@Test(expected = DatabaseException.class)
	public void testRemoveOrderWhenOrderIsNotPresent() throws DatabaseException {
		Orders o = new Orders();
		ordersDatabase.remove(o);
	}

	@Test
	@InSequence(6)
	public void testRemoveOrderWhenOrderIsPresent() throws DatabaseException {
		Orders o = entityManager.find(Orders.class, 1);
		ordersDatabase.remove(o);
		assertTrue(entityManager.createQuery("select o from Orders o").getResultList().isEmpty());

	}

	@Test
	@InSequence(7)
	public void testGetAllOrdersWhenOrdersListIsEmpty() {
		assertTrue(ordersDatabase.getAllOrders().isEmpty());
	}

	@Test(expected = DatabaseException.class)
	public void testRemoveOrderWhenOrderIsNull() throws DatabaseException {
		ordersDatabase.remove(null);
	}

}

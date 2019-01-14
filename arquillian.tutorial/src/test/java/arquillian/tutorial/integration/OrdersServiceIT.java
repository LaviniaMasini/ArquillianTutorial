package arquillian.tutorial.integration;

import static org.junit.Assert.*;

import java.time.LocalDate;
import java.util.List;

import javax.inject.Inject;
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

import arquillian.tutorial.database.OrdersDatabase;
import arquillian.tutorial.entity.Orders;
import arquillian.tutorial.entity.Products;
import arquillian.tutorial.entity.Users;
import arquillian.tutorial.exception.DatabaseException;
import arquillian.tutorial.service.OrdersService;

@RunWith(Arquillian.class)
public class OrdersServiceIT {

	@Inject
	private OrdersService ordersService;

	@PersistenceContext(name = "arquillian.tutorial")
	private EntityManager entityManager;

	@Deployment
	public static Archive<?> createDeployment() {
		return ShrinkWrap.create(WebArchive.class, "OrdersServiceIT.war").addPackage(OrdersDatabase.class.getPackage())
				.addClass(DatabaseException.class).addPackage(Orders.class.getPackage())
				.addPackage(OrdersService.class.getPackage()).addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml")
				.addAsResource("scripts/import.sql").addAsResource("test-persistence.xml", "META-INF/persistence.xml");
	}

	@Test
	@InSequence(1)
	public void testFindAllOrdersWhenListIsNotEmpty() {
		List<Orders> ordersList = ordersService.findAllOrders();
		assertEquals(0, ordersList.size());
	}

	@Test
	@InSequence(2)
	public void testAddOrderWhenItIsOk() throws DatabaseException {
		Products p = entityManager.find(Products.class, 1);
		Users u = entityManager.find(Users.class, 1);
		Orders o = new Orders(LocalDate.now(), p, u);
		assertTrue(ordersService.addOrder(o));
		assertEquals(1, entityManager.createQuery("select o from Orders o").getResultList().size());
	}

	@Test
	@InSequence(3)
	public void testFindByIdWhenIdIsPresent() {
		assertEquals(1, ordersService.findOrderById(1).getId());
	}

	@Test
	@InSequence(4)
	public void testAddOrderWhenItIsNotOk() {
		Orders o = new Orders();
		assertFalse(ordersService.addOrder(o));
		assertEquals(1, entityManager.createQuery("select o from Orders o").getResultList().size());
	}

	@Test
	@InSequence(5)
	public void testRemoveOrderWhenItIsNotOk() throws DatabaseException {
		assertFalse(ordersService.removeOrder(new Orders()));
		assertEquals(1, entityManager.createQuery("select o from Orders o").getResultList().size());

	}

	@Test
	@InSequence(6)
	public void testRemoveOrderWhenItIsOk() throws DatabaseException {
		Orders o = ordersService.findOrderById(1);
		assertTrue(ordersService.removeOrder(o));
		assertEquals(0, entityManager.createQuery("select o from Orders o").getResultList().size());

	}

	@Test
	@InSequence(7)
	public void testFindAllWhenListIsEmpty() {
		assertTrue(ordersService.findAllOrders().isEmpty());
	}

	@Test
	public void testFindByIdWhenIdIsNotPresent() {
		assertNull(ordersService.findOrderById(-1));
	}


}
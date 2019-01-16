package arquillian.tutorial.integration;

import static org.junit.Assert.*;

import javax.ejb.EJB;
import javax.transaction.HeuristicMixedException;
import javax.transaction.HeuristicRollbackException;
import javax.transaction.NotSupportedException;
import javax.transaction.RollbackException;
import javax.transaction.SystemException;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.junit.InSequence;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Test;
import org.junit.runner.RunWith;

import arquillian.tutorial.database.IProductsDatabase;
import arquillian.tutorial.database.ProductsDatabase;
import arquillian.tutorial.entity.Products;
import arquillian.tutorial.exception.DatabaseException;
import arquillian.tutorial.helper.AbstractTestHelper;

@RunWith(Arquillian.class)
public class ProductsDatabaseIT extends AbstractTestHelper{

	@EJB
	private IProductsDatabase productsDatabase;

	@Deployment
	public static Archive<?> createDeployment() {
		return ShrinkWrap.create(WebArchive.class, "ProductsDatabaseIT.war")
				.addPackage(ProductsDatabase.class.getPackage()).addClass(DatabaseException.class).addClass(AbstractTestHelper.class)
				.addPackage(Products.class.getPackage()).addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml")
				.addAsResource("scripts/import.sql").addAsResource("test-persistence.xml", "META-INF/persistence.xml");
	}

	@Test
	@InSequence(1)
	public void testGetAllProductsWhenListIsNotEmpty() {
		assertEquals(2, productsDatabase.getAllProducts().size());
	}

	@Test
	@InSequence(2)
	public void testGetProductByIdWhenIdIsPresent() {
		assertEquals(1, productsDatabase.getProductById(1).getId());
	}

	@Test
	public void testGetProductByIdWhenIdIsNotPresent() {
		assertNull(productsDatabase.getProductById(-1));
	}

	@Test
	@InSequence(3)
	public void testGetAllProductsWhenListIsEmpty() throws NotSupportedException, SystemException, RollbackException,
			HeuristicMixedException, HeuristicRollbackException {
		Products p = entityManager.find(Products.class, 1);
		Products p1 = entityManager.find(Products.class, 2);
		removeProduct(p);
		removeProduct(p1);
		assertEquals(0, productsDatabase.getAllProducts().size());

	}

}

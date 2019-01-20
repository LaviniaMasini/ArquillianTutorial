package arquillian.tutorial.integration;

import static org.junit.Assert.*;

import javax.inject.Inject;
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

import arquillian.tutorial.database.ProductsDatabase;
import arquillian.tutorial.entity.Products;
import arquillian.tutorial.exception.DatabaseException;
import arquillian.tutorial.helper.AbstractTestHelper;
import arquillian.tutorial.service.IProductsService;
import arquillian.tutorial.service.ProductsService;

@RunWith(Arquillian.class)
public class ProductsServiceIT extends AbstractTestHelper{

	@Inject
	private IProductsService productsService;

	@Deployment
	public static Archive<?> createDeployment() {
		return ShrinkWrap.create(WebArchive.class, "ProductsServiceIT.war")
				.addPackage(ProductsDatabase.class.getPackage()).addClass(DatabaseException.class).addClass(AbstractTestHelper.class)
				.addPackage(Products.class.getPackage()).addPackage(ProductsService.class.getPackage())
				.addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml").addAsResource("scripts/import.sql")
				.addAsResource("test-persistence.xml", "META-INF/persistence.xml");
	}

	@Test
	@InSequence(1)
	public void testFindAllProductsWhenListIsNotEmpty() {
		assertEquals(2, productsService.findAllProducts().size());
	}

	@Test
	@InSequence(2)
	public void testFindProductByIdWhenIdIsPresent() {
		assertEquals(1, productsService.findProductById(1).getId());
	}

	@Test
	public void testGetProductByIdWhenIdIsNotPresent() {
		assertNull(productsService.findProductById(-1));
	}

	@Test
	@InSequence(3)
	public void testGetAllProductsWhenListIsEmpty() throws NotSupportedException, SystemException, RollbackException,
			HeuristicMixedException, HeuristicRollbackException {
		Products p = entityManager.find(Products.class, 1);
		Products p1 = entityManager.find(Products.class, 2);
		removeProduct(p);
		removeProduct(p1);
		assertEquals(0, productsService.findAllProducts().size());

	}

}

package arquillian.tutorial;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.util.ReflectionTestUtils;

import arquillian.tutorial.database.ProductsDatabase;
import arquillian.tutorial.entity.Products;

public class ProductsDatabaseTest extends AbstractTestHelper {
	
	@Mock
	private EntityManager entityManager;

	@Mock
	private Query query;

	private ProductsDatabase productsDatabase;

	private List<Products> productsList;

	private static final String queryString = "select p from Products p";


	@Before
	public void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);
		productsDatabase = new ProductsDatabase();
		productsDatabase.setEntityManager(entityManager);
		productsList = new ArrayList<>();
	}
	
	@Test
	public void testGetAllProductsWhenDBIsEmpty() {
		when(entityManager.createQuery(queryString)).thenReturn(query);
		when(query.getResultList()).thenReturn(productsList);
		List<Products> productsListResult = productsDatabase.getAllProducts();
		verify(entityManager).createQuery(queryString);
		verify(query).getResultList();
		assertTrue(productsListResult.isEmpty());
	}
	
	@Test
	public void testGetAllProductsWhenDBContainsTwoProducts() {
		productsList.add(new Products("name1", "description1", "category1", 1.0));
		productsList.add(new Products("name2", "description2", "category2", 2.0));
		when(entityManager.createQuery(queryString)).thenReturn(query);
		when(query.getResultList()).thenReturn(productsList);
		List<Products> productsListResult = productsDatabase.getAllProducts();
		verify(entityManager).createQuery(queryString);
		verify(query).getResultList();
		assertEquals(2, productsListResult.size());
	}
	
	@Test
	public void testGetProductByIdWhenIdIsNotPresent() {
		int id = 1;
		when(entityManager.find(Products.class, id)).thenReturn(null);
		Products resultProduct = productsDatabase.getProductById(id);
		verify(entityManager, times(1)).find(Products.class, id);
		assertNull(resultProduct);

	}
	
	@Test
	public void testGetProductByIdWhenIdIsPresent() {
		int id = 1;
		String name = "name1";
		String description = "description1";
		String category = "category1";
		double price = 1.00;
		Products product = new Products(name, description, category, 1.0);
		ReflectionTestUtils.setField(product, "id", id);
		when(entityManager.find(Products.class, id)).thenReturn(product);
		Products resultProduct = productsDatabase.getProductById(1);
		verify(entityManager, times(1)).find(Products.class, id);
		assertProduct(resultProduct, name, description, category, price, id);


	}


}

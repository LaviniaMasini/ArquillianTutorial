package arquillian.tutorial;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.util.ReflectionTestUtils;

import arquillian.tutorial.database.IProductsDatabase;
import arquillian.tutorial.entity.Products;
import arquillian.tutorial.service.ProductsService;

public class ProductsServiceTest {

	@Mock
	private IProductsDatabase productsDatabase;

	private ProductsService productsService;

	private List<Products> productsList;

	@Before
	public void setup() throws Exception {
		productsList = new ArrayList<Products>();
		MockitoAnnotations.initMocks(this);
		productsService = new ProductsService();
		productsService.setProductsDatabase(productsDatabase);
	}

	@Test
	public void testFindAllProductsWhenListIsEmpty() {
		when(productsDatabase.getAllProducts()).thenReturn(productsList);
		List<Products> productsListResult = productsService.findAllProducts();
		verify(productsDatabase, times(1)).getAllProducts();
		assertTrue(productsListResult.isEmpty());
	}

	@Test
	public void testFindAllProductsWhenListContainsTwoProducts() {
		productsList.add(new Products("name1", "description1", "category1", 1.0));
		productsList.add(new Products("name2", "description2", "category2", 2.0));
		when(productsDatabase.getAllProducts()).thenReturn(productsList);
		List<Products> productsListResult = productsService.findAllProducts();
		verify(productsDatabase, times(1)).getAllProducts();
		assertEquals(2, productsListResult.size());
	}

	@Test
	public void testFindProductByIdWhenProductIsNotPresent() {
		int id = 1;
		when(productsDatabase.getProductById(id)).thenReturn(null);
		Products productResult = productsService.findProductById(id);
		verify(productsDatabase, times(1)).getProductById(id);
		assertNull(productResult);
	}

	@Test
	public void testFindProductByIdWhenProductIsPresent() {
		int id = 1;
		String name = "name1";
		String description = "description1";
		String category = "category1";
		double price = 1.00;
		Products product = new Products(name, description, category, 1.0);
		ReflectionTestUtils.setField(product, "id", id);
		when(productsDatabase.getProductById(id)).thenReturn(product);
		Products resultProduct = productsService.findProductById(id);
		verify(productsDatabase, times(1)).getProductById(id);
		assertProduct(resultProduct, name, description, category, price, id);
	}

	private void assertProduct(Products resultProduct, String name, String description, String category, double price,
			int id) {
		assertEquals(id, resultProduct.getId());
		assertEquals(name, resultProduct.getName());
		assertEquals(description, resultProduct.getDescription());
		assertEquals(category, resultProduct.getCategory());
		assertEquals(price, resultProduct.getPrice(), 0);
	}

}

package arquillian.tutorial.service;

import java.util.List;

import arquillian.tutorial.database.IProductsDatabase;
import arquillian.tutorial.entity.Products;

public class ProductsService {

	private IProductsDatabase productsDatabase;
	
	public void setProductsDatabase(IProductsDatabase productsDatabase) {
		this.productsDatabase = productsDatabase;
		
	}

	public List<Products> findAllProducts() {
		return productsDatabase.getAllProducts();
	}

	public Products findProductById(int id) {
		return productsDatabase.getProductById(id);
	}

}

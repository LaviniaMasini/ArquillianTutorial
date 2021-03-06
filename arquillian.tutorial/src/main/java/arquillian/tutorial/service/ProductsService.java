package arquillian.tutorial.service;

import java.util.List;
import javax.ejb.EJB;
import javax.enterprise.inject.Model;

import arquillian.tutorial.database.IProductsDatabase;
import arquillian.tutorial.entity.Products;

@Model
public class ProductsService implements IProductsService {

	@EJB
	private IProductsDatabase productsDatabase;

	@Override
	public void setProductsDatabase(IProductsDatabase productsDatabase) {
		this.productsDatabase = productsDatabase;

	}

	@Override
	public List<Products> findAllProducts() {
		return productsDatabase.getAllProducts();
	}

	@Override
	public Products findProductById(int id) {
		return productsDatabase.getProductById(id);
	}

}

package arquillian.tutorial.service;

import java.util.List;

import arquillian.tutorial.database.IProductsDatabase;
import arquillian.tutorial.entity.Products;

public interface IProductsService {

	void setProductsDatabase(IProductsDatabase productsDatabase);

	List<Products> findAllProducts();

	Products findProductById(int id);

}
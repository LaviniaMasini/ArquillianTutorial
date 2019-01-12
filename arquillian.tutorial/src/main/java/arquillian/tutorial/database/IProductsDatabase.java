package arquillian.tutorial.database;

import java.util.List;

import arquillian.tutorial.entity.Products;

public interface IProductsDatabase {

	List<Products> getAllProducts();

	Products getProductById(int id);

}

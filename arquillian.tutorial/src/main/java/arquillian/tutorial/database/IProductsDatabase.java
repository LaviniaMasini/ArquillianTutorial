package arquillian.tutorial.database;

import java.util.List;

import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;

import arquillian.tutorial.entity.Products;

@TransactionAttribute(TransactionAttributeType.REQUIRED)
public interface IProductsDatabase {

	List<Products> getAllProducts();

	Products getProductById(int id);
	
}

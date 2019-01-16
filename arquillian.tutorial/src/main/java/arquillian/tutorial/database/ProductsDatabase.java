package arquillian.tutorial.database;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.Query;

import arquillian.tutorial.entity.Products;

@Stateless
public class ProductsDatabase extends AbstractDatabase implements IProductsDatabase {

	@SuppressWarnings("unchecked")
	@Override
	public List<Products> getAllProducts() {
		Query query = entityManager.createQuery("select p from Products p");
		return query.getResultList();
	}

	@Override
	public Products getProductById(int id) {
		return entityManager.find(Products.class, id);
	}

}

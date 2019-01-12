package arquillian.tutorial.database;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import arquillian.tutorial.entity.Products;

public class ProductsDatabase implements IProductsDatabase {

	private EntityManager entityManager;

	public void setEntityManager(EntityManager entityManager) {
		this.entityManager = entityManager; 
	}

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

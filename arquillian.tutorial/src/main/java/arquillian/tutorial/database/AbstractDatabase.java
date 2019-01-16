package arquillian.tutorial.database;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

public class AbstractDatabase {

	@PersistenceContext(name = "arquillian.tutorial")
	protected EntityManager entityManager;

	public AbstractDatabase() {
		super();
	}

	public void setEntityManager(EntityManager entityManager) {
		this.entityManager = entityManager; 
	}

}
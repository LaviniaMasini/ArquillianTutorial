package arquillian.tutorial.database;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import arquillian.tutorial.entity.Users;
import arquillian.tutorial.exception.DatabaseException;

@Stateless
public class UsersDatabase implements IUsersDatabase {

	@PersistenceContext(name = "arquillian.tutorial")
	private EntityManager entityManager;

	public void setEntityManager(EntityManager entityManager) {
		this.entityManager = entityManager;

	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Users> getAllUsers() {
		Query query = entityManager.createQuery("select u from Users u");
		return query.getResultList();
	}

	@Override
	public Users getUserById(int id) {
		return entityManager.find(Users.class, id);
	}

	@Override
	public void add(Users user) throws DatabaseException {
		try {
			entityManager.persist(user);
		} catch (Exception e) {
			throw new DatabaseException(e);
		}

	}

	@Override
	public void update(Users user) throws DatabaseException {
		if (user != null && getUserById(user.getId()) != null) {
			entityManager.merge(user);
		} else {
			throw new DatabaseException();
		}
	}

	@Override
	public Users getUserByUsernameAndPassword(String username, String password) throws DatabaseException {
		Query query = entityManager
				.createQuery("select u from Users u where u.username = :username and u.password = :password");
		query.setParameter("username", username);
		query.setParameter("password", password);
		try {
			return (Users) query.getSingleResult();
		} catch (Exception e) {
			throw new DatabaseException(e);
		}

	}

}

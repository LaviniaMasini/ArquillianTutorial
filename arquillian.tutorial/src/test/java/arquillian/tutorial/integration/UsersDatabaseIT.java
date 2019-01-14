package arquillian.tutorial.integration;

import static org.junit.Assert.*;

import javax.ejb.EJB;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.junit.InSequence;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Test;
import org.junit.runner.RunWith;

import arquillian.tutorial.database.IUsersDatabase;
import arquillian.tutorial.database.UsersDatabase;
import arquillian.tutorial.entity.Users;
import arquillian.tutorial.exception.DatabaseException;
import arquillian.tutorial.helper.AbstractTestHelper;

@RunWith(Arquillian.class)
public class UsersDatabaseIT extends AbstractTestHelper {

	@EJB
	private IUsersDatabase usersDatabase;

	@PersistenceContext(name = "arquillian.tutorial")
	private EntityManager entityManager;

	@Deployment
	public static Archive<?> createDeployment() {
		return ShrinkWrap.create(WebArchive.class, "UsersDatabaseIT.war").addPackage(UsersDatabase.class.getPackage())
				.addClass(DatabaseException.class).addClass(AbstractTestHelper.class)
				.addPackage(Users.class.getPackage()).addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml")
				.addAsResource("test-persistence.xml", "META-INF/persistence.xml");
	}

	@Test
	@InSequence(1)
	public void testGetAllUsersWhenUsersListIsEmpty() {
		assertTrue(usersDatabase.getAllUsers().isEmpty());
	}

	@Test
	@InSequence(2)
	public void testAddWhenUsersAreNotPresent() throws DatabaseException {
		usersDatabase.add(new Users("firstname1", "lastname1", "address1", "email1", "username1", "password1"));
		usersDatabase.add(new Users("firstname2", "lastname2", "address2", "email2", "username2", "password2"));
		assertEquals(2, entityManager.createQuery("select u from Users u").getResultList().size());
	}

	@Test
	@InSequence(3)
	public void testGetAllUsersWhenListIsNotEmpty() {
		assertEquals(2, usersDatabase.getAllUsers().size());
	}

	@Test
	@InSequence(4)
	public void testGetUserByIdWhenIdIsPresent() {
		assertEquals(1, usersDatabase.getUserById(1).getId());
	}

	@Test
	public void testGetUserByIdWhenIdIsNotPresent() {
		assertNull(usersDatabase.getUserById(-1));
	}

	@Test(expected = DatabaseException.class)
	@InSequence(5)
	public void testAddWhenUserIsAlreadyPresent() throws DatabaseException {
		Users u = entityManager.find(Users.class, 1);
		usersDatabase.add(u);
	}

	@Test(expected = DatabaseException.class)
	@InSequence(6)
	public void testAddUserWhenUsernameAndPasswordAreAlreadyPresent() throws DatabaseException {
		Users u1 = entityManager.find(Users.class, 1);
		Users u = new Users();
		u.setUsername(u1.getUsername());
		u.setPassword(u1.getPassword());
		usersDatabase.add(u);
	}

	@Test
	@InSequence(7)
	public void testUpdateUserWhenUserIsPresent() throws DatabaseException {
		Users u = entityManager.find(Users.class, 1);
		u.setFirstname("firstnameUpd");
		u.setLastname("lastnameUpd");
		u.setAddress("addressUpd");
		u.setEmail("emailUpd");
		u.setUsername("usernameUpd");
		u.setPassword("passwordUpd");
		usersDatabase.update(u);
		Users result = entityManager.find(Users.class, 1);
		assertUser(result, 1, "firstnameUpd", "lastnameUpd", "addressUpd", "emailUpd", "username1", "password1");

	}

	@Test(expected = DatabaseException.class)
	public void testUpdateUserWhenUserIsNotInDB() throws DatabaseException {
		Users u = entityManager.find(Users.class, -1);
		usersDatabase.update(u);

	}

	@Test(expected = DatabaseException.class)
	public void testUpdateUserWhenUserIsNull() throws DatabaseException {
		usersDatabase.update(null);
	}
	
	@Test
	@InSequence(8)
	public void testGetUserByUsernameAndPasswordWhenUserIsPresent() throws DatabaseException {
		String username = "username2";
		String password = "password2";
		Users u = usersDatabase.getUserByUsernameAndPassword(username, password);
		assertEquals(username, u.getUsername());
		assertEquals(password, u.getPassword());
		
	}
	
	@Test(expected = DatabaseException.class)
	public void testGetUserByUsernameAndPasswordWhenUserIsNotPresent() throws DatabaseException {
		String username = "username6";
		String password = "password6";
		usersDatabase.getUserByUsernameAndPassword(username, password);
		
	}


}

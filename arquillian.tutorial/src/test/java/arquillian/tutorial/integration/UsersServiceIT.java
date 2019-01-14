package arquillian.tutorial.integration;

import static org.junit.Assert.*;

import java.util.List;

import javax.inject.Inject;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.junit.InSequence;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Test;
import org.junit.runner.RunWith;

import arquillian.tutorial.database.UsersDatabase;
import arquillian.tutorial.entity.Users;
import arquillian.tutorial.exception.DatabaseException;
import arquillian.tutorial.helper.AbstractTestHelper;
import arquillian.tutorial.service.UsersService;

@RunWith(Arquillian.class)
public class UsersServiceIT extends AbstractTestHelper {

	@Inject
	private UsersService usersService;

	@Deployment
	public static Archive<?> createDeployment() {
		return ShrinkWrap.create(WebArchive.class, "UsersServiceIT.war").addPackage(UsersDatabase.class.getPackage())
				.addClass(DatabaseException.class).addPackage(Users.class.getPackage()).addClass(AbstractTestHelper.class)
				.addPackage(UsersService.class.getPackage()).addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml")
				.addAsResource("test-persistence.xml", "META-INF/persistence.xml");
	}

	@Test
	@InSequence(1)
	public void testFindAllUsersWhenListIsEmpty() {
		List<Users> usersList = usersService.findAllUsers();
		assertEquals(0, usersList.size());
	}

	@Test
	@InSequence(2)
	public void testAddNewUserWhenAdditionIsOk() {
		Users user = new Users("firstname1", "lastname1", "address1", "email1", "username1", "password1");
		assertTrue(usersService.addUser(user));
		assertEquals(1, entityManager.createQuery("select u from Users u").getResultList().size());
	}
	
	@Test
	@InSequence(3)
	public void testFindByIdWhenIdIsPresent() {
		assertEquals(1, usersService.findUserById(1).getId());
	}
	
	@Test
	public void testFindByIdWhenIdIsNotPresent() {
		assertNull(usersService.findUserById(-1));
	}
	
	@Test
	@InSequence(4)
	public void testFindAll() {
		assertEquals(1, usersService.findAllUsers().size());
	}
	
	@Test
	@InSequence(5)
	public void testAddNewUserWhenAdditionIsNotOk() {
		Users user = new Users("firstname1", "lastname1", "address1", "email1", "username1", "password1");
		assertFalse(usersService.addUser(user));
		assertEquals(1, entityManager.createQuery("select u from Users u").getResultList().size());
	}
	
	@Test
	@InSequence(6)
	public void testUpdateUserWhenUpdatingIsOk() {
		Users u = entityManager.find(Users.class, 1);
		String firstname =  "firstname2";
		String lastname = "lastname2";
		String address = "address2";
		String email = "email2";
		u.setFirstname(firstname);
		u.setLastname(lastname);
		u.setAddress(address);
		u.setEmail(email);
		assertTrue(usersService.updateUser(u));
		assertUser(entityManager.find(Users.class, 1), 1, firstname, lastname, address, email, "username1", "password1");
		
	}
	
	@Test
	public void testUpdateUserWhenUpdatingIsNotOk() throws DatabaseException {
		Users u = new Users();
		assertFalse(usersService.updateUser(u));

	}
	
	@Test
	public void testFindUserWhenUsernameAndPasswordAreNotPresent() throws DatabaseException {
		assertNull(usersService.findUserByUsernameAndPassword("user", "pass"));
	}
	
	@Test
	@InSequence(7)
	public void testFindUserWhenUsernameAndPasswordArePresent() {
		Users u = usersService.findUserByUsernameAndPassword("username1", "password1");
		assertNotNull(u);
		assertEquals("username1", u.getUsername());
		assertEquals("password1", u.getPassword());
	}



}

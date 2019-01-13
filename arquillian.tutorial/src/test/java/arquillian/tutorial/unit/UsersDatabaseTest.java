package arquillian.tutorial.unit;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.util.ReflectionTestUtils;

import arquillian.tutorial.database.UsersDatabase;
import arquillian.tutorial.entity.Users;
import arquillian.tutorial.exception.DatabaseException;
import arquillian.tutorial.helper.AbstractTestHelper;

public class UsersDatabaseTest extends AbstractTestHelper {

	@Mock
	private EntityManager entityManager;

	@Mock
	private Query query;

	private UsersDatabase usersDatabase;

	private List<Users> usersList;

	private static final String queryString = "select u from Users u";

	private static final String queryStringInfo = "select u from Users u where u.username = :username and u.password = :password";

	@Before
	public void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);
		usersDatabase = new UsersDatabase();
		usersDatabase.setEntityManager(entityManager);
		usersList = new ArrayList<>();
	}

	@Test
	public void testGetAllUsersWhenDBIsEmpty() {
		when(entityManager.createQuery(queryString)).thenReturn(query);
		when(query.getResultList()).thenReturn(usersList);
		List<Users> usersListResult = usersDatabase.getAllUsers();
		verify(entityManager).createQuery(queryString);
		verify(query).getResultList();
		assertTrue(usersListResult.isEmpty());
	}

	@Test
	public void testGetAllUsersWhenDBContainsTwoUsers() {
		usersList.add(new Users("firstname1", "lastname1", "address1", "email1", "username1", "password1"));
		usersList.add(new Users("firstname2", "lastname2", "address2", "email2", "username2", "password2"));
		when(entityManager.createQuery(queryString)).thenReturn(query);
		when(query.getResultList()).thenReturn(usersList);
		List<Users> usersListResult = usersDatabase.getAllUsers();
		verify(entityManager).createQuery(queryString);
		verify(query).getResultList();
		assertEquals(2, usersListResult.size());
	}

	@Test
	public void testGetUserByIdWhenIdIsPresent() {
		int id = 1;
		String firstname = "firstname";
		String lastname = "lastname";
		String address = "address";
		String email = "email";
		String username = "username";
		String password = "password";
		Users user = new Users(firstname, lastname, address, email, username, password);
		ReflectionTestUtils.setField(user, "id", id);
		when(entityManager.find(Users.class, id)).thenReturn(user);
		Users resultUser = usersDatabase.getUserById(id);
		verify(entityManager, times(1)).find(Users.class, id);
		assertUser(resultUser, id, firstname, lastname, address, email, username, password);

	}

	@Test
	public void testGetUsersByIdWhenIdIsNotPresent() {
		int id = 1;
		when(entityManager.find(Users.class, id)).thenReturn(null);
		Users resultUser = usersDatabase.getUserById(id);
		verify(entityManager, times(1)).find(Users.class, id);
		assertNull(resultUser);

	}

	@Test
	public void testAddUuserWhenUserIsNotPresent() throws DatabaseException {
		Users u = new Users("firstname1", "lastname1", "address1", "email1", "username1", "password1");
		usersDatabase.add(u);
		verify(entityManager, times(1)).persist(u);
	}

	@Test(expected = DatabaseException.class)
	public void testAddUserWhenUserIsPresent() throws DatabaseException {
		IllegalStateException exc = new IllegalStateException();
		Users u = new Users("firstname1", "lastname1", "address1", "email1", "username1", "password1");
		ReflectionTestUtils.setField(u, "id", 1);
		doThrow(exc).when(entityManager).persist(u);
		usersDatabase.add(u);
		verify(entityManager, times(1)).persist(u);
		verifyNoMoreInteractions(entityManager);
	}

	@Test(expected = DatabaseException.class)
	public void testAddUserWhenUsernameAndPasswordAreAlreadyPresent() throws DatabaseException {
		IllegalStateException exc = new IllegalStateException();
		Users u = new Users("firstname", "lastname", "address", "email", "username", "password");
		doThrow(exc).when(entityManager).persist(u);
		usersDatabase.add(u);
		verify(entityManager, times(1)).persist(u);
		verifyNoMoreInteractions(entityManager);
	}
		
	@Test
	public void testUpdateUserWhenUserExists() throws DatabaseException {
		Users u = new Users("firstname", "lastname", "address", "email", "username", "password");
		int id = 1;
		ReflectionTestUtils.setField(u, "id", id);
		Users updatedUser = new Users("firstname2", "lastname2", "address2", "email2", "username", "password");
		ReflectionTestUtils.setField(updatedUser, "id", id);
		when(entityManager.find(Users.class, id)).thenReturn(u);
		when(entityManager.merge(u)).thenReturn(updatedUser);
		usersDatabase.update(u);
		verify(entityManager, times(1)).find(Users.class, id);
		verify(entityManager, times(1)).merge(u);

	}
	
	@Test(expected = DatabaseException.class)
	public void testUpdateUserWhenUserIsNotInDB() throws DatabaseException {
		Users u = new Users("firstname", "lastname", "address", "email", "username", "password");
		int id = 1;
		ReflectionTestUtils.setField(u, "id", id);
		when(entityManager.find(Users.class, id)).thenReturn(null);
		usersDatabase.update(u);
	}
	
	@Test(expected = DatabaseException.class)
	public void testUpdateUserWhenUserIsNull() throws DatabaseException {
		usersDatabase.update(null);
	}
	
	@Test
	public void testGetUserByUsernameAndPasswordWhenUserIsPresent() throws DatabaseException {
		Users u = new Users("firstname", "lastname", "address", "email", "username", "password");
		int id = 1;
		ReflectionTestUtils.setField(u, "id", id);
		when(entityManager.createQuery(queryStringInfo)).thenReturn(query);
		when(query.setParameter("username", "username")).thenReturn(query);
		when(query.setParameter("password", "password")).thenReturn(query);
		when(query.getSingleResult()).thenReturn(u);
		Users resultUser = usersDatabase.getUserByUsernameAndPassword("username", "password");
		verify(entityManager).createQuery(queryStringInfo);
		verify(query).setParameter("username", "username");
		verify(query).setParameter("password", "password");
		verify(query).getSingleResult();
		assertNotNull(resultUser);
	}
	
	@Test(expected = DatabaseException.class)
	public void testGetUserByUsernameAndPasswordWhenUserIsNotPresent() throws DatabaseException {
		IllegalStateException exc = new IllegalStateException();
		when(entityManager.createQuery(queryStringInfo)).thenReturn(query);
		when(query.setParameter("username", "username")).thenReturn(query);
		when(query.setParameter("password", "password")).thenReturn(query);
		when(query.getSingleResult()).thenThrow(exc);
		usersDatabase.getUserByUsernameAndPassword("username", "password");
		
	}


}

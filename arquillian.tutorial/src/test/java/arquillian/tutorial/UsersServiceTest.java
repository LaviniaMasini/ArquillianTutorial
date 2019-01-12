package arquillian.tutorial;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.util.ReflectionTestUtils;

import arquillian.tutorial.database.IUsersDatabase;
import arquillian.tutorial.entity.Users;
import arquillian.tutorial.exception.DatabaseException;
import arquillian.tutorial.service.UsersService;

public class UsersServiceTest extends AbstractTestHelper {

	@Mock
	private IUsersDatabase usersDatabase;

	private UsersService usersService;

	private List<Users> usersList;

	@Before
	public void setup() throws Exception {
		usersList = new ArrayList<Users>();
		MockitoAnnotations.initMocks(this);
		usersService = new UsersService();
		usersService.setUsersService(usersDatabase);
	}

	@Test
	public void testFindAllUsersWhenListIsEmpty() {
		when(usersDatabase.getAllUsers()).thenReturn(usersList);
		List<Users> usersListResult = usersService.findAllUsers();
		verify(usersDatabase, times(1)).getAllUsers();
		assertTrue(usersListResult.isEmpty());

	}

	@Test
	public void testFindAllUsersWhenListContainsTwoUsers() {
		usersList.add(new Users("firstname1", "lastname1", "address1", "email1", "username1", "password1"));
		usersList.add(new Users("firstname2", "lastname2", "address2", "email2", "username2", "password2"));
		when(usersDatabase.getAllUsers()).thenReturn(usersList);
		List<Users> usersListResult = usersService.findAllUsers();
		verify(usersDatabase, times(1)).getAllUsers();
		assertEquals(2, usersListResult.size());

	}

	@Test
	public void testFindUserByIdWhenUserIsNotPresent() {
		int id = 1;
		when(usersDatabase.getUserById(id)).thenReturn(null);
		Users userResult = usersService.findUserById(id);
		verify(usersDatabase, times(1)).getUserById(id);
		assertNull(userResult);
	}

	@Test
	public void testFindUserByIdWhenUserIsPresent() {
		int id = 1;
		String firstname = "firstname";
		String lastname = "lastname";
		String address = "address";
		String email = "email";
		String username = "username";
		String password = "password";
		Users user = new Users(firstname, lastname, address, email, username, password);
		ReflectionTestUtils.setField(user, "id", id);
		when(usersDatabase.getUserById(id)).thenReturn(user);
		Users resultUser = usersService.findUserById(id);
		verify(usersDatabase, times(1)).getUserById(id);
		assertUser(resultUser, id, firstname, lastname, address, email, username, password);
	}

	@Test
	public void testAddNewUserWhenAdditionIsOk() throws DatabaseException {
		Users user = new Users("firstname1", "lastname1", "address1", "email1", "username1", "password1");
		boolean resultUser = usersService.addUser(user);
		verify(usersDatabase, times(1)).add(user);
		assertTrue(resultUser);
	}

	@Test
	public void testAddNewUserWhenAdditionIsNotOk() throws DatabaseException {
		Users user = new Users("firstname1", "lastname1", "address1", "email1", "username1", "password1");
		doThrow(new DatabaseException("Error during user addition...")).when(usersDatabase).add(user);
		boolean resultUser = usersService.addUser(user);
		verify(usersDatabase, times(1)).add(user);
		assertFalse(resultUser);
	}

	@Test
	public void testUpdateUserWhenUpdatingIsOk() throws DatabaseException {
		Users user = new Users("firstname1", "lastname1", "address1", "email1", "username1", "password1");
		ReflectionTestUtils.setField(user, "id", 1);
		Users updatedUser = new Users("firstname2", "lastname2", "address2", "email2", "username2", "password2");
		ReflectionTestUtils.setField(updatedUser, "id", 1);
		boolean resultUser = usersService.updateUser(user);
		verify(usersDatabase, times(1)).update(user);
		assertTrue(resultUser);

	}

	@Test
	public void testUpdateUserWhenUpdatingIsNotOk() throws DatabaseException {
		Users user = new Users("firstname1", "lastname1", "address1", "email1", "username1", "password1");
		doThrow(new DatabaseException("Error during user updating...")).when(usersDatabase).update(user);
		boolean resultUser = usersService.updateUser(user);
		verify(usersDatabase, times(1)).update(user);
		assertFalse(resultUser);

	}
	
	@Test
	public void testGetUserWhenUsernameAndPasswordArePresent() throws DatabaseException {
		int id = 1;
		String firstname = "firstname";
		String lastname = "lastname";
		String address = "address";
		String email = "email";
		String username = "username";
		String password = "password";
		Users user = new Users(firstname, lastname, address, email, username, password);
		ReflectionTestUtils.setField(user, "id", 1);
		when(usersDatabase.getUserByUsernameAndPassword(username, password)).thenReturn(user);
		Users resultUser = usersService.findUserByUsernameAndPassword(username, password);
		verify(usersDatabase, times(1)).getUserByUsernameAndPassword(username, password);
		assertUser(resultUser, id, firstname, lastname, address, email, username, password);

	}
	
	@Test
	public void testGetUserWhenUsernameAndPasswordAreNotPresent() throws DatabaseException {
		String username = "username";
		String password = "password";
		when(usersDatabase.getUserByUsernameAndPassword(username, password)).thenThrow(new DatabaseException());
		Users resultUser = usersService.findUserByUsernameAndPassword(username, password);
		verify(usersDatabase, times(1)).getUserByUsernameAndPassword(username, password);
		assertNull(resultUser);

	}
	
	

}

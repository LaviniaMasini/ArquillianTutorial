package arquillian.tutorial.service;

import java.util.List;

import javax.ejb.EJB;
import javax.enterprise.inject.Model;

import org.apache.log4j.Logger;

import arquillian.tutorial.database.IUsersDatabase;
import arquillian.tutorial.entity.Users;
import arquillian.tutorial.exception.DatabaseException;

@Model
public class UsersService {

	private IUsersDatabase usersDatabase;

	private static final Logger LOGGER = Logger.getLogger(UsersService.class);

	@EJB
	public void setUsersService(IUsersDatabase usersDatabase) {
		this.usersDatabase = usersDatabase;
	}

	public List<Users> findAllUsers() {
		return usersDatabase.getAllUsers();
	}

	public Users findUserById(int id) {
		return usersDatabase.getUserById(id);
	}

	public boolean addUser(Users user) {
		try {
			usersDatabase.add(user);
			LOGGER.info("User added correctly");
			return true;
		} catch (DatabaseException e) {
			LOGGER.error("Error during user addition: " + e);
		}
		return false;
	}

	public boolean updateUser(Users user) {
		try {
			usersDatabase.update(user);
			LOGGER.info("User updated correctly");
			return true;
		} catch (DatabaseException e) {
			LOGGER.error("Error during user updating: " + e);
		}
		return false;
	}

	public Users findUserByUsernameAndPassword(String username, String password) {
		try {
			return usersDatabase.getUserByUsernameAndPassword(username, password);
		} catch (DatabaseException e) {
			LOGGER.info("The user is not present: " + e);
			return null;

		}
	}

}

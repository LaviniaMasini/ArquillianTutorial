package arquillian.tutorial.database;

import java.util.List;

import arquillian.tutorial.entity.Users;
import arquillian.tutorial.exception.DatabaseException;

public interface IUsersDatabase {

	List<Users> getAllUsers();

	Users getUserById(int id);

	void add(Users user) throws DatabaseException;

	void update(Users user) throws DatabaseException;

	Users getUserByUsernameAndPassword(String username, String password) throws DatabaseException;

}

package arquillian.tutorial.service;

import java.util.List;

import arquillian.tutorial.database.IUsersDatabase;
import arquillian.tutorial.entity.Users;

public interface IUsersService {

	void setUsersService(IUsersDatabase usersDatabase);

	List<Users> findAllUsers();

	Users findUserById(int id);

	boolean addUser(Users user);

	boolean updateUser(Users user);

	Users findUserByUsernameAndPassword(String username, String password);

}
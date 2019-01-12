package arquillian.tutorial.database;

import java.util.List;

import arquillian.tutorial.entity.Orders;
import arquillian.tutorial.exception.DatabaseException;

public interface IOrdersDatabase {

	List<Orders> getAllOrders();

	Orders getOrdersById(int id);

	void add(Orders order) throws DatabaseException;

	void remove(Orders order) throws DatabaseException;

}

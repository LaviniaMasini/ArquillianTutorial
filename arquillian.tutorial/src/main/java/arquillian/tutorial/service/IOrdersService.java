package arquillian.tutorial.service;

import java.util.List;

import arquillian.tutorial.database.IOrdersDatabase;
import arquillian.tutorial.entity.Orders;

public interface IOrdersService {

	void setOrdersDatabase(IOrdersDatabase ordersDatabase);

	List<Orders> findAllOrders();

	Orders findOrderById(int id);

	boolean addOrder(Orders order);

	boolean removeOrder(Orders order);

}
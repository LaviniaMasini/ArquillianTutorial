package arquillian.tutorial.service;

import java.util.List;

import javax.ejb.EJB;
import javax.enterprise.inject.Model;

import org.apache.log4j.Logger;

import arquillian.tutorial.database.IOrdersDatabase;
import arquillian.tutorial.entity.Orders;
import arquillian.tutorial.exception.DatabaseException;

@Model
public class OrdersService implements IOrdersService {

	private IOrdersDatabase ordersDatabase;

	private static final Logger LOGGER = Logger.getLogger(OrdersService.class);

	@Override
	@EJB
	public void setOrdersDatabase(IOrdersDatabase ordersDatabase) {
		this.ordersDatabase = ordersDatabase;
	}

	@Override
	public List<Orders> findAllOrders() {
		return ordersDatabase.getAllOrders();
	}

	@Override
	public Orders findOrderById(int id) {
		return ordersDatabase.getOrdersById(id);
	}

	@Override
	public boolean addOrder(Orders order) {
		try {
			ordersDatabase.add(order);
			LOGGER.info("Order added correctly");
			return true;
		} catch (DatabaseException e) {
			LOGGER.error("Error during order addition: " + e);
		}
		return false;
	}

	@Override
	public boolean removeOrder(Orders order) {
		try {
			ordersDatabase.remove(order);
			LOGGER.info("Order removed correctly");
			return true;
		} catch (DatabaseException e) {
			LOGGER.error("Error during order removal: " + e);
		}
		return false;
	}

}

package arquillian.tutorial.database;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.Query;

import arquillian.tutorial.entity.Orders;
import arquillian.tutorial.exception.DatabaseException;

@Stateless
public class OrdersDatabase extends AbstractDatabase implements IOrdersDatabase {

	@SuppressWarnings("unchecked")
	@Override
	public List<Orders> getAllOrders() {
		Query query = entityManager.createQuery("select o from Orders o");
		return query.getResultList();
	}

	@Override
	public Orders getOrdersById(int id) {
		return entityManager.find(Orders.class, id);
	}

	@Override
	public void add(Orders order) throws DatabaseException {
		try {
			entityManager.persist(order);
		} catch (Exception e) {
			throw new DatabaseException(e);
		}

	}

	@Override
	public void remove(Orders order) throws DatabaseException {
		try {
			entityManager.remove(getOrdersById(order.getId()));
		} catch (Exception e) {
			throw new DatabaseException(e);
		}

	}

}

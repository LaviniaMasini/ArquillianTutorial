package arquillian.tutorial.entity;

import java.io.Serializable;
import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "Orders")
public class Orders implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	@Column(name = "orderDate")
	private LocalDate orderDate;

	@ManyToOne(optional = false)
	@JoinColumn(name = "products_id")
	private Products product;

	@ManyToOne(optional = false)
	@JoinColumn(name = "users_id")
	private Users user;

	public Orders(LocalDate orderDate, Products product, Users user) {
		super();
		this.orderDate = orderDate;
		this.product = product;
		this.user = user;
	}

	public Orders() {
	}

	public LocalDate getOrderDate() {
		return orderDate;
	}

	public Products getProduct() {
		return product;
	}

	public Users getUser() {
		return user;
	}

	public int getId() {
		return id;
	}

}

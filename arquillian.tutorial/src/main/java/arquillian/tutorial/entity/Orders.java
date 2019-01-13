package arquillian.tutorial.entity;

import java.time.LocalDate;

public class Orders {
	
	private int id;

	private LocalDate orderDate;

	private Products product;

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

	public void setOrderDate(LocalDate orderDate) {
		this.orderDate = orderDate;
	}

	public Products getProduct() {
		return product;
	}

	public void setProduct(Products product) {
		this.product = product;
	}

	public Users getUser() {
		return user;
	}

	public void setUser(Users user) {
		this.user = user;
	}

	public int getId() {
		return id;
	}
	
	

}

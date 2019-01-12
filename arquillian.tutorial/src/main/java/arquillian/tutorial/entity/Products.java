package arquillian.tutorial.entity;

public class Products {
	
	private int id;

	private String name;
	
	private String description;

	private String category;

	private double price;

	public Products(String name, String description, String category, double price) {
		super();
		this.name = name;
		this.description = description;
		this.category = category;
		this.price = price;
	}

	public String getName() {
		return name;
	}

	public String getDescription() {
		return description;
	}

	public String getCategory() {
		return category;
	}

	public double getPrice() {
		return price;
	}

	public int getId() {
		return id;
	}
	
	

}

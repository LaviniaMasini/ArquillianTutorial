package arquillian.tutorial.functional;

import static org.junit.Assert.*;

import java.net.URL;
import java.time.LocalDate;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.drone.api.annotation.Drone;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.junit.InSequence;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.Filters;
import org.jboss.shrinkwrap.api.GenericArchive;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.importer.ExplodedImporter;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import arquillian.tutorial.database.ProductsDatabase;
import arquillian.tutorial.entity.Products;
import arquillian.tutorial.exception.DatabaseException;
import arquillian.tutorial.service.ProductsService;

@RunWith(Arquillian.class)
public class HomePageIT {
	
	@Drone
	protected WebDriver browser;
	@ArquillianResource
	protected URL url;
	
	protected static final String WEBAPP_SRC = "src/main/webapp";

	@Deployment(testable = false)
	public static Archive<?> createDeployment() {
		return ShrinkWrap.create(WebArchive.class, "HomePageIT.war").addPackage(Products.class.getPackage())
				.addPackage(ProductsService.class.getPackage()).addPackage(ProductsDatabase.class.getPackage())
				.addPackage(DatabaseException.class.getPackage()).addAsResource("scripts/import.sql")
				.merge(ShrinkWrap.create(GenericArchive.class).as(ExplodedImporter.class).importDirectory(WEBAPP_SRC)
						.as(GenericArchive.class), "/", Filters.include(".*\\.jsp$"))
				.addAsResource("test-persistence.xml", "META-INF/persistence.xml")
				.addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml");
	}

	@Test
	@InSequence(1)
	public void testMenu() {
		buildURL("/HomePageIT");
		assertMenu();
	}

	@Test
	@InSequence(2)
	public void testClickOnHomeBtn() {
		buildURL("/HomePageIT");
		browser.findElement(By.id("homeBtn")).click();
		assertMenu();
	}

	@Test
	@InSequence(3)
	public void testClickOnLoginBtn() {
		buildURL("/HomePageIT");
		browser.findElement(By.id("loginBtn")).click();
		assertEquals("Login Page", browser.getTitle());
	}
	
	@Test
	@InSequence(4)
	public void testAddProductWhenUserIsNotLogged() {
		buildURL("/HomePageIT");
		browser.findElement(By.id("ordersBtn")).click();
		assertEquals("Login Page", browser.getTitle());
	}
	
	@Test
	@InSequence(5)
	public void testUserLogin() {
		buildURL("/HomePageIT");
		browser.findElement(By.id("loginBtn")).click();
		browser.findElement(By.id("username")).sendKeys("username");
		browser.findElement(By.id("password")).sendKeys("password");
		browser.findElement(By.id("confirmBtn")).click();
	}
	
	@Test
	@InSequence(6)
	public void testAddToOrderByLoggedUser() {
		buildURL("/HomePageIT");
		browser.findElement(By.id("homeBtn")).click();
		browser.findElement(By.id("ordersBtn")).click();
		assertEquals("Order Review", browser.getTitle());
		assertUserCredentials("firstname", "lastname", "address", "email@email.com");
		assertProductInfo("name1", "description1", "category1", "1.0€");
		assertEquals(LocalDate.now().toString(), browser.findElement(By.tagName("h4")).getText());
	}

	private void assertMenu() {
		assertEquals("Home Page", browser.getTitle());
		assertEquals("Home", browser.findElement(By.id("homeBtn")).getText());
		assertEquals("Login", browser.findElement(By.id("loginBtn")).getText());
	}

	private void assertProductInfo(String name, String description, String category, String price) {
		assertEquals(name, browser.findElement(By.id("name")).getText());
		assertEquals(description, browser.findElement(By.id("description")).getText());
		assertEquals(category, browser.findElement(By.id("category")).getText());
		assertEquals(price, browser.findElement(By.id("price")).getText());
	}

	private void assertUserCredentials(String firstname, String lastname, String address, String email) {
		assertEquals(firstname, browser.findElement(By.id("firstname")).getText());
		assertEquals(lastname, browser.findElement(By.id("lastname")).getText());
		assertEquals(address, browser.findElement(By.id("address")).getText());
		assertEquals(email, browser.findElement(By.id("email")).getText());
	}
	
	private void buildURL(String path) {
		browser.get(url.toExternalForm().replaceAll(path, "").concat("arquillian.tutorial/"));
	}

}

package arquillian.tutorial.functional;

import static org.junit.Assert.*;


import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.junit.InSequence;
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

import arquillian.tutorial.database.UsersDatabase;
import arquillian.tutorial.entity.Users;
import arquillian.tutorial.exception.DatabaseException;
import arquillian.tutorial.helper.AbstractFunctionalTestHelper;
import arquillian.tutorial.service.UsersService;

@RunWith(Arquillian.class)
public class RegistrationPageIT extends AbstractFunctionalTestHelper{

	@Deployment(testable = false)
	public static Archive<?> createDeployment() {
		return ShrinkWrap.create(WebArchive.class, "RegistrationPageIT.war").addPackage(Users.class.getPackage())
				.addPackage(UsersService.class.getPackage()).addPackage(UsersDatabase.class.getPackage())
				.addPackage(DatabaseException.class.getPackage()).addAsResource("scripts/import.sql")
				.merge(ShrinkWrap.create(GenericArchive.class).as(ExplodedImporter.class).importDirectory(WEBAPP_SRC)
						.as(GenericArchive.class), "/", Filters.include(".*\\.jsp$"))
				.addAsResource("test-persistence.xml", "META-INF/persistence.xml")
				.addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml");
	}
	
	@Test
	@InSequence(1)
	public void testInsertWrongRepeatPassword() {
		buildURL("/RegistrationPageIT");
		browser.findElement(By.id("loginBtn")).click();
		browser.findElement(By.id("newUser")).click();
		assertEquals("Registration Page", browser.getTitle());
		browser.findElement(By.id("firstname")).sendKeys("firstname");
		browser.findElement(By.id("lastname")).sendKeys("lastname");
		browser.findElement(By.id("address")).sendKeys("address");
		browser.findElement(By.id("email")).sendKeys("email@email.com");
		browser.findElement(By.id("username")).sendKeys("username");
		browser.findElement(By.id("password")).sendKeys("password");
		browser.findElement(By.id("repeatedPassword")).sendKeys("password1");
		browser.findElement(By.id("confirmBtn")).click();
		assertEquals("Registration Page", browser.getTitle());
		assertEquals("The password and the repeated password must be the same",
				browser.findElement(By.id("error")).getText());
	}
	
	@Test
	@InSequence(2)
	public void testAlreadyPresentCredentials() {
		buildURL("/RegistrationPageIT");
		browser.findElement(By.id("loginBtn")).click();
		browser.findElement(By.id("newUser")).click();
		assertEquals("Registration Page", browser.getTitle());
		browser.findElement(By.id("firstname")).sendKeys("firstname");
		browser.findElement(By.id("lastname")).sendKeys("lastname");
		browser.findElement(By.id("address")).sendKeys("address");
		browser.findElement(By.id("email")).sendKeys("email@email.com");
		browser.findElement(By.id("username")).sendKeys("username");
		browser.findElement(By.id("password")).sendKeys("password");
		browser.findElement(By.id("repeatedPassword")).sendKeys("password");
		browser.findElement(By.id("confirmBtn")).click();
		assertEquals("Registration Page", browser.getTitle());
		assertEquals("These username and password are already present", browser.findElement(By.id("error")).getText());
	}
	
	@Test
	@InSequence(3)
	public void testRightDataRegistration() {
		buildURL("/RegistrationPageIT");
		browser.findElement(By.id("loginBtn")).click();
		browser.findElement(By.id("newUser")).click();
		assertEquals("Registration Page", browser.getTitle());
		browser.findElement(By.id("firstname")).sendKeys("firstname1");
		browser.findElement(By.id("lastname")).sendKeys("lastname1");
		browser.findElement(By.id("address")).sendKeys("address1");
		browser.findElement(By.id("email")).sendKeys("email@email1.com");
		browser.findElement(By.id("username")).sendKeys("username1");
		browser.findElement(By.id("password")).sendKeys("password1");
		browser.findElement(By.id("repeatedPassword")).sendKeys("password1");
		browser.findElement(By.id("confirmBtn")).click();
		assertUserSummaryPage("username1");
		browser.findElement(By.id("logoutBtn")).click();
		assertEquals("Login Page", browser.getTitle());
	}
	
	
	@Test
	public void testEmptyFirstname() {
		buildURL("/RegistrationPageIT");
		browser.findElement(By.id("loginBtn")).click();
		browser.findElement(By.id("newUser")).click();
		assertEmptyFieldMessage("firstname", "title", "Please fill out this field");

	}

	@Test
	public void testEmptyLastname() {
		buildURL("/RegistrationPageIT");
		browser.findElement(By.id("loginBtn")).click();
		browser.findElement(By.id("newUser")).click();
		assertEmptyFieldMessage("lastname", "title", "Please fill out this field");
	}

	@Test
	public void testEmptyAddress() {
		buildURL("/RegistrationPageIT");
		browser.findElement(By.id("loginBtn")).click();
		browser.findElement(By.id("newUser")).click();
		assertEmptyFieldMessage("address", "title", "Please fill out this field");
	}

	@Test
	public void testEmptyEmail() {
		buildURL("/RegistrationPageIT");
		browser.findElement(By.id("loginBtn")).click();
		browser.findElement(By.id("newUser")).click();
		assertEmptyFieldMessage("email", "title", "Please fill out this field");
	}

	@Test
	public void testEmptyUsername() {
		buildURL("/RegistrationPageIT");
		browser.findElement(By.id("loginBtn")).click();
		browser.findElement(By.id("newUser")).click();
		assertEmptyFieldMessage("username", "title", "Please fill out this field");
	}

	@Test
	public void testEmptyPassword() {
		buildURL("/RegistrationPageIT");
		browser.findElement(By.id("loginBtn")).click();
		browser.findElement(By.id("newUser")).click();
		assertEmptyFieldMessage("password", "title", "Please fill out this field");
	}

	@Test
	public void testEmptyRepeatPassword() {
		buildURL("/RegistrationPageIT");
		browser.findElement(By.id("loginBtn")).click();
		browser.findElement(By.id("newUser")).click();
		assertEmptyFieldMessage("repeatedPassword", "title", "Please fill out this field");
	}

	@Test
	public void testEmptyFirstnameAfterConfirm() {
		buildURL("/RegistrationPageIT");
		browser.findElement(By.id("loginBtn")).click();
		browser.findElement(By.id("newUser")).click();
		browser.findElement(By.id("confirmBtn")).click();
		assertEmptyFieldMessage("firstname", "validationMessage", "Please insert your Firstname");

	}

	@Test
	public void testEmptyLastnameAfterConfirm() {
		buildURL("/RegistrationPageIT");
		browser.findElement(By.id("loginBtn")).click();
		browser.findElement(By.id("newUser")).click();
		browser.findElement(By.id("confirmBtn")).click();
		assertEmptyFieldMessage("lastname", "validationMessage", "Please insert your Lastname");
	}

	@Test
	public void testEmptyAddressAfterConfirm() {
		buildURL("/RegistrationPageIT");
		browser.findElement(By.id("loginBtn")).click();
		browser.findElement(By.id("newUser")).click();
		browser.findElement(By.id("confirmBtn")).click();
		assertEmptyFieldMessage("address", "validationMessage", "Please insert your Address");
	}

	@Test
	public void testEmptyEmailAfterConfirm() {
		buildURL("/RegistrationPageIT");
		browser.findElement(By.id("loginBtn")).click();
		browser.findElement(By.id("newUser")).click();
		browser.findElement(By.id("confirmBtn")).click();
		assertEmptyFieldMessage("email", "validationMessage", "Please insert valid email");
	}

	@Test
	public void testEmptyUsernameAfterConfirm() {
		buildURL("/RegistrationPageIT");
		browser.findElement(By.id("loginBtn")).click();
		browser.findElement(By.id("newUser")).click();
		browser.findElement(By.id("confirmBtn")).click();
		assertEmptyFieldMessage("username", "validationMessage", "Please insert your Username");
	}

	@Test
	public void testEmptyPasswordAfterConfirm() {
		buildURL("/RegistrationPageIT");
		browser.findElement(By.id("loginBtn")).click();
		browser.findElement(By.id("newUser")).click();
		browser.findElement(By.id("confirmBtn")).click();
		assertEmptyFieldMessage("password", "validationMessage", "Please insert your Password");
	}

	@Test
	public void testEmptyRepeatPasswordAfterConfirm() {
		buildURL("/RegistrationPageIT");
		browser.findElement(By.id("loginBtn")).click();
		browser.findElement(By.id("newUser")).click();
		browser.findElement(By.id("confirmBtn")).click();
		assertEmptyFieldMessage("repeatedPassword", "validationMessage", "Please repeat your Password");
	}
	


}

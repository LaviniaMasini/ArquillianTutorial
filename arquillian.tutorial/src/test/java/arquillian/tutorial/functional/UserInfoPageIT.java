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
public class UserInfoPageIT extends AbstractFunctionalTestHelper {

	@Deployment(testable = false)
	public static Archive<?> createDeployment() {
		return ShrinkWrap.create(WebArchive.class, "UserInfoPageIT.war").addPackage(Users.class.getPackage())
				.addPackage(UsersService.class.getPackage()).addPackage(UsersDatabase.class.getPackage())
				.addPackage(DatabaseException.class.getPackage()).addAsResource("scripts/import.sql")
				.merge(ShrinkWrap.create(GenericArchive.class).as(ExplodedImporter.class).importDirectory(WEBAPP_SRC)
						.as(GenericArchive.class), "/", Filters.include(".*\\.jsp$"))
				.addAsResource("test-persistence.xml", "META-INF/persistence.xml")
				.addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml");
	}	

	@Test
	@InSequence(1)
	public void testLogin() {
		buildURL("/UserInfoPageIT");
		browser.findElement(By.id("loginBtn")).click();
		browser.findElement(By.id("username")).sendKeys("username");
		browser.findElement(By.id("password")).sendKeys("password");
		browser.findElement(By.id("confirmBtn")).click();
		assertUserSummaryPage("username");
	}
	
	@Test
	@InSequence(2)
	public void testPageTitle() {
		buildURL("/UserInfoPageIT");
		browser.findElement(By.id("welcomeBtn")).click();
		browser.findElement(By.id("userInfo")).click();
		assertEquals("Update your credentials", browser.getTitle());

	}
	
	@Test
	@InSequence(3)
	public void testUserInfo() {
		buildURL("/UserInfoPageIT");
		browser.findElement(By.id("welcomeBtn")).click();
		browser.findElement(By.id("userInfo")).click();
		assertInfo("firstname", "lastname", "address", "email@email.com");

	}
	
	@Test
	@InSequence(6)
	public void testEmptyFirstnameAfterConfirm() {
		buildURL("/UserInfoPageIT");
		browser.findElement(By.id("welcomeBtn")).click();
		browser.findElement(By.id("userInfo")).click();
		browser.findElement(By.id("firstname")).clear();
		browser.findElement(By.id("confirmBtn")).click();
		assertEmptyFieldMessage("firstname", "validationMessage", "Please insert valid Firstname");

	}

	@Test
	@InSequence(7)
	public void testEmptyLastnameAfterConfirm() {
		buildURL("/UserInfoPageIT");
		browser.findElement(By.id("welcomeBtn")).click();
		browser.findElement(By.id("userInfo")).click();
		browser.findElement(By.id("lastname")).clear();
		browser.findElement(By.id("confirmBtn")).click();
		assertEmptyFieldMessage("lastname", "validationMessage", "Please insert valid Lastname");
	}

	@Test
	@InSequence(8)
	public void testEmptyAddressAfterConfirm() {
		buildURL("/UserInfoPageIT");
		browser.findElement(By.id("welcomeBtn")).click();
		browser.findElement(By.id("userInfo")).click();
		browser.findElement(By.id("address")).clear();
		browser.findElement(By.id("confirmBtn")).click();
		assertEmptyFieldMessage("address", "validationMessage", "Please insert valid Address");
	}

	@Test
	@InSequence(9)
	public void testEmptyEmailAfterConfirm() {
		buildURL("/UserInfoPageIT");
		browser.findElement(By.id("welcomeBtn")).click();
		browser.findElement(By.id("userInfo")).click();
		browser.findElement(By.id("email")).clear();
		browser.findElement(By.id("confirmBtn")).click();
		assertEmptyFieldMessage("email", "validationMessage", "Please insert valid Email");
	}
	
	private void assertInfo(String firstname, String lastname, String address, String email) {
		assertEquals(firstname, browser.findElement(By.id("firstname")).getAttribute("value"));
		assertEquals(lastname, browser.findElement(By.id("lastname")).getAttribute("value"));
		assertEquals(address, browser.findElement(By.id("address")).getAttribute("value"));
		assertEquals(email, browser.findElement(By.id("email")).getAttribute("value"));
	}

}

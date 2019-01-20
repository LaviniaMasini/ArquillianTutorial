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
public class LoginPageIT extends AbstractFunctionalTestHelper {

	@Deployment(testable = false)
	public static Archive<?> createDeployment() {
		return ShrinkWrap.create(WebArchive.class, "LoginPageIT.war").addPackage(Users.class.getPackage())
				.addPackage(UsersService.class.getPackage()).addPackage(UsersDatabase.class.getPackage())
				.addPackage(DatabaseException.class.getPackage()).addAsResource("scripts/import.sql")
				.merge(ShrinkWrap.create(GenericArchive.class).as(ExplodedImporter.class).importDirectory(WEBAPP_SRC)
						.as(GenericArchive.class), "/", Filters.include(".*\\.jsp$"))
				.addAsResource("test-persistence.xml", "META-INF/persistence.xml")
				.addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml");
	}

	@Test
	@InSequence(1)
	public void testUncorrectLogin() {
		buildURL("/LoginPageIT");
		browser.findElement(By.id("loginBtn")).click();
		browser.findElement(By.id("username")).sendKeys("prova");
		browser.findElement(By.id("password")).sendKeys("prova");
		browser.findElement(By.id("confirmBtn")).click();
		assertEquals("Invalid username or password", browser.findElement(By.id("error")).getText());
	}

	@Test
	@InSequence(2)
	public void testRightLogin() {
		buildURL("/LoginPageIT");
		browser.findElement(By.id("loginBtn")).click();
		browser.findElement(By.id("username")).sendKeys("username");
		browser.findElement(By.id("password")).sendKeys("password");
		browser.findElement(By.id("confirmBtn")).click();
		assertUserSummaryPage();
	}

	@Test
	@InSequence(3)
	public void testLogoutFromMenu() {
		buildURL("/LoginPageIT");
		browser.findElement(By.id("welcomeBtn")).click();
		browser.findElement(By.id("logout")).click();
		assertEquals("Login Page", browser.getTitle());

	}

	@Test
	public void testEmptyUsernameLoginPage() {
		buildURL("/LoginPageIT");
		browser.findElement(By.id("loginBtn")).click();
		assertEmptyFieldMessage("username", "title", "Please fill out this field");

	}

	@Test
	public void testEmptyPasswordLoginPage() {
		buildURL("/LoginPageIT");
		browser.findElement(By.id("loginBtn")).click();
		assertEmptyFieldMessage("password", "title", "Please fill out this field");

	}

	@Test
	public void testEmptyUsernameLoginPageAfterConfirm() {
		buildURL("/LoginPageIT");
		browser.findElement(By.id("loginBtn")).click();
		browser.findElement(By.id("confirmBtn")).click();
		assertEmptyFieldMessage("username", "validationMessage", "Please insert valid Username");

	}

	@Test
	public void testEmptyPasswordLoginPageAfterConfirm() {
		buildURL("/LoginPageIT");
		browser.findElement(By.id("loginBtn")).click();
		browser.findElement(By.id("confirmBtn")).click();
		assertEmptyFieldMessage("password", "validationMessage", "Please insert valid Password");

	}

	private void assertUserSummaryPage() {
		assertEquals("User Home", browser.getTitle());
		assertEquals("Welcome username", browser.findElement(By.id("welcomeTitle")).getText());
		assertEquals("Welcome username", browser.findElement(By.id("welcomeBtn")).getText());
		browser.findElement(By.id("welcomeBtn")).click();
		assertEquals("User Info", browser.findElement(By.id("userInfo")).getText());
		assertEquals("Logout", browser.findElement(By.id("logout")).getText());
		assertEquals("User Info", browser.findElement(By.id("userInfoBtn")).getText());
		assertEquals("Logout", browser.findElement(By.id("logoutBtn")).getText());
	}

}

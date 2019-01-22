package arquillian.tutorial.helper;

import static org.junit.Assert.assertEquals;

import java.net.URL;

import org.jboss.arquillian.drone.api.annotation.Drone;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;

public class AbstractFunctionalTestHelper {

	@Drone
	protected WebDriver browser;
	@ArquillianResource
	protected URL url;

	protected static final String WEBAPP_SRC = "src/main/webapp";

	public AbstractFunctionalTestHelper() {
		super();
	}

	protected void buildURL(String path) {
		browser.get(url.toExternalForm().replaceAll(path, "").concat("arquillian.tutorial/"));
	}

	protected void assertEmptyFieldMessage(String field, String attribute, String msg) {
		Actions action = new Actions(browser);
		WebElement webElement = browser.findElement(By.id(field));
		action.clickAndHold(webElement).perform();
		String message = webElement.getAttribute(attribute);
		assertEquals(msg, message);
	}

	protected void assertUserSummaryPage(String username) {
		assertEquals("User Home", browser.getTitle());
		assertEquals("Welcome " + username, browser.findElement(By.id("welcomeTitle")).getText());
		assertEquals("Welcome " + username, browser.findElement(By.id("welcomeBtn")).getText());
		browser.findElement(By.id("welcomeBtn")).click();
		assertEquals("User Info", browser.findElement(By.id("userInfo")).getText());
		assertEquals("Logout", browser.findElement(By.id("logout")).getText());
		assertEquals("User Info", browser.findElement(By.id("userInfoBtn")).getText());
		assertEquals("Logout", browser.findElement(By.id("logoutBtn")).getText());
	}

}
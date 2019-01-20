package arquillian.tutorial.functional;

import static org.junit.Assert.*;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
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

import arquillian.tutorial.helper.AbstractFunctionalTestHelper;

@RunWith(Arquillian.class)
public class ErrorPageIT extends AbstractFunctionalTestHelper{

	@Deployment(testable = false)
	public static Archive<?> createDeployment() {
		return ShrinkWrap.create(WebArchive.class, "ErrorPageIT.war")
				.merge(ShrinkWrap.create(GenericArchive.class).as(ExplodedImporter.class).importDirectory(WEBAPP_SRC)
						.as(GenericArchive.class), "/", Filters.include(".*\\.jsp$"))
				.addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml");
	}

	@Test
	public void testErrorPage() {
		buildURLError("ErrorPageIT");
		assertEquals("Error Page", browser.getTitle());
		assertEquals("Something has gone wrong. There was an error...", browser.findElement(By.tagName("h3")).getText());
		
	}
	
	private void buildURLError(String path) {
		browser.get(url.toExternalForm().replaceAll(path, "").concat("arquillian.tutorial/prova"));
	}

}

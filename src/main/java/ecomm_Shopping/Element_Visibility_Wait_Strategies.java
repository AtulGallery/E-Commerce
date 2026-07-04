package ecomm_Shopping;

import java.time.Duration;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class Element_Visibility_Wait_Strategies {

	WebDriver driver;
	WebDriverWait wait;

	@BeforeMethod
	public void SetUp() {
		driver = new ChromeDriver();
		driver.get("https://rahulshettyacademy.com/AutomationPractice/");
		driver.manage().window().maximize();
		wait = new WebDriverWait(driver, Duration.ofSeconds(10));
	}

	//@Test
	public void ElementPresentbutNotVisible() {

		WebElement hiddenList = driver.findElement(By.id("ui-id-1"));
		boolean isVisibleBeforeTrigger = hiddenList.isDisplayed();
		System.out.println("Before typing — element found, isDisplayed(): " + isVisibleBeforeTrigger);

		try {
			driver.findElement(By.id("this-id-does-not-exist-12345"));
		} catch (NoSuchElementException e) {
			System.out.println("Caught NoSuchElementException — element not in DOM at all: " + e.getMessage());
		}

		WebElement autocompleteInput = driver.findElement(By.id("autocomplete"));
		autocompleteInput.sendKeys("ind");

		wait.until(ExpectedConditions.visibilityOf(hiddenList));

		boolean isVisibleAfterTrigger = hiddenList.isDisplayed();
		System.out.println("After typing — isDisplayed(): " + isVisibleAfterTrigger); // true

		Assert.assertTrue(isVisibleAfterTrigger, "Suggestion list should be visible after typing.");

	}

	public void clickWithRetry(By locator, int maxRetries) {

		int attempts = 0;

		while (attempts < maxRetries) {
			try {

				WebElement element = driver.findElement(locator);
				element.click();
				System.out.println("Click succeeded on attempt " + (attempts + 1));
				return;

			} catch (StaleElementReferenceException e) {
				attempts++;
				System.out.println("Stale element on attempt " + attempts + ", retrying...");

				if (attempts == maxRetries) {

					throw new RuntimeException("Element remained stale after " + maxRetries + " attempts: " + locator,
							e);
				}
			}
		}
	}
	@Test
	public void staleElementRetryDemo() {

	    By radioLocator = By.xpath("//legend[text()='Radio Button Example']/following-sibling::label[1]/input");

	    // Force a real staleness scenario
	    driver.findElement(radioLocator); // get initial reference (will become stale)
	    driver.navigate().refresh();       // DOM rebuilds — old reference now stale

	    // ✅ Use the retry helper instead of clicking the old (now-stale) reference directly
	    clickWithRetry(radioLocator, 3);
	    
	}
	@Test
	public void customWaitConditionForTextChange() {

	    WebElement nameInput = driver.findElement(By.id("name"));

	    // ✅ Capture the INITIAL value before any change
	    String initialValue = nameInput.getAttribute("value"); // empty string initially

	    nameInput.sendKeys("Atul"); // trigger the change

	    // ✅ Custom lambda-based ExpectedCondition — waits until value differs from initial
	    wait.until((WebDriver driver) -> {
	        String currentValue = nameInput.getAttribute("value");
	        return !currentValue.equals(initialValue); // true once it's different
	    });

	    System.out.println("Text changed from '" + initialValue + "' to '" 
	        + nameInput.getAttribute("value") + "'");
	}

	@AfterMethod
	public void TearDown() {
		if (driver != null) {
			driver.quit();
		}
	}

}

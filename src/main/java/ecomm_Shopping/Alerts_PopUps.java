package ecomm_Shopping;

import java.time.Duration;

import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.NoAlertPresentException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/*Part A — Alert Button

Enter your name "John" in the input field
Click the Alert button
Wait for the alert to appear — no Thread.sleep()
Read and print the alert message text
Assert the alert message contains "John"
Accept the alert
Assert that after accepting, the page is back to normal — verify the input field is still visible

Part B — Confirm Button

Enter "John" in the input field
Click the Confirm button
Wait for the alert to appear
Read and print the alert message
Dismiss the alert (click Cancel)
Assert the alert is dismissed — page does not crash
Then repeat — click Confirm again, but this time Accept it
Assert the message changes on the page after accepting (hint: look at what text appears below the confirm button after you accept/dismiss)

Part C — Edge Cases

What happens if you click Alert without entering a name? Handle it — assert the alert message is different
Handle NoAlertPresentException — if alert never appears, fail with a meaningful message
*/
public class Alerts_PopUps {

	WebDriver driver;
	WebDriverWait wait;

	@BeforeMethod
	public void setup() {
		driver = new ChromeDriver();
		driver.get("https://rahulshettyacademy.com/AutomationPractice/");
		driver.manage().window().maximize();
		wait = new WebDriverWait(driver, Duration.ofSeconds(10));

	}

	@Test
	public void switchToAlert() {
		WebElement nameInput = driver.findElement(
				By.xpath("//legend[text()='Switch To Alert Example']/following-sibling::input[@id='name']"));
		nameInput.sendKeys("John");

		WebElement alertButton = driver.findElement(
				By.xpath("//legend[text()='Switch To Alert Example']/following-sibling::input[@id='alertbtn']"));
		alertButton.click();

		Alert webAlert = wait.until(ExpectedConditions.alertIsPresent());

		String alertText = webAlert.getText();
		System.out.println(alertText);
		// Assert.assertEquals(alertText.contains("John"), true,"john text contains in
		// the alert message") ;
		Assert.assertTrue(alertText.contains("John"), "Alert message should contain John");
		webAlert.accept();
		Assert.assertTrue(nameInput.isDisplayed(), "Input field should still be visible after alert is accepted.");

	}

	@Test
	public void switchtoAlertConfirmBtn() {

	    WebElement nameInput = driver.findElement(By.id("name"));
	    nameInput.sendKeys("John");

	    WebElement confirmButton = driver.findElement(By.id("confirmbtn"));

	    
	    confirmButton.click();
	    Alert firstAlert = wait.until(ExpectedConditions.alertIsPresent());
	    String firstAlertText = firstAlert.getText();
	    System.out.println("Alert text: " + firstAlertText);
	    firstAlert.dismiss();

	    
	    Assert.assertTrue(nameInput.isDisplayed(),
	        "Input should still be visible after dismissing.");

	    
	    wait.until(ExpectedConditions.elementToBeClickable(confirmButton)).click();
	    Alert secondAlert = wait.until(ExpectedConditions.alertIsPresent()); 
	    String secondAlertText = secondAlert.getText();
	    System.out.println("Alert text again: " + secondAlertText);
	    secondAlert.accept();

	   
	    Assert.assertEquals(firstAlertText, secondAlertText,
	        "Both confirm alerts should show the same message.");

	    
	    Assert.assertTrue(nameInput.isDisplayed(),
	        "Input should still be visible after accepting.");
	}

	@Test
	public void NoAlertnamePresent() {
		driver.findElement(By.id("alertbtn")).click();

		try {
			Alert alert = wait.until(ExpectedConditions.alertIsPresent());
			String alertTextNoName = alert.getText();
			System.out.println("Alert with no name: " + alertTextNoName);

			
			Assert.assertFalse(alertTextNoName.contains("John"),
					"Alert should not contain 'John' when no name is entered.");

			alert.accept();

		} catch (NoAlertPresentException e) {
			Assert.fail("Alert did not appear after clicking Alert button: " + e.getMessage());
		}
	}

	@AfterMethod
	public void tearDown() {
		if (driver != null) {
			driver.quit();
		}
	}

}
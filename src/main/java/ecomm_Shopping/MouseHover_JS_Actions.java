package ecomm_Shopping;

import java.time.Duration;

import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class MouseHover_JS_Actions {

	WebDriver driver;
	WebDriverWait wait;

	@BeforeMethod
	public void SetUp() {

		driver = new ChromeDriver();
		driver.get("https://rahulshettyacademy.com/AutomationPractice/");
		driver.manage().window().maximize();

		wait = new WebDriverWait(driver, Duration.ofSeconds(10));

	}

	@Test
	public void RadioButtonExample() {

		Actions act = new Actions(driver);

		WebElement legendButton = driver.findElement(By.xpath("//legend[text()='Radio Button Example']"));
		WebElement radio2Btn = driver.findElement(
				By.xpath("//legend[text()='Radio Button Example']/following-sibling::label[2]/input[@value='radio2']"));
		act.moveToElement(legendButton).click(radio2Btn).build().perform();

		Assert.assertTrue(radio2Btn.isSelected(), "radio 2 button is successfully selected");
		
	}
	
	@Test
	public void javaScriptExecutorDemo() {

	    JavascriptExecutor js = (JavascriptExecutor) driver;

	    // B.1 — Scroll to section using JS
	    WebElement alertSection = driver.findElement(By.xpath("//legend[text()='Switch To Alert Example']"));
	    js.executeScript("arguments[0].scrollIntoView(true);", alertSection);

	    // B.2 — Get title via JS, compare with Selenium's getTitle()
	    String jsPageTitle = (String) js.executeScript("return document.title;");
	    String seleniumTitle = driver.getTitle();
	    Assert.assertEquals(jsPageTitle, seleniumTitle, "Both titles should match");

	    // B.3 — Set input value directly via JS, bypassing sendKeys()
	    WebElement inputName = driver.findElement(By.id("name"));
	    js.executeScript("arguments[0].value = 'NotAtulAsJS';", inputName);

	    String actualValue = inputName.getAttribute("value");
	    Assert.assertEquals(actualValue, "NotAtulAsJS", "Value should be set via JS");

	    // B.4 — Click via JS, then handle the resulting alert
	    WebElement alertBtn = driver.findElement(By.id("alertbtn"));
	    js.executeScript("arguments[0].click();", alertBtn);

	    Alert alert = wait.until(ExpectedConditions.alertIsPresent());
	    System.out.println("Alert text: " + alert.getText());
	    alert.accept();

	    // B.5 — Check DOM presence using querySelector
	    Object element = js.executeScript("return document.querySelector('#name');");
	    Assert.assertNotNull(element, "Element with id 'name' should exist in the DOM.");
	}

	@AfterMethod
	public void TearDown() {
		if (driver != null) {
			driver.quit();
		}
	}

}

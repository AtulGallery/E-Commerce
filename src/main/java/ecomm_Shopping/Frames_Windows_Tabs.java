package ecomm_Shopping;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchWindowException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class Frames_Windows_Tabs {

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
	public void newWindow() {

		String parentID = driver.getWindowHandle();

		WebElement openNewWindow = driver.findElement(
				By.xpath("//legend[text()='Switch Window Example']/following-sibling::button[@id='openwindow']"));
		openNewWindow.click();

		wait.until(ExpectedConditions.numberOfWindowsToBe(2));

		Set<String> allWindows = driver.getWindowHandles();

		List<String> childwindows = new ArrayList<String>(); // this is used in case of multiple child windows

		for (String handle : allWindows) {
			if (!handle.equals(parentID)) {
				childwindows.add(handle);
			}
		}
		String childTitle = "";
		for (String childHandle : childwindows) {
			driver.switchTo().window(childHandle);

			childTitle = driver.getTitle();
			String childURL = driver.getCurrentUrl();

			System.out.println("Title :" + childTitle + "URL :" + childURL);
			driver.close();
		}

		switchBacktoOriginalWindow(parentID);
		String parentTitle = driver.getTitle();

		Assert.assertNotEquals(childTitle, parentTitle, "Both the window titles are not equal");

		WebElement ParentPage = driver.findElement(By.xpath("//legend[text()='Switch Window Example']"));
		String textParentPage = ParentPage.getText().trim();
		System.out.println(textParentPage);

		Assert.assertTrue(ParentPage.isDisplayed(), "Original window element should be visible after switching back.");

	}
	@Test
	public void Newtab() {
		
		String parentTab = driver.getWindowHandle();
		
		driver.findElement(By.id("opentab")).click();
		
		wait.until(ExpectedConditions.numberOfWindowsToBe(2));
		
		Set<String> allTabs = driver.getWindowHandles();
		
		
		for(String childhandle : allTabs) {
			if(!childhandle.equals(parentTab)) {
			driver.switchTo().window(childhandle);
			String childtabTitle = driver.getTitle();
		String childtabURL = 	driver.getCurrentUrl();
		System.out.println("ChildTab Title :" + childtabTitle + "childtabURL :"+ childtabURL);
		driver.close();
			break;
			}
			
		}
		switchBacktoOriginalWindow(parentTab);
		
		
		WebElement insertedName = driver.findElement(By.id("name"));
		 insertedName.clear();	
		insertedName.sendKeys("Atul");
		String finalInsertedText = insertedName.getAttribute("value").trim();
		
		String inputText = "Atul";
		
		Assert.assertEquals(finalInsertedText, inputText, "Both the inserted text and input text are equal");
	}
	@Test
	public void EdgeCases_invalidWindowHandle() {
		
		try {
			 driver.switchTo().window("fake-handle-123");
			  Assert.fail("Expected NoSuchWindowException was not thrown.");
		}
		catch(NoSuchWindowException e ) {
			System.out.println("Correctly caught NoSuchWindowException: " + e.getMessage());
		}
			
			
	}
	
	public void switchBacktoOriginalWindow(String originalHandle) {
		try {
			driver.switchTo().window(originalHandle);
			System.out.println("Switched back to original window: " + originalHandle);
		}
		catch(NoSuchWindowException e) {
			System.out.println("Failed to switch back — handle invalid: " + e.getMessage());

		}
	}
	

	@AfterMethod
	public void teardown() {
		if (driver != null) {
			driver.quit();
		}
	}

}

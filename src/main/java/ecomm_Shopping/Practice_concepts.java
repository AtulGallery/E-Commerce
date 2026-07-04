package ecomm_Shopping;

import java.time.Duration;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;


/*
 * Write a complete Java + Selenium method that:
 * 
 * Accepts a String radioLabel as a parameter (e.g. "Radio2") Finds the correct
 * radio button dynamically using that label (no hardcoded index or ID) Clicks
 * it only if it is not already selected After clicking, asserts that it is now
 * selected Also verifies that the other two radio buttons are NOT selected
 * Handles the case where an invalid label is passed — throw a meaningful custom
 * exception
 * 
 * Constraints:
 * 
 * ❌ No Thread.sleep() ❌ No hardcoded XPath like
 * //input[@id='radio-btn-example'] ✅ Use dynamic locator strategy ✅ Use
 * WebDriverWait where needed
 */

public class Practice_concepts {
	
	
	WebDriverWait wait;
	WebDriver driver;
	
	@BeforeMethod
	public void Setup() {
		driver = new ChromeDriver();
		driver.get("https://rahulshettyacademy.com/AutomationPractice/");
		driver.manage().window().maximize();
		wait= new WebDriverWait(driver, Duration.ofSeconds(10));
	}
	public void SelectRadioButton(String radiolabel) throws Exception {
		List<WebElement> radioInputs = wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.xpath("//legend[text()='Radio Button Example']/following-sibling::label/input[@type='radio']")));
		List<WebElement> radioLabels = driver.findElements(By.xpath("//legend[text()='Radio Button Example']/following-sibling::label"));
		
		WebElement targetInput = null;
		
		for(int i=0;i<radioLabels.size();i++) {
			if(radioLabels.get(i).getText().trim().equals(radiolabel)) {
				targetInput = radioInputs.get(i);
				break;
			}
		}
		if(targetInput== null) {
			throw new IllegalArgumentException( "Invalid radio label: '" + radiolabel + "'. No matching radio button found.");
			}
		if(!targetInput.isSelected()) {
			targetInput.click();
		}
		
		Assert.assertTrue(targetInput.isSelected(), "Expected '" +radiolabel+"' to be selected but it was not ");
		
		for(int i=0;i<radioInputs.size();i++) {
			if(!radioLabels.get(i).getText().trim().equals(radiolabel)) {
				 Assert.assertFalse(radioInputs.get(i).isSelected(),"Expected '" + radioLabels.get(i).getText() + " ' to not be selected ");
			}
		}
		System.out.println("'" + radiolabel + "' selected and verified successfully.");

		
		}

	

	@Test
	public void test_RadioButton() throws Exception{
		SelectRadioButton("Radio2");
	}
		
	 @Test(expectedExceptions = IllegalArgumentException.class)
	    public void testRadioButton_invalidLabel() throws Exception {
	        SelectRadioButton("Radio99"); //
	    }	
	
	 @AfterMethod
	    public void teardown() {
	        if (driver != null) driver.quit();
	    }
				
	}


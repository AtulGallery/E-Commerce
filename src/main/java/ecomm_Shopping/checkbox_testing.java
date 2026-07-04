package ecomm_Shopping;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.annotations.Test;

public class checkbox_testing {
	@Test
	public static void clickCheckbox() {
		WebDriver driver = new ChromeDriver();
		driver.get("https://rahulshettyacademy.com/AutomationPractice/");
		driver.manage().window().maximize();
		driver.manage().timeouts().implicitlyWait(java.time.Duration.ofSeconds(5));
		
	WebElement checkbox1 = 	driver.findElement(By.id("checkBoxOption1"));
	checkbox1.click();
	System.out.println("Checkbox 1 selected: " + checkbox1.isSelected());
	
	checkbox1.click();
	System.out.println("Checkbox 1 selected after second click: " + checkbox1.isSelected());
	
	driver.manage().timeouts().implicitlyWait(java.time.Duration.ofSeconds(5));
	
	//get number of checkboxes in page
	int checkboxCount = driver.findElements(By.cssSelector("input[type='checkbox']")).size();
	System.out.println("Total checkboxes found: " + checkboxCount);
	}

}

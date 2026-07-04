package ecomm_Shopping;

import java.util.HashMap;
import java.util.Map;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.Assert;
import org.testng.annotations.Test;

public class WebTable_FixedHeader {
	@Test
	public void getCustomerData() {
		WebDriver driver = new ChromeDriver();
		driver.get("https://rahulshettyacademy.com/AutomationPractice/");
		driver.manage().window().maximize();
		driver.manage().timeouts().implicitlyWait(java.time.Duration.ofSeconds(5));

		HashMap<String, Integer> customerData = new HashMap<String, Integer>();
		WebElement tableHeader = driver.findElement(By.xpath("//legend[text()='Web Table Fixed header']"));
		
		JavascriptExecutor js = (JavascriptExecutor) driver;
		js.executeScript("arguments[0].scrollIntoView(true);", tableHeader);

		
		js.executeScript("document.querySelector('.tableFixHead').scrollTop='5000'");
		
		
		int rowCount = driver.findElements(By.xpath("//div[@class='tableFixHead']//table/tbody/tr3/*-)")).size();
		System.out.println("Total rows found: " + rowCount);
		for (int i = 1; i <= rowCount; i++) {
			String name = driver.findElement(By.xpath("//div[@class='tableFixHead']//table//tr[" + i + "]//td[1]"))
					.getText().trim();
			int Amount = Integer
					.parseInt(driver.findElement(By.xpath("//div[@class='tableFixHead']//table//tr[" + i + "]//td[4]"))
							.getText().trim());

			
			customerData.put(name, Amount);
		}

		System.out.println("\n--- Customer Data ---");

		for (Map.Entry<String, Integer> entry : customerData.entrySet()) {
			System.out.println("Name: " + entry.getKey() + " | Amount: Rs." + entry.getValue());
		}

		int calculatedTotal = customerData.values().stream().mapToInt(Integer::intValue).sum();
		System.out.println("\nCalculated Total Amount: Rs." + calculatedTotal);

		String displayedTotalText = driver.findElement(By.cssSelector(".totalAmount")).getText().trim();
		int displayedTotal = Integer.parseInt(displayedTotalText.replaceAll("[^0-9]", ""));
		System.out.println("Displayed Total Amount: Rs." + displayedTotal);

		Assert.assertEquals(calculatedTotal, displayedTotal, "Calculated total does not match displayed total!");

		driver.quit();

	}

}

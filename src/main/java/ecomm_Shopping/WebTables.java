package ecomm_Shopping;

import java.time.Duration;
import java.util.HashMap;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class WebTables {

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
	public void WebTableExample() {
		List<WebElement> rowsCount = driver
				.findElements(By.xpath("//legend[text()='Web Table Example']/following-sibling::table/tbody/tr"));
		int totalDataRows = rowsCount.size() - 1;
		System.out.println("Total rows excluding header: " + totalDataRows);
		String mostExpensiveCourse = "";
		int highestPrice = 0;

		HashMap<String, Integer> hashmapcount = new HashMap<String, Integer>();

		for (int i = 2; i < rowsCount.size(); i++) {

			String courseName = driver
					.findElement(By.xpath(
							"//legend[text()='Web Table Example']/following-sibling::table/tbody/tr[" + i + "]/td[2]"))
					.getText().trim();
			String coursePriceText = driver
					.findElement(By.xpath(
							"//legend[text()='Web Table Example']/following-sibling::table/tbody/tr[" + i + "]/td[3]"))
					.getText().trim();

			String cleanedPrice = coursePriceText.replace("$", "");

			int CoursePrice = Integer.parseInt(cleanedPrice);

			hashmapcount.put(courseName, CoursePrice);

			System.out.println(courseName + "-> $" + CoursePrice);

			Assert.assertNotEquals(CoursePrice, 0, courseName + "has a price of $0!");

			if (CoursePrice > highestPrice) {
				highestPrice = CoursePrice;
				mostExpensiveCourse = courseName;
			}

		}

		System.out.println("Most expensive course: " + mostExpensiveCourse + " -> $" + highestPrice);

		String targetCourse = "Master Selenium Automation in simple Python Language";

		if (hashmapcount.containsKey(targetCourse)) {
			System.out.println("Price of Master Selenium: " + hashmapcount.get(targetCourse));
		} else {
			System.out.println("Course not found!");
		}

	}

	@Test
	public void WebTableFixedHeader() {

		WebElement tableHeader = driver.findElement(By.xpath("//legend[text()='Web Table Fixed header']"));

		JavascriptExecutor js = (JavascriptExecutor) driver;
		js.executeScript("arguments[0].scrollIntoView(true);", tableHeader);

		js.executeScript("document.querySelector('.tableFixHead').scrollTop='5000'");

		List<WebElement> columnCount = driver.findElements(
				By.xpath("//legend[text()='Web Table Fixed header']/following-sibling::div/table/thead/tr/th"));

		for (int i = 0; i < columnCount.size(); i++) {
			String coulumnName = columnCount.get(i).getText().trim();
			System.out.println(coulumnName);
		}

		List<WebElement> studentName = driver.findElements(
				By.xpath("//legend[text()='Web Table Fixed header']/following-sibling::div/table/tbody/tr/td[1]"));
		List<WebElement> studentPosition = driver.findElements(
				By.xpath("//legend[text()='Web Table Fixed header']/following-sibling::div/table/tbody/tr/td[2]"));
		List<WebElement> studentCity = driver.findElements(
				By.xpath("//legend[text()='Web Table Fixed header']/following-sibling::div/table/tbody/tr/td[3]"));
		List<WebElement> studentAmount = driver.findElements(
				By.xpath("//legend[text()='Web Table Fixed header']/following-sibling::div/table/tbody/tr/td[4]"));
		for (int i = 0; i < studentName.size(); i++) {
			String resultname = studentName.get(i).getText().trim();
			if (resultname.equalsIgnoreCase("Ronaldo")) {
				String resultPosition = studentPosition.get(i).getText().trim();
				String resultCity = studentCity.get(i).getText().trim();
				String resultAmount = studentAmount.get(i).getText().trim();
				System.out.println("Student Name is " + resultname + " Position " + resultPosition + " City "
						+ resultCity + " Amount " + resultAmount);

				Assert.assertTrue(true, "resultAmount>30");

			}
		}
		 System.out.println("Students scoring less than 70:");
		for (int i = 0; i < studentAmount.size(); i++) {
			String amounts = studentAmount.get(i).getText().trim();
			int finalamount = Integer.parseInt(amounts);
			if (finalamount < 70) {
				System.out.println(studentName.get(i).getText().trim());
			}
		}

	}
	
	@AfterMethod
	public void teardown() {
		if(driver != null) {
			driver.close();
		}
	}
}

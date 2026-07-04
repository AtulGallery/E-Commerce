package ecomm_Shopping;

import java.time.Duration;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class iFrames {
	
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
	public void SwitchToiFrame() {
		driver.findElement(By.xpath("//legend[text()='iFrame Example']")).click();
	}

}

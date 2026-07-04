package ecomm_Shopping;

import java.io.File;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.time.Duration;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class Links_BrokenLinks {

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
	public void getAllLinks() {
		List<WebElement> allLinks = driver.findElements(By.tagName("a"));
		System.out.println("Total anchor tags found: " + allLinks.size());

		int skippedCount = 0;
		int validCount = 0;

		for (WebElement link : allLinks) {
			String hrefValue = link.getDomAttribute("href");

			if (hrefValue == null || hrefValue.isEmpty()) {
				skippedCount++;
				System.out.println("Skipped — no href: '" + link.getText() + "'");
				continue;
			}

			String displayText = link.getText();
			System.out.println("Display Text: " + displayText + " | href: " + hrefValue);
			validCount++;
			Assert.assertTrue(hrefValue.startsWith("http"), "Link href should start with http but was: " + hrefValue);

		}
		System.out.println("Valid links: " + validCount + " | Skipped (no href): " + skippedCount);

	}

	// @Test
	public void FooterLinks() {
		WebElement footerDriver = driver.findElement(By.id("gf-BIG"));
		int allLinksCount = footerDriver.findElements(By.tagName("a")).size();
		System.out.println(allLinksCount);
		WebElement CoulumnDriver = footerDriver.findElement(By.xpath("//table/tbody/tr/td[1]/ul"));

		System.out.println(CoulumnDriver.findElements(By.tagName("a")).size());

		for (int i = 1; i < CoulumnDriver.findElements(By.tagName("a")).size(); i++) {
			String clickonLinktab = Keys.chord(Keys.CONTROL, Keys.ENTER);

			CoulumnDriver.findElements(By.tagName("a")).get(i).sendKeys(clickonLinktab);

		}
		Set<String> abc = driver.getWindowHandles(); // 4
		Iterator<String> it = abc.iterator();

		while (it.hasNext()) {
			driver.switchTo().window(it.next());
			System.out.println(driver.getTitle());
		}
	}

	@Test
	public void getBrokenLinks() throws MalformedURLException, IOException, URISyntaxException {

		List<WebElement> allLinks = driver.findElements(By.tagName("a"));
		int workingLinks = 0;
		int brokenLinks = 0;
		int skippedLinks = 0;
		for (int i = 0; i < allLinks.size(); i++) {
			String url = allLinks.get(i).getDomAttribute("href");

			if (url == null || url.isEmpty() || !url.startsWith("http")) {
				skippedLinks++;
				System.out.println("Skipped (no valid href): " + url);
				continue;
			}

			HttpURLConnection conn = (HttpURLConnection) new URI(url).toURL().openConnection();
			conn.setRequestMethod("HEAD");
			conn.connect();
			int responsecode = conn.getResponseCode();
			conn.disconnect();

			if (responsecode >= 200 && responsecode < 400) {
				workingLinks++;
				System.out.println(url + " -> " + responsecode + " OK");
			} else {
				brokenLinks++;
				System.out.println(url + " -> " + responsecode + " BROKEN");
			}

		}
		System.out.println("Total: " + allLinks.size() + " | Working: " + workingLinks + " | Broken: " + brokenLinks
				+ " | Skipped: " + skippedLinks);

		Assert.assertEquals(brokenLinks, 0, "broken lists are not 0" + brokenLinks);
	}

	@Test
	public void headerVerification() {

	    // ✅ Part C.1 — Get all header nav buttons dynamically
	    List<WebElement> allButtons = driver.findElements(
	        By.xpath("//header[@class='jumbotron text-center header_style']/div/button"));

	    // ✅ Part C.2 — Assert exactly 3 buttons (Practice, Login, Signup)
	    Assert.assertEquals(allButtons.size(), 3,
	        "Expected 3 buttons in header but found: " + allButtons.size());

	    // ✅ Part C.3 — Verify each button is visible and clickable
	    for (WebElement button : allButtons) {
	        Assert.assertTrue(button.isDisplayed(),
	            button.getText() + " button should be visible");
	        Assert.assertTrue(button.isEnabled(),
	            button.getText() + " button should be enabled");
	        System.out.println("Button verified: " + button.getText());
	    }

	    // ✅ Part C.4 — Click Home link (opens new window), assert URL changed, come back
	    String originalHandle = driver.getWindowHandle();
	    String originalUrl = driver.getCurrentUrl();

	    WebElement homeLink = driver.findElement(
	        By.xpath("//header[@class='jumbotron text-center header_style']//a"));
	    homeLink.click();

	    // Wait for new window to appear
	    wait.until(ExpectedConditions.numberOfWindowsToBe(2));

	    // Switch to new window
	    Set<String> allHandles = driver.getWindowHandles();
	    for (String handle : allHandles) {
	        if (!handle.equals(originalHandle)) {
	            driver.switchTo().window(handle);
	            break;
	        }
	    }

	    // ✅ Assert URL is different from original
	    String newWindowUrl = driver.getCurrentUrl();
	    Assert.assertNotEquals(newWindowUrl, originalUrl,
	        "New window URL should differ from original page URL");
	    System.out.println("New window URL: " + newWindowUrl);

	    // ✅ Close new window and switch back
	    driver.close();
	    driver.switchTo().window(originalHandle);
	    System.out.println("Back on original page: " + driver.getCurrentUrl());

	    // ✅ Part C.5 — Assert Login button text is exact match
	    WebElement loginButton = driver.findElement(By.xpath("//button[text()='Login']"));
	    String loginText = loginButton.getText();
	    Assert.assertEquals(loginText, "Login",
	        "Login button text should be exactly 'Login' — case sensitive");
	    System.out.println("Login button text verified: " + loginText);
	}	
	@Test
	public void footerVerification() {

	    JavascriptExecutor js = (JavascriptExecutor) driver;

	    // ✅ Find footer element
	    WebElement footerSection = driver.findElement(By.xpath("//div[@class='footer-distributed']"));

	    // ✅ Scroll to footer FIRST before interacting
	    js.executeScript("arguments[0].scrollIntoView(true);", footerSection);

	    // ✅ Assert footer is visible
	    Assert.assertTrue(footerSection.isDisplayed(), "Footer should be visible on the page");

	    // ✅ Print full footer text
	    String footerText = footerSection.getText();
	    System.out.println("Footer content: " + footerText);

	    // ✅ Assert footer contains "Rahul Shetty"
	    Assert.assertTrue(footerText.contains("Rahul Shetty"),
	        "Footer should mention Rahul Shetty");

	    // ✅ Get all footer links and print them
	    List<WebElement> footerLinks = footerSection.findElements(By.tagName("a"));
	    System.out.println("Total footer links: " + footerLinks.size());

	    for (WebElement link : footerLinks) {
	        String linkText = link.getText();                    // ✅ get text
	        String href = link.getDomAttribute("href");          // ✅ store href properly

	        if (href == null || href.isEmpty()) {
	            System.out.println("Skipped — no href: " + linkText);
	            continue;
	        }

	        System.out.println("Footer link: " + linkText + " | href: " + href);
	    }

	    // ✅ Take and SAVE a screenshot of footer element
		/*
		 * File screenshotFile = ((TakesScreenshot)
		 * driver).getScreenshotAs(OutputType.FILE); try { //
		 * org.apache.commons.io.FileUtils.copyFile( screenshotFile, new
		 * File("screenshots/footer_screenshot.png") );
		 * System.out.println("Screenshot saved to screenshots/footer_screenshot.png");
		 * } catch (IOException e) { System.out.println("Screenshot save failed: " +
		 * e.getMessage()); }
		 */
	}	@AfterMethod
	public void TearDown() {
		if (driver != null) {
			driver.quit();
		}
	}

}

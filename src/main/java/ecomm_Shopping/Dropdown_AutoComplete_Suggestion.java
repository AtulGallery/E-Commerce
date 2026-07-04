package ecomm_Shopping;

import java.time.Duration;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class Dropdown_AutoComplete_Suggestion {

	WebDriver driver;
	WebDriverWait wait;

	@BeforeMethod
	public void setup() {
		driver = new ChromeDriver();
		driver.get("https://rahulshettyacademy.com/AutomationPractice/");
		driver.manage().window().maximize();
		wait = new WebDriverWait(driver, Duration.ofSeconds(10));
	}

	public void nativeSelectDropdown(String valueToSelect) {

		WebElement selectElement = wait.until(ExpectedConditions.presenceOfElementLocated(By
				.xpath("//legend[text()='Dropdown Example']/following-sibling::select[@id='dropdown-class-example']")));

		Select select = new Select(selectElement);
		select.selectByValue(valueToSelect);

		String selectedText = select.getFirstSelectedOption().getText().trim();

		Assert.assertEquals(selectedText, "Option2", "Selected option text did not match expected value.");

		List<WebElement> allAvailableOptions = select.getOptions();

		for (int i = 0; i < allAvailableOptions.size(); i++) {
			String optionNames = allAvailableOptions.get(i).getText().trim();

			System.out.println(optionNames);

		}
		Assert.assertEquals(allAvailableOptions.size(), 4, "Expected 4 options including placeholder.");
	}

	@Test
	public void SelectedValueinDropDown() {
		nativeSelectDropdown("option2");
	}

	public void autoCompleteSuggestion(String searchText, String exactCountry) {
		WebElement typeToSelectCountries = wait.until(ExpectedConditions.presenceOfElementLocated(
				By.xpath("//legend[text()='Suggession Class Example']/following-sibling::input[@id='autocomplete']")));
		typeToSelectCountries.clear();
		typeToSelectCountries.sendKeys(searchText);
		try {

			List<WebElement> allDisplayedCountries = wait
					.until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.xpath("//li[@class='ui-menu-item']//div")));

			WebElement exactmatch = null;

			for (WebElement country : allDisplayedCountries) {
				if (country.getText().trim().equals(exactCountry)) {
					exactmatch = country;
					break;
				}

			}
			Assert.assertNotNull(exactmatch, "'" + exactCountry + "' was not found in suggestion list.");

			exactmatch.click();

			String inputvalue = typeToSelectCountries.getAttribute("value");

			Assert.assertEquals(inputvalue, exactCountry, "Input field did not update to selected country.");

			System.out.println("Selected: " + inputvalue);
		} catch (TimeoutException e) {

			Assert.fail("Suggestions never appeared for input '" + searchText
					+ "'. Possible network issue or no matching results.");
		}
	}

	@Test
	public void testAutoComplete_validCountry() {
		autoCompleteSuggestion("ind", "India");
	}

	@Test
	public void AutoComplete_Suggestion_Box_NeverAppear() {
		autoCompleteSuggestion("indsss", "India");
	}

	@AfterMethod
	public void tearOut() {
		if (driver != null)
			driver.quit();
	}

}

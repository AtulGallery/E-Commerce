package ecomm_Shopping;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.Assert;
import org.testng.annotations.Test;

public class Shopping_Cart {

    @Test
    public void addToCart() {
        WebDriver driver = new ChromeDriver();
        driver.manage().window().maximize();
        driver.get("https://rahulshettyacademy.com/seleniumPractise/#/");
        driver.manage().timeouts().implicitlyWait(java.time.Duration.ofSeconds(5));

        HashMap<String, Integer> productMap = new HashMap<String, Integer>();

        // ✅ Fix 1: Fetch price from its own <p class="product-price"> element
        List<WebElement> productNames  = driver.findElements(By.xpath("//div[@class='products']/div/h4"));
        List<WebElement> productPrices = driver.findElements(By.xpath("//div[@class='products']/div/p[@class='product-price']"));

        for (int i = 0; i < productNames.size(); i++) {
            String fullText = productNames.get(i).getText().trim();       // "Raspberry - 1/4 Kg"

            // ✅ Fix 2: Guard against missing '-' in product name
            String[] parts = fullText.split("-");
            if (parts.length < 1) {
                System.out.println("Skipping unexpected format: " + fullText);
                continue;
            }

            String name = parts[0].trim();                                // "Raspberry"

            // ✅ Fix 3: Price extracted from separate element, not from h4 text
            int price = Integer.parseInt(productPrices.get(i).getText().replaceAll("[^0-9]", "").trim());

            productMap.put(name, price);

            if (name.equals("Cucumber") || name.equals("Brocolli") ||
                name.equals("Beetroot") || name.equals("Carrot")   ||
                name.equals("Raspberry")) {

                driver.findElement(
                    By.xpath("(//div[@class='product-action']//button[@type='button'])[" + (i + 1) + "]")).click();
            } else {
                System.out.println("Product not found: " + name);
            }
        }

        // Print all products and prices
        System.out.println("\n--- All Products on Page ---");
        for (Map.Entry<String, Integer> entry : productMap.entrySet()) {
            System.out.println("Product: " + entry.getKey() + " | Price: Rs." + entry.getValue());
        }

        // Open Cart
        driver.findElement(By.xpath("//img[@alt='Cart']")).click();
        driver.manage().timeouts().implicitlyWait(java.time.Duration.ofSeconds(5));

        // ✅ Fix 4: Re-fetch cart items after cart opens (stale element safety)
        List<WebElement> cartItems = driver.findElements(
            By.xpath("//ul[@class='cart-items']/li//p[@class='product-name']"));

        for (WebElement item : cartItems) {
            // ✅ Fix 5: Use contains() instead of split("-")[0] — safe for "Raspberry - 1/4 Kg"
            String cartProductName = item.getText().trim();

            if (cartProductName.contains("Carrot")) {
                System.out.println("\nCarrot found in cart - removing it...");
                driver.findElement(By.xpath(
                    "//ul[@class='cart-items']/li[.//p[contains(text(),'Carrot')]]//a[@class='product-remove']")).click();
                System.out.println("Carrot removed successfully!");
                break;
            } else {
                System.out.println(cartProductName + " is kept in cart.");
            }
        }

        // Proceed to Checkout
        driver.findElement(By.xpath("//button[text()='PROCEED TO CHECKOUT']")).click();
        driver.manage().timeouts().implicitlyWait(java.time.Duration.ofSeconds(5));

        // Apply Promo Code
        driver.findElement(By.xpath("//input[@placeholder='Enter promo code']")).sendKeys("rahulshettyacademy");
        driver.findElement(By.xpath("//button[text()='Apply']")).click();
        driver.manage().timeouts().implicitlyWait(java.time.Duration.ofSeconds(5));

        // Assert promo code applied
        Assert.assertTrue(
            driver.findElement(By.xpath("//span[@class='promoInfo']")).getText().contains("Code applied ..!"),
            "Promo code application failed!");

        // Place Order
        driver.findElement(By.xpath("//button[text()='Place Order']")).click();
        driver.manage().timeouts().implicitlyWait(java.time.Duration.ofSeconds(5));

        // Fill Country and Confirm
        driver.findElement(By.xpath("//select[@style='width: 200px;']")).click();
        driver.findElement(By.xpath("//option[text()='India']")).click();
        driver.findElement(By.xpath("//input[@type='checkbox']")).click();
        driver.findElement(By.xpath("//button[text()='Proceed']")).click();
        driver.manage().timeouts().implicitlyWait(java.time.Duration.ofSeconds(5));

        // Assert order placed successfully
        Assert.assertTrue(
            driver.findElement(By.xpath("//div[@class='wrapperTwo']//h1")).getText()
                .contains("Thank you, your order has been placed successfully"),
            "Order placement failed!");

        driver.quit();
    }
}
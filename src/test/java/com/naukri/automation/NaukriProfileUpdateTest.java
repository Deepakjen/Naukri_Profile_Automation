package com.naukri.automation;

import java.io.File;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

public class NaukriProfileUpdateTest {
	
	private WebDriver driver;
	private WebDriverWait wait;

	@BeforeClass
	public void setup() throws InterruptedException {
		ChromeOptions options = new ChromeOptions();
        
        // Headless setup with realistic behavior
        options.addArguments("--headless=new"); // Use 'new' to avoid old headless issues
        options.addArguments("--disable-gpu");
        options.addArguments("--window-size=1920,1080");
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage");

        // Fake a user agent (browser fingerprint)
        options.addArguments("user-agent=Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 "
                + "(KHTML, like Gecko) Chrome/114.0.5735.91 Safari/537.36");

        // Prevent detection of headless
        options.setExperimentalOption("excludeSwitches", new String[]{"enable-automation"});
        options.setExperimentalOption("useAutomationExtension", false);
	
        driver = new ChromeDriver(options);
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        driver.manage().window().maximize();
        driver.get("https://www.naukri.com/nlogin/login?URL=https://www.naukri.com/mnjuser/homepage");

        // Optional: remove navigator.webdriver flag via JS
        ((JavascriptExecutor) driver).executeScript(
                "Object.defineProperty(navigator, 'webdriver', {get: () => undefined})"
        );
        

        WebElement emailField = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//input[@type='text' and contains(@placeholder, 'Email ID')]")));
        emailField.sendKeys("deepakjena903@gmail.com");

        WebElement passwordField = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//input[@type='password']")));
        passwordField.sendKeys("Deepak@123");

        WebElement loginButton = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//button[@type='submit' and text()='Login']")));
        loginButton.click();

        Thread.sleep(10);
        // Wait and click the 3-bar menu
        WebElement hamburgerMenu = wait.until(ExpectedConditions.elementToBeClickable(
            By.cssSelector("div.nI-gNb-drawer__bars")));
        hamburgerMenu.click();
        
        // Step 2: Wait for drawer to be visible
        wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.cssSelector("div.naukri-drawer.right.open")));

        // Step 3: Click on "View & Update Profile"
        WebElement updateProfileLink = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//a[contains(text(), 'View & Update Profile')]")));
        updateProfileLink.click();
	}

	@Test
	public void updateResume() {
		WebElement updateResume = wait.until(ExpectedConditions.elementToBeClickable(
			By.xpath("//input[@value='Update resume']")));
		updateResume.click();

		String resumePath = System.getProperty("user.dir") + File.separator +
			"resources" + File.separator + "Deepak_Jena_QA_Resume_2025.pdf";

		WebElement fileInput = driver.findElement(By.xpath("//input[@type='file']"));
		fileInput.sendKeys(resumePath);
	}

	@AfterClass
	public void tearDown() {
		if (driver != null) {
			driver.quit();
		}
	}
}

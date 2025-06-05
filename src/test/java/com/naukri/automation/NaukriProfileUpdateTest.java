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

		// ✅ Mobile Emulation config (iPhone)
		Map<String, Object> deviceMetrics = new HashMap<>();
		deviceMetrics.put("width", 375);
		deviceMetrics.put("height", 812);
		deviceMetrics.put("pixelRatio", 3.0);

		Map<String, Object> mobileEmulation = new HashMap<>();
		mobileEmulation.put("deviceMetrics", deviceMetrics);
		mobileEmulation.put("userAgent", 
			"Mozilla/5.0 (iPhone; CPU iPhone OS 13_5 like Mac OS X) AppleWebKit/605.1.15 "
			+ "(KHTML, like Gecko) Version/13.1 Mobile/15E148 Safari/604.1");

		options.setExperimentalOption("mobileEmulation", mobileEmulation);

		// ✅ Headless + anti-detection
		options.addArguments("--headless=new");
		options.addArguments("--disable-gpu");
		options.addArguments("--no-sandbox");
		options.addArguments("--disable-dev-shm-usage");
		options.setExperimentalOption("excludeSwitches", new String[]{"enable-automation"});
		options.setExperimentalOption("useAutomationExtension", false);

		driver = new ChromeDriver(options);
		wait = new WebDriverWait(driver, Duration.ofSeconds(15));

		driver.get("https://www.naukri.com/nlogin/login?URL=https://www.naukri.com/mnjuser/homepage");

		// Hide navigator.webdriver
		((JavascriptExecutor) driver).executeScript(
			"Object.defineProperty(navigator, 'webdriver', {get: () => undefined})"
		);

		// Login flow
		WebElement emailField = wait.until(ExpectedConditions.elementToBeClickable(
			By.xpath("//input[@type='text' and contains(@placeholder, 'Email ID')]")));
		emailField.sendKeys("deepakjena903@gmail.com");

		WebElement passwordField = wait.until(ExpectedConditions.elementToBeClickable(
			By.xpath("//input[@type='password']")));
		passwordField.sendKeys("Deepak@123");

		WebElement loginButton = wait.until(ExpectedConditions.elementToBeClickable(
			By.xpath("//button[@type='submit' and text()='Login']")));
		loginButton.click();

		// ✅ Wait for mobile menu to be clickable
		wait.until(ExpectedConditions.presenceOfElementLocated(
			By.cssSelector("div.nI-gNb-drawer__bars")));

		Thread.sleep(2000); // Let layout settle

		WebElement menu = wait.until(ExpectedConditions.elementToBeClickable(
			By.cssSelector("div.nI-gNb-drawer__bars")));
		menu.click();

		// Wait for drawer
		wait.until(ExpectedConditions.visibilityOfElementLocated(
			By.cssSelector("div.naukri-drawer.right.open")));

		// Click "View & Update Profile"
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

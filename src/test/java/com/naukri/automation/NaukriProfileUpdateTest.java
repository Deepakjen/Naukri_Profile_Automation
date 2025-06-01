package com.naukri.automation;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.Assert;
import org.testng.annotations.*;

import java.time.Duration;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class NaukriProfileUpdateTest {
    WebDriver driver;

    @BeforeClass
    public void setup() {
        try {
            WebDriverManager.chromedriver().setup();
            driver = new ChromeDriver();
            driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
            driver.manage().window().maximize();
            driver.get("https://www.naukri.com/mnjuser/profile");

            // Fetch credentials from GitHub Secrets
            String email = System.getenv("NAUKRI_EMAIL");
            String password = System.getenv("NAUKRI_PASSWORD");

            if (email == null || email.isEmpty()) {
                throw new IllegalArgumentException("Environment variable 'NAUKRI_EMAIL' is not set or empty.");
            }
            if (password == null || password.isEmpty()) {
                throw new IllegalArgumentException("Environment variable 'NAUKRI_PASSWORD' is not set or empty.");
            }

            // Login logic
            WebElement emailField = driver.findElement(By.id("usernameField")); // Adjust this ID as per actual page
            emailField.sendKeys(email);

            WebElement continueBtn = driver.findElement(By.xpath("//button[@type='submit']"));
            continueBtn.click();

            WebElement passwordField = driver.findElement(By.id("passwordField")); // Adjust this ID as per actual page
            passwordField.sendKeys(password);

            WebElement loginBtn = driver.findElement(By.xpath("//button[@type='submit']"));
            loginBtn.click();

        } catch (Exception e) {
            e.printStackTrace();
            Assert.fail("Setup failed: " + e.getMessage());
        }
    }

    @Test(priority = 1)
    public void verifyProfilePageLoaded() {
        try {
            String currentUrl = driver.getCurrentUrl();
            Assert.assertTrue(currentUrl.contains("profile"), "Profile page did not load correctly.");
        } catch (Exception e) {
            takeScreenshot("verifyProfilePageLoaded");
            Assert.fail("Test failed: " + e.getMessage());
        }
    }

    @Test(priority = 2)
    public void updateResumeHeadline() {
        try {
            WebElement editButton = driver.findElement(By.xpath("//span[text()='Resume Headline']/following::span[text()='edit']"));
            editButton.click();

            WebElement textArea = driver.findElement(By.xpath("//textarea"));
            textArea.clear();
            textArea.sendKeys("Experienced QA Automation Engineer with expertise in Selenium, TestNG, and CI/CD pipelines.");

            WebElement saveButton = driver.findElement(By.xpath("//button[text()='Save']"));
            saveButton.click();

            Thread.sleep(2000); // Use WebDriverWait in real scenarios

        } catch (Exception e) {
            takeScreenshot("updateResumeHeadline");
            Assert.fail("Failed to update resume headline: " + e.getMessage());
        }
    }

    @Test(priority = 3)
    public void logout() {
        try {
            WebElement myNaukriDropdown = driver.findElement(By.xpath("//div[contains(@class, 'nI-gNb-drawer__user')]"));
            myNaukriDropdown.click();

            WebElement logoutButton = driver.findElement(By.linkText("Logout"));
            logoutButton.click();

        } catch (Exception e) {
            takeScreenshot("logout");
            Assert.fail("Logout failed: " + e.getMessage());
        }
    }

    @AfterClass
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }

    // Stub for screenshot (implement logic if needed)
    public void takeScreenshot(String methodName) {
        // Screenshot logic can be implemented here if needed
        System.out.println("Screenshot taken for method: " + methodName);
    }
}

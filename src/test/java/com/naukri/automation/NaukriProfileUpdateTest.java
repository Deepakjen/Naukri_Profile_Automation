package com.naukri.automation;

import java.io.File;
import java.io.IOException;
import java.time.Duration;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
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
    public void setup() {
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
        driver.get("https://www.naukri.com/");

        // Optional: remove navigator.webdriver flag via JS
        ((JavascriptExecutor) driver).executeScript(
                "Object.defineProperty(navigator, 'webdriver', {get: () => undefined})"
        );

        // Get credentials from environment variables
        String email = System.getenv("NAUKRI_EMAIL");
        String password = System.getenv("NAUKRI_PASSWORD");

        WebElement loginLayer = wait.until(ExpectedConditions.elementToBeClickable(By.id("login_Layer")));
        loginLayer.click();

        WebElement emailField = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//input[@type='text' and contains(@placeholder, 'Email ID')]")));
        emailField.sendKeys(email);

        WebElement passwordField = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//input[@type='password']")));
        passwordField.sendKeys(password);

        WebElement loginButton = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//button[contains(@class, 'loginButton')]")));
        loginButton.click();

        WebElement viewProfile = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//a[@href='/mnjuser/profile']")));
        viewProfile.click();
    }

    @Test
    public void updateResume() {
        WebElement updateResume = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//input[@value='Update resume']")));
        updateResume.click();

        // Get dynamic path
        String resumePath = System.getProperty("user.dir") + File.separator + "resources" + File.separator + "Deepak_Jena_QA_Resume_2025.pdf";

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

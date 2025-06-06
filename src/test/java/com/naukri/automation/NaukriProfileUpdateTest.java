package com.naukri.automation;

import java.time.Duration;

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
    public void setup() {
        ChromeOptions options = new ChromeOptions();

        // Headless setup with realistic behavior
        options.addArguments("--headless=new");
        options.addArguments("--disable-gpu");
        options.addArguments("--window-size=1920,1080");
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage");

        // Fake user agent
        options.addArguments("user-agent=Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 "
                + "(KHTML, like Gecko) Chrome/114.0.5735.91 Safari/537.36");

        // Prevent headless detection
        options.setExperimentalOption("excludeSwitches", new String[]{"enable-automation"});
        options.setExperimentalOption("useAutomationExtension", false);

        driver = new ChromeDriver(options);
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        driver.manage().window().maximize();
        driver.get("https://www.naukri.com/nlogin/login?URL=https://www.naukri.com/mnjuser/profile");

        // Remove webdriver flag
        ((JavascriptExecutor) driver).executeScript(
                "Object.defineProperty(navigator, 'webdriver', {get: () => undefined})");

        // Get credentials from environment variables
        String email = System.getenv("NAUKRI_EMAIL");
        String password = System.getenv("NAUKRI_PASSWORD");

        WebElement emailField = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//input[@type='text' and contains(@placeholder, 'Email ID')]")));
        emailField.sendKeys(email);

        WebElement passwordField = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//input[@type='password']")));
        passwordField.sendKeys(password);

        WebElement loginButton = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//button[@type='submit' and text()='Login']")));
        loginButton.click();
    }

    @Test
    public void ResumeHeadline() {
        JavascriptExecutor js = (JavascriptExecutor) driver;

        WebElement headlineSection = wait.until(ExpectedConditions.presenceOfElementLocated(
                By.xpath("//*[@id=\"lazyResumeHead\"]/div/div/div[1]/span[2]")));
        js.executeScript("arguments[0].scrollIntoView(true);", headlineSection);

        WebElement editIcon = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//span[contains(@class,'edit icon')]")));
        editIcon.click();

        WebElement textArea = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.id("resumeHeadlineTxt")));

        textArea.clear();
        textArea.sendKeys("QA Analyst | Manual & Automation Testing | HTML | Selenium | GitHub | CSS");

        WebElement saveBtn = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//button[text()='Save']")));
        saveBtn.click();
    }

    @AfterClass
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}

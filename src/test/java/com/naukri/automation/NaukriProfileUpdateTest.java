package com.naukri.automation;

import java.io.File;
import java.io.IOException;
import java.time.Duration;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.*;

public class NaukriProfileUpdateTest {

    private WebDriver driver;
    private WebDriverWait wait;

    @BeforeClass
    public void setup() throws InterruptedException {
        ChromeOptions options = new ChromeOptions();

        // Headless and anti-bot setup
        options.addArguments("--headless=new");
        options.addArguments("--disable-gpu");
        options.addArguments("--window-size=1920,1080");
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("user-agent=Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 "
                + "(KHTML, like Gecko) Chrome/114.0.5735.91 Safari/537.36");
        options.setExperimentalOption("excludeSwitches", new String[]{"enable-automation"});
        options.setExperimentalOption("useAutomationExtension", false);

        driver = new ChromeDriver(options);
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        driver.manage().window().maximize();
        driver.get("https://www.naukri.com/nlogin/login?URL=https://www.naukri.com/mnjuser/homepage");

        // Remove webdriver flag
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

        takeScreenshot("after-login.png");

        // Optional sleep for debugging race conditions
        Thread.sleep(2000);

        // Wait for hamburger menu to be present and visible
        WebElement hamburgerMenu = wait.until(ExpectedConditions.presenceOfElementLocated(
                By.cssSelector("div.nI-gNb-drawer__bars")));

        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", hamburgerMenu);

        try {
            wait.until(ExpectedConditions.elementToBeClickable(hamburgerMenu)).click();
        } catch (Exception e) {
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", hamburgerMenu);
        }

        takeScreenshot("after-hamburger.png");

        // Wait for drawer
        wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.cssSelector("div.naukri-drawer.right.open")));

        // Click on "View & Update Profile"
        WebElement updateProfileLink = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//a[contains(text(), 'View & Update Profile')]")));
        updateProfileLink.click();

        takeScreenshot("after-profile-click.png");
    }

    @Test
    public void updateResume() {
        WebElement updateResume = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//input[@value='Update resume']")));
        updateResume.click();

        String resumePath = System.getProperty("user.dir") + File.separator + "resources"
                + File.separator + "Deepak_Jena_QA_Resume_2025.pdf";

        WebElement fileInput = driver.findElement(By.xpath("//input[@type='file']"));
        fileInput.sendKeys(resumePath);

        takeScreenshot("after-resume-upload.png");
    }

    @AfterClass
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }

    public void takeScreenshot(String filename) {
        try {
            File scrFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
            FileUtils.copyFile(scrFile, new File(filename));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

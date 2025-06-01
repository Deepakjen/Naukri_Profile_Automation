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
import org.openqa.selenium.TimeoutException;
import org.testng.annotations.*;

public class NaukriProfileUpdateTest {

    private WebDriver driver;
    private WebDriverWait wait;

    public void takeScreenshot(String fileName) throws IOException {
        File screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
        FileUtils.copyFile(screenshot, new File(fileName));
    }

    @BeforeClass
    public void setup() throws IOException {
        ChromeOptions options = new ChromeOptions();

        if (System.getenv("HEADLESS") == null || System.getenv("HEADLESS").equals("true")) {
            options.addArguments("--headless=new");
        }

        options.addArguments("--disable-gpu");
        options.addArguments("--window-size=1920,1080");
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("user-agent=Mozilla/5.0 (Windows NT 10.0; Win64; x64)");

        options.setExperimentalOption("excludeSwitches", new String[]{"enable-automation"});
        options.setExperimentalOption("useAutomationExtension", false);

        driver = new ChromeDriver(options);
        wait = new WebDriverWait(driver, Duration.ofSeconds(30));
        driver.manage().window().maximize();
        driver.get("https://www.naukri.com/");

        wait.until(webDriver -> ((JavascriptExecutor) webDriver)
                .executeScript("return document.readyState").equals("complete"));

        ((JavascriptExecutor) driver).executeScript(
                "Object.defineProperty(navigator, 'webdriver', {get: () => undefined})");

        String email = System.getenv("NAUKRI_EMAIL");
        String password = System.getenv("NAUKRI_PASSWORD");

        try {
            WebElement loginLayer = wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath("//a[@id='login_Layer' and contains(@class, 'nI-gNb-lg-rg__login')]")));
            loginLayer.click();
        } catch (TimeoutException e) {
            takeScreenshot("login_button_not_found.png");
            throw e;
        }

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
    public void updateResume() throws IOException {
        WebElement updateResume = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//input[@value='Update resume']")));
        updateResume.click();

        String resumePath = System.getProperty("user.dir") + File.separator + "resources" + File.separator + "Deepak_Jena_QA_Resume_2025.pdf";

        WebElement fileInput = driver.findElement(By.xpath("//input[@type='file']"));
        fileInput.sendKeys(resumePath);

        takeScreenshot("resume_uploaded.png");
    }

    @AfterClass
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}

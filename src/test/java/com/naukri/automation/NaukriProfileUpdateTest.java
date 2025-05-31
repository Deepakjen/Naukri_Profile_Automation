package com.naukri.automation;

import java.awt.AWTException;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.datatransfer.StringSelection;
import java.awt.event.KeyEvent;
import java.time.Duration;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
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
        driver = new ChromeDriver();
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        driver.manage().window().maximize();
        driver.get("https://www.naukri.com/");

        // Click on login button
        driver.findElement(By.id("login_Layer")).click();

        // Wait for email field and enter email
        WebElement emailField = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//input[@type='text' and contains(@placeholder, 'Email ID')]")));
        emailField.sendKeys("deepakjena903@gmail.com");

        WebElement passwordField = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//input[@type='password']")));
        passwordField.sendKeys("Deepak@123");

        WebElement loginButton = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//button[contains(@class, 'loginButton')]")));
        loginButton.click();

        // Wait until redirected and "View profile" is visible
        WebElement viewProfile = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//a[@href='/mnjuser/profile']")));
        viewProfile.click();
    }

    @Test
    public void updateResume() throws AWTException {
        // Click on Update Resume button
        WebElement updateResume = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//input[@value='Update resume']")));
        updateResume.click();

        // Upload resume via hidden file input
        WebElement fileInput = driver.findElement(By.xpath("//input[@type='file']"));
        fileInput.sendKeys("C:\\Users\\djdip\\Downloads\\Deepak_Jena_QA_Resume_2025.pdf");

        // Use Robot to handle OS file dialog if needed
        StringSelection selection = new StringSelection("C:\\Users\\djdip\\Downloads\\Deepak_Jena_QA_Resume_2025.pdf");
        Toolkit.getDefaultToolkit().getSystemClipboard().setContents(selection, null);

        Robot robot = new Robot();
        robot.delay(2000);
        robot.keyPress(KeyEvent.VK_CONTROL);
        robot.keyPress(KeyEvent.VK_V);
        robot.keyRelease(KeyEvent.VK_V);
        robot.keyRelease(KeyEvent.VK_CONTROL);
        robot.keyPress(KeyEvent.VK_ENTER);
        robot.keyRelease(KeyEvent.VK_ENTER);
    }

    @AfterClass
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}



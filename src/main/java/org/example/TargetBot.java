package org.example;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import io.github.cdimascio.dotenv.*;

import java.time.Duration;

public class TargetBot extends Bot {

    Logger logger = LoggerFactory.getLogger(Bot.class.getName());
    private static final Dotenv dotenv = Dotenv.load();

    public TargetBot() {

    }

    boolean login() {
        driver.get("https://www.target.com/p/pok-233-mon-trading-card-game-scarlet-38-violet-8212-destined-rivals-elite-trainer-box/-/A-94300069#lnk=sametab");

        // Wait for the page to fully load
        try {
            new WebDriverWait(driver, Duration.ofSeconds(8))
                    .until(ExpectedConditions.titleContains("Target"));
            String title = driver.getTitle();
            String url = driver.getCurrentUrl();
            logger.info("Title: {} Url: {}", title, url);
        } catch (Exception e) {
            logger.error("Failed to retrieve meaningful data in time. ", e);
            return false;
        }

        // Wait up to 10 seconds for the buttons to be clickable
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        try {
            WebElement accountButton = wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("#account-sign-in")));
            accountButton.click();
        } catch (TimeoutException e) {
            logger.error("Timed out waiting to click account button. ", e);
            return false;
        }

        try {
            WebElement signInButton = wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("[data-test='accountNav-signIn']")));
            signInButton.click();
        } catch (TimeoutException e) {
            logger.error("Timed out waiting to click signin button. ", e);
            return false;
        }

        try {
            WebElement usernameField = wait.until(ExpectedConditions.elementToBeClickable(By.id("username")));
            String username = dotenv.get("USERNAME");
            usernameField.sendKeys(username);
        } catch (TimeoutException e) {
            logger.error("Timed out waiting for username field to be clickable. ", e);
            return false;
        }

        try {
            WebElement continueButton = wait.until(ExpectedConditions.elementToBeClickable(By.id("login")));
            continueButton.click();
            simulateHumanMouseMovement(driver);
            continueButton.click();
        } catch (TimeoutException e) {
            logger.error("Timed out waiting for login button to be clickable. ", e);
            return false;
        }

        try {
            WebElement passwordField = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("password")));
            String password = dotenv.get("PASSWORD");
            passwordField.sendKeys(password);
        } catch (TimeoutException e) {
            logger.error("Timed out waiting for password field to be clickable ", e);
            return false;
        }

        try {
            WebElement signIn = wait.until(ExpectedConditions.elementToBeClickable(By.id("login")));
            simulateHumanMouseMovement(driver);
            signIn.click();
        } catch (TimeoutException e) {
            logger.error("Timed out waiting for last login button to be clickable ", e);
            return false;
        }

        try {
            Thread.sleep(5000);
        } catch (Exception ignored) {

        }

        return true;
    }

    boolean checkStock() {
        driver.get("https://www.target.com/p/pok-233-mon-trading-card-game-scarlet-38-violet-8212-destined-rivals-elite-trainer-box/-/A-94300069#lnk=sametab");
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        try {
            WebElement addToCartButton = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("addToCartButtonOrTextIdFor94300069")));
            String attributeIsDisabled = addToCartButton.getDomAttribute("disabled");
            return Boolean.parseBoolean(attributeIsDisabled);
        } catch (Exception e) {
            logger.error("Timed out waiting for login button to be clickable. ", e);
            return false;
        }
    }

    boolean addToCart() {
        return false;
    }

    boolean checkout() {
        return false;
    }
}
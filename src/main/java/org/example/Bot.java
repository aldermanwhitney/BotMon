package org.example;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.devtools.DevTools;
import org.openqa.selenium.devtools.HasDevTools;
import org.openqa.selenium.devtools.v137.webauthn.WebAuthn;
import org.openqa.selenium.devtools.v137.webauthn.model.*;
import org.openqa.selenium.interactions.Actions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.Optional;
import java.util.Random;

public class Bot {

    Logger logger = LoggerFactory.getLogger(Bot.class.getName());
    WebDriver driver;

    public Bot() {
        initialize();
    }

    ChromeOptions generateChromeOptions() {
        ChromeOptions options = new ChromeOptions();

        // Avoid basic bot detection
        // Overwrite the default Selenium user agent header
        options.addArguments("--user-agent=Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/137.0.0.0 Safari/537.36");

        // Disable automation tags
        options.setExperimentalOption("excludeSwitches", Collections.singletonList("enable-automation"));
        options.setExperimentalOption("useAutomationExtension", false);

        // Enable autofill and form behavior
        options.addArguments("--enable-blink-features=IdleDetection,AutomationControlled");
        options.addArguments("--disable-blink-features=PasswordManagerEnabled");

        // Start maximized to avoid viewport checks
        options.addArguments("start-maximized");

        // Set language to US English to pass accept-language headers
        options.addArguments("--lang=en-US");

        // Open DevTools by default (for debugging only)
        options.addArguments("--auto-open-devtools-for-tabs");

        return options;
    }

    void initialize() {
        ChromeOptions options = generateChromeOptions();
        WebDriver driver = new ChromeDriver(options);
        DevTools devTools = ((HasDevTools) driver).getDevTools();
        devTools.createSession();

        // Get around Browser-level Passkeys popup
        // Enable WebAuthn
        devTools.send(WebAuthn.enable(Optional.of(false)));

        // Set up virtual authenticator
        VirtualAuthenticatorOptions virtualAuthOptions = new VirtualAuthenticatorOptions(AuthenticatorProtocol.CTAP2, Optional.of(Ctap2Version.CTAP2_1), AuthenticatorTransport.USB, Optional.of(false), Optional.of(false), Optional.of(false), Optional.of(false), Optional.of(false), Optional.of(false), Optional.of(true),  // automaticPresenceSimulation
                Optional.of(false), Optional.of(false), Optional.of(false));

        AuthenticatorId authId = devTools.send(WebAuthn.addVirtualAuthenticator(virtualAuthOptions));
        devTools.send(WebAuthn.setAutomaticPresenceSimulation(authId, true));

        this.driver = driver;
    }

    protected void simulateHumanMouseMovement(WebDriver driver) {
        Actions actions = new Actions(driver);

        int startX = 200;
        int startY = 200;
        int endX = 600;
        int endY = 400;
        int steps = 10; // Adjust for smoothness

        for (int i = 0; i <= steps; i++) {
            double t = (double) i / steps;
            int x = (int) (startX + t * (endX - startX));
            int y = (int) (startY + t * (endY - startY));
            actions.moveByOffset(x - startX, y - startY).perform();
            startX = x;
            startY = y;

            try {
                Thread.sleep(100 + new Random().nextInt(300)); // 300–600ms delay
            } catch (Exception e) {
                logger.error("Failed to pause between simulated mouse movements, bot detection may occur.");
            }
        }

    }

    boolean login() {
        return false;
    }

    void shutdown() {
        if (driver != null) {
            driver.quit();
        }
    }

    boolean checkStock() {
        return false;
    }

    boolean addToCart() {
        return false;
    }

    boolean checkout() {
        return false;
    }
}
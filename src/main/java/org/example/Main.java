package org.example;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Main {
    private static final Logger logger = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) {

        Bot b = new TargetBot();

        boolean success = b.login();
        if (!success) {
            logger.error("Failed to login.");
            b.shutdown();
            return;
        }

        boolean inStock = false;
        int i = 0;
        while (!inStock && i < 5) {
            inStock = b.checkStock();
            i++;
        }

        if (true) {
            logger.error("Rest of program not implemented. Ending.");
            b.shutdown();
            return;
        }

        boolean addedToCart = b.addToCart();
        while (inStock) {
            boolean checkoutSuccess = b.checkout();
            inStock = b.checkStock();
        }
    }
}
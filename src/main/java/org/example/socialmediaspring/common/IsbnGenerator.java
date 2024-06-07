package org.example.socialmediaspring.common;

import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import java.util.Random;

@Component
public class IsbnGenerator {

    public String generateISBN() {
        Random random = new Random();

        String prefix = "978";

        // Registration Group Element (for simplicity, using '1' for English-speaking area)
        String registrationGroup = "1";

        // Registrant Element - a random 5-digit number
        String registrant = String.format("%05d", random.nextInt(100000));

        // Publication Element - a random 3-digit number
        String publication = String.format("%03d", random.nextInt(1000));

        // Combine elements without check digit
        String isbnWithoutCheckDigit = prefix + registrationGroup + registrant + publication;

        // Calculate the check digit
        int checkDigit = calculateCheckDigit(isbnWithoutCheckDigit);

        // Combine all parts to form the final ISBN
        return isbnWithoutCheckDigit + checkDigit;
    }

    private static int calculateCheckDigit(String isbnWithoutCheckDigit) {
        int sum = 0;
        for (int i = 0; i < isbnWithoutCheckDigit.length(); i++) {
            int digit = Character.getNumericValue(isbnWithoutCheckDigit.charAt(i));
            sum += (i % 2 == 0) ? digit : digit * 3;
        }
        int remainder = sum % 10;
        return (10 - remainder) % 10;
    }
}

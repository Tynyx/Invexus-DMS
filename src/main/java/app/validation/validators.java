//  Invexus DMS - Validators (small, focused)
//  Author: LaTroy Richardson (CEN-3024C)
//
//  Purpose:
//  - Keep all user-input checking in one spot so my CLI and importer stay clean.
//  - Throw clear IllegalArgumentException messages so I can show fail tests on video.
//
//  Style:
//  - Methods are tiny and do one thing each.
//  - I favor “fail fast” with specific messages over silent defaults.
// /


package app.validation;

import app.domain.AssetStatus;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.DateTimeException;
import java.time.LocalDate;

public final class validators {
    private validators() {}

    // This method is to validate the unitCost attribute that would be inputted by the user and kicks back anything that
    // crash the CLI
    public static BigDecimal parseMoney(String s) {
        if (s == null || s.isBlank()) throw new IllegalArgumentException("Amount is required");
        try {
            BigDecimal v = new BigDecimal(s.trim());
            if (v.signum() < 0) throw new IllegalArgumentException("Amount must be positive");
            return v.setScale(2, RoundingMode.HALF_UP);
        } catch (NumberFormatException ex) {
            throw new IllegalArgumentException("Invalid currency amount: " + s);
        }
    }

    //This method is to read and validate all inputs for both attributes purchaseDate and WarrantyEndDate
    public static LocalDate parseDateFlexible(String s) {
        if (s == null || s.isBlank()) throw new IllegalArgumentException("Date is required");
        String[] parts = s.trim().split("[^0-9]+");
        if (parts.length != 3) throw new IllegalArgumentException("Date is required. ");
        try {
            int y = Integer.parseInt(parts[0]);
            int m = Integer.parseInt(parts[1]);
            int d = Integer.parseInt(parts[2]);
            return LocalDate.of(y, m, d);
        } catch (NumberFormatException | DateTimeException e) {
            throw new IllegalArgumentException("Invalid date values: " + s);
        }
    }

    //This method is make sure nothing crashes the AssetStatus attibrute and tell the user how to properly use it
    public static AssetStatus parseStatus(String s) {
        if (s == null || s.isBlank()) throw new IllegalArgumentException("Status is required");
        try {
            return AssetStatus.valueOf(s.toUpperCase());
        } catch (IllegalArgumentException ex) {
            throw new IllegalArgumentException("Status must be one of: IN_STOCK, ASSIGNED, REPAIR, RETIRED.");
        }
    }
    //This method is to help user use more sensible methods of using the boolean true or false to yes or no and shorthand
    public static boolean parseBooleanLoose(String s) {
        if (s == null || s.isBlank() ) return false;
        String t = s.trim().toLowerCase();
        if (t.equals("y") || t.equals("yes") || t.equals("true")) return true;
        if (t.equals("n") || t.equals("no") || t.equals("false")) return false;
        return Boolean.parseBoolean(t);
    }
}

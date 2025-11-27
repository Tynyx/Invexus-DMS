//  Invexus DMS - Validators (small, focused)
//  Author: LaTroy Richardson (CEN-3024C)
//
//  Purpose:
//  - Keep all user-input checking in one spot so my CLI and importer stay clean.
//  - Throw clear IllegalArgumentException messages, so I can show fail tests on video.
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

/**
 * Utility class containing parsing and validation helpers for user input.
 * <p>
 * All methods are static and throw {@link IllegalArgumentException} when the input
 * is missing or invalid, so callers can surface clear messages to the user.
 */
public final class validators {
    private validators() {}

    /**
     * Parses a currency amount from a string.
     * <p>
     * The value is trimmed, converted to a {@link BigDecimal}, ensured to be non-negative,
     * and scaled to two decimal places using {@link java.math.RoundingMode#HALF_UP}.
     *
     * @param s the raw amount string entered by the user
     * @return the parsed monetary value
     * @throws IllegalArgumentException if the input is {@code null}, blank, non-numeric, or negative
     */
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

    /**
     * Parses a date from a flexible string format.
     * <p>
     * The method extracts numeric year, month, and day components from the string,
     * allowing various separators (e.g., {@code 2024-10-31}, {@code 2024/10/31}, {@code 2024 10 31}).
     *
     * @param s the raw date string
     * @return the parsed {@link LocalDate}
     * @throws IllegalArgumentException if the input is {@code null}, blank, missing parts,
     *                                  or cannot be converted into a valid date
     */
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

    /**
     * Parses a status string into an {@link AssetStatus}, in a case-insensitive way.
     * <p>
     * The string is trimmed and uppercased before lookup.
     *
     * @param s the raw status string
     * @return the parsed {@link AssetStatus}
     * @throws IllegalArgumentException if the input is {@code null}, blank, or does not match a valid status
     */
    public static AssetStatus parseStatus(String s) {
        if (s == null || s.isBlank()) {
            throw new IllegalArgumentException("Status is required");
        }

        String normalized = s.trim()
                .toUpperCase()
                .replace(' ', '_'); // key line

        try {
            return AssetStatus.valueOf(normalized);
        } catch (IllegalArgumentException ex) {
            throw new IllegalArgumentException(
                    "Status must be one of: IN_STOCK, ASSIGNED, REPAIR, RETIRED."
            );
        }
    }

    /**
     * Parses a boolean value from a "loose" user-friendly string.
     * <p>
     * Accepts common variants such as {@code "y"}, {@code "yes"}, {@code "true"} for {@code true}
     * and {@code "n"}, {@code "no"}, {@code "false"} for {@code false}. A {@code null} or blank
     * string is treated as {@code false}.
     *
     * @param s the raw boolean-like string
     * @return {@code true} or {@code false} based on the interpreted value
     */
    public static boolean parseBooleanLoose(String s) {
        if (s == null || s.isBlank() ) return false;
        String t = s.trim().toLowerCase();
        if (t.equals("y") || t.equals("yes") || t.equals("true")) return true;
        if (t.equals("n") || t.equals("no") || t.equals("false")) return false;
        return Boolean.parseBoolean(t);
    }

    /**
     * Parses an integer quantity from a string, enforcing a valid range.
     * <p>
     * The value is trimmed, parsed as an integer, and validated to be between 1 and 999 inclusive.
     *
     * @param input the raw quantity string
     * @return the parsed quantity
     * @throws IllegalArgumentException if the input is not a number, or is outside the range 1–999
     */
    public static int parseInt(String input) {
        try {
            int value = Integer.parseInt(input.trim());
            if (value < 1 || value > 999) {
                throw new IllegalArgumentException("Quantity must be between 1 and 999.");
            }
            return value;
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid number format: " + input);
        }
    }

}

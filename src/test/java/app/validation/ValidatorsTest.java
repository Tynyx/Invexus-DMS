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



import app.domain.AssetStatus;
import app.validation.validators;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class ValidatorTest {

    @Test
    void parseMoney_valid() {
        assertEquals(0, new BigDecimal("79.99").compareTo(validators.parseMoney("79.99")));
    }

    @Test
    void parseMoney_invalid() {
        assertThrows(IllegalArgumentException.class, () -> validators.parseMoney("abc"));
    }

    @Test
    void parseDate_valid() {
        assertEquals(LocalDate.of(2025, 2, 10), validators.parseDateFlexible("2025-02-10"));
    }

    @Test
    void parseDate_invalid() {
        assertThrows(IllegalArgumentException.class, () -> validators.parseDateFlexible("2025/13/01"));
    }

    @Test
    void parseStatus_valid() {
        assertEquals(AssetStatus.REPAIR, validators.parseStatus("repair"));
    }

    @Test
    void parseStatus_invalid() {
        assertThrows(IllegalArgumentException.class, () -> validators.parseStatus("BROKEN"));
    }

    @Test
    void parseBooleanLoose_cases() {
        assertTrue(validators.parseBooleanLoose("y"));
        assertTrue(validators.parseBooleanLoose("Yes"));
        assertFalse(validators.parseBooleanLoose("n"));
        assertFalse(validators.parseBooleanLoose("No"));
        assertTrue(validators.parseBooleanLoose("true"));
        assertFalse(validators.parseBooleanLoose("false"));
        assertFalse(validators.parseBooleanLoose(null));
    }
}
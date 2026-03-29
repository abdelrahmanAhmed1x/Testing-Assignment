package Tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import org.junit.Before;
import org.junit.Test;

import Interfaces.IScrubDigits;
import Services.DigitScrubber;

/**
 * Test class for IScrubDigits / DigitScrubber.
 *
 * Covers:
 *  - Happy-path (positive) scenarios
 *  - Edge-case / error (negative) scenarios
 *
 * No mocking is needed here because DigitScrubber has no collaborators;
 * it is the SUT itself.
 */
public class DigitScrubberTest {

    private IScrubDigits sut;

    @Before
    public void setUp() {
        sut = new DigitScrubber();
    }

    // ---------------------------------------------------------------
    // Happy-path tests
    // ---------------------------------------------------------------

    @Test
    public void scrub_singleDigit_returnsX() {
        String result = sut.scrub("5");
        assertEquals("X", result);
    }

    @Test
    public void scrub_multipleDigits_allReplacedWithX() {
        String result = sut.scrub("1234567890");
        assertEquals("XXXXXXXXXX", result);
    }

    @Test
    public void scrub_mixedAlphanumeric_onlyDigitsReplaced() {
        String result = sut.scrub("Call me at 0501234567 please");
        assertEquals("Call me at XXXXXXXXXX please", result);
    }

    @Test
    public void scrub_noDigitsPresent_returnsInputUnchanged() {
        String input = "Hello, world!";
        String result = sut.scrub(input);
        assertEquals(input, result);
    }

    @Test
    public void scrub_phoneNumberWithDashes_masksOnlyDigits() {
        String result = sut.scrub("050-123-4567");
        assertEquals("XXX-XXX-XXXX", result);
    }

    @Test
    public void scrub_stringWithSpecialCharactersAndDigits_onlyDigitsMasked() {
        String result = sut.scrub("ID: #99 | Score: 42%");
        assertEquals("ID: #XX | Score: XX%", result);
    }

    @Test
    public void scrub_singleNonDigitCharacter_returnsUnchanged() {
        String result = sut.scrub("A");
        assertEquals("A", result);
    }

    @Test
    public void scrub_multiLineStringWithDigits_allDigitsMasked() {
        String result = sut.scrub("Line1: 12\nLine2: 34");
        assertEquals("LineX: XX\nLineX: XX", result);
    }

    // ---------------------------------------------------------------
    // Negative / edge-case tests
    // ---------------------------------------------------------------

    @Test(expected = NullPointerException.class)
    public void scrub_nullInput_throwsNullPointerException() {
        sut.scrub(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void scrub_emptyString_throwsIllegalArgumentException() {
        sut.scrub("");
    }

    @Test(expected = IllegalArgumentException.class)
    public void scrub_whitespaceOnlyString_throwsIllegalArgumentException() {
        sut.scrub("   ");
    }

    @Test(expected = IllegalArgumentException.class)
    public void scrub_tabOnlyString_throwsIllegalArgumentException() {
        sut.scrub("\t");
    }

    @Test(expected = IllegalArgumentException.class)
    public void scrub_newlineOnlyString_throwsIllegalArgumentException() {
        sut.scrub("\n");
    }

    @Test
    public void scrub_nullThrowsExceptionWithMessage() {
        try {
            sut.scrub(null);
            fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            assertNotNull("Exception message should not be null", e.getMessage());
        }
    }

    @Test
    public void scrub_blankThrowsExceptionWithMessage() {
        try {
            sut.scrub("  ");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertNotNull("Exception message should not be null", e.getMessage());
        }
    }
}
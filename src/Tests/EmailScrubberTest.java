package Tests;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

import Interfaces.IScrubEmails;
import Services.EmailScrubber;


public class EmailScrubberTest {

    private IScrubEmails sut;

    private static final String HIDDEN = "[EMAIL_HIDDEN]";

    @Before
    public void setUp() {
        sut = new EmailScrubber();
    }

    // Happy-path tests
    

    @Test
    public void scrub_simpleEmail_replacedWithToken() {
        String result = sut.scrub("Contact john.doe@example.com for info");
        assertEquals("Contact " + HIDDEN + " for info", result);
    }

    @Test
    public void scrub_emailAtStartOfString_replacedWithToken() {
        String result = sut.scrub("admin@company.org is the address");
        assertEquals(HIDDEN + " is the address", result);
    }

    @Test
    public void scrub_emailAtEndOfString_replacedWithToken() {
        String result = sut.scrub("Send mail to support@help.io");
        assertEquals("Send mail to " + HIDDEN, result);
    }

    @Test
    public void scrub_emailIsEntireString_replacedWithToken() {
        String result = sut.scrub("user@domain.com");
        assertEquals(HIDDEN, result);
    }

    @Test
    public void scrub_multipleEmailsInSentence_allReplaced() {
        String result = sut.scrub("From: alice@foo.com, To: bob@bar.net");
        assertEquals("From: " + HIDDEN + ", To: " + HIDDEN, result);
    }

    @Test
    public void scrub_noEmailPresent_returnsInputUnchanged() {
        String input = "No emails here, just plain text.";
        String result = sut.scrub(input);
        assertEquals(input, result);
    }

    @Test
    public void scrub_textContainsAtSignButNotValidEmail_returnsInputUnchanged() {
        // "@mention" on social media is not an e-mail
        String input = "Follow @username on Twitter";
        String result = sut.scrub(input);
        assertEquals(input, result);
    }

    @Test
    public void scrub_returnValueNeverNull_whenInputIsValid() {
        String result = sut.scrub("No emails here.");
        assertNotNull(result);
    }

    @Test
    public void scrub_partialEmailMissingDomain_notReplaced() {
        // "user@" has no domain — should not be treated as a valid e-mail
        String input = "Broken address: user@";
        String result = sut.scrub(input);
        assertEquals(input, result);
    }

    @Test
    public void scrub_partialEmailMissingTLD_notReplaced() {
        // "user@domain" has no TLD — should not be treated as a valid e-mail
        String input = "Broken address: user@domain";
        String result = sut.scrub(input);
        assertEquals(input, result);
    }

    // Negative / edge-case tests

    @Test(expected = NullPointerException.class)
    public void scrub_nullInput_throwsNullPointerException() {
        sut.scrub(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void scrub_emptyString_throwsIllegalArgumentsException() {
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

    @Test
    public void scrub_nullThrowsExceptionWithMessage() {
        try {
            sut.scrub(null);
            fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            assertNotNull("Exception message should not be null", e.getMessage());
        }
    }

    
}
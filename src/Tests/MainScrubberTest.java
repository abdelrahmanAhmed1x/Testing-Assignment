package Tests;

import static org.junit.Assert.*;
import org.junit.Rule;
import org.junit.Test;
import org.jmock.Expectations;
import org.jmock.Sequence;
import org.jmock.integration.junit4.JUnitRuleMockery;

import Interfaces.IScrub;
import Interfaces.IScrubDigits;
import Interfaces.IScrubEmails;
import Models.ScrubMode;
import Services.MainScrubber;


public class MainScrubberTest {

    @Rule
    public final JUnitRuleMockery context = new JUnitRuleMockery();

    private final IScrubDigits mockDigitScrubber = context.mock(IScrubDigits.class);
    private final IScrubEmails mockEmailScrubber = context.mock(IScrubEmails.class);

    private final IScrub sut = new MainScrubber(mockDigitScrubber, mockEmailScrubber);

    private static final String RAW_INPUT       = "Call 0501234567 or email bob@corp.io";
    private static final String DIGITS_SCRUBBED = "Call XXXXXXXXXX or email bob@corp.io";
    private static final String EMAILS_SCRUBBED = "Call 0501234567 or email [EMAIL_HIDDEN]";
    private static final String FULLY_SCRUBBED  = "Call XXXXXXXXXX or email [EMAIL_HIDDEN]";

   
    @Test
    public void scrub_onlyDigitsMode_invokesDigitScrubberExactlyOnce() {
        context.checking(new Expectations() {{
            oneOf(mockDigitScrubber).scrub(RAW_INPUT); will(returnValue(DIGITS_SCRUBBED));
            never(mockEmailScrubber).scrub(with(any(String.class)));
        }});

        sut.scrub(RAW_INPUT, ScrubMode.ONLY_DIGITS);
    }

    @Test
    public void scrub_onlyDigitsMode_returnsDigitScrubberResult() {
        context.checking(new Expectations() {{
            oneOf(mockDigitScrubber).scrub(RAW_INPUT); will(returnValue(DIGITS_SCRUBBED));
            never(mockEmailScrubber).scrub(with(any(String.class)));
        }});

        String result = sut.scrub(RAW_INPUT, ScrubMode.ONLY_DIGITS);

        assertEquals(DIGITS_SCRUBBED, result);
    }

    @Test
    public void scrub_onlyDigitsMode_passesExactInputToCollaborator() {
        context.checking(new Expectations() {{
            oneOf(mockDigitScrubber).scrub(RAW_INPUT); will(returnValue(DIGITS_SCRUBBED));
            never(mockEmailScrubber).scrub(with(any(String.class)));
        }});

        sut.scrub(RAW_INPUT, ScrubMode.ONLY_DIGITS);
    }

    @Test
    public void scrub_onlyDigitsMode_neverInvokesEmailScrubber() {
        context.checking(new Expectations() {{
            oneOf(mockDigitScrubber).scrub(RAW_INPUT); will(returnValue(DIGITS_SCRUBBED));
            never(mockEmailScrubber).scrub(with(any(String.class)));
        }});

        sut.scrub(RAW_INPUT, ScrubMode.ONLY_DIGITS);
        // never() expectation above fails the test if emailScrubber is called
    }

    

    @Test
    public void scrub_onlyEmailsMode_invokesEmailScrubberExactlyOnce() {
        context.checking(new Expectations() {{
            oneOf(mockEmailScrubber).scrub(RAW_INPUT); will(returnValue(EMAILS_SCRUBBED));
            never(mockDigitScrubber).scrub(with(any(String.class)));
        }});

        sut.scrub(RAW_INPUT, ScrubMode.ONLY_EMAILS);
    }

    @Test
    public void scrub_onlyEmailsMode_returnsEmailScrubberResult() {
        context.checking(new Expectations() {{
            oneOf(mockEmailScrubber).scrub(RAW_INPUT); will(returnValue(EMAILS_SCRUBBED));
            never(mockDigitScrubber).scrub(with(any(String.class)));
        }});

        String result = sut.scrub(RAW_INPUT, ScrubMode.ONLY_EMAILS);

        assertEquals(EMAILS_SCRUBBED, result);
    }

    @Test
    public void scrub_onlyEmailsMode_passesExactInputToCollaborator() {
        context.checking(new Expectations() {{
            oneOf(mockEmailScrubber).scrub(RAW_INPUT); will(returnValue(EMAILS_SCRUBBED));
            never(mockDigitScrubber).scrub(with(any(String.class)));
        }});

        sut.scrub(RAW_INPUT, ScrubMode.ONLY_EMAILS);
    }

    @Test
    public void scrub_onlyEmailsMode_neverInvokesDigitScrubber() {
        context.checking(new Expectations() {{
            oneOf(mockEmailScrubber).scrub(RAW_INPUT); will(returnValue(EMAILS_SCRUBBED));
            never(mockDigitScrubber).scrub(with(any(String.class)));
        }});

        sut.scrub(RAW_INPUT, ScrubMode.ONLY_EMAILS);
    }

    // FULL_SCRUBBING mode
    // =========================================================================

    @Test
    public void scrub_fullScrubbingMode_invokesDigitScrubberExactlyOnce() {
        context.checking(new Expectations() {{
            oneOf(mockDigitScrubber).scrub(RAW_INPUT);    will(returnValue(DIGITS_SCRUBBED));
            oneOf(mockEmailScrubber).scrub(DIGITS_SCRUBBED); will(returnValue(FULLY_SCRUBBED));
        }});

        sut.scrub(RAW_INPUT, ScrubMode.FULL_SCRUBBING);
    }

    @Test
    public void scrub_fullScrubbingMode_invokesEmailScrubberExactlyOnce() {
        context.checking(new Expectations() {{
            oneOf(mockDigitScrubber).scrub(RAW_INPUT);       will(returnValue(DIGITS_SCRUBBED));
            oneOf(mockEmailScrubber).scrub(DIGITS_SCRUBBED); will(returnValue(FULLY_SCRUBBED));
        }});

        sut.scrub(RAW_INPUT, ScrubMode.FULL_SCRUBBING);
    }

    @Test
    public void scrub_fullScrubbingMode_emailScrubberReceivesOutputOfDigitScrubber() {
        context.checking(new Expectations() {{
            oneOf(mockDigitScrubber).scrub(RAW_INPUT);       will(returnValue(DIGITS_SCRUBBED));
            oneOf(mockEmailScrubber).scrub(DIGITS_SCRUBBED); will(returnValue(FULLY_SCRUBBED));
        }});

        sut.scrub(RAW_INPUT, ScrubMode.FULL_SCRUBBING);
    }

    @Test
    public void scrub_fullScrubbingMode_returnsFinalEmailScrubberResult() {
        context.checking(new Expectations() {{
            oneOf(mockDigitScrubber).scrub(RAW_INPUT);       will(returnValue(DIGITS_SCRUBBED));
            oneOf(mockEmailScrubber).scrub(DIGITS_SCRUBBED); will(returnValue(FULLY_SCRUBBED));
        }});

        String result = sut.scrub(RAW_INPUT, ScrubMode.FULL_SCRUBBING);

        assertEquals(FULLY_SCRUBBED, result);
    }

    @Test
    public void scrub_fullScrubbingMode_bothCollaboratorsCalledInOrder() {
        final Sequence pipeline = context.sequence("pipeline");

        context.checking(new Expectations() {{
            // inSequence enforces digit scrubber is called BEFORE email scrubber
            oneOf(mockDigitScrubber).scrub(RAW_INPUT);       will(returnValue(DIGITS_SCRUBBED));
            inSequence(pipeline);
            oneOf(mockEmailScrubber).scrub(DIGITS_SCRUBBED); will(returnValue(FULLY_SCRUBBED));
            inSequence(pipeline);
        }});

        sut.scrub(RAW_INPUT, ScrubMode.FULL_SCRUBBING);
    }

     // negative tests 
     
    @Test
    public void scrub_onlyDigitsMode_collaboratorThrowsNullPointerException_returnsNull() {
        context.checking(new Expectations() {{
            oneOf(mockDigitScrubber).scrub(null); will(throwException(new NullPointerException("null input")));
            never(mockEmailScrubber).scrub(with(any(String.class)));
        }});

        String result = sut.scrub(null, ScrubMode.ONLY_DIGITS);

        assertNull(result);
    }

    @Test
    public void scrub_onlyDigitsMode_collaboratorThrowsIllegalArgumentException_returnsNull() {
        context.checking(new Expectations() {{
            oneOf(mockDigitScrubber).scrub("  "); will(throwException(new IllegalArgumentException("blank input")));
            never(mockEmailScrubber).scrub(with(any(String.class)));
        }});

        String result = sut.scrub("  ", ScrubMode.ONLY_DIGITS);

        assertNull(result);
    }

    @Test
    public void scrub_onlyEmailsMode_collaboratorThrowsNullPointerException_returnsNull() {
        context.checking(new Expectations() {{
            oneOf(mockEmailScrubber).scrub(null); will(throwException(new NullPointerException("null input")));
            never(mockDigitScrubber).scrub(with(any(String.class)));
        }});

        String result = sut.scrub(null, ScrubMode.ONLY_EMAILS);

        assertNull(result);
    }

    @Test
    public void scrub_onlyEmailsMode_collaboratorThrowsIllegalArgumentException_returnsNull() {
        context.checking(new Expectations() {{
            oneOf(mockEmailScrubber).scrub(""); will(throwException(new IllegalArgumentException("blank input")));
            never(mockDigitScrubber).scrub(with(any(String.class)));
        }});

        String result = sut.scrub("", ScrubMode.ONLY_EMAILS);

        assertNull(result);
    }

    @Test
    public void scrub_fullScrubbingMode_digitScrubberThrows_emailScrubberNeverCalled() {
        context.checking(new Expectations() {{
            oneOf(mockDigitScrubber).scrub(RAW_INPUT); will(throwException(new NullPointerException()));
            // Pipeline short-circuits — email scrubber must never be reached
            never(mockEmailScrubber).scrub(with(any(String.class)));
        }});

        sut.scrub(RAW_INPUT, ScrubMode.FULL_SCRUBBING);
    }

    @Test
    public void scrub_noInteractionsWithUnusedCollaborator_onlyDigitsMode() {
        context.checking(new Expectations() {{
            oneOf(mockDigitScrubber).scrub(RAW_INPUT); will(returnValue(DIGITS_SCRUBBED));
            never(mockEmailScrubber).scrub(with(any(String.class)));
        }});

        sut.scrub(RAW_INPUT, ScrubMode.ONLY_DIGITS);
    }

    @Test
    public void scrub_noInteractionsWithUnusedCollaborator_onlyEmailsMode() {
        context.checking(new Expectations() {{
            oneOf(mockEmailScrubber).scrub(RAW_INPUT); will(returnValue(EMAILS_SCRUBBED));
            never(mockDigitScrubber).scrub(with(any(String.class)));
        }});

        sut.scrub(RAW_INPUT, ScrubMode.ONLY_EMAILS);
    }
}
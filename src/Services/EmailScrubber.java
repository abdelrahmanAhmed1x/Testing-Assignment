package Services;

import Interfaces.IScrubEmails;

public class EmailScrubber implements IScrubEmails {
    @Override
    public String scrub(String input) {
        if (input == null) {
            throw new NullPointerException("Input cannot be null");
        }
        if (input.isBlank()){
            throw new IllegalArgumentException("input cannot be blank") ; 
        }
        return input.replaceAll("[a-zA-Z0-0._%+-]+@[a-zA-Z0-0.-]+\\.[a-zA-Z]{2,6}", "[EMAIL_HIDDEN]");
    }
}

package seedu.address.model.person;

import static java.util.Objects.requireNonNull;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.validator.routines.UrlValidator;

import seedu.address.commons.exceptions.IllegalValueException;

/**
 * Represents a Person's website information in the address book.
 * Guarantees: immutable; is valid as declared in {@link #isValidWebsite(String)}
 */

public class Website {
    public static final String MESSAGE_WEBSITE_CONSTRAINTS =
            "Person websites should be 3 alphanumeric strings separated by '.'";
    public static final String NO_WEBSITE = "https://websiteless.com";

    public final String value;

    /**
     * Validates given website.
     *
     * @throws IllegalValueException if given email address string is invalid.
     */
    public Website(String websiteName) throws IllegalValueException {
        requireNonNull(websiteName);
        String trimmedWebsite = websiteName.trim();

        if (!isValidWebsite(trimmedWebsite)) {
            throw new IllegalValueException(MESSAGE_WEBSITE_CONSTRAINTS);
        }

        this.value = trimmedWebsite;
    }

    /**
     * Returns if a given string is a valid person website.
     */
    public static boolean isValidWebsite(String test) {
        /*
        Pattern p = Pattern.compile(
                "(@)?(href=')?(HREF=')?(HREF=\")?(href=\")?(https://)?[a-zA-Z_0-9\\-]"
                        + "+(\\.\\w[a-zA-Z_0-9\\-]+)+(/[#&\\n\\-=?\\+\\%/\\.\\w]+)?");

        Matcher m = p.matcher(test);
        */
        String[] schemes = {"http","https"};
                UrlValidator urlValidator = new UrlValidator(schemes);

        return urlValidator.isValid(test);
    }

    @Override
    public String toString() {
        return value;
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof Website // instanceof handles nulls
                && this.value.equals(((Website) other).value)); // state check
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }

}

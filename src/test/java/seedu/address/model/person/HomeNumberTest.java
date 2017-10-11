package seedu.address.model.person;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class HomeNumberTest {

    @Test
    public void isValidPhone() {
        // invalid phone numbers
        assertFalse(HomeNumber.isValidHomeNumber("")); // empty string
        assertFalse(HomeNumber.isValidHomeNumber(" ")); // spaces only
        assertFalse(HomeNumber.isValidHomeNumber("91")); // less than 3 numbers
        assertFalse(HomeNumber.isValidHomeNumber("phone")); // non-numeric
        assertFalse(HomeNumber.isValidHomeNumber("9011p041")); // alphabets within digits
        assertFalse(HomeNumber.isValidHomeNumber("9312 1534")); // spaces within digits

        // valid phone numbers
        assertTrue(HomeNumber.isValidHomeNumber("911")); // exactly 3 numbers
        assertTrue(HomeNumber.isValidHomeNumber("93121534"));
        assertTrue(HomeNumber.isValidHomeNumber("124293842033123")); // long phone numbers
    }
}

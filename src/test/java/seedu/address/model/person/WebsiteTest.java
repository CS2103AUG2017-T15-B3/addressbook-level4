package seedu.address.model.person;

import static org.junit.Assert.*;

import org.junit.Test;

public class WebsiteTest {

    @Test
    public void isValidWebsite() {
        //blank website
        assertFalse(Website.isValidWebsite(""));
        assertFalse(Website.isValidWebsite(" "));

        //broken links with missing parts
        assertFalse(Website.isValidWebsite("wwwhelpcom"));
        assertFalse(Website.isValidWebsite("https://wwwworld"));
        assertFalse(Website.isValidWebsite("http//www.facebook.com"));
        assertFalse(Website.isValidWebsite(".com"));
        assertFalse(Website.isValidWebsite("https://.com"));
        assertFalse(Website.isValidWebsite("https//hadougen.com"));
        assertFalse(Website.isValidWebsite("https://hadougen."));
        assertFalse(Website.isValidWebsite("https://lamba.n"));

        //valid links
        assertTrue(Website.isValidWebsite("www.google.com"));
        assertTrue(Website.isValidWebsite("google.com"));
        assertTrue(Website.isValidWebsite("https://www.facebook.com/search"));
        assertTrue(Website.isValidWebsite("https://www.facebook.com"));
        assertTrue(Website.isValidWebsite("https://www.google.net"));
        assertTrue(Website.isValidWebsite("https://www.linkedin.com"));
        assertTrue(Website.isValidWebsite("https://www.facebook.net"));
    }
}

package seedu.address.commons.events.ui;

import seedu.address.commons.events.BaseEvent;

/**
 * An event requesting to view the website of a person.
 */
public class AccessWebsiteRequestEvent extends BaseEvent {

    public final String website;

    public AccessWebsiteRequestEvent(String website) {
        this.website = website;
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName();
    }
}

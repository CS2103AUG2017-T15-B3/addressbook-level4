package seedu.address.commons.events.ui;

//@@author itsdickson

import seedu.address.commons.events.BaseEvent;

/**
 * An event requesting to view the themes page.
 */
public class ShowThemeRequestEvent extends BaseEvent {

    @Override
    public String toString() {
        return this.getClass().getSimpleName();
    }
}
//@@author

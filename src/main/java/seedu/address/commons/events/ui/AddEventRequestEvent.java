package seedu.address.commons.events.ui;

import seedu.address.commons.events.BaseEvent;
import seedu.address.model.event.ReadOnlyEvent;

/**
 * Indicates a request to add Event.
 */
public class AddEventRequestEvent extends BaseEvent {

    public final ReadOnlyEvent event;

    public AddEventRequestEvent(ReadOnlyEvent event) {
        this.event = event;
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName();
    }
}

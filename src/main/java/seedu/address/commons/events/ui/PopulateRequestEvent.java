package seedu.address.commons.events.ui;

import seedu.address.commons.events.BaseEvent;
import seedu.address.model.event.ReadOnlyEvent;
import seedu.address.model.event.UniqueEventList;

/**
 * Indicates a request to add Event.
 */
public class PopulateRequestEvent extends BaseEvent {

    public final UniqueEventList eventList;

    public PopulateRequestEvent(UniqueEventList eventList) {
        this.eventList = eventList;
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName();
    }
}

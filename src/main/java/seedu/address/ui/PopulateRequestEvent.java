package seedu.address.ui;

import seedu.address.commons.events.BaseEvent;
import seedu.address.model.event.ReadOnlyEvent;
import seedu.address.model.event.UniqueEventList;

/**
 * Indicates a request to add Event.
 */
public class PopulateRequestEvent extends BaseEvent {

    public final ReadOnlyEvent event;
    public final UniqueEventList eventList;

    public PopulateRequestEvent(ReadOnlyEvent event, UniqueEventList eventList) {
        this.event = event;
        this.eventList = eventList;
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName();
    }
}

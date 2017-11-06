package seedu.address.commons.events.ui;

import seedu.address.commons.events.BaseEvent;
import seedu.address.ui.EventsCard;

//@@author archthegit

/**
 * Represents a selection change in the Events List Panel
 */
public class EventsPanelSelectionChangedEvent extends BaseEvent {


    private final EventsCard newSelection;

    public EventsPanelSelectionChangedEvent(EventsCard newSelection) {
        this.newSelection = newSelection;
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName();
    }

    public EventsCard getNewSelection() {
        return newSelection;
    }
}



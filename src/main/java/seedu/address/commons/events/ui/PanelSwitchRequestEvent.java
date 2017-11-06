package seedu.address.commons.events.ui;

import seedu.address.commons.events.BaseEvent;

//@@author archthegit

/**
 * generic Event request to toggle between any two panels
 */
public class PanelSwitchRequestEvent extends BaseEvent {

    public final String wantedPanel;

    public PanelSwitchRequestEvent(String wantedPanel) {
        this.wantedPanel = wantedPanel;
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName();
    }
}


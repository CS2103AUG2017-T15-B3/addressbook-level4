package seedu.address.logic.commands;

import seedu.address.commons.core.EventsCenter;
import seedu.address.commons.events.ui.PanelSwitchRequestEvent;
import seedu.address.model.event.EventPredicate;

//@@author archthegit

/**
 * Command to list events for the month
 */
public class EventsCommand extends Command {
    public static final String COMMAND_WORD = "events";
    public static final String COMMAND_ALIAS = "ev";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Lists all events for the next 30 days\n"
            + "Example: " + COMMAND_WORD;

    private final EventPredicate predicate = new EventPredicate();

    public EventsCommand() { }

    @Override
    public CommandResult execute() {
        model.updateFilteredEventList(predicate);
        EventsCenter.getInstance().post(new PanelSwitchRequestEvent(COMMAND_WORD));
        return new CommandResult(getEventsMessageSummary(model.getFilteredEventList().size()));
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof EventsCommand // instanceof handles nulls
                && this.predicate.equals(((EventsCommand) other).predicate)); // state check
    }
}



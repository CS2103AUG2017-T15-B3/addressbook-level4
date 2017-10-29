package seedu.address.ui;

import java.time.LocalDate;
import java.util.Optional;
import java.util.logging.Logger;

import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.util.Callback;
import seedu.address.commons.core.EventsCenter;
import seedu.address.commons.core.LogsCenter;
import seedu.address.commons.events.ui.CalendarPanelSelectionEvent;

import seedu.address.logic.commands.AddEventCommand;
import seedu.address.logic.parser.AddEventCommandParser;
import seedu.address.model.event.Date;
import seedu.address.model.event.Event;
import seedu.address.model.event.exceptions.BuildEvent;
import seedu.address.model.person.Address;
import seedu.address.model.person.Name;

import static seedu.address.logic.parser.CliSyntax.PREFIX_NAME;

/**
 * Create an anchor pane that can store additional data.
 */
public class AnchorPaneNode extends AnchorPane {

    // Date associated with this pane
    private LocalDate date;
    private final Logger logger = LogsCenter.getLogger(this.getClass());

    /**
     * Create a anchor pane node. Date is not assigned in the constructor.
     * @param children children of the anchor pane
     */
    public AnchorPaneNode(Node... children) {
        super(children);
        // Add action handler for mouse clicked
        this.setOnMouseClicked(e ->
                handleCalendarEvent(date.toString()));
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    private void handleCalendarEvent(String event){
        startDialog(event);
        EventsCenter.getInstance().post(new CalendarPanelSelectionEvent(date.toString()));
        logger.info(LogsCenter.getEventHandlingLogMessage(new CalendarPanelSelectionEvent(event)));
    }

    private void startDialog(String date){
        Dialog<Event> dialog = new Dialog<>();
        dialog.setTitle("Add Event");
        dialog.setHeaderText("Fill up the details for the event on " + date);

        Label label1 = new Label("Name: ");
        Label label2 = new Label("Address: ");
        TextField text1 = new TextField();
        TextField text2 = new TextField();

        GridPane grid = new GridPane();
        grid.add(label1, 1, 1);
        grid.add(text1, 2, 1);
        grid.add(label2, 1, 2);
        grid.add(text2, 2, 2);
        dialog.getDialogPane().setContent(grid);

        ButtonType button = new ButtonType("Add", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().add(button);

        dialog.setResultConverter(new Callback<ButtonType, Event>() {
            @Override
            public Event call(ButtonType b) {
                if (b == button){
                    Event newEvent = new BuildEvent().withName(text1.getText()).withDate(date)
                            .withAddress(text2.getText()).build();
                }
                return null;
            }
        });

        Optional<Event> result = dialog.showAndWait();

    }
}

package seedu.address.ui;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Optional;
import java.util.logging.Logger;

import javafx.scene.Node;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import seedu.address.commons.core.EventsCenter;
import seedu.address.commons.core.LogsCenter;
import seedu.address.commons.events.ui.AddEventRequestEvent;
import seedu.address.commons.events.ui.CalendarPanelSelectionEvent;
import seedu.address.model.event.Event;
import seedu.address.model.event.exceptions.BuildEvent;
//@@author SirGoose3432-reused

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
                handleCalendarEvent(date));
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }
    //@@author chernghann
    /**
     * Handles the event when the anchorpane is being clicked
     * @param event
     */
    private void handleCalendarEvent(LocalDate event) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d/MM/yyyy");
        String newDate = formatter.format(event);
        startDialog(newDate);
        EventsCenter.getInstance().post(new CalendarPanelSelectionEvent(date.toString()));
        logger.info(LogsCenter.getEventHandlingLogMessage(new CalendarPanelSelectionEvent(newDate)));
    }

    /**
     * This creates the Dialog window for Add Event
     */
    private void startDialog(String date) {
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

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == button) {
                return new BuildEvent().withName(text1.getText()).withDate(date).withAddress(text2.getText()).build();
            }
            return null;
        });

        Optional<Event> result = dialog.showAndWait();
        result.ifPresent(event -> {
            EventsCenter.getInstance().post(new AddEventRequestEvent(event));
        });
    }
}

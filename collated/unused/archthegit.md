# archthegit
###### /java/seedu/address/commons/events/ui/EventsPanelSelectionChangedEvent.java
``` java

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


```
###### /java/seedu/address/commons/events/ui/PanelSwitchRequestEvent.java
``` java

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

```
###### /java/seedu/address/logic/commands/EventsCommand.java
``` java

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


```
###### /java/seedu/address/model/event/EventPredicate.java
``` java

/**
 * Predicate class checks if a given event falls within the month
 */
public class EventPredicate implements Predicate<ReadOnlyEvent> {

    /**
     * Default constructor for EventPredicate Class
     */
    public EventPredicate() {
    }

    /**
     * Checks if event falls in the month
     * @param event
     * @return
     * @throws ParseException
     */
    public boolean checkIfEventFallsInMonth(ReadOnlyEvent event) throws ParseException {
        Date date = new SimpleDateFormat("dd/MM/yyyy").parse(event.getDate().toString());
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return ((cal.get(Calendar.MONTH)) == Calendar.getInstance().get(Calendar.MONTH));
    }


    @Override
    public boolean test(ReadOnlyEvent event) {
        boolean val = false;
        try {
            val = checkIfEventFallsInMonth(event);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return val;
    }


    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof EventPredicate); // instanceof handles nulls
    }
}


```
###### /java/seedu/address/ui/EventsCard.java
``` java

/**
 * Event card ui to hold event name, date and address
 */
public class EventsCard extends UiPart<Region> {
    private static final String FXML = "EventsListCard.fxml";

    /**
     * Note: Certain keywords such as "location" and "resources" are reserved keywords in JavaFX.
     * As a consequence, UI elements' variable names cannot be set to such keywords
     * or an exception will be thrown by JavaFX during runtime.
     *
     * @see <a href="https://github.com/se-edu/addressbook-level4/issues/336">The issue on AddressBook level 4</a>
     */

    public final ReadOnlyEvent event;

    @FXML
    private HBox cardPane;
    @FXML
    private Label name;
    @FXML
    private Label id;
    @FXML
    private Label address;
    @FXML
    private Label date;

    public EventsCard(ReadOnlyEvent event, int displayedIndex) {
        super(FXML);
        this.event = event;
        id.setText(displayedIndex + ". ");
        bindListeners(event);
    }

    /**
     * Binds the individual UI elements to observe their respective {@code Person} properties
     * so that they will be notified of any changes.
     */
    private void bindListeners(ReadOnlyEvent event) {
        name.textProperty().bind(Bindings.convert(event.nameProperty()));
        date.textProperty().bind(Bindings.convert(event.dateProperty()));
        address.textProperty().bind(Bindings.convert(event.addressProperty()));
    }

    @Override
    public boolean equals(Object other) {
        // short circuit if same object
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof PersonCard)) {
            return false;
        }

        // state check
        EventsCard card = (EventsCard) other;
        return id.getText().equals(card.id.getText())
                && event.equals(card.event);
    }
}

```
###### /java/seedu/address/ui/EventsListPanel.java
``` java

/**
 * Panel containing the list of events.
 */
public class EventsListPanel extends UiPart<Region> {

    private static final String FXML = "EventsListPanel.fxml";
    private final Logger logger = LogsCenter.getLogger(EventsListPanel.class);

    @FXML
    private ListView<EventsCard> eventListView;

    public EventsListPanel(ObservableList<ReadOnlyEvent> eventList) {
        super(FXML);
        setConnections(eventList);
        registerAsAnEventHandler(this);
    }

    private void setConnections(ObservableList<ReadOnlyEvent> eventList) {
        ObservableList<EventsCard> mappedList = EasyBind.map(
                eventList, (event) -> new EventsCard(event, eventList.indexOf(event) + 1));
        eventListView.setItems(mappedList);
        eventListView.setCellFactory(listView -> new EventListViewCell());
        setEventHandlerForSelectionChangeEvent();
    }

    private void setEventHandlerForSelectionChangeEvent() {
        eventListView.getSelectionModel().selectedItemProperty()
                .addListener((observable, oldValue, newValue) -> {
                    if (newValue != null) {
                        logger.fine("Selection in event list panel changed to : '" + newValue + "'");
                        raise(new EventsPanelSelectionChangedEvent(newValue));
                    }
                });
    }

    /**
    * Scrolls to the {@code EventsCard} at the {@code index} and selects it.
    */
    private void scrollTo(int index) {
        Platform.runLater(() -> {
            eventListView.scrollTo(index);
            eventListView.getSelectionModel().clearAndSelect(index);
        });
    }

    @Subscribe
    private void handleJumpToListRequestEvent(JumpToListRequestEvent event) {
        logger.info(LogsCenter.getEventHandlingLogMessage(event));
        scrollTo(event.targetIndex);
    }

    /**
    * Custom {@code ListCell} that displays the graphics of a {@code EventsCard}.
    */
    class EventListViewCell extends ListCell<EventsCard> {

        @Override
        protected void updateItem(EventsCard event, boolean empty) {
            super.updateItem(event, empty);

            if (empty || event == null) {
                setGraphic(null);
                setText(null);
            } else {
                setGraphic(event.getRoot());
            }
        }
    }
}
```
###### /resources/view/DetailsPanel.fxml
``` fxml

<StackPane fx:id="detailsPanel" xmlns="http://javafx.com/javafx/8.0.121" xmlns:fx="http://javafx.com/fxml/1">
<VBox fx:id="pane" prefHeight="600.0" prefWidth="800.0">
    <children>
        <Label fx:id="name" alignment="TOP_LEFT" prefHeight="50.0" prefWidth="360.0" styleClass="window_big_label" text="\$name" wrapText="true" />
        <FlowPane fx:id="tags" prefHeight="50.0" prefWidth="774.0" />
        <TextFlow prefHeight="50.0" prefWidth="100.0">
            <children>
                <Text fx:id="phoneField" fill="gray" styleClass="window_small_label" text="Phone: " />
                <Label fx:id="phone" styleClass="window_small_label" text="\$phone" />
            </children>
        </TextFlow>
        <TextFlow prefHeight="50.0" prefWidth="100.0">
            <children>
                <Text fx:id="homePhoneField" fill="gray" styleClass="window_small_label" text="Home Phone: " />
                <Label fx:id="homePhone" styleClass="window_small_label" text="\$homePhone" />
            </children>
        </TextFlow>
        <TextFlow prefHeight="50.0" prefWidth="400.0">
            <children>
                <Text fx:id="addressField" fill="gray" styleClass="window_small_label" text="Address: " />
                <Label fx:id="address" alignment="TOP_LEFT" maxHeight="800.0" styleClass="window_small_label" text="\$address" wrapText="true" />
            </children>
        </TextFlow>
        <TextFlow prefHeight="50.0" prefWidth="400.0">
            <children>
                <Text fx:id="emailField" fill="gray" styleClass="window_small_label" text="Email: " />
                <Label fx:id="email" styleClass="window_small_label" text="\$email" />
            </children>
        </TextFlow>
        <TextFlow prefHeight="50.0" prefWidth="400.0">
            <children>
                <Text fx:id="schoolEmailField" fill="gray" styleClass="window_small_label" text="School Email: " />
                <Label fx:id="schoolEmail" styleClass="window_small_label" text="\$schoolEmail" />
            </children>
        </TextFlow>
        <TextFlow prefHeight="50.0" prefWidth="400.0">
            <children>
                <Text fx:id="birthdayField" fill="gray" styleClass="window_small_label" text="Birthday: " />
                <Label fx:id="birthday" styleClass="window_small_label" text="\$birthday" />
            </children>
        </TextFlow>
        <TextFlow prefHeight="50.0" prefWidth="400.0">
            <children>
                <Text fx:id="websiteField" fill="gray" styleClass="window_small_label" text="Website: " />
                <Label fx:id="website" styleClass="window_small_label" text="\$website" />
            </children>
        </TextFlow>
    </children>
</VBox>
</StackPane>
```
###### /resources/view/EventsListCard.fxml
``` fxml

<HBox id="cardPane" fx:id="cardPane" xmlns="http://javafx.com/javafx/8" xmlns:fx="http://javafx.com/fxml/1">
    <GridPane HBox.hgrow="ALWAYS">
        <columnConstraints>
            <ColumnConstraints hgrow="SOMETIMES" minWidth="10" prefWidth="150" />
        </columnConstraints>
        <VBox alignment="CENTER_LEFT" minHeight="105" GridPane.columnIndex="0">
            <padding>
                <Insets top="5" right="5" bottom="5" left="15" />
            </padding>
            <HBox spacing="5" alignment="CENTER_LEFT">
                <Label fx:id="id" styleClass="cell_big_label">
                    <minWidth>
                        <!-- Ensures that the label text is never truncated -->
                        <Region fx:constant="USE_PREF_SIZE" />
                    </minWidth>
                </Label>
                <Label fx:id="name" text="\$first" styleClass="cell_big_label" />
            </HBox>

            <Label fx:id="date" styleClass="cell_small_label" text="\$date" />
            <Label fx:id="address" styleClass="cell_small_label" text="\$address" />
        </VBox>

    </GridPane>
</HBox>
```

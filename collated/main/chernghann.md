# chernghann
###### \java\seedu\address\commons\events\ui\AddEventRequestEvent.java
``` java
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
```
###### \java\seedu\address\commons\events\ui\CalendarPanelSelectionEvent.java
``` java
import seedu.address.commons.events.BaseEvent;

/**
 * Represents a selection change in the Calendar Panel
 */

public class CalendarPanelSelectionEvent extends BaseEvent {

    private final String newSelection;

    public CalendarPanelSelectionEvent (String newSelection) {
        this.newSelection = newSelection;
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName();
    }

}
```
###### \java\seedu\address\commons\events\ui\PopulateMonthEvent.java
``` java
import java.time.YearMonth;

import seedu.address.commons.events.BaseEvent;

/**
 * Indicates a request to add Event.
 */
public class PopulateMonthEvent extends BaseEvent {

    public final YearMonth yearMonth;

    public PopulateMonthEvent(YearMonth date) {
        this.yearMonth = date;
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName();
    }
}
```
###### \java\seedu\address\commons\events\ui\PopulateRequestEvent.java
``` java
import seedu.address.commons.events.BaseEvent;
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
```
###### \java\seedu\address\logic\commands\AddEventCommand.java
``` java
import static java.util.Objects.requireNonNull;
import static seedu.address.logic.parser.CliSyntax.PREFIX_ADDRESS;
import static seedu.address.logic.parser.CliSyntax.PREFIX_DATE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NAME;

import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.event.Event;
import seedu.address.model.event.ReadOnlyEvent;
import seedu.address.model.event.exceptions.DuplicateEventException;

/**
 * Adds a event to the address book.
 */
public class AddEventCommand extends UndoableCommand {

    public static final String COMMAND_WORD = "addevent";
    public static final String COMMAND_ALIAS = "ae";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Adds an event to the address book. "
            + "Parameters: "
            + PREFIX_NAME + "NAME "
            + PREFIX_DATE + "DATE "
            + PREFIX_ADDRESS + "ADDRESS\n"
            + "Example: " + COMMAND_WORD + " "
            + PREFIX_NAME + "ZoukOut "
            + PREFIX_DATE + "5/12/2017 "
            + PREFIX_ADDRESS + "Sentosa Beach, Siloso";

    public static final String MESSAGE_SUCCESS = "New event added: %1$s";
    public static final String MESSAGE_DUPLICATE_EVENT = "This event already exists.";

    private final Event toAdd;

    /**
     * Creates an AddEventCommand to add the specified {@code ReadOnlyEvent}
     */
    public AddEventCommand(ReadOnlyEvent event) {
        toAdd = new Event(event);
    }

    @Override
    public CommandResult executeUndoableCommand() throws CommandException {
        requireNonNull(model);
        try {
            model.addEvent(toAdd);
            return new CommandResult(String.format(MESSAGE_SUCCESS, toAdd));
        } catch (DuplicateEventException e) {
            throw new CommandException(MESSAGE_DUPLICATE_EVENT);
        }
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof AddEventCommand // instanceof handles nulls
                && toAdd.equals(((AddEventCommand) other).toAdd));
    }
}
```
###### \java\seedu\address\logic\parser\AddEventCommandParser.java
``` java
import static seedu.address.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_ADDRESS;
import static seedu.address.logic.parser.CliSyntax.PREFIX_DATE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NAME;

import java.util.stream.Stream;

import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.logic.commands.AddEventCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.event.Date;
import seedu.address.model.event.Event;
import seedu.address.model.event.EventName;
import seedu.address.model.event.ReadOnlyEvent;
import seedu.address.model.person.Address;

/**
 * Parses input arguments and creates a new AddEventCommand object
 */
public class AddEventCommandParser implements Parser<AddEventCommand> {
    /**
     * Parses the given {@code String} of arguments in the context of the AddEventCommand
     * and returns an AddEventCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    public AddEventCommand parse(String args) throws ParseException {
        ArgumentMultimap argMultimap =
                ArgumentTokenizer.tokenize(args, PREFIX_NAME, PREFIX_DATE, PREFIX_ADDRESS);

        if (!arePrefixesPresent(argMultimap, PREFIX_NAME, PREFIX_DATE, PREFIX_ADDRESS)) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                    AddEventCommand.MESSAGE_USAGE));
        }

        try {
            EventName name = ParserUtil.parseEventName(argMultimap.getValue(PREFIX_NAME)).get();
            Date date = ParserUtil.parseDate(argMultimap.getValue(PREFIX_DATE)).get();
            Address address = ParserUtil.parseAddress(argMultimap.getValue(PREFIX_ADDRESS)).get();

            ReadOnlyEvent event = new Event(name, date, address);

            return new AddEventCommand(event);
        } catch (IllegalValueException ive) {
            throw new ParseException(ive.getMessage(), ive);
        }
    }

    /**
     * Returns true if none of the prefixes contains empty {@code Optional} values in the given
     * {@code ArgumentMultimap}.
     */
    private static boolean arePrefixesPresent(ArgumentMultimap argumentMultimap, Prefix... prefixes) {
        return Stream.of(prefixes).allMatch(prefix -> argumentMultimap.getValue(prefix).isPresent());
    }

}
```
###### \java\seedu\address\model\AddressBook.java
``` java
    public void setEvents(List<? extends ReadOnlyEvent> events) throws DuplicateEventException {
        this.events.setEvent(events);
    }
```
###### \java\seedu\address\model\AddressBook.java
``` java
    public void addEvent(ReadOnlyEvent p) throws DuplicateEventException {
        events.add(p);
        EventsCenter.getInstance().post(new PopulateRequestEvent(events));
    }
```
###### \java\seedu\address\model\AddressBook.java
``` java
    @Override
    public ObservableList<ReadOnlyEvent> getEventList() {
        return events.asObservableList();
    }
```
###### \java\seedu\address\model\AddressBook.java
``` java

    @Override
    public ObservableList<Tag> getTagList() {
        return tags.asObservableList();
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof AddressBook // instanceof handles nulls
                && this.persons.equals(((AddressBook) other).persons)
                && this.tags.equalsOrderInsensitive(((AddressBook) other).tags))
                && this.events.equals(((AddressBook) other).events);
    }

    @Override
    public int hashCode() {
        // use this method for custom fields hashing instead of implementing your own
        return Objects.hash(persons, tags, events);
    }
}
```
###### \java\seedu\address\model\event\Date.java
``` java
import static java.util.Objects.requireNonNull;

import seedu.address.commons.exceptions.IllegalValueException;

/**
 * Represents a Person's birthday in the address book.
 * Guarantees: immutable; is valid as declared in {@link #isValidDate(String)}
 */
public class Date {

    public static final String MESSAGE_DATE_CONSTRAINTS =
            "The day month and year must be valid in form dd/mm/yyyy \n"
                    + "Single value days should be keyed in without 0 in front.";

    public static final String DATE_VALIDATION_REGEX = "^\\d{1,2}\\/\\d{1,2}\\/\\d{4}$";

    public final String value;

    /**
     * Validates given date.
     *
     * @throws IllegalValueException if given address string is invalid.
     */
    public Date(String date) throws IllegalValueException {
        requireNonNull(date);
        String trimmedDate = date.trim();
        if (!isValidDate(trimmedDate)) {
            throw new IllegalValueException(MESSAGE_DATE_CONSTRAINTS);
        }
        this.value = trimmedDate;
    }

    /**
     * Returns true if a given string is a valid person birthday.
     */
    public static boolean isValidDate(String test) {
        return test.matches(DATE_VALIDATION_REGEX);
    }

    @Override
    public String toString() {
        return value;
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof Date // instanceof handles nulls
                && this.value.equals(((Date) other).value)); // state check
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }

}
```
###### \java\seedu\address\model\event\Event.java
``` java
import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.CollectionUtil.requireAllNonNull;

import java.util.Objects;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import seedu.address.model.person.Address;

/**
 * Represents a Event in the address book.
 * Guarantees: details are present and not null, field values are validated.
 */
public class Event implements ReadOnlyEvent {

    private ObjectProperty<EventName> name;
    private ObjectProperty<Date> date;
    private ObjectProperty<Address> address;

    /**
     * Every field must be present and not null.
     */
    public Event(EventName name, Date date, Address address) {
        requireAllNonNull(name, date, address);
        this.name = new SimpleObjectProperty<>(name);
        this.date = new SimpleObjectProperty<>(date);
        this.address = new SimpleObjectProperty<>(address);
    }

    /**
     * Creates a copy of the given ReadOnlyPerson.
     */
    public Event(ReadOnlyEvent source) {
        this(source.getName(), source.getDate(), source.getAddress());
    }

    public void setName(EventName name) {
        this.name.set(requireNonNull(name));
    }

    @Override
    public ObjectProperty<EventName> nameProperty() {
        return name;
    }

    @Override
    public EventName getName() {
        return name.get();
    }

    public void setAddress(Address address) {
        this.address.set(requireNonNull(address));
    }

    @Override
    public ObjectProperty<Address> addressProperty() {
        return address;
    }

    @Override
    public Address getAddress() {
        return address.get();
    }

    public void setDate(Date date) {
        this.date.set(requireNonNull(date));
    }

    @Override
    public ObjectProperty<Date> dateProperty() {
        return date;
    }

    @Override
    public Date getDate() {
        return date.get();
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof ReadOnlyEvent // instanceof handles nulls
                && this.isSameStateAs((ReadOnlyEvent) other));
    }

    @Override
    public int hashCode() {
        // use this method for custom fields hashing instead of implementing your own
        return Objects.hash(name, date, address);
    }

    @Override
    public String toString() {
        return getAsText();
    }

}
```
###### \java\seedu\address\model\event\EventName.java
``` java
import static java.util.Objects.requireNonNull;

import seedu.address.commons.exceptions.IllegalValueException;

/**
 * Represents a Events's name in the address book.
 * Guarantees: immutable; is valid as declared in {@link #isValidName(String)}
 */
public class EventName {

    public static final String MESSAGE_EVENT_NAME_CONSTRAINTS =
            "Event names should only contain alphanumeric characters and spaces, and it should not be blank";

    /*
     * The first character of the address must not be a whitespace,
     * otherwise " " (a blank string) becomes a valid input.
     */
    public static final String EVENT_NAME_VALIDATION_REGEX = "[\\p{Alnum}][\\p{Alnum}'!, ]*";

    public final String fullName;

    /**
     * Validates given name.
     *
     * @throws IllegalValueException if given name string is invalid.
     */
    public EventName(String name) throws IllegalValueException {
        requireNonNull(name);
        String trimmedName = name.trim();
        if (!isValidName(trimmedName)) {
            throw new IllegalValueException(MESSAGE_EVENT_NAME_CONSTRAINTS);
        }
        this.fullName = trimmedName;
    }

    /**
     * Returns true if a given string is a valid person name.
     */
    public static boolean isValidName(String test) {
        return test.matches(EVENT_NAME_VALIDATION_REGEX);
    }


    @Override
    public String toString() {
        return fullName;
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof seedu.address.model.person.Name // instanceof handles nulls
                && this.fullName.equals(((seedu.address.model.person.Name) other).fullName)); // state check
    }

    @Override
    public int hashCode() {
        return fullName.hashCode();
    }
}
```
###### \java\seedu\address\model\event\exceptions\BuildEvent.java
``` java
import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.model.event.Date;
import seedu.address.model.event.Event;
import seedu.address.model.event.EventName;
import seedu.address.model.person.Address;

/**
 * A utility class to help with building Event objects.
 */
public class BuildEvent {
    public static final String DEFAULT_NAME = "ZoukOut";
    public static final String DEFAULT_ADDRESS = "Sentosa, Siloso Beach";
    public static final String DEFAULT_DATE = "12/12/2018";

    private Event event;

    public BuildEvent() {
        try {
            EventName defaultName = new EventName(DEFAULT_NAME);
            Date defaultDate = new Date(DEFAULT_DATE);
            Address defaultAddress = new Address(DEFAULT_ADDRESS);

            this.event = new Event(defaultName, defaultDate, defaultAddress);

        } catch (IllegalValueException ive) {
            throw new AssertionError("Default event's values are invalid.");
        }
    }

    /**
     * Sets the {@code Name} of the {@code Event} that we are building.
     */
    public BuildEvent withName(String name) {
        try {
            this.event.setName(new EventName(name));
        } catch (IllegalValueException ive) {
            throw new IllegalArgumentException("name is expected to be unique.");
        }
        return this;
    }

    /**
     * Sets the {@code Address} of the {@code Event} that we are building.
     */
    public BuildEvent withAddress(String address) {
        try {
            this.event.setAddress(new Address(address));
        } catch (IllegalValueException ive) {
            throw new IllegalArgumentException("address is expected to be unique.");
        }
        return this;
    }

    /**
     * Sets the {@code Date} of the {@code Event} that we are building.
     */
    public BuildEvent withDate(String date) {
        try {
            this.event.setDate(new Date(date));
        } catch (IllegalValueException ive) {
            throw new IllegalArgumentException("date is expected to be unique.");
        }
        return this;
    }

    public Event build() {
        return this.event;
    }

}
```
###### \java\seedu\address\model\event\exceptions\DuplicateEventException.java
``` java
import seedu.address.commons.exceptions.DuplicateDataException;

/**
 * Signals that the operation will result in duplicate Event objects.
 */
public class DuplicateEventException extends DuplicateDataException {
    public DuplicateEventException() {
        super("Operation would result in duplicate event");
    }
}
```
###### \java\seedu\address\model\event\exceptions\EventNotFoundException.java
``` java
/**
 * Signals that the operation is unable to find the specified event
 */

public class EventNotFoundException extends Exception {
    public EventNotFoundException() {
        super("event not found!");
    }
}
```
###### \java\seedu\address\model\event\ReadOnlyEvent.java
``` java
import javafx.beans.property.ObjectProperty;
import seedu.address.model.person.Address;

/**
 * A read-only immutable interface for a Person in the addressbook.
 * Implementations should guarantee: details are present and not null, field values are validated.
 */
public interface ReadOnlyEvent {

    ObjectProperty<EventName> nameProperty();
    EventName getName();
    ObjectProperty<Date> dateProperty();
    Date getDate();
    ObjectProperty<Address> addressProperty();
    Address getAddress();

    /**
     * Returns true if both have the same state. (interfaces cannot override .equals)
     */
    default boolean isSameStateAs(ReadOnlyEvent other) {
        return other == this // short circuit if same object
                || (other != null // this is first to avoid NPE below
                && other.getName().equals(this.getName()) // state checks here onwards
                && other.getDate().equals(this.getDate())
                && other.getAddress().equals(this.getAddress()));
    }

    /**
     * Formats the event as text, showing all event details.
     */
    default String getAsText() {
        final StringBuilder builder = new StringBuilder();
        builder.append(getName())
                .append(" Date: ")
                .append(getDate())
                .append(" Address: ")
                .append(getAddress());
        return builder.toString();
    }

}
```
###### \java\seedu\address\model\event\UniqueEventList.java
``` java
import static java.util.Objects.requireNonNull;

import java.util.Iterator;
import java.util.List;

import org.fxmisc.easybind.EasyBind;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import seedu.address.model.event.exceptions.DuplicateEventException;
import seedu.address.model.event.exceptions.EventNotFoundException;

/**
 * A list of events that enforces uniqueness between its elements and does not allow nulls.
 *
 * Supports a minimal set of list operations.
 *
 * @see Event#equals(Object)
 */
public class UniqueEventList implements Iterable<Event> {

    private final ObservableList<Event> internalList = FXCollections.observableArrayList();
    // used by asObservableList()
    private final ObservableList<ReadOnlyEvent> mappedList = EasyBind.map(internalList, (event) -> event);

    /**
     * Returns true if the list contains an equivalent event as the given argument.
     */
    public boolean contains(ReadOnlyEvent toCheck) {
        requireNonNull(toCheck);
        return internalList.contains(toCheck);
    }

    /**
     * Adds an event to the list.
     *
     * @throws DuplicateEventException if the person to add is a duplicate of an existing event in the list.
     */
    public void add(ReadOnlyEvent toAdd) throws DuplicateEventException {
        requireNonNull(toAdd);
        if (contains(toAdd)) {
            throw new DuplicateEventException();
        }
        internalList.add(new Event(toAdd));
    }

    public void setEvent(UniqueEventList replacement) {
        this.internalList.setAll(replacement.internalList);
    }

    public void setEvent(List<? extends ReadOnlyEvent> events) throws DuplicateEventException {
        final UniqueEventList replacement = new UniqueEventList();
        for (final ReadOnlyEvent event : events) {
            replacement.add(new Event(event));
        }
        setEvent(replacement);
    }

    /**
     * Removes the equivalent event from the list.
     *
     * @throws EventNotFoundException if no such event could be found in the list.
     */
    public boolean remove(ReadOnlyEvent toRemove) throws EventNotFoundException {
        requireNonNull(toRemove);
        final boolean eventFoundAndDeleted = internalList.remove(toRemove);
        if (!eventFoundAndDeleted) {
            throw new EventNotFoundException();
        }
        return eventFoundAndDeleted;
    }

    /**
     * Returns the backing list as an unmodifiable {@code ObservableList}.
     */
    public ObservableList<ReadOnlyEvent> asObservableList() {
        return FXCollections.unmodifiableObservableList(mappedList);
    }

    @Override
    public Iterator<Event> iterator() {
        return internalList.iterator();
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof UniqueEventList // instanceof handles nulls
                && this.internalList.equals(((UniqueEventList) other).internalList));
    }

    @Override
    public int hashCode() {
        return internalList.hashCode();
    }
}
```
###### \java\seedu\address\model\Model.java
``` java
    /** Adds the given event */
    void addEvent(ReadOnlyEvent event) throws DuplicateEventException;
```
###### \java\seedu\address\model\Model.java
``` java
    /** Returns an unmodifiable view of the filtered event list */
    ObservableList<ReadOnlyEvent> getFilteredEventList();
```
###### \java\seedu\address\model\Model.java
``` java
    /**
     * Updates the filter of the filtered events list to filter by the given {@code predicate}.
     * @throws NullPointerException if {@code predicate} is null.
     */
    void updateFilteredEventList(Predicate<ReadOnlyEvent> predicate);
```
###### \java\seedu\address\model\ModelManager.java
``` java
    @Override
    public synchronized void addEvent(ReadOnlyEvent event) throws DuplicateEventException {
        addressBook.addEvent(event);
        updateFilteredEventList(PREDICATE_SHOW_ALL_EVENTS);
        indicateAddressBookChanged();
    }
```
###### \java\seedu\address\model\ModelManager.java
``` java
    /**
     * Returns an unmodifiable view of the list of {@code ReadOnlyEvent} backed by the internal list of
     * {@code addressBook}
     */
    @Override
    public ObservableList<ReadOnlyEvent> getFilteredEventList() {
        return FXCollections.unmodifiableObservableList(filteredEvents);
    }

    @Override
    public void updateFilteredEventList(Predicate<ReadOnlyEvent> predicate) {
        requireNonNull(predicate);
        filteredEvents.setPredicate(predicate);
    }
```
###### \java\seedu\address\model\ModelManager.java
``` java
    @Subscribe
    private void handleAddEvent(AddEventRequestEvent event) throws DuplicateEventException {
        logger.info(LogsCenter.getEventHandlingLogMessage(event));
        addressBook.addEvent(event.event);
        updateFilteredEventList(PREDICATE_SHOW_ALL_EVENTS);
        indicateAddressBookChanged();
    }
```
###### \java\seedu\address\model\person\HomeNumber.java
``` java
import seedu.address.commons.exceptions.IllegalValueException;

/**
 * Represents a Person's home number in the address book.
 * Guarantees: immutable; is valid as declared in {@link #isValidHomeNumber(String)}
 */
public class HomeNumber {


    public static final String MESSAGE_HOME_NUMBER_CONSTRAINTS =
            "Home numbers can only contain numbers, and should be at least 3 digits long";
    public static final String HOME_NUMBER_VALIDATION_REGEX = "\\d{3,}";
    public static final String HOME_NUMBER_TEMPORARY = "NIL";

    public final String value;

```
###### \java\seedu\address\model\person\Person.java
``` java
    public void setHomeNumber(HomeNumber homeNumber) {
        this.homeNumber.set(requireNonNull(homeNumber));
    }

    @Override
    public ObjectProperty<HomeNumber> homeNumberProperty() {
        return homeNumber;
    }

    @Override
    public HomeNumber getHomeNumber() {
        return homeNumber.get();
    }
```
###### \java\seedu\address\model\person\Person.java
``` java
    public void setSchEmail(SchEmail schEmail) {
        this.schEmail.set(requireNonNull(schEmail));
    }

    @Override
    public ObjectProperty<SchEmail> schEmailProperty() {
        return schEmail;
    }

    @Override
    public SchEmail getSchEmail() {
        return schEmail.get();
    }

```
###### \java\seedu\address\model\person\SchEmail.java
``` java
import seedu.address.commons.exceptions.IllegalValueException;

/**
 * Represents a Person's phone number in the address book.
 * Guarantees: immutable; is valid as declared in {@link #isValidSchEmail(String)}
 */
public class SchEmail {

    public static final String MESSAGE_SCH_EMAIL_CONSTRAINTS =
            "School emails should be 2 alphanumeric/period strings separated by '@'";
    public static final String SCH_EMAIL_VALIDATION_REGEX = "[\\w\\.]+@[\\w\\.]+";
    public static final String SCH_EMAIL_TEMPORARY = "NIL";

    public final String value;

```
###### \java\seedu\address\model\ReadOnlyAddressBook.java
``` java
    ObservableList<ReadOnlyEvent> getEventList();
```
###### \java\seedu\address\storage\Storage.java
``` java
    Optional<ReadOnlyAddressBook> readBackupAddressBook() throws DataConversionException, IOException;
```
###### \java\seedu\address\storage\StorageManager.java
``` java
    public Optional<ReadOnlyAddressBook> readBackupAddressBook() throws DataConversionException, IOException {
        return readAddressBook(backUpLocation.getAddressBookFilePath());
    }
```
###### \java\seedu\address\storage\StorageManager.java
``` java

    @Override
    @Subscribe
    public void handleAddressBookChangedEvent(AddressBookChangedEvent event) {
        logger.info(LogsCenter.getEventHandlingLogMessage(event, "Local data changed, saving to file"));
        try {
            saveAddressBook(event.data);
            backUpAddressBook(event.data);
        } catch (IOException e) {
            raise(new DataSavingExceptionEvent(e));
        }
    }

```
###### \java\seedu\address\storage\StorageManager.java
``` java
    private void backUpAddressBook(ReadOnlyAddressBook addressBook) throws IOException {
        String backupPath = backUpLocation.getAddressBookFilePath();
        logger.fine("Backing up data to: " + backupPath);
        saveAddressBook(addressBook, backupPath);
    }
```
###### \java\seedu\address\storage\XmlAdaptedEvent.java
``` java
import javax.xml.bind.annotation.XmlElement;

import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.model.event.Date;
import seedu.address.model.event.Event;
import seedu.address.model.event.EventName;
import seedu.address.model.event.ReadOnlyEvent;
import seedu.address.model.person.Address;

/**
 * JAXB-friendly version of the Person.
 */
public class XmlAdaptedEvent {

    @XmlElement(required = true)
    private String name;
    @XmlElement(required = true)
    private String address;
    @XmlElement (required = true)
    private String date;

    /**
     * Constructs an XmlAdaptedEvent.
     * This is the no-arg constructor that is required by JAXB.
     */
    public XmlAdaptedEvent() {}


    /**
     * Converts a given Person into this class for JAXB use.
     *
     * @param source future changes to this will not affect the created XmlAdaptedPerson
     */
    public XmlAdaptedEvent(ReadOnlyEvent source) {
        name = source.getName().fullName;
        date = source.getDate().value;
        address = source.getAddress().value;
    }

    /**
     * Converts this jaxb-friendly adapted person object into the model's Person object.
     *
     * @throws IllegalValueException if there were any data constraints violated in the adapted person
     */
    public Event toModelType() throws IllegalValueException {
        final EventName name = new EventName(this.name);
        final Date date = new Date(this.date);
        final Address address = new Address(this.address);

        return new Event(name, date, address);
    }
}
```
###### \java\seedu\address\ui\AnchorPaneNode.java
``` java
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
```
###### \java\seedu\address\ui\Calendar.java
``` java
    /**
     * Move the month back by one. Repopulate the calendar with the correct dates.
     */
    public void previousMonth() {
        currentYearMonth = currentYearMonth.minusMonths(1);
        EventsCenter.getInstance().post(new PopulateMonthEvent(currentYearMonth));
    }

    /**
     * Move the month forward by one. Repopulate the calendar with the correct dates.
     */
    public void nextMonth() {
        currentYearMonth = currentYearMonth.plusMonths(1);
        EventsCenter.getInstance().post(new PopulateMonthEvent(currentYearMonth));
    }
```
###### \java\seedu\address\ui\Calendar.java
``` java
    /**
     * populating updated calendar for the switch buttons for the add of events
     * @param eventList
     */
    public void populateUpdatedCalendar(UniqueEventList eventList) {
        for (AnchorPaneNode ap : allCalendarDays) {
            ap.setStyle("-fx-background-color: transparent;");
            for (Event event1 : eventList) {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d/MM/yyyy");
                String newDate = formatter.format(ap.getDate());
                if (newDate.equals(event1.getDate().toString())) {
                    ap.getChildren();
                    ap.setStyle("-fx-background-color: #ffebcd;");
                }
            }
        }
    }
}
```
###### \java\seedu\address\ui\MainWindow.java
``` java
    /**
     * this method is to populate the calendar when there is an add event.
     * @param request
     */
    @Subscribe
    private void handlePopulateEvent(PopulateRequestEvent request) {
        logger.info(LogsCenter.getEventHandlingLogMessage(request));
        calendar.populateUpdatedCalendar(request.eventList);
    }

    /**
     * For populating the calendar when starting the application and changing the months
     * @param request
     */
    @Subscribe
    private void handlePopulateMonthEvent(PopulateMonthEvent request) {
        logger.info(LogsCenter.getEventHandlingLogMessage(request));
        calendar.populateCalendar(request.yearMonth, logic.getFilteredEventList());
    }
```

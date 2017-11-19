# itsdickson
###### \java\seedu\address\commons\events\ui\ChangeThemeRequestEvent.java
``` java

import seedu.address.commons.events.BaseEvent;

/**
 * Indicates a request to change the theme.
 */
public class ChangeThemeRequestEvent extends BaseEvent {

    public final String theme;

    public ChangeThemeRequestEvent(String theme) {
        this.theme = theme;
    }


    @Override
    public String toString() {
        return this.getClass().getSimpleName();
    }
}
```
###### \java\seedu\address\commons\events\ui\EventPanelSelectionChangedEvent.java
``` java

import seedu.address.commons.events.BaseEvent;
import seedu.address.ui.EventCard;

/**
 * Represents a selection change in the Event List Panel
 */
public class EventPanelSelectionChangedEvent extends BaseEvent {

    private final EventCard newSelection;

    public EventPanelSelectionChangedEvent(EventCard newSelection) {
        this.newSelection = newSelection;
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName();
    }

    public EventCard getNewSelection() {
        return newSelection;
    }
}
```
###### \java\seedu\address\commons\events\ui\ShowThemeRequestEvent.java
``` java

import seedu.address.commons.events.BaseEvent;

/**
 * Indicates a request to view the themes page.
 */
public class ShowThemeRequestEvent extends BaseEvent {

    @Override
    public String toString() {
        return this.getClass().getSimpleName();
    }
}
```
###### \java\seedu\address\logic\commands\DeleteEventCommand.java
``` java

import java.util.List;

import seedu.address.commons.core.Messages;
import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.event.ReadOnlyEvent;
import seedu.address.model.event.exceptions.EventNotFoundException;

/**
 * Deletes a event identified using it's last displayed index from the address book.
 */
public class DeleteEventCommand extends UndoableCommand {

    public static final String COMMAND_WORD = "deleteevent";
    public static final String COMMAND_ALIAS = "de";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Deletes the event identified by the index number used in the last person listing.\n"
            + "Parameters: INDEX (must be a positive integer)\n"
            + "Example: " + COMMAND_WORD + " 1";

    public static final String MESSAGE_DELETE_EVENT_SUCCESS = "Deleted Event: %1$s";

    private final Index targetIndex;

    public DeleteEventCommand(Index targetIndex) {
        this.targetIndex = targetIndex;
    }


    @Override
    public CommandResult executeUndoableCommand() throws CommandException {

        List<ReadOnlyEvent> lastShownList = model.getFilteredEventList();

        if (targetIndex.getZeroBased() >= lastShownList.size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_DISPLAYED_INDEX);
        }

        ReadOnlyEvent eventToDelete = lastShownList.get(targetIndex.getZeroBased());

        try {
            model.deleteEvent(eventToDelete);
        } catch (EventNotFoundException enfe) {
            assert false : "The target event cannot be missing";
        }

        return new CommandResult(String.format(MESSAGE_DELETE_EVENT_SUCCESS, eventToDelete));
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof DeleteEventCommand // instanceof handles nulls
                && this.targetIndex.equals(((DeleteEventCommand) other).targetIndex)); // state check
    }
}
```
###### \java\seedu\address\logic\commands\EventsCommand.java
``` java
/**
 * Lists all events in the address book to the user.
 */
public class EventsCommand extends Command {

    public static final String COMMAND_WORD = "events";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Shows a list of events.\n"
            + "Example: " + COMMAND_WORD;

    public static final String SHOWING_EVENTS_MESSAGE = "Listed all events.";

    @Override
    public CommandResult execute() {
        EventsCenter.getInstance().post(new TogglePanelEvent(COMMAND_WORD));
        EventsCenter.getInstance().post(new PersonPanelUnselectEvent());
        EventsCenter.getInstance().post(new AccessWebsiteRequestEvent("https://maps.google.com/"));
        return new CommandResult(SHOWING_EVENTS_MESSAGE);
    }
}
```
###### \java\seedu\address\logic\commands\FavouriteCommand.java
``` java

import java.util.List;

import seedu.address.commons.core.Messages;
import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.person.ReadOnlyPerson;
import seedu.address.model.person.exceptions.PersonNotFoundException;

/**
 * Favourites a person identified using it's last displayed index from the address book.
 */
public class FavouriteCommand extends UndoableCommand {

    public static final String COMMAND_WORD = "favourite";
    public static final String COMMAND_ALIAS = "fav";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Favourites the person identified by the index number used in the last person listing.\n"
            + "Parameters: INDEX (must be a positive integer)\n"
            + "Example: " + COMMAND_WORD + " 1";

    public static final String MESSAGE_FAVOURITE_PERSON_SUCCESS = "Favourited Person: %1$s";
    public static final String MESSAGE_FAVOURITE_PERSON_FAIL = "Person is already favourited";

    private final Index targetIndex;

    public FavouriteCommand(Index targetIndex) {
        this.targetIndex = targetIndex;
    }

    @Override
    public CommandResult executeUndoableCommand() throws CommandException {

        List<ReadOnlyPerson> lastShownList = model.getFilteredPersonList();

        if (targetIndex.getZeroBased() >= lastShownList.size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_DISPLAYED_INDEX);
        }

        ReadOnlyPerson personToFavourite = lastShownList.get(targetIndex.getZeroBased());

        if (personToFavourite.getFavourite().equals(true)) {
            throw new CommandException(MESSAGE_FAVOURITE_PERSON_FAIL);
        }

        try {
            model.favouritePerson(personToFavourite);
        } catch (PersonNotFoundException pnfe) {
            assert false : "The target person cannot be missing";
        }

        return new CommandResult(String.format(MESSAGE_FAVOURITE_PERSON_SUCCESS, personToFavourite));
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof FavouriteCommand // instanceof handles nulls
                && this.targetIndex.equals(((FavouriteCommand) other).targetIndex)); // state check
    }
}
```
###### \java\seedu\address\logic\commands\FavouriteListCommand.java
``` java

import seedu.address.model.person.NameContainsFavouritePredicate;

/**
 * Lists all favourited persons in the address book to the user.
 */
public class FavouriteListCommand extends Command {

    public static final String COMMAND_WORD = "favourites";

    public static final String MESSAGE_SUCCESS = "Listed all favourited persons";

    private static final NameContainsFavouritePredicate predicate = new NameContainsFavouritePredicate();

    @Override
    public CommandResult execute() {
        model.updateFilteredPersonList(predicate);
        return new CommandResult(MESSAGE_SUCCESS);
    }
}
```
###### \java\seedu\address\logic\commands\FindTagCommand.java
``` java

import seedu.address.model.person.TagContainsKeywordsPredicate;

/**
 * Finds and lists all persons in address book whose tag contains any of the argument keywords.
 * Keyword matching is case insensitive.
 */
public class FindTagCommand extends Command {

    public static final String COMMAND_WORD = "findtag";
    public static final String COMMAND_ALIAS = "ft";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Finds all persons whose tags contain any of "
            + "the specified keywords (case-insensitive) and displays them as a list with index numbers.\n"
            + "Parameters: KEYWORD [MORE_KEYWORDS]...\n"
            + "Example: " + COMMAND_WORD + " friend";

    private final TagContainsKeywordsPredicate predicate;

    public FindTagCommand(TagContainsKeywordsPredicate predicate) {
        this.predicate = predicate;
    }

    @Override
    public CommandResult execute() {
        model.updateFilteredPersonList(predicate);
        return new CommandResult(getMessageForPersonListShownSummary(model.getFilteredPersonList().size()));
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof FindTagCommand // instanceof handles nulls
                && this.predicate.equals(((FindTagCommand) other).predicate)); // state check
    }
}
```
###### \java\seedu\address\logic\commands\SwitchThemeCommand.java
``` java

import java.util.ArrayList;

import seedu.address.commons.core.EventsCenter;
import seedu.address.commons.core.Messages;
import seedu.address.commons.core.index.Index;
import seedu.address.commons.events.ui.ChangeThemeRequestEvent;
import seedu.address.logic.commands.exceptions.CommandException;

/**
 * Switches the current theme to a theme identified using it's index from the themes list in address book.
 */
public class SwitchThemeCommand extends Command {

    public static final String COMMAND_WORD = "switch";
    public static final String COMMAND_ALIAS = "sw";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Switches the current theme to the theme identified by the index number in the themes list.\n"
            + "Parameters: INDEX (must be a positive integer)\n"
            + "Example: " + COMMAND_WORD + " 1";

    public static final String MESSAGE_DARK_THEME_SUCCESS = "Switched Theme: Dark Theme";
    public static final String MESSAGE_LIGHT_THEME_SUCCESS = "Switched Theme: Light Theme";

    public static final String VIEW_PATH = "/view/";

    private final Index targetIndex;

    public SwitchThemeCommand(Index targetIndex) {
        this.targetIndex = targetIndex;
    }


    @Override
    public CommandResult execute() throws CommandException {

        ArrayList<String> themesList = model.getThemesList();

        if (targetIndex.getZeroBased() >= themesList.size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_DISPLAYED_INDEX);
        }

        String themeToChange = themesList.get(targetIndex.getZeroBased());
        String currentTheme = model.getCurrentTheme();

        if (currentTheme.equals(VIEW_PATH + themeToChange)) {
            throw new CommandException(Messages.MESSAGE_INVALID_SWITCH);
        }

        EventsCenter.getInstance().post(new ChangeThemeRequestEvent(themeToChange));

        if (themeToChange.equals("DarkTheme.css")) {
            return new CommandResult(MESSAGE_DARK_THEME_SUCCESS);
        } else {
            return new CommandResult(MESSAGE_LIGHT_THEME_SUCCESS);
        }
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof SwitchThemeCommand // instanceof handles nulls
                && this.targetIndex.equals(((SwitchThemeCommand) other).targetIndex)); // state check
    }
}
```
###### \java\seedu\address\logic\commands\ThemeListCommand.java
``` java

import seedu.address.commons.core.EventsCenter;
import seedu.address.commons.events.ui.ShowThemeRequestEvent;

/**
 * Lists all existing themes in the address book to the user.
 */
public class ThemeListCommand extends Command {

    public static final String COMMAND_WORD = "themes";

    public static final String MESSAGE_SUCCESS = "Listed all themes";


    @Override
    public CommandResult execute() {
        EventsCenter.getInstance().post(new ShowThemeRequestEvent());
        return new CommandResult(MESSAGE_SUCCESS);
    }
}
```
###### \java\seedu\address\logic\commands\UnfavouriteCommand.java
``` java

import java.util.List;

import seedu.address.commons.core.Messages;
import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.person.ReadOnlyPerson;
import seedu.address.model.person.exceptions.PersonNotFoundException;

/**
 * Unfavourites a person identified using it's last displayed index from the address book.
 */
public class UnfavouriteCommand extends UndoableCommand {

    public static final String COMMAND_WORD = "unfavourite";
    public static final String COMMAND_ALIAS = "unfav";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Unfavourites the person identified by the index number used in the last person listing.\n"
            + "Parameters: INDEX (must be a positive integer)\n"
            + "Example: " + COMMAND_WORD + " 1";

    public static final String MESSAGE_UNFAVOURITE_PERSON_SUCCESS = "Unfavourited Person: %1$s";
    public static final String MESSAGE_UNFAVOURITE_PERSON_FAIL = "Person is not favourited";

    private final Index targetIndex;

    public UnfavouriteCommand(Index targetIndex) {
        this.targetIndex = targetIndex;
    }

    @Override
    public CommandResult executeUndoableCommand() throws CommandException {

        List<ReadOnlyPerson> lastShownList = model.getFilteredPersonList();

        if (targetIndex.getZeroBased() >= lastShownList.size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_DISPLAYED_INDEX);
        }

        ReadOnlyPerson personToUnfavourite = lastShownList.get(targetIndex.getZeroBased());

        if (personToUnfavourite.getFavourite().equals(false)) {
            throw new CommandException(MESSAGE_UNFAVOURITE_PERSON_FAIL);
        }

        try {
            model.unfavouritePerson(personToUnfavourite);
        } catch (PersonNotFoundException pnfe) {
            assert false : "The target person cannot be missing";
        }

        return new CommandResult(String.format(MESSAGE_UNFAVOURITE_PERSON_SUCCESS, personToUnfavourite));
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof UnfavouriteCommand // instanceof handles nulls
                && this.targetIndex.equals(((UnfavouriteCommand) other).targetIndex)); // state check
    }
}
```
###### \java\seedu\address\logic\Logic.java
``` java
    /** Returns an unmodifiable view of the filtered list of events */
    ObservableList<ReadOnlyEvent> getFilteredEventList();
```
###### \java\seedu\address\logic\Logic.java
``` java
    void setCurrentTheme(String theme);
```
###### \java\seedu\address\logic\LogicManager.java
``` java
    @Override
    public ObservableList<ReadOnlyPerson> getFilteredPersonList() {
        return model.getFilteredPersonList();
    }
```
###### \java\seedu\address\logic\LogicManager.java
``` java
    @Override
    public void setCurrentTheme(String theme) {
        model.setCurrentTheme(theme);
    }
```
###### \java\seedu\address\logic\parser\DeleteEventCommandParser.java
``` java
/**
 * Parses input arguments and creates a new DeleteEventCommand object
 */
public class DeleteEventCommandParser implements Parser<DeleteEventCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the DeleteEventCommand
     * and returns an DeleteEventCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    public DeleteEventCommand parse(String args) throws ParseException {
        try {
            Index index = ParserUtil.parseIndex(args);
            return new DeleteEventCommand(index);
        } catch (IllegalValueException ive) {
            throw new ParseException(
                    String.format(MESSAGE_INVALID_COMMAND_FORMAT, DeleteEventCommand.MESSAGE_USAGE));
        }
    }

}
```
###### \java\seedu\address\logic\parser\FavouriteCommandParser.java
``` java

import static seedu.address.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;

import seedu.address.commons.core.index.Index;
import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.logic.commands.FavouriteCommand;
import seedu.address.logic.parser.exceptions.ParseException;

/**
 * Parses input arguments and creates a new FavouriteCommand object
 */
public class FavouriteCommandParser implements Parser<FavouriteCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the FavouriteCommand
     * and returns an FavouriteCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    public FavouriteCommand parse(String args) throws ParseException {
        try {
            Index index = ParserUtil.parseIndex(args);
            return new FavouriteCommand(index);
        } catch (IllegalValueException ive) {
            throw new ParseException(
                    String.format(MESSAGE_INVALID_COMMAND_FORMAT, FavouriteCommand.MESSAGE_USAGE));
        }
    }
}
```
###### \java\seedu\address\logic\parser\FindTagCommandParser.java
``` java

import static seedu.address.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;

import java.util.Arrays;

import seedu.address.logic.commands.FindTagCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.person.TagContainsKeywordsPredicate;

/**
 * Parses input arguments and creates a new FindTagCommand object
 */
public class FindTagCommandParser implements Parser<FindTagCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the FindTagCommand
     * and returns an FindCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    public FindTagCommand parse(String args) throws ParseException {
        String trimmedArgs = args.trim();
        if (trimmedArgs.isEmpty()) {
            throw new ParseException(
                    String.format(MESSAGE_INVALID_COMMAND_FORMAT, FindTagCommand.MESSAGE_USAGE));
        }

        String[] nameKeywords = trimmedArgs.split("\\s+");

        return new FindTagCommand(new TagContainsKeywordsPredicate(Arrays.asList(nameKeywords)));
    }

}
```
###### \java\seedu\address\logic\parser\SwitchThemeCommandParser.java
``` java

import static seedu.address.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;

import seedu.address.commons.core.index.Index;
import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.logic.commands.SwitchThemeCommand;
import seedu.address.logic.parser.exceptions.ParseException;

/**
 * Parses input arguments and creates a new SwitchThemeCommand object
 */
public class SwitchThemeCommandParser implements Parser<SwitchThemeCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the SwitchThemeCommand
     * and returns an SwitchThemeCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    public SwitchThemeCommand parse(String args) throws ParseException {
        try {
            Index index = ParserUtil.parseIndex(args);
            return new SwitchThemeCommand(index);
        } catch (IllegalValueException ive) {
            throw new ParseException(
                    String.format(MESSAGE_INVALID_COMMAND_FORMAT, SwitchThemeCommand.MESSAGE_USAGE));
        }
    }

}
```
###### \java\seedu\address\logic\parser\UnfavouriteCommandParser.java
``` java

import static seedu.address.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;

import seedu.address.commons.core.index.Index;
import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.logic.commands.UnfavouriteCommand;
import seedu.address.logic.parser.exceptions.ParseException;

/**
 * Parses input arguments and creates a new UnfavouriteCommand object
 */
public class UnfavouriteCommandParser implements Parser<UnfavouriteCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the UnfavouriteCommand
     * and returns an UnfavouriteCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    public UnfavouriteCommand parse(String args) throws ParseException {
        try {
            Index index = ParserUtil.parseIndex(args);
            return new UnfavouriteCommand(index);
        } catch (IllegalValueException ive) {
            throw new ParseException(
                    String.format(MESSAGE_INVALID_COMMAND_FORMAT, UnfavouriteCommand.MESSAGE_USAGE));
        }
    }
}
```
###### \java\seedu\address\model\AddressBook.java
``` java
    /**
     * Favourites {@code target} to this {@code AddressBook}.
     * @throws PersonNotFoundException if the {@code target} is not in this {@code AddressBook}.
     */
    public void favouritePerson(ReadOnlyPerson target) throws PersonNotFoundException {
        persons.favouritePerson(target);
    }

    /**
     * Unfavourites {@code target} from this {@code AddressBook}.
     * @throws PersonNotFoundException if the {@code target} is not in this {@code AddressBook}.
     */
    public void unfavouritePerson(ReadOnlyPerson target) throws PersonNotFoundException {
        persons.unfavouritePerson(target);
    }
```
###### \java\seedu\address\model\AddressBook.java
``` java
    /**
     * Deletes an event from the address book.
     *
     * @throws EventNotFoundException if the {@code event} is not in this {@code AddressBook}.
     */
    public boolean deleteEvent(ReadOnlyEvent event) throws EventNotFoundException {
        if (events.remove(event)) {
            EventsCenter.getInstance().post(new PopulateRequestEvent(events));
            return true;
        } else {
            throw new EventNotFoundException();
        }
    }

    /**
     * Initialises the Themes ArrayList
     */
    private void initialiseThemes() {
        themes.add("DarkTheme.css");
        themes.add("BrightTheme.css");
    }

    public ArrayList<String> getThemesList() {
        return themes;
    }
```
###### \java\seedu\address\model\Model.java
``` java
    /** Deletes the given event */
    void deleteEvent(ReadOnlyEvent event) throws EventNotFoundException;

    /** Favourites the given person */
    void favouritePerson(ReadOnlyPerson target) throws PersonNotFoundException;

    /** Unfavourites the given person */
    void unfavouritePerson(ReadOnlyPerson target) throws PersonNotFoundException;
```
###### \java\seedu\address\model\Model.java
``` java
    /** Returns the themes list */
    ArrayList<String> getThemesList();

    /** Sets the current theme */
    void setCurrentTheme(String theme);

    /** Returns the current theme */
    String getCurrentTheme();
```
###### \java\seedu\address\model\ModelManager.java
``` java
    @Override
    public synchronized void deleteEvent(ReadOnlyEvent event) throws EventNotFoundException {
        addressBook.deleteEvent(event);
        updateFilteredEventList(PREDICATE_SHOW_ALL_EVENTS);
        indicateAddressBookChanged();
    }

    @Override
    public void favouritePerson(ReadOnlyPerson target) throws PersonNotFoundException {
        addressBook.favouritePerson(target);
        indicateAddressBookChanged();
    }

    @Override
    public void unfavouritePerson(ReadOnlyPerson target) throws PersonNotFoundException {
        addressBook.unfavouritePerson(target);
        updateFilteredPersonList(new NameContainsFavouritePredicate());
        indicateAddressBookChanged();
    }

    @Override
    public ArrayList<String> getThemesList() {
        return this.addressBook.getThemesList();
    }

    @Override
    public void setCurrentTheme(String theme) {
        currentTheme = theme;
    }

    @Override
    public String getCurrentTheme() {
        return currentTheme;
    }
```
###### \java\seedu\address\model\person\NameContainsFavouritePredicate.java
``` java

import java.util.function.Predicate;

/**
 * Tests that a {@code ReadOnlyPerson}'s {@code Name} are favourited.
 */
public class NameContainsFavouritePredicate implements Predicate<ReadOnlyPerson> {

    public NameContainsFavouritePredicate() {
    }

    @Override
    public boolean test(ReadOnlyPerson person) {
        return person.getFavourite();
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof NameContainsFavouritePredicate); // instanceof handles nulls
    }
}
```
###### \java\seedu\address\model\person\Person.java
``` java
    public void setFavourite(Boolean favourite) {
        this.favourite.set(requireNonNull(favourite));
    }

    @Override
    public ObjectProperty<Boolean> favouriteProperty() {
        return favourite;
    }

    @Override
    public Boolean getFavourite() {
        return favourite.get();
    }
```
###### \java\seedu\address\model\person\TagContainsKeywordsPredicate.java
``` java

import java.util.List;
import java.util.function.Predicate;

import seedu.address.commons.util.StringUtil;

/**
 * Tests that a {@code ReadOnlyPerson}'s {@code Name} matches any of the keywords given.
 */
public class TagContainsKeywordsPredicate implements Predicate<ReadOnlyPerson> {

    private final List<String> keywords;

    public TagContainsKeywordsPredicate(List<String> keywords) {
        this.keywords = keywords;
    }

    @Override
    public boolean test(ReadOnlyPerson person) {
        return (keywords.stream().anyMatch(keyword -> StringUtil.containsWordIgnoreCase
                (person.getTagsText(), keyword)));
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof TagContainsKeywordsPredicate // instanceof handles nulls
                && this.keywords.equals(((TagContainsKeywordsPredicate) other).keywords)); // state check
    }
}
```
###### \java\seedu\address\model\person\UniquePersonList.java
``` java
    /**
     * Favourites the equivalent person in the list.
     *
     * @throws PersonNotFoundException if no such person could be found in the list.
     */
    public void favouritePerson(ReadOnlyPerson toFavourite) throws PersonNotFoundException {
        requireNonNull(toFavourite);
        int index = internalList.indexOf(toFavourite);
        if (index == -1) {
            throw new PersonNotFoundException();
        }

        internalList.get(index).setFavourite(true);
    }

    /**
     * Unfavourites the equivalent person from the list.
     *
     * @throws PersonNotFoundException if no such person could be found in the list.
     */
    public void unfavouritePerson(ReadOnlyPerson toUnfavourite) throws PersonNotFoundException {
        requireNonNull(toUnfavourite);
        int index = internalList.indexOf(toUnfavourite);
        if (index == -1) {
            throw new PersonNotFoundException();
        }

        internalList.get(index).setFavourite(false);
    }
```
###### \java\seedu\address\model\UserPrefs.java
``` java
    public String getTheme() {
        return theme == null ? "/view/DarkTheme.css" : theme;
    }

    public void setTheme(String theme) {
        this.theme = theme;
    }

    public void updateLastUsedTheme(String theme) {
        this.theme = theme;
    }
```
###### \java\seedu\address\ui\BrowserPanel.java
``` java
    /**
     * Sets the default HTML file based on the current theme.
     */
    public void setDefaultPage(String currentTheme) {
        URL defaultPage;
        if (currentTheme.contains(DARK_THEME_PAGE)) {
            defaultPage = MainApp.class.getResource(FXML_FILE_FOLDER + DEFAULT_PAGE);
        } else {
            defaultPage = MainApp.class.getResource(FXML_FILE_FOLDER + DEFAULT_LIGHT_PAGE);
        }
        loadPage(defaultPage.toExternalForm());
    }
```
###### \java\seedu\address\ui\CommandBox.java
``` java
    /**
     * Sets the command box style to indicate a succeeded command.
     */
    private void setStyleToIndicateCommandSuccess() {
        ObservableList<String> styleClass = commandTextField.getStyleClass();

        if (styleClass.contains(SUCCESS_STYLE_CLASS)) {
            return;
        }

        styleClass.add(SUCCESS_STYLE_CLASS);
    }
```
###### \java\seedu\address\ui\EventCard.java
``` java

import javafx.beans.binding.Bindings;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import seedu.address.model.event.ReadOnlyEvent;

/**
 * An UI component that displays information of a {@code Event}.
 */
public class EventCard extends UiPart<Region> {

    private static final String FXML = "EventListCard.fxml";

    /**
     * Note: Certain keywords such as "location" and "resources" are reserved keywords in JavaFX.
     * As a consequence, UI elements' variable names cannot be set to such keywords
     * or an exception will be thrown by JavaFX during runtime.
     *
     * @see <a href="https://github.com/se-edu/addressbook-level4/issues/336">The issue on AddressBook level 4</a>
     */

    public final ReadOnlyEvent event;

    @javafx.fxml.FXML
    private HBox eventCardPane;
    @FXML
    private Label name;
    @FXML
    private Label id;
    @FXML
    private Label date;
    @FXML
    private Label address;

    public EventCard(ReadOnlyEvent event, int displayedIndex) {
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
        if (!(other instanceof EventCard)) {
            return false;
        }

        // state check
        EventCard card = (EventCard) other;
        return id.getText().equals(card.id.getText())
                && event.equals(card.event);
    }
}
```
###### \java\seedu\address\ui\EventListPanel.java
``` java

import java.util.logging.Logger;

import org.fxmisc.easybind.EasyBind;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.Region;
import seedu.address.commons.core.LogsCenter;
import seedu.address.commons.events.ui.EventPanelSelectionChangedEvent;
import seedu.address.model.event.ReadOnlyEvent;

/**
 * Panel containing the list of events.
 */
public class EventListPanel extends UiPart<Region> {
    private static final String FXML = "EventListPanel.fxml";
    private final Logger logger = LogsCenter.getLogger(EventListPanel.class);

    @FXML
    private ListView<EventCard> eventListView;

    public EventListPanel(ObservableList<ReadOnlyEvent> eventList) {
        super(FXML);
        setConnections(eventList);
        registerAsAnEventHandler(this);
    }

    private void setConnections(ObservableList<ReadOnlyEvent> eventList) {
        ObservableList<EventCard> mappedList = EasyBind.map(
                eventList, (event) -> new EventCard(event, eventList.indexOf(event) + 1));
        eventListView.setItems(mappedList);
        eventListView.setCellFactory(listView -> new EventListViewCell());
        setEventHandlerForSelectionChangeEvent();
    }

    private void setEventHandlerForSelectionChangeEvent() {
        eventListView.getSelectionModel().selectedItemProperty()
                .addListener((observable, oldValue, newValue) -> {
                    if (newValue != null) {
                        logger.fine("Selection in event list panel changed to : '" + newValue + "'");
                        raise(new EventPanelSelectionChangedEvent(newValue));
                    }
                });
    }

    /**
     * Custom {@code ListCell} that displays the graphics of a {@code EventCard}.
     */
    class EventListViewCell extends ListCell<EventCard> {

        @Override
        protected void updateItem(EventCard event, boolean empty) {
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
###### \java\seedu\address\ui\MainWindow.java
``` java
    /**
     * Sets the default theme based on user preferences.
     */
    private void setWindowDefaultTheme(UserPrefs prefs) {
        getRoot().getStylesheets().add(prefs.getTheme());
    }

    /**
     * Returns the current theme of the main Window.
     */
    String getCurrentTheme() {
        return getRoot().getStylesheets().get(CURRENT_THEME_INDEX);
    }
```
###### \java\seedu\address\ui\MainWindow.java
``` java
    /**
     * Opens the theme window.
     */
    @FXML
    public void handleThemes() {
        ThemesWindow themesWindow = new ThemesWindow();
        themesWindow.show();
    }

    /**
     * Changes the theme based on the input theme.
     */
    public void handleChangeTheme(String theme) {
        if (getRoot().getStylesheets().size() > 1) {
            getRoot().getStylesheets().remove(CURRENT_THEME_INDEX);
        }
        getRoot().getStylesheets().add(VIEW_PATH + theme);
    }
```
###### \java\seedu\address\ui\MainWindow.java
``` java
    @Subscribe
    private void handleShowThemesEvent(ShowThemeRequestEvent event) {
        logger.info(LogsCenter.getEventHandlingLogMessage(event));
        handleThemes();
    }

    @Subscribe
    private void handleChangeThemeEvent(ChangeThemeRequestEvent event) {
        logger.info(LogsCenter.getEventHandlingLogMessage(event));
        handleChangeTheme(event.theme);
        browserPanel.setDefaultPage(event.theme);
        logic.setCurrentTheme(getCurrentTheme());
    }
```
###### \java\seedu\address\ui\PersonCard.java
``` java
    /**
     * Initialises the tags with a randomised color.
     *
     * @param person
     */
    private void initTags(ReadOnlyPerson person) {
        person.getTags().forEach(tag -> {
            Label label = new Label(tag.tagName);
            label.setStyle("-fx-background-color: " + getColorForTag(tag.tagName));
            tags.getChildren().add(label);
        });
    }

    /**
     * Randomises a color name into a HashMap of color names.
     *
     * @param tag
     * @return name of the color
     */
    private static String getColorForTag(String tag) {
        if (!tagColors.containsKey(tag)) {
            tagColors.put(tag, colors[random.nextInt(colors.length)]);
        }

        return tagColors.get(tag);
    }

    /**
     * Initialises the person with a favourite image if he/she is favourited.
     *
     * @param person
     */
    private void initFavourite(ReadOnlyPerson person) {
        if (person.isFavourite()) {
            favouriteImage.setVisible(true);
        } else {
            favouriteImage.setVisible(false);
        }
    }
```
###### \java\seedu\address\ui\ResultDisplay.java
``` java
    @Subscribe
    private void handleNewResultAvailableEvent(NewResultAvailableEvent event) {
        logger.info(LogsCenter.getEventHandlingLogMessage(event));
        if (isCommandFailure(event.message)) {
            setStyleToIndicateCommandFailure();
        } else {
            setStyleToDefault();
        }
        Platform.runLater(() -> displayed.setValue(event.message));
    }

    /**
     * Sets the result display style to use the default style.
     */
    private void setStyleToDefault() {
        resultDisplay.getStyleClass().remove(ERROR_STYLE_CLASS);
    }

    /**
     * Sets the result display style to indicate a failed command.
     */
    private void setStyleToIndicateCommandFailure() {
        ObservableList<String> styleClass = resultDisplay.getStyleClass();

        if (styleClass.contains(ERROR_STYLE_CLASS)) {
            return;
        }

        styleClass.add(ERROR_STYLE_CLASS);
    }
```
###### \java\seedu\address\ui\ThemesWindow.java
``` java

import java.util.logging.Logger;

import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.layout.Region;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import seedu.address.commons.core.LogsCenter;
import seedu.address.commons.util.FxViewUtil;

/**
 * Controller for a theme page
 */
public class ThemesWindow extends UiPart<Region> {

    public static final String THEMES_FILE_PATH = "/docs/Themes.html";

    private static final Logger logger = LogsCenter.getLogger(ThemesWindow.class);
    private static final String ICON = "/images/help_icon.png";
    private static final String FXML = "ThemesWindow.fxml";
    private static final String TITLE = "Themes";

    @FXML
    private WebView browser;

    private final Stage dialogStage;

    public ThemesWindow() {
        super(FXML);
        Scene scene = new Scene(getRoot());
        //Null passed as the parent stage to make it non-modal.
        dialogStage = createDialogStage(TITLE, null, scene);
        dialogStage.setMaximized(true); //TODO: set a more appropriate initial size
        FxViewUtil.setStageIcon(dialogStage, ICON);

        String userGuideUrl = getClass().getResource(THEMES_FILE_PATH).toString();
        browser.getEngine().load(userGuideUrl);
    }

    /**
     * Shows the themes window.
     * @throws IllegalStateException
     * <ul>
     *     <li>
     *         if this method is called on a thread other than the JavaFX Application Thread.
     *     </li>
     *     <li>
     *         if this method is called during animation or layout processing.
     *     </li>
     *     <li>
     *         if this method is called on the primary stage.
     *     </li>
     *     <li>
     *         if {@code dialogStage} is already showing.
     *     </li>
     * </ul>
     */
    public void show() {
        logger.fine("Showing themes page for the application.");
        dialogStage.showAndWait();
    }
}
```
###### \java\seedu\address\ui\UiManager.java
``` java
    /**
     * Sets the given theme as the main theme of the main window.
     * @param theme .g. {@code "DarkTheme.css"}
     */
    public void setTheme(String theme) {
        mainWindow.getRoot().getStylesheets().add("/view/" + theme);
    }
```
###### \resources\view\BrightTheme.css
``` css
.background {
    -fx-background-color: derive(#f7f7f7, 20%);
    background-color: #f7f7f7; /* Used in the default.html file */
}

.label {
    -fx-font-size: 11pt;
    -fx-font-family: "Segoe UI Semibold";
    -fx-text-fill: #555555;
    -fx-opacity: 0.9;
}

.label-bright {
    -fx-font-size: 11pt;
    -fx-font-family: "Segoe UI Semibold";
    -fx-text-fill: white;
    -fx-opacity: 1;
}

.label-header {
    -fx-font-size: 32pt;
    -fx-font-family: "Segoe UI Light";
    -fx-text-fill: white;
    -fx-opacity: 1;
}

.text-field {
    -fx-font-size: 12pt;
    -fx-font-family: "Segoe UI Semibold";
}

.tab-pane {
    -fx-padding: 0 0 0 1;
}

.tab-pane .tab-header-area {
    -fx-padding: 0 0 0 0;
    -fx-min-height: 0;
    -fx-max-height: 0;
}

.table-view {
    -fx-base: #f7f7f7;
    -fx-control-inner-background: #f7f7f7;
    -fx-background-color: #f7f7f7;
    -fx-table-cell-border-color: transparent;
    -fx-table-header-border-color: transparent;
    -fx-padding: 5;
}

.table-view .column-header-background {
    -fx-background-color: transparent;
}

.table-view .column-header, .table-view .filler {
    -fx-size: 35;
    -fx-border-width: 0 0 1 0;
    -fx-background-color: transparent;
    -fx-border-color:
        transparent
        transparent
        derive(-fx-base, 80%)
        transparent;
    -fx-border-insets: 0 10 1 0;
}

.table-view .column-header .label {
    -fx-font-size: 20pt;
    -fx-font-family: "Segoe UI Light";
    -fx-text-fill: white;
    -fx-alignment: center-left;
    -fx-opacity: 1;
}

.table-view:focused .table-row-cell:filled:focused:selected {
    -fx-background-color: -fx-focus-color;
}

.split-pane:horizontal .split-pane-divider {
    -fx-background-color: derive(#f7f7f7, 20%);
}

.split-pane:vertical .split-pane-divider {
    -fx-background-color: derive(#f7f7f7, 20%);
}

.split-pane {
    -fx-border-radius: 1;
    -fx-border-width: 1;
    -fx-background-color: derive(#f7f7f7, 20%);
}

.list-view {
    -fx-background-color: #dfe3ee;
    -fx-background-insets: 0;
    -fx-padding: 0;
}

.list-cell {
    -fx-label-padding: 0 0 0 0;
    -fx-graphic-text-gap : 0;
    -fx-padding: 0 0 0 0;
    -fx-background-color: transparent;
}

.list-cell:filled {
    -fx-background-color: transparent;
}

.list-cell:hover {
    -fx-background-color: derive(#8b9dc3, 50%);
}

.list-cell:filled:selected {
    -fx-background-color: derive(#8b9dc3, 50%);
}

.list-cell:filled:selected #cardPane {
    -fx-border-color: transparent;
}

.list-cell .label {
    -fx-text-fill: black;
}

.cell_big_label {
    -fx-font-family: "Segoe UI Semibold";
    -fx-font-size: 16px;
    -fx-text-fill: #010504;
}

.cell_small_label {
    -fx-font-family: "Segoe UI";
    -fx-font-size: 13px;
    -fx-text-fill: #010504;
}
.window_big_label {
    -fx-font-family: "Segoe UI Semibold";
    -fx-font-size: 32px;
    -fx-text-fill: #010504;
}

.window_small_label {
    -fx-font-family: "Segoe UI";
    -fx-font-size: 15px;
    -fx-text-fill: #010504;
}

.window_small_text {
    -fx-font-family: "Segoe UI";
    -fx-font-size: 15px;
    -fx-fill: derive(#1d1d1d, 40%);
}

.anchor-pane {
     -fx-background-color: derive(#f7f7f7, 20%);
}

.pane-with-border {
     -fx-background-color: derive(#f7f7f7, 20%);
     -fx-border-color: transparent;
}

.status-bar {
    -fx-background-color: derive(#f7f7f7, 20%);
    -fx-text-fill: black;
}

.result-display {
    -fx-background-color: transparent;
    -fx-font-family: "Segoe UI Light";
    -fx-font-size: 14pt;
    -fx-text-fill: black;
}

.result-display .label {
    -fx-text-fill: black !important;
}

.status-bar .label {
    -fx-font-family: "Segoe UI Light";
    -fx-text-fill: black;
}

.status-bar-with-border {
    -fx-background-color: derive(#f7f7f7, 30%);
    -fx-border-color: derive(#8b9dc3, 25%);
    -fx-border-width: 1px;
}

.status-bar-with-border .label {
    -fx-text-fill: black;
}

.grid-pane {
    -fx-background-color: derive(#f7f7f7, 30%);
    -fx-border-color: derive(#8b9dc3, 30%);
    -fx-border-width: 1px;
}

.grid-pane .anchor-pane {
    -fx-background-color: derive(#f7f7f7, 30%);
}

.calendar-color {
    -fx-border-color: derive(#1d1d1d, 20%);
    -fx-fill: derive(#1d1d1d, 20%);
    -fx-text-fill: derive(#1d1d1d, 20%);
}

.context-menu {
    -fx-background-color: derive(#f7f7f7, 50%);
}

.context-menu .label {
    -fx-text-fill: black;
}

.menu-bar {
    -fx-background-color: derive(#f7f7f7, 20%);
    -fx-border-color: #8b9dc3;
}

.menu-bar .label {
    -fx-font-size: 14pt;
    -fx-font-family: "Segoe UI Light";
    -fx-text-fill: black;
    -fx-opacity: 0.9;
}

.menu .left-container {
    -fx-background-color: black;
}

.button {
    -fx-padding: 5 15 5 15;
    -fx-border-color: transparent;
    -fx-border-width: 1;
    -fx-background-color: #f7f7f7;
    -fx-font-family: "Segoe UI", Helvetica, Arial, sans-serif;
    -fx-font-size: 11pt;
    -fx-text-fill: black;
    -fx-background-insets: 0 0 0 0, 0, 1, 2;
}

.button:hover {
    -fx-background-color: #dfe3ee;
}

.button:pressed, .button:default:hover:pressed {
    -fx-background-color: transparent;
    -fx-text-fill: #3b5998;
}

.button:focused {
    -fx-border-color: transparent;
    -fx-border-width: 1, 1;
    -fx-border-style: solid, segments(1, 1);
    -fx-border-insets: 1 1 1 1, 0;
}

.button:disabled, .button:default:disabled {
    -fx-opacity: 0.4;
    -fx-background-color: #f7f7f7;
    -fx-text-fill: black;
}

.button:default {
    -fx-background-color: -fx-focus-color;
    -fx-text-fill: black;
}

.button:default:hover {
    -fx-background-color: derive(-fx-focus-color, 30%);
}

.dialog-pane {
    -fx-background-color: #f7f7f7;
}

.dialog-pane > *.button-bar > *.container {
    -fx-background-color: #f7f7f7;
}

.dialog-pane > *.label.content {
    -fx-font-size: 14px;
    -fx-font-weight: bold;
    -fx-text-fill: black;
}

.dialog-pane:header *.header-panel {
    -fx-background-color: derive(#f7f7f7, 25%);
}

.dialog-pane:header *.header-panel *.label {
    -fx-font-size: 18px;
    -fx-font-style: italic;
    -fx-fill: white;
    -fx-text-fill: black;
}

.scroll-bar {
    -fx-background-color: #dfe3ee;
}

.scroll-bar .thumb {
    -fx-background-color: derive(#8b9dc3, 50%);
    -fx-background-insets: 3;
}

.scroll-bar .increment-button, .scroll-bar .decrement-button {
    -fx-background-color: transparent;
    -fx-padding: 0 0 0 0;
}

.scroll-bar .increment-arrow, .scroll-bar .decrement-arrow {
    -fx-shape: " ";
}

.scroll-bar:vertical .increment-arrow, .scroll-bar:vertical .decrement-arrow {
    -fx-padding: 1 8 1 8;
}

.scroll-bar:horizontal .increment-arrow, .scroll-bar:horizontal .decrement-arrow {
    -fx-padding: 8 1 8 1;
}

#browserPlaceholder {
    -fx-border-color: #8b9dc3;
}

#calendarPanel {
    -fx-border-color: #8b9dc3;
}

#cardPane {
    -fx-background-color: transparent;
    -fx-border-width: 0;
}

#commandBoxPlaceholder {
    -fx-background-color: #f7f7f7;
}

#commandTypeLabel {
    -fx-font-size: 11px;
    -fx-text-fill: #F70D1A;
}

#commandTextField {
    -fx-background-color: #f7f7f7;
    -fx-background-insets: 0;
    -fx-border-color: #8b9dc3;
    -fx-border-insets: 0;
    -fx-border-width: 1;
    -fx-font-family: "Segoe UI Light";
    -fx-font-size: 13pt;
    -fx-text-fill: black;
}

#commandTextField:focused {
    -fx-border-color: blue;
    -fx-border-width: 1;
}

#commandTextField.error {
    -fx-border-color: #ff2727;
    -fx-border-width: 1;
}

#commandTextField.success {
    -fx-border-color: limegreen;
    -fx-border-width: 1;
}

#detailsPanelPlaceholder {
    -fx-border-color: #8b9dc3;
}

#eventListPanel {
    -fx-effect: innershadow(gaussian, black, 10, 0, 0, 0);
}

#eventListPanelPlaceholder {
    -fx-border-color: #8b9dc3;
}

#filterField, #personListPanel, #personWebpage {
    -fx-effect: innershadow(gaussian, black, 10, 0, 0, 0);
}

#personListPanelPlaceholder {
    -fx-border-color: #8b9dc3;
}

#resultDisplay .content {
    -fx-background-color: #f7f7f7;
    -fx-background-radius: 0;
}

#resultDisplay {
    -fx-border-color: #8b9dc3;
}

#resultDisplayPlaceholder {
    -fx-background-color: #f7f7f7;
}

#tags {
    -fx-hgap: 7;
    -fx-vgap: 3;
}

#tags .label {
    -fx-text-fill: white;
    -fx-background-color: #3e7b91;
    -fx-padding: 1 3 1 3;
    -fx-border-radius: 2;
    -fx-background-radius: 2;
    -fx-font-size: 11;
}
```
###### \resources\view\DarkTheme.css
``` css
.background {
    -fx-background-color: #1d1d1d;
    background-color: #1d1d1d; /* Used in the default.html file */
}
```
###### \resources\view\DarkTheme.css
``` css
.table-view {
    -fx-base: #1d1d1d;
    -fx-control-inner-background: #1d1d1d;
    -fx-background-color: #1d1d1d;
    -fx-table-cell-border-color: transparent;
    -fx-table-header-border-color: transparent;
    -fx-padding: 5;
}

.table-view .column-header-background {
    -fx-background-color: transparent;
}

.table-view .column-header, .table-view .filler {
    -fx-size: 35;
    -fx-border-width: 0 0 1 0;
    -fx-background-color: transparent;
    -fx-border-color:
        transparent
        transparent
        derive(-fx-base, 80%)
        transparent;
    -fx-border-insets: 0 10 1 0;
}

.table-view .column-header .label {
    -fx-font-size: 20pt;
    -fx-font-family: "Segoe UI Light";
    -fx-text-fill: white;
    -fx-alignment: center-left;
    -fx-opacity: 1;
}

.table-view:focused .table-row-cell:filled:focused:selected {
    -fx-background-color: -fx-focus-color;
}

.split-pane:horizontal .split-pane-divider {
    -fx-background-color: #1d1d1d;
}

.split-pane:vertical .split-pane-divider {
    -fx-background-color: #1d1d1d;
}

.split-pane {
    -fx-border-radius: 1;
    -fx-border-width: 1;
    -fx-background-color: #1d1d1d;
}

.list-view {
    -fx-background-color: derive(#1d1d1d, 10%);
    -fx-background-insets: 0;
    -fx-padding: 0;
}

.list-cell {
    -fx-label-padding: 0 0 0 0;
    -fx-graphic-text-gap : 0;
    -fx-padding: 0 0 0 0;
    -fx-background-color: transparent;
}

.list-cell:filled {
    -fx-background-color: transparent;
}

.list-cell:hover {
    -fx-background-color: derive(#1d1d1d, 30%);
}

.list-cell:filled:selected {
    -fx-background-color: derive(#1d1d1d, 30%);
}

.list-cell:filled:selected #cardPane {
    -fx-border-color: transparent;
}

.list-cell .label {
    -fx-text-fill: white;
}
```
###### \resources\view\DarkTheme.css
``` css
.anchor-pane {
    -fx-background-color: #1d1d1d;
}

.pane-with-border {
    -fx-background-color: #1d1d1d;
    -fx-border-color: transparent;
    -fx-border-top-width: 1px;
}

.status-bar {
    -fx-background-color: #1d1d1d;
    -fx-text-fill: black;
}

.result-display {
    -fx-background-color: transparent;
    -fx-font-family: "Segoe UI Light";
    -fx-font-size: 14pt;
    -fx-text-fill: white;
}

.result-display .label {
    -fx-text-fill: black !important;
}

.status-bar .label {
    -fx-font-family: "Segoe UI Light";
    -fx-text-fill: white;
}

.status-bar-with-border {
    -fx-background-color: derive(#1d1d1d, 20%);
    -fx-border-color: derive(#1d1d1d, 15%);
    -fx-border-width: 1px;
}

.status-bar-with-border .label {
    -fx-text-fill: white;
}

.grid-pane {
    -fx-background-color: derive(#1d1d1d, 30%);
    -fx-border-color: derive(#1d1d1d, 30%);
    -fx-border-width: 1px;
}

.grid-pane .anchor-pane {
    -fx-background-color: derive(#1d1d1d, 30%);
}

.calendar-color {
    -fx-border-color: derive(#1d1d1d, 60%);
    -fx-fill: derive(#1d1d1d, 60%);
    -fx-background-color: transparent;
    -fx-text-fill: derive(#1d1d1d, 60%);
}

.context-menu {
    -fx-background-color: derive(#1d1d1d, 50%);
}

.context-menu .label {
    -fx-text-fill: white;
}

.menu-bar {
    -fx-background-color: #1d1d1d;
    -fx-border-color: derive(#1d1d1d, 30%);
}

.menu-bar .label {
    -fx-font-size: 14pt;
    -fx-font-family: "Segoe UI Light";
    -fx-text-fill: white;
    -fx-opacity: 0.9;
}

.menu .left-container {
    -fx-background-color: black;
}

.button {
    -fx-padding: 5 15 5 15;
    -fx-border-color: transparent;
    -fx-border-width: 1;
    -fx-background-radius: 0;
    -fx-background-color: transparent;
    -fx-font-family: "Segoe UI", Helvetica, Arial, sans-serif;
    -fx-font-size: 13pt;
    -fx-text-fill: derive(#1d1d1d, 60%);
    -fx-background-insets: 0 0 0 0, 0, 1, 2;
}

.button:hover {
    -fx-background-color: derive(#1d1d1d, 30%);
}

.button:pressed, .button:default:hover:pressed {
    -fx-background-color: derive(#1d1d1d, 30%);
    -fx-text-fill: #1d1d1d;
}

.button:focused {
    -fx-border-color: transparent;
    -fx-border-width: 1;
    -fx-border-style: solid, segments(1, 1);
    -fx-border-radius: 0, 0;
    -fx-border-insets: 1 1 1 1, 0;
}

.button:disabled, .button:default:disabled {
    -fx-opacity: 0.4;
    -fx-background-color: transparent;
    -fx-text-fill: white;
}

.button:default {
    -fx-background-color: -fx-focus-color;
    -fx-text-fill: #ffffff;
}

.button:default:hover {
    -fx-background-color: derive(-fx-focus-color, 30%);
}

.dialog-pane {
    -fx-background-color: #1d1d1d;
}

.dialog-pane > *.button-bar > *.container {
    -fx-background-color: #1d1d1d;
}

.dialog-pane > *.label.content {
    -fx-font-size: 14px;
    -fx-font-weight: bold;
    -fx-text-fill: white;
}

.dialog-pane:header *.header-panel {
    -fx-background-color: derive(#1d1d1d, 25%);
}

.dialog-pane:header *.header-panel *.label {
    -fx-font-size: 18px;
    -fx-font-style: italic;
    -fx-fill: white;
    -fx-text-fill: white;
}

.scroll-bar {
    -fx-background-color: transparent;
}

.scroll-bar .thumb {
    -fx-background-color: derive(#1d1d1d, 30%);
    -fx-background-insets: 3;
}

.scroll-bar .increment-button, .scroll-bar .decrement-button {
    -fx-background-color: transparent;
    -fx-padding: 0 0 0 0;
}

.scroll-bar .increment-arrow, .scroll-bar .decrement-arrow {
    -fx-shape: " ";
}

.scroll-bar:vertical .increment-arrow, .scroll-bar:vertical .decrement-arrow {
    -fx-padding: 1 8 1 8;
}

.scroll-bar:horizontal .increment-arrow, .scroll-bar:horizontal .decrement-arrow {
    -fx-padding: 8 1 8 1;
}

#browserPlaceholder {
    -fx-border-color: derive(#1d1d1d, 30%);
}

#calendarPanel {
    -fx-border-color: derive(#1d1d1d, 30%);
}

#cardPane {
    -fx-background-color: transparent;
    -fx-border-width: 0;
}

#commandBoxPlaceholder {
    -fx-background-color: #1d1d1d;
}

#commandTypeLabel {
    -fx-font-size: 11px;
    -fx-text-fill: #F70D1A;
}

#commandTextField {
    -fx-background-color: #1d1d1d;
    -fx-background-insets: 0;
    -fx-border-color: derive(#1d1d1d, 30%);
    -fx-border-insets: 0;
    -fx-border-width: 1;
    -fx-font-family: "Segoe UI Light";
    -fx-font-size: 13pt;
    -fx-text-fill: white;
}

#commandTextField:focused {
    -fx-border-color: #006DD0;
    -fx-border-width: 1;
}

#commandTextField.error {
    -fx-border-color: #ff2727;
    -fx-border-width: 1;
}

#commandTextField.success {
    -fx-border-color: green;
    -fx-border-width: 1;
}

#detailsPanelPlaceholder {
    -fx-border-color: derive(#1d1d1d, 30%);
}

#eventListPanel {
    -fx-effect: innershadow(gaussian, black, 10, 0, 0, 0);
}

#eventListPanelPlaceholder {
    -fx-border-color: derive(#1d1d1d, 30%);
}

#filterField, #personListPanel, #personWebpage {
    -fx-effect: innershadow(gaussian, black, 10, 0, 0, 0);
}

#personListPanelPlaceholder {
    -fx-border-color: derive(#1d1d1d, 30%);
}

#resultDisplay .content {
    -fx-background-color: #1d1d1d;
    -fx-background-radius: 0;
}

#resultDisplay {
    -fx-border-color: derive(#1d1d1d, 30%);
}

#resultDisplayPlaceholder {
    -fx-background-color: #1d1d1d;
}
```
###### \resources\view\defaultLight.html
``` html
<html>
<head>
    <link rel="stylesheet" href="BrightTheme.css">
</head>

<body class="background">
</body>
</html>
```
###### \resources\view\EventListCard.fxml
``` fxml

<?import javafx.geometry.Insets?>
<?import javafx.scene.control.Label?>
<?import javafx.scene.layout.ColumnConstraints?>
<?import javafx.scene.layout.GridPane?>
<?import javafx.scene.layout.HBox?>
<?import javafx.scene.layout.Region?>
<?import javafx.scene.layout.VBox?>

<HBox id="eventCardPane" fx:id="eventCardPane" xmlns="http://javafx.com/javafx/8" xmlns:fx="http://javafx.com/fxml/1">
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
###### \resources\view\EventListPanel.fxml
``` fxml

<?import javafx.scene.control.ListView?>
<?import javafx.scene.layout.VBox?>

<VBox xmlns="http://javafx.com/javafx/8.0.111" xmlns:fx="http://javafx.com/fxml/1">
    <ListView fx:id="eventListView" VBox.vgrow="ALWAYS" />
</VBox>
```
###### \resources\view\MainWindow.fxml
``` fxml
      <StackPane fx:id="personAndEventListPlaceholder" maxWidth="270.0" minWidth="270.0" prefWidth="270.0">
         <children>
            <VBox fx:id="personList" minWidth="270" prefWidth="340">
               <padding>
                  <Insets bottom="10" left="10" right="10" top="10" />
               </padding>
               <children>
                  <StackPane fx:id="personListPanelPlaceholder" VBox.vgrow="ALWAYS" />
               </children>
            </VBox>
            <VBox fx:id="eventList" minWidth="270.0" prefWidth="340.0">
               <children>
                  <StackPane fx:id="eventListPanelPlaceholder" VBox.vgrow="ALWAYS" />
               </children>
               <padding>
                  <Insets bottom="10.0" left="10.0" right="10.0" top="10.0" />
               </padding>
            </VBox>
         </children>
         <padding>
            <Insets bottom="10.0" left="10.0" right="10.0" top="10.0" />
         </padding>
      </StackPane>
```
###### \resources\view\ThemesWindow.fxml
``` fxml

<?import javafx.scene.layout.StackPane?>
<?import javafx.scene.web.WebView?>

<StackPane fx:id="themesWindowRoot" xmlns="http://javafx.com/javafx/8.0.111" xmlns:fx="http://javafx.com/fxml/1">
  <WebView fx:id="browser" />
</StackPane>
```

package seedu.address.testutil;

import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.model.event.Event;
import seedu.address.model.event.Date;
import seedu.address.model.event.ReadOnlyEvent;
import seedu.address.model.person.Address;
import seedu.address.model.person.Name;


public class EventBuilder {
    public static final String DEFAULT_NAME = "ZoukOut";
    public static final String DEFAULT_ADDRESS = "Sentosa, Siloso Beach";
    public static final String DEFAULT_DATE = "12/12/2018";

    private Event event;

    public EventBuilder(){
        try{
            Name defaultName = new Name(DEFAULT_NAME);
            Date defaultDate = new Date(DEFAULT_DATE);
            Address defaultAddress = new Address(DEFAULT_ADDRESS);

            this.event = new Event(defaultName, defaultDate, defaultAddress);

        } catch (IllegalValueException ive) {
            throw new AssertionError("Default event's values are invalid.");
        }
    }

    /**
     * Initializes the EventBuilder with the data of {@code EventToCopy}.
     */
    public EventBuilder(ReadOnlyEvent eventToCopy) {
        this.event = new Event(eventToCopy);
    }

    /**
     * Sets the {@code Name} of the {@code Event} that we are building.
     */
    public EventBuilder withName(String name) {
        try {
            this.event.setName(new Name(name));
        } catch (IllegalValueException ive) {
            throw new IllegalArgumentException("name is expected to be unique.");
        }
        return this;
    }

    /**
     * Sets the {@code Address} of the {@code Event} that we are building.
     */
    public EventBuilder withAddress(String address) {
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
    public EventBuilder withDate(String date) {
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

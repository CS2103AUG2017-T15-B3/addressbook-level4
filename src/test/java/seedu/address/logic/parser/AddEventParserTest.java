package seedu.address.logic.parser;

import static seedu.address.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.commands.CommandTestUtil.EVENT_ADDRESS_A_DESC;
import static seedu.address.logic.commands.CommandTestUtil.EVENT_ADDRESS_B_DESC;
import static seedu.address.logic.commands.CommandTestUtil.EVENT_DATE_A_DESC;
import static seedu.address.logic.commands.CommandTestUtil.EVENT_DATE_B_DESC;
import static seedu.address.logic.commands.CommandTestUtil.EVENT_NAME_A_DESC;
import static seedu.address.logic.commands.CommandTestUtil.EVENT_NAME_B_DESC;
import static seedu.address.logic.commands.CommandTestUtil.INVALID_ADDRESS_DESC;
import static seedu.address.logic.commands.CommandTestUtil.INVALID_DATE_DESC;
import static seedu.address.logic.commands.CommandTestUtil.INVALID_NAME_DESC;
import static seedu.address.logic.commands.CommandTestUtil.VALID_EVENT_B_ADDRESS;
import static seedu.address.logic.commands.CommandTestUtil.VALID_EVENT_B_DATE;
import static seedu.address.logic.commands.CommandTestUtil.VALID_EVENT_B_NAME;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;

import org.junit.Test;

import seedu.address.logic.commands.AddEventCommand;
import seedu.address.model.event.Date;
import seedu.address.model.event.Event;
import seedu.address.model.person.Address;
import seedu.address.model.person.Name;
import seedu.address.testutil.EventBuilder;

public class AddEventParserTest {
    private AddEventCommandParser parser = new AddEventCommandParser();

    @Test
    public void parse_compulsoryFieldMissing_failure() {
        String expectedMessage = String.format(MESSAGE_INVALID_COMMAND_FORMAT, AddEventCommand.MESSAGE_USAGE);

        // missing name prefix
        assertParseFailure(parser, AddEventCommand.COMMAND_WORD + VALID_EVENT_B_NAME
                + EVENT_DATE_B_DESC + EVENT_ADDRESS_B_DESC, expectedMessage);

        // missing date prefix
        assertParseFailure(parser, AddEventCommand.COMMAND_WORD + EVENT_NAME_B_DESC
                + VALID_EVENT_B_DATE + EVENT_ADDRESS_B_DESC, expectedMessage);

        // missing address prefix
        assertParseFailure(parser, AddEventCommand.COMMAND_WORD + EVENT_NAME_B_DESC
                + EVENT_DATE_B_DESC + VALID_EVENT_B_ADDRESS, expectedMessage);

        // all prefixes missing
        assertParseFailure(parser, AddEventCommand.COMMAND_WORD + VALID_EVENT_B_NAME
                + VALID_EVENT_B_DATE + VALID_EVENT_B_ADDRESS, expectedMessage);
    }

    @Test
    public void parse_invalidValue_failure() {
        // invalid name
        assertParseFailure(parser, AddEventCommand.COMMAND_WORD + INVALID_NAME_DESC
                + EVENT_DATE_B_DESC + EVENT_ADDRESS_B_DESC, Name.MESSAGE_NAME_CONSTRAINTS);

        // invalid date
        assertParseFailure(parser, AddEventCommand.COMMAND_WORD + EVENT_NAME_B_DESC
                + INVALID_DATE_DESC + EVENT_ADDRESS_B_DESC, Date.MESSAGE_DATE_CONSTRAINTS);

        // invalid address
        assertParseFailure(parser, AddEventCommand.COMMAND_WORD + EVENT_NAME_B_DESC
                + EVENT_DATE_B_DESC + INVALID_ADDRESS_DESC, Address.MESSAGE_ADDRESS_CONSTRAINTS);
    }
}

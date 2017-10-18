package seedu.address.logic.parser;

import static seedu.address.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_PERSON;

import org.junit.Test;

import seedu.address.logic.commands.AccessCommand;

public class AccessCommandParserTest {
    private AccessCommandParser parser = new AccessCommandParser();

    @Test
    public void parse_validArgs_returnsAccessCommand() {
        assertParseSuccess(parser, "1", new AccessCommand(INDEX_FIRST_PERSON));

    }

    @Test
    public void parse_invalidArgs_throwsParseException() {
        assertParseFailure(parser, "b", String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                AccessCommand.MESSAGE_USAGE));
    }
}

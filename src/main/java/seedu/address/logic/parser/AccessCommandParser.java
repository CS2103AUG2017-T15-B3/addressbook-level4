package seedu.address.logic.parser;

import static seedu.address.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;

import seedu.address.commons.core.index.Index;
import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.logic.commands.AccessCommand;
import seedu.address.logic.parser.exceptions.ParseException;

/**
 * Parses input arguments and creates a new AccessCommand object
 */
public class AccessCommandParser implements Parser<AccessCommand> {
    /**
     * Parses the given {@code String} of arguments in the context of the AccessCommand
     * and returns an AccessCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    @Override
    public AccessCommand parse(String args) throws ParseException {
        try {
            Index index = ParserUtil.parseIndex(args);
            return new AccessCommand(index);
        } catch (IllegalValueException ive) {
            throw new ParseException(
                    String.format(MESSAGE_INVALID_COMMAND_FORMAT, AccessCommand.MESSAGE_USAGE));
        }
    }
}

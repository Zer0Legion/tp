package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_ADDRESS;
import static seedu.address.logic.parser.CliSyntax.PREFIX_EMAIL;
import static seedu.address.logic.parser.CliSyntax.PREFIX_LESS_THAN;
import static seedu.address.logic.parser.CliSyntax.PREFIX_MORE_THAN;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NAME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_PHONE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_SPECIAL;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TAG;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;

import org.junit.jupiter.api.Test;

import seedu.address.logic.commands.FindCommand;
import seedu.address.model.person.Score;

public class FindCommandParserTest {

    private FindCommandParser parser = new FindCommandParser();
    private String invalidPrefix = "z" + PREFIX_SPECIAL.toString();

    @Test
    public void parse_emptyArg_throwsParseException() {
        assertParseFailure(parser, "     ",
                           String.format(MESSAGE_INVALID_COMMAND_FORMAT, FindCommand.MESSAGE_USAGE));
    }


    @Test
    public void parse_invalidPrefix_throwsParseException() {
        // no leading and trailing whitespaces
        assertParseFailure(parser, " " + invalidPrefix + "Alice",
                           String.format(MESSAGE_INVALID_COMMAND_FORMAT, FindCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_validNameArg_returnsFindCommand() {
        // no leading and trailing whitespaces
        FindCommand expectedFindCommand =
                new FindCommand(PREFIX_NAME, "Alice");
        assertParseSuccess(parser, " " + PREFIX_NAME + "Alice", expectedFindCommand);

        // whitespace before keyword
        assertParseSuccess(parser, " " + PREFIX_NAME + " Alice", expectedFindCommand);
    }

    @Test
    public void parse_validEmailArg_returnsFindCommand() {
        // no leading and trailing whitespaces
        FindCommand expectedFindCommand =
                new FindCommand(PREFIX_EMAIL, "alice@gmail.com");
        assertParseSuccess(parser, " " + PREFIX_EMAIL + "alice@gmail.com", expectedFindCommand);

        // whitespace before keyword
        assertParseSuccess(parser, " " + PREFIX_EMAIL + "  alice@gmail.com  ", expectedFindCommand);
    }

    @Test
    public void parse_validPhoneArg_returnsFindCommand() {
        // no leading and trailing whitespaces
        FindCommand expectedFindCommand =
                new FindCommand(PREFIX_PHONE, "91234567");
        assertParseSuccess(parser, " " + PREFIX_PHONE + "91234567", expectedFindCommand);

        // whitespace before and after keyword
        assertParseSuccess(parser, " " + PREFIX_PHONE + "  91234567  ", expectedFindCommand);
    }

    @Test
    public void parse_validAddressArg_returnsFindCommand() {
        // no leading and trailing whitespaces
        FindCommand expectedFindCommand =
                new FindCommand(PREFIX_ADDRESS, "123, Jurong West Ave 6");
        assertParseSuccess(parser, " " + PREFIX_ADDRESS + "123, Jurong West Ave 6", expectedFindCommand);

        // whitespace before keyword
        assertParseSuccess(parser, " " + PREFIX_ADDRESS + "  123, Jurong West Ave 6  ", expectedFindCommand);
    }

    @Test
    public void parse_validTagArg_returnsFindCommand() {
        // no leading and trailing whitespaces
        FindCommand expectedFindCommand =
                new FindCommand(PREFIX_TAG, "friends");
        assertParseSuccess(parser, " " + PREFIX_TAG + "friends", expectedFindCommand);

        // whitespace before keyword
        assertParseSuccess(parser, " " + PREFIX_TAG + "  friends  ", expectedFindCommand);
    }

    @Test
    public void parse_validLessThanArg_returnsFindCommand() {
        // no leading and trailing whitespaces
        FindCommand expectedFindCommand =
                new FindCommand(PREFIX_LESS_THAN, "50");
        assertParseSuccess(parser, " " + PREFIX_LESS_THAN + "50", expectedFindCommand);

        // whitespace before keyword
        assertParseSuccess(parser, " " + PREFIX_LESS_THAN + "  50  ", expectedFindCommand);
    }

    @Test
    public void parse_validMoreThanArg_returnsFindCommand() {
        // no leading and trailing whitespaces
        FindCommand expectedFindCommand =
                new FindCommand(PREFIX_MORE_THAN, "50.55");
        assertParseSuccess(parser, " " + PREFIX_MORE_THAN + "50.55", expectedFindCommand);

        // whitespace before keyword
        assertParseSuccess(parser, " " + PREFIX_MORE_THAN + "  50.55  ", expectedFindCommand);
    }

    @Test
    public void parse_invalidMoreThanArg_throwsParseException() {
        assertParseFailure(parser, " " + PREFIX_MORE_THAN + "20.201",
                Score.MESSAGE_CONSTRAINTS);
    }

    @Test
    public void parse_invalidLessThanArg_throwsParseException() {
        assertParseFailure(parser, " " + PREFIX_LESS_THAN + "abc",
                Score.MESSAGE_CONSTRAINTS);
    }
}

package seedu.address.logic.parser;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;

import seedu.address.logic.commands.FindCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.person.Score;

/**
 * Parses input arguments and creates a new FindCommand object
 */
public class FindCommandParser implements Parser<FindCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the FindCommand
     * and returns a FindCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    public FindCommand parse(String args) throws ParseException {
        requireNonNull(args);
        ArgumentMultimap argMultimap =
                ArgumentTokenizer.tokenize(args, FindCommand.ACCEPTED_PREFIXES);

        Prefix prefix = extractPrefixForFindCommand(argMultimap);
        String keyword = extractValidKeyword(argMultimap, prefix);

        return new FindCommand(prefix, keyword);
    }

    /**
     * Checks if the given {@code ArgumentMultimap} contains only one valid, non-empty prefix for the FindCommand
     * and returns the found prefix.
     * @throws ParseException if the user input does not conform to the expected format
     */
    private Prefix extractPrefixForFindCommand(ArgumentMultimap argMultimap) throws ParseException {
        if (argMultimap.isSinglePrefix()) {
            for (Prefix prefix : FindCommand.ACCEPTED_PREFIXES) {
                if (argMultimap.getValue(prefix).isPresent()
                    && !argMultimap.getValue(prefix).get().isEmpty()) {
                    return prefix;
                }
            }
        }

        throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, FindCommand.MESSAGE_USAGE));
    }

    /**
     * Checks if the value of the given {@code ArgumentMultimap} is a positive decimal number
     * if the prefix is PREFIX_LESSTHAN or PREFIX_GREATERTHAN.
     * @throws ParseException if the user input does not conform to the expected format
     */
    private String extractValidKeyword(ArgumentMultimap argMultimap, Prefix prefix) throws ParseException {
        if (prefix.equals(CliSyntax.PREFIX_LESS_THAN) || prefix.equals(CliSyntax.PREFIX_MORE_THAN)) {
            if (!Score.isValidScoreString(argMultimap.getValue(prefix).get())) {
                throw new ParseException(Score.MESSAGE_CONSTRAINTS);
            }
        }
        return argMultimap.getValue(prefix).get();
    }
}

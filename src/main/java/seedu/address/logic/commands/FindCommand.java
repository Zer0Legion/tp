package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.CollectionUtil.isAnyNonNull;
import static seedu.address.logic.parser.CliSyntax.PREFIX_ADDRESS;
import static seedu.address.logic.parser.CliSyntax.PREFIX_EMAIL;
import static seedu.address.logic.parser.CliSyntax.PREFIX_LESS_THAN;
import static seedu.address.logic.parser.CliSyntax.PREFIX_MATRIC_NUMBER;
import static seedu.address.logic.parser.CliSyntax.PREFIX_MORE_THAN;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NAME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_PHONE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_REFLECTION;
import static seedu.address.logic.parser.CliSyntax.PREFIX_STUDIO;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TAG;

import java.util.Arrays;
import java.util.logging.Logger;

import seedu.address.commons.core.LogsCenter;
import seedu.address.commons.util.ToStringBuilder;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.logic.parser.Prefix;
import seedu.address.model.Model;
import seedu.address.model.exam.Exam;
import seedu.address.model.person.ExamPredicate;
import seedu.address.model.person.PersonDetailPredicate;

/**
 * Finds and lists all persons in address book whose name contains any of the argument keywords.
 * Keyword matching is case insensitive.
 */
public class FindCommand extends Command {
    public static final Prefix[] ACCEPTED_PREFIXES = {PREFIX_NAME, PREFIX_PHONE, PREFIX_EMAIL,
                                                      PREFIX_ADDRESS, PREFIX_TAG, PREFIX_MATRIC_NUMBER,
                                                      PREFIX_REFLECTION, PREFIX_STUDIO, PREFIX_LESS_THAN,
                                                      PREFIX_MORE_THAN};


    public static final String COMMAND_WORD = "find";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Finds all persons with a specified aspect "
            + "containing specified keyword (case-insensitive) or\n"
            + "which meets the criteria with the specified value (positive decimal up to 2 decimal places)\n"
            + "and displays them as a list with index numbers.\n"
            + "The currently supported prefixes are: "
            + Arrays.toString(ACCEPTED_PREFIXES) + "\n"
            + "Parameters: PREFIX|KEYWORD\n"
            + "Example: " + COMMAND_WORD + " " + PREFIX_NAME + "Alice";

    public static final String MESSAGE_SCORE_GREATER_THAN_MAX = "Value cannot be greater than the maximum score.";

    private static final Logger logger = LogsCenter.getLogger(FindCommand.class);

    private final Prefix prefix;
    private final String keyword;

    /**
     * Creates a FindCommand to find the specified Persons
     */
    public FindCommand(Prefix prefix, String keyword) {
        this.prefix = prefix;
        this.keyword = keyword;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);
        logger.info("Executing find command");
        if (isExamRequired()) {
            updateFilteredPersonListByScore(model);
        } else {
            updateFilteredPersonListByPersonDetail(model);
        }
        logger.info("Find command executed successfully");
        return new CommandResult(
                String.format(Messages.MESSAGE_PERSONS_LISTED_OVERVIEW, model.getFilteredPersonList().size()));
    }

    /**
     * Updates the filtered person list by score.
     * @param model Where the person list is stored.
     * @throws CommandException
     */
    private void updateFilteredPersonListByScore(Model model) throws CommandException {
        Exam selectedExam = model.getSelectedExam().getValue();
        if (!isAnyNonNull(selectedExam)) {
            throw new CommandException(Messages.MESSAGE_NO_EXAM_SELECTED);
        }
        if (selectedExam.getMaxScore().getScore() < Double.parseDouble(keyword)) {
            throw new CommandException(MESSAGE_SCORE_GREATER_THAN_MAX);
        }
        model.updateFilteredPersonList(new ExamPredicate(prefix, keyword, selectedExam));
    }

    /**
     * Updates the filtered person list by person detail.
     * @param model Where the person list is stored.
     */
    private void updateFilteredPersonListByPersonDetail(Model model) {
        model.updateFilteredPersonList(new PersonDetailPredicate(prefix, keyword));
    }

    /**
     * Checks if the prefix requires an exam to be selected.
     * @return True if the prefix is PREFIX_LESSTHAN or PREFIX_GREATERTHAN.
     */
    private boolean isExamRequired() {
        return prefix.equals(PREFIX_LESS_THAN) || prefix.equals(PREFIX_MORE_THAN);
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof FindCommand)) {
            return false;
        }

        FindCommand otherFindCommand = (FindCommand) other;
        return prefix.equals(otherFindCommand.prefix)
                && keyword.equals(otherFindCommand.keyword);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("prefix", prefix)
                .add("keyword", keyword)
                .toString();
    }
}

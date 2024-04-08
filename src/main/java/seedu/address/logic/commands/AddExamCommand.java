package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NAME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_SCORE;

import seedu.address.commons.util.ToStringBuilder;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.exam.Exam;

/**
 * Adds an exam to the address book.
 */
public class AddExamCommand extends Command {

    public static final String COMMAND_WORD = "addExam";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Adds an exam to the address book. "
            + "Parameters: " + PREFIX_NAME + "EXAM_NAME " + PREFIX_SCORE + "MAXIMUM_SCORE\n"
            + "Example: " + COMMAND_WORD + " "
            + PREFIX_NAME + "Midterm " + PREFIX_SCORE + "100";

    public static final String MESSAGE_SUCCESS = "New exam added: %1$s";
    public static final String MESSAGE_DUPLICATE_EXAM = "This exam already exists in the address book";

    private final Exam toAdd;

    /**
     * Creates an AddExamCommand to add the specified {@code Exam}
     */
    public AddExamCommand(Exam exam) {
        requireNonNull(exam);
        toAdd = exam;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);

        if (model.hasExam(toAdd)) {
            throw new CommandException(MESSAGE_DUPLICATE_EXAM);
        }

        model.addExam(toAdd);
        return new CommandResult(String.format(MESSAGE_SUCCESS, Messages.format(toAdd)));
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof AddExamCommand)) {
            return false;
        }

        AddExamCommand otherAddExamCommand = (AddExamCommand) other;
        return toAdd.equals(otherAddExamCommand.toAdd);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("toAdd", toAdd)
                .toString();
    }
}

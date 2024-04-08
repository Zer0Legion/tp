package seedu.address.logic.commands;

import static seedu.address.commons.util.CollectionUtil.isAnyNonNull;
import static seedu.address.commons.util.CollectionUtil.requireAllNonNull;
import static seedu.address.logic.parser.CliSyntax.PREFIX_SCORE;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.exam.Exam;
import seedu.address.model.person.Person;
import seedu.address.model.person.Score;



/**
 * Adds a score to a person in the address book.
 */
public class AddScoreCommand extends Command {

    public static final String COMMAND_WORD = "addScore";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Adds a score to the person identified by the index number used in the last person listing. "
            + "Parameters: INDEX (must be a positive integer) "
            + PREFIX_SCORE + "SCORE\n"
            + "Example: " + COMMAND_WORD + " 1 "
            + PREFIX_SCORE + "85";

    public static final String MESSAGE_ADD_SCORE_SUCCESS = "Added score %s for %s";
    public static final String MESSAGE_SCORE_EXISTS = "This person already has a score for this exam."
            + " Use editScore instead.";
    public static final String MESSAGE_SCORE_GREATER_THAN_MAX =
            "Score for %s cannot be greater than the maximum score.";

    private final Index targetIndex;
    private final Score score;

    /**
     * Creates an AddScoreCommand to add the specified {@code Score} to the person at the specified {@code Index}.
     */
    public AddScoreCommand(Index targetIndex, Score score) {
        requireAllNonNull(targetIndex, score);
        this.targetIndex = targetIndex;
        this.score = score;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        List<Person> lastShownList = model.getFilteredPersonList();

        if (targetIndex.getZeroBased() >= lastShownList.size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
        }

        Person personToEdit = lastShownList.get(targetIndex.getZeroBased());
        Map<Exam, Score> updatedScores = new HashMap<>(personToEdit.getScores());
        Exam selectedExam = model.getSelectedExam().getValue();

        if (!isAnyNonNull(selectedExam)) {
            throw new CommandException(Messages.MESSAGE_NO_EXAM_SELECTED);
        }

        if (selectedExam.getMaxScore().getScore() < score.getScore()) {
            throw new CommandException(String.format(MESSAGE_SCORE_GREATER_THAN_MAX, selectedExam.getName()));
        }

        if (updatedScores.containsKey(selectedExam)) {
            throw new CommandException(MESSAGE_SCORE_EXISTS);
        }

        model.addExamScoreToPerson(personToEdit, selectedExam, score);
        return new CommandResult(String.format("Added score %s for %s", score, personToEdit.getName()));
    }


    @Override
    public boolean equals(Object other) {
        return other == this
                || (other instanceof AddScoreCommand
                && targetIndex.equals(((AddScoreCommand) other).targetIndex)
                && score.equals(((AddScoreCommand) other).score));
    }
}

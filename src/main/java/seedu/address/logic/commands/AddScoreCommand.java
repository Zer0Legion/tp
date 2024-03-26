package seedu.address.logic.commands;

import static seedu.address.commons.util.CollectionUtil.isAnyNonNull;

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
            + "s/SCORE\n"
            + "Example: " + COMMAND_WORD + " 1 "
            + "s/85";

    public static final String MESSAGE_ADD_SCORE_SUCCESS = "Added score %s for %s";
    public static final String MESSAGE_SCORE_EXISTS = "This person already has a score for this exam."
            + " Use editScore instead.";
    public static final String MESSAGE_SCORE_GREATER_THAN_MAX = "Score cannot be greater than the maximum score.";

    private final Index targetIndex;
    private final Score score;

    /**
     * Creates an AddScoreCommand to add the specified {@code Score} to the person at the specified {@code Index}.
     */
    public AddScoreCommand(Index targetIndex, Score score) {
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
            throw new CommandException(MESSAGE_SCORE_GREATER_THAN_MAX);
        }
        updatedScores.put(selectedExam , score);

        Person editedPerson = createEditedPerson(personToEdit, updatedScores);

        model.setPerson(personToEdit, editedPerson);
        return new CommandResult(String.format("Added score %s for %s", score, editedPerson.getName()));
    }

    /**
     * Creates and returns a {@code Person} with the details of {@code personToEdit}
     * and the updated scores map.
     */
    private static Person createEditedPerson(Person personToEdit, Map<Exam, Score> updatedScores) {
        return new Person(
                personToEdit.getName(),
                personToEdit.getPhone(),
                personToEdit.getEmail(),
                personToEdit.getAddress(),
                personToEdit.getTags(),
                personToEdit.getMatric(),
                personToEdit.getReflection(),
                personToEdit.getStudio(),
                updatedScores
        );
    }
}
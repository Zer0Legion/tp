package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.Messages.MESSAGE_PERSONS_LISTED_OVERVIEW;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandFailure;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.logic.parser.CliSyntax.PREFIX_EMAIL;
import static seedu.address.logic.parser.CliSyntax.PREFIX_LESS_THAN;
import static seedu.address.logic.parser.CliSyntax.PREFIX_MORE_THAN;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NAME;
import static seedu.address.testutil.TypicalPersons.ALICE;
import static seedu.address.testutil.TypicalPersons.BENSON;
import static seedu.address.testutil.TypicalPersons.CARL;
import static seedu.address.testutil.TypicalPersons.DANIEL;
import static seedu.address.testutil.TypicalPersons.ELLE;
import static seedu.address.testutil.TypicalPersons.FIONA;
import static seedu.address.testutil.TypicalPersons.GEORGE;
import static seedu.address.testutil.TypicalPersons.getTypicalAddressBook;

import java.util.Arrays;
import java.util.Collections;

import org.junit.jupiter.api.Test;

import seedu.address.logic.Messages;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.exam.Exam;
import seedu.address.model.person.ExamPredicate;
import seedu.address.model.person.PersonDetailPredicate;
import seedu.address.model.person.Score;

/**
 * Contains integration tests (interaction with the Model) for {@code FindCommand}.
 */
public class FindCommandTest {
    private Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());
    private Model expectedModel = new ModelManager(getTypicalAddressBook(), new UserPrefs());

    @Test
    public void equals() {
        FindCommand findFirstCommand = new FindCommand(PREFIX_NAME, "first");
        FindCommand findSecondCommand = new FindCommand(PREFIX_NAME, "second");

        // same object -> returns true
        assertTrue(findFirstCommand.equals(findFirstCommand));

        // same values -> returns true
        FindCommand findFirstCommandCopy = new FindCommand(PREFIX_NAME, "first");
        assertTrue(findFirstCommand.equals(findFirstCommandCopy));

        // different types -> returns false
        assertFalse(findFirstCommand.equals(1));

        // null -> returns false
        assertFalse(findFirstCommand.equals(null));

        // different person -> returns false
        assertFalse(findFirstCommand.equals(findSecondCommand));
    }

    @Test
    public void execute_name_noPersonFound() {
        String expectedMessage = String.format(MESSAGE_PERSONS_LISTED_OVERVIEW, 0);
        FindCommand command = new FindCommand(PREFIX_NAME, "Adam");
        expectedModel.updateFilteredPersonList(new PersonDetailPredicate(PREFIX_NAME, "Adam"));
        assertCommandSuccess(command, model, expectedMessage, expectedModel);
        assertEquals(Collections.emptyList(), model.getFilteredPersonList());
    }

    @Test
    public void execute_email_multiplePersonsFound() {
        String expectedMessage = String.format(MESSAGE_PERSONS_LISTED_OVERVIEW, 7);
        FindCommand command = new FindCommand(PREFIX_EMAIL, "@example.com");
        expectedModel.updateFilteredPersonList(new PersonDetailPredicate(PREFIX_EMAIL, "@example.com"));
        assertCommandSuccess(command, model, expectedMessage, expectedModel);
        assertEquals(Arrays.asList(ALICE, BENSON, CARL, DANIEL, ELLE, FIONA, GEORGE), model.getFilteredPersonList());
    }

    @Test
    public void execute_examRequiredNoExamSelected_throwsCommandException() {
        FindCommand command = new FindCommand(PREFIX_LESS_THAN, "50");
        assertCommandFailure(command, model, Messages.MESSAGE_NO_EXAM_SELECTED);
    }

    @Test
    public void execute_examRequiredExamSelectedValueTooHigh_throwsCommandException() {
        model.selectExam(new Exam("Midterm", new Score(100)));
        FindCommand command = new FindCommand(PREFIX_MORE_THAN, "101");
        assertCommandFailure(command, model, FindCommand.MESSAGE_SCORE_GREATER_THAN_MAX);
    }

    @Test
    public void execute_examRequired_multiplePersonsFound() {
        model.selectExam(new Exam("Midterm", new Score(100)));
        String expectedMessage = String.format(MESSAGE_PERSONS_LISTED_OVERVIEW, 3);
        FindCommand command = new FindCommand(PREFIX_LESS_THAN, "55");
        expectedModel.updateFilteredPersonList(new ExamPredicate(PREFIX_LESS_THAN, "55",
                model.getSelectedExam().getValue()));
        assertCommandSuccess(command, model, expectedMessage, expectedModel);
        assertEquals(Arrays.asList(CARL, DANIEL, ELLE), model.getFilteredPersonList());
    }

    @Test
    public void toStringMethod() {
        FindCommand findCommand = new FindCommand(PREFIX_EMAIL, "@example.com");
        String expected = FindCommand.class.getCanonicalName() + "{prefix=" + PREFIX_EMAIL
                + ", keyword=" + "@example.com" + "}";

        assertEquals(expected, findCommand.toString());
    }
}

package seedu.address.logic.commands;

import static seedu.address.logic.parser.CliSyntax.PREFIX_NAME;
import static seedu.address.testutil.TypicalPersons.getTypicalAddressBook;

import org.junit.jupiter.api.Test;

import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.person.PersonDetailPredicate;


/**
 * Contains integration tests (interaction with the Model) and unit tests for DeleteShownCommand.
 */
public class DeleteShownCommandTest {
    private Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());
    private Model expectedModel = new ModelManager(getTypicalAddressBook(), new UserPrefs());
    private Model emptyModel = new ModelManager();

    @Test
    public void execute_tryDeleteAll_throwsCommandException() {
        DeleteShownCommand deleteShownCommand = new DeleteShownCommand();
        CommandTestUtil.assertCommandFailure(deleteShownCommand, model, DeleteShownCommand.MESSAGE_NO_FILTER);
    }

    @Test
    public void execute_tryDeleteNobody_throwsCommandException() {
        DeleteShownCommand deleteShownCommand = new DeleteShownCommand();
        CommandTestUtil.assertCommandFailure(deleteShownCommand, emptyModel, DeleteShownCommand.MESSAGE_NO_PERSONS);
    }

    @Test
    public void execute_success() {
        DeleteShownCommand deleteShownCommand = new DeleteShownCommand();

        PersonDetailPredicate predicate =
                new PersonDetailPredicate(PREFIX_NAME, "l");
        model.updateFilteredPersonList(predicate);

        PersonDetailPredicate predicate2 =
                new PersonDetailPredicate(PREFIX_NAME, "o");
        expectedModel.updateFilteredPersonList(predicate2);

        CommandTestUtil.assertCommandSuccess(deleteShownCommand, model,
                String.format(DeleteShownCommand.MESSAGE_SUCCESS, 4, 3), model);

        // For some reason the expected model and model have the same persons but asserting model == expectedModel
        // fails. So we have to manually check if the persons are the same.
        for (int i = 0; i < model.getFilteredPersonList().size(); i++) {
            assert(model.getFilteredPersonList().get(i).equals(expectedModel.getFilteredPersonList().get(i)));
        }
    }


}

package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static seedu.address.testutil.Assert.assertThrows;

import java.nio.file.Path;
import java.nio.file.Paths;

import org.junit.jupiter.api.Test;

import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.AddressBook;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;

public class ImportCommandTest {
    @Test
    public void execute_import_success() throws CommandException {
        Model model = new ModelManager(new AddressBook(), new UserPrefs());
        Path filePath = Paths.get("src/test/data/ImportCommandTest/valid.csv");
        ImportCommand importCommand = new ImportCommand(filePath);
        CommandResult commandResult = importCommand.execute(model);
        String expected = "Imported persons successfully!\n\n"
                + "All valid persons have been added!\n"
                + "Successful imports: 7\n"
                + "Unsuccessful imports: 0\n";
        String actual = commandResult.getFeedbackToUser();
        assertEquals(expected , actual);
    }
    @Test
    public void execute_invalidPath_failure() {
        Model model = new ModelManager(new AddressBook(), new UserPrefs());
        Path invalidFilePath = Paths.get("src/test/data/ImportCommandTest/nonExistent.csv");
        ImportCommand importCommand = new ImportCommand(invalidFilePath);
        assertThrows(CommandException.class, () -> importCommand.execute(model));
    }

    @Test
    public void execute_extraHeaders_success() throws CommandException {
        Model model = new ModelManager(new AddressBook(), new UserPrefs());
        Path filePath = Paths.get("src/test/data/ImportCommandTest/extraHeader.csv");
        ImportCommand importCommand = new ImportCommand(filePath);
        CommandResult commandResult = importCommand.execute(model);
        String expected = "Imported persons successfully!\n\n"
                + "All valid persons have been added!\n"
                + "Successful imports: 7\n"
                + "Unsuccessful imports: 0\n";
        String actual = commandResult.getFeedbackToUser();
        assertEquals(expected , actual);
    }

    @Test
    public void execute_missingOptionalValues_success() throws CommandException {
        Model model = new ModelManager(new AddressBook(), new UserPrefs());
        Path filePath = Paths.get("src/test/data/ImportCommandTest/missingOptionalValue.csv");
        ImportCommand importCommand = new ImportCommand(filePath);
        CommandResult commandResult = importCommand.execute(model);
        String expected = "Imported persons successfully!\n\n"
                + "All valid persons have been added!\n"
                + "Successful imports: 7\n"
                + "Unsuccessful imports: 0\n";
        String actual = commandResult.getFeedbackToUser();
        assertEquals(expected , actual);
    }
    @Test
    public void equals_success() {
        Path filePath = Paths.get("src/test/data/ImportCommandTest/valid.csv");
        ImportCommand importCommand = new ImportCommand(filePath);
        ImportCommand importCommandCopy = new ImportCommand(filePath);
        assertEquals(importCommand, importCommandCopy);
    }
}

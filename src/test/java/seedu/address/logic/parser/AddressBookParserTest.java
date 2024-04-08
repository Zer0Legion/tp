package seedu.address.logic.parser;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.Messages.MESSAGE_UNKNOWN_COMMAND;
import static seedu.address.logic.commands.CommandTestUtil.VALID_TAG_STUDENT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_IMPORT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NAME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_SCORE;
import static seedu.address.testutil.Assert.assertThrows;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_PERSON;

import org.junit.jupiter.api.Test;

import seedu.address.logic.commands.AddCommand;
import seedu.address.logic.commands.AddExamCommand;
import seedu.address.logic.commands.AddScoreCommand;
import seedu.address.logic.commands.ClearCommand;
import seedu.address.logic.commands.CopyCommand;
import seedu.address.logic.commands.DeleteCommand;
import seedu.address.logic.commands.DeleteExamCommand;
import seedu.address.logic.commands.DeleteScoreCommand;
import seedu.address.logic.commands.DeleteShownCommand;
import seedu.address.logic.commands.DeselectExamCommand;
import seedu.address.logic.commands.EditCommand;
import seedu.address.logic.commands.EditCommand.EditPersonDescriptor;
import seedu.address.logic.commands.EditScoreCommand;
import seedu.address.logic.commands.ExitCommand;
import seedu.address.logic.commands.ExportCommand;
import seedu.address.logic.commands.FindCommand;
import seedu.address.logic.commands.HelpCommand;
import seedu.address.logic.commands.ImportCommand;
import seedu.address.logic.commands.ImportExamScoresCommand;
import seedu.address.logic.commands.ListCommand;
import seedu.address.logic.commands.SelectExamCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.person.Person;
import seedu.address.model.person.Score;
import seedu.address.testutil.EditPersonDescriptorBuilder;
import seedu.address.testutil.PersonBuilder;
import seedu.address.testutil.PersonUtil;

public class AddressBookParserTest {

    private final AddressBookParser parser = new AddressBookParser();

    @Test
    public void parseCommand_add() throws Exception {
        Person person = new PersonBuilder().withTags(VALID_TAG_STUDENT).build();
        AddCommand command = (AddCommand) parser.parseCommand(PersonUtil.getAddCommand(person));
        assertEquals(new AddCommand(person), command);
    }

    @Test
    public void parseCommand_clear() throws Exception {
        assertTrue(parser.parseCommand(ClearCommand.COMMAND_WORD) instanceof ClearCommand);
        assertTrue(parser.parseCommand(ClearCommand.COMMAND_WORD + " 3") instanceof ClearCommand);
    }

    @Test
    public void parseCommand_delete() throws Exception {
        DeleteCommand command = (DeleteCommand) parser.parseCommand(
                DeleteCommand.COMMAND_WORD + " " + INDEX_FIRST_PERSON.getOneBased());
        assertEquals(new DeleteCommand(INDEX_FIRST_PERSON), command);
    }

    @Test
    public void parseCommand_edit() throws Exception {
        Person person = new PersonBuilder().build();
        EditPersonDescriptor descriptor = new EditPersonDescriptorBuilder(person).build();
        EditCommand command = (EditCommand) parser.parseCommand(EditCommand.COMMAND_WORD + " "
                + INDEX_FIRST_PERSON.getOneBased() + " " + PersonUtil.getEditPersonDescriptorDetails(descriptor));
        assertEquals(new EditCommand(INDEX_FIRST_PERSON, descriptor), command);
    }

    @Test
    public void parseCommand_editScore() throws Exception {
        String prefix = PREFIX_SCORE.toString();
        Score score = new Score(17);
        EditScoreCommand command = (EditScoreCommand) parser.parseCommand(EditScoreCommand.COMMAND_WORD + " "
                + INDEX_FIRST_PERSON.getOneBased() + " " + prefix + score.getScore());
        assertEquals(new EditScoreCommand(INDEX_FIRST_PERSON, score), command);
    }

    @Test
    public void parseCommand_exit() throws Exception {
        assertTrue(parser.parseCommand(ExitCommand.COMMAND_WORD) instanceof ExitCommand);
        assertTrue(parser.parseCommand(ExitCommand.COMMAND_WORD + " 3") instanceof ExitCommand);
    }

    @Test
    public void parseCommand_find() throws Exception {
        String prefix = PREFIX_NAME.toString();
        String parameter = "Johan";
        FindCommand command = (FindCommand) parser.parseCommand(
                FindCommand.COMMAND_WORD + " " + prefix + parameter);
        assertEquals(new FindCommand(PREFIX_NAME, parameter), command);
    }

    @Test
    public void parseCommand_help() throws Exception {
        assertTrue(parser.parseCommand(HelpCommand.COMMAND_WORD) instanceof HelpCommand);
        assertTrue(parser.parseCommand(HelpCommand.COMMAND_WORD + " 3") instanceof HelpCommand);
    }

    @Test
    public void parseCommand_list() throws Exception {
        assertTrue(parser.parseCommand(ListCommand.COMMAND_WORD) instanceof ListCommand);
        assertTrue(parser.parseCommand(ListCommand.COMMAND_WORD + " 3") instanceof ListCommand);
    }

    @Test
    public void parseCommand_copy() throws Exception {
        assertTrue(parser.parseCommand(CopyCommand.COMMAND_WORD) instanceof CopyCommand);
        assertTrue(parser.parseCommand(CopyCommand.COMMAND_WORD + " 3") instanceof CopyCommand);
    }

    @Test
    public void parseCommand_export() throws Exception {
        assertTrue(parser.parseCommand(ExportCommand.COMMAND_WORD) instanceof ExportCommand);
    }

    @Test
    public void parseCommand_import() throws Exception {
        assertTrue(parser.parseCommand(ImportCommand.COMMAND_WORD + " "
                + PREFIX_IMPORT + "src.csv") instanceof ImportCommand);
    }

    @Test
    public void parseCommand_importExam() throws Exception {
        assertTrue(parser.parseCommand(
                ImportExamScoresCommand.COMMAND_WORD + " " + PREFIX_IMPORT + "src.csv")
                instanceof ImportExamScoresCommand);
    }

    @Test
    public void parseCommand_deleteShown() throws Exception {
        assertTrue(parser.parseCommand(DeleteShownCommand.COMMAND_WORD) instanceof DeleteShownCommand);
    }

    @Test
    public void parseCommand_selectExam() throws Exception {
        assertTrue(parser.parseCommand(SelectExamCommand.COMMAND_WORD + " 1") instanceof SelectExamCommand);
    }

    @Test
    public void parseCommand_unrecognisedInput_throwsParseException() {
        assertThrows(ParseException.class, String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                HelpCommand.MESSAGE_USAGE), () -> parser.parseCommand(""));
    }

    @Test
    void parseCommand_addExam() throws Exception {
        assertTrue(parser.parseCommand(AddExamCommand.COMMAND_WORD + " "
                + PREFIX_NAME + "Midterm " + PREFIX_SCORE + "100")
                instanceof AddExamCommand);
    }

    @Test
    public void parseCommand_deleteExam() throws Exception {
        assertTrue(parser.parseCommand("deleteExam 1") instanceof DeleteExamCommand);
    }

    @Test
    public void parseCommand_deleteScore() throws Exception {
        DeleteScoreCommand command = (DeleteScoreCommand) parser.parseCommand(
                DeleteScoreCommand.COMMAND_WORD + " " + INDEX_FIRST_PERSON.getOneBased());
        assertEquals(new DeleteScoreCommand(INDEX_FIRST_PERSON), command);
    }

    @Test
    public void parseCommand_deselectExam() throws Exception {
        assertTrue(parser.parseCommand("deselectExam") instanceof DeselectExamCommand);
    }

    @Test
    public void parseCommand_addScore() throws Exception {
        assertTrue(parser.parseCommand("addScore 1 " + PREFIX_SCORE + "100") instanceof AddScoreCommand);
    }

    @Test
    public void parseCommand_unknownCommand_throwsParseException() {
        assertThrows(ParseException.class, MESSAGE_UNKNOWN_COMMAND, () -> parser.parseCommand("unknownCommand"));
    }
}

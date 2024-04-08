package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandFailure;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.testutil.TypicalPersons.getTypicalAddressBook;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.exam.Exam;
import seedu.address.model.person.Score;

public class ImportExamScoresCommandTest {
    public static final String PATH_VALID = "src/test/data/ImportExamCommandTest/valid.csv";
    public static final String PATH_EXTRA = "src/test/data/ImportExamCommandTest/extra.csv";
    public static final String PATH_EMPTY_CSV = "src/test/data/ImportExamCommandTest/valid_empty_csv.csv";
    public static final String PATH_INVALID = "invalid/path/to/file.csv";
    public static final String PATH_SCORE_NOT_NUMBER = "src/test/data/ImportExamCommandTest/not_number.csv";
    public static final String PATH_DUPLICATE_EXAMS = "src/test/data/ImportExamCommandTest/duplicate_exams.csv";
    public static final String PATH_MISMATCH_CSV_HEADERS = "src/test/data/ImportExamCommandTest/wrong_headers.csv";
    private ImportExamScoresCommand importExamScoresCommand;
    private Model model;

    @BeforeEach
    public void setUp() {
        model = mock(Model.class);
        model = new ModelManager(getTypicalAddressBook(), new UserPrefs());
    }

    @Test
    public void execute_validFilePath_success() {
        Path filePath = Paths.get(PATH_EMPTY_CSV);
        importExamScoresCommand = new ImportExamScoresCommand(filePath);

        assertCommandFailure(importExamScoresCommand, model, ImportExamScoresCommand.ERROR_EMAIL_FIRST_VALUE);
    }

    @Test
    public void execute_invalidFilePath_throwsCommandException() {
        Path invalidFilePath = Paths.get(PATH_INVALID);
        importExamScoresCommand = new ImportExamScoresCommand(invalidFilePath);

        assertThrows(CommandException.class, () -> importExamScoresCommand.execute(model));
    }

    @Test
    public void testGenerateErrorReportEmpty() {
        String result = new ImportExamScoresCommand(Paths.get(PATH_VALID)).generateErrorReport();
        assertEquals("", result);
    }

    @Test
    public void testSuccess() {
        Path filePath = Paths.get(PATH_VALID);
        ImportExamScoresCommand importExamScoresCommand = new ImportExamScoresCommand(filePath);
        String expectedMessage = String.format(ImportExamScoresCommand.MESSAGE_SUCCESS, filePath);
        assertCommandSuccess(importExamScoresCommand, model, expectedMessage, model);
    }

    @Test
    public void testEquals() {
        Path filePath = Paths.get(PATH_VALID);
        ImportExamScoresCommand importExamScoresCommand = new ImportExamScoresCommand(filePath);
        ImportExamScoresCommand importExamScoresCommandCopy = new ImportExamScoresCommand(filePath);
        assertEquals(importExamScoresCommand, importExamScoresCommandCopy);
    }

    @Test
    public void testFailing() {
        Path filePath = Paths.get(PATH_EXTRA);
        Exam midterm = new Exam("BadMidterm", new Score(100));
        model.addExam(midterm);
        ImportExamScoresCommand importExamScoresCommand = new ImportExamScoresCommand(filePath);

        String expectedError = buildErrorReport(filePath.toString(),
                "email of non@example.com: " + ImportExamScoresCommand.MESSAGE_PERSON_DOES_NOT_EXIST,
                "email of johnd@example.com: " + String.format(
                        ImportExamScoresCommand.MESSAGE_GRADE_TOO_HIGH, "Midterm"),
                "exam of NonExistent: " + ImportExamScoresCommand.MESSAGE_EXAM_DOES_NOT_EXIST);

        assertCommandSuccess(importExamScoresCommand, model, expectedError, model);
    }

    @Test
    public void test_notNumber() {
        Path filePath = Paths.get(PATH_SCORE_NOT_NUMBER);
        ImportExamScoresCommand importExamScoresCommand = new ImportExamScoresCommand(filePath);

        String expectedError = buildErrorReport(filePath.toString(),
                "email of alice@example.com: "
                        + String.format(ImportExamScoresCommand.MESSAGE_SCORE_NOT_NUMBER, "Midterm"));

        assertCommandSuccess(importExamScoresCommand, model, expectedError, model);
    }

    @Test
    public void test_duplicateExamHeaders() {
        Path filePath = Paths.get(PATH_DUPLICATE_EXAMS);
        ImportExamScoresCommand importExamScoresCommand = new ImportExamScoresCommand(filePath);

        String expectedError = buildErrorReport(filePath.toString(),
                "exam of Midterm: " + ImportExamScoresCommand.MESSAGE_DUPLICATE_EXAM);
        assertCommandSuccess(importExamScoresCommand, model, expectedError, model);
    }

    @Test
    public void testIsEmailFirstValue_validEmailFirstValue_success() {
        ImportExamScoresCommand importExamScoresCommand = new ImportExamScoresCommand(Paths.get(PATH_VALID));

        String[][] valid = {{"email"}};
        List<String[]> lst = List.of(valid);

        assertTrue(importExamScoresCommand.isEmailFirstValue(lst));
    }

    @Test
    public void testIsEmailFirstValue_listSizeZero_failure() {
        ImportExamScoresCommand importExamScoresCommand = new ImportExamScoresCommand(Paths.get(PATH_VALID));

        String[][] invalid = {};
        List<String[]> lst = List.of(invalid);

        assertFalse(importExamScoresCommand.isEmailFirstValue(lst));
    }

    @Test
    public void testIsEmailFirstValue_stringArraySizeZero_failure() {
        ImportExamScoresCommand importExamScoresCommand = new ImportExamScoresCommand(Paths.get(PATH_VALID));

        String[][] invalid = {{}};
        List<String[]> lst = List.of(invalid);

        assertFalse(importExamScoresCommand.isEmailFirstValue(lst));
    }

    @Test
    public void testMismatchCsvHeaders_failure() {
        ImportExamScoresCommand importExamScoresCommand = new ImportExamScoresCommand(
                Paths.get(PATH_MISMATCH_CSV_HEADERS));

        assertCommandFailure(importExamScoresCommand, model, ImportExamScoresCommand.ERROR_WRONG_CSV_FORMAT);
    }


    private String buildErrorReport(String filePath, String... errors) {
        StringBuilder errorReport = new StringBuilder();
        errorReport.append(String.format(ImportExamScoresCommand.MESSAGE_SUCCESS, filePath));
        errorReport.append(ImportExamScoresCommand.PREFIX_ERROR_REPORT);
        for (String error : errors) {
            errorReport.append(error + "\n");
        }
        return errorReport.toString();
    }

}

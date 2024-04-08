package seedu.address.model;

import static org.junit.Assert.assertNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NAME;
import static seedu.address.model.Model.PREDICATE_SHOW_ALL_PERSONS;
import static seedu.address.testutil.Assert.assertThrows;
import static seedu.address.testutil.TypicalPersons.ALICE;
import static seedu.address.testutil.TypicalPersons.BENSON;
import static seedu.address.testutil.TypicalPersons.CARL;
import static seedu.address.testutil.TypicalPersons.DANIEL;
import static seedu.address.testutil.TypicalPersons.ELLE;
import static seedu.address.testutil.TypicalPersons.FIONA;
import static seedu.address.testutil.TypicalPersons.GEORGE;
import static seedu.address.testutil.TypicalPersons.MIDTERM;
import static seedu.address.testutil.TypicalPersons.getTypicalAddressBook;

import java.nio.file.Path;
import java.nio.file.Paths;

import org.junit.jupiter.api.Test;

import seedu.address.commons.core.GuiSettings;
import seedu.address.model.exam.Exam;
import seedu.address.model.person.Person;
import seedu.address.model.person.PersonDetailPredicate;
import seedu.address.model.person.Score;
import seedu.address.testutil.AddressBookBuilder;

public class ModelManagerTest {

    private ModelManager modelManager = new ModelManager();

    @Test
    public void constructor() {
        assertEquals(new UserPrefs(), modelManager.getUserPrefs());
        assertEquals(new GuiSettings(), modelManager.getGuiSettings());
        assertEquals(new AddressBook(), new AddressBook(modelManager.getAddressBook()));
    }

    @Test
    public void setUserPrefs_nullUserPrefs_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> modelManager.setUserPrefs(null));
    }

    @Test
    public void setUserPrefs_validUserPrefs_copiesUserPrefs() {
        UserPrefs userPrefs = new UserPrefs();
        userPrefs.setAddressBookFilePath(Paths.get("address/book/file/path"));
        userPrefs.setGuiSettings(new GuiSettings(1, 2, 3, 4));
        modelManager.setUserPrefs(userPrefs);
        assertEquals(userPrefs, modelManager.getUserPrefs());

        // Modifying userPrefs should not modify modelManager's userPrefs
        UserPrefs oldUserPrefs = new UserPrefs(userPrefs);
        userPrefs.setAddressBookFilePath(Paths.get("new/address/book/file/path"));
        assertEquals(oldUserPrefs, modelManager.getUserPrefs());
    }

    @Test
    public void setGuiSettings_nullGuiSettings_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> modelManager.setGuiSettings(null));
    }

    @Test
    public void setGuiSettings_validGuiSettings_setsGuiSettings() {
        GuiSettings guiSettings = new GuiSettings(1, 2, 3, 4);
        modelManager.setGuiSettings(guiSettings);
        assertEquals(guiSettings, modelManager.getGuiSettings());
    }

    @Test
    public void setAddressBookFilePath_nullPath_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> modelManager.setAddressBookFilePath(null));
    }

    @Test
    public void setAddressBookFilePath_validPath_setsAddressBookFilePath() {
        Path path = Paths.get("address/book/file/path");
        modelManager.setAddressBookFilePath(path);
        assertEquals(path, modelManager.getAddressBookFilePath());
    }

    @Test
    public void hasPerson_nullPerson_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> modelManager.hasPerson(null));
    }

    @Test
    public void hasExam_nullExam_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> modelManager.hasExam(null));
    }

    @Test
    public void hasPerson_personNotInAddressBook_returnsFalse() {
        assertFalse(modelManager.hasPerson(ALICE));
    }

    @Test
    public void hasExam_examNotInAddressBook_returnsFalse() {
        assertFalse(modelManager.hasExam(new Exam("Midterm", new Score(100))));
    }

    @Test
    public void hasPerson_personInAddressBook_returnsTrue() {
        modelManager.addPerson(ALICE);
        assertTrue(modelManager.hasPerson(ALICE));
    }

    @Test
    public void hasExam_examInAddressBook_returnsTrue() {
        Exam midterm = new Exam("Midterm", new Score(100));
        modelManager.addExam(midterm);
        assertTrue(modelManager.hasExam(midterm));
    }

    @Test
    public void getFilteredPersonList_modifyList_throwsUnsupportedOperationException() {
        assertThrows(UnsupportedOperationException.class, () -> modelManager.getFilteredPersonList().remove(0));
    }

    @Test
    public void getFilteredExamList_modifyList_throwsUnsupportedOperationException() {
        assertThrows(UnsupportedOperationException.class, () -> modelManager.getExamList().remove(0));
    }

    @Test
    public void selectExam_deselectExam_getSelectedExam() {
        Exam exam = new Exam("Midterm", new Score(100));

        // initially, no exam is selected
        assertNull(modelManager.getSelectedExam().getValue());

        // select an exam
        modelManager.selectExam(exam);
        assertEquals(exam, modelManager.getSelectedExam().getValue());

        // deselect the exam
        modelManager.deselectExam();
        assertNull(modelManager.getSelectedExam().getValue());
    }

    @Test
    public void deleteExam_examInAddressBook_removesExamFromPersons() {
        Exam midterm = new Exam("Midterm", new Score(100));
        modelManager.addExam(midterm);
        modelManager.addPerson(ALICE);
        modelManager.selectExam(midterm);

        // add score for ALICE
        modelManager.addExamScoreToPerson(ALICE, midterm, new Score(85));

        // delete the exam
        modelManager.deleteExam(midterm);

        // ALICE should not have the exam in her scores
        assertFalse(modelManager.getFilteredPersonList().get(0).getScores().containsKey(midterm));
    }

    @Test
    public void getExamScoreStatistics_scoresInEvenPersonsInAddressBook_success() {
        ModelManager modelManager = new ModelManager();

        // Add some persons with scores
        Person daniel = DANIEL;
        Person elle = ELLE;
        Person fiona = FIONA;
        Person george = GEORGE;

        modelManager.addPerson(daniel);
        modelManager.addPerson(elle);
        modelManager.addPerson(fiona);
        modelManager.addPerson(george);

        Exam midterm = MIDTERM;

        // Calculate statistics
        modelManager.selectExam(midterm);
        ScoreStatistics stats = modelManager.getSelectedExamStatistics().getValue();

        // Verify the statistics
        assertEquals(55, stats.getMean());
        assertEquals(55, stats.getMedian());
    }

    @Test
    public void getExamScoreStatistics_scoresInAllPersonsInAddressBook_success() {
        ModelManager modelManager = new ModelManager();

        // Add some persons with scores
        Person carl = CARL;
        Person daniel = DANIEL;
        Person elle = ELLE;
        Person fiona = FIONA;
        Person george = GEORGE;

        modelManager.addPerson(carl);
        modelManager.addPerson(daniel);
        modelManager.addPerson(elle);
        modelManager.addPerson(fiona);
        modelManager.addPerson(george);

        Exam midterm = MIDTERM;

        // Calculate statistics
        modelManager.selectExam(midterm);
        ScoreStatistics stats = modelManager.getSelectedExamStatistics().getValue();

        // Verify the statistics
        assertEquals(50, stats.getMean());
        assertEquals(50, stats.getMedian());
    }

    @Test
    public void getExamScoreStatistics_someScoresInPersonsInAddressBook_success() {

        ModelManager modelManager = new ModelManager(getTypicalAddressBook(), new UserPrefs());

        Exam midterm = MIDTERM;

        // Calculate statistics
        modelManager.selectExam(midterm);
        ScoreStatistics stats = modelManager.getSelectedExamStatistics().getValue();

        // Verify the statistics
        assertEquals(50, stats.getMean());
        assertEquals(50, stats.getMedian());
    }

    @Test
    public void getExamScoreStatistics_noScoresInPersonsInAddressBook_success() {
        ModelManager modelManager = new ModelManager();

        // Add some persons without scores
        Person alice = ALICE;
        Person benson = BENSON;

        modelManager.addPerson(alice);
        modelManager.addPerson(benson);

        Exam midterm = MIDTERM;

        // Calculate statistics
        modelManager.selectExam(midterm);
        ScoreStatistics stats = modelManager.getSelectedExamStatistics().getValue();

        // Verify the statistics
        assertEquals(-1, stats.getMean());
        assertEquals(-1, stats.getMedian());
    }

    @Test
    public void getExamScoreStatistics_noPersonsInAddressBook_success() {
        ModelManager modelManager = new ModelManager();

        Exam midterm = MIDTERM;

        // Calculate statistics
        modelManager.selectExam(midterm);
        ScoreStatistics stats = modelManager.getSelectedExamStatistics().getValue();

        // Verify the statistics
        assertEquals(-1, stats.getMean());
        assertEquals(-1, stats.getMedian());
    }

    @Test
    public void equals() {
        AddressBook addressBook = new AddressBookBuilder().withPerson(ALICE).withPerson(BENSON).build();
        AddressBook differentAddressBook = new AddressBook();
        UserPrefs userPrefs = new UserPrefs();

        // same values -> returns true
        modelManager = new ModelManager(addressBook, userPrefs);
        ModelManager modelManagerCopy = new ModelManager(addressBook, userPrefs);
        assertTrue(modelManager.equals(modelManagerCopy));

        // same object -> returns true
        assertTrue(modelManager.equals(modelManager));

        // null -> returns false
        assertFalse(modelManager.equals(null));

        // different types -> returns false
        assertFalse(modelManager.equals(5));

        // different addressBook -> returns false
        assertFalse(modelManager.equals(new ModelManager(differentAddressBook, userPrefs)));

        // different filteredList -> returns false
        modelManager.updateFilteredPersonList(
            new PersonDetailPredicate(PREFIX_NAME, ALICE.getName().fullName));
        assertFalse(modelManager.equals(new ModelManager(addressBook, userPrefs)));

        // resets modelManager to initial state for upcoming tests
        modelManager.updateFilteredPersonList(PREDICATE_SHOW_ALL_PERSONS);

        // different userPrefs -> returns false
        UserPrefs differentUserPrefs = new UserPrefs();
        differentUserPrefs.setAddressBookFilePath(Paths.get("differentFilePath"));
        assertFalse(modelManager.equals(new ModelManager(addressBook, differentUserPrefs)));
    }
}

package seedu.address.logic.util;

import java.nio.file.Path;
import java.util.function.Predicate;

import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import seedu.address.commons.core.GuiSettings;
import seedu.address.model.Model;
import seedu.address.model.ReadOnlyAddressBook;
import seedu.address.model.ReadOnlyUserPrefs;
import seedu.address.model.ScoreStatistics;
import seedu.address.model.exam.Exam;
import seedu.address.model.person.Person;
import seedu.address.model.person.Score;

/**
 * A default model stub that have all of the methods failing.
 */
public class ModelStub implements Model {
    @Override
    public void setUserPrefs(ReadOnlyUserPrefs userPrefs) {
        throw new AssertionError("This method should not be called.");
    }

    @Override
    public ReadOnlyUserPrefs getUserPrefs() {
        throw new AssertionError("This method should not be called.");
    }

    @Override
    public GuiSettings getGuiSettings() {
        throw new AssertionError("This method should not be called.");
    }

    @Override
    public void setGuiSettings(GuiSettings guiSettings) {
        throw new AssertionError("This method should not be called.");
    }

    @Override
    public Path getAddressBookFilePath() {
        throw new AssertionError("This method should not be called.");
    }

    @Override
    public void setAddressBookFilePath(Path addressBookFilePath) {
        throw new AssertionError("This method should not be called.");
    }

    @Override
    public void addPerson(Person person) {
        throw new AssertionError("This method should not be called.");
    }

    @Override
    public void setAddressBook(ReadOnlyAddressBook newData) {
        throw new AssertionError("This method should not be called.");
    }

    @Override
    public ReadOnlyAddressBook getAddressBook() {
        throw new AssertionError("This method should not be called.");
    }

    @Override
    public boolean hasPerson(Person person) {
        throw new AssertionError("This method should not be called.");
    }

    @Override
    public void deletePerson(Person target) {
        throw new AssertionError("This method should not be called.");
    }

    @Override
    public void setPerson(Person target, Person editedPerson) {
        throw new AssertionError("This method should not be called.");
    }

    @Override
    public ObservableList<Person> getFilteredPersonList() {
        throw new AssertionError("This method should not be called.");
    }

    @Override
    public void updateFilteredPersonList(Predicate<Person> predicate) {
        throw new AssertionError("This method should not be called.");
    }

    @Override
    public boolean hasExam(Exam exam) {
        throw new AssertionError("This method should not be called.");
    }

    @Override
    public void deleteExam(Exam target) {
        throw new AssertionError("This method should not be called.");
    }

    @Override
    public void addExam(Exam exam) {
        throw new AssertionError("This method should not be called.");
    }

    @Override
    public ObservableList<Person> getPersonByEmail(String email) {
        throw new AssertionError("This method should not be called.");
    }

    @Override
    public ObservableList<Exam> getExamByName(String examName) {
        throw new AssertionError("This method should not be called.");
    }

    @Override
    public void selectExam(Exam target) {
        throw new AssertionError("This method should not be called.");
    }

    @Override
    public void deselectExam() {
        throw new AssertionError("This method should not be called.");
    }

    @Override
    public ObservableValue<Exam> getSelectedExam() {
        throw new AssertionError("This method should not be called.");
    }

    @Override
    public ObservableList<Exam> getExamList() {
        throw new AssertionError("This method should not be called.");
    }

    @Override
    public void addExamScoreToPerson(Person target, Exam exam, Score score) {
        throw new AssertionError("This method should not be called.");
    }

    @Override
    public void removeExamScoreFromPerson(Person target, Exam exam) {
        throw new AssertionError("This method should not be called.");
    }

    @Override
    public ObservableValue<ScoreStatistics> getSelectedExamStatistics() {
        throw new AssertionError("This method should not be called.");
    }

}

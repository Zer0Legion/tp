package seedu.address.model;

import java.nio.file.Path;
import java.util.function.Predicate;

import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import seedu.address.commons.core.GuiSettings;
import seedu.address.model.exam.Exam;
import seedu.address.model.person.Person;
import seedu.address.model.person.Score;

/**
 * The API of the Model component.
 */
public interface Model {
    /** {@code Predicate} that always evaluate to true */
    Predicate<Person> PREDICATE_SHOW_ALL_PERSONS = unused -> true;

    /**
     * Replaces user prefs data with the data in {@code userPrefs}.
     */
    void setUserPrefs(ReadOnlyUserPrefs userPrefs);

    /**
     * Returns the user prefs.
     */
    ReadOnlyUserPrefs getUserPrefs();

    /**
     * Returns the user prefs' GUI settings.
     */
    GuiSettings getGuiSettings();

    /**
     * Sets the user prefs' GUI settings.
     */
    void setGuiSettings(GuiSettings guiSettings);

    /**
     * Returns the user prefs' address book file path.
     */
    Path getAddressBookFilePath();

    /**
     * Sets the user prefs' address book file path.
     */
    void setAddressBookFilePath(Path addressBookFilePath);

    /**
     * Replaces address book data with the data in {@code addressBook}.
     */
    void setAddressBook(ReadOnlyAddressBook addressBook);

    /** Returns the AddressBook */
    ReadOnlyAddressBook getAddressBook();

    /**
     * Returns true if a person with the same identity as {@code person} exists in the address book.
     */
    boolean hasPerson(Person person);

    /**
     * Deletes the given person.
     * The person must exist in the address book.
     */
    void deletePerson(Person target);

    /**
     * Adds the given person.
     * {@code person} must not already exist in the address book.
     */
    void addPerson(Person person);

    /**
     * Replaces the given person {@code target} with {@code editedPerson}.
     * {@code target} must exist in the address book.
     * The person identity of {@code editedPerson} must not be the same as another existing person in the address book.
     */
    void setPerson(Person target, Person editedPerson);

    /**
     * Returns a read-only observable list of persons with the given email, if any.
     * @param email The email to get the person by.
     * @return A read-only observable list of persons with the given email.
     */
    ObservableList<Person> getPersonByEmail(String email);

    /**
     * Adds an exam score to the given person {@code target},
     * maintains immutability. A new person is set into the list with the exam score added
     * @param target The person to add the exam score to.
     * @param exam The exam to add the score to.
     * @param score The score to add.
     */
    void addExamScoreToPerson(Person target, Exam exam, Score score);

    /**
     * Removes an exam from the given person {@code target},
     * maintains immutability. A new person is set into the list with the exam removed
     * @param target The person to remove the exam from.
     * @param exam The exam to remove.
     */
    void removeExamScoreFromPerson(Person target, Exam exam);

    /** Returns an unmodifiable view of the filtered person list */
    ObservableList<Person> getFilteredPersonList();

    /**
     * Updates the filter of the filtered person list to filter by the given {@code predicate}.
     * @throws NullPointerException if {@code predicate} is null.
     */
    void updateFilteredPersonList(Predicate<Person> predicate);

    /**
     * Returns true if an exam with the same identity as {@code exam} exists in the address book.
     */
    boolean hasExam(Exam exam);

    /**
     * Deletes the given exam.
     * The exam must exist in the address book.
     */
    void deleteExam(Exam target);

    /**
     * Adds the given exam.
     * {@code exam} must not already exist in the address book.
     */
    void addExam(Exam exam);

    /**
     * Selects the given exam.
     * The exam must exist in the address book.
     */
    void selectExam(Exam target);

    /**
     * Deselects the selected exam.
     */
    void deselectExam();

    /**
     * Returns the selected exam.
     */
    ObservableValue<Exam> getSelectedExam();

    /**
     * Returns the exam with the given name.
     * @param examName The name of the exam to get.
     * @return The exam with the given name.
     */
    ObservableList<Exam> getExamByName(String examName);

    /** Returns an unmodifiable view of the filtered exam list */
    ObservableList<Exam> getExamList();

    /**
     * Returns the exam score statistics for the given exam.
     */
    ObservableValue<ScoreStatistics> getSelectedExamStatistics();
}

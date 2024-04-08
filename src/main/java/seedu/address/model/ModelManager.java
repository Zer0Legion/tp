package seedu.address.model;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.CollectionUtil.requireAllNonNull;

import java.nio.file.Path;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import seedu.address.commons.core.GuiSettings;
import seedu.address.commons.core.LogsCenter;
import seedu.address.model.exam.Exam;
import seedu.address.model.person.Person;
import seedu.address.model.person.Score;

/**
 * Represents the in-memory model of the address book data.
 */
public class ModelManager implements Model {
    private static final Logger logger = LogsCenter.getLogger(ModelManager.class);

    private final AddressBook addressBook;
    private final UserPrefs userPrefs;
    private final FilteredList<Person> filteredPersons;
    private final SimpleObjectProperty<Exam> selectedExam;
    private final SimpleObjectProperty<ScoreStatistics> selectedExamStatistics;

    /**
     * Initializes a ModelManager with the given addressBook and userPrefs.
     */
    public ModelManager(ReadOnlyAddressBook addressBook, ReadOnlyUserPrefs userPrefs) {
        requireAllNonNull(addressBook, userPrefs);

        logger.fine("Initializing with address book: " + addressBook + " and user prefs " + userPrefs);

        this.addressBook = new AddressBook(addressBook);
        this.userPrefs = new UserPrefs(userPrefs);
        filteredPersons = new FilteredList<>(this.addressBook.getPersonList());
        selectedExam = new SimpleObjectProperty<>(null);
        selectedExamStatistics = new SimpleObjectProperty<>(null);
    }

    public ModelManager() {
        this(new AddressBook(), new UserPrefs());
    }

    //=========== UserPrefs ==================================================================================

    @Override
    public void setUserPrefs(ReadOnlyUserPrefs userPrefs) {
        requireNonNull(userPrefs);
        this.userPrefs.resetData(userPrefs);
    }

    @Override
    public ReadOnlyUserPrefs getUserPrefs() {
        return userPrefs;
    }

    @Override
    public GuiSettings getGuiSettings() {
        return userPrefs.getGuiSettings();
    }

    @Override
    public void setGuiSettings(GuiSettings guiSettings) {
        requireNonNull(guiSettings);
        userPrefs.setGuiSettings(guiSettings);
    }

    @Override
    public Path getAddressBookFilePath() {
        return userPrefs.getAddressBookFilePath();
    }

    @Override
    public void setAddressBookFilePath(Path addressBookFilePath) {
        requireNonNull(addressBookFilePath);
        userPrefs.setAddressBookFilePath(addressBookFilePath);
    }

    //=========== AddressBook ================================================================================

    @Override
    public void setAddressBook(ReadOnlyAddressBook addressBook) {
        this.addressBook.resetData(addressBook);
    }

    @Override
    public ReadOnlyAddressBook getAddressBook() {
        return addressBook;
    }

    @Override
    public boolean hasPerson(Person person) {
        requireNonNull(person);
        return addressBook.hasPerson(person);
    }

    @Override
    public void deletePerson(Person target) {
        addressBook.removePerson(target);
    }

    @Override
    public void addPerson(Person person) {
        addressBook.addPerson(person);
        updateFilteredPersonList(PREDICATE_SHOW_ALL_PERSONS);
    }

    @Override
    public void setPerson(Person target, Person editedPerson) {
        requireAllNonNull(target, editedPerson);

        addressBook.setPerson(target, editedPerson);
    }

    @Override
    public ObservableList<Person> getPersonByEmail(String email) {
        return addressBook.getPersonByEmail(email);
    }

    @Override
    public void addExamScoreToPerson(Person person, Exam exam, Score score) {
        Person newPerson = person.addExamScore(exam, score);
        setPerson(person, newPerson);
        updateSelectedExamStatistics();

    }

    @Override
    public void removeExamScoreFromPerson(Person person, Exam exam) {
        Person newPerson = person.removeExam(exam);
        setPerson(person, newPerson);
        updateSelectedExamStatistics();
    }

    //=========== Filtered Person List Accessors =============================================================

    /**
     * Returns an unmodifiable view of the list of {@code Person} backed by the internal list of
     * {@code versionedAddressBook}
     */
    @Override
    public ObservableList<Person> getFilteredPersonList() {
        return filteredPersons;
    }

    @Override
    public void updateFilteredPersonList(Predicate<Person> predicate) {
        requireNonNull(predicate);
        filteredPersons.setPredicate(predicate);
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof ModelManager)) {
            return false;
        }

        ModelManager otherModelManager = (ModelManager) other;
        return addressBook.equals(otherModelManager.addressBook)
                && userPrefs.equals(otherModelManager.userPrefs)
                && filteredPersons.equals(otherModelManager.filteredPersons);
    }

    //=========== Exam ================================================================================

    @Override
    public boolean hasExam(Exam exam) {
        requireNonNull(exam);
        return addressBook.hasExam(exam);
    }

    @Override
    public void deleteExam(Exam target) {
        addressBook.removeExam(target);
        for (Person person : addressBook.getPersonList()) {
            if (person.hasExamScore(target)) {
                removeExamScoreFromPerson(person, target);
            }
        }
        if (selectedExam.getValue() != null && selectedExam.getValue().equals(target)) {
            deselectExam();
        }
    }

    @Override
    public void addExam(Exam exam) {
        addressBook.addExam(exam);
    }

    @Override
    public ObservableList<Exam> getExamList() {
        return addressBook.getExamList();
    }

    @Override
    public void selectExam(Exam target) {
        requireNonNull(target);
        selectedExam.set(target);
        updateSelectedExamStatistics();
    }

    @Override
    public void deselectExam() {
        selectedExam.set(null);
        updateSelectedExamStatistics();
    }

    /**
     * Returns a view of the selected exam. (For updating UI purposes)
     */
    @Override
    public ObservableValue<Exam> getSelectedExam() {
        return selectedExam;
    }

    @Override
    public ObservableValue<ScoreStatistics> getSelectedExamStatistics() {
        updateSelectedExamStatistics();
        return selectedExamStatistics;
    }

    private void updateSelectedExamStatistics() {
        if (selectedExam.getValue() == null) {
            selectedExamStatistics.set(null);
            return;
        }
        selectedExamStatistics.set(getExamScoreStatistics(selectedExam.getValue()));
    }

    private ScoreStatistics getExamScoreStatistics(Exam exam) {
        // Get all scores for the exam that exist in the filtered persons
        List<Score> scores = filteredPersons.stream()
            .map(person -> person.getScores().get(exam))
            .filter(Objects::nonNull)
            .collect(Collectors.toList());

        if (scores.isEmpty()) {
            return new ScoreStatistics();
        }

        double sum = scores.stream().mapToDouble(Score::getScore).sum();
        double mean = sum / scores.size(); //Division by zero is handled by the if statement above
        double median = getMedian(scores);

        return new ScoreStatistics(mean, median);
    }

    private double getMedian(List<Score> scores) {
        Collections.sort(scores);
        int size = scores.size();
        if (size % 2 == 0) {
            return (scores.get(size / 2 - 1).getScore() + scores.get(size / 2).getScore()) / 2.0;
        } else {
            return scores.get(size / 2).getScore();
        }
    }

    @Override
    public ObservableList<Exam> getExamByName(String examName) {
        return addressBook.getExamByName(examName);
    }

}

package seedu.address.model.exam;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.AppUtil.checkArgument;

import seedu.address.logic.parser.ParserUtil;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.person.Score;

/**
 * Represents an Exam in the address book.
 * Guarantees: immutable; name is valid as declared in {@link #isValidName(String)}
 */
public class Exam {

    public static final String MESSAGE_CONSTRAINTS = "Names should only contain alphanumeric characters and spaces "
                                                      + "up to 160 characters, and it should not be blank. "
                                                      + "Exam Score must be greater than zero and "
                                                      + "can have up to 2 decimal places.";

    /*
     * The first character of the name must not be a whitespace,
     * otherwise " " (a blank string) becomes a valid input.
     * The rest of the string can contain any alphanumeric character and spaces.
     */
    public static final String VALIDATION_REGEX_EXAM_NAME = "[\\p{Alnum}][\\p{Alnum} ]{1,159}";

    public final String name;
    public final Score maxScore;
    /**
     * Constructs an {@code Exam}.
     *
     * @param name A valid name.
     * @param maxScore A valid max score.
     */
    public Exam(String name, Score maxScore) {
        requireNonNull(name);
        checkArgument(isValidExamName(name), MESSAGE_CONSTRAINTS);
        this.name = name;
        this.maxScore = maxScore;
    }

    public String getName() {
        return name;
    }

    public Score getMaxScore() {
        return maxScore;
    }

    /**
     * Returns true if both exams have the same name.
     * This defines a weaker notion of equality between two exams.
     */
    public boolean isSameExam(Exam otherExam) {
        if (otherExam == this) {
            return true;
        }

        return otherExam != null
                && otherExam.getName().equals(getName());
    }

    /**
     * Returns if a given string is a valid name.
     */
    public static boolean isValidExamName(String test) {
        return test.matches(VALIDATION_REGEX_EXAM_NAME);
    }

    /**
     * Returns true if a given string is a valid exam score.
     */
    public static boolean isValidExamScoreString(String test) {
        try {
            Score score = ParserUtil.parseScore(test);
            return score.getScore() > 0;
        } catch (ParseException e) {
            return false;
        }
    }

    @Override
    public String toString() {
        return name + ": " + maxScore;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        if (!(other instanceof Exam)) {
            return false;
        }

        Exam otherExam = (Exam) other;
        return name.equals(otherExam.name)
                && maxScore.equals(otherExam.maxScore);
    }

    @Override
    public int hashCode() {
        return name.hashCode() + maxScore.hashCode();
    }
}

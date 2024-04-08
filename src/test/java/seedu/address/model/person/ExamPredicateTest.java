package seedu.address.model.person;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.parser.CliSyntax.PREFIX_LESS_THAN;
import static seedu.address.logic.parser.CliSyntax.PREFIX_MORE_THAN;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NAME;

import org.junit.jupiter.api.Test;

import seedu.address.commons.util.ToStringBuilder;
import seedu.address.testutil.TypicalPersons;

public class ExamPredicateTest {

    @Test
    public void equals() {
        String firstPredicateKeyword = "55";
        String secondPredicateKeyword = "45";

        ExamPredicate firstPredicate =
                new ExamPredicate(
                        PREFIX_LESS_THAN, firstPredicateKeyword, TypicalPersons.MIDTERM);
        ExamPredicate secondPredicate =
                new ExamPredicate(
                        PREFIX_LESS_THAN, secondPredicateKeyword, TypicalPersons.MIDTERM);
        ExamPredicate thirdPredicate =
                new ExamPredicate(
                        PREFIX_MORE_THAN, firstPredicateKeyword, TypicalPersons.MIDTERM);
        ExamPredicate fourthPredicate =
                new ExamPredicate(
                        PREFIX_MORE_THAN, secondPredicateKeyword, TypicalPersons.MIDTERM);

        // same object -> returns true
        assertEquals(firstPredicate, firstPredicate);

        // different types -> returns false
        assertFalse(firstPredicate.equals(1));

        // null -> returns false
        assertFalse(firstPredicate.equals(null));

        // same values -> returns true
        ExamPredicate firstPredicateCopy =
                new ExamPredicate(
                        PREFIX_LESS_THAN, firstPredicateKeyword, TypicalPersons.MIDTERM);

        assertEquals(firstPredicate, firstPredicateCopy);

        // same prefix, different keyword -> returns false
        assertFalse(firstPredicate.equals(secondPredicate));

        // same keyword, different prefix -> returns false
        assertFalse(firstPredicate.equals(thirdPredicate));

        // different prefix and keyword -> returns false
        assertFalse(firstPredicate.equals(fourthPredicate));
    }

    @Test
    public void toString_validParams_returnsCorrectString() {
        String keyword = "55";
        ExamPredicate predicate =
                new ExamPredicate(
                        PREFIX_LESS_THAN, keyword, TypicalPersons.MIDTERM);

        String expectedString = new ToStringBuilder("seedu.address.model.person."
                + "ExamPredicate")
                .add("prefix", PREFIX_LESS_THAN)
                .add("keyword", keyword)
                .add("exam", TypicalPersons.MIDTERM)
                .toString();

        assertEquals(expectedString, predicate.toString());
    }

    @Test
    public void test_prefixGreaterThanKeyword_returnsTrue() {
        // One keyword
        ExamPredicate predicate =
                new ExamPredicate(
                        PREFIX_MORE_THAN, "55", TypicalPersons.MIDTERM);

        // keyword matches score
        assertTrue(predicate.test(TypicalPersons.FIONA));

        // keyword does not match score
        assertFalse(predicate.test(TypicalPersons.ELLE));
    }

    @Test
    public void test_examNotFound_returnsFalse() {
        // One keyword
        ExamPredicate predicate =
                new ExamPredicate(
                        PREFIX_MORE_THAN, "55", TypicalPersons.QUIZ);

        // keyword matches score for different exam
        assertFalse(predicate.test(TypicalPersons.FIONA));

        // keyword does not match score for different exam
        assertFalse(predicate.test(TypicalPersons.ELLE));
    }

    @Test
    public void test_wrongPrefix_returnsFalse() {
        // One keyword
        ExamPredicate predicate =
                new ExamPredicate(
                PREFIX_NAME, "55", TypicalPersons.MIDTERM);

        // should not match
        assertFalse(predicate.test(TypicalPersons.FIONA));
    }

    @Test
    public void isExamRequired_validPrefixes_returnsTrue() {
        ExamPredicate predicate =
                new ExamPredicate(
                        PREFIX_MORE_THAN, "55", TypicalPersons.MIDTERM);

        assertTrue(predicate.isExamRequired());
    }

    @Test
    public void isExamRequired_invalidPrefixes_returnsFalse() {
        ExamPredicate predicate =
                new ExamPredicate(
                PREFIX_NAME, "55", TypicalPersons.MIDTERM);

        assertFalse(predicate.isExamRequired());
    }
}

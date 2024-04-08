package seedu.address.logic.parser;

import static java.util.Objects.requireNonNull;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import seedu.address.commons.core.index.Index;
import seedu.address.commons.util.StringUtil;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.exam.Exam;
import seedu.address.model.person.Address;
import seedu.address.model.person.Email;
import seedu.address.model.person.Name;
import seedu.address.model.person.Phone;
import seedu.address.model.person.Score;
import seedu.address.model.student.Matric;
import seedu.address.model.student.Reflection;
import seedu.address.model.student.Studio;
import seedu.address.model.tag.Tag;

/**
 * Contains utility methods used for parsing strings in the various *Parser classes.
 */
public class ParserUtil {

    public static final String MESSAGE_INVALID_INDEX = "Index is not a non-zero unsigned integer.";

    /**
     * Parses {@code oneBasedIndex} into an {@code Index} and returns it. Leading and trailing whitespaces will be
     * trimmed.
     * @throws ParseException if the specified index is invalid (not non-zero unsigned integer).
     */
    public static Index parseIndex(String oneBasedIndex) throws ParseException {
        String trimmedIndex = oneBasedIndex.trim();
        if (!StringUtil.isNonZeroUnsignedInteger(trimmedIndex)) {
            throw new ParseException(MESSAGE_INVALID_INDEX);
        }
        return Index.fromOneBased(Integer.parseInt(trimmedIndex));
    }

    /**
     * Parses {@code matric} into an {@code Matric} and returns it. Leading and trailing whitespaces will be trimmed.
     * @param matric the matric number to be parsed
     * @return the parsed matric number
     * @throws ParseException if the specified matric number is invalid
     */
    public static Matric parseMatric(String matric) throws ParseException {
        String trimmedMatric = matric.trim();
        if (!Matric.isValidMatric(trimmedMatric)) {
            throw new ParseException(Matric.MESSAGE_CONSTRAINTS);
        }
        return new Matric(trimmedMatric);
    }

    /**
     * Parses {@code matric} in the context of an Edit command where blank Matrics are accepted.
     * @param matric the matric number to be parsed
     * @return the parsed matric number
     * @throws ParseException if the specified matric number is invalid
     */
    public static Matric parseMatricForEdit(String matric) throws ParseException {
        String trimmedMatric = matric.trim();
        if (!Matric.isValidConstructorParam(trimmedMatric)) {
            throw new ParseException(Matric.MESSAGE_CONSTRAINTS);
        }
        return new Matric(trimmedMatric);
    }

    /**
     * Parses {@code reflection} into a {@code Reflection} and returns it. Leading and trailing whitespaces will be
     * trimmed.
     * @param reflection the reflection to be parsed
     * @return the parsed reflection
     * @throws ParseException if the specified reflection is invalid
     */
    public static Reflection parseReflection(String reflection) throws ParseException {
        requireNonNull(reflection);
        String trimmedReflection = reflection.trim();
        if (!Reflection.isValidReflection(trimmedReflection)) {
            throw new ParseException(Reflection.MESSAGE_CONSTRAINTS);
        }
        return new Reflection(trimmedReflection);
    }

    /**
     * Parses {@code reflection} in the context of an Edit command where blank Reflections are accepted.
     * @param reflection the reflection to be parsed
     * @return the parsed reflection
     * @throws ParseException if the specified reflection is invalid
     */
    public static Reflection parseReflectionForEdit(String reflection) throws ParseException {
        requireNonNull(reflection);
        String trimmedReflection = reflection.trim();
        if (!Reflection.isValidConstructorParam(trimmedReflection)) {
            throw new ParseException(Reflection.MESSAGE_CONSTRAINTS);
        }
        return new Reflection(trimmedReflection);
    }

    /**
     * Parses {@code studio} into a {@code Studio} and returns it. Leading and trailing whitespaces will be trimmed.
     * @param studio the studio to be parsed
     * @return the parsed studio
     * @throws ParseException if the specified studio is invalid
     */
    public static Studio parseStudio(String studio) throws ParseException {
        requireNonNull(studio);
        String trimmedStudio = studio.trim();
        if (!Studio.isValidStudio(trimmedStudio)) {
            throw new ParseException(Studio.MESSAGE_CONSTRAINTS);
        }
        return new Studio(trimmedStudio);
    }

    /**
     * Parses {@code studio} in the context of an Edit command where blank Studios are accepted.
     * @param studio the studio to be parsed
     * @return the parsed studio
     * @throws ParseException if the specified studio is invalid
     */
    public static Studio parseStudioForEdit(String studio) throws ParseException {
        requireNonNull(studio);
        String trimmedStudio = studio.trim();
        if (!Studio.isValidConstructorParam(trimmedStudio)) {
            throw new ParseException(Studio.MESSAGE_CONSTRAINTS);
        }
        return new Studio(trimmedStudio);
    }

    /**
     * Parses a {@code String name} into a {@code Name}.
     * Leading and trailing whitespaces will be trimmed.
     *
     * @throws ParseException if the given {@code name} is invalid.
     */
    public static Name parseName(String name) throws ParseException {
        requireNonNull(name);
        String trimmedName = name.trim();
        if (!Name.isValidName(trimmedName)) {
            throw new ParseException(Name.MESSAGE_CONSTRAINTS);
        }
        return new Name(trimmedName);
    }

    /**
     * Parses a {@code String phone} into a {@code Phone}.
     * Leading and trailing whitespaces will be trimmed.
     *
     * @throws ParseException if the given {@code phone} is invalid.
     */
    public static Phone parsePhone(String phone) throws ParseException {
        requireNonNull(phone);
        String trimmedPhone = phone.trim();
        if (!Phone.isValidPhone(trimmedPhone)) {
            throw new ParseException(Phone.MESSAGE_CONSTRAINTS);
        }
        return new Phone(trimmedPhone);
    }

    /**
     * Parses a {@code String address} into an {@code Address}.
     * Leading and trailing whitespaces will be trimmed.
     *
     * @throws ParseException if the given {@code address} is invalid.
     */
    public static Address parseAddress(String address) throws ParseException {
        requireNonNull(address);
        String trimmedAddress = address.trim();
        if (!Address.isValidAddress(trimmedAddress)) {
            throw new ParseException(Address.MESSAGE_CONSTRAINTS);
        }
        return new Address(trimmedAddress);
    }

    /**
     * Parses a {@code String email} into an {@code Email}.
     * Leading and trailing whitespaces will be trimmed.
     *
     * @throws ParseException if the given {@code email} is invalid.
     */
    public static Email parseEmail(String email) throws ParseException {
        requireNonNull(email);
        String trimmedEmail = email.trim();
        if (!Email.isValidEmail(trimmedEmail)) {
            throw new ParseException(Email.MESSAGE_CONSTRAINTS);
        }
        return new Email(trimmedEmail);
    }

    /**
     * Parses a {@code String tag} into a {@code Tag}.
     * Leading and trailing whitespaces will be trimmed.
     *
     * @throws ParseException if the given {@code tag} is invalid.
     */
    public static Tag parseTag(String tag) throws ParseException {
        requireNonNull(tag);
        String trimmedTag = tag.trim();
        if (!Tag.isValidTagName(trimmedTag)) {
            throw new ParseException(Tag.MESSAGE_CONSTRAINTS);
        }
        return new Tag(trimmedTag);
    }

    /**
     * Parses {@code Collection<String> tags} into a {@code Set<Tag>}.
     */
    public static Set<Tag> parseTags(Collection<String> tags) throws ParseException {
        requireNonNull(tags);
        final Set<Tag> tagSet = new HashSet<>();
        for (String tagName : tags) {
            tagSet.add(parseTag(tagName));
        }
        return tagSet;
    }

    /**
     * Parses a {@code String filePath} into a {@code filePath}.
     * Leading and trailing whitespaces will be trimmed.
     *
     * @throws ParseException if the given {@code filePath} is invalid.
     */
    public static Path parseFilePath(String filePath) throws ParseException {
        requireNonNull(filePath);
        String trimmedTag = filePath.trim();
        // add error handling for what to do when invalid input is passed
        return Paths.get(trimmedTag);
    }

    /**
     * Parses a {@code String score} into an {@code int}.
     * Leading and trailing whitespaces will be trimmed.
     *
     * @throws ParseException if the given {@code score} is invalid.
     */
    public static Score parseScore(String score) throws ParseException {
        requireNonNull(score);
        String trimmedScore = score.trim();
        if (!Score.isValidScoreString(trimmedScore)) {
            throw new ParseException(Score.MESSAGE_CONSTRAINTS);
        }

        double parsedScore = Double.parseDouble(trimmedScore);

        return new Score(parsedScore);
    }

    /**
     * Parses a {@code String examScore} into an {@code int}.
     * Leading and trailing whitespaces will be trimmed.
     *
     * @throws ParseException if the given {@code examScore} is invalid.
     */
    public static Score parseExamScore(String score) throws ParseException {
        requireNonNull(score);
        String trimmedScore = score.trim();
        if (!Exam.isValidExamScoreString(trimmedScore)) {
            throw new ParseException(Exam.MESSAGE_CONSTRAINTS);
        }

        double parsedScore = Double.parseDouble(trimmedScore);
        return new Score(parsedScore);
    }

    /**
     * Parses a {@code String examName} into a {@code String}.
     * Leading and trailing whitespaces will be trimmed.
     *
     * @throws ParseException if the given {@code examName} is invalid.
     */
    public static String parseExamName(String examName) throws ParseException {
        requireNonNull(examName);
        String trimmedExamName = examName.trim();
        if (!Exam.isValidExamName(trimmedExamName)) {
            throw new ParseException(Exam.MESSAGE_CONSTRAINTS);
        }
        return trimmedExamName;
    }
}

package seedu.address.storage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.model.exam.Exam;
import seedu.address.model.person.Address;
import seedu.address.model.person.Email;
import seedu.address.model.person.Name;
import seedu.address.model.person.Person;
import seedu.address.model.person.Phone;
import seedu.address.model.person.Score;
import seedu.address.model.student.Matric;
import seedu.address.model.student.Reflection;
import seedu.address.model.student.Studio;
import seedu.address.model.tag.Tag;

/**
 * Jackson-friendly version of {@link Person}.
 */
class JsonAdaptedPerson {

    public static final String MISSING_FIELD_MESSAGE_FORMAT = "Person's %s field is missing!";

    private final String name;
    private final String phone;
    private final String email;
    private final String address;
    private final List<JsonAdaptedTag> tags = new ArrayList<>();
    private final String matric;
    private final String reflection;
    private final String studio;
    private final List<JsonAdaptedExamScore> examScores = new ArrayList<>();

    /**
     * Constructs a {@code JsonAdaptedPerson} with the given person details.
     */
    @JsonCreator
    public JsonAdaptedPerson(@JsonProperty("name") String name, @JsonProperty("phone") String phone,
            @JsonProperty("email") String email, @JsonProperty("address") String address,
            @JsonProperty("tags") List<JsonAdaptedTag> tags, @JsonProperty("matric") String matric,
            @JsonProperty("reflection") String reflection,
            @JsonProperty("studio") String studio,
            @JsonProperty("examScores") List<JsonAdaptedExamScore> examScores) {
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.address = address;
        if (tags != null) {
            this.tags.addAll(tags);
        }
        this.matric = matric;
        this.reflection = reflection;
        this.studio = studio;
        if (examScores != null) {
            this.examScores.addAll(examScores);
        }
    }

    /**
     * Converts a given {@code Person} into this class for Jackson use.
     */
    public JsonAdaptedPerson(Person source) {
        name = source.getName().fullName;
        phone = source.getPhone().value;
        email = source.getEmail().value;
        address = source.getAddress().value;
        tags.addAll(source.getTags().stream()
                .map(JsonAdaptedTag::new)
                .collect(Collectors.toList()));
        matric = source.getMatric().matricNumber;
        reflection = source.getReflection().reflection;
        studio = source.getStudio().studio;
        examScores.addAll(source.getScores().entrySet().stream()
                .map(entry -> new JsonAdaptedExamScore(entry.getKey().getName(),
                                                       Double.toString(entry.getKey().getMaxScore().getScore()),
                                                       Double.toString(entry.getValue().getScore())))
                .collect(Collectors.toList()));
    }

    /**
     * Converts this Jackson-friendly adapted person object into the model's {@code Person} object.
     *
     * @throws IllegalValueException if there were any data constraints violated in the adapted person.
     */
    public Person toModelType() throws IllegalValueException {
        final List<Tag> personTags = new ArrayList<>();
        for (JsonAdaptedTag tag : tags) {
            personTags.add(tag.toModelType());
        }

        final Map<Exam, Score> personExamScores = new HashMap<>();
        for (JsonAdaptedExamScore examScore : examScores) {
            personExamScores.put(examScore.toModelTypeExam(), examScore.toModelTypeScore());
        }

        if (name == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT, Name.class.getSimpleName()));
        }
        if (!Name.isValidName(name)) {
            throw new IllegalValueException(Name.MESSAGE_CONSTRAINTS);
        }
        final Name modelName = new Name(name);

        if (phone == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT, Phone.class.getSimpleName()));
        }
        if (!Phone.isValidPhone(phone)) {
            throw new IllegalValueException(Phone.MESSAGE_CONSTRAINTS);
        }
        final Phone modelPhone = new Phone(phone);

        if (email == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT, Email.class.getSimpleName()));
        }
        if (!Email.isValidEmail(email)) {
            throw new IllegalValueException(Email.MESSAGE_CONSTRAINTS);
        }
        final Email modelEmail = new Email(email);

        if (address == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT, Address.class.getSimpleName()));
        }
        if (!Address.isValidAddress(address)) {
            throw new IllegalValueException(Address.MESSAGE_CONSTRAINTS);
        }
        if (!Matric.isValidConstructorParam(matric)) {
            throw new IllegalValueException(Matric.MESSAGE_CONSTRAINTS);
        }
        if (!Reflection.isValidConstructorParam(reflection)) {
            throw new IllegalValueException(Reflection.MESSAGE_CONSTRAINTS);
        }
        if (!Studio.isValidConstructorParam(studio)) {
            throw new IllegalValueException(Studio.MESSAGE_CONSTRAINTS);
        }

        final Address modelAddress = new Address(address);

        final Set<Tag> modelTags = new HashSet<>(personTags);

        final Matric modelMatric = new Matric(matric);

        final Reflection modelReflection = new Reflection(reflection);

        final Studio modelStudio = new Studio(studio);

        final Map<Exam, Score> scores = new HashMap<>(personExamScores);

        return new Person(modelName, modelPhone, modelEmail, modelAddress,
                          modelTags, modelMatric, modelReflection, modelStudio, scores);
    }

}

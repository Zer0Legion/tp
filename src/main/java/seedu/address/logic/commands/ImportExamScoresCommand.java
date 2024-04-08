package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.parser.CliSyntax.PREFIX_IMPORT;

import java.nio.file.Path;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import javafx.collections.ObservableList;
import seedu.address.commons.util.CsvUtil;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.exam.Exam;
import seedu.address.model.person.Person;
import seedu.address.model.person.Score;

/**
 * Imports a CSV file containing details of existing exams into the application.
 */
public class ImportExamScoresCommand extends Command {

    public static final String COMMAND_WORD = "importExamScores";
    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Imports exam scores from specified filepath."
            + " Must be an absolute CSV file path without any inverted commas. Parameter: "
            + PREFIX_IMPORT + "FILEPATH\n"
            + "Example: " + COMMAND_WORD + " " + PREFIX_IMPORT + "C:usr/lib/text.csv";
    public static final String MESSAGE_SCORE_NOT_NUMBER = "Score for %s is not a valid score";
    public static final String MESSAGE_PERSON_DOES_NOT_EXIST = "Person with the given email does not exist";
    public static final String MESSAGE_SUCCESS = "Imported exam scores from: %s";
    public static final String MESSAGE_DUPLICATE_EXAM =
            "Duplicate exam header. Only the records of the first exam will be imported if present.";
    public static final String EXAM_CSV_HEADER = "Exam:";
    public static final String PREFIX_ERROR_REPORT =
            "\nBelow are the errors for any rows that were skipped:\n";
    public static final String MESSAGE_EXAM_DOES_NOT_EXIST = "Exam does not exist";
    public static final String MESSAGE_GRADE_TOO_HIGH = "Grade for %s exceeds maximum score";
    public static final String HEADER_EMAIL = "email";
    public static final String HEADER_EXAM = "exam";
    public static final String ERROR_EMAIL_FIRST_VALUE =
            "Please ensure that the email column exists only in the first column in the CSV file, without duplicates.";
    public static final String ERROR_WRONG_CSV_FORMAT = "Please ensure that the CSV file is in the correct format.\n"
            + "The first row should contain the headers of the exams and the first column should contain the emails.\n"
            + "There should be no empty cells in the CSV file.";
    public static final String ERROR_BUILDER = "%s of %s: %s\n";
    private StringBuilder errorReport;
    private Path filepath;

    /**
     * Creates an ImportExamCommand to import exams from the specified file path.
     * @param filePath the path of the file
     */
    public ImportExamScoresCommand(Path filePath) {
        this.errorReport = new StringBuilder();
        this.filepath = filePath;
    }

    // Error report methods

    /**
     * Adds an error to the error report.
     * @param field the offending field
     * @param subject the subject of the error
     * @param error the error message
     */
    void addToErrorReport(String field, String subject, String error) {
        errorReport.append(String.format(ERROR_BUILDER, field, subject, error));
    }

    /**
     * Generates the error report.
     * @return the error report
     */
    String generateErrorReport() {
        String errorReportString = errorReport.toString();
        if (errorReportString.isBlank()) {
            return "";
        } else {
            return PREFIX_ERROR_REPORT + errorReportString;
        }
    }

    private void detectDuplicateExamHeaders(List<String[]> lst) {
        String[] examNames = getExamNames(lst);
        HashSet<String> uniqueExamNames = new HashSet<>();
        for (int i = 1; i < examNames.length; i++) {
            if (!uniqueExamNames.add(examNames[i]) && isExam(examNames[i])) {
                addToErrorReport(
                        HEADER_EXAM, getExamName(examNames[i]), MESSAGE_DUPLICATE_EXAM);
            }
        }
    }

    // Exam mapping methods

    /**
     * Reorganizes the parsed CSV data into a mapping of individual results from exams.
     * @param lst the list of String arrays representing the lines of the CSV file
     * @return a mapping of exam names to a mapping of student emails to their respective scores
     */
    private HashMap<String, HashMap<String, Double>> createExamsMapping(List<String[]> lst) {
        HashMap<String, HashMap<String, Double>> map = new HashMap<>();

        // Check if there is at least a header row within the CSV file.
        if (hasHeader(lst)) {
            detectDuplicateExamHeaders(lst);
            createExamNameHeaders(lst, map);
            updateExamResults(lst, map);
        }
        return map;
    }

    private void createExamNameHeaders(List<String[]> lst, HashMap<String, HashMap<String, Double>> map) {
        String[] examNames = getExamNames(lst);

        for (int i = 1; i < examNames.length; i++) {
            map.put(examNames[i], new HashMap<>());
        }
    }

    private void updateExamResults(List<String[]> lst, HashMap<String, HashMap<String, Double>> map) {
        String[] examNames = getExamNames(lst);

        for (int i = 1; i < lst.size(); i++) {
            String[] row = lst.get(i);
            updateExamResult(map, examNames, row);
        }
    }

    private void updateExamResult(HashMap<String, HashMap<String, Double>> map, String[] examNames, String[] row) {
        if (hasEmail(row)) {
            String email = row[0];
            addRows(map, examNames, row, email);
        }
    }

    private void addRows(
            HashMap<String, HashMap<String, Double>> map, String[] examNames, String[] row, String email) {
        for (int j = 1; j < row.length; j++) {
            addRow(map, examNames, row, email, j);
        }
    }

    private void addRow(
            HashMap<String, HashMap<String, Double>> map, String[] examNames, String[] row, String email, int j) {
        if (Score.isValidScoreString(row[j])) {
            map.get(examNames[j]).put(email, new Score(Double.parseDouble(row[j])).getScore());
        } else {
            if (isExam(examNames[j])) {
                addToErrorReport(
                        HEADER_EMAIL, email, String.format(MESSAGE_SCORE_NOT_NUMBER, getExamName(examNames[j])));
            }
        }
    }

    private void addScores(HashMap<String, HashMap<String, Double>> headers, Model model) {
        Object[] examNames = headers.keySet().toArray();

        for (int i = 0; i < examNames.length; i++) {
            ObservableList<Exam> exams = model.getExamByName(examNames[i].toString().strip());
            if (exams.size() > 0) {
                Exam exam = exams.get(0);
                insertGrades(headers, model, exam, examNames[i]);
            } else {
                addToErrorReport(HEADER_EXAM, (String) examNames[i], MESSAGE_EXAM_DOES_NOT_EXIST);
            }
        }
    }

    private void insertGrades(
            HashMap<String, HashMap<String, Double>> headers, Model model, Exam exam, Object examNames) {
        HashMap<String, Double> grades = headers.get((String) examNames);
        Object[] emails = grades.keySet().toArray();

        for (int j = 0; j < emails.length; j++) {
            String email = (String) emails[j];
            Double grade = grades.get(email);

            addScoreToPerson(model, email, exam, grade);
        }
    }

    private void addScoreToPerson(Model model, String email, Exam exam, Double grade) {
        ObservableList<Person> persons = model.getPersonByEmail(email);
        if (persons.size() > 0) {
            Person person = persons.get(0);
            if (grade <= exam.maxScore.value) {
                model.addExamScoreToPerson(person, exam, new Score(grade));
            } else {
                addToErrorReport(HEADER_EMAIL, email, String.format(MESSAGE_GRADE_TOO_HIGH, exam.getName()));
            }
        } else {
            addToErrorReport(HEADER_EMAIL, email, MESSAGE_PERSON_DOES_NOT_EXIST);
        }
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);

        List<String[]> lst = CsvUtil.readAllLinesForImportExamScores(filepath);
        System.out.println(lst);
        if (!isEmailFirstValue(lst)) {
            throw new CommandException(ERROR_EMAIL_FIRST_VALUE);
        }

        try {
            parseScoresFromRawCsv(model, lst);
        } catch (NullPointerException | ArrayIndexOutOfBoundsException e) {
            throw new CommandException(ERROR_WRONG_CSV_FORMAT);
        }

        return new CommandResult(
                String.format(MESSAGE_SUCCESS, filepath.toString()) + generateErrorReport());
    }

    private void parseScoresFromRawCsv(Model model, List<String[]> lst) throws CommandException {
        reverse(lst);
        HashMap<String, HashMap<String, Double>> headers = createExamsMapping(lst);

        HashMap<String, HashMap<String, Double>> headersForExams = removeNonExams(headers);
        addScores(headersForExams, model);
    }

    // Trivial methods

    @Override
    public boolean equals(Object other) {
        return other == this
                || (other instanceof ImportExamScoresCommand
                && filepath.equals(((ImportExamScoresCommand) other).filepath));
    }

    private boolean hasHeader(List<String[]> lst) {
        return lst.size() > 0;
    }

    private String[] getExamNames(List<String[]> lst) {
        return lst.get(0);
    }

    private boolean hasEmail(String[] row) {
        return row.length > 0;
    }

    private boolean isExam(String examName) {
        return examName.startsWith(EXAM_CSV_HEADER);
    }

    private boolean isEmailHeader(String header) {
        return header.equals(HEADER_EMAIL);
    }

    // Utility methods

    private String getExamName(String examName) {
        return examName.substring(EXAM_CSV_HEADER.length()).strip();
    }

    private HashMap<String, HashMap<String, Double>> removeNonExams(HashMap<String, HashMap<String, Double>> map) {
        HashMap<String, HashMap<String, Double>> newMap = new HashMap<>();
        for (String key : map.keySet()) {
            if (isExam(key) || isEmailHeader(key)) {
                newMap.put(getExamName(key), map.get(key));
            }
        }
        return newMap;
    }

    private void reverse(List<String[]> lst) {
        for (int i = 0; i < lst.size(); i++) {
            String[] row = lst.get(i);
            reverseRowButKeepEmail(lst, i, row);
        }
    }

    private static void reverseRowButKeepEmail(List<String[]> lst, int i, String[] row) {
        String[] reversedRow = new String[row.length];
        if (row.length > 0) {
            reversedRow[0] = row[0];
        }
        if (row.length > 1) {
            reverseElements(row, reversedRow);
        }
        lst.set(i, reversedRow);
    }

    private static void reverseElements(String[] row, String[] reversedRow) {
        for (int j = 1; j < row.length; j++) {
            reversedRow[j] = row[row.length - j];
        }
    }

    boolean isEmailFirstValue(List<String[]> lst) {
        if (!(lst.size() > 0 && lst.get(0).length > 0 && lst.get(0)[0].equals(HEADER_EMAIL))) {
            return false;
        }
        for (int i = 0; i < lst.get(0).length; i++) {
            if (i != 0 && lst.get(0)[i].equals(HEADER_EMAIL)) {
                return false;
            }
        }
        return true;
    }


}

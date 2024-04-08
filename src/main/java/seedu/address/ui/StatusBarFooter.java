package seedu.address.ui;

import java.nio.file.Path;
import java.nio.file.Paths;

import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.Region;
import seedu.address.model.ScoreStatistics;
/**
 * A ui for the status bar that is displayed at the footer of the application.
 */
public class StatusBarFooter extends UiPart<Region> {

    private static final String FXML = "StatusBarFooter.fxml";

    @FXML
    private Label saveLocationStatus;
    @FXML
    private Label gradeStatistics;
    private ObservableValue<ScoreStatistics> selectedExamStatistics;

    /**
     * Creates a {@code StatusBarFooter} with the given {@code Path}.
     */
    public StatusBarFooter(Path saveLocation, ObservableValue<ScoreStatistics> selectedExamStatistics) {
        super(FXML);
        saveLocationStatus.setText(Paths.get(".").resolve(saveLocation).toString());
        this.selectedExamStatistics = selectedExamStatistics;
    }

    /**
     * Updates the status bar to display the grade statistics of the selected exam.
     */
    public void update() {
        if (selectedExamStatistics.getValue() == null) {
            gradeStatistics.setText("");
        } else {
            gradeStatistics.setText(selectedExamStatistics.getValue().toString());
        }
    }

}

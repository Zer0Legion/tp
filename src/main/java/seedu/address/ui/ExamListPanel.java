package seedu.address.ui;

import java.util.logging.Logger;

import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.Region;
import seedu.address.commons.core.LogsCenter;
import seedu.address.model.exam.Exam;

/**
 * Panel containing the list of exams.
 */
public class ExamListPanel extends UiPart<Region> {
    private static final String FXML = "ExamListPanel.fxml";
    private final Logger logger = LogsCenter.getLogger(ExamListPanel.class);

    @FXML
    private ListView<Exam> examListView;
    private ObservableValue<Exam> selectedExam;

    /**
     * Creates a {@code ExamListPanel} with the given {@code ObservableList}.
     */
    public ExamListPanel(ObservableList<Exam> examList, ObservableValue<Exam> selectedExam) {
        super(FXML);
        examListView.setItems(examList);
        examListView.setCellFactory(listView -> new ExamListViewCell());
        examListView.setSelectionModel(new NoSelectionModel<Exam>());
        this.selectedExam = selectedExam;
    }

    public void update() {
        examListView.refresh();
    }

    /**
     * Custom {@code ListCell} that displays the graphics of a {@code Exam} using a {@code ExamCard}.
     */
    class ExamListViewCell extends ListCell<Exam> {
        @Override
        protected void updateItem(Exam exam, boolean empty) {
            super.updateItem(exam, empty);

            if (empty || exam == null) {
                setGraphic(null);
                setText(null);
            } else {
                setGraphic(new ExamCard(exam, getIndex() + 1, selectedExam).getRoot());
            }
        }
    }

}

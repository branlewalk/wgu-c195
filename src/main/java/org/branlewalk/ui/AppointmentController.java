package org.branlewalk.ui;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.fxml.LoadException;
import javafx.scene.Node;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Arrays;
import java.util.Date;
import java.util.ResourceBundle;

public class AppointmentController implements Initializable {
    @FXML
    public TextField titleField, locationField, descriptionField, urlField, contactField;
    @FXML
    public DatePicker endPicker, startPicker;
    @FXML
    public ComboBox customerComboBox, typeComboBox, endComboBox, startComboBox;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        endComboBox.setItems(createTimeList());
        startComboBox.setItems(createTimeList());
        setTime();

    }




    public void handleSaveButton(ActionEvent actionEvent) {
    }

    public void handleCancelButton(ActionEvent actionEvent) {
        final Node source = (Node) actionEvent.getSource();
        final Stage stage = (Stage) source.getScene().getWindow();
        stage.close();
    }

    private ObservableList<String> createTimeList() {
        ObservableList<String> list = FXCollections.observableArrayList();
        for (String suffix : Arrays.asList("am", "pm")) {
            for (int i : Arrays.asList(12, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11)) {
                list.add(i + ":00 " + suffix);
                list.add(i + ":30 " + suffix);
            }
        }
        return list;
    }


    public void setDate(Date date) {
        endPicker.setValue(date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
        startPicker.setValue(date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
    }

    private void setTime() {
        LocalDateTime currentTime = LocalDateTime.now();
        int hour = currentTime.getHour();
        if (hour < 12) {
            if ( hour == 0) {
                hour = 12;
            }
            startComboBox.setValue(hour+":00 am");
            endComboBox.setValue(hour+ ":30 am");
        } else {
            hour -= 12;
            if ( hour == 0) {
                hour = 12;
            }
            startComboBox.setValue(hour+":00 pm");
            endComboBox.setValue(hour+ ":30 pm");
        }
    }

}

package org.branlewalk.ui;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.branlewalk.dao.AppointmentDAO;
import org.branlewalk.dao.AppointmentDaoImpl;
import org.branlewalk.dao.CustomerDAO;
import org.branlewalk.dao.CustomerDaoImpl;
import org.branlewalk.domain.Customer;

import java.net.URL;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalTime;
import java.time.ZoneOffset;
import java.util.Date;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Arrays;
import java.util.ResourceBundle;

public class AppointmentController implements Initializable {
    private static final long THIRTY_MINUTES_MILLIS = 30 * 60 * 1000L;
    private static final long ONE_HOUR_MILLIS = 60 * 60 * 1000L;
    @FXML
    public TextField titleField, locationField, descriptionField, urlField, contactField;
    @FXML
    public DatePicker endPicker, startPicker;
    @FXML
    public ComboBox customerComboBox, typeComboBox;
    @FXML
    public ComboBox<TimeOfDay> endComboBox, startComboBox;
    private Connection connection;
    private String username;
    private AppointmentDAO appointmentDAO;

    private boolean cancel;

    public AppointmentController() throws SQLException {
        username = LoginController.username;
        connection = Main.connection();
        appointmentDAO = new AppointmentDaoImpl(connection, username);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            customerComboBox.setItems(setCustomers());
            typeComboBox.setItems(setTypes());
        } catch (SQLException e) {
            e.printStackTrace();
        }
        endComboBox.setItems(createTimeList());
        startComboBox.setItems(createTimeList());
        setTime();

    }

    public void handleSaveButton(ActionEvent actionEvent) throws SQLException {
        addAppointment();
        Main.closeWindow(actionEvent);
    }

    public void handleCancelButton(ActionEvent actionEvent) {

        Main.closeWindow(actionEvent);
        cancel = true;
    }

    private ObservableList<TimeOfDay> createTimeList() {

        ObservableList<TimeOfDay> list = FXCollections.observableArrayList();
        long millisSinceMidnight = 0;
        for (String suffix : Arrays.asList("am", "pm")) {

            for (int i : Arrays.asList(12, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11)) {

                list.add(new TimeOfDay(i + ":00 " + suffix, millisSinceMidnight));
                millisSinceMidnight += THIRTY_MINUTES_MILLIS;
                list.add(new TimeOfDay(i + ":30 " + suffix, millisSinceMidnight));
                millisSinceMidnight += THIRTY_MINUTES_MILLIS;
                System.out.println("millis equals " + millisSinceMidnight);
            }
        }
        return list;
    }

    private void addAppointment() throws SQLException {
        Date start = new java.sql.Date(Date.from(startPicker.getValue().atStartOfDay().atZone(ZoneId.systemDefault()).toInstant()).getTime() + startComboBox.getValue().getMillisSinceMidnight());
        Date end = new java.sql.Date(Date.from(endPicker.getValue().atStartOfDay().atZone(ZoneId.systemDefault()).toInstant()).getTime() + endComboBox.getValue().getMillisSinceMidnight());
        appointmentDAO.create(customerComboBox.getValue().toString(), titleField.getText(), descriptionField.getText(), locationField.getText(), contactField.getText(), typeComboBox.getValue().toString(), urlField.getText(), start, end);
    }

    public void setDate(Date date) {
        endPicker.setValue(date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
        startPicker.setValue(date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
    }

    private void setTime() {
        LocalDateTime currentTime = LocalDateTime.now();
        int hour = currentTime.getHour();

        long millisSinceMidnight = ONE_HOUR_MILLIS * hour;
        if (hour < 12) {
            setTimeOfDay(hour, millisSinceMidnight);
        } else {
            hour -= 12;
            setTimeOfDay(hour, millisSinceMidnight);
        }
    }

    private void setTimeOfDay(int hour, long millisSinceMidnight) {
        if (hour == 0) {
            hour = 12;
        }
        startComboBox.setValue(new TimeOfDay(hour + ":00 am", millisSinceMidnight));
        endComboBox.setValue(new TimeOfDay(hour + ":30 am", millisSinceMidnight + THIRTY_MINUTES_MILLIS));
    }

    private ObservableList<String> setCustomers() throws SQLException {
        CustomerDAO customerDAO = new CustomerDaoImpl(connection, username);
        ObservableList<Customer> customers = customerDAO.findAll();
        ObservableList<String> customerNames = FXCollections.observableArrayList();
        for (int i = 0; i < customers.size(); i++) {
            customerNames.add(customers.get(i).getName());
        }
        return customerNames;
    }

    private ObservableList<String> setTypes() throws SQLException {
        ObservableList<String> types = FXCollections.observableArrayList();
        types.setAll(appointmentDAO.findTypes());
        return types;
    }

    public boolean wasCancel() {
        return cancel;
    }


}

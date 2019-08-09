package org.branlewalk.view;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import org.branlewalk.dao.AppointmentDAO;
import org.branlewalk.dao.AppointmentDaoImpl;
import org.branlewalk.dao.CustomerDAO;
import org.branlewalk.dao.CustomerDaoImpl;
import org.branlewalk.domain.Appointment;
import org.branlewalk.domain.Customer;
import org.branlewalk.domain.InvalidAppointmentException;
import org.branlewalk.domain.TimeOfDay;
import org.branlewalk.dto.AppointmentDTO;
import org.branlewalk.dto.CustomerDTO;

import java.net.URL;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.DateFormat;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;
import java.time.LocalDateTime;
import java.time.ZoneId;

public class AppointmentController implements Initializable {
    private static final long ONE_MINUTE_MILLIS = 60 * 1000L;
    private static final long THIRTY_MINUTES_MILLIS = 30 * ONE_MINUTE_MILLIS;
    private static final long ONE_HOUR_MILLIS = 60 * ONE_MINUTE_MILLIS;


    @FXML
    public TextField titleField, locationField, descriptionField, urlField, contactField;
    @FXML
    public DatePicker endPicker, startPicker;
    @FXML
    public ComboBox<String> customerComboBox, typeComboBox;
    @FXML
    public ComboBox<TimeOfDay> endComboBox, startComboBox;
    @FXML
    public Button deleteButton;
    private Connection connection;
    private String username;
    private AppointmentDAO appointmentDAO;

    private boolean cancel;
    private Appointment appointment;
    public static final LocalTime END_OF_BUSINESS_HOURS = LocalTime.of(17, 0);
    public static final LocalTime START_OF_BUSINESS_HOURS = LocalTime.of(9, 0);

    public AppointmentController(Appointment appointment) throws SQLException {
        this(appointment, new AppointmentDaoImpl(Main.connection(), LoginController.username));

    }

    public AppointmentController(Appointment appointment, AppointmentDAO appointmentDAO) throws SQLException {
        this.appointment = appointment;
        this.appointmentDAO = appointmentDAO;
        username = LoginController.username;
        connection = Main.connection();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        endComboBox.setItems(createTimeList());
        startComboBox.setItems(createTimeList());
        setTime();
        if (appointment != null) {
            deleteButton.setVisible(true);
            endPicker.setValue(appointment.getEnd().toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
            startPicker.setValue(appointment.getStart().toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
            titleField.setText(appointment.getTitle());
            locationField.setText(appointment.getLocation());
            descriptionField.setText(appointment.getDescription());
            urlField.setText(appointment.getUrl());
            contactField.setText(appointment.getContact());
            startComboBox.setValue(getTimeOfDay(appointment.getStart()));
            endComboBox.setValue(getTimeOfDay(appointment.getEnd()));
            customerComboBox.setValue(appointment.getCustomerName());
            typeComboBox.setValue(appointment.getType());
        } else {
            deleteButton.setVisible(false);
        }
        try {
            customerComboBox.setItems(setCustomers());
            typeComboBox.setItems(setTypes());
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public void handleSaveButton(ActionEvent actionEvent) throws SQLException {
        editAppointment(actionEvent, appointment == null ? this::addAppointment : this::updateAppointment);

    }

    interface AppointmentEditor {
        void edit() throws SQLException, InvalidAppointmentException;
    }

    private void editAppointment(ActionEvent actionEvent, AppointmentEditor editor) throws SQLException {
        try {
            editor.edit();
            Main.closeWindow(actionEvent);
        } catch (InvalidAppointmentException e) {
            appointmentFailedAlert(e);
        }
    }

    private void appointmentFailedAlert(InvalidAppointmentException e) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Error");
        alert.setHeaderText("Unable to add/change appointment");
        alert.setContentText(e.getMessage());
        alert.showAndWait();
    }

    public void handleDeleteButton(ActionEvent actionEvent) throws SQLException {
        appointmentDAO.delete(appointment.getId());
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
            }
        }
        return list;
    }

    private void updateAppointment() throws SQLException, InvalidAppointmentException {
        Date start = getDate(startPicker.getValue(), startComboBox.getValue());
        if (!duringBusinessHours(start)) {
            throw new InvalidAppointmentException("Start time must be during business hours!");
        }
        Date end = getDate(endPicker.getValue(), endComboBox.getValue());
        if (!duringBusinessHours(end)) {
            throw new InvalidAppointmentException("End time must be during business hours!");
        }
        validateTimeConflicts(start,end, appointment.getId());
        validateDataInput();
        CustomerDAO customerDAO = new CustomerDaoImpl(connection, username);
        CustomerDTO customerDTO = customerDAO.find(customerComboBox.getValue());
        AppointmentDTO appointmentDTO = new AppointmentDTO(appointment.getId(), customerDTO.getCustomerId(),
                appointment.getUserId(), titleField.getText(), descriptionField.getText(), locationField.getText(),
                contactField.getText(), typeComboBox.getValue(), urlField.getText(), start, end);
        appointmentDAO.update(username, appointmentDTO);
    }

    private void addAppointment() throws SQLException, InvalidAppointmentException {
        Date start = getDate(startPicker.getValue(), startComboBox.getValue());
        if (!duringBusinessHours(start)) {
            throw new InvalidAppointmentException("Start time must be during business hours!");
        }
        Date end = getDate(endPicker.getValue(), endComboBox.getValue());
        if (!duringBusinessHours(end)) {
            throw new InvalidAppointmentException("End time must be during business hours!");
        }
        validateTimeConflicts(start,end, 0);
        validateDataInput();
        appointmentDAO.create(customerComboBox.getValue(), titleField.getText(), descriptionField.getText(),
                locationField.getText(), contactField.getText(), typeComboBox.getValue(), urlField.getText(), start, end);
    }

    private boolean validateDataInput() throws InvalidAppointmentException {
        if (customerComboBox.getValue() == null) {
            throw new InvalidAppointmentException("No customer was selected");
        }
        if (titleField.getText().isEmpty()) {
            throw new InvalidAppointmentException("No appointment title was entered");
        }
        if (descriptionField.getText().isEmpty()) {
            throw new InvalidAppointmentException("No appointment description was entered");
        }
        if (locationField.getText().isEmpty()) {
            throw new InvalidAppointmentException("No appointment location was entered");
        }
        if (contactField.getText().isEmpty()) {
            throw new InvalidAppointmentException("No appointment contact was entered");
        }
        if (urlField.getText().isEmpty()) {
            throw new InvalidAppointmentException("No website was entered");
        }
        if (typeComboBox.getValue() == null) {
            throw new InvalidAppointmentException("No appointment type was selected or entered");
        }
        return true;
    }

    public boolean validateTimeConflicts(Date start, Date end, int id) throws InvalidAppointmentException, SQLException {
        ObservableList<Appointment> conflictedAppointments = appointmentDAO.findAllForDate(start);
        for (Appointment conflictedAppointment : conflictedAppointments) {
            if (conflictedAppointment.getId() == id) {
                continue;
            }
            Date appointmentStart = conflictedAppointment.getStart();
            Date appointmentEnd = conflictedAppointment.getEnd();
            if(start.before(appointmentStart) && end.after(appointmentStart) ) {
                throw new InvalidAppointmentException("Appointment overlaps with appointment " + conflictedAppointment.getTitle());
            }
            if(start.after(appointmentStart) && end.before(appointmentEnd) ) {
                throw new InvalidAppointmentException("Appointment overlaps with appointment " + conflictedAppointment.getTitle());
            }
            if(start.before(appointmentEnd) && end.after(appointmentEnd) ) {
                throw new InvalidAppointmentException("Appointment overlaps with appointment " + conflictedAppointment.getTitle());
            }
            if((start.equals(appointmentStart) || start.after((appointmentStart))) && (end.equals(appointmentEnd) || end.before(appointmentEnd)) ) {
                throw new InvalidAppointmentException("Appointment overlaps with appointment " + conflictedAppointment.getTitle());
            }
        }

        return true;
    }

    private boolean duringBusinessHours(Date date) {
        LocalTime time = date.toInstant().atZone(ZoneId.systemDefault()).toLocalTime();
        return !(time.isBefore(START_OF_BUSINESS_HOURS) || time.isAfter(END_OF_BUSINESS_HOURS));
    }

    public void setAppointment(Appointment appointment) {
        this.appointment = appointment;
    }

    private Date getDate(LocalDate date, TimeOfDay time) {
        return new java.sql.Timestamp(Date.from(date.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant()).getTime() + time.getMillisSinceMidnight());
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

    private TimeOfDay getTimeOfDay(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        DateFormat format = DateFormat.getTimeInstance(DateFormat.SHORT);
        long millisSinceMidnight = calendar.get(Calendar.HOUR_OF_DAY) * ONE_HOUR_MILLIS + calendar.get(Calendar.MINUTE) * ONE_MINUTE_MILLIS;
        return new TimeOfDay(format.format(calendar.getTime()), millisSinceMidnight);
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

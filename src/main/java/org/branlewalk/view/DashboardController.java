package org.branlewalk.view;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import org.branlewalk.dao.AppointmentDAO;
import org.branlewalk.dao.AppointmentDaoImpl;
import org.branlewalk.domain.Appointment;
import org.branlewalk.domain.WeekDays;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.time.ZoneId;
import java.util.*;

public class DashboardController implements Initializable {

    @FXML
    private GridPane monthViewPane, weekViewPane;
    @FXML
    private Label monthYearLabel, weekMonthLabel;
    @FXML
    private HBox monthController, weekController;

    private Calendar calendar;
    private List<TableView<Appointment>> monthDays;
    private List<TableView<Appointment>> weekDays;
    private WeekDays days;
    private boolean monthView;
    private final AppointmentDAO appointmentDAO;

    public DashboardController() throws SQLException {
        appointmentDAO = new AppointmentDaoImpl(Main.connection(), LoginController.username);
    }


    @FXML
    public void initialize(URL location, ResourceBundle resources) {
        LocalDateTime ldt = LocalDateTime.now();
        Date now = java.sql.Timestamp.valueOf((ldt));
        Date nowPlus15min = java.sql.Timestamp.valueOf((ldt.plusMinutes(15)));

        calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        monthDays = new ArrayList<>();
        weekDays = new ArrayList<>();
        days = new WeekDays(calendar);
        for (int row = 0; row < 6; row++) {
            for (int col = 0; col < 7; col++) {
                TableView<Appointment> day = new TableView<>();
                monthDays.add(day);
                monthViewPane.setConstraints(day, col, row);
                monthViewPane.getChildren().add(day);
            }
        }
        for (int col = 0; col < 7; col++) {
            TableView<Appointment> day = new TableView<>();
            weekDays.add(day);
            weekViewPane.setConstraints(day, col, 0);
            weekViewPane.getChildren().add(day);
        }
        weekViewPane.setVisible(false);
        weekController.setVisible(false);
        try {
            updateMonthView();
            checkReminders(now, nowPlus15min);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void updateMonthView() throws SQLException {
        monthYearLabel.setText(calendar.getDisplayName(Calendar.MONTH, Calendar.LONG_FORMAT, Locale.US) + " " +
                calendar.get(Calendar.YEAR));

        Calendar end = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        end.setTime(calendar.getTime());
        end.add(Calendar.DATE, YearMonth.of(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH) + 1).lengthOfMonth() - 1);
        ObservableList<Appointment> monthsAppointments = getAppointments(calendar.getTime(), end.getTime());

        int col;
        int firstDay = calendar.get(Calendar.DAY_OF_WEEK) - 1;
        int currentDay = 0;
        boolean start = false;
        boolean finish = false;
        int dayNumber = 0;
        for (int row = 0; row < 6; row++) {
            for (col = 0; col < 7; col++, dayNumber++) {
                TableView<Appointment> day = monthDays.get(dayNumber);
                day.getColumns().clear();
                if (!start && !finish) {
                    if (col == firstDay) {
                        currentDay = 1;
                        start = true;
                    }
                } else if (start) {
                    if (currentDay == YearMonth.of(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH) + 1).lengthOfMonth()) {
                        finish = true;
                    }
                    currentDay++;
                }
                if (start && !finish) {
                    Calendar dayCalendar = Calendar.getInstance();
                    dayCalendar.setTime(calendar.getTime());
                    dayCalendar.add(Calendar.DATE, currentDay - 1);
                    populateDayView(day, String.valueOf(currentDay), dayCalendar.getTime(), monthsAppointments);
                } else {
                    day.visibleProperty().setValue(false);
                }

            }
        }
    }

    private void updateWeekView() throws SQLException {
        weekMonthLabel.setText(days.getLabel());

        List<Integer> dayStrings = days.getCurrent();
        Calendar begin = Calendar.getInstance();
        begin.setTime(calendar.getTime());
        begin.add(Calendar.DATE, -6);
        ObservableList<Appointment> weeksAppointments = getAppointments(begin.getTime(), calendar.getTime());

        for (int col = 0; col < dayStrings.size(); col++) {
            TableView<Appointment> day = weekDays.get(col);
            day.getColumns().clear();
            String header = String.valueOf(dayStrings.get(col));
            Calendar dayCalendar = Calendar.getInstance();
            dayCalendar.setTime(calendar.getTime());
            dayCalendar.add(Calendar.DATE, col - 6);
            populateDayView(day, header, dayCalendar.getTime(), weeksAppointments);
        }
    }

    private ObservableList<Appointment> getAppointments(Date begin, Date end) throws SQLException {
        return appointmentDAO.findAllForDateRange(begin, end);
    }

    private void populateDayView(TableView<Appointment> day, String header, Date date, ObservableList<Appointment> appointmentList) throws SQLException {
        TableColumn<Appointment, String> appointments = new TableColumn<>(header);
        appointments.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getTitle()));

        ObservableList<Appointment> filteredAppointments = getAppointmentsFor(appointmentList, date);

        day.setItems(filteredAppointments);
        if (filteredAppointments.size() == 0) {
            day.setPlaceholder(new Label());
        }
        appointments.prefWidthProperty().bind(day.widthProperty());
        day.getColumns().add(appointments);
        day.visibleProperty().setValue(true);

        // Lambda expression to open a new appointment window from clicking on the appointment for a specific day
        day.onMouseClickedProperty().set(event -> {
            try {
                Appointment selectedAppointment = day.getSelectionModel().getSelectedItem();
                if (selectedAppointment == null) {
                    if (!Main.newAppointmentWindow("Appointment", "Appointment.fxml", date)) {
                        if (weekViewPane.isVisible()) {
                            updateWeekView();
                        } else {
                            updateMonthView();
                        }
                    }
                } else {
                    if (!Main.editAppointmentWindow("Appointment", "Appointment.fxml", selectedAppointment)) {
                        if (weekViewPane.isVisible()) {
                            updateWeekView();
                        } else {
                            updateMonthView();
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    private ObservableList<Appointment> getAppointmentsFor(ObservableList<Appointment> appointmentList, Date date) {
        ObservableList<Appointment> appointments = FXCollections.observableArrayList();
        LocalDate day = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        Date begin = Timestamp.valueOf(day.atStartOfDay());
        Date end = Timestamp.valueOf(day.plusDays(1).atStartOfDay());
        for (Appointment appointment : appointmentList) {
            if (appointment.getStart().after(begin) && appointment.getStart().before(end)) {
                appointments.add(appointment);
            }
        }
        return appointments;
    }

    private ObservableList<String> createAppointmentTitles(ObservableList<Appointment> appointmentList) {
        ObservableList<String> titles = FXCollections.observableArrayList();
        for (Appointment appointment : appointmentList) {
            titles.add(appointment.toString());
        }
        return titles;
    }

    private void checkReminders(Date now, Date nowPlus15min) throws SQLException {
        ObservableList<Appointment> reminder = appointmentDAO.findAllTimesForDateRange(now, nowPlus15min);
        if (reminder.size() != 0) {
            String type = reminder.get(0).getTitle();
            String customer = reminder.get(0).getCustomerName();
            Date start = reminder.get(0).getStart();
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Reminder: Appointment");
            alert.setHeaderText("You have the following appointment soon");
            alert.setContentText(type + " with " + customer +
                    " will begin at " + start + ".");
            alert.showAndWait();
        }

    }

    public void handleReportingButtonAction(ActionEvent actionEvent) throws IOException {
        Main.newWindow("Reporting", "Reporting.fxml");

    }

    public void handlePreviousMonthButton(ActionEvent actionEvent) throws SQLException {
        calendar.add(Calendar.MONTH, -1);
        updateMonthView();
    }

    public void handleNextMonthButton(ActionEvent actionEvent) throws SQLException {
        calendar.add(Calendar.MONTH, +1);
        updateMonthView();
    }

    public void handleMonthViewButton(ActionEvent actionEvent) throws SQLException {
        updateMonthView();
        monthViewPane.setVisible(true);
        monthController.setVisible(true);
        weekViewPane.setVisible(false);
        weekController.setVisible(false);
    }

    public void handleNextWeekButton(ActionEvent actionEvent) throws SQLException {
        calendar.add(Calendar.WEEK_OF_MONTH, 1);
        updateWeekView();
    }


    public void handlePreviousWeekButton(ActionEvent actionEvent) throws SQLException {
        calendar.add(Calendar.WEEK_OF_MONTH, -1);
        updateWeekView();
    }


    public void handleWeekViewButton(ActionEvent actionEvent) throws SQLException {
        updateWeekView();
        weekViewPane.setVisible(true);
        weekController.setVisible(true);
        monthViewPane.setVisible(false);
        monthController.setVisible(false);
    }

    public void handleCustomersButton(ActionEvent actionEvent) throws IOException {
        Main.newWindow("Customers", "CustomerList.fxml");
    }
}

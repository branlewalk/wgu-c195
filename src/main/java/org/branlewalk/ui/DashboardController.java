package org.branlewalk.ui;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import org.branlewalk.dao.AppointmentDAO;
import org.branlewalk.dao.AppointmentDaoImpl;
import org.branlewalk.domain.Appointment;

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
    private List<TableView<String>> monthDays;
    private List<TableView<String>> weekDays;
    private WeekDays days;
    private boolean monthView;


    @FXML
    public void initialize(URL location, ResourceBundle resources) {
        calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        monthDays = new ArrayList<TableView<String>>();
        weekDays = new ArrayList<TableView<String>>();
        days = new WeekDays(calendar);
        for (int row = 0; row < 6; row++) {
            for (int col = 0; col < 7; col++) {
                TableView<String> day = new TableView<>();
                monthDays.add(day);
                monthViewPane.setConstraints(day, col, row);
                monthViewPane.getChildren().add(day);
            }
        }
        for (int col = 0; col < 7; col++) {
            TableView<String> day = new TableView<>();
            weekDays.add(day);
            weekViewPane.setConstraints(day, col, 0);
            weekViewPane.getChildren().add(day);
        }
        weekViewPane.setVisible(false);
        weekController.setVisible(false);
        try {
            updateMonthView();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void updateMonthView() throws SQLException {
        monthYearLabel.setText(calendar.getDisplayName(Calendar.MONTH, Calendar.LONG_FORMAT, Locale.US) + " " +
                calendar.get(Calendar.YEAR));

        Calendar end = Calendar.getInstance();
        end.setTime(calendar.getTime());
        end.add(Calendar.DATE, YearMonth.of(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH) + 1).lengthOfMonth()-1);
        ObservableList<Appointment> monthsAppointments = getAppointments(calendar.getTime(), end.getTime());

        int col;
        int firstDay = calendar.get(Calendar.DAY_OF_WEEK) - 1;
        int currentDay = 0;
        boolean start = false;
        boolean finish = false;
        int dayNumber = 0;
        for (int row = 0; row < 6; row++) {
            for (col = 0; col < 7; col++, dayNumber++) {
                TableView<String> day = monthDays.get(dayNumber);
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
                    dayCalendar.add(Calendar.DATE, currentDay-1);
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
            TableView<String> day = weekDays.get(col);
            day.getColumns().clear();
            String header = String.valueOf(dayStrings.get(col));
            Calendar dayCalendar = Calendar.getInstance();
            dayCalendar.setTime(calendar.getTime());
            dayCalendar.add(Calendar.DATE, col-6);
            populateDayView(day, header, dayCalendar.getTime(), weeksAppointments);
        }
    }

    private ObservableList<Appointment> getAppointments(Date begin, Date end) throws SQLException {
        AppointmentDAO appointmentDAO = new AppointmentDaoImpl(Main.connection(), LoginController.username);
        return appointmentDAO.findAllForDateRange(begin, end);
    }

    private void populateDayView(TableView<String> day, String header, Date date, ObservableList<Appointment> appointmentList) throws SQLException {
        TableColumn<String, String> appointments = new TableColumn<>(header);
        appointments.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue()));

        ObservableList<Appointment> filteredAppointments = getAppointmentsFor(appointmentList, date);

        day.setItems(createAppointmentTitles(filteredAppointments));
        if (filteredAppointments.size() == 0) {
            day.setPlaceholder(new Label());
        }
        appointments.prefWidthProperty().bind(day.widthProperty());
        day.getColumns().add(appointments);
        day.visibleProperty().setValue(true);
        day.onMouseClickedProperty().set(event -> {
            try {
                if(!Main.newAppointmentWindow("Appointment", "Appointment.fxml", date)) {
                    if (weekViewPane.isVisible()) {
                        updateWeekView();
                    } else {
                        updateMonthView();
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
            if(appointment.getStart().after(begin) && appointment.getStart().before(end)) {
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
        Main.newWindow("Customers","CustomerList.fxml");
    }
}

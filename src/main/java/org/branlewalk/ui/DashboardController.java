package org.branlewalk.ui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.time.YearMonth;
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
        updateMonthView();
    }

    private void updateMonthView() {
        monthYearLabel.setText(calendar.getDisplayName(Calendar.MONTH, Calendar.LONG_FORMAT, Locale.US) + " " +
                calendar.get(Calendar.YEAR));
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
                    populateDayView(day, String.valueOf(currentDay), dayCalendar.getTime());
                } else {
                    day.visibleProperty().setValue(false);
                }

            }
        }
    }

    private void updateWeekView() {
        weekMonthLabel.setText(days.getLabel());

        List<Integer> dayStrings = days.getCurrent();

        for (int col = 0; col < dayStrings.size(); col++) {
            TableView<String> day = weekDays.get(col);
            day.getColumns().clear();
            String header = String.valueOf(dayStrings.get(col));
            Calendar dayCalendar = Calendar.getInstance();
            dayCalendar.setTime(calendar.getTime());
            dayCalendar.add(Calendar.DATE, col-6);
            populateDayView(day, header, dayCalendar.getTime());
        }
    }

    private void populateDayView(TableView<String> day, String header, Date date) {
        TableColumn<String, String> appointments = new TableColumn<>(header);
        day.setPlaceholder(new Label());
        appointments.prefWidthProperty().bind(day.widthProperty());
        day.getColumns().add(appointments);
        day.visibleProperty().setValue(true);
        day.onMouseClickedProperty().set(event -> {
            try {
                newWindow2("Appointment", "Appointment.fxml", date);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    public static void newWindow2(String title, String view, Date date) throws IOException {
        FXMLLoader loader = new FXMLLoader(Main.class.getResource(view));
        Parent root = loader.load();
        AppointmentController controller = loader.getController();
        controller.setDate(date);
        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setTitle(title);
        stage.setScene(new Scene(root));
        stage.showAndWait();
    }

    public void handleReportingButtonAction(ActionEvent actionEvent) throws IOException {
        Main.newWindow("Reporting", "Reporting.fxml");

    }

    public void handlePreviousMonthButton(ActionEvent actionEvent) {
        calendar.add(Calendar.MONTH, -1);
        updateMonthView();
    }

    public void handleNextMonthButton(ActionEvent actionEvent) {
        calendar.add(Calendar.MONTH, +1);
        updateMonthView();
    }

    public void handleMonthViewButton(ActionEvent actionEvent) {
        updateMonthView();
        monthViewPane.setVisible(true);
        monthController.setVisible(true);
        weekViewPane.setVisible(false);
        weekController.setVisible(false);
    }

    public void handleNextWeekButton(ActionEvent actionEvent) {
        calendar.add(Calendar.WEEK_OF_MONTH, 1);
        updateWeekView();
    }


    public void handlePreviousWeekButton(ActionEvent actionEvent) {
        calendar.add(Calendar.WEEK_OF_MONTH, -1);
        updateWeekView();
    }


    public void handleWeekViewButton(ActionEvent actionEvent) {
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

package org.branlewalk.ui;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import org.branlewalk.dao.AppointmentDAO;
import org.branlewalk.dao.AppointmentDaoImpl;
import org.branlewalk.domain.Appointment;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.DateFormatSymbols;
import java.util.Arrays;
import java.util.Locale;
import java.util.ResourceBundle;

public class ReportingController implements Initializable {

    private final Connection connection;
    private final String username;
    private ObservableList<String> months;
    private CategoryAxis xAxis;
    private NumberAxis yAxis;
    private XYChart.Series chartData;

    public ReportingController() throws SQLException {
        connection = Main.connection();
        username = LoginController.username;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        xAxis = new CategoryAxis();
        yAxis = new NumberAxis();
        chartData = new XYChart.Series();
        String[] month = DateFormatSymbols.getInstance(Locale.ENGLISH).getMonths();
        months = FXCollections.observableArrayList();
        months.addAll(Arrays.asList(month));
    }

    public void handleAppointmentsByMonth(ActionEvent actionEvent) throws IOException, SQLException {
        xAxis.setCategories(months);
        setAppointmentCount();
        Main.newReportWindow("Appointments by Month", "Report.fxml", xAxis, yAxis, chartData);
    }


    public void handleAppointmentsByUsers(ActionEvent actionEvent) throws IOException {
        Main.newReportWindow("Appointments by Users", "Report.fxml", xAxis, yAxis, chartData);
    }

    public void handleAppointmentsByCustomers(ActionEvent actionEvent) throws IOException {
        Main.newReportWindow("Appointments by Customers", "Report.fxml", xAxis, yAxis, chartData);/**/
    }

    private void setAppointmentCount() throws SQLException {
        AppointmentDAO appointmentDAO = new AppointmentDaoImpl(connection, username);
        ObservableList<Appointment> appointments = appointmentDAO.findAll();
        int[] appointmentByMonthCounter = new int[12];
        for (Appointment appointment : appointments) {
            int month = appointment.getMonthValue();
            appointmentByMonthCounter[month]++;
        }
        for (int i = 0; i < appointmentByMonthCounter.length; i++) {
            chartData.getData().add(new XYChart.Data<>(months.get(i), appointmentByMonthCounter[i]));
        }
    }


}

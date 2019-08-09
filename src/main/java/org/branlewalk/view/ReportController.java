package org.branlewalk.view;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import org.branlewalk.dao.AppointmentDAO;
import org.branlewalk.dao.AppointmentDaoImpl;
import org.branlewalk.domain.Appointment;


import java.net.URL;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.YearMonth;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.ResourceBundle;


public class ReportController implements Initializable {


    @FXML
    public TableView<Appointment> tableView;
    @FXML
    public TableColumn<Appointment, String> typeColumn;
    @FXML
    public TableColumn<Appointment, String> customerColumn;
    @FXML
    public TableColumn<Appointment, String> consultantColumn;
    @FXML
    public TableColumn<Appointment, String> titleColumn;
    @FXML
    public TableColumn<Appointment, String> startColumn;
    @FXML
    public TableColumn<Appointment, String> endColumn;
    @FXML
    public Label titleLabel;
    private String data;
    private AppointmentDAO appointmentDAO;

    public ReportController() throws SQLException {
        Connection connection = Main.connection();
        String username = LoginController.username;
        appointmentDAO = new AppointmentDaoImpl(connection, username);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        typeColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getType()));
        customerColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getCustomerName()));
        consultantColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getUserName()));
        titleColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getTitle()));
        startColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getStartDateString()));
        endColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getEndDateString()));
    }


    public void setTypeOfReport(String type) throws ParseException, SQLException {
        switch (type) {
            case "appointmentByType":
                getAppointmentbyType();
                break;
            case "appointmentByCustomer":
                getAppointmentByCustomer();
                break;
            case "appointmentByConsultant":
                getAppointmentByConsultant();
                break;
        }
    }

    private void getAppointmentByConsultant() throws SQLException {
        titleLabel.setText("Appointment by Consultant");
        ObservableList<Appointment> appointments = appointmentDAO.findAll();
        ObservableList<Appointment> appointmentsByConsultant = FXCollections.observableArrayList();
        for (Appointment appointment : appointments) {
            if (appointment.getUserName().equals(data)) {
                appointmentsByConsultant.add(appointment);
            }
        }
        tableView.setItems(appointmentsByConsultant);

    }

    private void getAppointmentByCustomer() throws SQLException {
        titleLabel.setText("Appointment by Customer");
        ObservableList<Appointment> appointments = appointmentDAO.findAll();
        ObservableList<Appointment> appointmentsByCustomer = FXCollections.observableArrayList();
        for (Appointment appointment : appointments) {
            if (appointment.getCustomerName().equals(data)) {
                appointmentsByCustomer.add(appointment);
            }
        }
        tableView.setItems(appointmentsByCustomer);
    }

    private void getAppointmentbyType() throws ParseException, SQLException {
        titleLabel.setText("Appointment by Type");
        SimpleDateFormat format = new SimpleDateFormat("dd MMMM yyyy", Locale.ENGLISH);
        Date date = format.parse(data);
        Calendar start = Calendar.getInstance();
        start.setTime(date);
        Calendar end = Calendar.getInstance();
        start.set(Calendar.DAY_OF_MONTH, 1);
        end.setTime(start.getTime());
        end.add(Calendar.DATE, YearMonth.of(start.get(Calendar.YEAR), start.get(Calendar.MONTH) + 1).lengthOfMonth() - 1);
        ObservableList<Appointment> appointmentsByType = appointmentDAO.findAllForDateRange(start.getTime(), end.getTime());
        tableView.setItems(appointmentsByType);
        tableView.getSortOrder().add(typeColumn);
    }

    public void setData(String data) {
        this.data = data;
    }

    public void handleBackButton(ActionEvent actionEvent) {
        Main.closeWindow(actionEvent);
    }
}

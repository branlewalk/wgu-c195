package org.branlewalk.view;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import org.branlewalk.dao.*;
import org.branlewalk.domain.Customer;
import org.branlewalk.domain.User;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.DateFormatSymbols;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

public class ReportingController implements Initializable {

    @FXML
    public ComboBox<String> monthComboBox, yearComboBox, consultantComboBox, customerComboBox;
    private UserDAO userDAO;
    private AppointmentDAO appointmentDAO;
    private CustomerDAO customerDAO;
    private ObservableList<String> months;

    public ReportingController() throws SQLException {
        Connection connection = Main.connection();
        String username = LoginController.username;
        customerDAO = new CustomerDaoImpl(connection, username);
        appointmentDAO = new AppointmentDaoImpl(connection, username);
        userDAO = new UserDaoImpl(connection);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        monthComboBox.setItems(setMonthList());
        yearComboBox.setItems(setYearList());
        consultantComboBox.setItems(setConsultantList());
        customerComboBox.setItems(setCustomerList());
    }

    public void handleGoAppointmentByType(ActionEvent actionEvent) throws Exception {
        String data = "01 " + monthComboBox.getValue() + " " + yearComboBox.getValue();
        Main.newReportWindow("Reports: Appointments by Type", "Report.fxml", "appointmentByType", data);
    }

    public void handleGoAppointmentByConsultant(ActionEvent actionEvent) throws Exception {
        Main.newReportWindow("Reports: Appointments by Type", "Report.fxml", "appointmentByConsultant", consultantComboBox.getValue());

    }

    public void handleGoAppointmentByCustomer(ActionEvent actionEvent) throws Exception {
        Main.newReportWindow("Reports: Appointments by Type", "Report.fxml", "appointmentByCustomer", customerComboBox.getValue());
    }

    public void handleBackButton(ActionEvent actionEvent) {
        Main.closeWindow(actionEvent);
    }

    private ObservableList<String> setMonthList() {
        String[] month = DateFormatSymbols.getInstance(Locale.ENGLISH).getMonths();

        months = FXCollections.observableArrayList();
        months.addAll(Arrays.asList(month));
        return months;
    }

    private ObservableList<String> setYearList() {
        ObservableList<String> years = FXCollections.observableArrayList();
        for (int i = 2010; i < 2031; i++) {
            years.add(Integer.toString(i));
        }
        return years;
    }

    private ObservableList<String> setConsultantList() {
        ObservableList<User> users = FXCollections.observableArrayList();
        try {
            users = userDAO.findAll();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        ObservableList<String> consultants = FXCollections.observableArrayList();
        for (int i = 0; i < users.size(); i++) {
            consultants.add(users.get(i).getName());
        }
        return consultants;
    }

    private ObservableList<String> setCustomerList() {
        ObservableList<Customer> customers = FXCollections.observableArrayList();
        try {
            customers = customerDAO.findAll();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        ObservableList<String> customerNames = FXCollections.observableArrayList();
        for (int i = 0; i < customers.size(); i++) {
            customerNames.add(customers.get(i).getName());
        }
        return customerNames;
    }


}

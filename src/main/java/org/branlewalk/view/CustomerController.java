package org.branlewalk.view;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import org.branlewalk.dao.*;
import org.branlewalk.domain.Customer;
import org.branlewalk.domain.InvalidAppointmentException;
import org.branlewalk.domain.InvalidCustomerException;
import org.branlewalk.dto.AddressDTO;
import org.branlewalk.dto.CityDTO;
import org.branlewalk.dto.CountryDTO;
import org.branlewalk.dto.CustomerDTO;


import java.net.URL;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class CustomerController implements Initializable {

    private final AddressDAO addressDAO;
    private final CityDAO cityDAO;
    private final CountryDAO countryDAO;
    @FXML
    private Button deleteButton;
    @FXML
    private TextField customerNameField, address1Field, address2Field, cityField, countryField, postalCodeField, phoneField;
    private String username;
    private final Customer customer;
    private CustomerDAO customerDAO;

    public CustomerController(Customer customer) throws SQLException {
        username = LoginController.username;
        Connection connection = Main.connection();
        customerDAO = new CustomerDaoImpl(connection, username);
        addressDAO = new AddressDaoImpl(connection);
        cityDAO = new CityDaoImpl(connection);
        countryDAO = new CountryDaoImpl(connection);
        this.customer = customer;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        if (customer != null) {
            deleteButton.setVisible(true);
            customerNameField.setText(customer.getName());
            address1Field.setText(customer.getAddress1());
            address2Field.setText(customer.getAddress2());
            cityField.setText(customer.getCity());
            countryField.setText(customer.getCountry());
            postalCodeField.setText(customer.getPostalCode());
            phoneField.setText(customer.getPhone());
        } else {
            deleteButton.setVisible(false);
        }
    }

    @FXML
    public void handleSaveButton(ActionEvent actionEvent) throws SQLException {
        try {
            if (customer != null) {
                validateDataInput();
                updateCustomer();
            } else {
                validateDataInput();
                addCustomer();
            }
            Main.closeWindow(actionEvent);
        } catch (InvalidCustomerException e) {
            customerFailedAlert(e);
        }

    }


    @FXML
    public void handleCancelButton(ActionEvent actionEvent) {
        Main.closeWindow(actionEvent);
    }

    @FXML
    public void handleDeleteButton(ActionEvent actionEvent) throws SQLException {
       customerDAO.delete(customer.getId());
        Main.closeWindow(actionEvent);
    }


    private void addCustomer() throws SQLException {
        customerDAO.create(customerNameField.getText(), address1Field.getText(), address2Field.getText(), cityField.getText(),
                countryField.getText(), postalCodeField.getText(), phoneField.getText());
    }

    private void updateCustomer() throws SQLException {
        CountryDTO countryDTO = countryDAO.find(countryField.getText());
        CityDTO cityDTO = cityDAO.find(cityField.getText());
        AddressDTO addressDTO = addressDAO.find(address1Field.getText());

        if (countryDTO == null) {
            countryDTO = countryDAO.create(countryField.getText(), username);
        }

        if (cityDTO == null) {
            cityDTO = cityDAO.create(cityField.getText(), countryDTO.getCountryId(), username);
        }

        if (addressDTO == null) {
            addressDTO = addressDAO.create(address1Field.getText(), address2Field.getText(), cityDTO.getCityId(),
                    postalCodeField.getText(), phoneField.getText(), username);
        }


        CustomerDTO customerDTO = new CustomerDTO(customer.getId(), customerNameField.getText(),
                addressDTO.getAddressId(), true);
        customerDAO.update(username, customerDTO);

    }

    private boolean validateDataInput() throws InvalidCustomerException {
        if (customerNameField.getText().isEmpty()) {
            throw new InvalidCustomerException("No name was entered");
        }
        if (address1Field.getText().isEmpty()) {
            throw new InvalidCustomerException("No address was entered");
        }
        if (cityField.getText().isEmpty()) {
            throw new InvalidCustomerException("No city was entered");
        }
        if (countryField.getText().isEmpty()) {
            throw new InvalidCustomerException("No country was entered");
        }
        if (postalCodeField.getText().isEmpty()) {
            throw new InvalidCustomerException("No postal code was entered");
        }
        if (phoneField.getText().isEmpty()) {
            throw new InvalidCustomerException("No phone number was entered");
        }
        return true;
    }

    private void customerFailedAlert(InvalidCustomerException e) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Error");
        alert.setHeaderText("Unable to add/change customer");
        alert.setContentText(e.getMessage());
        alert.showAndWait();
    }

}

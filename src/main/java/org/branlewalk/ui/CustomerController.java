package org.branlewalk.ui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.branlewalk.dao.*;


import java.sql.Connection;
import java.sql.SQLException;

public class CustomerController {

    @FXML
    private TextField customerNameField, address1Field, address2Field, cityField, countryField, postalCodeField, phoneField;
    private String username;
    private Connection connection;

    public CustomerController() throws SQLException {
        username = LoginController.username;
        connection = Main.connection();
    }

    @FXML
    public void handleSaveButton(ActionEvent actionEvent) throws SQLException {
        addCustomer();
        final Node source = (Node) actionEvent.getSource();
        final Stage stage = (Stage) source.getScene().getWindow();
        stage.close();
    }

    @FXML
    public void handleCancelButton(ActionEvent actionEvent) {
        final Node source = (Node) actionEvent.getSource();
        final Stage stage = (Stage) source.getScene().getWindow();
        stage.close();
    }

    void addCustomer() throws SQLException {
        CustomerDAO customerDAO = new CustomerDaoImpl(connection, username);
        customerDAO.create(customerNameField.getText(), address1Field.getText(), address2Field.getText(), cityField.getText(),
                countryField.getText(), postalCodeField.getText(), phoneField.getText());
    }

}

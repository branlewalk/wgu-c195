package org.branlewalk.ui;


import javafx.beans.property.SimpleStringProperty;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import org.branlewalk.dao.CustomerDAO;
import org.branlewalk.dao.CustomerDaoImpl;
import org.branlewalk.domain.Customer;


import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;


public class CustomerListController implements Initializable {

    @FXML
    TableView<Customer> customerListTable;
    @FXML
    TableColumn<Customer, String> nameColumn;
    @FXML
    TableColumn<Customer, String> phoneColumn, postalColumn;
    @FXML
    TableColumn<Customer, String> cityColumn;
    @FXML
    TableColumn<Customer, String> countryColumn;
    private String username;
    private CustomerDAO customerDAO;

    public CustomerListController() {
        username = LoginController.username;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        nameColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getName()));
        phoneColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getPhone()));
        postalColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getPostalCode()));
        cityColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getCity()));
        countryColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getCountry()));

        try {
            customerDAO = new CustomerDaoImpl(Main.connection(), username);
            update();
            customerListTable.setColumnResizePolicy((param) -> true );
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    private void update() throws SQLException {
        ObservableList<Customer> customers = customerDAO.findAll();
        customerListTable.setItems(customers);
    }

    public void handleDeleteButton(ActionEvent actionEvent) {

    }

    public void handleUpdateButton(ActionEvent actionEvent) {

    }

    public void handleAddButton(ActionEvent actionEvent) throws IOException, SQLException {
        Main.newWindow("Customer", "Customer.fxml");
        update();
    }


}



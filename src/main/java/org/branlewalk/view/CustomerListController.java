package org.branlewalk.view;


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

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        username = LoginController.username;
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
        // Lambda expression to open a new customer window from clicking on the customer list
        customerListTable.onMouseClickedProperty().set( event -> {
            try {
                if (customerListTable.getSelectionModel().isEmpty()) {
                    handleAddButton(new ActionEvent());
                } else {
                    handleUpdateButton(new ActionEvent());
                }
        } catch (Exception e) {
                e.printStackTrace();
            }
        });


    }

    private void update() throws SQLException {
        ObservableList<Customer> customers = customerDAO.findAll();
        customerListTable.setItems(customers);
    }

    public void handleDeleteButton(ActionEvent actionEvent) {

    }

    public void handleUpdateButton(ActionEvent actionEvent) throws Exception {
        Customer customer = customerListTable.getSelectionModel().getSelectedItem();
        Main.newCustomerWindow("Customer", "Customer.fxml", customer);
        update();
    }

    public void handleAddButton(ActionEvent actionEvent) throws Exception {
        Main.newCustomerWindow("Customer", "Customer.fxml", null);
        update();
    }


}



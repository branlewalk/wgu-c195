package org.branlewalk.dao;

import javafx.collections.ObservableList;
import org.branlewalk.domain.Customer;
import org.branlewalk.dto.CustomerDTO;

import java.sql.SQLException;


public interface CustomerDAO {

    CustomerDTO create(String customerName, int addressID, boolean active, String createdBy) throws SQLException;

    CustomerDTO read(int id) throws SQLException;

    void update(String lastUpdateBy, CustomerDTO updateDTO) throws SQLException;

    void delete(int id) throws SQLException;

    CustomerDTO find(String name) throws SQLException;

    ObservableList<Customer> findAll() throws SQLException;

    Customer create(String customerName, String address1, String address2, String city, String postalCode, String country, String phone) throws SQLException;
}
package org.branlewalk.dao;

import org.branlewalk.dto.CustomerDTO;

import java.sql.*;


public class CustomerDaoImpl implements CustomerDAO {
    private Connection connection;

    public CustomerDaoImpl(Connection connection) {

        this.connection = connection;
    }

    public void create(CustomerDTO customerDTO, String createdBy, Date createDate) throws SQLException {
        String query = "INSERT INTO customer (customerId,customerName,addressId,active,createDate,createdBy,lastUpdateBy) VALUES (?,?,?,?,?,?,?)";
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setInt(1, customerDTO.getCustomerId());
        statement.setString(2, customerDTO.getCustomerName());
        statement.setInt(3, customerDTO.getAddressId());
        statement.setBoolean(4, customerDTO.isActive());
        statement.setDate(5, createDate);
        statement.setString(6, createdBy);
        statement.setString(7, createdBy);
        statement.execute();
        statement.close();
    }

    public CustomerDTO read(int id) throws SQLException {
        String query = "SELECT customerId,customerName,addressId,active,createDate,createdBy,lastUpdateBy FROM customer WHERE customerId = ?";
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setInt(1,id);
        ResultSet resultSet = statement.executeQuery();
        if (resultSet.next()) {
            return new CustomerDTO(resultSet.getInt("customerId"),
                    resultSet.getString("customerName"),
                    resultSet.getInt("addressId"),
                    resultSet.getBoolean("active"));
        }
        return null;
    }

    public void update(String lastUpdateBy, CustomerDTO updateDTO) throws SQLException {
        String query = "UPDATE customer SET customerName = ?, addressId = ?, active = ?, lastUpdateBy = ? WHERE customerId = ?";
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setString(1, updateDTO.getCustomerName());
        statement.setInt(2, updateDTO.getAddressId());
        statement.setBoolean(3, updateDTO.isActive());
        statement.setString(4, lastUpdateBy);
        statement.setInt(5, updateDTO.getCustomerId());
        statement.execute();
        statement.close();

    }

    public void delete(int id) throws SQLException {
        String query = "DELETE FROM customer WHERE customerId = ?";
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setInt(1,id);
        statement.execute();
        statement.close();
    }
}

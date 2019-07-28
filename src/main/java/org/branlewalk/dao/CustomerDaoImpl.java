package org.branlewalk.dao;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.branlewalk.domain.Customer;
import org.branlewalk.domain.CustomerImpl;
import org.branlewalk.dto.AddressDTO;
import org.branlewalk.dto.CityDTO;
import org.branlewalk.dto.CountryDTO;
import org.branlewalk.dto.CustomerDTO;

import java.sql.*;


public class CustomerDaoImpl extends DaoIdGenerator<CustomerDTO> implements CustomerDAO {
    private final String username;
    private final CountryDAO countryDAO;
    private final CityDAO cityDAO;
    private final AddressDAO addressDAO;

    public CustomerDaoImpl(Connection connection, String username) {
        super(connection, "customer", "customerId");
        this.username = username;
        countryDAO = new CountryDaoImpl(connection);
        cityDAO = new CityDaoImpl(this.connection);
        addressDAO = new AddressDaoImpl(this.connection);
    }

    public CustomerDTO create(String customerName, int addressId, boolean active, String createdBy) throws SQLException {
        String query = "INSERT INTO customer (customerId,customerName,addressId,active,createDate,createdBy,lastUpdateBy) VALUES (?,?,?,?,NOW(),?,?)";
        PreparedStatement statement = connection.prepareStatement(query);
        int id = findId();
        statement.setInt(1, id);
        statement.setString(2, customerName);
        statement.setInt(3, addressId);
        statement.setBoolean(4, active);
        statement.setString(5, createdBy);
        statement.setString(6, createdBy);
        statement.execute();
        statement.close();
        return new CustomerDTO(id, customerName, addressId, active);
    }

    public CustomerDTO read(int id) throws SQLException {
        String query = "SELECT customerId,customerName,addressId,active,createDate,createdBy,lastUpdateBy FROM customer WHERE customerId = ?";
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setInt(1, id);
        return getCustomerDTO(statement);
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
        statement.setInt(1, id);
        statement.execute();
        statement.close();
    }

    public CustomerDTO find(String name) throws SQLException {
        String query = "SELECT customerId,customerName,addressId,active,createDate,createdBy,lastUpdateBy FROM customer WHERE customerId = ?";
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setString(1, name);
        return getCustomerDTO(statement);
    }

    public ObservableList<Customer> findAll() throws SQLException {
        ObservableList<Customer> customers = FXCollections.observableArrayList();
        String query = "SELECT * FROM customer WHERE active = 1";
        PreparedStatement statement = connection.prepareStatement(query);
        ResultSet resultSet = statement.executeQuery();
        while (resultSet.next()) {
            customers.add(getCustomer(resultSet));
        }
        return customers;
    }

    @Override
    public Customer create(String customerName, String address1, String address2, String city, String postalCode, String country, String phone) throws SQLException {
        CountryDTO countryDTO = countryDAO.create(country, username);
        CityDTO cityDTO = cityDAO.create(city, countryDTO.getCountryId(), username);
        AddressDTO addressDTO = addressDAO.create(address1, address2, cityDTO.getCityId(), postalCode, phone, username);
        CustomerDTO customerDTO = create(customerName, addressDTO.getAddressId(), true, username);
        return new CustomerImpl(customerDTO, addressDTO, cityDTO, countryDTO);
    }

    private CustomerImpl getCustomer(ResultSet resultSet) throws SQLException {
        CustomerDTO customerDTO = new CustomerDTO(resultSet.getInt("customerId"), resultSet.getString("customerName"),
                resultSet.getInt("addressId"), resultSet.getBoolean("active"));
        AddressDTO addressDTO = addressDAO.read(customerDTO.getAddressId());
        CityDTO cityDTO = cityDAO.read(addressDTO.getCityId());
        CountryDTO countryDTO = countryDAO.read((cityDTO.getCountryId()));
        if (addressDTO != null && cityDTO != null && countryDTO != null) {
            return new CustomerImpl(customerDTO, addressDTO, cityDTO, countryDTO);

        }
        throw new SQLException("Address, City and/or Country not found.");
    }

    private CustomerDTO getCustomerDTO(PreparedStatement statement) throws SQLException {
        ResultSet resultSet = statement.executeQuery();
        if (resultSet.next()) {
            return new CustomerDTO(resultSet.getInt("customerId"),
                    resultSet.getString("customerName"),
                    resultSet.getInt("addressId"),
                    resultSet.getBoolean("active"));
        }
        return null;
    }


}

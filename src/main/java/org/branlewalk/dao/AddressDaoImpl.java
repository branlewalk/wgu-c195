package org.branlewalk.dao;

import org.branlewalk.dto.AddressDTO;

import java.sql.*;


public class AddressDaoImpl extends DaoIdGenerator<AddressDTO> implements AddressDAO {
    private Connection connection;

    public AddressDaoImpl(Connection connection) {

        this.connection = connection;
    }

    public void create(AddressDTO addressDTO, String createdBy, Date createDate) throws SQLException {
        String query = "INSERT INTO address (addressId,address,address2,cityId,postalCode,phone,createDate,createdBy,lastUpdateBy) VALUES (?,?,?,?,?,?,?,?,?)";
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setInt(1, findId());
        statement.setString(2, addressDTO.getAddress());
        statement.setString(3, addressDTO.getAddress2());
        statement.setInt(4, addressDTO.getCityId());
        statement.setString(5, addressDTO.getPostalCode());
        statement.setString(6, addressDTO.getPhone());
        statement.setDate(7, createDate);
        statement.setString(8, createdBy);
        statement.setString(9, createdBy);
        statement.execute();
        statement.close();
    }

    @Override
    public AddressDTO read(int id) throws SQLException {
        String query = "SELECT addressId,address,address2,cityId,postalCode,phone FROM address WHERE addressId = ?";
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setInt(1,id);
        ResultSet resultSet = statement.executeQuery();
        if (resultSet.next()) {
            return new AddressDTO(resultSet.getInt("addressId"),
                    resultSet.getString("address"),
                    resultSet.getString("address2"),
                    resultSet.getInt("cityId"),
                    resultSet.getString("postalCode"),
                    resultSet.getString("phone"));
        }
        return null;
    }

    public void update(String lastUpdateBy, AddressDTO updateDTO) throws SQLException {
        String query = "UPDATE address SET address = ?, address2 = ?, cityId = ?, postalCode = ?, phone = ?, lastUpdateBy = ? WHERE addressId = ?";
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setString(1, updateDTO.getAddress());
        statement.setString(2, updateDTO.getAddress2());
        statement.setInt(3, updateDTO.getCityId());
        statement.setString(4, updateDTO.getPostalCode());
        statement.setString(5, updateDTO.getPhone());
        statement.setString(5, lastUpdateBy);
        statement.setInt(6, updateDTO.getAddressId());
        statement.execute();
        statement.close();

    }

    public void delete(int id) throws SQLException {
        String query = "DELETE FROM address WHERE addressId = ?";
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setInt(1,id);
        statement.execute();
        statement.close();
    }
}

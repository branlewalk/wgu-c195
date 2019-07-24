package org.branlewalk.dao;

import org.branlewalk.dto.AddressDTO;

import java.sql.*;


public class AddressDaoImpl extends DaoIdGenerator<AddressDTO> implements AddressDAO {
    private Connection connection;

    public AddressDaoImpl(Connection connection) {

        this.connection = connection;
    }

    public AddressDTO create(String address, String address2, int cityId, String postalCode, String phone, String createdBy) throws SQLException {
        String query = "INSERT INTO address (addressId,address,address2,cityId,postalCode,phone,createDate,createdBy,lastUpdateBy) VALUES (?,?,?,?,?,?,NOW(),?,?)";
        PreparedStatement statement = connection.prepareStatement(query);
        int id = findId();
        statement.setInt(1, id);
        statement.setString(2, address);
        statement.setString(3, address2);
        statement.setInt(4, cityId);
        statement.setString(5, postalCode);
        statement.setString(6, phone);
        statement.setString(7, createdBy);
        statement.setString(8, createdBy);
        statement.execute();
        statement.close();
        return new AddressDTO(id, address, address2, cityId, postalCode, phone);
    }

    @Override
    public AddressDTO read(int id) throws SQLException {
        String query = "SELECT addressId,address,address2,cityId,postalCode,phone FROM address WHERE addressId = ?";
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setInt(1, id);
        return getAddressDTO(statement);
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
        statement.setInt(1, id);
        statement.execute();
        statement.close();
    }

    public AddressDTO find(String name) throws SQLException {
        String query = "SELECT addressId,address,address2,cityId,postalCode,phone FROM address WHERE address = ?";
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setString(1, name);
        return getAddressDTO(statement);
    }

    private AddressDTO getAddressDTO(PreparedStatement statement) throws SQLException {
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
}

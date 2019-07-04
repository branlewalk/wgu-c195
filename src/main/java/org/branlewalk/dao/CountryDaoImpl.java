package org.branlewalk.dao;

import org.branlewalk.dto.CountryDTO;

import java.sql.*;


public class CountryDaoImpl implements CountryDAO {
    private Connection connection;

    public CountryDaoImpl(Connection connection) {

        this.connection = connection;
    }

    public void create(CountryDTO countryDTO, String createdBy, Date createDate) throws SQLException {
        String query = "INSERT INTO country (countryId,country,createDate,createdBy,lastUpdateBy) VALUES (?,?,?,?,?)";
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setInt(1, countryDTO.getCountryId());
        statement.setString(2, countryDTO.getCountry());
        statement.setDate(3, createDate);
        statement.setString(4, createdBy);
        statement.setString(5, createdBy);
        statement.execute();
        statement.close();
    }

    public CountryDTO read(int id) throws SQLException {
        String query = "SELECT countryId,country FROM country WHERE countryId = ?";
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setInt(1,id);
        ResultSet resultSet = statement.executeQuery();
        if (resultSet.next()) {
            return new CountryDTO(resultSet.getInt("countryId"), resultSet.getString("country"));
        }
        return null;
    }

    public void update(String lastUpdateBy, CountryDTO updateDTO) throws SQLException {
        String query = "UPDATE country SET country = ?, lastUpdateBy = ? WHERE countryId = ?";
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setString(1, updateDTO.getCountry());
        statement.setString(2, lastUpdateBy);
        statement.setInt(3, updateDTO.getCountryId());
        statement.execute();
        statement.close();

    }

    public void delete(int id) throws SQLException {
        String query = "DELETE FROM country WHERE countryId = ?";
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setInt(1,id);
        statement.execute();
        statement.close();
    }
}

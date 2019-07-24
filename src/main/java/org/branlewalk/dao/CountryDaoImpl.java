package org.branlewalk.dao;

import org.branlewalk.dto.CountryDTO;

import java.sql.*;


public class CountryDaoImpl extends DaoIdGenerator<CountryDTO> implements CountryDAO {
    private Connection connection;

    public CountryDaoImpl(Connection connection) {

        this.connection = connection;
    }

    public CountryDTO create(String country, String createdBy) throws SQLException {
        String query = "INSERT INTO country (countryId,country,createDate,createdBy,lastUpdateBy) VALUES (?,?,NOW(),?,?)";
        PreparedStatement statement = connection.prepareStatement(query);
        int id = findId();
        statement.setInt(1, id);
        statement.setString(2, country);
        statement.setString(3, createdBy);
        statement.setString(4, createdBy);
        statement.execute();
        statement.close();
        return new CountryDTO(id, country);
    }

    public CountryDTO read(int id) throws SQLException {
        String query = "SELECT countryId,country FROM country WHERE countryId = ?";
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setInt(1,id);
        return getCountryDTO(statement);
    }



    public void update(CountryDTO updateDTO, String lastUpdateBy) throws SQLException {
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

    public CountryDTO find(String name) throws SQLException {
        String query = "SELECT countryId,country FROM country WHERE country = ?";
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setString(1,name);
        return getCountryDTO(statement);
    }

    private CountryDTO getCountryDTO(PreparedStatement statement) throws SQLException {
        ResultSet resultSet = statement.executeQuery();
        if (resultSet.next()) {
            return new CountryDTO(resultSet.getInt("countryId"), resultSet.getString("country"));
        }
        return null;
    }
}

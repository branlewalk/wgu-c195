package org.branlewalk.dao;

import org.branlewalk.dto.CityDTO;

import java.sql.*;


public class CityDaoImpl extends DaoIdGenerator<CityDTO> implements CityDAO {
    private Connection connection;

    public CityDaoImpl(Connection connection) {

        this.connection = connection;
    }

    public CityDTO create(String city, int countryId, String createdBy) throws SQLException {
        String query = "INSERT INTO city (cityId,city,countryId,createDate,createdBy,lastUpdateBy) VALUES (?,?,?,NOW(),?,?)";
        PreparedStatement statement = connection.prepareStatement(query);
        int id = findId();
        statement.setInt(1, id);
        statement.setString(2, city);
        statement.setInt(3, countryId);
        statement.setString(4, createdBy);
        statement.setString(5, createdBy);
        statement.execute();
        statement.close();
        return new CityDTO(id, city, countryId);
    }

    public CityDTO read(int id) throws SQLException {
        String query = "SELECT cityId,city,countryId FROM city WHERE cityId = ?";
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setInt(1, id);
        return getCityDTO(statement);
    }

    public void update(String lastUpdateBy, CityDTO updateDTO) throws SQLException {
        String query = "UPDATE city SET city = ?, countryID = ?, lastUpdateBy = ? WHERE cityId = ?";
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setString(1, updateDTO.getCity());
        statement.setInt(2, updateDTO.getCountryId());
        statement.setString(3, lastUpdateBy);
        statement.setInt(4, updateDTO.getCityId());
        statement.execute();
        statement.close();

    }

    public void delete(int id) throws SQLException {
        String query = "DELETE FROM city WHERE cityId = ?";
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setInt(1, id);
        statement.execute();
        statement.close();
    }

    public CityDTO find(String name) throws SQLException {
        String query = "SELECT cityId,city,countryId FROM city WHERE city = ?";
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setString(1, name);
        return getCityDTO(statement);
    }

    private CityDTO getCityDTO(PreparedStatement statement) throws SQLException {
        ResultSet resultSet = statement.executeQuery();
        if (resultSet.next()) {
            return new CityDTO(resultSet.getInt("cityId"),
                    resultSet.getString("city"),
                    resultSet.getInt("countryId"));
        }
        return null;
    }
}

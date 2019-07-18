package org.branlewalk.dao;

import org.branlewalk.dto.CityDTO;

import java.sql.*;


public class CityDaoImpl extends DaoIdGenerator<CityDTO> implements CityDAO {
    private Connection connection;

    public CityDaoImpl(Connection connection) {

        this.connection = connection;
    }

    public void create(CityDTO cityDTO, String createdBy, Date createDate) throws SQLException {
        String query = "INSERT INTO city (cityId,city,countryId,createDate,createdBy,lastUpdateBy) VALUES (?,?,?,?,?,?)";
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setInt(1, findId());
        statement.setString(2, cityDTO.getCity());
        statement.setInt(3, cityDTO.getCountryId());
        statement.setDate(4, createDate);
        statement.setString(5, createdBy);
        statement.setString(6, createdBy);
        statement.execute();
        statement.close();
    }

    public CityDTO read(int id) throws SQLException {
        String query = "SELECT cityId,city,countryId FROM city WHERE cityId = ?";
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setInt(1,id);
        ResultSet resultSet = statement.executeQuery();
        if (resultSet.next()) {
            return new CityDTO(resultSet.getInt("cityId"),
                    resultSet.getString("city"),
                    resultSet.getInt("countryId"));
        }
        return null;
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
        statement.setInt(1,id);
        statement.execute();
        statement.close();
    }
}

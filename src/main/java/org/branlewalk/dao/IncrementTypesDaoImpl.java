package org.branlewalk.dao;

import org.branlewalk.dto.IncrementTypesDTO;

import java.sql.*;


public class IncrementTypesDaoImpl implements IncrementTypesDAO {
    private Connection connection;

    public IncrementTypesDaoImpl(Connection connection) {
        this.connection = connection;
    }

    public void create(IncrementTypesDTO incrementTypesDTO, String createdBy, Date createDate) throws SQLException {
        String query = "INSERT INTO incrementTypes (incrementTypesId,incrementTypes) VALUES (?,?)";
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setInt(1, incrementTypesDTO.getIncrementTypesId());
        statement.setString(2, incrementTypesDTO.getIncrementDescription());
        statement.execute();
        statement.close();
    }

    public IncrementTypesDTO read(int id) throws SQLException {
        String query = "SELECT incrementTypesId,incrementTypes FROM incrementtypes WHERE incrementTypesId = ?";
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setInt(1,id);
        ResultSet resultSet = statement.executeQuery();
        if (resultSet.next()) {
            return new IncrementTypesDTO(resultSet.getInt("incrementTypesId"),
                    resultSet.getString("incrementTypesDescription"));
        }
        return null;
    }

    public void update(String lastUpdateBy, IncrementTypesDTO updateDTO) throws SQLException {
        String query = "UPDATE incrementtypes SET incrementTypesName = ? WHERE incrementTypesId = ?";
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setString(1, updateDTO.getIncrementDescription());
        statement.setInt(2, updateDTO.getIncrementTypesId());
        statement.execute();
        statement.close();

    }

    public void delete(int id) throws SQLException {
        String query = "DELETE FROM incrementtypes WHERE incrementTypesId = ?";
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setInt(1,id);
        statement.execute();
        statement.close();
    }
}

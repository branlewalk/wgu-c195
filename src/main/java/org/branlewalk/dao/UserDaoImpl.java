package org.branlewalk.dao;

import org.branlewalk.dto.UserDTO;
import java.sql.*;


public class UserDaoImpl implements UserDAO {
    private Connection connection;

    public UserDaoImpl(Connection connection) {

        this.connection = connection;
    }

    public void create(UserDTO userDTO, String createdBy, Date createDate) throws SQLException {
        String query = "INSERT INTO user (userId,userName,password,active,createDate,createdBy,lastUpdateBy) VALUES (?,?,?,?,?,?,?)";
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setInt(1, userDTO.getId());
        statement.setString(2, userDTO.getName());
        statement.setString(3, userDTO.getPassword());
        statement.setBoolean(4, userDTO.isActive());
        statement.setDate(5, createDate);
        statement.setString(6, createdBy);
        statement.setString(7, createdBy);
        statement.execute();
        statement.close();
    }

    public UserDTO read(int id) throws SQLException {
        String query = "SELECT userId,userName,password,active FROM user WHERE userId = ?";
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setInt(1,id);
        ResultSet resultSet = statement.executeQuery();
        if (resultSet.next()) {
            return new UserDTO(resultSet.getInt("userId"),
                    resultSet.getString("userName"),
                    resultSet.getString("password"),
                    resultSet.getBoolean("active"));
        }
        return null;
    }

    public void update(String lastUpdateBy, UserDTO updateDTO) throws SQLException {
        String query = "UPDATE user SET userName = ?, password = ?, active = ?, lastUpdateBy = ? WHERE userId = ?";
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setString(1, updateDTO.getName());
        statement.setString(2, updateDTO.getPassword());
        statement.setBoolean(3, updateDTO.isActive());
        statement.setString(4, lastUpdateBy);
        statement.setInt(5, updateDTO.getId());
        statement.execute();
        statement.close();

    }

    public void delete(int id) throws SQLException {
        String query = "DELETE FROM user WHERE userId = ?";
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setInt(1,id);
        statement.execute();
        statement.close();
    }
}

package org.branlewalk.dao;

import org.branlewalk.dto.UserDTO;

import java.sql.*;


public class UserDAO {
    private Connection connection;

    public UserDAO(Connection connection) {

        this.connection = connection;
    }

    public void add(UserDTO userDTO, String byme, Date createDate) throws SQLException {
        PreparedStatement statement = connection.prepareStatement(
                "INSERT INTO user (userId,userName,password,active,createDate,createdBy,lastUpdateBy) VALUES (?,?,?,?,?,?,?)");
        statement.setInt(1, userDTO.getId());
        statement.setString(2, userDTO.getName());
        statement.setString(3, userDTO.getPassword());
        statement.setBoolean(4, userDTO.isActive());
        statement.setDate(5, createDate);
        statement.setString(6, byme);
        statement.setString(7, byme);
        statement.execute();
        statement.close();
    }

    public UserDTO get(int id) throws SQLException {
        PreparedStatement statement = connection.prepareStatement("SELECT userId,userName,password,active FROM user WHERE userId = ?");
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
}

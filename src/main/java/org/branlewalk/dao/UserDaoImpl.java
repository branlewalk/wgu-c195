package org.branlewalk.dao;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.branlewalk.domain.User;
import org.branlewalk.domain.UserImpl;
import org.branlewalk.dto.UserDTO;

import java.sql.*;


public class UserDaoImpl extends DaoIdGenerator<UserDTO> implements UserDAO {


    public UserDaoImpl(Connection connection) {
        super(connection, "user", "userId");

    }

    public UserDTO create(String userName, String password, String createdBy) throws SQLException {
        String query = "INSERT INTO user (userId,userName,password,active,createDate,createdBy,lastUpdateBy) VALUES (?,?,?,?,NOW(),?,?)";
        PreparedStatement statement = connection.prepareStatement(query);
        int id = findId();
        statement.setInt(1, id);
        statement.setString(2, userName);
        statement.setString(3, password);
        statement.setBoolean(4, true);
        statement.setString(5, createdBy);
        statement.setString(6, createdBy);
        statement.execute();
        statement.close();
        return new UserDTO(id ,userName, password, true);
    }

    @Override
    public User create(String userName, String password) throws SQLException {
        UserDTO userDTO = create(userName, password, userName);
        return new UserImpl(userDTO);
    }

    @Override
    public UserDTO read(int id) throws SQLException {
        String query = "SELECT userId,userName,password,active FROM user WHERE userId = ?";
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setInt(1, id);
        return getUserDTO(statement);
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
        statement.setInt(1, id);
        statement.execute();
        statement.close();
    }

    public UserDTO find(String name) throws SQLException {
        String query = "SELECT userId,userName,password,active FROM user WHERE userName = ?";
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setString(1, name);
        return getUserDTO(statement);
    }

    private UserDTO getUserDTO(PreparedStatement statement) throws SQLException {
        ResultSet resultSet = statement.executeQuery();
        if (resultSet.next()) {
            return new UserDTO(resultSet.getInt("userId"),
                    resultSet.getString("userName"),
                    resultSet.getString("password"),
                    resultSet.getBoolean("active"));
        }
        return null;
    }

    public ObservableList<User> findAll() throws SQLException {
        ObservableList<User> users = FXCollections.observableArrayList();
        String query = "SELECT * FROM user WHERE active = 1";
        PreparedStatement statement = connection.prepareStatement(query);
        ResultSet resultSet = statement.executeQuery();
        while (resultSet.next()) {
            users.add(getUser(resultSet));
        }
        return users;
    }

    private UserImpl getUser(ResultSet resultSet) throws SQLException {
        UserDTO userDTO = new UserDTO(resultSet.getInt("userId"), resultSet.getString("userName"),
                resultSet.getString("password"), resultSet.getBoolean("active"));
        return new UserImpl(userDTO);
    }
}

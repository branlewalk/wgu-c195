package org.branlewalk.dao;

import javafx.collections.ObservableList;
import org.branlewalk.domain.User;
import org.branlewalk.dto.UserDTO;

import java.sql.*;


public interface UserDAO {

    User create(String userName, String password) throws SQLException;

    UserDTO read(int id) throws SQLException;

    void update(String lastUpdateBy, UserDTO updateDTO) throws SQLException;

    void delete(int id) throws SQLException;

    UserDTO find(String name) throws SQLException;

    ObservableList<User> findAll() throws SQLException;
}
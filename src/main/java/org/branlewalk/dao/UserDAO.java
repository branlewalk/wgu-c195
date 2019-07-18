package org.branlewalk.dao;

import org.branlewalk.dto.UserDTO;

import java.sql.*;


public interface UserDAO {

    void create(String userName, String password, String createdBy, Date createDate) throws SQLException;

    UserDTO read(int id) throws SQLException;

    void update(String lastUpdateBy, UserDTO updateDTO) throws SQLException;

    void delete(int id) throws SQLException;

    UserDTO find(String name) throws SQLException;
}
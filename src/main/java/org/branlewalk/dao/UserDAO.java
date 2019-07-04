package org.branlewalk.dao;

import org.branlewalk.dto.UserDTO;

import java.sql.*;


public interface UserDAO {

    void create(UserDTO userDTO, String createdBy, Date createDate) throws SQLException;

    UserDTO read(int id) throws SQLException;

    void update(String lastUpdateBy, UserDTO updateDTO) throws SQLException;

    void delete(int id) throws SQLException;
}
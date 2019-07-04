package org.branlewalk.dao;

import org.branlewalk.dto.CustomerDTO;

import java.sql.Date;
import java.sql.SQLException;


public interface CustomerDAO {

    void create(CustomerDTO userDTO, String createdBy, Date createDate) throws SQLException;

    CustomerDTO read(int id) throws SQLException;

    void update(String lastUpdateBy, CustomerDTO updateDTO) throws SQLException;

    void delete(int id) throws SQLException;
}
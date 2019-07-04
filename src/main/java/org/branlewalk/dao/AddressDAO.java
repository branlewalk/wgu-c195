package org.branlewalk.dao;

import org.branlewalk.dto.AddressDTO;

import java.sql.Date;
import java.sql.SQLException;


public interface AddressDAO {

    void create(AddressDTO userDTO, String createdBy, Date createDate) throws SQLException;

    AddressDTO read(int id) throws SQLException;

    void update(String lastUpdateBy, AddressDTO updateDTO) throws SQLException;

    void delete(int id) throws SQLException;
}
package org.branlewalk.dao;

import org.branlewalk.dto.AddressDTO;

import java.sql.SQLException;


public interface AddressDAO {

    AddressDTO create(String address, String address2, int cityId, String postalCode, String phone, String createdBy) throws SQLException;

    AddressDTO read(int id) throws SQLException;

    void update(String lastUpdateBy, AddressDTO updateDTO) throws SQLException;

    void delete(int id) throws SQLException;

    AddressDTO find(String name) throws SQLException;
}
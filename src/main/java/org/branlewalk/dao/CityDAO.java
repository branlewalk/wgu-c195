package org.branlewalk.dao;

import org.branlewalk.dto.CityDTO;

import java.sql.SQLException;


public interface CityDAO {

    CityDTO create(String city, int countryId, String createdBy) throws SQLException;

    CityDTO read(int id) throws SQLException;

    void update(String lastUpdateBy, CityDTO updateDTO) throws SQLException;

    void delete(int id) throws SQLException;

    CityDTO find(String name) throws SQLException;
}
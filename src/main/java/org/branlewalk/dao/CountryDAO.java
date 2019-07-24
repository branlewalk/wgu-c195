package org.branlewalk.dao;

import org.branlewalk.dto.CountryDTO;

import java.sql.SQLException;


public interface CountryDAO {

    CountryDTO create(String country, String createdBy) throws SQLException;

    CountryDTO read(int id) throws SQLException;

    void update(CountryDTO updateDTO, String lastUpdateBy) throws SQLException;

    void delete(int id) throws SQLException;

    CountryDTO find(String name) throws SQLException;
}
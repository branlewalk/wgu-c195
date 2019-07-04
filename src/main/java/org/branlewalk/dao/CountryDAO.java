package org.branlewalk.dao;

import org.branlewalk.dto.CountryDTO;

import java.sql.Date;
import java.sql.SQLException;


public interface CountryDAO {

    void create(CountryDTO userDTO, String createdBy, Date createDate) throws SQLException;

    CountryDTO read(int id) throws SQLException;

    void update(String lastUpdateBy, CountryDTO updateDTO) throws SQLException;

    void delete(int id) throws SQLException;
}
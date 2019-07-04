package org.branlewalk.dao;

import org.branlewalk.dto.CityDTO;

import java.sql.Date;
import java.sql.SQLException;


public interface CityDAO {

    void create(CityDTO userDTO, String createdBy, Date createDate) throws SQLException;

    CityDTO read(int id) throws SQLException;

    void update(String lastUpdateBy, CityDTO updateDTO) throws SQLException;

    void delete(int id) throws SQLException;
}
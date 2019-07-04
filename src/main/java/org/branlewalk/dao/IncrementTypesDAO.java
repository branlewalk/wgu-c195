package org.branlewalk.dao;

import org.branlewalk.dto.IncrementTypesDTO;

import java.sql.Date;
import java.sql.SQLException;


public interface IncrementTypesDAO {

    void create(IncrementTypesDTO userDTO, String createdBy, Date createDate) throws SQLException;

    IncrementTypesDTO read(int id) throws SQLException;

    void update(String lastUpdateBy, IncrementTypesDTO updateDTO) throws SQLException;

    void delete(int id) throws SQLException;
}
package org.branlewalk.dao;

import org.branlewalk.dto.ReminderDTO;

import java.sql.Date;
import java.sql.SQLException;


public interface ReminderDAO {

    void create(ReminderDTO userDTO, String createdBy, Date createDate) throws SQLException;

    ReminderDTO read(int id) throws SQLException;

    void update(String lastUpdateBy, ReminderDTO updateDTO) throws SQLException;

    void delete(int id) throws SQLException;
}
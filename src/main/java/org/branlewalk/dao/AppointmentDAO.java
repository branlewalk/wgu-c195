package org.branlewalk.dao;

import org.branlewalk.dto.AppointmentDTO;
import org.branlewalk.dto.AppointmentDTO;

import java.sql.Date;
import java.sql.SQLException;


public interface AppointmentDAO {

    void create(AppointmentDTO appointmentDTO, String createdBy, Date createDate) throws SQLException;

    AppointmentDTO read(int id) throws SQLException;

    void update(String lastUpdateBy, AppointmentDTO updateDTO) throws SQLException;

    void delete(int id) throws SQLException;
}
package org.branlewalk.dao;

import javafx.collections.ObservableList;
import org.branlewalk.domain.Appointment;
import org.branlewalk.dto.AppointmentDTO;

import java.util.Date;
import java.sql.SQLException;
import java.util.List;


public interface AppointmentDAO {

    AppointmentDTO create(int customerId, int userId, String title, String description, String location, String contact, String type, String url, Date start, Date end, String createdBy) throws SQLException;

    Appointment create(String customerName, String title, String description, String location, String contact, String type, String url, Date start, Date end) throws SQLException;

    AppointmentDTO read(int id) throws SQLException;

    void update(String lastUpdateBy, AppointmentDTO updateDTO) throws SQLException;

    void delete(int id) throws SQLException;

    List<String> findTypes() throws SQLException;

    ObservableList<Appointment> findAll() throws SQLException;

    ObservableList<Appointment> findAllForDate(Date date) throws SQLException;


    ObservableList<Appointment> findAllForDateRange(Date beginDate, Date endDate) throws SQLException;
}
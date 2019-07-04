package org.branlewalk.dao;

import org.branlewalk.dto.ReminderDTO;

import java.sql.*;


public class ReminderDaoImpl implements ReminderDAO {
    private Connection connection;

    public ReminderDaoImpl(Connection connection) {

        this.connection = connection;
    }

    public void create(ReminderDTO reminderDTO, String createdBy, Date createDate) throws SQLException {
        String query = "INSERT INTO reminder (reminderId,reminderDate,snoozeIncrement,snoozeIncrementTypeId,appointmentId,createDate,createdBy,lastUpdateBy) VALUES (?,?,?,?,?,?,?,?,?)";
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setInt(1, reminderDTO.getReminderId());
        statement.setDate(2, reminderDTO.getReminderDate());
        statement.setInt(3, reminderDTO.getSnoozeIncrement());
        statement.setInt(4, reminderDTO.getSnoozeIncrementTypeId());
        statement.setInt(5, reminderDTO.getAppointmentId());
        statement.setDate(6, createDate);
        statement.setString(7, createdBy);
        statement.setString(8, createdBy);
        statement.execute();
        statement.close();
    }

    public ReminderDTO read(int id) throws SQLException {
        String query = "SELECT reminderId,reminderDate,snoozeIncrement,snoozeIncrementTypeId,appointmentId FROM reminder WHERE reminderId = ?";
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setInt(1,id);
        ResultSet resultSet = statement.executeQuery();
        if (resultSet.next()) {
            return new ReminderDTO(resultSet.getInt("reminderId"),
                    resultSet.getDate("reminderDate"),
                    resultSet.getInt("snoozeIncrement"),
                    resultSet.getInt("snoozeIncrementTypeId"),
                    resultSet.getInt("appointmentId"));
        }
        return null;
    }

    public void update(String lastUpdateBy, ReminderDTO updateDTO) throws SQLException {
        String query = "UPDATE reminder SET reminder = ?, reminder2 = ?, cityId = ?, postalCode = ?, phone = ?, lastUpdateBy = ? WHERE reminderId = ?";
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setDate(1, updateDTO.getReminderDate());
        statement.setInt(2, updateDTO.getSnoozeIncrement());
        statement.setInt(3, updateDTO.getSnoozeIncrementTypeId());
        statement.setInt(4, updateDTO.getAppointmentId());
        statement.setString(5, lastUpdateBy);
        statement.setInt(6, updateDTO.getReminderId());
        statement.execute();
        statement.close();

    }

    public void delete(int id) throws SQLException {
        String query = "DELETE FROM reminder WHERE reminderId = ?";
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setInt(1,id);
        statement.execute();
        statement.close();
    }
}

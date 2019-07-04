package org.branlewalk.dao;

import org.branlewalk.dto.AppointmentDTO;

import java.sql.*;


public class AppointmentDaoImpl implements AppointmentDAO {
    private Connection connection;

    public AppointmentDaoImpl(Connection connection) {

        this.connection = connection;
    }

    public void create(AppointmentDTO dto, String createdBy, Date createDate) throws SQLException {
        String query = "INSERT INTO appointment (appointmentId,customerId,userId,title,description,location,contact,type,url," +
                "start,end,createDate,createdBy,lastUpdateBy) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setInt(1, dto.getAppointmentId());
        statement.setInt(2, dto.getCustomerId());
        statement.setInt(3, dto.getUserId());
        statement.setString(4, dto.getTitle());
        statement.setString(5, dto.getDescription());
        statement.setString(6, dto.getLocation());
        statement.setString(7, dto.getContact());
        statement.setString(8, dto.getType());
        statement.setString(9, dto.getUrl());
        statement.setDate(10, dto.getStart());
        statement.setDate(11, dto.getEnd());
        statement.setDate(12, createDate);
        statement.setString(13, createdBy);
        statement.setString(14, createdBy);
        statement.execute();
        statement.close();
    }

    public AppointmentDTO read(int id) throws SQLException {
        String query = "SELECT appointmentId,customerId,userId,title,description,location,contact,type,url,start,end" +
                " FROM user WHERE appointmentId = ?";
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setInt(1, id);
        ResultSet resultSet = statement.executeQuery();
        if (resultSet.next()) {
            return new AppointmentDTO(resultSet.getInt("appointmentId"),
                    resultSet.getInt("customerId"),
                    resultSet.getInt("userId"),
                    resultSet.getString("title"),
                    resultSet.getString("description"),
                    resultSet.getString("location"),
                    resultSet.getString("contact"),
                    resultSet.getString("type"),
                    resultSet.getString("url"),
                    resultSet.getDate("start"),
                    resultSet.getDate("end"));
        }
        return null;
    }

    public void update(String lastUpdateBy, AppointmentDTO updateDTO) throws SQLException {
        String query = "UPDATE appointment SET customerId = ?, userId = ?, title = ?, , description = ?, location = ?," +
                "contact = ?, type = ?, url = ?, start = ?, end = ?, lastUpdateBy = ? WHERE appointmentId = ?";
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setInt(1, updateDTO.getCustomerId());
        statement.setInt(2, updateDTO.getUserId());
        statement.setString(3, updateDTO.getTitle());
        statement.setString(4, updateDTO.getDescription());
        statement.setString(5, updateDTO.getLocation());
        statement.setString(6, updateDTO.getContact());
        statement.setString(7, updateDTO.getType());
        statement.setString(8, updateDTO.getUrl());
        statement.setDate(9, updateDTO.getStart());
        statement.setDate(10, updateDTO.getEnd());
        statement.setString(11, lastUpdateBy);
        statement.setInt(12, updateDTO.getAppointmentId());
        statement.execute();
        statement.close();

    }

    public void delete(int appointmentId) throws SQLException {
        String query = "DELETE FROM user WHERE userId = ?";
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setInt(1, appointmentId);
        statement.execute();
        statement.close();
    }
}

package org.branlewalk.dao;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.branlewalk.domain.Appointment;
import org.branlewalk.domain.AppointmentImpl;
import org.branlewalk.dto.*;


import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.ArrayList;
import java.util.List;


public class AppointmentDaoImpl extends DaoIdGenerator<AppointmentDTO> implements AppointmentDAO {

    private final String username;
    private final CountryDaoImpl countryDAO;
    private final CityDaoImpl cityDAO;
    private final AddressDaoImpl addressDAO;
    private final CustomerDaoImpl customerDAO;
    private final UserDaoImpl userDAO;

    public AppointmentDaoImpl(Connection connection, String username) {
        super(connection, "appointment", "appointmentId");
        this.username = username;
        countryDAO = new CountryDaoImpl(connection);
        cityDAO = new CityDaoImpl(this.connection);
        addressDAO = new AddressDaoImpl(this.connection);
        customerDAO = new CustomerDaoImpl(this.connection, username);
        userDAO = new UserDaoImpl(this.connection);
    }

    public AppointmentDTO create(int customerId, int userId, String title, String description, String location, String contact, String type, String url, Date start, Date end, String createdBy) throws SQLException {
        String query = "INSERT INTO appointment (appointmentId,customerId,userId,title,description,location,contact,type,url," +
                "start,end,createDate,createdBy,lastUpdateBy) VALUES (?,?,?,?,?,?,?,?,?,?,?,NOW(),?,?)";
        PreparedStatement statement = connection.prepareStatement(query);
        int id = findId();
        Timestamp s = new Timestamp(start.getTime());
        Timestamp e = new Timestamp(end.getTime());
        statement.setInt(1, id);
        statement.setInt(2, customerId);
        statement.setInt(3, userId);
        statement.setString(4, title);
        statement.setString(5, description);
        statement.setString(6, location);
        statement.setString(7, contact);
        statement.setString(8, type);
        statement.setString(9, url);
        statement.setTimestamp(10, s);
        statement.setTimestamp(11, e);
        statement.setString(12, createdBy);
        statement.setString(13, createdBy);
        statement.execute();
        statement.close();
        return new AppointmentDTO(id, customerId, userId, title, description, location, contact, type, url, start, end);
    }

    @Override
    public Appointment create(String customerName, String title, String description, String location, String contact, String type, String url, Date start, Date end) throws SQLException {
        CustomerDTO customerDTO = customerDAO.find(customerName);
        UserDTO userDTO = userDAO.find(username);
        AppointmentDTO appointmentDTO = create(customerDTO.getCustomerId(), userDTO.getId(), title, description, location, contact, type, url, start, end, username);
        return new AppointmentImpl(appointmentDTO, userDTO, customerDTO);
    }

    public AppointmentDTO read(int id) throws SQLException {
        String query = "SELECT appointmentId,customerId,userId,title,description,location,contact,type,url,start,end" +
                " FROM appointment WHERE appointmentId = ?";
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
        String query = "UPDATE appointment SET customerId = ?, userId = ?, title = ?, description = ?, location = ?," +
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
        statement.setTimestamp(9, new Timestamp(updateDTO.getStart().getTime()));
        statement.setTimestamp(10, new Timestamp(updateDTO.getEnd().getTime()));
        statement.setString(11, lastUpdateBy);
        statement.setInt(12, updateDTO.getAppointmentId());
        statement.execute();
        statement.close();

    }

    public void delete(int appointmentId) throws SQLException {
        String query = "DELETE FROM appointment WHERE appointmentId = ?";
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setInt(1, appointmentId);
        statement.execute();
        statement.close();
    }

    @Override
    public List<String> findTypes() throws SQLException {
        String query = "SELECT DISTINCT type FROM appointment";
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(query);
        ArrayList<String> types = new ArrayList<>();
        while (resultSet.next()) {
            types.add(resultSet.getString("type"));
        }
        return types;
    }

    public ObservableList<Appointment> findAll() throws SQLException {
        ObservableList<Appointment> appointments = FXCollections.observableArrayList();
        String query = "SELECT * FROM appointment";
        PreparedStatement statement = connection.prepareStatement(query);
        ResultSet resultSet = statement.executeQuery();
        while (resultSet.next()) {
            appointments.add(getAppointment(resultSet));
        }
        return appointments;
    }

    public ObservableList<Appointment> findAllForDate(Date date) throws SQLException {
        ObservableList<Appointment> appointments = FXCollections.observableArrayList();
        LocalDate day = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        LocalDateTime begin = day.atStartOfDay();
        LocalDateTime end = day.plusDays(1).atStartOfDay();
        return findAll(appointments, begin, end);
    }

    @Override
    public ObservableList<Appointment> findAllForDateRange(Date beginDate, Date endDate) throws SQLException {
        ObservableList<Appointment> appointments = FXCollections.observableArrayList();
        LocalDateTime begin = getStartOfDay(beginDate);
        LocalDateTime end = getStartOfDay(endDate).plusDays(1);
        return findAll(appointments, begin, end);
    }

    private ObservableList<Appointment> findAll(ObservableList<Appointment> appointments, LocalDateTime begin, LocalDateTime end) throws SQLException {
        String query = "SELECT * FROM appointment WHERE start > ? AND start < ? ";
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setTimestamp(1, Timestamp.valueOf(begin));
        statement.setTimestamp(2, Timestamp.valueOf(end));
        ResultSet resultSet = statement.executeQuery();
        while (resultSet.next()) {
            appointments.add(getAppointment(resultSet));
        }
        return appointments;
    }

    private LocalDateTime getStartOfDay(Date beginDate) {
        LocalDate day = beginDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        return day.atStartOfDay();
    }

    private Appointment getAppointment(ResultSet resultSet) throws SQLException {
        AppointmentDTO appointmentDTO = new AppointmentDTO(resultSet.getInt("appointmentId"),
                resultSet.getInt("customerId"), resultSet.getInt("userId"),
                resultSet.getString("title"), resultSet.getString("description"),
                resultSet.getString("location"), resultSet.getString("contact"),
                resultSet.getString("type"), resultSet.getString("url"),
                resultSet.getDate("start"), resultSet.getDate("end"));
        UserDTO userDTO = userDAO.read(appointmentDTO.getUserId());
        CustomerDTO customerDTO = customerDAO.read(appointmentDTO.getCustomerId());
        AddressDTO addressDTO = addressDAO.read(customerDTO.getAddressId());
        CityDTO cityDTO = cityDAO.read(addressDTO.getCityId());
        CountryDTO countryDTO = countryDAO.read((cityDTO.getCountryId()));
        if (addressDTO != null && cityDTO != null && countryDTO != null) {
            return new AppointmentImpl(appointmentDTO, userDTO, customerDTO);

        }
        throw new SQLException("Address, City and/or Country not found.");
    }
}

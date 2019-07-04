package org.branlewalk.dao;

import org.branlewalk.dto.AppointmentDTO;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.sql.*;
import java.util.Calendar;
import java.util.TimeZone;

import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.hamcrest.core.IsNull.nullValue;
import static org.junit.Assert.assertThat;

public class AppointmentDAOTest {

    private Connection connection;
    private String selectQuery = "SELECT appointmentId,customerId,userId,title,description,location,contact,type,url," +
            "start,end,createDate,createdBy,lastUpdateBy FROM appointment";

    @Before
    public void setup() throws ClassNotFoundException, SQLException {
        String driver = "com.mysql.cj.jdbc.Driver";
        String db = "U05vOU";
        String url = "jdbc:mysql://52.206.157.109/" + db;
        String user = "U05vOU";
        String pass = "53688621490";
        Class.forName(driver);
        connection = DriverManager.getConnection(url, user, pass);
        System.out.println("Connected to database : " + db);
    }

    @After
    public void tearDown() throws Exception {
        connection.close();
    }

    @Test
    public void create() throws SQLException {
        AppointmentDTO dto = createUser();
        Date createDate = getDate(1985,3,25);
        String createdBy = "byme";
        new AppointmentDaoImpl(connection).create(dto, createdBy, createDate);
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(selectQuery);
        assertThat(resultSet.next(), is(true));
        assertThat(resultSet.getInt("appointmentId"), is(dto.getAppointmentId()));
        assertThat(resultSet.getInt("customerId"), is(dto.getCustomerId()));
        assertThat(resultSet.getInt("userId"), is(dto.getUserId()));
        assertThat(resultSet.getString("title"), is(dto.getTitle()));
        assertThat(resultSet.getString("description"), is(dto.getDescription()));
        assertThat(resultSet.getString("location"), is(dto.getLocation()));
        assertThat(resultSet.getString("contact"), is(dto.getContact()));
        assertThat(resultSet.getString("type"), is(dto.getType()));
        assertThat(resultSet.getString("url"), is(dto.getUrl()));
        assertThat(resultSet.getDate("start"), is(dto.getStart()));
        assertThat(resultSet.getDate("end"), is(dto.getEnd()));
        assertThat(resultSet.getDate("createDate"), is(createDate));
        assertThat(resultSet.getString("createdBy"), is(createdBy));
        assertThat(resultSet.getString("lastUpdateBy"), is(createdBy));
        resultSet.close();
        deleteUser(dto);
    }

    @Test
    public void read() throws SQLException {
        AppointmentDAO AppointmentDAO = new AppointmentDaoImpl(connection);
        AppointmentDTO dto = createUser();
        AppointmentDAO.create(dto, "byme", getDate(1985,8,2));
        AppointmentDTO actualDTO = AppointmentDAO.read(dto.getAppointmentId());
        assertThat(actualDTO, notNullValue());
        assertThat(actualDTO.getAppointmentId(), is(dto.getAppointmentId()));
        assertThat(actualDTO.getCustomerId(), is(dto.getCustomerId()));
        assertThat(actualDTO.getUserId(), is(dto.getUserId()));
        assertThat(actualDTO.getTitle(), is(dto.getTitle()));
        assertThat(actualDTO.getDescription(), is(dto.getDescription()));
        assertThat(actualDTO.getLocation(), is(dto.getLocation()));
        assertThat(actualDTO.getContact(), is(dto.getContact()));
        assertThat(actualDTO.getType(), is(dto.getType()));
        assertThat(actualDTO.getUrl(), is(dto.getUrl()));
        assertThat(actualDTO.getStart(), is(dto.getStart()));
        assertThat(actualDTO.getEnd(), is(dto.getEnd()));
        deleteUser(dto);
    }

    @Test
    public void read_userNotFound() throws SQLException {
        AppointmentDAO AppointmentDAO = new AppointmentDaoImpl(connection);
        AppointmentDTO actualDTO = AppointmentDAO.read(33);
        assertThat(actualDTO, nullValue());
    }

    @Test
    public void update() throws SQLException {
        AppointmentDAO AppointmentDAO = new AppointmentDaoImpl(connection);
        AppointmentDTO dto = createUser();
        AppointmentDAO.create(dto, "byme", getDate(2016,5,31));
        AppointmentDTO updateDTO = new AppointmentDTO(dto.getCustomerId(),1, 1, "title", "description", "loc", "contact", "type", "url", getDate(1999,1,25 ), getDate(1999, 1,25));
        AppointmentDAO.update( "bythem", updateDTO);
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(selectQuery);
        assertThat(resultSet.next(), is(true));
        assertThat(resultSet.getInt("appointmentId"), is(dto.getAppointmentId()));
        assertThat(resultSet.getInt("customerId"), is(dto.getCustomerId()));
        assertThat(resultSet.getInt("userId"), is(dto.getUserId()));
        assertThat(resultSet.getString("title"), is(dto.getTitle()));
        assertThat(resultSet.getString("description"), is(dto.getDescription()));
        assertThat(resultSet.getString("location"), is(dto.getLocation()));
        assertThat(resultSet.getString("contact"), is(dto.getContact()));
        assertThat(resultSet.getString("type"), is(dto.getType()));
        assertThat(resultSet.getString("url"), is(dto.getUrl()));
        assertThat(resultSet.getDate("start"), is(dto.getStart()));
        assertThat(resultSet.getDate("end"), is(dto.getEnd()));
        assertThat(resultSet.getString("lastUpdateBy"), is("bythem"));
        statement.close();
        deleteUser(dto);
    }

    @Test
    public void delete() throws SQLException {
        AppointmentDAO appointmentDAO = new AppointmentDaoImpl(connection);
        AppointmentDTO dto = createUser();
        appointmentDAO.create(dto,"byme", getDate(1999,12,31));
        assertThat(appointmentDAO.read(dto.getAppointmentId()), notNullValue());
        appointmentDAO.delete(dto.getAppointmentId());
        PreparedStatement statement = connection.prepareStatement("SELECT * FROM appointment WHERE appointmentId = ?");
        statement.setInt(1,dto.getAppointmentId());
        ResultSet resultSet = statement.executeQuery();
        assertThat(resultSet.next(), is(false));
    }


    private void deleteUser(AppointmentDTO dto) throws SQLException {
        PreparedStatement statement = connection.prepareStatement("DELETE FROM appointment WHERE appointmentId = ?");
        statement.setInt(1, dto.getAppointmentId());
        statement.execute();
        statement.close();
    }

    private AppointmentDTO createUser() throws SQLException {
        AppointmentDTO dto = new AppointmentDTO(1,1, 1, "title", "description", "loc", "contact", "type", "url", getDate(1999,1,25 ), getDate(1999, 1,25));
        deleteUser(dto);
        return dto;
    }

    private Date getDate(int year, int month, int day) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month-1, day, 0, 0, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        calendar.setTimeZone(TimeZone.getTimeZone("GMT"));
        return new Date(calendar.getTime().getTime());
    }

}
package org.branlewalk.dao;

import org.branlewalk.dto.UserDTO;
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

public class UserDAOTest {

    private Connection connection;

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
        UserDTO dto = createUser();
        Date createDate = getDate(1985,3,25);
        String createdBy = "byme";
        new UserDAO(connection).create(dto, createdBy, createDate);
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery("SELECT userId,userName,password,active,createDate,createdBy,lastUpdateBy FROM user");
        assertThat(resultSet.next(), is(true));
        assertThat(resultSet.getInt("userId"), is(dto.getId()));
        assertThat(resultSet.getString("userName"), is(dto.getName()));
        assertThat(resultSet.getString("password"), is(dto.getPassword()));
        assertThat(resultSet.getBoolean("active"), is(dto.isActive()));
        assertThat(resultSet.getDate("createDate").getTime(), is(createDate.getTime()));
        assertThat(resultSet.getString("createdBy"), is(createdBy));
        assertThat(resultSet.getString("lastUpdateBy"), is(createdBy));
        statement.close();
        deleteUser(dto);
    }

    @Test
    public void read() throws SQLException {
        UserDAO userDAO = new UserDAO(connection);
        UserDTO dto = createUser();
        userDAO.create(dto, "byme", getDate(1985,8,2));
        UserDTO actualDTO = userDAO.read(dto.getId());
        assertThat(actualDTO, notNullValue());
        assertThat(actualDTO.getId(), is(dto.getId()));
        assertThat(actualDTO.getName(), is(dto.getName()));
        assertThat(actualDTO.getPassword(), is(dto.getPassword()));
        assertThat(actualDTO.isActive(), is(dto.isActive()));
        deleteUser(dto);
    }

    @Test
    public void read_userNotFound() throws SQLException {
        UserDAO userDAO = new UserDAO(connection);
        UserDTO actualDTO = userDAO.read(33);
        assertThat(actualDTO, nullValue());
    }

    @Test
    public void update() throws SQLException {
        UserDAO userDAO = new UserDAO(connection);
        UserDTO dto = createUser();
        userDAO.create(dto, "byme", getDate(2016,5,31));
        UserDTO updateDTO = new UserDTO(dto.getId(),"bythem", "newPW", false);
        userDAO.update( "bythem", updateDTO);
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery("SELECT userId,userName,password,active,lastUpdateBy FROM user");
        assertThat(resultSet.next(), is(true));
        assertThat(resultSet.getInt("userId"), is(updateDTO.getId()));
        assertThat(resultSet.getString("userName"), is(updateDTO.getName()));
        assertThat(resultSet.getString("password"), is(updateDTO.getPassword()));
        assertThat(resultSet.getBoolean("active"), is(updateDTO.isActive()));
        assertThat(resultSet.getString("lastUpdateBy"), is("bythem"));
        statement.close();
        deleteUser(dto);
    }

    @Test
    public void delete() throws SQLException {
        UserDAO userDAO = new UserDAO(connection);
        UserDTO dto = createUser();
        UserDTO deletedDTO = userDAO.read(dto.getId());
        userDAO.delete(dto.getId());
        assertThat(deletedDTO, nullValue());
    }


    private void deleteUser(UserDTO dto) throws SQLException {
        PreparedStatement statement = connection.prepareStatement("DELETE FROM user WHERE userId = ?");
        statement.setInt(1, dto.getId());
        statement.execute();
        statement.close();
    }

    private UserDTO createUser() throws SQLException {
        UserDTO dto = new UserDTO(1, "name", "password", false);
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
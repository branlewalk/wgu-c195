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
    public void add() throws SQLException {
        UserDTO dto = new UserDTO(1, "name", "password", false);
        deleteUser(dto);
        Calendar calendar = Calendar.getInstance();
        calendar.set(1985, Calendar.JANUARY, 25, 0, 0, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        calendar.setTimeZone(TimeZone.getTimeZone("GMT"));
        Date createDate = new Date(calendar.getTime().getTime());
        String createdBy = "byme";
        new UserDAO(connection).add(dto, createdBy, createDate);
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
    public void get() throws SQLException {
        UserDTO dto = new UserDTO(1, "name", "password", false);
        deleteUser(dto);
        UserDAO userDAO = new UserDAO(connection);
        userDAO.add(dto, "byme", new Date(0));
        UserDTO actualDTO = userDAO.get(dto.getId());
        assertThat(actualDTO, notNullValue());
        assertThat(actualDTO.getId(), is(dto.getId()));
        assertThat(actualDTO.getName(), is(dto.getName()));
        assertThat(actualDTO.getPassword(), is(dto.getPassword()));
        assertThat(actualDTO.isActive(), is(dto.isActive()));
        deleteUser(dto);
    }

    @Test
    public void get_userNotFound() throws SQLException {
        UserDAO userDAO = new UserDAO(connection);
        UserDTO actualDTO = userDAO.get(33);
        assertThat(actualDTO, nullValue());
    }

    private void deleteUser(UserDTO dto) throws SQLException {
        PreparedStatement statement = connection.prepareStatement("DELETE FROM user WHERE userId = ?");
        statement.setInt(1, dto.getId());
        statement.execute();
        statement.close();
    }


}
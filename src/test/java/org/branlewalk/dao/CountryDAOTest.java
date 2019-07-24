package org.branlewalk.dao;

import org.branlewalk.dto.CountryDTO;
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

public class CountryDAOTest {

    private Connection connection;
    private String selectQuery = "SELECT countryId,country,createDate,createdBy,lastUpdateBy FROM country";

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
        CountryDTO dto = createCountry();
        String createdBy = "byme";
        new CountryDaoImpl(connection).create(dto.getCountry(), createdBy);
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(selectQuery);
        assertThat(resultSet.next(), is(true));
        assertThat(resultSet.getInt("countryId"), is(dto.getCountryId()));
        assertThat(resultSet.getString("country"), is(dto.getCountry()));
        assertThat(resultSet.getString("createdBy"), is(createdBy));
        assertThat(resultSet.getString("lastUpdateBy"), is(createdBy));
        statement.close();
        deleteCountry(dto);
    }

    @Test
    public void read() throws SQLException {
        CountryDAO countryDAO = new CountryDaoImpl(connection);
        CountryDTO dto = createCountry();
        countryDAO.create(dto.getCountry(), "byme");
        CountryDTO actualDTO = countryDAO.read(dto.getCountryId());
        assertThat(actualDTO, notNullValue());
        assertThat(actualDTO.getCountryId(), is(dto.getCountryId()));
        assertThat(actualDTO.getCountry(), is(dto.getCountry()));
        deleteCountry(dto);
    }

    @Test
    public void read_countryNotFound() throws SQLException {
        CountryDAO countryDAO = new CountryDaoImpl(connection);
        CountryDTO actualDTO = countryDAO.read(33);
        assertThat(actualDTO, nullValue());
    }

    @Test
    public void update() throws SQLException {
        CountryDAO countryDAO = new CountryDaoImpl(connection);
        CountryDTO dto = createCountry();
        countryDAO.create(dto.getCountry(), "byme");
        CountryDTO updateDTO = new CountryDTO(dto.getCountryId(),"switzerland");
        countryDAO.update(updateDTO, "bythem");
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(selectQuery);
        assertThat(resultSet.next(), is(true));
        assertThat(resultSet.getInt("countryId"), is(updateDTO.getCountryId()));
        assertThat(resultSet.getString("country"), is(updateDTO.getCountry()));
        assertThat(resultSet.getString("lastUpdateBy"), is("bythem"));
        statement.close();
        deleteCountry(dto);
    }

    @Test
    public void delete() throws SQLException {
        CountryDAO countryDAO = new CountryDaoImpl(connection);
        CountryDTO dto = createCountry();
        countryDAO.create(dto.getCountry(), "byme");
        assertThat(countryDAO.read(dto.getCountryId()), notNullValue());
        countryDAO.delete(dto.getCountryId());
        PreparedStatement statement = connection.prepareStatement("SELECT * FROM country WHERE countryId = ?");
        statement.setInt(1,dto.getCountryId());
        ResultSet resultSet = statement.executeQuery();
        assertThat(resultSet.next(), is(false));
    }


    private void deleteCountry(CountryDTO dto) throws SQLException {
        PreparedStatement statement = connection.prepareStatement("DELETE FROM country WHERE countryId = ?");
        statement.setInt(1, dto.getCountryId());
        statement.execute();
        statement.close();
    }

    private CountryDTO createCountry() throws SQLException {
        CountryDTO dto = new CountryDTO(1, "sweden");
        deleteCountry(dto);
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
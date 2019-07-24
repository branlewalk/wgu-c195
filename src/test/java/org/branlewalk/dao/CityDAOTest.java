package org.branlewalk.dao;

import org.branlewalk.dto.CityDTO;
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

public class CityDAOTest {

    private Connection connection;
    private CountryDAO countryDAO;
    private CountryDTO countryDTO;
    private String selectQuery = "SELECT cityId,city,countryId,createDate,createdBy,lastUpdateBy FROM city";

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
        countryDTO = new CountryDTO(1, "sweden");
        countryDAO = new CountryDaoImpl(connection);
        countryDAO.create(countryDTO.getCountry(),"byme");
    }

    @After
    public void tearDown() throws Exception {
        PreparedStatement statement = connection.prepareStatement("DELETE FROM country WHERE countryId = ?");
        statement.setInt(1, countryDTO.getCountryId());
        statement.execute();
        statement.close();
        connection.close();
    }

    @Test
    public void create() throws SQLException {
        CityDTO dto = createCity();
        String createdBy = "byme";
        new CityDaoImpl(connection).create(dto.getCity(), dto.getCityId(), createdBy);
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(selectQuery);
        assertThat(resultSet.next(), is(true));
        assertThat(resultSet.getInt("cityId"), is(dto.getCityId()));
        assertThat(resultSet.getString("city"), is(dto.getCity()));
        assertThat(resultSet.getInt("countryId"), is(dto.getCountryId()));

        assertThat(resultSet.getString("createdBy"), is(createdBy));
        assertThat(resultSet.getString("lastUpdateBy"), is(createdBy));
        statement.close();
        deleteCity(dto);
    }

    @Test
    public void read() throws SQLException {
        CityDAO cityDAO = new CityDaoImpl(connection);
        CityDTO dto = createCity();
        String createdBy = "byme";
        cityDAO.create(dto.getCity(), dto.getCityId(), createdBy);
        CityDTO actualDTO = cityDAO.read(dto.getCityId());
        assertThat(actualDTO, notNullValue());
        assertThat(actualDTO.getCityId(), is(dto.getCityId()));
        assertThat(actualDTO.getCity(), is(dto.getCity()));
        assertThat(actualDTO.getCountryId(), is(dto.getCountryId()));
        deleteCity(dto);
    }

    @Test
    public void read_cityNotFound() throws SQLException {
        CityDAO cityDAO = new CityDaoImpl(connection);
        CityDTO actualDTO = cityDAO.read(33);
        assertThat(actualDTO, nullValue());
    }

    @Test
    public void update() throws SQLException {
        CityDAO cityDAO = new CityDaoImpl(connection);
        CityDTO dto = createCity();
        String createdBy = "byme";
        cityDAO.create(dto.getCity(), dto.getCityId(), createdBy);
        CityDTO updateDTO = new CityDTO(dto.getCityId(),"new york", countryDTO.getCountryId());
        cityDAO.update( "bythem", updateDTO);
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(selectQuery);
        assertThat(resultSet.next(), is(true));
        assertThat(resultSet.getInt("cityId"), is(updateDTO.getCityId()));
        assertThat(resultSet.getString("city"), is(updateDTO.getCity()));
        assertThat(resultSet.getInt("countryId"), is(updateDTO.getCountryId()));
        assertThat(resultSet.getString("lastUpdateBy"), is("bythem"));
        statement.close();
        deleteCity(dto);
    }

    @Test
    public void delete() throws SQLException {
        CityDAO cityDAO = new CityDaoImpl(connection);
        CityDTO dto = createCity();
        String createdBy = "byme";
        cityDAO.create(dto.getCity(), dto.getCityId(), createdBy);
        assertThat(cityDAO.read(dto.getCityId()), notNullValue());
        cityDAO.delete(dto.getCityId());
        PreparedStatement statement = connection.prepareStatement("SELECT * FROM city WHERE cityId = ?");
        statement.setInt(1,dto.getCityId());
        ResultSet resultSet = statement.executeQuery();
        assertThat(resultSet.next(), is(false));
    }


    private void deleteCity(CityDTO dto) throws SQLException {
        PreparedStatement statement = connection.prepareStatement("DELETE FROM city WHERE cityId = ?");
        statement.setInt(1, dto.getCityId());
        statement.execute();
        statement.close();
    }

    private CityDTO createCity() throws SQLException {
        CityDTO dto = new CityDTO(1, "seattle", countryDTO.getCountryId());
        deleteCity(dto);
        return dto;
    }

}
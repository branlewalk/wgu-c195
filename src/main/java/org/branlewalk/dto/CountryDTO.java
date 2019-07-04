package org.branlewalk.dto;

import java.sql.Date;

public class CountryDTO {

    private int countryId;
    private String country;


    public CountryDTO(int countryId, String country) {
        this.countryId = countryId;
        this.country = country;

    }

    public int getCountryId() {
        return countryId;
    }

    public void setCountryId(int countryId) {
        this.countryId = countryId;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

}

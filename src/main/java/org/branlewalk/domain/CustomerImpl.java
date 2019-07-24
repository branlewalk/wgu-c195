package org.branlewalk.domain;

import org.branlewalk.dto.AddressDTO;
import org.branlewalk.dto.CityDTO;
import org.branlewalk.dto.CountryDTO;
import org.branlewalk.dto.CustomerDTO;

public class CustomerImpl implements Customer {

    private final CustomerDTO customerDTO;
    private final AddressDTO addressDTO;
    private final CityDTO cityDTO;
    private final CountryDTO countryDTO;

    public CustomerImpl(CustomerDTO customerDTO, AddressDTO addressDTO, CityDTO cityDTO, CountryDTO countryDTO) {
        this.customerDTO = customerDTO;
        this.addressDTO = addressDTO;
        this.cityDTO = cityDTO;
        this.countryDTO = countryDTO;
    }

    @Override
    public String getName() {
        return customerDTO.getCustomerName();
    }

    @Override
    public String getPhone() {
        return addressDTO.getPhone();
    }

    @Override
    public String getPostalCode() {
        return addressDTO.getPostalCode();
    }

    @Override
    public String getCity() {
        return cityDTO.getCity();
    }

    @Override
    public String getCountry() {
        return countryDTO.getCountry();
    }
}

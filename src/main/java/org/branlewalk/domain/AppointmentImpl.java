package org.branlewalk.domain;

import org.branlewalk.dto.*;


import java.util.Calendar;
import java.util.Date;

public class AppointmentImpl implements Appointment {

    private final AppointmentDTO appointmentDTO;
    private final UserDTO userDTO;
    private final CustomerDTO customerDTO;

    public AppointmentImpl(AppointmentDTO appointmentDTO, UserDTO userDTO, CustomerDTO customerDTO) {
        this.appointmentDTO = appointmentDTO;
        this.userDTO = userDTO;
        this.customerDTO = customerDTO;
    }

    @Override
    public String toString() {
        return appointmentDTO.getTitle();
    }

    @Override
    public int getMonthValue() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(appointmentDTO.getStart());
        return calendar.get(Calendar.MONTH);
    }

    @Override
    public String getType() {
        return appointmentDTO.getType();
    }

    @Override
    public String getDescription() {
        return appointmentDTO.getDescription();
    }

    @Override
    public String getCustomerName() {
        return customerDTO.getCustomerName();
    }

    @Override
    public String getUserName() {
        return userDTO.getName();
    }

    @Override
    public String getTitle() {
        return appointmentDTO.getTitle();
    }

    @Override
    public String getLocation() {
        return appointmentDTO.getLocation();
    }

    @Override
    public String getUrl() {
        return appointmentDTO.getUrl();
    }

    @Override
    public Date getStart() {
        return appointmentDTO.getStart();
    }

    @Override
    public Date getEnd() {
        return appointmentDTO.getEnd();
    }
}

package org.branlewalk.domain;

import org.branlewalk.dto.*;


import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class AppointmentImpl implements Appointment {

    private final AppointmentDTO appointmentDTO;
    private final UserDTO userDTO;
    private final CustomerDTO customerDTO;
    private final String DATE_FORMAT;
    private DateFormat format;

    public AppointmentImpl(AppointmentDTO appointmentDTO, UserDTO userDTO, CustomerDTO customerDTO) {
        this.appointmentDTO = appointmentDTO;
        this.userDTO = userDTO;
        this.customerDTO = customerDTO;
        DATE_FORMAT = "MMM d, yyyy HH:mm a";
        format = new SimpleDateFormat(DATE_FORMAT);
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
    public int getCustomerId() {
        return customerDTO.getCustomerId();
    }

    @Override
    public int getUserId() {
        return userDTO.getId();
    }

    @Override
    public String getContact() {
        return appointmentDTO.getContact();
    }

    @Override
    public int getId() {
        return appointmentDTO.getAppointmentId();
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
    @Override
    public String getStartDateString() {
        return format.format(appointmentDTO.getStart());
    }
    @Override
    public String getEndDateString() {
        return format.format(appointmentDTO.getEnd());
    }

}

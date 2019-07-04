package org.branlewalk.dto;

import java.sql.Date;

public class ReminderDTO {

    private int reminderId;
    private Date reminderDate;
    private int snoozeIncrement;
    private int snoozeIncrementTypeId;
    private int appointmentId;

    public ReminderDTO(int reminderId, Date reminderDate, int snoozeIncrement, int snoozeIncrementTypeId, int appointmentId) {
        this.reminderId = reminderId;
        this.reminderDate = reminderDate;
        this.snoozeIncrement = snoozeIncrement;
        this.snoozeIncrementTypeId = snoozeIncrementTypeId;
        this.appointmentId = appointmentId;
    }

    public int getReminderId() {
        return reminderId;
    }

    public void setReminderId(int reminderId) {
        this.reminderId = reminderId;
    }

    public Date getReminderDate() {
        return reminderDate;
    }

    public void setReminderDate(Date reminderDate) {
        this.reminderDate = reminderDate;
    }

    public int getSnoozeIncrement() {
        return snoozeIncrement;
    }

    public void setSnoozeIncrement(int snoozeIncrement) {
        this.snoozeIncrement = snoozeIncrement;
    }

    public int getSnoozeIncrementTypeId() {
        return snoozeIncrementTypeId;
    }

    public void setSnoozeIncrementTypeId(int snoozeIncrementTypeId) {
        this.snoozeIncrementTypeId = snoozeIncrementTypeId;
    }

    public int getAppointmentId() {
        return appointmentId;
    }

    public void setAppointmentId(int appointmentId) {
        this.appointmentId = appointmentId;
    }
}

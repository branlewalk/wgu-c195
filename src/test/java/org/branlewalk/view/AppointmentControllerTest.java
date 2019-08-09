package org.branlewalk.view;

import javafx.collections.FXCollections;
import org.branlewalk.dao.AppointmentDAO;
import org.branlewalk.domain.Appointment;
import org.branlewalk.domain.AppointmentImpl;
import org.branlewalk.domain.InvalidAppointmentException;
import org.easymock.EasyMockSupport;
import org.junit.Test;

import java.sql.SQLException;
import java.util.Date;

import static org.easymock.EasyMock.expect;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;

public class AppointmentControllerTest extends EasyMockSupport {

    @Test
    public void validateTimeConflicts_noneBefore() throws SQLException, InvalidAppointmentException {
        AppointmentDAO dao = mock(AppointmentDAO.class);
        Appointment appointment = mock(Appointment.class);
        expect(dao.findAllForDate(new Date(9))).andReturn(FXCollections.observableArrayList(appointment));
        expect(appointment.getStart()).andReturn(new Date(10));
        expect(appointment.getEnd()).andReturn(new Date(11));
        expect(appointment.getId()).andReturn(1);
        AppointmentController appointmentController = new AppointmentController(null, dao);

        replayAll();
        assertThat(appointmentController.validateTimeConflicts(new Date(9), new Date(10), 0), is(true));
        verifyAll();

    }

    @Test
    public void validateTimeConflicts_noneAfter() throws SQLException, InvalidAppointmentException {
        AppointmentDAO dao = mock(AppointmentDAO.class);
        Appointment appointment = mock(Appointment.class);
        Date start = new Date(9);
        expect(dao.findAllForDate(start)).andReturn(FXCollections.observableArrayList(appointment));
        expect(appointment.getStart()).andReturn(new Date(7));
        expect(appointment.getEnd()).andReturn(new Date(9));
        expect(appointment.getId()).andReturn(1);
        AppointmentController appointmentController = new AppointmentController(null, dao);

        replayAll();
        assertThat(appointmentController.validateTimeConflicts(start, new Date(10), 0), is(true));
        verifyAll();

    }

    @Test
    public void validateTimeConflicts_overlapStart() throws SQLException, InvalidAppointmentException {
        AppointmentDAO dao = mock(AppointmentDAO.class);
        Appointment appointment = mock(Appointment.class);
        expect(dao.findAllForDate(new Date(9))).andReturn(FXCollections.observableArrayList(appointment));
        expect(appointment.getStart()).andReturn(new Date(10));
        expect(appointment.getEnd()).andReturn(new Date(12));
        expect(appointment.getId()).andReturn(1);
        AppointmentController appointmentController = new AppointmentController(null, dao);

        replayAll();
        try {
            appointmentController.validateTimeConflicts(new Date(9), new Date(11), 0);
            fail("Should throw validation exception");
        } catch (InvalidAppointmentException e) {
            //
        }
        verifyAll();
    }

    @Test
    public void validateTimeConflicts_inside() throws SQLException, InvalidAppointmentException {
        AppointmentDAO dao = mock(AppointmentDAO.class);
        Appointment appointment = mock(Appointment.class);
        Date start = new Date(11);
        expect(dao.findAllForDate(start)).andReturn(FXCollections.observableArrayList(appointment));
        expect(appointment.getStart()).andReturn(new Date(10));
        expect(appointment.getEnd()).andReturn(new Date(13));
        expect(appointment.getId()).andReturn(1);
        AppointmentController appointmentController = new AppointmentController(null, dao);

        replayAll();
        try {
            appointmentController.validateTimeConflicts(start, new Date(12), 0);
            fail("Should throw validation exception");
        } catch (InvalidAppointmentException e) {
            //
        }
        verifyAll();
    }

    @Test
    public void validateTimeConflicts_partiallyInsideBefore() throws SQLException, InvalidAppointmentException {
        AppointmentDAO dao = mock(AppointmentDAO.class);
        Appointment appointment = mock(Appointment.class);
        Date start = new Date(11);
        expect(dao.findAllForDate(start)).andReturn(FXCollections.observableArrayList(appointment));
        expect(appointment.getStart()).andReturn(new Date(11));
        expect(appointment.getEnd()).andReturn(new Date(13));
        expect(appointment.getId()).andReturn(1);
        AppointmentController appointmentController = new AppointmentController(null, dao);

        replayAll();
        try {
            appointmentController.validateTimeConflicts(start, new Date(12), 0);
            fail("Should throw validation exception");
        } catch (InvalidAppointmentException e) {
            //
        }
        verifyAll();
    }

    @Test
    public void validateTimeConflicts_partiallyInsideEnd() throws SQLException, InvalidAppointmentException {
        AppointmentDAO dao = mock(AppointmentDAO.class);
        Appointment appointment = mock(Appointment.class);
        Date start = new Date(12);
        expect(dao.findAllForDate(start)).andReturn(FXCollections.observableArrayList(appointment));
        expect(appointment.getStart()).andReturn(new Date(11));
        expect(appointment.getEnd()).andReturn(new Date(13));
        expect(appointment.getId()).andReturn(1);
        AppointmentController appointmentController = new AppointmentController(null, dao);

        replayAll();
        try {
            appointmentController.validateTimeConflicts(start, new Date(13), 0);
            fail("Should throw validation exception");
        } catch (InvalidAppointmentException e) {
            //
        }
        verifyAll();
    }

    @Test
    public void validateTimeConflicts_overlapEnd() throws SQLException, InvalidAppointmentException {
        AppointmentDAO dao = mock(AppointmentDAO.class);
        Appointment appointment = mock(Appointment.class);
        Date start = new Date(12);
        expect(dao.findAllForDate(start)).andReturn(FXCollections.observableArrayList(appointment));
        expect(appointment.getStart()).andReturn(new Date(10));
        expect(appointment.getEnd()).andReturn(new Date(13));
        expect(appointment.getId()).andReturn(1);
        AppointmentController appointmentController = new AppointmentController(null, dao);

        replayAll();
        try {
            appointmentController.validateTimeConflicts(start, new Date(14), 0);
            fail("Should throw validation exception");
        } catch (InvalidAppointmentException e) {
            //
        }
        verifyAll();
    }

    @Test
    public void validateTimeConflicts_exact() throws SQLException, InvalidAppointmentException {
        AppointmentDAO dao = mock(AppointmentDAO.class);
        Appointment appointment = mock(Appointment.class);
        Date start = new Date(12);
        expect(dao.findAllForDate(start)).andReturn(FXCollections.observableArrayList(appointment));
        expect(appointment.getStart()).andReturn(new Date(12));
        expect(appointment.getEnd()).andReturn(new Date(14));
        expect(appointment.getId()).andReturn(1);
        AppointmentController appointmentController = new AppointmentController(null, dao);

        replayAll();
        try {
            appointmentController.validateTimeConflicts(start, new Date(14), 0);
            fail("Should throw validation exception");
        } catch (InvalidAppointmentException e) {
            //
        }
        verifyAll();
    }

}
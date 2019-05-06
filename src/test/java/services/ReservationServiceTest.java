package services;

import data.HashMapRepository;
import data.Repository;
import errors.EntryNotFoundException;
import errors.ValidationException;
import models.Reservation;
import models.Restaurant;
import models.Table;
import models.User;
import org.joda.time.LocalDate;
import org.joda.time.LocalTime;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

public class ReservationServiceTest {

    private Repository repository;

    private User user;
    private Restaurant restaurant;
    private Table table;
    private Reservation reservation;

    private ReservationService reservationService;

    @BeforeEach
    public void setUp()
    {
        repository = new HashMapRepository();

        user = new User();
        user.setEmail("email@poczta.pl");
        user.setPassword("haslo1");
        user.setActive(true);
        user.setUserType(User.Type.STANDARD);

        repository.add(user);

        restaurant = new Restaurant();
        restaurant.setName("Restauracja");
        restaurant.setAddress("ul. Słupska 51, 80310 Gdańsk");
        restaurant.setOpenHour(new LocalTime(9,30,0));
        restaurant.setCloseHour(new LocalTime(22,0,0));

        repository.add(restaurant);

        table = new Table();
        table.setRestaurantId(restaurant.getId());
        table.setSeats(4);

        repository.add(table);

        reservation = new Reservation();
        reservation.setDate(new LocalDate(2016,7,11));
        reservation.setTime(new LocalTime(11,30,0));
        reservation.setTableId(table.getId());
        reservation.setUserId(user.getId());

        reservationService = new ReservationService(repository);
    }

    @Test
    public void addValidReservation()
    {
        assertThatCode(()->
            reservationService.add(reservation)
        ).doesNotThrowAnyException();
    }

    @Test
    public void addInvalidReservation()
    {
        reservation.setTime(new LocalTime(11,11,4));

        assertThatExceptionOfType(ValidationException.class).isThrownBy(
                () -> reservationService.add(reservation)
        ).withMessageContaining("Reservation");
    }

    @Test
    public void addInvalidReservationBeforeRestaurantsOpen()
    {
        reservation.setTime(new LocalTime(1,30,0));

        assertThatExceptionOfType(ValidationException.class).isThrownBy(
                () -> reservationService.add(reservation)
        ).withMessageContaining("Reservation");
    }

    @Test
    public void addReservationWithInvalidUser()
    {
        reservation.setUserId(5L);

        assertThatExceptionOfType(EntryNotFoundException.class).isThrownBy(
                () -> reservationService.add(reservation)
        ).withMessageContaining("User");
    }

    @Test
    public void addReservationWithInvalidTable()
    {
        reservation.setTableId(5L);

        assertThatExceptionOfType(EntryNotFoundException.class).isThrownBy(
                () -> reservationService.add(reservation)
        ).withMessageContaining("Table");
    }

    @Test
    public void deleteExistingReservation()
    {
        repository.add(reservation);

        assertThatCode(
                () -> reservationService.delete(reservation)
        ).doesNotThrowAnyException();
    }

    @Test
    public void deleteNonExistingReservation()
    {
        reservation.setId(3L);

        assertThatExceptionOfType(EntryNotFoundException.class).isThrownBy(
                () -> reservationService.delete(reservation)
        ).withMessageContaining("Reservation");
    }

    @Test
    public void updateExistingValidReservation()
    {
        repository.add(reservation);

        assertThatCode(
                () -> reservationService.update(reservation)
        ).doesNotThrowAnyException();
    }

    @Test
    public void updateNonExistingReservation()
    {
        reservation.setId(3L);

        assertThatExceptionOfType(EntryNotFoundException.class).isThrownBy(
                () -> reservationService.update(reservation)
        ).withMessageContaining("Reservation");
    }

    @Test
    public void updateReservationWithInvalidData()
    {
        repository.add(reservation);
        reservation.setTime(new LocalTime(11,11,4));

        assertThatExceptionOfType(ValidationException.class).isThrownBy(
                () -> reservationService.update(reservation)
        ).withMessageContaining("Reservation");
    }

    @Test
    public void updateReservationWithInvalidReservationTime()
    {
        repository.add(reservation);
        reservation.setTime(new LocalTime(1,0,0));

        assertThatExceptionOfType(ValidationException.class).isThrownBy(
                () -> reservationService.update(reservation)
        ).withMessageContaining("Reservation");
    }

    @Test
    public void updateReservationWithInvalidUser()
    {
        repository.add(reservation);
        reservation.setUserId(5L);

        assertThatExceptionOfType(EntryNotFoundException.class).isThrownBy(
                () -> reservationService.update(reservation)
        ).withMessageContaining("User");
    }

    @Test
    public void updateReservationWithInvalidTable()
    {
        repository.add(reservation);
        reservation.setTableId(5L);

        assertThatExceptionOfType(EntryNotFoundException.class).isThrownBy(
                () -> reservationService.update(reservation)
        ).withMessageContaining("Table");
    }

    @Test
    public void getReservation()
    {
        repository.add(reservation);

        Reservation r = reservationService.get(1L);

        assertThat(r).isEqualTo(reservation);
    }

    @Test
    public void getReservationsTable() throws Exception
    {
        repository.add(reservation);

        Table t = reservationService.getTable(reservation);

        assertThat(t).isEqualTo(table);
    }

    @Test
    public void getReservationsUser() throws Exception
    {
        repository.add(reservation);

        User u = reservationService.getUser(reservation);

        assertThat(u).isEqualTo(user);
    }

    @Test
    public void getTableOfNonExistingReservation()
    {
        reservation.setId(5L);

        assertThatExceptionOfType(EntryNotFoundException.class).isThrownBy(
                () -> reservationService.getTable(reservation)
        ).withMessageContaining("Reservation");
    }

    @Test
    public void getUserOfNonExistingReservation()
    {
        reservation.setId(5L);

        assertThatExceptionOfType(EntryNotFoundException.class).isThrownBy(
                () -> reservationService.getUser(reservation)
        ).withMessageContaining("Reservation");
    }
}

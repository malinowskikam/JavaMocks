package models;

import org.joda.time.LocalDate;
import org.joda.time.LocalTime;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

public class ModelValidationTests
{
    private static User user;
    private static Restaurant restaurant;
    private static Table table;
    private static Reservation reservation;


    @ParameterizedTest
    @CsvFileSource(resources = "../valid_users.csv")
    public void validUsers(String email,String password)
    {
         user = new User(0L, email,password,true,User.Type.STANDARD);
         assertAll(
                 () ->
                 {
                     assertThat(user.isValid()).isTrue();
                     assertThat(user.getValidationError()).isNull();
                 }
         );

    }

    @ParameterizedTest
    @CsvFileSource(resources = "../invalid_users.csv")
    public void invalidUsers(String email,String password)
    {
        user = new User(0L, email,password,true,User.Type.STANDARD);

        assertAll(
                () ->
                {
                    assertThat(user.isValid()).isFalse();
                    assertThat(user.getValidationError()).isNotNull();
                }
        );
    }

    @ParameterizedTest
    @CsvFileSource(resources = "../valid_restaurants.csv")
    public void validUestaurant(String name,String address, String open, String close)
    {
        restaurant = new Restaurant(0L, name,address,new LocalTime(open),new LocalTime(close));

        assertAll(
                () ->
                {
                    assertThat(restaurant.isValid()).isTrue();
                    assertThat(restaurant.getValidationError()).isNull();
                }
        );
    }

    @ParameterizedTest
    @CsvFileSource(resources = "../invalid_restaurants.csv")
    public void invalidRestaurants(String name,String address, String open, String close)
    {
        restaurant = new Restaurant(0L, name, address, new LocalTime(open), new LocalTime(close));

        assertAll(
                () ->
                {
                    assertThat(restaurant.isValid()).isFalse();
                    assertThat(restaurant.getValidationError()).isNotNull();
                }
        );
    }

    @ParameterizedTest
    @CsvFileSource(resources = "../valid_tables.csv")
    public void validTables(int seats, Long restaurantId)
    {
        table = new Table(0L,seats,restaurantId);

        assertAll(
                () ->
                {
                    assertThat(table.isValid()).isTrue();
                    assertThat(table.getValidationError()).isNull();
                }
        );
    }

    @ParameterizedTest
    @CsvFileSource(resources = "../invalid_tables.csv")
    public void invalidTables(int seats, Long restaurantId)
    {
        table = new Table(0L,seats,restaurantId);

        assertAll(
                () ->
                {
                    assertThat(table.isValid()).isFalse();
                    assertThat(table.getValidationError()).isNotNull();
                }
        );
    }

    @ParameterizedTest
    @CsvFileSource(resources = "../valid_reservations.csv")
    public void validReservations(Long id, Long userId, Long tableId, String time, String date)
    {
        reservation = new Reservation(id,userId,tableId,new LocalTime(time),new LocalDate(date));

        assertAll(
                () ->
                {
                    assertThat(reservation.isValid()).isTrue();
                    assertThat(reservation.getValidationError()).isNull();
                }
        );
    }

    @ParameterizedTest
    @CsvFileSource(resources = "../invalid_reservations.csv")
    public void invalidReservations(Long id, Long userId, Long tableId, String time, String date)
    {
        reservation = new Reservation(id,userId,tableId,new LocalTime(time),new LocalDate(date));

        assertAll(
                () ->
                {
                    assertThat(reservation.isValid()).isFalse();
                    assertThat(reservation.getValidationError()).isNotNull();
                }
        );
    }

    @Test
    public void nullTimeReservation()
    {
        reservation = new Reservation(null,1L,1L,null,new LocalDate(2018,1,1));

        assertAll(
                () ->
                {
                    assertThat(reservation.isValid()).isFalse();
                    assertThat(reservation.getValidationError()).isNotNull();
                }
        );
    }

    @Test
    public void nullDateReservation()
    {
        reservation = new Reservation(null,1L,1L,new LocalTime(10,0,0),null);

        assertAll(
                () ->
                {
                    assertThat(reservation.isValid()).isFalse();
                    assertThat(reservation.getValidationError()).isNotNull();
                }
        );
    }
}

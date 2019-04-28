package models;

import models.*;
import org.joda.time.LocalDate;
import org.joda.time.LocalTime;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

public class ModelValidationTests // testy hamcrest
{
    static User user;
    static Restaurant restaurant;
    static Table table;
    static Reservation reservation;


    @ParameterizedTest
    @CsvFileSource(resources = "valid_users.csv")
    void valid_users(String email,String password)
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
    @CsvFileSource(resources = "invalid_users.csv")
    void invalid_users(String email,String password)
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
    @CsvFileSource(resources = "valid_restaurants.csv")
    void valid_restaurant(String name,String address, String open, String close)
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
    @CsvFileSource(resources = "invalid_restaurants.csv")
    void invalid_restaurants(String name,String address, String open, String close)
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
    @CsvFileSource(resources = "valid_tables.csv")
    void valid_tables(int seats, Long restaurantId)
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
    @CsvFileSource(resources = "invalid_tables.csv")
    void invalid_tables(int seats, Long restaurantId)
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
    @CsvFileSource(resources = "valid_reservations.csv")
    void valid_reservations(Long id, Long userId, Long tableId, String time, String date)
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
    @CsvFileSource(resources = "invalid_reservations.csv")
    void invalid_reservations(Long id, Long userId, Long tableId, String time, String date)
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
    void null_time_reservation()
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
    void null_date_reservation()
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

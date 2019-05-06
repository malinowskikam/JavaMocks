package services;

import data.Repository;
import errors.EntryNotFoundException;
import errors.ValidationException;
import models.Reservation;
import models.Restaurant;
import models.Table;
import models.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.easymock.EasyMock.*;

//Testy EasyMock
public class RestaurantServiceTest {

    //Mocks
    private Repository repository;
    private Restaurant restaurant;

    //Tested
    private RestaurantService restaurantService;

    @BeforeEach
    public void setUp()
    {
        repository = createMock(Repository.class);
        restaurant = mock(Restaurant.class);

        restaurantService = new RestaurantService(repository);
    }

    @Test
    public void addValidRestaurant()
    {
        expect(restaurant.isValid()).andReturn(true);
        expect(restaurant.getId()).andReturn(1L);
        repository.add(restaurant);
        expectLastCall().andAnswer(()->null);

        replay(restaurant);
        replay(repository);

        assertThatCode(
                () -> restaurantService.add(restaurant)
        ).doesNotThrowAnyException();

        verify(restaurant);
        verify(repository);
    }

    @Test
    public void addInvalidRestaurant()
    {
        expect(restaurant.isValid()).andReturn(false);
        expect(restaurant.getValidationError()).andReturn("validation error message");

        replay(restaurant);
        replay(repository);

        assertThatExceptionOfType(ValidationException.class).isThrownBy(
                () -> restaurantService.add(restaurant)
        ).withMessageContaining("Restaurant");

        verify(restaurant);
        verify(repository);
    }

    @Test
    public void deleteExistingRestaurant()
    {
        expect(restaurant.getId()).andReturn(1L);
        expect(repository.get(1L,Restaurant.class)).andReturn(restaurant);
        repository.delete(restaurant);
        expectLastCall().andAnswer(()->null);

        replay(restaurant);
        replay(repository);

        assertThatCode(
                () -> restaurantService.delete(restaurant)
        ).doesNotThrowAnyException();

        verify(restaurant);
        verify(repository);
    }

    @Test
    public void deleteNonExistingRestaurant()
    {
        expect(restaurant.getId()).andReturn(1L).times(2);
        expect(repository.get(1L,Restaurant.class)).andReturn(null);

        replay(restaurant);
        replay(repository);

        assertThatExceptionOfType(EntryNotFoundException.class).isThrownBy(
                () -> restaurantService.delete(restaurant)
        ).withMessageContaining("Restaurant");

        verify(restaurant);
        verify(repository);
    }

    @Test
    public void updateExistingValidRestaurant()
    {
        expect(restaurant.getId()).andReturn(1L);
        expect(restaurant.isValid()).andReturn(true);
        expect(repository.get(1L,Restaurant.class)).andReturn(restaurant);
        repository.update(restaurant);
        expectLastCall().andAnswer(()->null);

        replay(restaurant);
        replay(repository);

        assertThatCode(
                () -> restaurantService.update(restaurant)
        ).doesNotThrowAnyException();

        verify(repository);
        verify(restaurant);
    }

    @Test
    public void updateNonExistingRestaurant()
    {
        expect(restaurant.getId()).andReturn(1L).times(2);
        expect(repository.get(1L,Restaurant.class)).andReturn(null);

        replay(restaurant);
        replay(repository);

        assertThatExceptionOfType(EntryNotFoundException.class).isThrownBy(
                () -> restaurantService.update(restaurant)
        ).withMessageContaining("Restaurant");

        verify(repository);
        verify(restaurant);
    }

    @Test
    public void updateRestaurantWithInvalidData()
    {
        expect(restaurant.getId()).andReturn(1L);
        expect(restaurant.isValid()).andReturn(false);
        expect(restaurant.getValidationError()).andReturn("validation error message");
        expect(repository.get(1L,Restaurant.class)).andReturn(restaurant);

        replay(restaurant);
        replay(repository);

        assertThatExceptionOfType(ValidationException.class).isThrownBy(
                () -> restaurantService.update(restaurant)
        ).withMessageContaining("Restaurant");

        verify(repository);
        verify(restaurant);
    }

    @Test
    public void getRestaurant()
    {
        expect(repository.get(1L, Restaurant.class)).andReturn(restaurant);

        replay(repository);
        replay(restaurant);

        Restaurant u = restaurantService.get(1L);

        assertThat(u).isEqualTo(restaurant);

        verify(repository);
        verify(restaurant);
    }

    @Test
    public void getTables() throws Exception
    {
        Table table1 = createMock(Table.class);
        Table table2 = createMock(Table.class);
        Restaurant restaurant2 = createMock(Restaurant.class);

        expect(restaurant.getId()).andReturn(1L);
        expect(table1.getId()).andReturn(1L);
        expect(table2.getId()).andReturn(2L);
        expect(table1.getRestaurantId()).andReturn(1L);
        expect(table2.getRestaurantId()).andReturn(2L);

        expect(repository.get(1L,Restaurant.class)).andReturn(restaurant).times(2);
        expect(repository.get(2L,Restaurant.class)).andReturn(restaurant2);
        expect(repository.get(1L,Table.class)).andReturn(table1);
        expect(repository.get(2L,Table.class)).andReturn(table2);

        expect(repository.getAll(Table.class)).andReturn(Arrays.asList(table1,table2));

        replay(repository);
        replay(restaurant);
        replay(restaurant2);
        replay(table1);
        replay(table2);

        List<Table> tables = restaurantService.getTables(restaurant);

        assertThat(tables.size()).isEqualTo(1);

        verify(repository);
        verify(restaurant);
        verify(restaurant2);
        verify(table1);
        verify(table2);
    }

    @Test
    public void getReservations() throws Exception
    {
        Table table1 = createMock(Table.class);
        Table table2 = createMock(Table.class);
        Table table3 = createMock(Table.class);
        Reservation reservation1 = createMock(Reservation.class);
        Reservation reservation2 = createMock(Reservation.class);
        Reservation reservation3 = createMock(Reservation.class);
        Restaurant restaurant2 = createMock(Restaurant.class);

        expect(restaurant.getId()).andReturn(1L).times(2);
        expect(table1.getId()).andReturn(1L);
        expect(table2.getId()).andReturn(2L);
        expect(table3.getId()).andReturn(3L);
        expect(table1.getRestaurantId()).andReturn(1L);
        expect(table2.getRestaurantId()).andReturn(1L);
        expect(table3.getRestaurantId()).andReturn(2L);
        expect(reservation1.getId()).andReturn(1L);
        expect(reservation2.getId()).andReturn(2L);
        expect(reservation3.getId()).andReturn(3L);
        expect(reservation1.getTableId()).andReturn(1L);
        expect(reservation2.getTableId()).andReturn(2L);
        expect(reservation3.getTableId()).andReturn(3L);

        expect(repository.get(1L,Restaurant.class)).andReturn(restaurant).times(4);
        expect(repository.get(2L,Restaurant.class)).andReturn(restaurant2);
        expect(repository.get(1L,Table.class)).andReturn(table1).times(2);
        expect(repository.get(2L,Table.class)).andReturn(table2).times(2);
        expect(repository.get(3L,Table.class)).andReturn(table3).times(2);
        expect(repository.get(1L,Reservation.class)).andReturn(reservation1);
        expect(repository.get(2L,Reservation.class)).andReturn(reservation2);
        expect(repository.get(3L,Reservation.class)).andReturn(reservation3);
        expect(repository.getAll(Table.class)).andReturn(Arrays.asList(table1,table2,table3));
        expect(repository.getAll(Reservation.class)).andReturn(Arrays.asList(reservation1,reservation2,reservation3));

        replay(repository);
        replay(restaurant);
        replay(restaurant2);
        replay(reservation1);
        replay(reservation2);
        replay(reservation3);
        replay(table1);
        replay(table2);
        replay(table3);

        List<Reservation> reservations = restaurantService.getReservations(restaurant);

        assertThat(reservations.size()).isEqualTo(2);

        verify(repository);
        verify(restaurant);
        verify(restaurant2);
        verify(table1);
        verify(table2);
        verify(table3);
        verify(reservation1);
        verify(reservation2);
        verify(reservation3);
    }

    @Test
    public void getUsers() throws Exception
    {
        Table table1 = createMock(Table.class);
        Table table2 = createMock(Table.class);
        Reservation reservation1 = createMock(Reservation.class);
        Reservation reservation2 = createMock(Reservation.class);
        User user1 = createMock(User.class);
        User user2 = createMock(User.class);

        expect(restaurant.getId()).andReturn(1L).times(3);
        expect(table1.getId()).andReturn(1L);
        expect(table2.getId()).andReturn(2L);
        expect(table1.getRestaurantId()).andReturn(1L);
        expect(table2.getRestaurantId()).andReturn(1L);
        expect(reservation1.getId()).andReturn(1L).times(2);
        expect(reservation2.getId()).andReturn(2L).times(2);
        expect(reservation1.getTableId()).andReturn(1L);
        expect(reservation2.getTableId()).andReturn(2L);
        expect(reservation1.getUserId()).andReturn(1L);
        expect(reservation2.getUserId()).andReturn(2L);


        expect(repository.get(1L,Restaurant.class)).andReturn(restaurant).times(5);
        expect(repository.get(1L,Table.class)).andReturn(table1).times(2);
        expect(repository.get(2L,Table.class)).andReturn(table2).times(2);
        expect(repository.get(1L,Reservation.class)).andReturn(reservation1).times(2);
        expect(repository.get(2L,Reservation.class)).andReturn(reservation2).times(2);
        expect(repository.get(1L,User.class)).andReturn(user1);
        expect(repository.get(2L,User.class)).andReturn(user1);
        expect(repository.getAll(Table.class)).andReturn(Arrays.asList(table1,table2));
        expect(repository.getAll(Reservation.class)).andReturn(Arrays.asList(reservation1,reservation2));

        replay(repository);
        replay(restaurant);
        replay(reservation1);
        replay(reservation2);
        replay(table1);
        replay(table2);
        replay(user1);
        replay(user2);

        List<User> users = restaurantService.getUsers(restaurant);

        assertThat(users.size()).isEqualTo(2);

        verify(repository);
        verify(restaurant);
        verify(table1);
        verify(table2);
        verify(reservation1);
        verify(reservation2);
        verify(user1);
        verify(user2);
    }

    @Test
    public void getTablesOfNonExistingRestaurant()
    {
        expect(restaurant.getId()).andReturn(1L).times(2);
        expect(repository.get(1L,Restaurant.class)).andReturn(null);

        replay(restaurant);
        replay(repository);

        assertThatExceptionOfType(EntryNotFoundException.class).isThrownBy(
                () -> restaurantService.getTables(restaurant)
        ).withMessageContaining("Restaurant");

        verify(repository);
        verify(restaurant);
    }

    @Test
    public void getReservationsOfNonExistingRestaurant()
    {
        expect(restaurant.getId()).andReturn(1L).times(2);
        expect(repository.get(1L,Restaurant.class)).andReturn(null);

        replay(restaurant);
        replay(repository);

        assertThatExceptionOfType(EntryNotFoundException.class).isThrownBy(
                () -> restaurantService.getReservations(restaurant)
        ).withMessageContaining("Restaurant");

        verify(repository);
        verify(restaurant);
    }

    @Test
    public void getUsersOfNonExistingRestaurant()
    {
        expect(restaurant.getId()).andReturn(1L).times(2);
        expect(repository.get(1L,Restaurant.class)).andReturn(null);

        replay(restaurant);
        replay(repository);

        assertThatExceptionOfType(EntryNotFoundException.class).isThrownBy(
                () -> restaurantService.getUsers(restaurant)
        ).withMessageContaining("Restaurant");

        verify(repository);
        verify(restaurant);
    }

}

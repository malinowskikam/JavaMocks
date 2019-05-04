package services;

import data.Repository;
import errors.EntryNotFoundException;
import errors.ValidationException;
import models.Restaurant;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

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

}

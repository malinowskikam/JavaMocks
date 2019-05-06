package data;

import models.Reservation;
import models.Restaurant;
import models.Table;
import models.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.junit.jupiter.api.Assertions.assertAll;

public class HashMapRepositoryTests {

    private Repository repository;

    @BeforeEach
    public void setUp()
    {
        repository = new HashMapRepository();
    }

    @Test
    public void usersTest()
    {
        User u = new User();

        assertThatCode(() -> {
            repository.add(u);
            repository.get(u.getId(),User.class);
            repository.update(u);
            repository.getAll(User.class);
            repository.delete(u);
        }).doesNotThrowAnyException();
    }

    @Test
    public void reservationsTest()
    {
        Reservation r = new Reservation();

        assertThatCode(() -> {
            repository.add(r);
            repository.get(r.getId(),Reservation.class);
            repository.update(r);
            repository.getAll(Reservation.class);
            repository.delete(r);
        }).doesNotThrowAnyException();
    }

    @Test
    public void tablesTest()
    {
        Table t = new Table();

        assertThatCode(() -> {
            repository.add(t);
            repository.get(t.getId(),Table.class);
            repository.update(t);
            repository.getAll(Table.class);
            repository.delete(t);
        }).doesNotThrowAnyException();
    }

    @Test
    public void restaurantsTest()
    {
        Restaurant r = new Restaurant();

        assertThatCode(() -> {
            repository.add(r);
            repository.get(r.getId(),Restaurant.class);
            repository.update(r);
            repository.getAll(Restaurant.class);
            repository.delete(r);
        }).doesNotThrowAnyException();
    }

    @Test
    public void wrongModelClassTests()
    {
        Object o = new Object();

        assertAll(
                () -> assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(() ->repository.add(o)),
                () -> assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(() ->repository.update(o)),
                () -> assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(() ->repository.delete(o)),
                () -> assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(() ->repository.get(1L,Object.class)),
                () -> assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(() ->repository.add(Object.class))
        );
    }

}

package services;

import data.Repository;
import errors.EntryNotFoundException;
import errors.ValidationException;
import models.Restaurant;
import models.Table;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.easymock.EasyMock.*;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;

public class TableServiceTest {
    private Repository repository;
    private Table table;
    private Restaurant restaurant;

    //Tested
    private TableService tableService;

    @BeforeEach
    public void setUp()
    {
        repository = createMock(Repository.class);
        table = mock(Table.class);
        restaurant = mock(Restaurant.class);

        tableService = new TableService(repository);
    }

    @Test
    public void addValidTable()
    {
        expect(table.isValid()).andReturn(true);
        expect(table.getId()).andReturn(1L);
        expect(table.getRestaurantId()).andReturn(1L);
        repository.add(table);
        expectLastCall().andAnswer(()->null);
        expect(repository.get(1L,Restaurant.class)).andReturn(restaurant);

        replay(table);
        replay(restaurant);
        replay(repository);

        assertThatCode(
                () -> tableService.add(table)
        ).doesNotThrowAnyException();

        verify(restaurant);
        verify(repository);
        verify(table);
    }

    @Test
    public void addInvalidTable()
    {
        expect(table.isValid()).andReturn(false);
        expect(table.getValidationError()).andReturn("validation error message");

        replay(table);
        replay(repository);

        assertThatExceptionOfType(ValidationException.class).isThrownBy(
                () -> tableService.add(table)
        ).withMessageContaining("Table");

        verify(table);
        verify(repository);
    }

    @Test
    public void addTableWithNonExistingRestaurant()
    {
        expect(table.isValid()).andReturn(true);
        expect(table.getRestaurantId()).andReturn(1L).times(2);
        expect(repository.get(1L,Restaurant.class)).andReturn(null);

        replay(table);
        replay(repository);

        assertThatExceptionOfType(EntryNotFoundException.class).isThrownBy(
                () -> tableService.add(table)
        ).withMessageContaining("Restaurant");

        verify(table);
        verify(repository);
    }

    @Test
    public void deleteExistingTable()
    {
        expect(table.getId()).andReturn(1L);
        expect(repository.get(1L,Table.class)).andReturn(table);
        repository.delete(table);
        expectLastCall().andAnswer(()->null);

        replay(table);
        replay(repository);

        assertThatCode(
                () -> tableService.delete(table)
        ).doesNotThrowAnyException();

        verify(table);
        verify(repository);
    }

    @Test
    public void deleteNonExistingTable()
    {
        expect(table.getId()).andReturn(1L).times(2);
        expect(repository.get(1L,Table.class)).andReturn(null);

        replay(table);
        replay(repository);

        assertThatExceptionOfType(EntryNotFoundException.class).isThrownBy(
                () -> tableService.delete(table)
        ).withMessageContaining("Table");

        verify(table);
        verify(repository);
    }

    @Test
    public void updateExistingValidTable()
    {
        expect(table.isValid()).andReturn(true);
        expect(table.getId()).andReturn(1L);
        expect(table.getRestaurantId()).andReturn(1L);
        repository.update(table);
        expectLastCall().andAnswer(()->null);
        expect(repository.get(1L,Restaurant.class)).andReturn(restaurant);
        expect(repository.get(1L,Table.class)).andReturn(table);

        replay(table);
        replay(restaurant);
        replay(repository);

        assertThatCode(
                () -> tableService.update(table)
        ).doesNotThrowAnyException();

        verify(repository);
        verify(restaurant);
        verify(table);
    }

    @Test
    public void updateNonExistingTable()
    {
        expect(table.getId()).andReturn(1L).times(2);
        expect(repository.get(1L,Table.class)).andReturn(null);

        replay(table);
        replay(repository);

        assertThatExceptionOfType(EntryNotFoundException.class).isThrownBy(
                () -> tableService.update(table)
        ).withMessageContaining("Table");

        verify(repository);
        verify(table);
    }

    @Test
    public void updateRestaurantWithNonExistingRestaurant()
    {
        expect(table.getId()).andReturn(1L);
        expect(table.getRestaurantId()).andReturn(1L).times(2);
        expect(repository.get(1L,Table.class)).andReturn(table);
        expect(repository.get(1L,Restaurant.class)).andReturn(null);

        replay(table);
        replay(repository);

        assertThatExceptionOfType(EntryNotFoundException.class).isThrownBy(
                () -> tableService.update(table)
        ).withMessageContaining("Restaurant");

        verify(repository);
        verify(table);
    }

    @Test
    public void updateRestaurantWithInvalidData()
    {
        expect(table.getId()).andReturn(1L);
        expect(table.getRestaurantId()).andReturn(1L);
        expect(table.isValid()).andReturn(false);
        expect(table.getValidationError()).andReturn("validation error message");
        expect(repository.get(1L,Table.class)).andReturn(table);
        expect(repository.get(1L,Restaurant.class)).andReturn(restaurant);

        replay(table);
        replay(repository);

        assertThatExceptionOfType(ValidationException.class).isThrownBy(
                () -> tableService.update(table)
        ).withMessageContaining("Table");

        verify(repository);
        verify(table);
    }

    @Test
    public void getTable()
    {
        expect(repository.get(1L, Table.class)).andReturn(table);

        replay(repository);
        replay(table);

        Table t = tableService.get(1L);

        assertThat(t).isEqualTo(table);

        verify(repository);
        verify(table);
    }
}

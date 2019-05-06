package services;

import data.Repository;
import models.Restaurant;
import models.Table;
import errors.EntryNotFoundException;
import errors.ValidationException;

public class TableService
{
    private Repository database;

    public TableService(Repository db)
    {
        database = db;
    }

    public Long add(Table table) throws ValidationException,EntryNotFoundException
    {
        RestaurantService restaurantService = new RestaurantService(database);
        if(!table.isValid())
            throw new ValidationException("Table",table.getValidationError());

        if(null==restaurantService.get(table.getRestaurantId()))
            throw new EntryNotFoundException("Restaurant",table.getRestaurantId());

        database.add(table);

        return table.getId();
    }

    public void delete(Table table) throws EntryNotFoundException
    {
        Table t = database.get(table.getId(),Table.class);
        if(t==null)
            throw new EntryNotFoundException("Table",table.getId());

        database.delete(table);
    }

    public void update(Table table) throws ValidationException,EntryNotFoundException
    {
        RestaurantService restaurantService = new RestaurantService(database);
        Table t = database.get(table.getId(),Table.class);
        if(t==null)
            throw new EntryNotFoundException("Table",table.getId());

        if(null==restaurantService.get(table.getRestaurantId()))
            throw new EntryNotFoundException("Restaurant",table.getRestaurantId());

        if(!table.isValid())
            throw new ValidationException("Table",table.getValidationError());

        database.update(table);
    }

    public Table get(Long id)
    {
        return database.get(id,Table.class);
    }

    public Restaurant getRestaurant(Table table) throws EntryNotFoundException
    {
        RestaurantService restaurantService = new RestaurantService(database);
        Table t = database.get(table.getId(),Table.class);
        if(t==null)
            throw new EntryNotFoundException("Table",table.getId());

        return restaurantService.get(table.getRestaurantId());
    }
}
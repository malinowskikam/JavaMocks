package services;

import data.Repository;
import models.Restaurant;
import models.Table;
import errors.EntryNotFoundException;
import errors.ValidationException;

public class TableService
{
    private Repository database;

    private RestaurantService restaurantService;

    public TableService(Repository db)
    {
        database = db;
        restaurantService = new RestaurantService(db);
    }

    public Long addTable(Table table) throws ValidationException,EntryNotFoundException
    {
        if(!table.isValid())
            throw new ValidationException("Table",table.getValidationError());

        if(null==restaurantService.get(table.getRestaurantId()))
            throw new EntryNotFoundException("Restaurant",table.getRestaurantId());

        database.add(table);

        return table.getId();
    }

    public void deleteTable(Table table) throws EntryNotFoundException
    {
        Table t = database.get(table.getId(),Table.class);
        if(t==null)
            throw new EntryNotFoundException("Table",table.getId());

        database.delete(table);
    }

    public void updateTable(Table table) throws ValidationException,EntryNotFoundException
    {
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
        Table t = database.get(table.getId(),Table.class);
        if(t==null)
            throw new EntryNotFoundException("Table",table.getId());

        Restaurant r = restaurantService.get(table.getRestaurantId());
        if(r==null)
            throw new EntryNotFoundException("Restaurant",table.getRestaurantId());

        return r;
    }
}
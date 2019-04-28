package services;

import data.Repository;
import models.Restaurant;
import errors.EntryNotFoundException;
import errors.ValidationException;

public class RestaurantService
{
    private Repository database;

    public RestaurantService(Repository db)
    {
        database = db;
    }

    public Long add(Restaurant restaurant) throws ValidationException
    {
        if(!restaurant.isValid())
            throw new ValidationException("Restaurant",restaurant.getValidationError());

        database.add(restaurant);

        return restaurant.getId();
    }

    public void deleteRestaurant(Restaurant restaurant) throws EntryNotFoundException
    {
        Restaurant r = database.get(restaurant.getId(),Restaurant.class);
        if(r==null)
            throw new EntryNotFoundException("Restaurant",restaurant.getId());

        database.delete(r);
    }

    public void updateRestaurant(Restaurant restaurant) throws ValidationException,EntryNotFoundException
    {
        Restaurant r = database.get(restaurant.getId(),Restaurant.class);
        if(r==null)
            throw new EntryNotFoundException("Restaurant",restaurant.getId());

        if(!restaurant.isValid())
            throw new ValidationException("Restaurant",restaurant.getValidationError());

        database.update(restaurant);
    }

    public Restaurant get(Long id)
    {
        return database.get(id,Restaurant.class);
    }
}
package services;

import data.Repository;
import models.Reservation;
import models.Restaurant;
import errors.EntryNotFoundException;
import errors.ValidationException;
import models.Table;
import models.User;

import java.util.ArrayList;
import java.util.List;

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

    public void delete(Restaurant restaurant) throws EntryNotFoundException
    {
        Restaurant r = database.get(restaurant.getId(),Restaurant.class);
        if(r==null)
            throw new EntryNotFoundException("Restaurant",restaurant.getId());

        database.delete(r);
    }

    public void update(Restaurant restaurant) throws ValidationException,EntryNotFoundException
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

    public List<Table> getTables(Restaurant restaurant) throws EntryNotFoundException
    {
        TableService tableService = new TableService(database);
        Restaurant r = database.get(restaurant.getId(),Restaurant.class);
        if(r==null)
            throw new EntryNotFoundException("Restaurant",restaurant.getId());

        List<Table> tables = database.getAll(Table.class);

        List<Table> restaurantsTables = new ArrayList<>();

        for(Table t : tables)
        {
            if(tableService.getRestaurant(t).equals(r))
                restaurantsTables.add(t);
        }

        return restaurantsTables;
    }

    public List<Reservation> getReservations(Restaurant restaurant) throws EntryNotFoundException
    {
        ReservationService reservationService = new ReservationService(database);
        Restaurant r = database.get(restaurant.getId(),Restaurant.class);
        if(r==null)
            throw new EntryNotFoundException("Restaurant",restaurant.getId());

        List<Reservation> reservations = database.getAll(Reservation.class);
        List<Table> tables = getTables(r);

        List<Reservation> restaurantsReservations = new ArrayList<>();

        for(Reservation res : reservations)
        {
            if(tables.contains(reservationService.getTable(res)))
                restaurantsReservations.add(res);
        }

        return restaurantsReservations;
    }

    public List<User> getUsers(Restaurant restaurant) throws EntryNotFoundException
    {
        ReservationService reservationService = new ReservationService(database);
        Restaurant r = database.get(restaurant.getId(),Restaurant.class);
        if(r==null)
            throw new EntryNotFoundException("Restaurant",restaurant.getId());

        List<Reservation> reservations = getReservations(restaurant);

        List<User> users = new ArrayList<>();

        for(Reservation reservation : reservations)
        {
            users.add(reservationService.getUser(reservation));
        }

        return users;
    }
}
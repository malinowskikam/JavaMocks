package services;

import data.Repository;
import models.*;
import errors.EntryNotFoundException;
import errors.ValidationException;
import validation.Validators;

public class ReservationService
{
    private Repository database;

    public ReservationService(Repository db)
    {
        database = db;
    }

    public Long add(Reservation reservation) throws ValidationException, EntryNotFoundException
    {
        UserService userService = new UserService(database);
        TableService tableService = new TableService(database);
        RestaurantService restaurantService = new RestaurantService(database);

        if(!reservation.isValid())
            throw new ValidationException("Reservation",reservation.getValidationError());

        if(null== userService.get(reservation.getUserId()))
            throw new EntryNotFoundException("User",reservation.getUserId());

        Table table = tableService.get(reservation.getTableId());
        if(null==table)
            throw new EntryNotFoundException("Table",reservation.getTableId());
        else
        {
            Restaurant restaurant = restaurantService.get(table.getRestaurantId());
            if(!Validators.isValidReservationTime(reservation,restaurant))
                throw new ValidationException("Reservation","reservation time should be in restaurant working hours");
        }

        database.add(reservation);

        return reservation.getId();
    }

    public void delete(Reservation reservation) throws EntryNotFoundException
    {
        Reservation r = database.get(reservation.getId(),Reservation.class);
        if(r==null)
            throw new EntryNotFoundException("Reservation",reservation.getId());

        database.delete(reservation);
    }

    public void update(Reservation reservation) throws ValidationException,EntryNotFoundException
    {
        UserService userService = new UserService(database);
        TableService tableService = new TableService(database);
        RestaurantService restaurantService = new RestaurantService(database);

        Reservation r = database.get(reservation.getId(),Reservation.class);
        if(r==null)
            throw new EntryNotFoundException("Reservation",reservation.getId());

        if(null== userService.get(reservation.getUserId()))
            throw new EntryNotFoundException("User",reservation.getUserId());

        Table table = tableService.get(reservation.getTableId());
        if(null==table)
            throw new EntryNotFoundException("Table",reservation.getTableId());
        else
        {
            Restaurant restaurant = restaurantService.get(table.getRestaurantId());
            if(!Validators.isValidReservationTime(reservation,restaurant))
                throw new ValidationException("Reservation","reservation time should be in restaurant working hours");
        }

        if(!reservation.isValid())
            throw new ValidationException("Reservation",reservation.getValidationError());

        database.update(reservation);
    }

    public Reservation get(Long id)
    {
        return database.get(id,Reservation.class);
    }

    public Table getTable(Reservation reservation) throws EntryNotFoundException
    {
        TableService tableService = new TableService(database);

        Reservation r = database.get(reservation.getId(),Reservation.class);
        if(r==null)
            throw new EntryNotFoundException("Reservation",reservation.getId());

        return tableService.get(r.getTableId());
    }

    public User getUser(Reservation reservation) throws EntryNotFoundException
    {
        UserService userService = new UserService(database);

        Reservation r = database.get(reservation.getId(),Reservation.class);
        if(r==null)
            throw new EntryNotFoundException("Reservation",reservation.getId());

        return userService.get(r.getUserId());
    }

}
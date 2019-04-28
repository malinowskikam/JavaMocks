package services;

import data.Repository;
import models.*;
import errors.EntryNotFoundException;
import errors.ValidationException;
import validation.Validators;

public class ReservationService
{
    private Repository database;

    private UserService userService;
    private TableService tableService;
    private RestaurantService restaurantService;

    public ReservationService(Repository db)
    {
        database = db;
        userService = new UserService(db);
        tableService = new TableService(db);
        restaurantService = new RestaurantService(db);
    }

    public Long add(Reservation reservation) throws ValidationException, EntryNotFoundException
    {
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

}
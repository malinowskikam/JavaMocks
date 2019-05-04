package services;

import data.Repository;
import models.*;
import errors.*;

import java.util.ArrayList;
import java.util.List;

public class UserService
{
    private Repository database;

    public UserService(Repository db)
    {
        database=db;
    }

    public Long add(User user) throws ValidationException
    {
        if(!user.isValid())
            throw new ValidationException("User",user.getValidationError());

        database.add(user);

        return user.getId();
    }

    public void delete(User user) throws EntryNotFoundException
    {
        User u = database.get(user.getId(),User.class);
        if(u==null)
            throw new EntryNotFoundException("User",user.getId());

        database.delete(u);
    }

    public void update(User user) throws ValidationException,EntryNotFoundException
    {
        User u = database.get(user.getId(),User.class);
        if(u==null)
            throw new EntryNotFoundException("User",user.getId());

        if(!user.isValid())
            throw new ValidationException("User",user.getValidationError());

        database.update(user);
    }

    public User get(Long id)
    {
        return database.get(id,User.class);
    }

    public List<Reservation> getReservations(User user) throws EntryNotFoundException
    {
        User u = database.get(user.getId(),User.class);
        if(u==null)
            throw new EntryNotFoundException("User",user.getId());

        List<Reservation> reservations = database.getAll(Reservation.class);

        List<Reservation> usersReservations = new ArrayList<>();
        for(Reservation reservation : reservations)
        {
            if(reservation.getUserId().equals(u.getId()))
                usersReservations.add(reservation);
        }

        return usersReservations;
    }

    public User register(String email,String password) throws ValidationException
    {
        User u = new User(null,email,password,false,User.Type.STANDARD);

        add(u);

        return u;
    }

    public void changePassword(User user, String oldPassword, String newPassword) throws ValidationException,EntryNotFoundException
    {
        User u = get(user.getId());

        if(u==null)
            throw new EntryNotFoundException("User",user.getId());

        if(oldPassword.equals(u.getPassword()))
        {
            u.setPassword(newPassword);
            update(u);
        }
        else
        {
            throw new ValidationException("User","passwords does not match");
        }
    }

    public void activate(User user) throws EntryNotFoundException,ValidationException
    {
        User u = get(user.getId());

        if(u==null)
            throw new EntryNotFoundException("User",user.getId());

        u.setActive(true);
        update(u);
    }


    public User login(String email, String password)
    {
        List<User> users = database.getAll(User.class);

        for (User u : users)
            if (u.getEmail().equals(email) && u.getPassword().equals(password))
                return u;

        return null;
    }

    public void changeType(User user, User.Type type) throws EntryNotFoundException,ValidationException
    {
        User u = get(user.getId());

        if(u==null)
            throw new EntryNotFoundException("User",user.getId());

        u.setUserType(type);
        update(u);
    }

}
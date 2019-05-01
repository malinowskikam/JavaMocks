package services;

import data.Repository;
import models.*;
import errors.*;

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

    public User register(String email,String password) throws ValidationException
    {
        User u = new User(null,email,password,false,User.Type.STANDARD);

        add(u);

        return u;
    }
}
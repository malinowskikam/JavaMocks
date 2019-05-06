package data;

import models.Reservation;
import models.Restaurant;
import models.Table;
import models.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class HashMapRepository implements Repository
{
    private HashMap<Long, Reservation> reservations;
    private HashMap<Long, Restaurant> restaurants;
    private HashMap<Long, Table> tables;
    private HashMap<Long, User> users;

    private Long nextReservationId;
    private Long nextRestaurantId;
    private Long nextTableId;
    private Long nextUserId;

    public HashMapRepository()
    {
        reservations = new HashMap<>();
        restaurants = new HashMap<>();
        tables = new HashMap<>();
        users = new HashMap<>();

        nextReservationId = 0L;
        nextRestaurantId = 0L;
        nextTableId = 0L;
        nextUserId = 0L;
    }

    public Long getNextReservationId() {
        return ++nextReservationId;
    }

    public Long getNextRestaurantId() {
        return ++nextRestaurantId;
    }

    public Long getNextTableId() {
        return ++nextTableId;
    }

    public Long getNextUserId() {
        return ++nextUserId;
    }

    @Override
    public <T> void add(T model) {
        if(model instanceof Reservation)
        {
            Reservation r = (Reservation)model;
            r.setId(getNextReservationId());
            reservations.put(r.getId(),r);
        }
        else if(model instanceof Restaurant)
        {
            Restaurant r = (Restaurant)model;
            r.setId(getNextRestaurantId());
            restaurants.put(r.getId(),r);
        }
        else if(model instanceof Table)
        {
            Table t = (Table)model;
            t.setId(getNextTableId());
            tables.put(t.getId(),t);
        }
        else if(model instanceof User)
        {
            User u = (User)model;
            u.setId(getNextUserId());
            users.put(u.getId(),u);
        }
        else throw new IllegalArgumentException("Wrong argument type");
    }

    @Override
    public <T> void update(T model) {
        if(model instanceof Reservation)
        {
            Reservation r = (Reservation)model;
            reservations.replace(r.getId(),r);
        }
        else if(model instanceof Restaurant)
        {
            Restaurant r = (Restaurant)model;
            restaurants.replace(r.getId(),r);
        }
        else if(model instanceof Table)
        {
            Table t = (Table)model;
            tables.replace(t.getId(),t);
        }
        else if(model instanceof User)
        {
            User u = (User)model;
            users.replace(u.getId(),u);
        }
        else throw new IllegalArgumentException("Wrong argument type");
    }

    @Override
    public <T> T get(long id, Class<T> modelClass) {
        if (modelClass.equals(Reservation.class))
        {
            return (T)reservations.get(id);
        } else if (modelClass.equals(Restaurant.class))
        {
            return (T)restaurants.get(id);
        } else if (modelClass.equals(Table.class))
        {
            return (T)tables.get(id);
        } else if (modelClass.equals(User.class))
        {
            return (T)users.get(id);
        }
        else throw new IllegalArgumentException("Wrong argument type");
    }

    @Override
    public <T> List<T> getAll(Class<T> modelClass) {
        if (modelClass.equals(Reservation.class))
        {
            return (List<T>)new ArrayList<>(reservations.values());
        } else if (modelClass.equals(Restaurant.class))
        {
            return (List<T>)new ArrayList<>(restaurants.values());
        } else if (modelClass.equals(Table.class))
        {
            return (List<T>)new ArrayList<>(tables.values());
        } else if (modelClass.equals(User.class))
        {
            return (List<T>)new ArrayList<>(users.values());
        }
        else throw new IllegalArgumentException("Wrong argument type");
    }

    @Override
    public <T> void delete(T model) {
        if(model instanceof Reservation)
        {
            Reservation r = (Reservation)model;
            reservations.remove(r.getId());
        }
        else if(model instanceof Restaurant)
        {
            Restaurant r = (Restaurant)model;
            restaurants.remove(r.getId());
        }
        else if(model instanceof Table)
        {
            Table t = (Table)model;
            tables.remove(t.getId());
        }
        else if(model instanceof User)
        {
            User u = (User)model;
            users.remove(u.getId());
        }
        else throw new IllegalArgumentException("Wrong argument type");
    }
}

package models;

import validation.Validatable;

import java.io.Serializable;
import java.util.Objects;

import static validation.Validators.isNotNull;
import static validation.Validators.isPositiveInteger;

public class Table implements Validatable, Serializable
{
    private Long id;
    private int seats;
    private Long restaurantId;

    public Table()
    {
    }

    public Table(Long id, int seats, Long restaurantId) {
        this.id = id;
        this.seats = seats;
        this.restaurantId = restaurantId;
    }

    public Long getId()
    {
        return id;
    }

    public void setId(Long id)
    {
        this.id = id;
    }

    public int getSeats()
    {
        return seats;
    }

    public void setSeats(int seats)
    {
        this.seats = seats;
    }

    public Long getRestaurantId()
    {
        return restaurantId;
    }

    public void setRestaurantId(Long restaurantId)
    {
        this.restaurantId = restaurantId;
    }

    @Override
    public boolean isValid()
    {
        return isPositiveInteger(getSeats()) &&
                isNotNull(getRestaurantId());
    }

    @Override
    public String getValidationError()
    {
        if(!isPositiveInteger(getSeats()))
            return "seat number is not valid";
        if(!isNotNull(getRestaurantId()))
            return "restaurant id is not valid";
        return null;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Table table = (Table) o;
        return seats == table.seats &&
                Objects.equals(restaurantId, table.restaurantId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(seats, restaurantId);
    }
}
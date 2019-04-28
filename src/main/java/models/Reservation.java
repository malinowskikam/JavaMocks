package models;

import org.joda.time.LocalDate;
import org.joda.time.LocalTime;
import validation.Validatable;

import java.io.Serializable;
import java.util.Objects;

import static validation.Validators.isMultipleOfHalfhour;
import static validation.Validators.isNotNull;

public class Reservation implements Validatable, Serializable
{
    private Long id;
    private Long userId;
    private Long tableId;
    private LocalTime time;
    private LocalDate date;

    public Reservation()
    {
    }

    public Reservation(Long id, Long userId, Long tableId, LocalTime time, LocalDate date) {
        this.id = id;
        this.userId = userId;
        this.tableId = tableId;
        this.time = time;
        this.date = date;
    }

    public Long getId()
    {
        return id;
    }

    public void setId(Long id)
    {
        this.id = id;
    }

    public Long getUserId()
    {
        return userId;
    }

    public void setUserId(Long userId)
    {
        this.userId = userId;
    }

    public Long getTableId()
    {
        return tableId;
    }

    public void setTableId(Long tableId)
    {
        this.tableId = tableId;
    }

    public LocalTime getTime()
    {
        return time;
    }

    public void setTime(LocalTime time)
    {
        this.time = time;
    }

    public LocalDate getDate()
    {
        return date;
    }

    public void setDate(LocalDate date)
    {
        this.date = date;
    }

    @Override
    public boolean isValid()
    {
        return isNotNull(userId) &&
                isNotNull(tableId) &&
                isNotNull(time) && isMultipleOfHalfhour(time) &&
                isNotNull(date);
    }

    @Override
    public String getValidationError()
    {
        if(!isNotNull(userId))
            return "user id is not valid";
        if(!isNotNull(tableId))
            return "table id is not valid";
        if(!(isNotNull(time) && isMultipleOfHalfhour(time)))
            return "time is not valid";
        if(!isNotNull(date))
            return "date is not valid";
        return null;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Reservation that = (Reservation) o;
        return Objects.equals(userId, that.userId) &&
                Objects.equals(tableId, that.tableId) &&
                Objects.equals(time, that.time) &&
                Objects.equals(date, that.date);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, tableId, time, date);
    }
}
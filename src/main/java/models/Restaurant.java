package models;

import org.joda.time.LocalTime;
import validation.Validatable;

import java.io.Serializable;
import java.util.Objects;

import static validation.Validators.isMultipleOfHalfhour;
import static validation.Validators.isNotEmpty;
import static validation.Validators.isNotNull;

public class Restaurant implements Validatable, Serializable {
    Long id;
    String name;
    String address;
    LocalTime openHour;
    LocalTime closeHour;

    public Restaurant() {
    }

    public Restaurant(Long id, String name, String address, LocalTime openHour, LocalTime closeHour) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.openHour = openHour;
        this.closeHour = closeHour;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public LocalTime getOpenHour() {
        return openHour;
    }

    public void setOpenHour(LocalTime openHour) {
        this.openHour = openHour;
    }

    public LocalTime getCloseHour() {
        return closeHour;
    }

    public void setCloseHour(LocalTime closeHour) {
        this.closeHour = closeHour;
    }

    @Override
    public boolean isValid() {
        return isNotNull(name) && isNotEmpty(name) &&
                isNotNull(address) && isNotEmpty(address) &&
                isNotNull(openHour) && isMultipleOfHalfhour(openHour) &&
                isNotNull(closeHour) && isMultipleOfHalfhour(closeHour);
    }

    @Override
    public String getValidationError() {
        if (!(isNotNull(name) && isNotEmpty(name)))
            return "name is not valid";
        if (!(isNotNull(address) && isNotEmpty(address)))
            return "address is not valid";
        if (!(isNotNull(openHour) && isMultipleOfHalfhour(openHour)))
            return "open hour is not valid";
        if (!(isNotNull(closeHour) && isMultipleOfHalfhour(closeHour)))
            return "close hour is not valid";
        return null;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Restaurant that = (Restaurant) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(name, that.name) &&
                Objects.equals(address, that.address) &&
                Objects.equals(openHour, that.openHour) &&
                Objects.equals(closeHour, that.closeHour);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, address, openHour, closeHour);
    }
}



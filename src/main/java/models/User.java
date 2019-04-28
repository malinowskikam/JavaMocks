package models;

import validation.Validatable;

import java.io.Serializable;
import java.util.Objects;

import static validation.Validators.isEmail;
import static validation.Validators.isNotEmpty;
import static validation.Validators.isNotNull;

public class User implements Validatable, Serializable
{
    public enum Type { STANDARD, MODERATOR, ADMIN }

    private Long id;
    private String email;
    private String password;
    private boolean isActive;
    private Type userType;


    public User()
    {
    }

    public User(Long id, String email, String password, boolean isActive, Type userType) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.isActive = isActive;
        this.userType = userType;
    }

    public Long getId()
    {
        return id;
    }

    public void setId(Long id)
    {
        this.id = id;
    }

    public String getEmail()
    {
        return email;
    }

    public void setEmail(String email)
    {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public Type getUserType() {
        return userType;
    }

    public void setUserType(Type userType) {
        this.userType = userType;
    }

    @Override
    public boolean isValid()
    {
        return
                isNotNull(email) && isNotEmpty(email) && isEmail(email) &&
                isNotNull(password) && isNotEmpty(password) &&
                isNotNull(userType);
    }

    @Override
    public String getValidationError()
    {
        if(!(isNotNull(email) && isNotEmpty(email) && isEmail(email)))
            return "email is not valid";
        if(!(isNotNull(password) && isNotEmpty(password)))
            return "password is not valid";
        if(!isNotNull(userType))
            return "user type is not valid";

        return null;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return isActive == user.isActive &&
                Objects.equals(email, user.email) &&
                Objects.equals(password, user.password) &&
                userType == user.userType;
    }

    @Override
    public int hashCode() {
        return Objects.hash(email, password, isActive, userType);
    }
}
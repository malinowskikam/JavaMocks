package services;

import data.Repository;
import errors.EntryNotFoundException;
import errors.ValidationException;
import models.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.Mockito.*;


//Testy Mockito
public class UserServiceTest {

    //Mocks
    private Repository repository;
    private User user;

    //Tested
    private UserService userService;

    @BeforeEach
    public void setUp()
    {
        repository = mock(Repository.class);
        user = mock(User.class);

        userService = new UserService(repository);
    }

    @Test
    public void addValidUser()
    {
        doAnswer((a)->null).when(repository).add(user);
        doReturn(true).when(user).isValid();

        assertThatCode(
                () -> userService.add(user)
        ).doesNotThrowAnyException();

        verify(repository).add(user);
        verify(user).isValid();
    }

    @Test
    public void addInvalidUser()
    {
        doReturn(false).when(user).isValid();
        doReturn("validation error message").when(user).getValidationError();

        assertThatExceptionOfType(ValidationException.class).isThrownBy(
                () -> userService.add(user)
        ).withMessageContaining("User");

        verify(user).isValid();
        verify(user).getValidationError();
    }

    @Test
    public void deleteExistingUser()
    {
        doReturn(1L).when(user).getId();
        doReturn(user).when(repository).get(1L,User.class);
        doAnswer((a)->null).when(repository).delete(user);

        assertThatCode(
                () -> userService.delete(user)
        ).doesNotThrowAnyException();

        verify(user).getId();
        verify(repository).get(1L,User.class);
        verify(repository).delete(user);
    }

    @Test
    public void deleteNonExistingUser()
    {
        doReturn(1L).when(user).getId();
        doReturn(null).when(repository).get(1L,User.class);

        assertThatExceptionOfType(EntryNotFoundException.class).isThrownBy(
                () -> userService.delete(user)
        ).withMessageContaining("User");

        verify(user,times(2)).getId();
        verify(repository).get(1L,User.class);
    }

    @Test
    public void updateExistingValidUser()
    {
        doReturn(1L).when(user).getId();
        doReturn(true).when(user).isValid();
        doAnswer((a)->null).when(repository).update(user);
        doReturn(user).when(repository).get(1L,User.class);

        assertThatCode(
                () -> userService.update(user)
        ).doesNotThrowAnyException();

        verify(repository).get(1L,User.class);
        verify(user).getId();
        verify(user).isValid();
        verify(repository).update(user);
    }

    @Test
    public void updateNonExistingUser()
    {
        doReturn(1L).when(user).getId();
        doReturn(null).when(repository).get(1L,User.class);

        assertThatExceptionOfType(EntryNotFoundException.class).isThrownBy(
                () -> userService.update(user)
        ).withMessageContaining("User");

        verify(repository).get(1L,User.class);
        verify(user,times(2)).getId();
    }

    @Test
    public void updateUserWithInvalidData()
    {
        doReturn(1L).when(user).getId();
        doReturn(false).when(user).isValid();
        doReturn(user).when(repository).get(1L,User.class);
        doReturn("validation error message").when(user).getValidationError();

        assertThatExceptionOfType(ValidationException.class).isThrownBy(
                () -> userService.update(user)
        ).withMessageContaining("User");

        verify(repository).get(1L,User.class);
        verify(user).getId();
        verify(user).getValidationError();
        verify(user).isValid();
    }

    @Test
    public void getUser()
    {
        doReturn(user).when(repository).get(1L,User.class);

        User u = userService.get(1L);

        assertThat(u).isEqualTo(user);

        verify(repository).get(1L,User.class);
    }

    @Test
    public void registerUser()
    {
        doAnswer((a)->null).when(repository).add(any());

        assertThatCode(
                () -> userService.register("email@poczta.pl","haslo1")
        ).doesNotThrowAnyException();

        verify(repository).add(any());
    }

    @Test
    public void changePassword()
    {
        doReturn(user).when(repository).get(1L,User.class);
        doReturn(1L).when(user).getId();
        doReturn("old_password").when(user).getPassword();
        doReturn(true).when(user).isValid();
        doAnswer((a)->null).when(repository).update(user);
        doAnswer((a)->null).when(user).setPassword("new_password");

        assertThatCode(
                () -> userService.changePassword(user,"old_password","new_password")
        ).doesNotThrowAnyException();

        verify(repository,times(2)).get(1L,User.class);
        verify(user,times(2)).getId();
        verify(user).getPassword();
        verify(user).isValid();
        verify(repository).update(user);
        verify(user).setPassword("new_password");
    }

    @Test
    public void changePasswordPasswordsDoesNotMatch()
    {
        doReturn(user).when(repository).get(1L,User.class);
        doReturn(1L).when(user).getId();
        doReturn("old_password").when(user).getPassword();

        assertThatExceptionOfType(ValidationException.class).isThrownBy(
                () -> userService.changePassword(user,"wrong_password","new_password")
        ).withMessageContaining("User");

        verify(repository).get(1L,User.class);
        verify(user).getId();
        verify(user).getPassword();
    }

    @Test
    public void changePasswordOfNonExistingUser()
    {
        doReturn(null).when(repository).get(1L,User.class);
        doReturn(1L).when(user).getId();

        assertThatExceptionOfType(EntryNotFoundException.class).isThrownBy(
                () -> userService.changePassword(user,"new_password","new_password")
        ).withMessageContaining("User");

        verify(repository).get(1L,User.class);
        verify(user,times(2)).getId();
    }

    @Test
    public void activateUser()
    {
        doReturn(user).when(repository).get(1L,User.class);
        doReturn(1L).when(user).getId();
        doReturn(true).when(user).isValid();
        doAnswer((a)->null).when(repository).update(user);
        doAnswer((a)->null).when(user).setActive(true);

        assertThatCode(
                () -> userService.activate(user)
        ).doesNotThrowAnyException();

        verify(repository,times(2)).get(1L,User.class);
        verify(user,times(2)).getId();
        verify(user).isValid();
        verify(repository).update(user);
        verify(user).setActive(true);
    }

    @Test
    public void loginUser()
    {
        String email = "email@poczta.pl";
        String password = "password";

        doReturn(password).when(user).getPassword();
        doReturn(email).when(user).getEmail();
        doReturn(Arrays.asList(user)).when(repository).getAll(User.class);

        User u = userService.login(email,password);

        assertThat(u).isEqualTo(user);

        verify(user).getPassword();
        verify(user).getEmail();
        verify(repository).getAll(User.class);
    }

    @Test
    public void loginWrongCredentials()
    {
        String email = "email@poczta.pl";
        String password = "password";

        doReturn(new ArrayList<User>()).when(repository).getAll(User.class);

        User u = userService.login(email,password);

        assertThat(u).isNull();

        verify(repository).getAll(User.class);
    }

    @Test
    public void changeUserType()
    {
        doReturn(user).when(repository).get(1L,User.class);
        doReturn(1L).when(user).getId();
        doReturn(true).when(user).isValid();
        doAnswer((a)->null).when(repository).update(user);
        doAnswer((a)->null).when(user).setUserType(User.Type.MODERATOR);

        assertThatCode(
                () -> userService.changeType(user,User.Type.MODERATOR)
        ).doesNotThrowAnyException();

        verify(repository,times(2)).get(1L,User.class);
        verify(user,times(2)).getId();
        verify(user).isValid();
        verify(repository).update(user);
        verify(user).setUserType(User.Type.MODERATOR);
    }

    @Test
    public void changeTypeOfNonExistingUser()
    {
        doReturn(null).when(repository).get(1L,User.class);
        doReturn(1L).when(user).getId();

        assertThatExceptionOfType(EntryNotFoundException.class).isThrownBy(
                () -> userService.changeType(user,User.Type.MODERATOR)
        ).withMessageContaining("User");

        verify(repository).get(1L,User.class);
        verify(user,times(2)).getId();
    }

}

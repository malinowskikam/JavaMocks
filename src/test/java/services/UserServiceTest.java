package services;

import data.Repository;
import errors.EntryNotFoundException;
import errors.ValidationException;
import models.Reservation;
import models.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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
        doReturn(1L).when(user).getId();

        assertThatCode(
                () -> userService.add(user)
        ).doesNotThrowAnyException();

        verify(repository).add(user);
        verify(user).isValid();
        verify(user).getId();
        verifyNoMoreInteractions(repository);
        verifyNoMoreInteractions(user);
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
        verifyNoMoreInteractions(user);
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
        verifyNoMoreInteractions(repository);
        verifyNoMoreInteractions(user);
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
        verifyNoMoreInteractions(repository);
        verifyNoMoreInteractions(user);
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

        verify(user).getId();
        verify(user).isValid();
        verify(repository).update(user);
        verify(repository).get(1L,User.class);
        verifyNoMoreInteractions(repository);
        verifyNoMoreInteractions(user);
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
        verifyNoMoreInteractions(repository);
        verifyNoMoreInteractions(user);
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
        verifyNoMoreInteractions(repository);
        verifyNoMoreInteractions(user);
    }

    @Test
    public void getUser()
    {
        doReturn(user).when(repository).get(1L,User.class);

        User u = userService.get(1L);

        assertThat(u).isEqualTo(user);

        verify(repository).get(1L,User.class);
        verifyNoMoreInteractions(repository);
        verifyNoMoreInteractions(user);
    }

    @Test
    public void getReservations() throws Exception
    {
        Reservation reservation1 = mock(Reservation.class);
        Reservation reservation2 = mock(Reservation.class);

        doReturn(1L).when(user).getId();
        doReturn(user).when(repository).get(1L,User.class);
        doReturn(Arrays.asList(reservation1,reservation2)).when(repository).getAll(Reservation.class);

        doReturn(1L).when(reservation1).getUserId();
        doReturn(1L).when(reservation2).getUserId();

        List<Reservation> reservations = userService.getReservations(user);

        assertThat(reservations.size()).isEqualTo(2);

        verify(repository).getAll(Reservation.class);
        verify(user,times(3)).getId();
        verify(repository).get(1L,User.class);
        verify(reservation1).getUserId();
        verify(reservation2).getUserId();
        verifyNoMoreInteractions(repository);
        verifyNoMoreInteractions(user);
    }

    @Test
    public void registerUser()
    {
        doAnswer((a)->null).when(repository).add(any());

        assertThatCode(
                () -> userService.register("email@poczta.pl","haslo1")
        ).doesNotThrowAnyException();

        verify(repository).add(any());
        verifyNoMoreInteractions(repository);
        verifyNoMoreInteractions(user);
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
        verifyNoMoreInteractions(repository);
        verifyNoMoreInteractions(user);
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
        verifyNoMoreInteractions(repository);
        verifyNoMoreInteractions(user);
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
        verifyNoMoreInteractions(repository);
        verifyNoMoreInteractions(user);
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
        verifyNoMoreInteractions(repository);
        verifyNoMoreInteractions(user);
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
        verifyNoMoreInteractions(repository);
        verifyNoMoreInteractions(user);
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
        verifyNoMoreInteractions(repository);
        verifyNoMoreInteractions(user);
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
        verifyNoMoreInteractions(repository);
        verifyNoMoreInteractions(user);
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
        verifyNoMoreInteractions(repository);
        verifyNoMoreInteractions(user);
    }

}

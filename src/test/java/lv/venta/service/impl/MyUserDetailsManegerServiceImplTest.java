package lv.venta.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.test.util.ReflectionTestUtils;

import lv.venta.model.MyUser;
import lv.venta.repo.IMyUserRepo;

class MyUserDetailsManegerServiceImplTest {

    @Test
    void loadUserByUsername_whenUserExists_returnsUserDetails() {
        IMyUserRepo repo = mock(IMyUserRepo.class);

        String username = "kristers";

        MyUser user = new MyUser();
        user.setUsername(username); // <-- explicit, readable, Sonar-safe

        when(repo.existsByUsername(username)).thenReturn(true);
        when(repo.findByUsername(username)).thenReturn(user);

        MyUserDetailsManegerServiceImpl service = new MyUserDetailsManegerServiceImpl();
        ReflectionTestUtils.setField(service, "userRepo", repo);

        UserDetails details = service.loadUserByUsername(username);

        assertNotNull(details);
        assertEquals(username, details.getUsername());

        verify(repo).existsByUsername(username);
        verify(repo).findByUsername(username);
    }

    @Test
    void loadUserByUsername_whenUserDoesNotExist_throwsException() {
        IMyUserRepo repo = mock(IMyUserRepo.class);

        String username = "missing";

        when(repo.existsByUsername(username)).thenReturn(false);

        MyUserDetailsManegerServiceImpl service = new MyUserDetailsManegerServiceImpl();
        ReflectionTestUtils.setField(service, "userRepo", repo);

        assertThrows(
                UsernameNotFoundException.class,
                () -> service.loadUserByUsername(username)
        );

        verify(repo).existsByUsername(username);
        verify(repo, never()).findByUsername(any());
    }
}

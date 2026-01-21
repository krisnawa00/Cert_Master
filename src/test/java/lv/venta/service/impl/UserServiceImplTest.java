package lv.venta.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.springframework.test.util.ReflectionTestUtils;

import lv.venta.model.MyUser;
import lv.venta.repo.IMyUserRepo;

class UserServiceImplTest {

    @Test
    void getUserIdFromUsername_returnsUserId() {
        UserServiceImpl service = new UserServiceImpl();

        IMyUserRepo repoMock = mock(IMyUserRepo.class);
        ReflectionTestUtils.setField(service, "userRepo", repoMock);

        MyUser userMock = mock(MyUser.class);
        when(userMock.getUId()).thenReturn(42);
        when(repoMock.findByUsername("testuser")).thenReturn(userMock);

        int result = service.getUserIdFromUsername("testuser");

        assertEquals(42, result);
        verify(repoMock, times(1)).findByUsername("testuser");
        verify(userMock, times(1)).getUId();
    }
}

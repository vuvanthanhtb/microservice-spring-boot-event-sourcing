package com.thanhvv.user;

import com.thanhvv.user.controller.UserController;
import com.thanhvv.user.data.User;
import com.thanhvv.user.model.UserDto;
import com.thanhvv.user.service.UserService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
public class UserControllerTests {
    @Mock
    private UserService userService;

    @InjectMocks
    private UserController userController;

    private User user;
    private UserDto userDto;

    @BeforeEach
    public void setUp() {
        this.user = new User("dGhhbmh2diBraW5kbGUgMjAy", "vuvanthanhtb@gmail.com", "dGhhbmh2diBraW5kbGUgMjAy===", "employeeId");
        this.userDto = new UserDto("dGhhbmh2diBraW5kbGUgMjAy", "vuvanthanhtb@gmail.com", "dGhhbmh2diBraW5kbGUgMjAy===", "employeeId", "dGhhbmh2diBraW5kbGUgMjAy", "dGhhbmh2diBraW5kbGUgMjAy");
        ReflectionTestUtils.setField(this.userController, "userService", this.userService);
    }

    @Test
    void getAllUsers() {
        List<User> users = new ArrayList<>();
        users.add(user);
        when(this.userService.getAllUsers()).thenReturn(users);
        Assertions.assertEquals(users, this.userController.getAllUsers());
    }

    @Test
    void createUser() {
        when(this.userService.createUser(user)).thenReturn(this.user);
        Assertions.assertEquals(this.user, this.userController.createUser(user));
    }

    @Test
    void login() {
        when(this.userService.login(anyString(), anyString())).thenReturn(userDto);
        Assertions.assertEquals(userDto, this.userController.login(anyString(), anyString()));
    }
}

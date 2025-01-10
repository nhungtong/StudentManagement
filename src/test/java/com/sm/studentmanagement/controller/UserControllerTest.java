package com.sm.studentmanagement.controller;

import com.sm.studentmanagement.entity.User;
import com.sm.studentmanagement.service.UserService;

import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import org.springframework.ui.Model;
import org.springframework.security.core.Authentication;

import java.util.Arrays;
import java.util.List;

@ExtendWith(MockitoExtension.class)
public class UserControllerTest {

    private MockMvc mockMvc;

    @Mock
    private SecurityContext securityContext;

    @Mock
    private Authentication authentication;

    @Mock
    private UserService userService;

    @InjectMocks
    private UserController userController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        SecurityContextHolder.setContext(securityContext);
        mockMvc = MockMvcBuilders.standaloneSetup(userController).build();
    }

    @Test
    void testShowRegisterPage() throws Exception {
        mockMvc.perform(get("/user/register"))
                .andExpect(status().isOk())
                .andExpect(view().name("user/register"));
    }
    @Test
    void testRegisterUser() throws Exception {
        when(userService.existsByUserName("testUser")).thenReturn(false);

        mockMvc.perform(post("/user/register")
                        .param("userName", "testUser")
                        .param("userPassword", "password123"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/user/login"));

        verify(userService, times(1)).saveUser(any());
    }

    @Test
    void testShowLoginPage() throws Exception {
        mockMvc.perform(get("/user/login"))
                .andExpect(status().isOk())
                .andExpect(view().name("user/login"));
    }

    @Test
    void testHandleLogin() throws Exception {
        Authentication authentication = Mockito.mock(Authentication.class);
        Mockito.when(authentication.isAuthenticated()).thenReturn(true);
        HttpServletRequest request = Mockito.mock(HttpServletRequest.class);
        Model model = Mockito.mock(Model.class);
        RedirectAttributes redirectAttributes = Mockito.mock(RedirectAttributes.class);

        String result = userController.handleLogin(request, authentication, model, redirectAttributes);
        assertEquals("redirect:/user/dashboard", result);
    }
    @Test
    void testUserDashboard() throws Exception {
        Authentication authentication = Mockito.mock(Authentication.class);
        when(authentication.isAuthenticated()).thenReturn(true);
        when(authentication.getName()).thenReturn("testUser");

        SecurityContext securityContext = Mockito.mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);

        User user = new User();
        user.setUserName("testUser");
        when(userService.findByUserName("testUser")).thenReturn(user);

        mockMvc.perform(get("/user/dashboard"))
                .andExpect(view().name("user/dashboard"));
    }
    @Test
    void testShowUpdateForm() throws Exception
    {
        User user = new User();
        user.setUserName("testUser");

        when(userService.findByUserName("testUser")).thenReturn(user);

        Authentication authentication = Mockito.mock(Authentication.class);
        when(authentication.getName()).thenReturn("testUser");
        SecurityContext securityContext = Mockito.mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);

        mockMvc.perform(get("/user/update"))
                .andExpect(view().name("user/update"))
                .andExpect(model().attribute("user", user));

        verify(userService, times(1)).findByUserName("testUser");
    }

    @Test
    void testUpdateUser() throws Exception {
        Long userId = 1L;
        String userName = "testUser";
        String userPassword = "newPassword";
        String userEmail = "testuser@example.com";
        String userFullName = "Test User";

        when(userService.updateUser(userId, userName, userPassword, userEmail, userFullName)).thenReturn(true);
        mockMvc.perform(post("/user/update")
                        .param("userId", String.valueOf(userId))
                        .param("userName", userName)
                        .param("userPassword", userPassword)
                        .param("userEmail", userEmail)
                        .param("userFullName", userFullName))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/user/dashboard"))
                .andExpect(flash().attribute("message", "Updated successfully!"));
    }
    @Test
    void testLogout() throws Exception {
        when(securityContext.getAuthentication()).thenReturn(authentication);

        mockMvc.perform(get("/user/logout"))
                .andExpect(redirectedUrl("/user/login"));

        verify(securityContext, times(1)).getAuthentication();
        verify(authentication, never()).isAuthenticated(); // Không cần gọi thêm isAuthenticated ở đây.
    }

    private List<User> createTestUsers() {
        User user1 = new User();
        user1.setUserId(1L);
        user1.setUserName("user1");
        user1.setUserRole("USER1");
        user1.setUserEmail("user1@example.com");
        user1.setUserFullName("User One");

        User user2 = new User();
        user2.setUserId(2L);
        user2.setUserName("user2");
        user2.setUserRole("USER2");
        user2.setUserEmail("user2@example.com");
        user2.setUserFullName("User Two");

        return Arrays.asList(user1, user2);
    }

    @Test
    void testShowUserList() throws Exception {

        List<User> mockUsers = createTestUsers();

        when(userService.getAllUsers()).thenReturn(mockUsers);

        mockMvc.perform(get("/user/management"))
                .andExpect(status().isOk())
                .andExpect(view().name("user/management"))
                .andExpect(model().attributeExists("users"))
                .andExpect(model().attribute("users", mockUsers));

        verify(userService, times(1)).getAllUsers();
    }

    @Test
    void testDeleteUser() throws Exception {
        Long userId = 1L;

        mockMvc.perform(post("/user/delete/{id}", userId))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/user/management"))
                .andExpect(flash().attribute("message", "Deleted successfully!"));

        verify(userService, times(1)).deleteUserById(userId);
    }
}

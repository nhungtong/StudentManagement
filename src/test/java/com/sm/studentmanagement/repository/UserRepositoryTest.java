package com.sm.studentmanagement.repository;

import com.sm.studentmanagement.entity.User;
import com.sm.studentmanagement.service.UserService;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.jpa.repository.Query;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class UserRepositoryTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    private User createTestUser() {
        User user = new User();
        user.setUserId(1L);
        user.setUserName("testUser");
        user.setUserPassword("test");
        user.setUserRole("USER");
        user.setUserEmail("test@gmail.com");
        user.setUserFullName("Test User");
        return user;
    }

    @Test
    public void testFindByUsername() {

        User user = createTestUser();

        when(userRepository.findByUserName("testUser")).thenReturn(user);
        User foundUser = userRepository.findByUserName("testUser");
        assertThat(foundUser.getUserName()).isEqualTo("testUser");
    }

    @Test
    public void testExitByUsername() {

        when(userRepository.existsByUserName("testUser")).thenReturn(true);
        boolean result = userService.existsByUserName("testUser");
        assertThat(result).isTrue();
        verify(userRepository, times(1)).existsByUserName("testUser");
    }

    @Test
    void testUpdateUser() {
        Long userId = 1L;
        String userName = "john_doe";
        String userPassword = "newpassword";
        String userEmail = "john.doe@example.com";
        String userFullName = "John Doe";

        when(userRepository.updateUser(
                userId, userName, userPassword, userEmail, userFullName))
                .thenReturn(1);

        int updatedCount = userRepository.updateUser(
                userId, userName, userPassword, userEmail, userFullName);

        assertEquals(1, updatedCount);

        verify(userRepository).updateUser(
                userId, userName, userPassword, userEmail, userFullName);
    }
}

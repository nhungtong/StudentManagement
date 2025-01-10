package com.sm.studentmanagement.service;


import com.sm.studentmanagement.entity.Student;
import com.sm.studentmanagement.entity.User;
import com.sm.studentmanagement.repository.StudentRepository;
import com.sm.studentmanagement.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {
    @Mock
    private UserRepository userRepository;

    @Mock
    private StudentRepository studentRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

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
    void testSaveUser() {

        User user = createTestUser();
        userService.saveUser(user);
        verify(userRepository, times(1)).save(user);
    }

    @Test
    void testFindUserName() {

        User user = createTestUser();
        userService.findByUserName("testUser");
        verify(userRepository, times(1)).findByUserName(user.getUserName());
    }

    @Test
    void testLoadUserByUserName() {
        User user = createTestUser();
        when(userRepository.findByUserName("testUser")).thenReturn(user);
        UserDetails userDetails = userService.loadUserByUsername("testUser");
        assertNotNull(userDetails);
        assertEquals("testUser", userDetails.getUsername());
        assertEquals("test", userDetails.getPassword());
        assertTrue(userDetails.getAuthorities().stream()
                .anyMatch(authority -> authority.getAuthority().equals("USER")));
    }

    @Test
    void testExitsByUserName() {
        User user = createTestUser();
        userService.existsByUserName("testUser");
        verify(userRepository, times(1)).existsByUserName(user.getUserName());
    }

    @Test
    void testFindByUserId()
    {
        User user = createTestUser();
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        User result = userService.findByUserId(1L);
        assertNotNull(result);
        assertEquals(1L, result.getUserId());
        assertEquals("testUser", result.getUserName());
    }

    @Test
    void testUpdateUser()
    {
        User existingUser = createTestUser();
        when(userRepository.findById(1L)).thenReturn(Optional.of(existingUser));
        when(userRepository.updateUser(1L, "newUserName", "encodedPassword", "newEmail@example.com", "New Full Name")).thenReturn(1);
        when(passwordEncoder.encode("newPassword")).thenReturn("encodedPassword");
        boolean result = userService.updateUser(1L, "newUserName", "newPassword", "newEmail@example.com", "New Full Name");
        assertTrue(result);
        verify(userRepository, times(1)).updateUser(1L, "newUserName", "encodedPassword", "newEmail@example.com", "New Full Name");
    }

    @Test
    void testGetAllUser(){
        List<User> testUsers = createTestUsers();
        when(userRepository.findAll()).thenReturn(testUsers);
        List<User> result = userService.getAllUsers();
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("user1", result.get(0).getUserName());
        assertEquals("user2", result.get(1).getUserName());
    }

    @Test
    void testDeleteUserById()
    {
        Long userId = 1L;
        userService.deleteUserById(userId);
        verify(userRepository, times(1)).deleteById(userId);
    }

    @Test
    void testGetStudentDetails()
    {
        String username = "testUser";
        User user = new User();
        user.setUserName(username);
        user.setUserEmail("testUser@example.com");

        Student student = new Student();
        student.setStudentEmail("testUser@example.com");
        student.setStudentFullname("Test Student");

        when(userRepository.findByUserName(username)).thenReturn(user);
        when(studentRepository.findStudentByEmail(user.getUserEmail())).thenReturn(Optional.of(student));

        Student result = userService.getStudentDetails(username);

        assertNotNull(result);
        assertEquals("Test Student", result.getStudentFullname());
        verify(userRepository, times(1)).findByUserName(username);
        verify(studentRepository, times(1)).findStudentByEmail(user.getUserEmail());
    }
}

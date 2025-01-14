package com.sm.studentmanagement.service;

import java.util.List;
import java.util.Optional;

import com.sm.studentmanagement.entity.Student;
import com.sm.studentmanagement.entity.User;
import com.sm.studentmanagement.repository.StudentRepository;
import com.sm.studentmanagement.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.Collections;

@Service
@Transactional
public class UserService implements UserDetailsService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;


    public void saveUser(User user) {
        userRepository.save(user);
    }

    public User findByUserName(String userName) {
        return userRepository.findByUserName(userName);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUserName(username);
        if (user == null) {
            throw new UsernameNotFoundException("User not found");
        }
        return new org.springframework.security.core.userdetails.User(
                user.getUserName(),
                user.getUserPassword(),
                getAuthorities(user.getUserRole())
        );
    }

    public boolean existsByUserName(String userName) {
        return userRepository.existsByUserName(userName);
    }

    public User findByUserId(Long userId) {
        Optional<User> user = userRepository.findById(userId);
        if (user.isPresent()) {
            return user.get();
        } else {
            return null;
        }
    }

    private Collection<? extends GrantedAuthority> getAuthorities(String role) {
        return Collections.singletonList(new SimpleGrantedAuthority(role));
    }

    public boolean updateUser(Long userId, String userName, String userPassword, String email, String fullName) {
        User existingUser = findByUserId(userId);

        String encodedPassword = (userPassword == null || userPassword.isEmpty()) ?
                existingUser.getUserPassword() : passwordEncoder.encode(userPassword);

        int updated = userRepository.updateUser(userId, userName, encodedPassword, email, fullName);
        return updated > 0;
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public void deleteUserById(Long id) {
        userRepository.deleteById(id);
    }

    public Student getStudentDetails(String username) {
        User user = userRepository.findByUserName(username);
        return studentRepository.findStudentByEmail(user.getUserEmail())
                .orElseThrow(() -> new EntityNotFoundException("Student not found for email: " + user.getUserEmail()));
    }
}

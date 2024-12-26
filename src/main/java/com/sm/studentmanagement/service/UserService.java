package com.sm.studentmanagement.service;

import java.util.Optional;
import com.sm.studentmanagement.entity.User;
import com.sm.studentmanagement.repository.UserRepository;
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
    private PasswordEncoder passwordEncoder;

    /// Lưu người dùng mới
    public void saveUser(User user) {
//        user.setUserPassword(passwordEncoder.encode(user.getUserPassword()));
        userRepository.save(user);
    }

    // Tìm người dùng theo tên người dùng
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
                getAuthorities(user.getUserRole()) // Role phải chính xác
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
        User existingUser = findByUserId(userId);  // Tìm người dùng từ cơ sở dữ liệu
        if (existingUser == null) {
            throw new IllegalArgumentException("Không tìm thấy người dùng với ID: " + userId);
        }

        // Mã hóa mật khẩu mới nếu có
        String encodedPassword = (userPassword == null || userPassword.isEmpty()) ?
                existingUser.getUserPassword() : passwordEncoder.encode(userPassword);

        int updated = userRepository.updateUser(userId, userName, encodedPassword, email, fullName);
        return updated > 0;  // Trả về true nế
    }
}

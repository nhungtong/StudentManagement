package com.sm.studentmanagement.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.ui.Model;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import com.sm.studentmanagement.entity.User;
import com.sm.studentmanagement.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    private PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @GetMapping("/register")
    public String showRegisterPage() {
        return "user/register";
    }

    @PostMapping("/register")
    public String registerUser(User user) {
        if (user.getUserPassword() == null || user.getUserPassword().isEmpty()) {
            return "user/register";
        }
        if (userService.existsByUserName(user.getUserName())) {
            return "redirect:/user/register?error=usernameExists";
        }
        user.setUserPassword(passwordEncoder.encode(user.getUserPassword()));
        userService.saveUser(user);
        return "redirect:/user/login";
    }

    @GetMapping("/login")
    public String showLoginPage() {
        return "user/login";
    }

    @PostMapping("/user/login")
    public String handleLogin(HttpServletRequest request, Authentication authentication, Model model) {
        if (authentication != null && authentication.isAuthenticated()) {
            return "redirect:/user/dashboard";
        }
        model.addAttribute("error", "Invalid username or password");
        return "redirect:/login";
    }

    @GetMapping("/dashboard")
    public String userDashboard(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            String userName = authentication.getName();
            User user = userService.findByUserName(userName);
            model.addAttribute("user", user);
            return "user/dashboard";
        } else {
            return "redirect:/login";
        }
    }

    @GetMapping("/update")
    public String showUpdateForm(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userName = authentication.getName();
        User user = userService.findByUserName(userName);
        model.addAttribute("user", user);
        return "user/update";
    }

    @PostMapping("/update")
    public String updateUser( @RequestParam("userId") Long userId,
                              @RequestParam("userName") String userName,
                              @RequestParam("userPassword") String userPassword,
                              @RequestParam("userEmail") String userEmail,
                              @RequestParam("userFullName") String userFullName) {
        if (userId == null) {
            throw new IllegalArgumentException("ID người dùng không hợp lệ");
        }
        boolean updated = userService.updateUser(userId, userName, userPassword, userEmail, userFullName);
        if (!updated) {
            throw new IllegalArgumentException("Không tìm thấy người dùng với ID: " + userId);
        }
        return "redirect:/user/dashboard";
    }
    @GetMapping("/logout")
    public String logout(HttpServletRequest request, HttpServletResponse response) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null) {
            new SecurityContextLogoutHandler().logout(request, response, authentication);
        }
        return "redirect:/user/login";
    }

    @GetMapping("/management")
    public String showUserList(Model model) {
        List<User> users = userService.getAllUsers();
        model.addAttribute("users", users);
        return "user/management";
    }

    @PostMapping("/delete/{id}")
    public String deleteUser(@PathVariable Long id) {
        userService.deleteUserById(id);
        return "redirect:/user/management";
    }
}

package com.sm.studentmanagement.repository;

import com.sm.studentmanagement.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UserRepository extends JpaRepository<User, Long> {
    @Query("SELECT u FROM User u WHERE u.userName = :username")
    User findByUserName(@Param("username")String userName);
    boolean existsByUserName(String userName);
    User findByUserId(Long userId);
    @Modifying
    @Query("UPDATE User u SET u.userName = :userName, u.userPassword = :userPassword,u.userEmail = :userEmail, u.userFullName = :userFullName WHERE u.userId = :userId")
    int updateUser(
            @Param("userId") Long userId,
            @Param("userName") String userName,
            @Param("userPassword") String userPassword,
            @Param("userEmail") String userEmail,
            @Param("userFullName") String userFullName);
}

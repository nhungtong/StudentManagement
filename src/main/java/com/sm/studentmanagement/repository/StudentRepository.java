package com.sm.studentmanagement.repository;

import com.sm.studentmanagement.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface StudentRepository extends JpaRepository<Student, Long> {
    List<Student> findByStudentCodeContainingOrStudentFullnameContaining(String studentCode, String studentFullname);
    Optional<Student> findByStudentEmail(String studentEmail);
}

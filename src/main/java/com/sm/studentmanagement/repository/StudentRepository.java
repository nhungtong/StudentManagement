package com.sm.studentmanagement.repository;

import com.sm.studentmanagement.entity.Student;
import com.sm.studentmanagement.entity.Class;
import jakarta.annotation.Nullable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface StudentRepository extends JpaRepository<Student, Long> {

    List<Student> findByStudentCodeContainingOrStudentFullnameContaining(String studentCode, String studentFullname);

    @Query("SELECT s FROM Student s JOIN Class c ON s.studentId = c.classId WHERE s.studentEmail = :studentEmail")
    Optional<Student> findStudentByEmail(@Param("studentEmail") String studentEmail);

    @Query("SELECT s FROM Student s WHERE s.studentClass.classId = :classId")
    List<Student> findStudentsByClassId(@Param("classId") Long classId);

    int countByStudentClass(Class studentClass);

    @Query("SELECT s FROM Student s JOIN s.enrollments e WHERE (CAST(:fromDate AS date) IS NULL OR e.enrollmentDate >= :fromDate) AND (CAST(:toDate AS date) IS NULL OR e.enrollmentDate <= :toDate)")
    List<Student> findStudentsByEnrollmentDate(
            @Param("fromDate") @Nullable LocalDate fromDate,
            @Param("toDate") @Nullable LocalDate toDate);

    @Modifying
    @Query("UPDATE Student s SET s.studentCode = :studentCode, s.studentFullname = :fullName, s.studentBirthdate = :dob, s.studentGender = :gender, s.studentPhone = :phone, s.studentEmail = :email, s.studentClass.classId = :classId WHERE s.studentId = :id")
    int updateStudent(
            @Param("id") Long id,
            @Param("studentCode") String studentCode,
            @Param("fullName") String fullName,
            @Param("dob") LocalDate dob,
            @Param("gender") String gender,
            @Param("phone") String phone,
            @Param("email") String email,
            @Param("classId") Long classId);
}

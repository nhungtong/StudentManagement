package com.sm.studentmanagement.service;

import com.sm.studentmanagement.entity.Student;
import com.sm.studentmanagement.entity.Class;
import com.sm.studentmanagement.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class StudentService {
    @Autowired
    private StudentRepository studentRepository;

    public List<Student> getAllStudents() {
        return studentRepository.findAll();
    }

    public Optional<Student> getStudentById(Long studentId) {
        return studentRepository.findById(studentId);
    }

    public List<Student> searchStudents(String studentCode, String studentFullname) {
        return studentRepository.findByStudentCodeContainingOrStudentFullnameContaining(studentCode, studentFullname);
    }

    public Student saveStudent(Student student) {
        return studentRepository.save(student);
    }

    public void deleteStudent(Long studentId) {
        studentRepository.deleteById(studentId);
    }

    public List<Student> getStudentsByClassId(Long classId) {
        return studentRepository.findStudentsByClassId(classId);
    }

    public int countStudentsInClass(Class studentClass) {
        return studentRepository.countByStudentClass(studentClass);
    }

    public List<Student> filterStudentsByEnrollmentDate(LocalDate fromDate, LocalDate toDate) {
        return studentRepository.findStudentsByEnrollmentDate(fromDate, toDate);
    }
}

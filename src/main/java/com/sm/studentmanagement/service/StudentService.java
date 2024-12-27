package com.sm.studentmanagement.service;

import com.sm.studentmanagement.entity.Student;
import com.sm.studentmanagement.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    public void updateStudent(Student student) {
        Student existingStudent = studentRepository.findById(student.getStudentId()).orElse(null);
        if (existingStudent != null) {

            existingStudent.setStudentCode(student.getStudentCode());
            existingStudent.setStudentFullname(student.getStudentFullname());
            existingStudent.setStudentBirthdate(student.getStudentBirthdate());
            existingStudent.setStudentGender(student.getStudentGender());
            existingStudent.setStudentPhone(student.getStudentPhone());
            existingStudent.setStudentEmail(student.getStudentEmail());
            existingStudent.setStudentClass(student.getStudentClass());

            studentRepository.save(existingStudent);
        }
    }
    public void deleteStudent(Long studentId) {
        studentRepository.deleteById(studentId);
    }
    public Optional<Student> getStudentByEmail(String studentEmail) {
        return studentRepository.findByStudentEmail(studentEmail);
    }
}

package com.sm.studentmanagement.service;

import com.sm.studentmanagement.entity.Enrollment;
import com.sm.studentmanagement.entity.Student;
import com.sm.studentmanagement.entity.Class;
import com.sm.studentmanagement.repository.StudentRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
public class StudentServiceTest {
    @Mock
    private StudentRepository studentRepository;

    @InjectMocks
    private StudentService studentService;

    private List<Student> createTestStudents() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        Student student1 = new Student();
        student1.setStudentId(1L);
        student1.setStudentCode("S123");
        student1.setStudentFullname("John Doe");
        LocalDate birthdate = LocalDate.parse("12/03/2003", formatter);
        student1.setStudentBirthdate(birthdate);
        student1.setStudentGender("Male");
        student1.setStudentPhone("0123456789");
        student1.setStudentEmail("john.doe@gmail.com");

        Student student2 = new Student();
        student2.setStudentId(2L);
        student2.setStudentCode("S456");
        student2.setStudentFullname("Jane Smith");
        LocalDate birthdate1 = LocalDate.parse("09/03/2003", formatter);
        student2.setStudentBirthdate(birthdate1);
        student2.setStudentGender("Female");
        student2.setStudentPhone("0123456789");
        student2.setStudentEmail("jane.smith@gmail.com");

        return Arrays.asList(student1, student2);
    }
    @Test
    void testGetAllStudents() {
        List<Student> students = createTestStudents();
        when(studentRepository.findAll()).thenReturn(students);
        List<Student> result = studentService.getAllStudents();
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("John Doe", students.get(0).getStudentFullname());
        assertEquals("jane.smith@gmail.com", students.get(1).getStudentEmail());
        verify(studentRepository, times(1)).findAll();
    }
    private Student createTestStudent()
    {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        Student student = new Student();
        student.setStudentId(1L);
        student.setStudentCode("S123");
        student.setStudentFullname("John Doe");
        LocalDate birthdate = LocalDate.parse("12/03/2003", formatter);
        student.setStudentBirthdate(birthdate);
        student.setStudentGender("Male");
        student.setStudentPhone("0123456789");
        student.setStudentEmail("john.doe@gmail.com");
        return student;
    }
    @Test
    void testGetStudentById() {
        Student student = createTestStudent();
        when(studentRepository.findById(1L)).thenReturn(Optional.of(student));
        Optional<Student> result = studentService.getStudentById(1L);

        assertTrue(result.isPresent(), "Student should be present");
        assertEquals("John Doe", result.get().getStudentFullname(), "Student name should match");
        assertEquals(1L, result.get().getStudentId(), "Student ID should be 1");

        verify(studentRepository, times(1)).findById(1L);
    }
    @Test
    void testSearchStudents() {
        List<Student> expectedStudents = createTestStudents();

        when(studentRepository.findByStudentCodeContainingOrStudentFullnameContaining("S123", "John Doe"))
                .thenReturn(expectedStudents);

        List<Student> result = studentService.searchStudents("S123", "John Doe");

        assertNotNull(result, "Result should not be null");
        assertEquals(2, result.size(), "There should be 2 students returned");
        assertEquals("John Doe", result.get(0).getStudentFullname(), "First student's name should match");

        verify(studentRepository, times(1)).findByStudentCodeContainingOrStudentFullnameContaining("S123", "John Doe");
    }
    @Test
    void testSaveStudent() {
        Student student = createTestStudent();

        when(studentRepository.save(student)).thenReturn(student);

        Student result = studentService.saveStudent(student);

        assertNotNull(result, "Result should not be null");
        assertEquals(student.getStudentId(), result.getStudentId(), "Student ID should match");
        assertEquals(student.getStudentFullname(), result.getStudentFullname(), "Student Fullname should match");

        verify(studentRepository, times(1)).save(student);
    }
    @Test
    void testUpdateStudent() {

        Long studentId = 1L;
        String studentCode = "S321";
        String fullName = "John Doen";
        LocalDate dob = LocalDate.of(2003, 3, 12);
        String gender = "Male";
        String phone = "0123456789";
        String email = "john.doen@gmail.com";
        Long classId = 101L;

        when(studentRepository.updateStudent(studentId, studentCode, fullName, dob, gender, phone, email, classId))
                .thenReturn(1);

        int result = studentService.updateStudent(studentId, studentCode, fullName, dob, gender, phone, email, classId);

        assertEquals(1, result, "The update method should return 1 for a successful update");

        verify(studentRepository, times(1)).updateStudent(studentId, studentCode, fullName, dob, gender, phone, email, classId);
    }
    @Test
    void testDeleteStudent() {

        Long studentId = 1L;
        doNothing().when(studentRepository).deleteById(studentId);
        studentService.deleteStudent(studentId);
        verify(studentRepository, times(1)).deleteById(studentId);
    }
    @Test
    void testGetStudentsByClassId() {

        Long classId = 1L;
        List<Student> students = createTestStudents();
        when(studentRepository.findStudentsByClassId(classId)).thenReturn(students);
        List<Student> result = studentService.getStudentsByClassId(classId);

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("John Doe", result.get(0).getStudentFullname());
        assertEquals("Jane Smith", result.get(1).getStudentFullname());

        verify(studentRepository, times(1)).findStudentsByClassId(classId);
    }
    @Test
    void testCountStudentsInClass() {

        Class studentClass = new Class();
        studentClass.setClassId(1L);
        studentClass.setClassName("Class A");

        when(studentRepository.countByStudentClass(studentClass)).thenReturn(3);
        int result = studentService.countStudentsInClass(studentClass);

        assertEquals(3, result);

        verify(studentRepository, times(1)).countByStudentClass(studentClass);
    }
    private List<Student> createTestStudents(LocalDate fromDate, LocalDate toDate) {
        Student student1 = new Student();
        student1.setStudentFullname("John Doe");
        student1.setStudentEmail("john.doe@example.com");

        Enrollment enrollment1 = new Enrollment();
        enrollment1.setEnrollmentDate(LocalDate.of(2023, 3, 1));
        student1.setEnrollments(Arrays.asList(enrollment1));

        Student student2 = new Student();
        student2.setStudentFullname("Jane Smith");
        student2.setStudentEmail("jane.smith@example.com");

        Enrollment enrollment2 = new Enrollment();
        enrollment2.setEnrollmentDate(LocalDate.of(2023, 4, 15));
        student2.setEnrollments(Arrays.asList(enrollment2));

        Student student3 = new Student();
        student3.setStudentFullname("Alice Johnson");
        student3.setStudentEmail("alice.johnson@example.com");

        Enrollment enrollment3 = new Enrollment();
        enrollment3.setEnrollmentDate(LocalDate.of(2023, 6, 10));
        student3.setEnrollments(Arrays.asList(enrollment3));

        return Arrays.asList(student1, student2, student3);
    }
    @Test
    void testGetEnrollments() {
        LocalDate fromDate = LocalDate.of(2023, 1, 1);
        LocalDate toDate = LocalDate.of(2023, 12, 31);

        List<Student> students = createTestStudents(fromDate, toDate);
        when(studentRepository.findStudentsByEnrollmentDate(fromDate, toDate))
                .thenReturn(students);
        List<Student> result = studentService.filterStudentsByEnrollmentDate(fromDate, toDate);

        assertThat(result).hasSize(3);
        assertThat(result.get(0).getStudentFullname()).isEqualTo("John Doe");
        assertThat(result.get(1).getStudentFullname()).isEqualTo("Jane Smith");
        assertThat(result.get(2).getStudentFullname()).isEqualTo("Alice Johnson");
    }
}

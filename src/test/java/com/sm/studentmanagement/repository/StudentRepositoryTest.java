package com.sm.studentmanagement.repository;

import com.sm.studentmanagement.entity.Enrollment;
import com.sm.studentmanagement.entity.Student;
import com.sm.studentmanagement.entity.Class;
import com.sm.studentmanagement.service.StudentService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class StudentRepositoryTest {

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
        student2.setStudentGender("Male");
        student2.setStudentPhone("0123456789");
        student2.setStudentEmail("jane.smith@gmail.com");

        Student student3 = new Student();
        student3.setStudentId(3L);
        student3.setStudentCode("A789");
        student3.setStudentFullname("Jack Brown");
        LocalDate birthdate2 = LocalDate.parse("09/08/2003", formatter);
        student3.setStudentBirthdate(birthdate2);
        student3.setStudentGender("Male");
        student3.setStudentPhone("0123456789");
        student3.setStudentEmail("jack.brown@gmail.com");

        return Arrays.asList(student1, student2, student3);
    }

    @Test
    void testFindByStudentCodeContainingOrStudentFullnameContaining() {
        MockitoAnnotations.openMocks(this);
        List<Student> sampleStudents = createTestStudents();
        when(studentRepository.findByStudentCodeContainingOrStudentFullnameContaining("S", "Jane"))
                .thenReturn(sampleStudents.subList(0, 2));
        List<Student> result = studentRepository.findByStudentCodeContainingOrStudentFullnameContaining("S", "Jane");

        assertThat(result).hasSize(2);
        assertThat(result).extracting("studentCode").containsExactlyInAnyOrder("S123", "S456");
    }
    private Student createTestStudent() {
        Student student = new Student();
        student.setStudentId(1L);
        student.setStudentCode("S001");
        student.setStudentFullname("John Doe");
        student.setStudentBirthdate(LocalDate.of(2000, 1, 1));
        student.setStudentGender("Male");
        student.setStudentPhone("123456789");
        student.setStudentEmail("john.doe@example.com");
        student.setStudentClass(new Class());
        student.getStudentClass().setClassId(101L);
        return student;
    }
    @Test
    void testFindStudentByEmail() {
        Student testStudent = createTestStudent();
        when(studentRepository.findStudentByEmail("john.doe@example.com"))
                .thenReturn(Optional.of(testStudent));

        Optional<Student> result = studentRepository.findStudentByEmail("john.doe@example.com");

        assertThat(result).isPresent();
        assertThat(result.get().getStudentEmail()).isEqualTo("john.doe@example.com");
    }
    private List<Student> createTestStudents(Long classId) {
        Class studentClass = new Class();
        studentClass.setClassId(classId);
        studentClass.setClassName("Class " + classId);

        Student student1 = new Student();
        student1.setStudentId(1L);
        student1.setStudentFullname("John Doe");
        student1.setStudentClass(studentClass);

        Student student2 = new Student();
        student2.setStudentId(2L);
        student2.setStudentFullname("Jane Smith");
        student2.setStudentClass(studentClass);

        return Arrays.asList(student1, student2);
    }
    @Test
    void testFindStudentsByClassId() {
        Long classId = 101L;
        List<Student> students = createTestStudents(classId);
        when(studentRepository.findStudentsByClassId(classId)).thenReturn(students);
        List<Student> result = studentRepository.findStudentsByClassId(classId);

        assertThat(result).hasSize(2);
        assertThat(result.get(0).getStudentFullname()).isEqualTo("John Doe");
        assertThat(result.get(1).getStudentFullname()).isEqualTo("Jane Smith");
    }
    private Class createTestClass(Long classId) {
        Class studentClass = new Class();
        studentClass.setClassId(classId);
        studentClass.setClassName("Class " + classId);
        return studentClass;
    }
    @Test
    void testCountByStudentClass() {
        Class studentClass = createTestClass(101L);
        when(studentRepository.countByStudentClass(studentClass)).thenReturn(3);
        int result = studentRepository.countByStudentClass(studentClass);
        assertThat(result).isEqualTo(3);
    }
    private List<Student> createTestStudents(LocalDate fromDate, LocalDate toDate) {
        Enrollment enrollment1 = new Enrollment();
        enrollment1.setEnrollmentDate(LocalDate.of(2023, 1, 10));

        Enrollment enrollment2 = new Enrollment();
        enrollment2.setEnrollmentDate(LocalDate.of(2023, 5, 15));

        Enrollment enrollment3 = new Enrollment();
        enrollment3.setEnrollmentDate(LocalDate.of(2023, 10, 20));

        Student student1 = new Student();
        student1.setStudentId(1L);
        student1.setStudentFullname("John Doe");
        student1.setEnrollments(Arrays.asList(enrollment1));

        Student student2 = new Student();
        student2.setStudentId(2L);
        student2.setStudentFullname("Jane Smith");
        student2.setEnrollments(Arrays.asList(enrollment2));

        Student student3 = new Student();
        student3.setStudentId(3L);
        student3.setStudentFullname("Alice Johnson");
        student3.setEnrollments(Arrays.asList(enrollment3));

        return Arrays.asList(student1, student2, student3);
    }
    @Test
    void testFindStudentsByEnrollmentDate() {
        LocalDate fromDate = LocalDate.of(2023, 1, 1);
        LocalDate toDate = LocalDate.of(2023, 12, 31);

        List<Student> students = createTestStudents(fromDate, toDate);

        when(studentRepository.findStudentsByEnrollmentDate(fromDate, toDate))
                .thenReturn(students);

        List<Student> result = studentRepository.findStudentsByEnrollmentDate(fromDate, toDate);

        assertThat(result).hasSize(3);
        assertThat(result.get(0).getStudentFullname()).isEqualTo("John Doe");
        assertThat(result.get(1).getStudentFullname()).isEqualTo("Jane Smith");
        assertThat(result.get(2).getStudentFullname()).isEqualTo("Alice Johnson");
    }
    @Test
    void testUpdateStudent() {

        Long studentId = 1L;
        String studentCode = "S001_UPDATED";
        String fullName = "John Doe Updated";
        LocalDate dob = LocalDate.of(2000, 1, 2);
        String gender = "Male";
        String phone = "987654321";
        String email = "john.doe.updated@example.com";
        Long classId = 102L;

        when(studentRepository.updateStudent(
                studentId, studentCode, fullName, dob, gender, phone, email, classId))
                .thenReturn(1);

        int updatedCount = studentRepository.updateStudent(
                studentId, studentCode, fullName, dob, gender, phone, email, classId);

        assertEquals(1, updatedCount);

        verify(studentRepository).updateStudent(
                studentId, studentCode, fullName, dob, gender, phone, email, classId);
    }
}

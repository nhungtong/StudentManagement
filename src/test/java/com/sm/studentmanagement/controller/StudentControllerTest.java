package com.sm.studentmanagement.controller;

import com.sm.studentmanagement.entity.Student;
import com.sm.studentmanagement.service.StudentService;
import com.sm.studentmanagement.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.security.Principal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class StudentControllerTest {

    private MockMvc mockMvc;

    @Mock
    private UserService userService;

    @Mock
    private StudentService studentService;

    @InjectMocks
    private StudentController studentController;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(studentController).build();
    }

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
    void testShowStudentList() throws Exception {
        List<Student> mockStudents = createTestStudents();

        when(studentService.getAllStudents()).thenReturn(mockStudents);

        mockMvc.perform(get("/students/list"))
                .andExpect(status().isOk())
                .andExpect(view().name("students/list"))
                .andExpect(model().attributeExists("students"))
                .andExpect(model().attribute("students", mockStudents));

        verify(studentService, times(1)).getAllStudents();
    }
    @Test
    void testShowAddStudentForm() throws Exception {
        mockMvc.perform(get("/students/add"))
                .andExpect(status().isOk())
                .andExpect(view().name("students/addForm"))
                .andExpect(model().attributeExists("student"))
                .andReturn();
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
    void testAddStudent() throws Exception {
        Student student = createTestStudent();

        mockMvc.perform(post("/students/add")
                        .flashAttr("student", student))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/students/list"))
                .andExpect(flash().attribute("message", "Student added successfully!"));

        verify(studentService, times(1)).saveStudent(student);
    }
    @Test
    void testShowEditStudentForm() throws Exception {
        Long studentId = 1L;
        Student student = createTestStudent();

        when(studentService.getStudentById(studentId)).thenReturn(Optional.of(student));

        mockMvc.perform(get("/students/edit").param("id", String.valueOf(studentId)))
                .andExpect(status().isOk())
                .andExpect(view().name("students/updateForm"))
                .andExpect(model().attribute("student", student));

        verify(studentService, times(1)).getStudentById(studentId);
    }
    @Test
    void testUpdateStudent() throws Exception {
        Long studentId = 1L;
        String updatedStudentCode = "S1234";
        String updatedFullName = "Johnathan Doe";
        LocalDate updatedDob = LocalDate.parse("12/03/2003", DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        String updatedGender = "Male";
        String updatedPhone = "0123456789";
        String updatedEmail = "johnathan.doe@gmail.com";
        Long updatedClassId = 2L;

        when(studentService.updateStudent(eq(studentId), eq(updatedStudentCode), eq(updatedFullName),
                eq(updatedDob), eq(updatedGender), eq(updatedPhone), eq(updatedEmail), eq(updatedClassId)))
                .thenReturn(1);

        mockMvc.perform(post("/students/edit")
                        .param("id", String.valueOf(studentId))
                        .param("studentCode", updatedStudentCode)
                        .param("fullName", updatedFullName)
                        .param("dob", updatedDob.toString())
                        .param("gender", updatedGender)
                        .param("phone", updatedPhone)
                        .param("email", updatedEmail)
                        .param("classId", String.valueOf(updatedClassId)))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/students/list"))
                .andExpect(flash().attribute("message", "Student updated successfully!"));

        verify(studentService, times(1)).updateStudent(eq(studentId), eq(updatedStudentCode), eq(updatedFullName),
                eq(updatedDob), eq(updatedGender), eq(updatedPhone), eq(updatedEmail), eq(updatedClassId));
    }
    @Test
    void testDeleteStudent() throws Exception {
        Long studentId = 1L;

        doNothing().when(studentService).deleteStudent(studentId);

        mockMvc.perform(get("/students/delete/{id}", studentId))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/students/list"))
                .andExpect(flash().attribute("message", "Student deleted successfully!"));

        verify(studentService, times(1)).deleteStudent(studentId);
    }
    @Test
    void testSearchStudents() throws Exception {
        String query = "John";
        List<Student> students = createTestStudents();

        when(studentService.searchStudents(query, query)).thenReturn(students);

        mockMvc.perform(get("/students/search").param("query", query))
                .andExpect(status().isOk())
                .andExpect(view().name("students/list"))
                .andExpect(model().attribute("students", students));

        verify(studentService, times(1)).searchStudents(query, query);
    }
    @Test
    void testShowStudentDetails() throws Exception {

        String username = "john.doe";
        Student student = createTestStudent();

        Principal principal = mock(Principal.class);
        when(principal.getName()).thenReturn(username);

        when(userService.getStudentDetails(username)).thenReturn(student);

        mockMvc.perform(get("/students/viewProfile").principal(principal))
                .andExpect(status().isOk())
                .andExpect(view().name("students/viewProfile"))
                .andExpect(model().attribute("student", student));

        verify(userService, times(1)).getStudentDetails(username);
    }
}

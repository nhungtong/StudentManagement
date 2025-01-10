package com.sm.studentmanagement.controller;

import com.sm.studentmanagement.entity.Class;
import com.sm.studentmanagement.entity.Student;
import com.sm.studentmanagement.service.ClassService;
import com.sm.studentmanagement.service.StudentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
public class ChartControllerTest {
    private MockMvc mockMvc;

    @Mock
    private ClassService classService;

    @Mock
    private StudentService studentService;

    @InjectMocks
    private ChartController chartController;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(chartController).build();
    }
    private List<Class> createTessClass()
    {
        Class class1 = new Class();
        class1.setClassId(1L);
        class1.setClassName("Test 1");
        class1.setClassDescription("test1");

        Class class2 = new Class();
        class2.setClassId(2L);
        class2.setClassName("Test 2");
        class2.setClassDescription("test2");

        return Arrays.asList(class1, class2);
    }
    @Test
    void testViewOverview() throws Exception {
        mockMvc.perform(get("/charts/overview"))
                .andExpect(status().isOk())
                .andExpect(view().name("charts/overview"));
    }
    @Test
    void testViewStudentStatistics() throws Exception {

        List<Class> classes = createTessClass();

        Map<String, Integer> classStudentCounts = new HashMap<>();
        classStudentCounts.put("Test 1", 20);
        classStudentCounts.put("Test 2", 25);

        when(classService.getAllClasses()).thenReturn(classes);
        when(studentService.countStudentsInClass(any(Class.class))).thenReturn(20, 25);  // Trả về số lượng sinh viên giả

        mockMvc.perform(get("/charts/statistics"))
                .andExpect(status().isOk())
                .andExpect(view().name("charts/statistics"))
                .andExpect(model().attributeExists("classStudentCounts"))
                .andExpect(model().attribute("classStudentCounts", classStudentCounts));

        verify(classService, times(1)).getAllClasses();
        verify(studentService, times(2)).countStudentsInClass(any(Class.class));
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
    void testViewStudentReport() throws Exception {

        LocalDate fromDate = LocalDate.of(2023, 1, 1);
        LocalDate toDate = LocalDate.of(2023, 12, 31);
        List<Student> students = createTestStudents();

        when(studentService.filterStudentsByEnrollmentDate(fromDate, toDate)).thenReturn(students);

        mockMvc.perform(get("/charts/reports")
                        .param("fromDate", "2023-01-01")
                        .param("toDate", "2023-12-31"))
                .andExpect(status().isOk())
                .andExpect(view().name("charts/reports"))
                .andExpect(model().attributeExists("students"))
                .andExpect(model().attribute("students", students))
                .andExpect(model().attribute("fromDate", fromDate))
                .andExpect(model().attribute("toDate", toDate));

        verify(studentService, times(1)).filterStudentsByEnrollmentDate(fromDate, toDate);
    }

}

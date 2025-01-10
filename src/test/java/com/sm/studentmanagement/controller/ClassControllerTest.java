package com.sm.studentmanagement.controller;

import com.sm.studentmanagement.entity.Class;
import com.sm.studentmanagement.entity.Student;
import com.sm.studentmanagement.service.ClassService;
import com.sm.studentmanagement.service.StudentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
public class ClassControllerTest {

    private MockMvc mockMvc;

    @Mock
    private StudentService studentService;

    @Mock
    private ClassService classService;

    @InjectMocks
    private ClassController classController;

    @Captor
    private ArgumentCaptor<Class> classCaptor;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(classController).build();
    }

    private List<Class> createTessClass()
    {
        Class class1 = new Class();
        class1.setClassId(1L);
        class1.setClassName("Test 1");
        class1.setClassDescription("test1");

        com.sm.studentmanagement.entity.Class class2 = new Class();
        class2.setClassId(2L);
        class2.setClassName("Test 2");
        class2.setClassDescription("test2");

        return Arrays.asList(class1, class2);
    }
    private Class createTestClass()
    {
        Class class1 = new Class();
        class1.setClassId(1L);
        class1.setClassName("Test 1");
        class1.setClassDescription("test1");
        return class1;
    }
    @Test
    void testShowClassesList() throws Exception {

        List<Class> classes = createTessClass();
        when(classService.getAllClasses()).thenReturn(classes);

        mockMvc.perform(get("/classes/list"))
                .andExpect(status().isOk())
                .andExpect(view().name("classes/list"))
                .andExpect(model().attribute("classes", classes));

        verify(classService, times(1)).getAllClasses();
    }
    @Test
    void testShowAddClassForm() throws Exception {

        mockMvc.perform(get("/classes/add"))
                .andExpect(status().isOk())
                .andExpect(view().name("classes/addForm"))
                .andExpect(model().attributeExists("classes"));
    }

    @Test
    void testAddClass() throws Exception {
        Class testClass = createTestClass();

        mockMvc.perform(post("/classes/add")
                        .flashAttr("testClass", testClass))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/classes/list"))
                .andExpect(flash().attribute("message", "Class added successfully!"));

        verify(classService, times(1)).saveClass(classCaptor.capture());
    }
    @Test
    void testShowEditClassForm() throws Exception {
        Long classId = 1L;
        Class classObj = createTestClass();

        when(classService.getClassById(classId)).thenReturn(Optional.of(classObj));

        mockMvc.perform(get("/classes/edit/{id}", classId))
                .andExpect(status().isOk())
                .andExpect(view().name("classes/updateForm"))
                .andExpect(model().attribute("classes", classObj));

        verify(classService, times(1)).getClassById(classId);
    }
    @Test
    void testUpdateStudent() throws Exception {

        Class testClass = createTestClass();
        when(classService.saveClass(any(Class.class))).thenReturn(testClass);

        mockMvc.perform(post("/classes/edit/{id}", 1L)
                        .flashAttr("classObj", testClass))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/classes/list"))
                .andExpect(flash().attribute("message", "Class updated successfully!"));

        verify(classService, times(1)).saveClass(classCaptor.capture());
    }
    @Test
    void testDeleteClass() throws Exception {
        Long classId = 1L;

        doNothing().when(classService).deleteClass(classId);

        mockMvc.perform(get("/classes/delete/{id}", classId))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/classes/list"))
                .andExpect(flash().attribute("message", "Class deleted successfully!"));

        verify(classService, times(1)).deleteClass(classId);
    }
    @Test
    void testSearchClass() throws Exception {

        Long classId = 1L;
        String className = "Test Class";
        List<Class> mockClasses = createTessClass();

        when(classService.searchClass(classId, className)).thenReturn(mockClasses);

        mockMvc.perform(get("/classes/search")
                        .param("classId", classId.toString())
                        .param("className", className))
                .andExpect(status().isOk())
                .andExpect(view().name("classes/list"))
                .andExpect(model().attributeExists("classes"))
                .andExpect(model().attribute("classes", mockClasses));

        verify(classService, times(1)).searchClass(classId, className);
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
    void testViewStudentsInClass() throws Exception {

        Long classId = 1L;
        List<Student> students = createTestStudents();
        Class classInfo = createTestClass();

        when(studentService.getStudentsByClassId(classId)).thenReturn(students);
        when(classService.getClassById(classId)).thenReturn(Optional.of(classInfo));

        mockMvc.perform(get("/classes/{id}/students", classId))
                .andExpect(status().isOk())
                .andExpect(view().name("classes/viewStudents"))
                .andExpect(model().attributeExists("students"))
                .andExpect(model().attribute("students", students))
                .andExpect(model().attributeExists("className"))
                .andExpect(model().attribute("className", "Test 1"));

        verify(studentService, times(1)).getStudentsByClassId(classId);
        verify(classService, times(1)).getClassById(classId);
    }
}

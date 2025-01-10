package com.sm.studentmanagement.service;

import com.sm.studentmanagement.repository.ClassRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.Mock;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import com.sm.studentmanagement.entity.Class;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ClassServiceTest {

    @Mock
    private ClassRepository classRepository;

    @InjectMocks
    private ClassService classService;

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
    void testGetAllClasses() {
        List<Class> classes = createTessClass();
        when(classRepository.findAll()).thenReturn(classes);
        List<Class> result = classService.getAllClasses();
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(1L, classes.get(0).getClassId());
        assertEquals("Test 2", classes.get(1).getClassName());
        verify(classRepository, times(1)).findAll();
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
    void testGetClassById() {
        Class testClass = createTestClass();
        when(classRepository.findById(1L)).thenReturn(Optional.of(testClass));
        Optional<Class> result = classService.getClassById(1L);
        assertTrue(result.isPresent(), "Class should be present");
        assertEquals("Test 1", result.get().getClassName(), "Class name should match");
        assertEquals(1L, result.get().getClassId(), "Class ID should be 1");

        verify(classRepository, times(1)).findById(1L);
    }
   @Test
    void testSaveClass()
   {
       Class testClass = createTestClass();
       when(classRepository.save(any(Class.class))).thenReturn(testClass);
       Class result = classService.saveClass(testClass);

       assertNotNull(result);
       assertEquals(testClass.getClassId(), result.getClassId());
       assertEquals(testClass.getClassName(), result.getClassName());

       verify(classRepository, times(1)).save(testClass);
   }
   @Test
    void testDeleteClass()
   {
       Long classId = 1L;
       classService.deleteClass(classId);
       verify(classRepository, times(1)).deleteById(classId);
   }
    @Test
    void testSearchClass() {
        List<Class> testClass = createTessClass();
        Long classId = 1L;
        String className = "Test 1";
        when(classRepository.findByClassIdOrClassNameContaining(classId, className)).thenReturn(testClass);

        List<Class> result = classService.searchClass(classId, className);

        assertThat(result).isNotNull();
        assertThat(result).hasSize(2);
        assertThat(result.get(0).getClassId()).isEqualTo(1L);
        assertThat(result.get(0).getClassName()).isEqualTo("Test 1");

        verify(classRepository, times(1)).findByClassIdOrClassNameContaining(classId, className);}
}

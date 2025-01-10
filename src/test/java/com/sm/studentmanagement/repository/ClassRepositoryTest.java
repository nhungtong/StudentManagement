package com.sm.studentmanagement.repository;

import com.sm.studentmanagement.service.ClassService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import com.sm.studentmanagement.entity.Class;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ClassRepositoryTest {
    @Mock
    private ClassRepository classRepository;

    @InjectMocks
    private ClassService classService;
    private List<Class> createTestClasses() {
        Class class1 = new Class();
        class1.setClassId(1L);
        class1.setClassName("Math 101");

        Class class2 = new Class();
        class2.setClassId(2L);
        class2.setClassName("Science 102");

        return Arrays.asList(class1, class2);
    }

    @Test
    void testFindByClassIdOrClassNameContaining_ClassId() {
        Long classId = 1L;
        String className = null;

        List<Class> classes = createTestClasses();
        when(classRepository.findByClassIdOrClassNameContaining(classId, className))
                .thenReturn(classes);

        List<Class> foundClasses = classRepository.findByClassIdOrClassNameContaining(classId, className);

        assertEquals(2, foundClasses.size());
        assertTrue(foundClasses.stream().anyMatch(c -> c.getClassId().equals(classId)));

        verify(classRepository).findByClassIdOrClassNameContaining(classId, className);
    }
    @Test
    void testFindByClassIdOrClassNameContaining_ClassName() {
        Long classId = null;
        String className = "Math";

        List<Class> classes = createTestClasses();

        when(classRepository.findByClassIdOrClassNameContaining(classId, className))
                .thenReturn(classes.stream().filter(c -> c.getClassName().contains(className)).collect(Collectors.toList()));

        List<Class> foundClasses = classRepository.findByClassIdOrClassNameContaining(classId, className);

        assertEquals(1, foundClasses.size());
        assertTrue(foundClasses.stream().anyMatch(c -> c.getClassName().contains(className)),
                "Expected class name to contain 'Math'");

        verify(classRepository).findByClassIdOrClassNameContaining(classId, className);
    }
}

package com.sm.studentmanagement.service;

import com.sm.studentmanagement.entity.Class;
import com.sm.studentmanagement.repository.ClassRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class ClassService {

    @Autowired
    private ClassRepository classRepository;

    public List<Class> getAllClasses() {
        return classRepository.findAll();
    }
    public Optional<Class> getClassById(Long classId) {
        return classRepository.findById(classId);
    }
    public Class saveClass(Class classObj) {
        return classRepository.save(classObj);
    }
    public void deleteClass(Long classId) {
        classRepository.deleteById(classId);
    }
    public List<Class> searchClass(Long classId, String className) {
        return classRepository.findByClassIdOrClassNameContaining(classId, className);
    }
}

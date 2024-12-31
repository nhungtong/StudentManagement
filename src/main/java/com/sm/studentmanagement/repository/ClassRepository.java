package com.sm.studentmanagement.repository;

import com.sm.studentmanagement.entity.Class;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ClassRepository extends JpaRepository<Class,Long> {
    List<Class> findByClassIdOrClassNameContaining(Long classId, String className);
}

package com.sm.studentmanagement.entity;
import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
@Table(name = "enrollment")
public class Enrollment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long enrollmentId;

    @ManyToOne
    @JoinColumn(name = "student_id", referencedColumnName = "id")
    private Student student;

    @ManyToOne
    @JoinColumn(name = "class_id", referencedColumnName = "id")
    private Class studentClass;

    @Column(name = "enrollment_date", nullable = false)
    private LocalDate enrollmentDate;
}

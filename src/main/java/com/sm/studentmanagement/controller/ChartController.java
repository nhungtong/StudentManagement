package com.sm.studentmanagement.controller;

import com.sm.studentmanagement.entity.Class;
import com.sm.studentmanagement.entity.Student;
import com.sm.studentmanagement.service.ClassService;
import com.sm.studentmanagement.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/charts")
public class ChartController {
    @Autowired
    private ClassService classService;

    @Autowired
    private StudentService studentService;

    @GetMapping("/overview")
    public String viewOverview(Model model) {
        return "charts/overview";
    }

    @GetMapping("/statistics")
    public String viewStudentStatistics(Model model) {
        List<Class> classes = classService.getAllClasses();
        Map<String, Integer> classStudentCounts = new HashMap<>();
        for (Class clazz : classes) {
            int studentCount = studentService.countStudentsInClass(clazz);
            classStudentCounts.put(clazz.getClassName(), studentCount);
        }
        if (classStudentCounts.isEmpty()) {
            model.addAttribute("message", "No data available.");
        }
        model.addAttribute("classStudentCounts", classStudentCounts);
        return "charts/statistics";
    }

    @GetMapping("/reports")
    public String viewStudentReport(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fromDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate toDate,
            Model model) {

        List<Student> students = studentService.filterStudentsByEnrollmentDate(fromDate, toDate);
        model.addAttribute("students", students);
        model.addAttribute("fromDate", fromDate);
        model.addAttribute("toDate", toDate);

        return "charts/reports";
    }
}

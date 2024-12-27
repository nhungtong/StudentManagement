package com.sm.studentmanagement.controller;

import com.sm.studentmanagement.entity.Student;
import com.sm.studentmanagement.entity.User;
import com.sm.studentmanagement.service.StudentService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/students")
public class StudentController {
    @Autowired
    private StudentService studentService;

    @GetMapping("/list")
    public String showStudentList(Model model) {
        model.addAttribute("students", studentService.getAllStudents());
        return "students/list";
    }

    @GetMapping("/add")
    public String showAddStudentForm(Model model) {
        model.addAttribute("student", new Student());
        return "students/addForm";
    }
    @PostMapping("/add")
    public String addStudent(@Valid Student student, BindingResult result) {
        if (result.hasErrors()) {
            return "students/addForm";
        }
        studentService.saveStudent(student);
        return "redirect:/students/list";
    }

    @GetMapping("/edit/{id}")
    public String showEditStudentForm(@PathVariable("id") Long id, Model model) {
        Optional<Student> student = studentService.getStudentById(id);
        if (student.isPresent()) {
            model.addAttribute("student", student.get());
            return "students/updateForm";
        }
        return "redirect:/students/list";
    }

    @PostMapping("/edit/{id}")
    public String updateStudent(@PathVariable("id") Long id, Student student) {
        student.setStudentId(id);
        studentService.saveStudent(student);
        return "redirect:/students/list";
    }

    @GetMapping("/delete/{id}")
    public String deleteStudent(@PathVariable("id") Long id) {
        studentService.deleteStudent(id);
        return "redirect:/students/list";
    }

    @GetMapping("/search")
    public String searchStudents(@RequestParam("query") String query, Model model) {
        List<Student> students = studentService.searchStudents(query, query);
        model.addAttribute("students", students);
        return "students/list";
    }

    @GetMapping("/students/viewProfile")
    public String viewStudentProfile(Model model, @AuthenticationPrincipal User user) {

        Optional<Student> student = studentService.getStudentByEmail(user.getUserEmail());
        if (student.isPresent()) {
            model.addAttribute("student", student);
            return "students/viewProfile";
        }
        return "redirect:/user/dashboard";
    }
}

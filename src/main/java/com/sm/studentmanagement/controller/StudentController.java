package com.sm.studentmanagement.controller;

import com.sm.studentmanagement.entity.Student;
import com.sm.studentmanagement.service.StudentService;
import com.sm.studentmanagement.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/students")
public class StudentController {
    @Autowired
    private StudentService studentService;

    @Autowired
    private UserService userService;

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

    @GetMapping("/viewProfile")
    public String showStudentDetails(Model model, Principal principal) {
        String username = principal.getName();
        Student student = userService.getStudentDetails(username);
        model.addAttribute("student", student);
        return "students/viewProfile";
    }
}

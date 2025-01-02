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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.time.LocalDate;
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
    public String addStudent(@Valid Student student, BindingResult result, RedirectAttributes redirectAttributes ) {
        if (result.hasErrors()) {
            return "students/addForm";
        }
        studentService.saveStudent(student);
        redirectAttributes.addFlashAttribute("message", "Student added successfully!");
        return "redirect:/students/list";
    }

//    @GetMapping("/edit/{id}")
//    public String showEditStudentForm(@PathVariable("id") Long id, Model model) {
//        Optional<Student> student = studentService.getStudentById(id);
//        if (student.isPresent()) {
//            model.addAttribute("student", student.get());
//            return "students/updateForm";
//        }
//        return "redirect:/students/list";
//    }

//    @PostMapping("/edit/{id}")
//    public String updateStudent(@PathVariable("id") Long id, Student student, RedirectAttributes redirectAttributes) {
//        student.setStudentId(id);
//        studentService.saveStudent(student);
//        return "redirect:/students/list";
//    }


    @GetMapping("/edit")
    public String showEditStudentForm(@RequestParam("id") Long id, Model model) {
        Optional<Student> student = studentService.getStudentById(id);
        if (student.isPresent()) {
            model.addAttribute("student", student.get());
            return "students/updateForm";
        }
        return "redirect:/students/list";
    }
    @PostMapping("/edit")
    public String updateStudent(
            @RequestParam("id") Long id,
            @RequestParam("studentCode") String studentCode,
            @RequestParam("fullName") String fullName,
            @RequestParam("dob") LocalDate dob,
            @RequestParam("gender") String gender,
            @RequestParam("phone") String phone,
            @RequestParam("email") String email,
            @RequestParam("classId") Long classId,
            Model model,
            RedirectAttributes redirectAttributes) {

        int result = studentService.updateStudent(id, studentCode, fullName, dob, gender, phone, email, classId);
        if (result > 0) {
            redirectAttributes.addFlashAttribute("message", "Student updated successfully!");
        } else {
            redirectAttributes.addFlashAttribute("message", "Student update failed. Student not found.");
        }
        return "redirect:/students/list";
    }

    @GetMapping("/delete/{id}")
    public String deleteStudent(@PathVariable("id") Long id, RedirectAttributes redirectAttributes) {
        studentService.deleteStudent(id);
        redirectAttributes.addFlashAttribute("message", "Student deleted successfully!");
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

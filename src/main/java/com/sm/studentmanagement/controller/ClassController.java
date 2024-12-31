package com.sm.studentmanagement.controller;

import com.sm.studentmanagement.entity.Class;
import com.sm.studentmanagement.entity.Student;
import com.sm.studentmanagement.service.ClassService;
import com.sm.studentmanagement.service.StudentService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/classes")
public class ClassController {

    @Autowired
    private ClassService classService;

    @Autowired
    private StudentService studentService;

    @GetMapping("/list")
    public String showClassesList(Model model) {
        model.addAttribute("classes", classService.getAllClasses());
        return "classes/list";
    }
    @GetMapping("/add")
    public String showAddClassForm(Model model) {
        model.addAttribute("classes", new Class());
        return "classes/addForm";
    }

    @PostMapping("/add")
    public String addClass(@Valid Class classObj, BindingResult result)
    {
        if(result.hasErrors())
        {
            return "classes/addForm";
        }
        classService.saveClass(classObj);
        return "redirect:/classes/list";
    }

    @GetMapping("/edit/{id}")
    public String showEditClassForm(@PathVariable("id") Long id, Model model) {
        Optional<Class> classObj = classService.getClassById(id);
        if (classObj.isPresent()) {
            model.addAttribute("classes", classObj.get());
            return "classes/updateForm";
        }
        return "redirect:/classes/list";
    }

    @PostMapping("/edit/{id}")
    public String updateStudent(@PathVariable("id") Long id, Class classObj) {
        classObj.setClassId(id);
        classService.saveClass(classObj);
        return "redirect:/classes/list";
    }

    @GetMapping("/delete/{id}")
    public String deleteClass(@PathVariable("id") Long id) {
        classService.deleteClass(id);
        return "redirect:/classes/list";
    }

    @GetMapping("/search")
    public String searchClass(@RequestParam(value = "classId", required = false) Long classId,
                              @RequestParam(value = "className", required = false) String className,
                              Model model) {
        List<Class> classObj = classService.searchClass(classId, className);
        model.addAttribute("classes", classObj);
        return "classes/list";
    }
    @GetMapping("/{id}/students")
    public String viewStudentsInClass(@PathVariable("id") Long classId, Model model) {
        List<Student> students = studentService.getStudentsByClassId(classId);
        Optional<Class> classInfo = classService.getClassById(classId);
        model.addAttribute("students", students);
        model.addAttribute("className", classInfo.get().getClassName());
        return "classes/viewStudents";
    }
}

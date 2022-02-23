package com.example.prj_do_an_two.controller;

import com.example.prj_do_an_two.entity.Category;
import com.example.prj_do_an_two.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class MainCustomerController {
    @Autowired
    CategoryService categoryService;
    @GetMapping("")
    public String viewHomePage(Model model) {
        List<Category> categories = categoryService.listNoChildrenCategories();
        model.addAttribute("categories", categories);

        return "index";
    }

}

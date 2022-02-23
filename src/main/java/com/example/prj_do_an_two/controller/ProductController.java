package com.example.prj_do_an_two.controller;

import com.example.prj_do_an_two.constant.Constant;
import com.example.prj_do_an_two.entity.Category;
import com.example.prj_do_an_two.entity.Product;
import com.example.prj_do_an_two.exception.CategoryException;
import com.example.prj_do_an_two.exception.ProductNotFoundException;
import com.example.prj_do_an_two.service.CategoryService;
import com.example.prj_do_an_two.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class ProductController {
    @Autowired
    private ProductService productService;

    @Autowired
    private CategoryService categoryService;

    @GetMapping("/c/{category_alias}")
    public String viewCategoryFirstPage(@PathVariable("category_alias") String alias,
                                        Model model) {
        return viewCategoryByPage(alias, model, 1);
    }

    @GetMapping("/c/{category_alias}/{pageNum}")
    public String viewCategoryByPage(@PathVariable("category_alias") String alias, Model model,
                               @PathVariable("pageNum") int pageNum) {

        try {
            Category category = categoryService.getCategory(alias);
            List<Category> categoryListParents = categoryService.getCategoryParents(category);
            Page<Product> pageProducts = productService.listByCategory(pageNum, category.getId());


            List<Product> productList = pageProducts.getContent();
            long startCount = (pageNum-1)* Constant.PRODUCTS_IN_PAGE + 1;
            long endCountExpected = startCount + Constant.PRODUCTS_IN_PAGE -1;
            long endCount = (endCountExpected > pageProducts.getTotalElements()) ?
                    pageProducts.getTotalElements() : endCountExpected;

            model.addAttribute("startCount", startCount);
            model.addAttribute("pageNum", pageNum);
            model.addAttribute("totalsPage", pageProducts.getTotalPages());
            model.addAttribute("endCount", endCount);
            model.addAttribute("products", productList);
            model.addAttribute("category", category);
            model.addAttribute("totalItems", pageProducts.getTotalElements());
            model.addAttribute("alias", category.getAlias());


            model.addAttribute("categoryListParents", categoryListParents);
            model.addAttribute("pageTitle", category.getName());
            return "products/products_by_category";
        } catch (CategoryException e) {
            return "error/404";
        }

    }

    @GetMapping("/p/{product_alias}")
    public String viewProductDetail(@PathVariable("product_alias") String alias, Model model) {
        try {
            Product product = productService.getProduct(alias);
            List<Category> categoryListParents = categoryService.getCategoryParents(product.getCategory());
            model.addAttribute("product", product);
            model.addAttribute("pageTitle", product.getName());
            model.addAttribute("categoryListParents", categoryListParents);
            return "products/product_detail";
        } catch (ProductNotFoundException e) {
            return "error/404";
        }
    }

    @GetMapping("/search")
    public String search(@RequestParam("keyword") String keyword, Model model) {
        return searchByPage(keyword, 1, model);
    }
    @GetMapping("/search/page/{pageNum}")
    public String searchByPage(@RequestParam("keyword") String keyword,
                         @PathVariable("pageNum") Integer pageNum,
                         Model model) {

        Page<Product> productPage = productService.search(keyword, pageNum);
        long startCount = (pageNum-1)* Constant.PRODUCTS_IN_PAGE + 1;
        long endCountExpected = startCount + Constant.PRODUCTS_IN_PAGE -1;
        long endCount = (endCountExpected > productPage.getTotalElements()) ?
                productPage.getTotalElements() : endCountExpected;

        model.addAttribute("startCount", startCount);
        model.addAttribute("pageNum", pageNum);
        model.addAttribute("totalsPage", productPage.getTotalPages());
        model.addAttribute("endCount", endCount);
        model.addAttribute("totalItems", productPage.getTotalElements());


        model.addAttribute("keyword", keyword);
        model.addAttribute("listResult", productPage.getContent());
        return "products/search";
    }
}

package com.example.prj_do_an_two.restController;

import com.example.prj_do_an_two.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CustomerRestController {
    @Autowired
    private CustomerService customerService;

    @PostMapping("/customer/check_unique_email")
    public String checkDuplicateEmail(@RequestParam("email") String email) {
        return customerService.isEmailUnique(email) ? "OK" : "Duplicated";
    }
}

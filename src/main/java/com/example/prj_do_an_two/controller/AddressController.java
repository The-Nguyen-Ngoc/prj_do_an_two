package com.example.prj_do_an_two.controller;

import com.example.prj_do_an_two.config.Utility;
import com.example.prj_do_an_two.entity.Address;
import com.example.prj_do_an_two.entity.Country;
import com.example.prj_do_an_two.entity.Customer;
import com.example.prj_do_an_two.exception.CustomerNotFoundException;
import com.example.prj_do_an_two.service.AddressService;
import com.example.prj_do_an_two.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
public class AddressController {

    @Autowired
    private AddressService addressService;
    @Autowired
    private CustomerService customerService;

    @GetMapping("/address_book")
    public String addressBook(Model model, HttpServletRequest request) throws CustomerNotFoundException {
        Customer customer = getAuthenticatedCustomer(request);
        List<Address> addresses = addressService.getAllAddress(customer);
        boolean defaultPrimary = true;
        for (Address address : addresses) {
            if(address.isDefaulForShipping()){
                defaultPrimary = false;
                break;
            }
        }

        model.addAttribute("addresses", addresses);
        model.addAttribute("customer", customer);
        model.addAttribute("defaultPrimary", defaultPrimary);
        return "address_book/address_book";
    }


    private Customer getAuthenticatedCustomer(HttpServletRequest request) throws CustomerNotFoundException {
        String email = Utility.getEmailOfAuthenticatedCustomer(request);
        return customerService.getCustomerByEmail(email);
    }

    @GetMapping("/address_book/create")
    public String addNewAddress(Model model){

        List<Country> countries = customerService.listAllCountries();

        model.addAttribute("address", new Address());
        model.addAttribute("countries",countries);

        return "address_book/addressbook_form";
    }

    @PostMapping("/address_book/save")
    public String saveAddress(Model model, Address address, RedirectAttributes redirectAttributes
    ,HttpServletRequest request) throws CustomerNotFoundException {
        Customer customer = getAuthenticatedCustomer(request);
        address.setCustomer(customer);
        addressService.save(address);
        redirectAttributes.addFlashAttribute("message", "Thêm địa chỉ thành công");
        return "redirect:/address_book";
    }

    @GetMapping("address_book/edit/{id}")
    public String edit(@PathVariable("id") Integer addressId, Model model,
                       HttpServletRequest request) throws CustomerNotFoundException {
        Customer customer = getAuthenticatedCustomer(request);
        List<Country> countries = customerService.listAllCountries();
        Address address = addressService.get(addressId, customer.getId());

        model.addAttribute("address", address);
        model.addAttribute("countries", countries);

        return "address_book/addressbook_form";
    }

    @GetMapping("/address_book/delete/{id}")
    public String deleteAddress(@PathVariable("id") Integer addressId, RedirectAttributes redirectAttributes,
                                HttpServletRequest request) throws CustomerNotFoundException {
        Customer customer = getAuthenticatedCustomer(request);
        addressService.delete(addressId, customer.getId());
        redirectAttributes.addFlashAttribute("message", "Xóa địa chỉ thành công");
        return "redirect:/address_book";
    }

    @GetMapping("/address_book/default/{id}")
    public String setDefault(@PathVariable("id") Integer addressId, RedirectAttributes redirectAttributes,
                             HttpServletRequest request) throws CustomerNotFoundException {
        Customer customer = getAuthenticatedCustomer(request);
        addressService.setDefaultAddress(addressId, customer.getId());

        return "redirect:/address_book";
    }
}

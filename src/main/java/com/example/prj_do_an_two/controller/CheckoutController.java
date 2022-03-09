package com.example.prj_do_an_two.controller;

import com.example.prj_do_an_two.config.Utility;
import com.example.prj_do_an_two.entity.*;
import com.example.prj_do_an_two.exception.CustomerNotFoundException;
import com.example.prj_do_an_two.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
public class CheckoutController {
    @Autowired
    private CheckoutService checkoutService;
    @Autowired
    private CustomerService customerService;
    @Autowired
    private AddressService addressService;
    @Autowired
    private ShippingRateService shippingRateService;
    @Autowired
    private ShoppingCartService shoppingCartService;

    @GetMapping("/checkout")
    public String checkout(Model model, HttpServletRequest request) throws CustomerNotFoundException {
        Customer customer = getAuthenticatedCustomer(request);

        Address defaultAddress = addressService.getDefaultAddress(customer);
        ShippingRate shippingRate = null;

        if(defaultAddress != null){
            model.addAttribute( "shippingAddress", defaultAddress);
            shippingRate = shippingRateService.getShippingRateForAddress(defaultAddress);
        }else {
            model.addAttribute( "shippingAddress", customer.getAddress());
            shippingRate = shippingRateService.getShippingRateForCustomer(customer);
        }
        if(shippingRate == null) return "redirect:/cart";

        List<CartItem> cartItemList = shoppingCartService.listCartItems(customer);
        CheckoutInfo checkoutInfo = checkoutService.prepareCheckout(cartItemList, shippingRate);

        model.addAttribute("checkoutInfo", checkoutInfo);
        model.addAttribute("cartItems", cartItemList);

        return "checkout/checkout";
    }



    private Customer getAuthenticatedCustomer(HttpServletRequest request) throws CustomerNotFoundException {
        String email = Utility.getEmailOfAuthenticatedCustomer(request);
        return customerService.getCustomerByEmail(email);
    }
}

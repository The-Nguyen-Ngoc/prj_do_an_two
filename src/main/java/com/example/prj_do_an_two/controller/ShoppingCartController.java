package com.example.prj_do_an_two.controller;

import com.example.prj_do_an_two.config.Utility;
import com.example.prj_do_an_two.entity.*;
import com.example.prj_do_an_two.exception.CustomerNotFoundException;
import com.example.prj_do_an_two.service.*;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
public class ShoppingCartController {
    @Autowired
    private ShoppingCartService shoppingCartService;
    @Autowired
    private CustomerService customerService;
    @Autowired
    AddressService addressService;
    @Autowired
    ShippingRateService shippingRateService;
    @Autowired
    OrderService orderService;
    @Autowired
    CheckoutService checkoutService;

    @SneakyThrows
    @GetMapping("/cart")
    public String viewCart(Model model, HttpServletRequest request){
        Customer customer = getAuthenticatedCustomer(request);
        List<CartItem> cartItemList = shoppingCartService.listCartItems(customer);
        float totalPrice = 0;
        for (CartItem cartItem : cartItemList) {
            totalPrice += cartItem.getSubTotal();
        }

        Address defaultAddress = addressService.getDefaultAddress(customer);
        ShippingRate shippingRate = null;
        boolean usePrimaryAddressAsDefault = false;

        if(defaultAddress != null){
            shippingRate = shippingRateService.getShippingRateForAddress(defaultAddress);
        }else {
            usePrimaryAddressAsDefault = true;
            shippingRate = shippingRateService.getShippingRateForCustomer(customer);
        }

        model.addAttribute("usePrimaryAddressAsDefault",usePrimaryAddressAsDefault);
        model.addAttribute("shippingSupported",shippingRate!=null);
        model.addAttribute("cartItems", cartItemList);
        model.addAttribute("totalPrice", totalPrice);
        return "cart/shopping_cart";
    }

    private Customer getAuthenticatedCustomer(HttpServletRequest request) throws CustomerNotFoundException {
        String email = Utility.getEmailOfAuthenticatedCustomer(request);
        return customerService.getCustomerByEmail(email);
    }



}

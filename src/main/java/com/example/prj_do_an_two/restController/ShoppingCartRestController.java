package com.example.prj_do_an_two.restController;


import com.example.prj_do_an_two.config.Utility;
import com.example.prj_do_an_two.entity.Customer;
import com.example.prj_do_an_two.exception.CustomerNotFoundException;
import com.example.prj_do_an_two.exception.ShoppingCartException;
import com.example.prj_do_an_two.service.CustomerService;
import com.example.prj_do_an_two.service.ShoppingCartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
public class ShoppingCartRestController {
    @Autowired
    private ShoppingCartService shoppingCartService;
    @Autowired
    private CustomerService customerService;

    @PostMapping("cart/add/{productId}/{quantity}")
    public String addProductToCart(@PathVariable(name = "productId") Integer productId,
                                   @PathVariable(name = "quantity") Integer quantity,
                                   HttpServletRequest request) {

        try {
            Customer customer = getAuthenticatedCustomer(request);
            Integer updatedQuantity = shoppingCartService.addProduct(productId, quantity, customer);
            return updatedQuantity + " sản phẩm đã được thêm vào giỏ hàng";
        } catch (CustomerNotFoundException e) {
            return "Bạn phải đăng nhập để thêm sản phẩm vào giỏ hàng";
        }catch (ShoppingCartException e){
            return e.getMessage();
        }
    }

    private Customer getAuthenticatedCustomer(HttpServletRequest request) throws CustomerNotFoundException {
        String email = Utility.getEmailOfAuthenticatedCustomer(request);

        if (email == null) {
            throw new CustomerNotFoundException("No authenticated customer found");
        }
        return customerService.getCustomerByEmail(email);
    }


    @PostMapping("cart/update/{productId}/{quantity}")
    public String updateQuantity(@PathVariable(name = "productId") Integer productId,
                                 @PathVariable(name = "quantity") Integer quantity,
                                 HttpServletRequest request){

        try {
            Customer customer = getAuthenticatedCustomer(request);
            float subtotal =  shoppingCartService.updateQuantity(productId, quantity, customer);
            return String.valueOf(subtotal);
        } catch (CustomerNotFoundException e) {
            return "Bạn phải đăng nhập để cập nhật sản phẩm trong giỏ hàng";
        }catch (ShoppingCartException e){
            return e.getMessage();
        }
    }

    @DeleteMapping("cart/remove/{productId}")
    public String removeProduct(@PathVariable(name = "productId") Integer productId,
                                HttpServletRequest request){

        try {
            Customer customer = getAuthenticatedCustomer(request);
            shoppingCartService.removeProduct(productId, customer);
            return "Sản phẩm đã được xóa khỏi giỏ hàng";
        } catch (CustomerNotFoundException e) {
            return "Bạn phải đăng nhập để xóa sản phẩm trong giỏ hàng";
        }catch (ShoppingCartException e){
            return e.getMessage();
        }
    }
}

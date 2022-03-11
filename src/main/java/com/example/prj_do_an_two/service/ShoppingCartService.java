package com.example.prj_do_an_two.service;

import com.example.prj_do_an_two.entity.CartItem;
import com.example.prj_do_an_two.entity.Customer;
import com.example.prj_do_an_two.entity.Product;
import com.example.prj_do_an_two.exception.ShoppingCartException;
import com.example.prj_do_an_two.repository.CartItemRepo;
import com.example.prj_do_an_two.repository.ProductRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ShoppingCartService {
    @Autowired
    CartItemRepo cartItemRepo;
    @Autowired
     ProductRepo productRepo;

    public Integer addProduct(Integer productId, Integer quantity, Customer customer) throws ShoppingCartException {
        Integer updatedQuantity = quantity;
        Product product = new Product(productId);

        CartItem cartItem = cartItemRepo.findByProductAndCustomer(product, customer);

        if (cartItem != null) {
            updatedQuantity = cartItem.getQuantity() + quantity;

            if (updatedQuantity > 5) {
                throw new ShoppingCartException("Không thể thêm " + quantity + " sản phẩm"
                        + " vào giỏ hàng của bạn vì đã có " + cartItem.getQuantity()
                        + " SP trong giỏ (Tối đa 5 SP cùng loại) ");
            }
        } else {
            cartItem = new CartItem();
            cartItem.setCustomer(customer);
            cartItem.setProduct(product);
        }
        cartItem.setQuantity(updatedQuantity);
        cartItemRepo.save(cartItem);

        return updatedQuantity;

    }

    public List<CartItem> listCartItems(Customer customer) {
        return cartItemRepo.findByCustomer(customer);
    }

    public float updateQuantity(Integer productId, Integer quantity, Customer customer)
            throws ShoppingCartException {
        cartItemRepo.updateQuantity(quantity, customer.getId(), productId);
        Product product = productRepo.findById(productId).get();
        float subtotal = product.getDiscountPrice() * quantity;
        return subtotal;
    }

    public void removeProduct(Integer productId, Customer customer) throws ShoppingCartException {
        cartItemRepo.deleteByCustomerAndProduct(customer.getId(), productId);
    }

    public void deleteByCustomer(Customer customer) {
        cartItemRepo.deleteByCustomer(customer.getId());
    }
}

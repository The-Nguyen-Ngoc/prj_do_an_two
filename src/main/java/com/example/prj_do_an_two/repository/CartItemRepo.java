package com.example.prj_do_an_two.repository;

import com.example.prj_do_an_two.entity.CartItem;
import com.example.prj_do_an_two.entity.Customer;
import com.example.prj_do_an_two.entity.Product;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface CartItemRepo extends CrudRepository<CartItem, Integer> {

    List<CartItem> findByCustomer(Customer customer);

    CartItem findByProductAndCustomer(Product product, Customer customer);

    @Transactional
    @Modifying
    @Query("update CartItem c set c.quantity = ?1 where c.customer.id = ?2 and c.product.id = ?3")
    void updateQuantity(Integer quantity, Integer customerId, Integer productId);

    @Transactional
    @Modifying
    @Query("delete from CartItem c where c.customer.id = ?1 and c.product.id = ?2")
    void deleteByCustomerAndProduct(Integer customerId, Integer productId);

    @Modifying
    @Transactional
    @Query("delete from CartItem c where c.customer.id = ?1")
    void deleteByCustomer(Integer customerId);
}

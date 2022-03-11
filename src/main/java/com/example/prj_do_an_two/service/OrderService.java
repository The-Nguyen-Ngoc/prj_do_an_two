package com.example.prj_do_an_two.service;

import com.example.prj_do_an_two.entity.*;
import com.example.prj_do_an_two.repository.OrderRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Set;

@Service
public class OrderService {
    public static final int ORDER_PER_PAGE = 10;

    @Autowired
    OrderRepo orderRepo;

    public Order createOrder(Customer customer, Address address, List<CartItem> cartItems,
                             PaymentMethod paymentMethod, CheckoutInfo checkoutInfo) {
        Order order = new Order();
        order.setOrderTime(new Date());
        order.setStatus(OrderStatus.NEW);
        order.setCustomer(customer);
        order.setProductCost(checkoutInfo.getProductCost());
        order.setSubtotal(checkoutInfo.getProductTotal());
        order.setShippingCost(checkoutInfo.getShippingCostTotal());
        order.setTax(0.0f);
        order.setTotal(checkoutInfo.getPaymentTotal());
        order.setPaymentMethod(paymentMethod);
        order.setDeliverDays(checkoutInfo.getDeliverDays());
        order.setDeliverDate(checkoutInfo.getDeliverDate());

        if(address == null) {
            order.copyAddressFromCustomer();
        }else {
            order.copyShippingAddress(address);
        }
        Set<OrderDetail> orderSet = order.getOrderDetails();
        for(CartItem cartItem : cartItems) {
            Product product = cartItem.getProduct();

            OrderDetail orderDetail = new OrderDetail();
            orderDetail.setOrder(order);
            orderDetail.setProduct(product);
            orderDetail.setQuantity(cartItem.getQuantity());
            orderDetail.setUnitPrice(product.getDiscountPrice());
            orderDetail.setProductsCost(product.getCost() * cartItem.getQuantity());
            orderDetail.setSubtotal(cartItem.getSubTotal());
            orderDetail.setShippingCost(cartItem.getShippingCost());

            orderSet.add(orderDetail);
        }

        return orderRepo.save(order);
    }


}

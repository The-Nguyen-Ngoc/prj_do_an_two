package com.example.prj_do_an_two.service;

import com.example.prj_do_an_two.entity.CartItem;
import com.example.prj_do_an_two.entity.CheckoutInfo;
import com.example.prj_do_an_two.entity.Product;
import com.example.prj_do_an_two.entity.ShippingRate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CheckoutService {
    public static final int DIM =150;

    public CheckoutInfo prepareCheckout(List<CartItem> cartItems, ShippingRate shippingRate){
        CheckoutInfo checkoutInfo = new CheckoutInfo();
        float productCost = calculateProductCost(cartItems);
        float productTotal = calculateProductTotal(cartItems);
        float shippingCostTotal = calculateShippingCost(cartItems, shippingRate);
        float paymentTotal = productTotal + shippingCostTotal;

        checkoutInfo.setProductCost(productCost);
        checkoutInfo.setProductTotal(productTotal);
        checkoutInfo.setDeliverDays(shippingRate.getDays());
        checkoutInfo.setCodSupported(shippingRate.isCodSupported());
        checkoutInfo.setShippingCostTotal(shippingCostTotal);
        checkoutInfo.setPaymentTotal(paymentTotal);

        return checkoutInfo;
    }

    private float calculateShippingCost(List<CartItem> cartItems, ShippingRate shippingRate) {
        float shippingCostTotal = 0.0f;

        for(CartItem cartItem : cartItems){
            Product product = cartItem.getProduct();
            float dimWeight =
                    (product.getWidth() * product.getHeight() * product.getLength()) / DIM;
            float finalWeight = product.getWeight()> dimWeight ?product.getWeight(): dimWeight;
            float shippingCost = cartItem.getQuantity()* finalWeight*shippingRate.getRate();
            cartItem.setShippingCost(shippingCost);

            shippingCostTotal += shippingCost;

        }
        return shippingCostTotal;
    }

    private float calculateProductTotal(List<CartItem> cartItems) {
        float total = 0.0f;
        for(CartItem cartItem : cartItems){
            total += cartItem.getSubTotal();
        }
        return total;
    }

    private float calculateProductCost(List<CartItem> cartItems) {
        float cost = 0.0f;
        for(CartItem cartItem : cartItems){
            cost += cartItem.getQuantity() * cartItem.getProduct().getCost();
        }
        return cost;
    }
}

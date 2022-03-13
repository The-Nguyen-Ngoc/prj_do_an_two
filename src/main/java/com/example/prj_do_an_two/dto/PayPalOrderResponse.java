package com.example.prj_do_an_two.dto;

import lombok.Data;

@Data
public class PayPalOrderResponse {
    private String id;
    private String status;

    public boolean validate(String orderId) {
        return id.equals(orderId) && status.equals("COMPLETED");
    }
}

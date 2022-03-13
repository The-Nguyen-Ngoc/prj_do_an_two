package com.example.prj_do_an_two.exception;

public class PayPalApiException extends Exception {
    public PayPalApiException(String detailMessage) {
        super(detailMessage);
    }
}

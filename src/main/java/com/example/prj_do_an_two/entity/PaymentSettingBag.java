package com.example.prj_do_an_two.entity;

import java.util.List;

public class PaymentSettingBag extends SettingBag{
    public PaymentSettingBag(List<Setting> listSettings) {
        super(listSettings);
    }

    public String getURL(){
        return super.getValue("PAYPAL_API_BASE_URL");
    }
    public String getClientID(){
        return super.getValue("PAYPAL_API_CLIENT_ID");
    }
    public String getClientSecret(){
        return super.getValue("PAYPAL_API_CLIENT_SECRET");
    }
}

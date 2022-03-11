package com.example.prj_do_an_two.entity;

import java.util.List;

public class EmailSettingBag extends SettingBag {
    public EmailSettingBag(List<Setting> listSettings) {
        super(listSettings);
    }

    public String getHost(){
        return super.getValue("MAIL_HOST");
    }
    public String getUserName(){
        return super.getValue("MAIL_USERNAME");
    }
    public int getPort(){
        return Integer.parseInt(super.getValue("MAIL_PORT"));
    }
    public String getPassword(){
        return super.getValue("MAIL_PASSWORD");
    }
    public String getSmtpAuth(){
        return super.getValue("SMTP_AUTH");
    }
    public String getSmtpSecured(){
        return super.getValue("SMTP_SECURED");
    }
    public String getFromAddress(){
        return super.getValue("MAIL_FROM");
    }
    public String getCustomerVerifySubject(){
        return super.getValue("CUSTOMER_VERIFY_SUBJECT");
    }
    public String getCustomerVerifyContent(){
        return super.getValue("CUSTOMER_VERIFY_CONTENT");
    }
    public String getSenderName(){
        return super.getValue("MAIL_SENDER_NAME");
    }
    public String getOrderConfirmationObject(){
        return super.getValue("ORDER_CONFIRMATION_SUBJECT");
    }
    public String getOrderConfirmationContent(){
        return super.getValue("ORDER_CONFIRMATION_CONTENT");
    }
}

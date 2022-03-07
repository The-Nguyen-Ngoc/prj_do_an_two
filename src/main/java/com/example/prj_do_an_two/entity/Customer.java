package com.example.prj_do_an_two.entity;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "customers")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, unique = true, length = 50)
    private String email;
    @Column(nullable = false, length = 64)
    private String password;
    @Column(nullable = false, name = "first_name", length = 50)
    private String firstName;
    @Column(nullable = false, name = "last_name", length = 50)
    private String lastName;
    @Column(nullable = false, name = "phone_number", length = 15)
    private String phoneNumber;
    @Column(name = "address_line_1" , nullable = false, length = 64)
    private String addressLine1;
    @Column(name = "address_line_2", length = 64)
    private String addressLine2;
    @Column(nullable = false, length = 45)
    private String city;
    @Column(nullable = false, length = 45)
    private String state;
    @Column(name = "postal_code", length = 64)
    private String postalCode;
    @Column(name = "verification_code", length = 64)
    private String verificationCode;

    private boolean enabled;

    @Column(name = "created_time")
    private Date createdTime;

    @ManyToOne
    @JoinColumn(name = "country_id")
    private Country country;

    @Enumerated(EnumType.STRING)
    @Column(name = "authentication_type", length = 10)
    private AuthenticationType authenticationType;

    @Column(name = "reset_password_token", length = 30)
    private String resetPasswordToken;

    @Transient
    public String getFullName() {
        return firstName + " " + lastName;
    }

    @Transient
    public String getAddress() {
        String address = firstName;

        if (lastName != null && !lastName.isEmpty()) address +=", " + lastName;
        if(!address.isEmpty()) address += ", "+ addressLine1;
        if(addressLine2 != null && !addressLine2.isEmpty()) address += ", "+ addressLine2;
        if(!city.isEmpty()) address += ", "+ city;
        if(state !=null&& !state.isEmpty()) address += ", "+ state;
        address += ", " + country.getName();
        if(!postalCode.isEmpty()) address += ". Mã Bưu Điện: "+ postalCode;
        if(!phoneNumber.isEmpty()) address += ". Số Điện Thoại: "+ phoneNumber;
        return address;
    }
}

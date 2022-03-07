package com.example.prj_do_an_two.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "addresses")
public class Address {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @Column(name = "first_name", nullable = false, length = 45)
    private String firstName;
    @Column(name = "last_name", nullable = false, length = 45)
    private String lastName;
    @Column(name = "phone_number", nullable = false, length = 15)
    private String phoneNumber;
    @Column(name = "address_line_1", nullable = false, length = 64)
    private String addressLine1;
    @Column(name = "address_line_2",length = 64)
    private String addressLine2;

    @Column(nullable = false, length = 45)
    private String city;
    @Column(nullable = false, length = 45)
    private String state;
    @Column(name = "postal_code" ,nullable = false, length = 10)
    private String postalCode;

    @ManyToOne
    @JoinColumn(name = "country_id")
    private Country country;
    @ManyToOne
    @JoinColumn(name = "customer_id")
    private Customer customer;

    @Column(name = "default_address")
    private boolean defaulForShipping;

    @Transient
    public String getAddress() {
        String address = firstName;

        if (lastName != null && !lastName.isEmpty()) address +=" " + lastName;
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

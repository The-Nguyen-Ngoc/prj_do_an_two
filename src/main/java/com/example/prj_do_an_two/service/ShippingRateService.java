package com.example.prj_do_an_two.service;

import com.example.prj_do_an_two.entity.Address;
import com.example.prj_do_an_two.entity.Customer;
import com.example.prj_do_an_two.entity.ShippingRate;
import com.example.prj_do_an_two.repository.ShippingRateRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ShippingRateService {
    @Autowired
    private ShippingRateRepo shippingRateRepo;

    public ShippingRate getShippingRateForCustomer(Customer customer){

        String state = customer.getState();

        if(state == null|| state.isEmpty()){
            state= customer.getCity();
        }

        return shippingRateRepo.findByCountryAndState(customer.getCountry(), state);
    }

    public ShippingRate getShippingRateForAddress(Address address){

        String state = address.getState();

        if(state == null|| state.isEmpty()){
            state= address.getCity();
        }

        return shippingRateRepo.findByCountryAndState(address.getCountry(), state);
    }
}

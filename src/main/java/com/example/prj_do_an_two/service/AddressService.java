package com.example.prj_do_an_two.service;

import com.example.prj_do_an_two.entity.Address;
import com.example.prj_do_an_two.entity.Customer;
import com.example.prj_do_an_two.repository.AddressRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AddressService {
    @Autowired
    private AddressRepository addressRepository;

    public List<Address> getAllAddress(Customer customer) {
        return addressRepository.findByCustomer(customer);
    }

    public void save(Address address) {
        addressRepository.save(address);
    }

    public Address get(Integer addressId, Integer customerId) {
        return addressRepository.findByIdAndCustomer(addressId, customerId);

    }

    public void delete(Integer addressId, Integer customerId) {
        addressRepository.deleteByIdAndCustomer(addressId, customerId);
    }

    public void setDefaultAddress(Integer addressId, Integer customerId) {
        if(addressId>0){

        addressRepository.setDefaultAddress(addressId);
        }
        addressRepository.setNonDefaultOthers(addressId, customerId);
    }

    public Address getDefaultAddress(Customer customer){
        return addressRepository.findDefaultByCustomer(customer.getId());
    }
}

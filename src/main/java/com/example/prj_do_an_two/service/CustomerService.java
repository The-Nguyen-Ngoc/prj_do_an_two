package com.example.prj_do_an_two.service;

import com.example.prj_do_an_two.entity.AuthenticationType;
import com.example.prj_do_an_two.entity.Country;
import com.example.prj_do_an_two.entity.Customer;
import com.example.prj_do_an_two.repository.CountryRepository;
import com.example.prj_do_an_two.repository.CustomerRepository;
import net.bytebuddy.utility.RandomString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class CustomerService {
    @Autowired
    CountryRepository countryRepository;
    @Autowired
    CustomerRepository customerRepository;
    @Autowired
    PasswordEncoder passwordEncoder;

    public List<Country> listAllCountries(){
        return countryRepository.findAllByOrderByNameAsc();
    }

    public boolean isEmailUnique(String email){
        Customer customer = customerRepository.findByEmail(email);
        return customer == null;
    }

    public void registerCustomer(Customer customer){
        encodePassword(customer);
        customer.setEnabled(false);
        customer.setCreatedTime(new Date());
        customer.setAuthenticationType(AuthenticationType.DATABASE);

        String randomCode = RandomString.make(64);
        customer.setVerificationCode(randomCode);
        customerRepository.save(customer);
    }

    private void encodePassword(Customer customer) {
        String encodedPassword = passwordEncoder.encode(customer.getPassword());
        customer.setPassword(encodedPassword);
    }

    public boolean verify(String verificationCode){
       Customer customer =  customerRepository.findByVerificationCode(verificationCode);

       if(customer == null || customer.isEnabled()) {
           return false;
       }
       else {
           customerRepository.enable(customer.getId());
           return true;
       }
    }

    public void updateAuthentication(Customer customer, AuthenticationType type){
        if(!customer.getAuthenticationType().equals(type)){
            customerRepository.updateAuthenticationType( type,customer.getId());
        }
    }

    public Customer getCustomerByEmail(String email){
        return customerRepository.findByEmail(email);
    }

    public void addNewCustomerUponOAuthLogin(String name, String email, String countryCode) {
        Customer customer = new Customer();
        setName(name, customer);
        customer.setEmail(email);
        customer.setEnabled(true);
        customer.setCreatedTime(new Date());
        customer.setAuthenticationType(AuthenticationType.GOOGLE);
        customer.setPassword("");
        customer.setAddressLine1("");
        customer.setAddressLine2("");
        customer.setCity("");
        customer.setPhoneNumber("");
        customer.setState("");
        customer.setPostalCode("");
        customer.setCountry(countryRepository.findByCode(countryCode));

        customerRepository.save(customer);
    }

    private void setName(String name, Customer customer) {
        String[] names = name.split(" ");
        if(names.length <2){
            customer.setFirstName(name);
            customer.setLastName("");
        }else {
            customer.setFirstName(names[0]);
            customer.setLastName(name.replaceFirst(names[0], ""));
        }
    }
}

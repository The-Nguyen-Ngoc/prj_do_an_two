package com.example.prj_do_an_two.config;

import com.example.prj_do_an_two.entity.Customer;
import com.example.prj_do_an_two.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomerDetailService implements UserDetailsService {
    @Autowired private CustomerRepository customerRepository;


    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Customer customer = customerRepository.findByEmail(email);

        if(customer == null) {
            throw new UsernameNotFoundException("User not found");
        }
        return new CustomerUserDetails(customer);
    }
}

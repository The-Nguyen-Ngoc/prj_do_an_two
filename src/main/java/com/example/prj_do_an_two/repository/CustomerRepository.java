package com.example.prj_do_an_two.repository;

import com.example.prj_do_an_two.entity.Customer;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerRepository extends CrudRepository<Customer, Integer> {

    @Query("select c From Customer c where c.email = ?1")
    Customer findByEmail(String email);

    @Query("select c From Customer c where c.verificationCode = ?1")
    Customer findByVerificationCode(String code);

    @Query("update Customer c set c.enabled = true where c.id = ?1")
    @Modifying
    public void enable(Integer integer);
}

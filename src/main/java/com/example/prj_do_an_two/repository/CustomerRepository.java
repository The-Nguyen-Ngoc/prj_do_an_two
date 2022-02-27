package com.example.prj_do_an_two.repository;

import com.example.prj_do_an_two.entity.AuthenticationType;
import com.example.prj_do_an_two.entity.Customer;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface CustomerRepository extends CrudRepository<Customer, Integer> {

    @Query("select c From Customer c where c.email = ?1")
    Customer findByEmail(String email);

    @Query("select c From Customer c where c.verificationCode = ?1")
    Customer findByVerificationCode(String code);

    @Transactional
    @Query("update Customer c set c.enabled = true, c.verificationCode = null where c.id = ?1")
    @Modifying
     void enable(Integer integer);

    @Modifying
    @Transactional
    @Query("update Customer c set c.authenticationType = ?1 where c.id = ?2")
     void updateAuthenticationType(AuthenticationType type, Integer id);
}

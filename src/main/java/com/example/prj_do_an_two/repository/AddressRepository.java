package com.example.prj_do_an_two.repository;


import com.example.prj_do_an_two.entity.Address;
import com.example.prj_do_an_two.entity.Customer;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface AddressRepository extends CrudRepository<Address, Integer> {

    List<Address> findByCustomer(Customer customer);

    @Query("select a from Address a where a.id = ?1 AND a.customer.id = ?2")
    Address findByIdAndCustomer(Integer addressId, Integer customerId);

    @Transactional
    @Query("delete FROM Address a where a.id = ?1 AND a.customer.id = ?2")
    @Modifying
    void deleteByIdAndCustomer(Integer addressId, Integer customerId);
    @Modifying
    @Transactional
    @Query("update Address a SET a.defaulForShipping = true WHERE a.id = ?1")
    void setDefaultAddress(Integer addressId);

    @Transactional
    @Query("update Address a set a.defaulForShipping = false where a.id <> ?1 and a.customer.id = ?2")
    @Modifying
    void setNonDefaultOthers(Integer addressId, Integer customerId);

    @Query("SELECT a FROM Address a WHERE a.customer.id = ?1 and a.defaulForShipping = true")
    Address findDefaultByCustomer(Integer customerId);
}

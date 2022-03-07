package com.example.prj_do_an_two.repository;

import com.example.prj_do_an_two.entity.Country;
import com.example.prj_do_an_two.entity.ShippingRate;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ShippingRateRepo extends CrudRepository<ShippingRate, Integer> {

    ShippingRate findByCountryAndState(Country country, String state);
}

package com.example.prj_do_an_two.repository;

import com.example.prj_do_an_two.entity.Currency;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CurrencyRepo extends CrudRepository<Currency, Integer> {

    List<Currency> findAllByOrderByNameAsc();
}

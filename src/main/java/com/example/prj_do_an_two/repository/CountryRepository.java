package com.example.prj_do_an_two.repository;

import com.example.prj_do_an_two.entity.Country;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@Repository
public interface CountryRepository extends CrudRepository<Country, Integer> {

    List<Country> findAllByOrderByNameAsc();

    @Transactional
    @Modifying
    @Query("DELETE FROM Country c WHERE c.code = ?1")
    void deleteByCode(String code);
}

package com.example.prj_do_an_two.repository;

import com.example.prj_do_an_two.entity.Country;
import com.example.prj_do_an_two.entity.State;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@Repository
public interface StateRepository extends CrudRepository<State, Integer> {

    @Transactional
    @Modifying
    void deleteById(Integer id);
    List<State> findAllByCountryOrderByNameAsc(Country country);
}

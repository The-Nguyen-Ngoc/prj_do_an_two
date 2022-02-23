package com.example.prj_do_an_two.repository;

import com.example.prj_do_an_two.entity.Category;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoryRepo extends CrudRepository<Category, Integer> {

    @Query("SELECT c from Category c where c.enabled = true ORDER BY c.name ASC")
    List<Category> findAllEnabled();

    @Query("select c from Category c where c.enabled = true and c.alias = ?1")
    Category findByAliasEnabled(String alias);

}

package com.example.prj_do_an_two.repository;

import com.example.prj_do_an_two.entity.Setting;
import com.example.prj_do_an_two.entity.SettingCategory;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SettingRepo extends CrudRepository<Setting, String> {
     List<Setting> findByCategory(SettingCategory category);

     @Query("SELECT s from Setting s where s.category = ?1 or s.category = ?2")
     List<Setting> findByTwoCategories(SettingCategory categoryOne, SettingCategory categoryTwo);
}

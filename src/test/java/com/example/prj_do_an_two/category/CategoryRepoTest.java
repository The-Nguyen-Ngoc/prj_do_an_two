package com.example.prj_do_an_two.category;

import com.example.prj_do_an_two.entity.Category;
import com.example.prj_do_an_two.repository.CategoryRepo;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.jdbc.DataJdbcTest;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;

import java.util.List;

@DataJdbcTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class CategoryRepoTest {
    @Autowired
    CategoryRepo categoryRepo;

    @Test
    public void testListCateEnabled() {
       List<Category> categoryList = categoryRepo.findAllEnabled();
       categoryList.forEach((category -> {
           System.out.println(category.getName());
       }) );
    }
}

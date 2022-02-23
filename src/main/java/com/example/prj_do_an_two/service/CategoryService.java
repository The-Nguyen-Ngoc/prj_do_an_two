package com.example.prj_do_an_two.service;

import com.example.prj_do_an_two.entity.Category;
import com.example.prj_do_an_two.exception.CategoryException;
import com.example.prj_do_an_two.repository.CategoryRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
public class CategoryService {
    @Autowired
    CategoryRepo categoryRepo;

    public List<Category> listNoChildrenCategories(){
        List<Category> categories = new ArrayList<>();
        List<Category> listCategories = categoryRepo.findAllEnabled();
        listCategories.forEach(category -> {
            Set<Category> children = category.getChildren();

            if(children==null  || children.size()==0){
                categories.add(category);
            }
        });

        return categories;
    }

    public Category getCategory(String alias) throws CategoryException {
         Category category = categoryRepo.findByAliasEnabled(alias);
         if(category==null){
             throw new CategoryException("Category not found by alias: "+alias);
         }
        return category;
    }

    public List<Category> getCategoryParents(Category child){
       List<Category> parents = new ArrayList<>();

       Category parent = child.getParent();
       while (parent!=null){
           parents.add(0,parent);
           parent = parent.getParent();
       }
       parents.add(child);
       return parents;
    }
}

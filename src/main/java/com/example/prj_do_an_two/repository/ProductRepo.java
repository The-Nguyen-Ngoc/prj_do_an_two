package com.example.prj_do_an_two.repository;

import com.example.prj_do_an_two.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepo extends PagingAndSortingRepository<Product, Integer> {

    @Query("SELECT p FROM Product p WHERE p.enabled = true "
            + "AND (p.category.id = :categoryId OR p.category.allParentIDs LIKE %:categoryIDMatch%)"
            + " ORDER BY p.name ASC")
    Page<Product> listByCategory(@Param("categoryId") Integer categoryId, @Param("categoryIDMatch") String categoryIDMatch, Pageable pageable);

    Product findByAlias(String alias);

    @Query(value = "SELECT * FROM products where products.enabled = true AND " +
            "MATCH(name,short_description, full_description) AGAINST (?1)", nativeQuery = true)
    Page<Product> search(String keyword, Pageable pageable);
}

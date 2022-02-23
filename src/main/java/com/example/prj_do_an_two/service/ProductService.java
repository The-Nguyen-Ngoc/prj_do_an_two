package com.example.prj_do_an_two.service;

import com.example.prj_do_an_two.entity.Product;
import com.example.prj_do_an_two.exception.ProductNotFoundException;
import com.example.prj_do_an_two.repository.ProductRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.webjars.NotFoundException;

@Service
public class ProductService {
    public static final int PRODUCT_PER_PAGE = 10;

    @Autowired
    private ProductRepo productRepository;

    public Page<Product> listByCategory(int pageNum,Integer categoryId) {
        String categoryIdMatch="-"+ String.valueOf(categoryId)+ "-";
        Pageable pageable = PageRequest.of(pageNum-1, PRODUCT_PER_PAGE);

        return productRepository.listByCategory(categoryId,categoryIdMatch, pageable);
    }

    public Product getProduct(String alias) throws ProductNotFoundException {
        Product product = productRepository.findByAlias(alias);

        if(product == null) {
            throw new ProductNotFoundException("Product not found with alias: " + alias);
        }
        return product;
    }

    public Page<Product> search(String keyword, int pageNum) {
        Pageable pageable = PageRequest.of(pageNum-1, PRODUCT_PER_PAGE);
        return productRepository.search(keyword, pageable);
    }
}

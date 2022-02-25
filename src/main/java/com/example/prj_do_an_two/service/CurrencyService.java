package com.example.prj_do_an_two.service;

import com.example.prj_do_an_two.repository.CurrencyRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CurrencyService {
    @Autowired
    private CurrencyRepo currencyRepository;
}

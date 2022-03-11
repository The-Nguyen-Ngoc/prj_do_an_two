package com.example.prj_do_an_two.service;


import com.example.prj_do_an_two.entity.*;
import com.example.prj_do_an_two.repository.SettingRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SettingService {
    @Autowired
    private SettingRepo settingRepository;


    public List<Setting> getGeneralSettings(){
      return settingRepository.findByTwoCategories(SettingCategory.GENERAL, SettingCategory.CURRENCY);

    }

    public EmailSettingBag getEmailSettings(){
        List<Setting> settings = settingRepository.findByCategory(SettingCategory.MAIL_SERVER);
        settings.addAll(settingRepository.findByCategory(SettingCategory.MAIL_TEMPLATES));

        return new EmailSettingBag(settings);
    }

    public CurrencySettingBag getCurrencySettings(){
        List<Setting> settings = settingRepository.findByCategory(SettingCategory.CURRENCY);
        return new CurrencySettingBag(settings);
    }

}

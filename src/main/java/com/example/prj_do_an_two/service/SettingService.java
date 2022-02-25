package com.example.prj_do_an_two.service;


import com.example.prj_do_an_two.entity.GeneralSettingBag;
import com.example.prj_do_an_two.entity.Setting;
import com.example.prj_do_an_two.entity.SettingCategory;
import com.example.prj_do_an_two.repository.SettingRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class SettingService {
    @Autowired
    private SettingRepo settingRepository;


    public List<Setting> getGeneralSettings(){
      return settingRepository.findByTwoCategories(SettingCategory.GENERAL, SettingCategory.CURRENCY);

    }

}

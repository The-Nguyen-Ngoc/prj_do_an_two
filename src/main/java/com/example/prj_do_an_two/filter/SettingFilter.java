package com.example.prj_do_an_two.filter;

import com.example.prj_do_an_two.entity.Setting;
import com.example.prj_do_an_two.service.SettingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.List;

@Component
public class SettingFilter implements Filter {
    @Autowired
    private SettingService settingService;
    @Override
    public void doFilter(ServletRequest servletRequest,
                         ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        String uri = request.getRequestURI().toString();

        if(uri.endsWith(".css")||uri.endsWith(".js")||uri.endsWith(".png")||uri.endsWith(".jpg")){
            filterChain.doFilter(servletRequest,servletResponse);
            return;
        }

        List<Setting> settingList = settingService.getGeneralSettings();

        settingList.forEach(setting -> {
            System.out.println(setting);
            request.setAttribute(setting.getKey(),setting.getValue());
        } );
        filterChain.doFilter(servletRequest, servletResponse);
    }
}

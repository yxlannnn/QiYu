package com.atguigu.lease.web.admin.custom.config;


import com.atguigu.lease.web.admin.custom.converter.StringToBaseEnumConverterFactory;
import com.atguigu.lease.web.admin.custom.interceptor.AuthenticationInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfiguration implements WebMvcConfigurer {


    @Autowired
    private AuthenticationInterceptor authenticationInterceptor;

    @Autowired
    private StringToBaseEnumConverterFactory stringToBaseEnumConverterFactory;
    @Override
    public void addFormatters(FormatterRegistry registry) {

        registry.addConverterFactory(this.stringToBaseEnumConverterFactory);
    }


    //TODO 注册拦截器
    @Override
    public void addInterceptors(InterceptorRegistry registry) {

        //TODO 设置拦截器的拦截范围，拦截范围为/admin路径下面的任意级和任意内容
        registry.addInterceptor(this.authenticationInterceptor)
                .addPathPatterns("/admin/**")
                .excludePathPatterns("/admin/login/**");
    }
}

package com.configuration;

import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;


@Configuration
@MapperScan("com.mapper")
public class MapperConfig {

    @Autowired
    DataSourceConfig dataSourceConfig;

    @Bean
    public SqlSessionFactoryBean getSqlSessionFactoryBean(){
        SqlSessionFactoryBean factoryBean=new SqlSessionFactoryBean();
        factoryBean.setDataSource(dataSourceConfig.getDataSoure());
        ClassPathResource[] mapperLocations=new ClassPathResource[]{
                new ClassPathResource("mappers/userMapper.xml"),
                new ClassPathResource("mappers/authMapper.xml")
        };
        factoryBean.setMapperLocations(mapperLocations);
        factoryBean.setConfigLocation(new ClassPathResource("mybatis-config.xml"));
        return factoryBean;
    }
}

package com.sns.db.dynamicds;

import com.alibaba.druid.spring.boot.autoconfigure.DruidDataSourceBuilder;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.env.Environment;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import tk.mybatis.mapper.common.Mapper;
import tk.mybatis.mapper.entity.Config;
import tk.mybatis.mapper.mapperhelper.MapperHelper;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

@Configuration
@MapperScan(basePackages = "com.sns.dao", sqlSessionFactoryRef = "SqlSessionFactory")
public class MultiDataSourceConfig {

    @Autowired
    private Environment env;

    @Primary
    @Bean(name = DataSourceType.DS0)
    @ConfigurationProperties(prefix = "spring.datasource.db0")
    public DataSource db0(){
        return DruidDataSourceBuilder.create().build();
    }

    @Bean(name = DataSourceType.DS1)
    @ConfigurationProperties(prefix = "spring.datasource.db1")
    public DataSource db1(){
        return DruidDataSourceBuilder.create().build();
    }


    @Bean(name = "dynamicDataSource")
    public DataSource dynamicDataSource() {
        DynamicDataSource dynamicDataSource = new DynamicDataSource();
        // 默认数据源
        dynamicDataSource.setDefaultTargetDataSource(db0());

        // 配置多数据源
        Map<Object, Object> dsMap = new HashMap(5);
        dsMap.put(DataSourceType.DS0, db0());
        dsMap.put(DataSourceType.DS1, db1());

        dynamicDataSource.setTargetDataSources(dsMap);

        return dynamicDataSource;
    }

    @Bean(name = "SqlSessionFactory")
    public SqlSessionFactory test1SqlSessionFactory()
            throws Exception {
        SqlSessionFactoryBean bean = new SqlSessionFactoryBean();
        bean.setDataSource(dynamicDataSource());
        bean.setTypeAliasesPackage(env.getProperty("mybatis.type-aliases-package"));
        bean.setMapperLocations(new PathMatchingResourcePatternResolver().getResources(env.getProperty("mybatis.mapper-locations")));

        // 从刚刚创建的 sqlSessionFactory 中获取 session
        SqlSession session = bean.getObject().openSession();
        // 创建一个MapperHelper
        MapperHelper mapperHelper = new MapperHelper();
        //特殊配置
        Config config = new Config();
        //主键自增回写方法,默认值MYSQL,详细说明请看文档
        config.setIDENTITY(env.getProperty("mapper.identity"));
        //支持getter和setter方法上的注解
        config.setEnableMethodAnnotation(true);
        //设置 insert 和 update 中，是否判断字符串类型!=''
        config.setNotEmpty(Boolean.valueOf(env.getProperty("mapper.not-empty")));
        //设置配置
        mapperHelper.setConfig(config);
        //注册通用接口，和其他集成方式中的 mappers 参数作用相同
        //4.0 之后的版本，如果类似 Mapper.class 这样的基础接口带有 @RegisterMapper 注解，就不必在这里注册
        mapperHelper.registerMapper(Mapper.class);
        mapperHelper.processConfiguration(session.getConfiguration());
        return bean.getObject();
    }

//    @Bean
//    public PlatformTransactionManager transactionManager() {
//        return new DataSourceTransactionManager(dynamicDataSource());
//    }
}

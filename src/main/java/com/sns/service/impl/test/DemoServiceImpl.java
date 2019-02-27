package com.sns.service.impl.test;


import com.sns.db.dynamicds.DS;
import com.sns.db.dynamicds.DataSourceType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.sns.dao.test.DemoMapper;
import com.sns.model.test.Demo;
import com.sns.service.AbstractService;
import com.sns.service.test.DemoService;

@Service
public class DemoServiceImpl extends AbstractService<Demo> implements DemoService {

    @Autowired
    private DemoMapper demoMapper;


    @Override
    @DS
    public Demo getByName(String name) {
        return demoMapper.getByName(name);
    }

    @Override
    @DS(DataSourceType.DS1)
    public Demo getByName1(String name) {
        return demoMapper.getByName(name);
    }
}

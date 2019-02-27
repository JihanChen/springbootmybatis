package com.sns.dao.test;

import com.sns.db.dynamicds.DS;
import com.sns.util.MyMapper;
import com.sns.model.test.Demo;

public interface DemoMapper extends MyMapper<Demo> {
    Demo getByName(String name);

    Demo getByName1(String name);
}
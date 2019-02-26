package com.sns.dao.test;

import com.sns.util.MyMapper;
import com.sns.model.test.Demo;

public interface DemoMapper extends MyMapper<Demo> {

    Demo getByName(String name);
}
package com.sns.service.test;


import com.sns.model.test.Demo;
import com.sns.service.Service;

public interface DemoService extends Service<Demo> {

    Demo getByName(String name);

    Demo getByName1(String name);
}

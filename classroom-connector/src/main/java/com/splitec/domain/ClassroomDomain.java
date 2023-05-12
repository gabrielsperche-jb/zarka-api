package com.splitec.domain;

import com.splitec.repository.ClassroomRepository;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class ClassroomDomain {

  ClassroomRepository repository = new ClassroomRepository();

  public String retrieveActivity(String requestName) throws InstantiationException, IllegalAccessException,
      NoSuchMethodException, InvocationTargetException {
    Class<? extends ClassroomRepository> cls = repository.getClass();
    Object obj = cls.newInstance();
    Method method = cls.getMethod(requestName);
    return (String) method.invoke(obj);
  }
}

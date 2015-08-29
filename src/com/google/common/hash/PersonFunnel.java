package com.google.common.hash;
//枚举类PersonFunnel，应该只有几个固定的对象
//它实现了Funnel<Person>接口，那么必须实现funnel（Person，PrimitiveSink）方法
//这个方法完成的功能就是把一个person对象放入了一个PrimitiveSink对象
//PrimitiveSink的对象有putUnencodedChars，putInt等方法。估计是类似流或者数组。
//PrimitiveSink是个接口，
public enum PersonFunnel implements Funnel<Person> {
    INSTANCE;
    public void funnel(Person person, PrimitiveSink into) {
      into.putUnencodedChars(person.getFirstName())
          .putUnencodedChars(person.getLastName())
          .putInt(person.getAge());
    }
  }
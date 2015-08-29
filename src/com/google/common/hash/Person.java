package com.google.common.hash;

public class Person implements Funnel<Person>{
/**由于Funnel这个接口是继承自Serializable接口的，所以
 你实现了这个接口，也相当于Person对象实现了Serializabe接口
 也就是说Person类的对象都可以保存在磁盘上，那么为了版本管理必须用这个ID
这相当于这个类的指纹值版本控制问题P42.
	 * 
	 */
	private static final long serialVersionUID = 1L;
public String getFirstName(){
	return "Runting";
	
}
public String getLastName(){
	return "Zhao";
	
}
public int getAge(){
	return 24;
	
	
}
public void funnel(Person person, PrimitiveSink into) {
    into.putUnencodedChars(person.getFirstName())
        .putUnencodedChars(person.getLastName())
        .putInt(person.getAge());
  }
}

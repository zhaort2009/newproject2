import java.lang.annotation.ElementType;


public class Test {
	public static void main(String[] args){
		System.out.println("hello");
		Test t=new Test(5);
		
		Class<?> c= t.getClass();
		
		System.out.println(c.getName());  //这个是使用对象来调用 getClass
		                                 //注意通配符Class<?>,因为Class类型必须有<T>是一个泛型类
		
		Class<?> c1=Test.class;
		Class<?> ci= Integer.class;
		System.out.println(c1.getName()); //这个是使用Test类中的class这个静态变量
		System.out.println(ci.getName());
		Size[] values=Size.values();
		for(Size e:values){System.out.println(e.toString());}
		for(ElementType a:ElementType.values()){System.out.println(a.toString());}
		Integer i=new Integer(3);
		Object oi=i;
		System.out.println(oi.getClass().getName());
	}
	private int age;
	public int getAge() {
		return age;
	}


	public void setAge(int age) {
		this.age = age;
	}


	public Test(int i){
		this.age=i;
		
	}
	

public Test() {
	// TODO Auto-generated constructor stub
}

}

enum Size{SMALL, MEDIUM, LARGE, EXTRA_lARGE};

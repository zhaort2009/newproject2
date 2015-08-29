package TestMessageDigest;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import editMultiBloomFilter.Footprint;

public class testMap {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Map<byte[], Integer> smallMap=new HashMap<byte[], Integer>();
		byte[] a={18, -38, -38, 31, -1, 77, 71, -121, -83, -29, 51, 49, 71, 32, 44, 59, 68, 62, 55, 111};
		byte[] b={18, -38, -38, 31, -1, 77, 71, -121, -83, -29, 51, 49, 71, 32, 44, 59, 68, 62, 55, 111};
	   smallMap.put(a,1);
	   Boolean hBoolean=smallMap.containsKey(a);
	   System.out.println(hBoolean);
	   Boolean h2Boolean=smallMap.containsKey(a);
	   System.out.println(h2Boolean);
	   
	   //cache指的就是Map结构，开辟一个固定大小，后面不断更新它
	   
	   //测试1：当一个hashMap装不满的时候，第一次查询肯定是空的
	   
	   //测试2,：当程序执行到取指纹值表入缓存的时候，缓存如何入，数组
	   
	   //测试3：缓存入完，如何重新修改Map结构
	           //是否要转移到内存数组中，还是直接加入Map结构，转移到数组貌似更快
	   
	   
	   //测试4：当Map结构存满了的时候，下一次取指纹值表入缓存，缓存可以覆盖
	   
	   //测试5：缓存覆盖完了之后，Map也能覆盖吗，如何清空，并放入新数据
	   
	   
	}

}

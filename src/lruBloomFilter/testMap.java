package lruBloomFilter;

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
	}

}

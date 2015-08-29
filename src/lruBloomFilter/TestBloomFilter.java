package lruBloomFilter;

import com.google.common.hash.BloomFilter;
import com.sun.org.apache.bcel.internal.generic.NEW;


public class TestBloomFilter {
	public static void main(String[] args){
	Footprint footprint1=new Footprint();
	byte[] fp1={1,2,3,4};
	footprint1.setFootprint(fp1);
	
	Footprint footprint2=new Footprint();
	byte[] fp2={1,2,3,4};
	footprint2.setFootprint(fp2);
	
	Footprint footprint3=new Footprint();
	byte[] fp3={1,2,9,4};
	footprint3.setFootprint(fp3);
	
	FootprintFunnel funnel=new FootprintFunnel();
	BloomFilter<Footprint> bloomFilter=BloomFilter.create(funnel, 1000, 0.001F);
	bloomFilter.put(footprint1);
	Boolean i=bloomFilter.mightContain(footprint2);
	Boolean j=bloomFilter.mightContain(footprint3);
	System.out.println(i);
	System.out.println(j);
	
	}
	

}

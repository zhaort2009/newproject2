package editMultiBloomFilter;


import com.google.common.hash.BloomFilter;

public class TestMultiBloomFilter {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Footprint footprint1=new Footprint();
		byte[] fp1={1,2,3,4};
		footprint1.setFootprint(fp1);
		
		Footprint footprint2=new Footprint();
		byte[] fp2={1,2,3,4};
		footprint2.setFootprint(fp2);
		
		Footprint footprint3=new Footprint();
		byte[] fp3={1,2,9,4};
		footprint3.setFootprint(fp3);
		
		
		
		MultiBloomFilter multiBloomFilter=new MultiBloomFilter(3, 2, 800, 0.001F);
		
		int containResult=multiBloomFilter.mayContain(footprint1);
		int putResult=multiBloomFilter.put(footprint1,800);
		int result=multiBloomFilter.mayContain(footprint1);
		
		int containResult2=multiBloomFilter.mayContain(footprint3);
		
		System.out.println(containResult);
		System.out.println(containResult2);
		System.out.println(putResult);
		System.out.println(result);

	}

}

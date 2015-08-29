package TestMessageDigest;

public class TestMultiThread {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		String s1="e://data/other/serialized-form.html";
		String s2="e://data/other/serialized-form1.html";
		String s3="e://data/other/serialized-form2.html";
		String s4="e://data/other/serialized-form3.html";
		String s5="e://data/other/serialized-form4.html";
		String s6="e://data/other/serialized-form5.html";
		String s7="e://data/other/serialized-form6.html";
		String[] strings=new String[7];
		
		strings[0]=s1;
		strings[1]=s2;
		strings[2]=s3;
		strings[3]=s4;
		strings[4]=s5;
		strings[5]=s6;
		strings[6]=s7;
		
		long start=System.currentTimeMillis();
		System.out.println(start+"ms");
       for (int i = 0; i < strings.length; i++) {
    	   TestFileRunnable r=new TestFileRunnable(strings[i]);
    	   Thread t=new Thread(r);
    	   t.start();
		
	}
      
       
	}

}

package TestMessageDigest;

import java.nio.ByteBuffer;

public class TestTime {

	/**
	 * @param args
	 * 
	 */
	
	int time;
	
	public int getTime() {
		return time;
	}

	public void setTime(int time) {
		this.time = time;
	}

	public static void main(String[] args) {
		
	
		// TODO Auto-generated method stub
		Runtime tt1=Runtime.getRuntime();
		long ss1=tt1.freeMemory();
		System.out.println("ss1="+ss1);
		byte[] a=new byte[2000000];
		ByteBuffer byteBuffer=ByteBuffer.wrap(a);
		byte b=8;
		byte c=9;
		byteBuffer.clear();
		byteBuffer.put(b);
		byteBuffer.put(c);
		byteBuffer.flip();
		
		int t=0;
		while (byteBuffer.hasRemaining()) {
			byte type =  byteBuffer.get();
			t=t+1;
		}
		
		System.out.println(t+"个");
		System.out.println(a[0]);
		System.out.println(a[1]);
		//a[0].setTime(5);
		/*for (int i = 0; i < a.length; i++) {
			a[i]=i;
		}*/
		if(a[0]==0){System.out.println("啥也没有");}
		Runtime tt2=Runtime.getRuntime();
		long total=tt2.totalMemory();
	   long max=tt2.maxMemory();
		long ss2=tt2.freeMemory();
		System.out.println("ss2="+ss2);
		System.out.println("total="+total);
		System.out.println("max="+max);
		System.out.println("ss2-ss1="+(ss2-ss1)+"字节");
		/*long start=System.nanoTime();
		
		double j=Math.pow(i, i);
		long end=System.nanoTime();
		System.out.println((end-start)+"nanoseconds");
		
		/*System.out.println("end");
		int t=5;
		TestTime tttt=new TestTime();
		tttt.setTime(t);
		System.out.println(tttt.getTime());*/

	}

}

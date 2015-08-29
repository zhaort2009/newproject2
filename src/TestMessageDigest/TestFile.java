package TestMessageDigest;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

public class TestFile {

	/**
	 * @param args
	 */
	
	public static void testFile(String fileString){
		File file=new File(fileString);
		try {
			FileInputStream dataInputStream = new FileInputStream(file);
			FileChannel fileChannel=dataInputStream.getChannel();
			
			byte[] buf = new byte[4096];
			ByteBuffer bbuf=ByteBuffer.wrap(buf);
			
			MessageDigest messageDigest;
			
			//int end=0; 
			int times=0;
			
			while ((fileChannel.read(bbuf))!=-1) {
				messageDigest = MessageDigest.getInstance("SHA-1");
				
				bbuf.flip();
				byte[] newbuf=new byte[bbuf.limit()];
			
				
				bbuf.get(newbuf);
				
				//特别注意每次读入到bbuf后buf的值都改变了！！！
				//直接操作buf是最简单了！！
				messageDigest.update(newbuf);
				byte[] digest = messageDigest.digest();	
				//System.out.println("digest="+Arrays.toString(digest));
				times=times+1;
				bbuf.clear();
				//long position=fileChannel.position();
				//System.out.println(position);
				
			}
			System.out.println(times);
			// long endtime=System.currentTimeMillis();
			// System.out.println(endtime);
			//System.out.println("我是分界线"+'\n');
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	
		
	}
	
	
	public static void testReadOnce(String fileString){
		RandomAccessFile dataInputStream;
		//FileInputStream dataInputStream;
		try {
			dataInputStream = new RandomAccessFile(fileString,"r");
			//dataInputStream = new FileInputStream(fileString);
			FileChannel fileChannel=dataInputStream.getChannel();
			
			byte[] buf = new byte[1024*1024];
			ByteBuffer bbuf=ByteBuffer.wrap(buf);
			
			fileChannel.read(bbuf,10000);
			//System.out.println("测试");
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}
	public static void main(String[] args) {
		long start=System.currentTimeMillis();
		//testReadOnce("e://data/other/UltraEdit_17.00.0.1035_SC.exe");
		//testReadOnce("e://data/3.dat");
		testFile("e://data/other3/1.dat");
		testFile("e://data/other3/2.dat");
        long end=System.currentTimeMillis();
        System.out.println((end-start)+"ms");
        
	}

}

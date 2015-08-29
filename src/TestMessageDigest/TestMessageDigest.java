package TestMessageDigest;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

import editMultiBloomFilter.Footprint;

public class TestMessageDigest {
	//总结，
	
	public static void main(String[] args) {
		
		final int BLOCKSIZE = 512;
		//byte[] buf = new byte[4];
		
	MessageDigest messageDigest;
	try {
		byte[] buf = {1,2,3,4};
		messageDigest = MessageDigest.getInstance("SHA-1");
		messageDigest.update(buf);
		byte[] digest = messageDigest.digest();
		for (int i = 0; i < buf.length; i++) {
			System.out.println(buf[i]);
		}
		
		System.out.println(Arrays.toString(buf));
		System.out.println(Arrays.toString(digest));
		System.out.println(digest.length);
		byte s=2;
		System.out.println(s);
		
		int i,j;
		int blockNum=67305985;
		byte[] hashInfo = new byte[digest.length + 4];
		for (i = 0; i < digest.length; ++i)
			hashInfo[i] = digest[i];
		for (j = 0; i < hashInfo.length; ++i){
			hashInfo[i] = (byte)((blockNum >> j) & 0xff);
			j=j+8;
			}
		System.out.println(Arrays.toString(hashInfo));
	String string1=Arrays.toString(hashInfo);
		
		 File file2 = new File("3.txt");
			
			if (file2.exists()) {
				System.out.println("hash log exist, will be overwritten");
			}
			
			try {
				PrintWriter out = new PrintWriter(file2);
				out.println(string1);
				out.close();
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		
		
		
		
        File file = new File("testMessageDigest.txt");
		
		if (file.exists()) {
			System.out.println("hash log exist, will be overwritten");
		}
		
		try {
			FileOutputStream hashLogOutputStream = new FileOutputStream(file);
			hashLogOutputStream.write(hashInfo);
			hashLogOutputStream.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		 

		
	
		try {
			
			DataInputStream in = new DataInputStream(new FileInputStream(file));
			int digestLength=20;
			int hashInfoLength=24;
			byte[] fpr = new byte[digestLength];
			int pbar=0;
			int t,m;
			for (t = 0; t < 20; ++t)
				fpr[t] = in.readByte();
			for (m = 0; t < hashInfoLength; ++t){
				byte pbarb=in.readByte();
				pbar+=(pbarb & 0xff)<<m;
				m=m+8;
				}
			System.out.println(Arrays.toString(fpr));
			System.out.println(pbar);
			
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
		
	
		
		
	} catch (NoSuchAlgorithmException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	
	
	
	}
}

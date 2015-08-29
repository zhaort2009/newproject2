package TestMessageDigest;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.Random;

public class TestWrite {

	/**
	 * @param args
	 * @throws IOException 
	 */
	
	
	public static void main(String[] args) throws IOException {
		
		long t1=System.currentTimeMillis();
		
		FileOutputStream hashLogOutputStream= new FileOutputStream("e://data/other5/write.dat");
		FileChannel hashLogwriteChannel = hashLogOutputStream.getChannel();
		
		byte[] hashInfo = new byte[40*1024];
		ByteBuffer writeByteBuffer=ByteBuffer.wrap(hashInfo);
		
		
		 hashLogwriteChannel.write(writeByteBuffer);
		 hashLogwriteChannel.force(true);
		 long t2=System.currentTimeMillis();
		 System.out.println("存新块"+(t2-t1)+"ms");
		 
		 long tt1=System.currentTimeMillis();
		 
	   	 long  blockNum=new Random().nextLong();
		 for (int j = 7; j>=0; j--){
				hashInfo[20+j] = (byte)(blockNum & 0xff);
				blockNum>>=8;
				}
		 long tt2=System.currentTimeMillis();
		 System.out.println("blockNum移位"+(tt2-tt1)+"ms");
	}

}

package TestMessageDigest;


import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;

public class MapMemeryBuffer {

    public static void main(String[] args) throws Exception {
    	
    	 Runtime tt1=Runtime.getRuntime();
 		long ss1=tt1.freeMemory();
 		System.out.println("ss1="+ss1);
 		
 		
    	//分配缓冲区在内存中一共14M
 		byte[] bbb = new byte[14 * 1024 * 1024];
        ByteBuffer byteBuf = ByteBuffer.wrap(bbb);
        //wrap只算了一份bbb的容量，和直接ByteBuffer allowcate一样
        //又建立一个内存结构，在堆上14M
        //byte[] bbbc = new byte[14 * 1024 * 1024];
        RandomAccessFile fis = new RandomAccessFile("e://data/other/UltraEdit_17.00.0.1035_SC.exe","r");
        //新建了一个文件输出流
        FileOutputStream fos = new FileOutputStream("e://data/other/outFile.txt");
        //将文件加入通道
        FileChannel fc = fis.getChannel();
        
        long timeStar = System.currentTimeMillis();// 得到当前的时间
        
        fc.read(byteBuf);// 1 读取
       
        long timeEnd = System.currentTimeMillis();// 得到当前的时间
	  
       // MappedByteBuffer mbb = fc.map(FileChannel.MapMode.READ_ONLY, 0, fc.size());
       // System.out.println(mbb.capacity());
        //System.out.println(fc.size()/1024);
      
        System.out.println("Read time :" + (timeEnd - timeStar) + "ms");
        
        Runtime tt2=Runtime.getRuntime();
		long total=tt2.totalMemory();
	     long max=tt2.maxMemory();
		long ss2=tt2.freeMemory();
		System.out.println("ss2="+ss2);
		System.out.println("total="+total);
		System.out.println("max="+max);
		System.out.println("ss2-ss1="+(ss2-ss1)+"字节");
		
		
        /*timeStar = System.nanoTime();
        fos.write(bbb);//2.写入
        //mbb.flip();
        timeEnd = System.nanoTime();
        System.out.println("Write time :" + (timeEnd - timeStar) + "ns");
        fos.flush();*/
        fc.close();
        fis.close();
        
    }

}


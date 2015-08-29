package TestMessageDigest;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.channels.FileChannel;
import java.util.HashMap;
import java.util.LinkedList;

import com.google.common.hash.BloomFilter;

public class SingleFilterMain {
	
	FileChannel hashLogwriteChannel;
    RandomAccessFile hashLogRandomAccessFile;
	//MultiBloomFilter multiBloomFilter;
	HashMap<String, Long> cacheHashMap;
	BloomFilter<Footprint> bloomFilter;
	
	
int CreateHashLogwriteChannel(String filePath) {
		
		File file = new File(filePath);
		
		if (file.exists()) {
			System.out.println("hash log exist, will be overwritten");
		}
		
		try {
			FileOutputStream hashLogOutputStream= new FileOutputStream(file);
			hashLogwriteChannel = hashLogOutputStream.getChannel();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return 0;
	}
	
	//创建指纹指表输入流，它会给域赋值 ；这个filePath应该和输出流
	//新建的文件是一个文件，并且这个文件要已经存在
	
 int CreateHashLogRandomAccessFile(String filePath) {
		File file = new File (filePath);
		
		if (!file.exists() || !file.isFile()) {
			System.out.println("Failed: file not exist!");
			return -1;
		}
		try {
			hashLogRandomAccessFile = new RandomAccessFile(file,"r");
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return 0;
	}
	
 void CreateSingleBloomFilter(int insertions,double rate){
		
		int givenExpectedInsertions=insertions;
		double givenRate=rate;
		
		
		
		//System.out.println("叶子节点数"+leafInsertion);
		
		bloomFilter=BloomFilter.create(new FootprintFunnel(), givenExpectedInsertions,  givenRate);
		
		
	}
	
 
 public LinkedList<File> listFiles(String path){
	 
	 LinkedList<File> fileList = new LinkedList<File>();
	 // int fileNum = 0, folderNum = 0;
      File file = new File(path);
      if (file.exists()) {
          LinkedList<File> directroyList = new LinkedList<File>();
          File[] files = file.listFiles();
          for (File file2 : files) {
              if (file2.isDirectory()) {
                  //System.out.println("文件夹:" + file2.getAbsolutePath());
                  directroyList.add(file2);
                 // folderNum++;
              } else {
                  //System.out.println("文件:" + file2.getAbsolutePath());
                  fileList.add(file2);
               // fileNum++;
              }
          }
          File temp_file;
          while (!directroyList.isEmpty()) {
              temp_file = directroyList.removeFirst();
              files = temp_file.listFiles();
              for (File file2 : files) {
                  if (file2.isDirectory()) {
                      //System.out.println("文件夹:" + file2.getAbsolutePath());
                      directroyList.add(file2);
                      //folderNum++;
                  } else {
                      System.out.println("文件:" + file2.getAbsolutePath());
                      fileList.add(file2);
                      //fileNum++;
                  }
              }
             // System.out.println("文件夹共有:" + folderNum + ",文件共有:" + fileNum);
              
              
              
          }
      } else {
          System.out.println("文件不存在!");
      }
    
      return fileList;
 }
 
 
 
 
	public void SingleDedup(String hashLogName,String dataFile) throws FileNotFoundException {
		
		//(1)建立全局指纹值表
		CreateHashLogwriteChannel(hashLogName);
		CreateHashLogRandomAccessFile(hashLogName);
		//(2)建立全局单层布隆过滤器
		CreateSingleBloomFilter(2000,0.0001F);
		//(3)建立全局的Map缓存
		cacheHashMap=new HashMap<String,Long>();
		//(4)调用自己的函数listFiles得到所有文件的列表
		LinkedList<File> arrayListFiles=listFiles(dataFile);
		//(5)循环处理文件列表中的所有文件
		int i=0;
		for (File file : arrayListFiles) {
			i=i+1;
		   FileInputStream aSingleFilterInputStream=new FileInputStream(file);
		   SingleFilterDedup aSingleFilterDedup=new SingleFilterDedup(aSingleFilterInputStream,i);
		   aSingleFilterDedup.deduplication(hashLogRandomAccessFile, hashLogwriteChannel,cacheHashMap,bloomFilter);
		   
		   int totalHashlogIndex=SingleFilterDedup.getHashlogIndex();
		   int totalInsertTimes=SingleFilterDedup.getInsertTimes();
		   int duplicateTimes=SingleFilterDedup.getDuplicateTimes();
		   System.out.println("总的hashLogIndex数目="+totalHashlogIndex);
		   System.out.println("总的insert方法调用次数="+totalInsertTimes);
		   System.out.println("总的重复块数目="+duplicateTimes);
		}
		
		try {
			hashLogwriteChannel.close();
			hashLogRandomAccessFile.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	
	public static void main(String[] args) throws FileNotFoundException{
		long t1=System.currentTimeMillis();
		SingleFilterMain aSingleFilterMain=new SingleFilterMain();
		aSingleFilterMain.SingleDedup("e://data/hash/hashtable.dat","e://data/other3");
		long t2=System.currentTimeMillis();
		System.out.println("总时间"+(t2-t1)+"ms");
		
	}
	

}

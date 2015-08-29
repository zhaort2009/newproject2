package TestMessageDigest;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.channels.FileChannel;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.LinkedList;

public class GenerateCombineFile {

	/**
	 * @param args
	 */
	
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
	

	public void combine(File a, File b) throws IOException{
		
		RandomAccessFile ra = new RandomAccessFile(a, "rw");
		RandomAccessFile rb = new RandomAccessFile(b, "rw");
		RandomAccessFile rat1 = new RandomAccessFile(new File(a.toString()+ "1"), "rw");
		
		RandomAccessFile rbt1 = new RandomAccessFile(new File(b.toString()+ "1"), "rw");
		RandomAccessFile rbt2 = new RandomAccessFile(new File(b.toString()+ "2"), "rw");
		
		 FileChannel raChannel = ra.getChannel();
		 FileChannel rbChannel = rb.getChannel();
		 FileChannel rat1Channel = rat1.getChannel();
		 FileChannel rbt1Channel = rbt1.getChannel();
		 FileChannel rbt2Channel = rbt2.getChannel();
		 
		 raChannel.transferTo(2048*1024, 1024*1024, rat1Channel);
		 rbChannel.transferTo(0, 1024*1024, rbt1Channel);
		 rbChannel.transferTo(1024*1024, 1024*1024, rbt2Channel);
		 
		 raChannel.truncate(1024*1024);
		 
		 rbt1Channel.position(0L);
		 raChannel.transferFrom(rbt1Channel, 1024*1024, 1024*1024);
		 rbt1Channel.close();
		 
		 rat1Channel.position(0L);
		 raChannel.transferFrom(rat1Channel, 2048*1024, 1024*1024);
		 rat1Channel.close();
		 
		 rbt2Channel.position(0L);
		 raChannel.transferFrom(rbt2Channel, 3072*1024, 1024*1024);
		 rbt2Channel.close();
		
		
		 
		 rbChannel.close();
		 raChannel.close();
		 
		 Files.delete(Paths.get(b.toString()));
		 Files.delete(Paths.get(b.toString()+"1"));
		Files.delete(Paths.get(b.toString()+"2"));
		Files.delete(Paths.get(a.toString()+"1"));
	}
	/*public void generateNewFile(String dataFile) {
		
		LinkedList<File> arrayListFiles=listFiles(dataFile);
		//(5)循环处理文件列表中的所有文件
		int i=0;
		for (File file : arrayListFiles) {
			i=i+1;
		   FileInputStream aFileDeduplicationInputStream=new FileInputStream(file);
		   FileDeduplication aFileDeduplication=new FileDeduplication(aFileDeduplicationInputStream,i);
		 //  aFileDeduplication.deduplication(multiBloomFilter, hashLogRandomAccessFile, hashLogwriteChannel,cacheHashMap);
		}
		
	}*/
	
	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub

		 GenerateCombineFile testCombineFile=new GenerateCombineFile();
		 testCombineFile.combine(new File("e://data/other3/1.dat"), new File("e://data/other3/2.dat"));
		 
	}

}

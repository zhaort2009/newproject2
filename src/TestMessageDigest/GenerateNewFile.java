package TestMessageDigest;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.LinkedList;

public class GenerateNewFile {

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
	
	public void insert(File afile, long offset, byte[] content) throws IOException {
		  RandomAccessFile r = new RandomAccessFile(afile, "rw");
		  RandomAccessFile rtemp = new RandomAccessFile(new File(afile.toString()+ "~"), "rw");
		  long fileSize = r.length();
		  FileChannel sourceChannel = r.getChannel();
		  FileChannel targetChannel = rtemp.getChannel();
		  //后半部分先转移给别的文件
		  sourceChannel.transferTo(offset, (fileSize - offset), targetChannel);
		  //然后截断，就可以在最后面写了。位置就是文件末尾
		  sourceChannel.truncate(offset);
		  r.seek(offset);
		  r.write(content);
		  long newOffset = r.getFilePointer();
		  targetChannel.position(0L);
		  sourceChannel.transferFrom(targetChannel, newOffset, (fileSize - offset));
		  sourceChannel.close();
		  targetChannel.close();
		  Files.delete(Paths.get(afile.toString()+"~"));
		}
	
	public void generateNewFile(String dataFile) throws IOException {
		
		LinkedList<File> arrayListFiles=listFiles(dataFile);
		//(5)循环处理文件列表中的所有文件
		int i=0;
		for (File file : arrayListFiles) {
			i=i+1;
		byte[] insertbyte=new byte[i];
		 insert(file, 0, insertbyte);
		}
		
	}
	

	public static void main(String[] args) throws IOException {
		
		GenerateNewFile aFile=new GenerateNewFile();
		aFile.generateNewFile("e://data/other4");
		// TODO Auto-generated method stub

	}

}

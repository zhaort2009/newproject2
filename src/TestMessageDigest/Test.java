package TestMessageDigest;

import java.io.File;
import java.util.LinkedList;

public class Test {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		LinkedList<File> aFiles=new LinkedList<File>();
		//File aFile=aFiles.get(0);
		System.out.println(aFiles.size());
		byte[] b=new byte[10];
		System.out.println(b[0]);
		// TODO Auto-generated method stub
		GlobleMain aGlobleMain=new GlobleMain();
		aGlobleMain.listFiles("e://data");

	}

}

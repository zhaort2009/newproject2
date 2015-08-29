package TestMessageDigest;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;

public class TestPrintWriter {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
try {
	PrintWriter out=new PrintWriter("employee.txt","UTF-8");
	out.println("hello runting");
	out.flush();  //用flush也可以
	//out.close();  //如果不close就根本显示不出来。
} catch (FileNotFoundException | UnsupportedEncodingException e) {
	// TODO Auto-generated catch block
	e.printStackTrace();
}
	}

}

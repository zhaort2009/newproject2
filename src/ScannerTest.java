import java.util.Scanner;


public class ScannerTest {
	public static void main(String[] args){
		ScannerTest a=new ScannerTest();
		a.i=5;
		Scanner in=new Scanner(System.in);
		System.out.println("What is your name");
		String name=in.nextLine();
		System.out.println("Hello "+name);
		
	}
	private int i;

}

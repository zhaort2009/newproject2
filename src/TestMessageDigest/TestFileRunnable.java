package TestMessageDigest;

public class TestFileRunnable implements Runnable {
	 
	String filenameString;

	/**
	 * @param args
	 */
	public TestFileRunnable(String ss){
		filenameString=ss;
	}
	
	public void run(){
			
		TestFile.testFile(filenameString);
				
				
				}
	
		

	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}

package TestMessageDigest;
//记得要探究一下，为什么这两种算法结果正好差了1，原来试int和byte转换的时候并没有什么问题。
public class TestLongtoByte {

	/**
	 * @param args
	 */
	public static byte[] longToBytes(long l) {
	    byte[] result = new byte[8];
	    for (int i = 7; i >= 0; i--) {
	        result[i] = (byte)(l & 0xFF);
	        l >>= 8;
	    }
	    return result;
	}

	public static byte[] longToBytes2(long l) {
	    byte[] result = new byte[8];
	    int i=20;int j=0;
	    for (j = 0; i < 28; ++i){
			result[i-20] = (byte)((l >> j) & 0xff);
			j=j+8;
	    }
	    return result;
	}
	
	public static long bytesToLong(byte[] b) {
	    long result = 0;
	    for (int i = 0; i < 8; i++) {
	        result <<= 8;
	        result |= (b[i] & 0xFF);
	    }
	    return result;
	}
	public static long bytesToLong2(byte[] b) {
	    long result = 0;
	    int k2=20; int j=0;
	    for (j = 0; k2<28; ++k2){
			byte pbarb=b[k2-20];
			result+=(pbarb & 0xff)<<j;
			j=j+8;
			}
	    return result;
	}
	public static long bytesToLong3(byte[] b) {
	    long result = 0;
	    int k2=20; int j=0;
	    for (int m = 7; m>=0; m--){
	    	result<<=8;
			byte pbarb=b[m];
			result |=(pbarb & 0xff);
			
			}
	    return result;
	}
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		long t=-1155869325L;
		 byte[] b=longToBytes(t);
		 long result=bytesToLong(b);
		 System.out.println(result);
		 
		 long t2=-1155869325L;
		 byte[] b2=longToBytes2(t2);
		 long result2=bytesToLong3(b2);
		 System.out.println(result2);
	}

}

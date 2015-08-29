package TestMessageDigest;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Random;

import com.google.common.hash.BloomFilter;

public class SingleFilterDedup {

	
	public final int BLOCKSIZE = 4096;
	private FileInputStream dataInputStream;
	private Random rand;
	private static int hashlogIndex=0;
    private static int insertTimes=0;
    private static int duplicateTimes=0;
  
	
	public static int getDuplicateTimes() {
		return duplicateTimes;
	}


	public static int getHashlogIndex() {
		return hashlogIndex;
	}


	public static int getInsertTimes() {
		return insertTimes;
	}


	public SingleFilterDedup(FileInputStream dataInputStream, int randseed) {
		super();
		this.dataInputStream = dataInputStream;
		this.rand =new Random(randseed);
	}
	

	//返回块号，会返回一个整型值，4个字节
	int GetBlockNum() {
		return rand.nextInt();
	}
	
	

	
 
	
public  void deduplication (RandomAccessFile hashLogRandomAccessFile, FileChannel  hashLogwriteChannel,HashMap<String, Long> cacheMap,BloomFilter<Footprint> bloomFilter) {
		//所有块都可以用，每个块只是改变它们的值
		FileChannel fileChannel=dataInputStream.getChannel();
		byte[] buf = new byte[BLOCKSIZE];
		ByteBuffer bbuf=ByteBuffer.wrap(buf);
		
		byte[] hashInfo;
		
		MessageDigest messageDigest;
		Footprint footprint ;
		
		
		
		
		try {
			
			
			
			while ((fileChannel.read(bbuf))!=-1) {
				long tt1=System.currentTimeMillis();
				//里面是每个块的变量
				bbuf.flip();
				byte[] newbuf=new byte[bbuf.limit()];
				bbuf.get(newbuf);	
				
			messageDigest = MessageDigest.getInstance("SHA-1");
			messageDigest.update(newbuf);
			//一次while循环只有一个digest后面都可以用
			byte[] digest = messageDigest.digest();
			
			//System.out.println("一个4096B数据块="+Arrays.toString(newbuf));
			//System.out.println("此4096B数据块的指纹值"+Arrays.toString(digest));
			
			
			
			
			
			//首先查询是不是在cacheMap中，第一次Lookup方法
			if ((cacheMap.size()!=0)&&(cacheMap.containsKey(Arrays.toString(digest)))) {
				long ts1=System.currentTimeMillis();
				Long resultPbaRefer=cacheMap.get(Arrays.toString(digest));
				System.out.println("重复块找到的地址="+resultPbaRefer);
				duplicateTimes++;
				//resultPbaRefer[1]+=1;TODO 以后要在cache替换时因为reference的改变把整个改变的指纹指表刷新到磁盘上
				//reference和时间先统一都不管了，读进来的时候就不读了。
				bbuf.clear();
				long ts2=System.currentTimeMillis();
				System.out.println("第一次命中时间"+(ts2-ts1)+"ms");
				continue;
					
				
			}
			else {
				
				
				footprint= new Footprint();
				
				long blockNum = (long)GetBlockNum();
				//System.out.println("blockNum="+blockNum);
				
				int reference=1;
				
				footprint.setFootprint(digest);
				footprint.setPhysicalBlockAdress(blockNum);
				if(!bloomFilter.mightContain(footprint)){
					long t1=System.currentTimeMillis();
					hashInfo = new byte[40];
					ByteBuffer writeByteBuffer=ByteBuffer.wrap(hashInfo);
					
					for (int i = 0; i <20; ++i)
						hashInfo[i] = digest[i];
					
					for (int j = 7; j>=0; j--){
						hashInfo[20+j] = (byte)(blockNum & 0xff);
						blockNum>>=8;
						}
					for (int k = 3; k>=0; k--) {
						hashInfo[28+k] = (byte)(reference & 0xff);
						reference>>=8;
					}	
					
					long t=System.currentTimeMillis();
					
					for (int m = 7; m>=0; m--) {
						hashInfo[32+m] = (byte)( t & 0xff);
						t>>=8;
					}	
					
					
					
					//System.out.println("hashInfo="+Arrays.toString(hashInfo));
					++hashlogIndex;
					//System.out.println("hashlogIndex="+hashlogIndex);
					//FileChannel hashLogOutputStreamChannel=globleHashLogOutputStream.getChannel();
					 hashLogwriteChannel.write(writeByteBuffer);
					 hashLogwriteChannel.force(true);
					//hashLogOutputStreamChannel.close();
					 bloomFilter.put(footprint);
					 long t2=System.currentTimeMillis();
					 System.out.println("存新块"+(t2-t1)+"ms");
				}
				else{
					
					FileChannel fcread=hashLogRandomAccessFile.getChannel();
					byte[] readbuf=new byte[2*1024*1024];
					ByteBuffer readbyteBuffer=ByteBuffer.wrap(readbuf);
					int end=0;
					fcread.position(0);
					while ((end=fcread.read(readbyteBuffer))!=-1) {
						long it1=System.currentTimeMillis();
						//insert方法
						cacheMap.clear();
						insertTimes++;
						readbyteBuffer.flip();
						for (int k = 0; k < readbyteBuffer.limit(); k=k+40) {
							byte[] fpr=new byte[20];
							long pbar=0;
							int k2;
							for (k2 = 0; k2 < 20; k2++) {
								fpr[k2]=readbuf[k+k2];
							}
							
							for (int n = 0; n<8; n++){
								byte pbarb=readbuf[k+20+n];
								pbar<<=8;
								pbar |=(pbarb & 0xff);
								}
							
							cacheMap.put(Arrays.toString(fpr), pbar);
							
						}
						long it2=System.currentTimeMillis();
						System.out.println("insert的时间"+(it2-it1)+"ms");
						//lookup方法
						if(cacheMap.containsKey(Arrays.toString(footprint.getFootprint()))){
							long tss1=System.currentTimeMillis();
							long resultPba=cacheMap.get(Arrays.toString(footprint.getFootprint()));
								System.out.println("重复块找到的地址="+resultPba);
								duplicateTimes++;
								long tss2=System.currentTimeMillis();
								System.out.println("取后命中时间"+(tss2-it2)+"ms");
								break;
							}
						readbyteBuffer.clear();
					}//指纹指表的while，就靠这个
				}//这是布隆过滤器的else，重复块，但是缓存没有命中。
		 }//以上是如果cacheMap没有命中的 else
			bbuf.clear();
			long tt2=System.currentTimeMillis();
			System.out.println("一个块"+(tt2-tt1)+"ms");
			}//这个括弧是while结束的括号，一个文件的所有块处理完毕
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	
	}

}

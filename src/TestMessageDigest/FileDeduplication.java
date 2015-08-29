package TestMessageDigest;

import java.io.DataInput;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class FileDeduplication {

	
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


		public FileDeduplication(FileInputStream dataInputStream, int randseed) {
			super();
			this.dataInputStream = dataInputStream;
			this.rand =new Random(randseed);
		}
		

		//返回块号，会返回一个整型值，4个字节
		int GetBlockNum() {
			return rand.nextInt();
		}
		
		
	
		
	 
		
	public  void deduplication (MultiBloomFilter globleMultiBloomFilter, RandomAccessFile hashLogRandomAccessFile, FileChannel  hashLogwriteChannel,HashMap<String, Long> cacheMap) {
			//所有块都可以用，每个块只是改变它们的值
			FileChannel fileChannel=dataInputStream.getChannel();
			byte[] buf = new byte[BLOCKSIZE];
			ByteBuffer bbuf=ByteBuffer.wrap(buf);
			
			byte[] hashInfo;
			
			MessageDigest messageDigest;
			Footprint footprint ;
			
			
			int  footprintIndex=0;
			
			try {
				
				
				
				while ((fileChannel.read(bbuf))!=-1) {
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
					
					Long resultPbaRefer=cacheMap.get(Arrays.toString(digest));
					//System.out.println("重复块找到的地址="+resultPbaRefer);
					duplicateTimes++;
					//resultPbaRefer[1]+=1;TODO 以后要在cache替换时因为reference的改变把整个改变的指纹指表刷新到磁盘上
					//reference和时间先统一都不管了，读进来的时候就不读了。
					bbuf.clear();
					continue;
						
					
				}
				else {
					footprint= new Footprint();
					
					long blockNum = (long)GetBlockNum();
					//System.out.println("blockNum="+blockNum);
					
					int reference=1;
					
					footprint.setFootprint(digest);
					footprint.setPhysicalBlockAdress(blockNum);
					
					
					footprintIndex = globleMultiBloomFilter.mayContain(footprint);
					//System.out.println("footprintIndex="+footprintIndex);
				if (footprintIndex < 0) {
					//过滤器中不存在，应写入指纹指表，然后加入过滤器
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
					
					globleMultiBloomFilter.put(footprint, hashlogIndex);		
				}
				else {
				
					//第一次Inert方法
					//RandomAccessFile inAccessFile=new RandomAccessFile(hashLogName, "r");
					//Map<String, Integer> smallMap=new HashMap<String, Integer>();
					cacheMap.clear();
					int givenLayer=globleMultiBloomFilter.getLayer();
					int givenExpectedInsertions=globleMultiBloomFilter.getExpectedInsertions();
					int givenSubFilter=globleMultiBloomFilter.getSubFilter();
					
					int leafcount=(int)Math.pow(givenSubFilter, givenLayer-1);
					int leafInsertion = givenExpectedInsertions / leafcount;
					if ((givenExpectedInsertions % leafcount) != 0)
						++leafInsertion;
					
					FileChannel fcread=hashLogRandomAccessFile.getChannel();
					byte[] readbuf=new byte[1200*1024];
					ByteBuffer readbyteBuffer=ByteBuffer.wrap(readbuf);
					//万一文件从position开始的位置数据不够1M怎么办呢
					//只要position对就一定不会报错，不够1M，readbyteBuffer的limit就不到容量呗，没关系
					int readHashlogInex=footprintIndex*leafInsertion+1;
					long position=(readHashlogInex-1)*40;
					fcread.read(readbyteBuffer, position);
					readbyteBuffer.flip();
					insertTimes++;
					//System.out.println("readHashlogIndex="+readHashlogInex);
					for (int k = 0; k < readbyteBuffer.limit()-39; k=k+40) {
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
					
					
					//第二次Lookup方法
						
					if(cacheMap.containsKey(Arrays.toString(footprint.getFootprint()))){
					long resultPba=cacheMap.get(Arrays.toString(footprint.getFootprint()));
						//System.out.println("重复块找到的地址="+resultPba);
						duplicateTimes++;
					}
					
					else{  //这里包含第三次的Lookup方法和第二次的Insert方法
						
						System.out.println("find another path");
						int elsefootprintIndex=globleMultiBloomFilter.mayContainInElseLeaf(footprint);
						while(true){
							if(elsefootprintIndex<0){
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
								
								globleMultiBloomFilter.put(footprint, hashlogIndex);		
								
								break;
								}
							else{
								
								insertTimes++;
								cacheMap.clear();
								int readElseHashlogInex=elsefootprintIndex*leafInsertion+1;
								//System.out.println("elsefootprintIndex="+elsefootprintIndex);
								//System.out.println("readElseHashlogIndex="+readElseHashlogInex);
								readbyteBuffer.clear();
								position=(readElseHashlogInex-1)*40;
								fcread.read(readbyteBuffer, position);
								readbyteBuffer.flip();
								//System.out.println("readHashlogIndex="+readHashlogInex);
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
								
								//Lookup方法
									
								if(cacheMap.containsKey(Arrays.toString(footprint.getFootprint()))){
									
								long resultPba=cacheMap.get(Arrays.toString(footprint.getFootprint()));
									//System.out.println("重复块找到的地址="+resultPba);
									duplicateTimes++;
									break;
								}
								
							}
							
							
							
							
						}
						//注意这段语句和一开始就返回-1是一模一样的，因为对于重复的数据块，并没有执行这段语句
								
						
					}//查找另外一条路径的else，包括一个while循环。第二次Lookup得到否
					
				}//mightcontain判断是
				
			 }//以上是如果cacheMap没有命中的 else
				bbuf.clear();
				//long position=fileChannel.position();
				//System.out.println(position);
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

package lruBloomFilter;

import java.io.DataInput;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import com.sun.org.apache.bcel.internal.generic.NEW;

import editMultiBloomFilter.Footprint;
import editMultiBloomFilter.MultiBloomFilter;

public class TestDeduplication {
	//final int BLOCKSIZE = 512;
	FileInputStream dataInputStream;
	//第三个域:绑定到指纹值表的输出流
	FileOutputStream hashLogOutputStream;
	//第四个域:绑定到指纹指表的输入流，只有一个，用它创建一个新文件先
	FileInputStream hashLogInputStream;
	//应该会用它产生随机的一个什么
	Random rand=new Random(1);
	RandomAccessFile inAccessFile;
	//创建数据输入流
	int CreateDataInputStream(String filePath) {
		File file = new File (filePath);
		
		if (!file.exists() || !file.isFile()) {
			System.out.println("Failed: file not exist!");
			return -1;
		}
		try {
			dataInputStream = new FileInputStream(file);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return 0;
	}
	//创建指纹指表输出流;它会给域赋值
	
	int CreateHashLogOutputStream(String filePath) {
		
		File file = new File(filePath);
		
		if (file.exists()) {
			System.out.println("hash log exist, will be overwritten");
		}
		
		try {
			hashLogOutputStream = new FileOutputStream(file);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return 0;
	}
	
	//创建指纹指表输入流，它会给域赋值 ；这个filePath应该和输出流
	//新建的文件是一个文件，并且这个文件要已经存在
	
	int CreateHashLogInputStream(String filePath) {
		File file = new File (filePath);
		
		if (!file.exists() || !file.isFile()) {
			System.out.println("Failed: file not exist!");
			return -1;
		}
		try {
			hashLogInputStream = new FileInputStream(file);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return 0;
	}
	//返回块号，会返回一个整型值，4个字节
	int GetBlockNum() {
		return rand.nextInt();
	}
	
	
	public static Footprint readData(DataInput in)throws IOException{
		int digestLength=20;
		int hashInfoLength=24;
		byte[] fpr = new byte[digestLength];
		int pbar=0;
		int i,j;
		for (i = 0; i < 20; ++i)
			fpr[i] = in.readByte();
		for (j = 0; i < hashInfoLength; ++i){
			byte pbarb=in.readByte();
			pbar+=(pbarb & 0xff)<<j;
			j=j+8;
			}
		return new Footprint(fpr,pbar);
	}
	
	
	
public void deduplication (String hashLogName) {
		
		//CreateDataInputStream(filePath);
		CreateHashLogOutputStream(hashLogName);
		
		//byte[] buf = new byte[BLOCKSIZE];
		byte[] hashInfo;
		int i, j;
		MessageDigest messageDigest;
		Footprint footprint = new Footprint();
		int givenLayer=3;
		int givenSubFilter=2;
		int givenExpectedInsertions=8;
		double givenRate=0.0001F;
		int leafcount=(int)Math.pow(givenSubFilter, givenLayer-1);
		int leafInsertion = givenExpectedInsertions / leafcount;
		if ((givenExpectedInsertions % leafcount) != 0)
			++leafInsertion;
		System.out.println("叶子节点数"+leafInsertion);
		MultiBloomFilter multiBloomFilter = new MultiBloomFilter(givenLayer, givenSubFilter, givenExpectedInsertions, givenRate);
		
		int  footprintIndex, hashlogIndex = 0;
		//输入流的read方法的后两个参数0，都是buf数组的下标参数，第三个参数
		//不能比buf的大小小。读入到buf的哪个位置。这样相当于将要被去重的文件
		//每512个字节读入buf，现在只读入了第一个块while-1//TODO 这里要加循环，计算其指纹值，并产生了一个int型，int可以
		//直接付给long，不用转换。
		//这就是把实例化footprint一个条目了，其实师兄这个和我的没啥大区别，
		//只是有些重复的条目不存入指纹指表就行了，一样的
		//存一个条目hashlogindex增加1，后面put的时候要算出是位于指纹指表的哪部分
		try {
			//dataInputStream.read(buf, 0, BLOCKSIZE);
			byte[] fp1={1,2,3,4};
			byte[] fp2={5,6,7,8};
			byte[] fp3={9,10,11,12};
			byte[] fp4={13,14,15,16};
			byte[] fp5={1,2,3,4};
			byte[] fp6={5,6,7,8};
			byte[] fp7={9,10,11,12};
			byte[] fp8={13,14,15,16};
			ArrayList<byte[]> b=new ArrayList<byte[]>();
			b.add(fp1);
			b.add(fp2);
			b.add(fp3);
			b.add(fp4);
			b.add(fp5);
			b.add(fp6);
			b.add(fp7);
			b.add(fp8);
			
				
			for (int z = 0; z < b.size(); z++) {
			messageDigest = MessageDigest.getInstance("SHA-1");
			messageDigest.update(b.get(z));
			byte[] digest = messageDigest.digest();
			System.out.println("fp="+Arrays.toString(b.get(z)));
			System.out.println("digest="+Arrays.toString(digest));
			
			int blockNum = GetBlockNum();
			System.out.println("blockNum="+blockNum);
			
			footprint.setFootprint(digest);
			footprint.setPhysicalBlockAdress(blockNum);
			
			footprintIndex = multiBloomFilter.mayContain(footprint);
			System.out.println("footprintIndex="+footprintIndex);
			if (footprintIndex < 0) {
				//过滤器中不存在，应写入hashlog，然后加入过滤器
				hashInfo = new byte[digest.length + 4];
				for (i = 0; i < digest.length; ++i)
					hashInfo[i] = digest[i];
				for (j = 0; i < hashInfo.length; ++i){
					hashInfo[i] = (byte)((blockNum >> j) & 0xff);
					j=j+8;
					}
				System.out.println("hashInfo="+Arrays.toString(hashInfo));
				++hashlogIndex;
				System.out.println("hashlogIndex="+hashlogIndex);
				hashLogOutputStream.write(hashInfo);
				hashLogOutputStream.flush();
				multiBloomFilter.put(footprint, hashlogIndex);		
			}
			else {
				//过滤器中存在，应读出指纹值，判断是否为重复数据块
				//Insert方法利用footprintIndex读出指纹值表的相应部分Insert方法
				//Lookup方法查找是否在指纹值表中，//TODO 这里先实现一个没有LRU算法的
				//就是单纯的布隆过滤器，就是只有一个磁盘块大小512个字节，一个扇区大小
				//就等于不缓存，要缓存就要先检查
				//CreateHashLogInputStream(hashLogName);
				//Inert方法
				RandomAccessFile inAccessFile=new RandomAccessFile(hashLogName, "r");
				Map<String, Integer> smallMap=new HashMap<String, Integer>();
				int readHashlogInex=footprintIndex*leafInsertion+1;
				System.out.println("readHashlogIndex="+readHashlogInex);
				for (int k = 0; k <leafInsertion ; k++) {
					//System.out.println("起始读入位置="+(readHashlogInex+k-1)*Footprint.RECORD_SIZE);
					inAccessFile.seek((readHashlogInex+k-1)*Footprint.RECORD_SIZE);
					Footprint temFootprint=readData(inAccessFile);
					//System.out.println("读出来的digest="+Arrays.toString(temFootprint.getFootprint()));
					smallMap.put(Arrays.toString(temFootprint.getFootprint()), temFootprint.getPhysicalBlockAdress());
					
				}
				
				//Lookup方法
					
				if(smallMap.containsKey(Arrays.toString(footprint.getFootprint()))){
					
				int resultPba=smallMap.get(Arrays.toString(footprint.getFootprint()));
					System.out.println("重复块找到的地址="+resultPba);
				}
				
				else{
					
					//System.out.println("find another path");
					int elsefootprintIndex=multiBloomFilter.mayContainInElseLeaf(footprint);
					while(true){
						if(elsefootprintIndex<0){
							hashInfo = new byte[digest.length + 4];
							for (i = 0; i < digest.length; ++i)
								hashInfo[i] = digest[i];
							for (j = 0; i < hashInfo.length; ++i){
								hashInfo[i] = (byte)((blockNum >> j) & 0xff);
								j=j+8;
								}
							System.out.println("hashInfo="+Arrays.toString(hashInfo));
							++hashlogIndex;
							System.out.println("其它hashlogIndex="+hashlogIndex);
							hashLogOutputStream.write(hashInfo);
							hashLogOutputStream.flush();
							multiBloomFilter.put(footprint, hashlogIndex);
							break;
							}
						else{
							int readElseHashlogInex=elsefootprintIndex*leafInsertion+1;
							System.out.println("elsefootprintIndex="+elsefootprintIndex);
							System.out.println("readElseHashlogIndex="+readElseHashlogInex);
							for (int k = 0; k <leafInsertion ; k++) {
								//System.out.println("起始读入位置="+(readHashlogInex+k-1)*Footprint.RECORD_SIZE);
								inAccessFile.seek((readElseHashlogInex+k-1)*Footprint.RECORD_SIZE);
								Footprint temFootprint=readData(inAccessFile);
								//System.out.println("读出来的digest="+Arrays.toString(temFootprint.getFootprint()));
								smallMap.put(Arrays.toString(temFootprint.getFootprint()), temFootprint.getPhysicalBlockAdress());
								
							}
							
							//Lookup方法
								
							if(smallMap.containsKey(Arrays.toString(footprint.getFootprint()))){
								
							int resultPba=smallMap.get(Arrays.toString(footprint.getFootprint()));
								System.out.println("重复块找到的地址="+resultPba);
								break;
							}
							
						}
						
						
						
						
					}
					//注意这段语句和一开始就返回-1是一模一样的，因为对于重复的数据块，并没有执行这段语句
							
					
				}
				
			}
		}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	
	}
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		new TestDeduplication().deduplication("deduplication.dat");

	}

}

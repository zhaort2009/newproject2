package editMultiBloomFilter;

import com.google.common.hash.BloomFilter;
import java.util.ArrayList;
import  java.util.Stack;

public class MultiBloomFilter {
	//层数L，分叉数N subFilter
	private int layer;
	private int subFilter;
	private int expectedInsertions;
	private double rate;
	private ArrayList<ArrayList<BloomFilter<Footprint>>> multiBloomFilter;
	Stack<BloomFilterIndex> candidateFilters;
	
	public MultiBloomFilter (int layerNum, int subFilterNum, int insertion, double r) {
		layer = layerNum;
		subFilter = subFilterNum;
		expectedInsertions = insertion;
		rate = r;
		multiBloomFilter = new ArrayList<>(layer);
		create();
	}
	
	/*
	public MultiBloomFilter () {
		layer = 2;
		subFilter = 4;
		expectedInsertions = 100;
		rate = 0.03;
		multiBloomFilter = new ArrayList<>(layer);
		create();
	}
	*/
	private int create () {
		int i, j, insertion, count = 1;
		ArrayList<BloomFilter<Footprint>> bloomFilterList;
		BloomFilter<Footprint> bloomFilter;
		
		for (i = 0; i < layer; ++i) {
			bloomFilterList = new ArrayList<>(count);
			for (j = 0; j < count; ++j) {
				insertion = expectedInsertions / count;
				if ((expectedInsertions % count) != 0)
					++insertion;
				bloomFilter = BloomFilter.create(new FootprintFunnel(), insertion, rate);
				bloomFilterList.add(bloomFilter);
			}
			multiBloomFilter.add(bloomFilterList);
			count *= subFilter;
		}
		return 0;
	}
	
	public int put (Footprint fp, int index) {
		int i, j, leafInsertion,leafcount;
		ArrayList<BloomFilter<Footprint>> bloomFilterList;
		
		
		//先计算出应该放在最底层的哪个叶子上，叶子层的j
		//先计算出每个过滤器的大小，当不能整除时，应该加 1
		leafcount=(int)Math.pow(subFilter, layer-1);
		System.out.println(leafcount);
		leafInsertion = expectedInsertions / leafcount;
		if ((expectedInsertions % leafcount) != 0)
			++leafInsertion;
		j = (index-1) / leafInsertion;  //这里index-1，不减1，会产生0,1,2,3,会产生4，但是原来的整个减1会产生-1，只有index-1，才对，因为index从1开始
		//从叶子一层开始,用下一层的j算出上一层的j一定要保证两个j是一棵树上
		//放在哪个叶子节点上并不关键，但是必须保证和上一层是父子关系
		for (i = layer-1; i >=0; --i) {
			//据此计算出应该插入到哪个叶子过滤器
			bloomFilterList = multiBloomFilter.get(i);
			bloomFilterList.get(j).put(fp);
		    j=j/subFilter; 
		}
		return 0;
	}
	
	public int mayContain (Footprint fp) {
		int i, beg, end;
		
		BloomFilterIndex filterIndex;
		ArrayList<BloomFilter<Footprint>> bloomFilterList;
		
		
		//第一层过滤器首先入栈
		filterIndex = new BloomFilterIndex(0, 0);
		candidateFilters = new Stack<>();
		candidateFilters.push(filterIndex);
		
		while (!candidateFilters.isEmpty()) {
			
			//从栈中取出一个过滤器，判断fp是否存在
			filterIndex = candidateFilters.pop();
			bloomFilterList = multiBloomFilter.get(filterIndex.layer);
			if (bloomFilterList.get(filterIndex.index).mightContain(fp)) {
				//到达叶子节点则说明fp存在，此时需要返回索引值
				if (filterIndex.layer == layer - 1)
					return filterIndex.index;
				else {
					//没有到达叶子节点时，需要把该节点的所有子节点倒序入栈，这样下次循环时，子节点会顺序弹出
					beg = (filterIndex.index + 1) * subFilter - 1;
					end = filterIndex.index * subFilter;
					for (i = beg; i >= end; --i) {
						candidateFilters.push(new BloomFilterIndex(filterIndex.layer + 1, i));
					}
				}
			}
		}
		//所有的节点都检查过，没有找到，不存在
		return -1;
	}
	
	
	//这个函数是要检查如果第一条路径命中失效了，要找寻另一条路径。
	//
	
	
	public int mayContainInElseLeaf (Footprint fp) {
		int i, beg, end;
		
		BloomFilterIndex filterIndex;
		ArrayList<BloomFilter<Footprint>> bloomFilterList;
		
	
		//可以考虑到，肯定不会最先调用这个方法，这个方法调用值之前肯定会调用mayContain
		//candidateFilters域会保留上一次调用mayContain的stack
		while (!candidateFilters.isEmpty()) {
			
			//从栈中取出一个过滤器，判断fp是否存在
			filterIndex = candidateFilters.pop();
			bloomFilterList = multiBloomFilter.get(filterIndex.layer);
			if (bloomFilterList.get(filterIndex.index).mightContain(fp)) {
				//到达叶子节点则说明fp存在，此时需要返回索引值
				if (filterIndex.layer == layer - 1)
					return filterIndex.index;
				else {
					//没有到达叶子节点时，需要把该节点的所有子节点倒序入栈，这样下次循环时，子节点会顺序弹出
					beg = (filterIndex.index + 1) * subFilter - 1;
					end = filterIndex.index * subFilter;
					for (i = beg; i >= end; --i) {
						candidateFilters.push(new BloomFilterIndex(filterIndex.layer + 1, i));
					}
				}
			}
		}
		//所有的节点都检查过，没有找到，不存在
		return -1;
	}
	
}

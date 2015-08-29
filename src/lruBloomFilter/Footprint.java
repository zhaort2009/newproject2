package lruBloomFilter;

public class Footprint {
	private byte[] fp;
	private int physicalBlockAdress;
	public static int RECORD_SIZE=24; 
	public Footprint() {
		
	}
	public Footprint(byte[] fp, int physicalBlockAdress) {
		
		this.fp = fp;
		this.physicalBlockAdress = physicalBlockAdress;
	}
	public void setFootprint (byte[] f) { fp = f; }
	public void setPhysicalBlockAdress (int pba) { physicalBlockAdress = pba; }
	public int getPhysicalBlockAdress () { return physicalBlockAdress; }
	public byte[] getFootprint () { return fp; }
}
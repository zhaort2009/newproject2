package TestMessageDigest;

public class Footprint {
	public static int RECORD_SIZE=40;
	
	
	private byte[] fp;
	private long physicalBlockAdress;
	private int reference;
	private long  time;
	
	
	public Footprint() {
		
	}
	
	
	public Footprint(byte[] fp, long physicalBlockAdress, int reference,
			long time) {
		this.fp = fp;
		this.physicalBlockAdress = physicalBlockAdress;
		this.reference = reference;
		this.time = time;
	}


	public int getReference() {
		return reference;
	}
	public void setReference(int reference) {
		this.reference = reference;
	}
	public long getTime() {
		return time;
	}
	public void setTime(long time) {
		this.time = time;
	}
	public void setFootprint (byte[] f) { fp = f; }
	public byte[] getFootprint () { return fp; }
	
	public void setPhysicalBlockAdress (long blockNum) { physicalBlockAdress = blockNum; }
	public long getPhysicalBlockAdress () { return physicalBlockAdress; }
	
}
package de.uniol.inf.is.odysseus.test.generator.datatype;

/**
 * 
 * @author pBruns
 *
 */
public class ByteTestGenerator implements IDatatypeTestGenerator{
	private static IDatatypeTestGenerator instance;
	
	private byte[] testcases;
	private int rotation;
	
	public ByteTestGenerator() {
		this.testcases = new byte[]{Byte.MAX_VALUE, Byte.MIN_VALUE, (byte) (Byte.MAX_VALUE+1), (byte) (Byte.MIN_VALUE-1), 0};
	}

	@Override
	public Object getValue() {
		byte value = this.testcases[this.rotation];
		this.rotation++;
		if(this.rotation==this.testcases.length){
			this.rotation=0;
		}
		return value;
	}
	
	public static IDatatypeTestGenerator getInstance() {
		if(instance==null){
			instance = new ByteTestGenerator();
		}
		return instance;
	}
}

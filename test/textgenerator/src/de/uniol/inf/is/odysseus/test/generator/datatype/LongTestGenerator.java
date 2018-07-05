package de.uniol.inf.is.odysseus.test.generator.datatype;

/**
 * 
 * @author pBruns
 *
 */
public class LongTestGenerator implements IDatatypeTestGenerator{
	private static IDatatypeTestGenerator instance;
	private long[] testcases;
	private int rotation;
	
	public LongTestGenerator() {
		this.testcases = new long[]{Long.MAX_VALUE, Long.MIN_VALUE, Long.MAX_VALUE+1, Long.MIN_VALUE-1, 0};
	}

	@Override
	public Object getValue() {
		long value = this.testcases[this.rotation];
		this.rotation++;
		if(this.rotation==this.testcases.length){
			this.rotation=0;
		}
		return value;
	}
	
	public static IDatatypeTestGenerator getInstance() {
		if(instance==null){
			instance = new LongTestGenerator();
		}
		return instance;
	}
}

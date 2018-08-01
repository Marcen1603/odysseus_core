package de.uniol.inf.is.odysseus.test.generator.datatype;

/**
 * 
 * @author pBruns
 *
 */
public class ShortTestGenerator implements IDatatypeTestGenerator{
	private static IDatatypeTestGenerator instance;
	private short[] testcases;
	private int rotation;
	
	public ShortTestGenerator() {
		this.testcases = new short[]{Short.MAX_VALUE, Short.MIN_VALUE, (short) (Short.MAX_VALUE+1), (short) (Short.MIN_VALUE-1), 0};
	}

	@Override
	public Object getValue() {
		short value = this.testcases[this.rotation];
		this.rotation++;
		if(this.rotation==this.testcases.length){
			this.rotation=0;
		}
		return value;
	}

	
	public static IDatatypeTestGenerator getInstance() {
		if(instance==null){
			instance = new ShortTestGenerator();
		}
		return instance;
	}
}

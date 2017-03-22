package de.uniol.inf.is.odysseus.test.generator.datatype;

/**
 * 
 * @author pBruns
 *
 */
public class IntegerTestGenerator implements IDatatypeTestGenerator{
	private static IDatatypeTestGenerator instance;
	
	private int[] testcases;
	private int rotation;
	
	public IntegerTestGenerator() {
		this.testcases = new int[]{Integer.MAX_VALUE, Integer.MIN_VALUE, Integer.MAX_VALUE+1, Integer.MIN_VALUE-1, 0};
	}

	@Override
	public Object getValue() {
		int value = this.testcases[this.rotation];
		this.rotation++;
		if(this.rotation==this.testcases.length){
			this.rotation=0;
		}
		return value;
	}

	public static IDatatypeTestGenerator getInstance() {
		if(instance==null){
			instance = new IntegerTestGenerator();
		}
		return instance;
	}
}

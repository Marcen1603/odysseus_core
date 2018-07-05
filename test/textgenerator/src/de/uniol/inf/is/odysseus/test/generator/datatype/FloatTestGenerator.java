package de.uniol.inf.is.odysseus.test.generator.datatype;

/**
 * 
 * @author pBruns
 *
 */
public class FloatTestGenerator implements IDatatypeTestGenerator{
	private static IDatatypeTestGenerator instance;
	private float[] testcases;
	private int rotation;
	
	public FloatTestGenerator() {
		this.testcases = new float[]{Float.MAX_VALUE, Float.MIN_VALUE, Float.MAX_VALUE+1, Float.MIN_VALUE-1, Float.NEGATIVE_INFINITY, Float.POSITIVE_INFINITY, Float.NaN, 0.0f};
	}

	@Override
	public Object getValue() {
		float value = this.testcases[this.rotation];
		this.rotation++;
		if(this.rotation==this.testcases.length){
			this.rotation=0;
		}
		return value;
	}
	
	public static IDatatypeTestGenerator getInstance() {
		if(instance==null){
			instance = new FloatTestGenerator();
		}
		return instance;
	}
}

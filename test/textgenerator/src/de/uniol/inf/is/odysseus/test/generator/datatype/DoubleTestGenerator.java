package de.uniol.inf.is.odysseus.test.generator.datatype;

/**
 * 
 * @author pBruns
 *
 */
public class DoubleTestGenerator implements IDatatypeTestGenerator{
	private static IDatatypeTestGenerator instance;
	
	private double[] testcases;
	private int rotation;
	
	public DoubleTestGenerator() {
		this.testcases = new double[]{Double.MAX_VALUE, Double.MIN_VALUE, Double.MAX_VALUE+1, Double.MIN_VALUE-1, Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY, Double.NaN, 0.0};
	}

	@Override
	public Object getValue() {
		double value = this.testcases[this.rotation];
		this.rotation++;
		if(this.rotation==this.testcases.length){
			this.rotation=0;
		}
		return value;
	}
	
	public static IDatatypeTestGenerator getInstance() {
		if(instance==null){
			instance = new DoubleTestGenerator();
		}
		return instance;
	}

}

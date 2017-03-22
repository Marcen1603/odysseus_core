package de.uniol.inf.is.odysseus.test.generator.datatype;

import java.util.ArrayList;
import java.util.List;

/**
 * 
 * @author pBruns
 *
 */
public class ListTestGenerator implements IDatatypeTestGenerator{
	private static IDatatypeTestGenerator instance;
	
	private List[] testcases;
	private int rotation;
	
	public ListTestGenerator() {
		this.testcases = new List[]{new ArrayList<>(), null};
	}

	@Override
	public Object getValue() {
		List value = this.testcases[this.rotation];
		this.rotation++;
		if(this.rotation==this.testcases.length){
			this.rotation=0;
		}
		return value;
	}
	
	public static IDatatypeTestGenerator getInstance() {
		if(instance==null){
			instance = new ListTestGenerator();
		}
		return instance;
	}
}

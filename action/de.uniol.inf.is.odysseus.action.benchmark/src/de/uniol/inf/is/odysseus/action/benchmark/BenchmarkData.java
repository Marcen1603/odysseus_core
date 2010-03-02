package de.uniol.inf.is.odysseus.action.benchmark;

import de.uniol.inf.is.odysseus.base.IClone;
import de.uniol.inf.is.odysseus.base.IMetaAttribute;

public class BenchmarkData implements IMetaAttribute {
	private String identifier;
	private String no;
	
	public BenchmarkData(String identifier, String no){
		this.identifier = identifier;
		this.no = no;
	}
	
	/**
	 * Copy constructor
	 * @param benchmarkData
	 */
	public BenchmarkData(BenchmarkData benchmarkData) {
		this.identifier = benchmarkData.identifier;
		this.no = benchmarkData.no;
	}

	@Override
	public IClone clone() {
		return new BenchmarkData(this);
	}
	
	public String getIdentifier() {
		return identifier;
	}
	
	public String getNo() {
		return no;
	}

}

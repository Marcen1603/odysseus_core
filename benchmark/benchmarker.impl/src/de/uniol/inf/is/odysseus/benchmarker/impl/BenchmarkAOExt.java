package de.uniol.inf.is.odysseus.benchmarker.impl;

import de.uniol.inf.is.odysseus.logicaloperator.AbstractLogicalOperator;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttributeList;

/**
 * @author Jonas Jacobi
 */
public class BenchmarkAOExt extends AbstractLogicalOperator {

	private static final long serialVersionUID = 9094076133083176377L;
	private int processingTimeInns;
	private double selectivity;
	
	public BenchmarkAOExt(BenchmarkAOExt benchmarkAO) {
		super();
		this.processingTimeInns = benchmarkAO.processingTimeInns;
		this.selectivity = benchmarkAO.selectivity;
	}
	
	public BenchmarkAOExt(int processingTimeInns, double selectivity) {
		this.processingTimeInns = processingTimeInns;
		this.selectivity = selectivity;
	}
	
	public int getProcessingTimeInns() {
		return processingTimeInns;
	}
	
	public void setProcessingTimeInns(int processingTimeInns) {
		this.processingTimeInns = processingTimeInns;
	}
	
	public double getSelectivity() {
		return selectivity;
	}
	
	public void setSelectivity(double selectivity) {
		this.selectivity = selectivity;
	}

	@Override
	public SDFAttributeList getOutputSchema() {
		return getInputSchema(0);
	}
	
	@Override
	public boolean isAllPhysicalInputSet() {
		boolean set = true;
		for (int i=0;i<getNumberOfInputs() && set;i++){
			set = set && (getPhysSubscriptionTo(i) != null);
		}
		return set;
	}
	
	@Override
	public BenchmarkAOExt clone() {
		return new BenchmarkAOExt(this);
	}
}

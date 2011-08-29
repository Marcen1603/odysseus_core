package de.uniol.inf.is.odysseus.costmodel.operator;

import java.util.Map;

import de.uniol.inf.is.odysseus.costmodel.operator.datasrc.IHistogram;
import de.uniol.inf.is.odysseus.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttribute;

public class OperatorEstimation {

	private IPhysicalOperator operator;
	
	private Map<SDFAttribute, IHistogram> histograms = null;
	private IDataStream dataStream = null;
	
	private IOperatorDetailCost detailCost = null;
	private Double selectivity = null; 
	
	public OperatorEstimation(IPhysicalOperator estimatedOperator) {
		if( estimatedOperator == null )
			throw new IllegalArgumentException("estimatedOperator is null");
		
		this.operator = estimatedOperator;
	}

	public Map<SDFAttribute, IHistogram> getHistograms() {
		return histograms;
	}

	public void setHistograms(Map<SDFAttribute, IHistogram> outputHistograms) {
		this.histograms = outputHistograms;
	}

	public IDataStream getDataStream() {
		return dataStream;
	}

	public void setDataStream(IDataStream dataStream) {
		this.dataStream = dataStream;
	}

	public IOperatorDetailCost getDetailCost() {
		return detailCost;
	}

	public void setDetailCost(IOperatorDetailCost detailCost) {
		this.detailCost = detailCost;
	}

	public Double getSelectivity() {
		return selectivity;
	}

	public void setSelectivity(Double selectivity) {
		this.selectivity = selectivity;
	}

	public IPhysicalOperator getOperator() {
		return operator;
	}
	
	public boolean check() {
		if( getSelectivity() == null ) return false;
		if( getOperator() == null ) return false;
		if( getDataStream() == null ) return false;
		if( getHistograms() == null ) return false;
		if( getDataStream() == null ) return false;
		return true;
	}
}

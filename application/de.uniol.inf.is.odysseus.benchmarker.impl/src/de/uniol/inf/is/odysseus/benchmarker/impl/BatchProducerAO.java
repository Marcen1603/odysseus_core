package de.uniol.inf.is.odysseus.benchmarker.impl;

import java.util.ArrayList;
import java.util.List;

import de.uniol.inf.is.odysseus.logicaloperator.base.AbstractLogicalOperator;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttributeList;

public class BatchProducerAO extends AbstractLogicalOperator {
	private static final long serialVersionUID = 730372057392120823L;

	private ArrayList<Integer> elementCounts = new ArrayList<Integer>();
	private ArrayList<Long> frequencies = new ArrayList<Long>();
	private int invertedPriorityRatio = 0;

	public BatchProducerAO() {
	}

	@SuppressWarnings("unchecked")
	public BatchProducerAO(BatchProducerAO batchProducerAO) {
		this.invertedPriorityRatio = batchProducerAO.invertedPriorityRatio;
		this.elementCounts = (ArrayList<Integer>) batchProducerAO.elementCounts
				.clone();
		this.frequencies = (ArrayList<Long>) batchProducerAO.frequencies
				.clone();
	}

	public void addBatch(int size, long wait) {
		this.elementCounts.add(size);
		this.frequencies.add(wait);
	}

	@Override
	public AbstractLogicalOperator clone() {
		return new BatchProducerAO(this);
	}

	@Override
	public SDFAttributeList getOutputSchema() {
		return new SDFAttributeList();
	}

	public List<Integer> getElementCounts() {
		return elementCounts;
	}

	public List<Long> getFrequencies() {
		return frequencies;
	}

	public int getInvertedPriorityRatio() {
		return invertedPriorityRatio;
	}

	public void setInvertedPriorityRatio(int invertedPriorityRatio) {
		this.invertedPriorityRatio = invertedPriorityRatio;
	}
}

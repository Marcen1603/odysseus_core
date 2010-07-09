package de.uniol.inf.is.odysseus.benchmarker.impl;

import java.util.ArrayList;
import java.util.List;

import de.uniol.inf.is.odysseus.logicaloperator.base.AbstractLogicalOperator;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttributeList;

public class TestProducerAO extends AbstractLogicalOperator {
	private static final long serialVersionUID = -6067355249297203590L;
	private ArrayList<Integer> elementCounts = new ArrayList<Integer>();
	private ArrayList<Long> frequencies = new ArrayList<Long>();
	private int invertedPriorityRatio = 0;

	@Override
	public SDFAttributeList getOutputSchema() {
		return new SDFAttributeList();
	}
	
	public TestProducerAO() {
	}

	@SuppressWarnings("unchecked")
	public TestProducerAO(TestProducerAO testProducerAO) {
		this.invertedPriorityRatio = testProducerAO.invertedPriorityRatio;
		this.elementCounts = (ArrayList<Integer>) testProducerAO.elementCounts
				.clone();
		this.frequencies = (ArrayList<Long>) testProducerAO.frequencies.clone();
	}

	public void addTestPart(int size, long wait) {
		this.elementCounts.add(size);
		this.frequencies.add(wait);
	}

	@Override
	public AbstractLogicalOperator clone() {
		return new TestProducerAO(this);
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

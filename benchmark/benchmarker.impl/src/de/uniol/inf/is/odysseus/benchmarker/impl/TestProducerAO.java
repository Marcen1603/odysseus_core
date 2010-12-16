package de.uniol.inf.is.odysseus.benchmarker.impl;

import java.util.ArrayList;
import java.util.List;

import de.uniol.inf.is.odysseus.logicaloperator.AbstractLogicalOperator;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttributeList;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFDatatypeFactory;

public class TestProducerAO extends AbstractLogicalOperator {
	private static final long serialVersionUID = -6067355249297203590L;
	private ArrayList<Integer> elementCounts = new ArrayList<Integer>();
	private ArrayList<Long> frequencies = new ArrayList<Long>();
	private int invertedPriorityRatio = 0;
	
	final private SDFAttributeList outputSchema;


	@Override
	public SDFAttributeList getOutputSchema() {
		return outputSchema;
	}
	
	public TestProducerAO() {
		this.outputSchema = new SDFAttributeList();
		SDFAttribute a = new SDFAttribute("Dummy");
		a.setDatatype(SDFDatatypeFactory.getDatatype("Long"));
		outputSchema.add(a);
	}

	@SuppressWarnings("unchecked")
	public TestProducerAO(TestProducerAO testProducerAO) {
		this.invertedPriorityRatio = testProducerAO.invertedPriorityRatio;
		this.elementCounts = (ArrayList<Integer>) testProducerAO.elementCounts
				.clone();
		this.frequencies = (ArrayList<Long>) testProducerAO.frequencies.clone();
		this.outputSchema = testProducerAO.outputSchema;
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

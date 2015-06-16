package de.uniol.inf.is.odysseus.multithreaded.interoperator.parameter;

import de.uniol.inf.is.odysseus.multithreaded.interoperator.keyword.InterOperatorParallelizationPreParserKeyword.BufferSizeConstants;
import de.uniol.inf.is.odysseus.multithreaded.interoperator.keyword.InterOperatorParallelizationPreParserKeyword.DegreeOfParalleizationConstants;

public class MultithreadedOperatorSettings {

	private DegreeOfParalleizationConstants degreeConstant = DegreeOfParalleizationConstants.AUTO;
	private int degreeOfParallelization = 0;

	private BufferSizeConstants bufferSizeConstant = BufferSizeConstants.AUTO;
	private int bufferSize = 0;

	private String multithreadingStrategy = "";

	private String fragementationType = "";

	public DegreeOfParalleizationConstants getDegreeConstant() {
		return degreeConstant;
	}

	public void setDegreeConstant(DegreeOfParalleizationConstants degreeConstant) {
		this.degreeConstant = degreeConstant;
		if (degreeConstant == DegreeOfParalleizationConstants.AUTO
				|| degreeConstant == DegreeOfParalleizationConstants.GLOBAL) {
			degreeOfParallelization = 0;
		}
	}

	public int getDegreeOfParallelization() {
		return degreeOfParallelization;
	}

	public void setDegreeOfParallelization(int degreeOfParallelization) {
		this.degreeOfParallelization = degreeOfParallelization;
		if (degreeOfParallelization != 0) {
			degreeConstant = DegreeOfParalleizationConstants.USERDEFINED;
		}
	}

	public BufferSizeConstants getBufferSizeConstant() {
		return bufferSizeConstant;
	}

	public void setBufferSizeConstant(BufferSizeConstants bufferSizeConstant) {
		this.bufferSizeConstant = bufferSizeConstant;
		if (bufferSizeConstant == BufferSizeConstants.AUTO) {
			bufferSize = 0;
		}
	}

	public int getBufferSize() {
		return bufferSize;
	}

	public void setBufferSize(int bufferSize) {
		this.bufferSize = bufferSize;
		if (bufferSize != 0) {
			bufferSizeConstant = BufferSizeConstants.USERDEFINED;
		}
	}

	public String getMultithreadingStrategy() {
		return multithreadingStrategy;
	}

	public void setMultithreadingStrategy(String multithreadingStrategy) {
		this.multithreadingStrategy = multithreadingStrategy;
	}

	public String getFragementationType() {
		return fragementationType;
	}

	public void setFragementationType(String fragementationType) {
		this.fragementationType = fragementationType;
	}

}

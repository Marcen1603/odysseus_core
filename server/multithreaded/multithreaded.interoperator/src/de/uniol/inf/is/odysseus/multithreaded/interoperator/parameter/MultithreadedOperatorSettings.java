package de.uniol.inf.is.odysseus.multithreaded.interoperator.parameter;

import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.multithreaded.autodetect.PerformanceDetectionHelper;
import de.uniol.inf.is.odysseus.multithreaded.helper.FragmentationTypeHelper;
import de.uniol.inf.is.odysseus.multithreaded.interoperator.keyword.InterOperatorParallelizationPreParserKeyword.BufferSizeConstants;
import de.uniol.inf.is.odysseus.multithreaded.interoperator.keyword.InterOperatorParallelizationPreParserKeyword.DegreeOfParalleizationConstants;
import de.uniol.inf.is.odysseus.multithreaded.interoperator.strategy.IMultithreadedTransformationStrategy;
import de.uniol.inf.is.odysseus.multithreaded.keyword.ParallelizationPreParserKeyword;
import de.uniol.inf.is.odysseus.server.fragmentation.horizontal.logicaloperator.AbstractFragmentAO;

public class MultithreadedOperatorSettings {

	private DegreeOfParalleizationConstants degreeConstant = DegreeOfParalleizationConstants.GLOBAL;
	private int degreeOfParallelization = 0;

	private BufferSizeConstants bufferSizeConstant = BufferSizeConstants.GLOBAL;
	private int bufferSize = 0;

	private String multithreadingStrategy = "";

	private Class<? extends AbstractFragmentAO> fragementationType;

	public static MultithreadedOperatorSettings createDefaultSettings(
			IMultithreadedTransformationStrategy<? extends ILogicalOperator> strategy,
			int globalDegreeOfParallelization, int globalBufferSize) {
		MultithreadedOperatorSettings settings = new MultithreadedOperatorSettings();

		settings.doPostCalculationsForSettings(strategy,
				globalDegreeOfParallelization, globalBufferSize);
		return settings;
	}

	public void doPostCalculationsForSettings(
			IMultithreadedTransformationStrategy<? extends ILogicalOperator> strategy,
			int globalDegreeOfParallelization, int globalBufferSize) {
		// if GLOBAL is selected, we use the global parallelization degree
		if (getDegreeConstant() == DegreeOfParalleizationConstants.GLOBAL) {
			setDegreeOfParallelization(globalDegreeOfParallelization);
			// if AUTO is selected we detect the number of cores
		} else if (getDegreeConstant() == DegreeOfParalleizationConstants.AUTO) {
			int detectedNumberOfCores = PerformanceDetectionHelper
					.getNumberOfCores();
			setDegreeOfParallelization(detectedNumberOfCores);
		}

		// if AUTO is selected, we use an default value for buffers
		if (getBufferSizeConstant() == BufferSizeConstants.GLOBAL) {
			setBufferSize(globalBufferSize);
		} else if (getBufferSizeConstant() == BufferSizeConstants.AUTO) {
			setBufferSize(ParallelizationPreParserKeyword.AUTO_BUFFER_SIZE);
		}

		if (this.getFragementationType() != null) {
			// Validate fragmentationtype for this strategy
			boolean fragmentationTypeIsAllowed = false;
			for (Class<? extends AbstractFragmentAO> fragmentationType : strategy
					.getAllowedFragmentationTypes()) {
				if (fragmentationType == this.getFragementationType()) {
					fragmentationTypeIsAllowed = true;
				}
			}
			if (!fragmentationTypeIsAllowed) {
				throw new IllegalArgumentException("Selected strategy "
						+ this.getMultithreadingStrategy()
						+ " is not compatible with the selected fragmentationType "
						+ getFragementationType().getSimpleName());
			}
		} else {
			// get preferred fragmentation type for strategy
			setFragementationType(strategy.getPreferredFragmentationType());
		}

	}

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

	public Class<? extends AbstractFragmentAO> getFragementationType() {
		return fragementationType;
	}

	public void setFragementationType(
			Class<? extends AbstractFragmentAO> fragementationType) {
		this.fragementationType = fragementationType;
	}

	public void setFragementationType(String fragementationType) {
		if (FragmentationTypeHelper
				.isValidFragmentationType(fragementationType)) {
			this.fragementationType = FragmentationTypeHelper
					.getFragmentationTypeByName(fragementationType);
		} else {
			throw new IllegalArgumentException("Invalid fragmentation type");
		}
	}

	public boolean hasMultithreadingStrategy() {
		return !multithreadingStrategy.isEmpty();
	}

}

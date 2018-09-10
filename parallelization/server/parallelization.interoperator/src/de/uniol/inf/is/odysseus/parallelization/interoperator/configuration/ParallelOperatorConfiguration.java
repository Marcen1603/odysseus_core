/********************************************************************************** 
 * Copyright 2015 The Odysseus Team
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package de.uniol.inf.is.odysseus.parallelization.interoperator.configuration;

import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.parallelization.autodetect.PerformanceDetectionHelper;
import de.uniol.inf.is.odysseus.parallelization.helper.FragmentationTypeHelper;
import de.uniol.inf.is.odysseus.parallelization.interoperator.constants.BufferSizeConstants;
import de.uniol.inf.is.odysseus.parallelization.interoperator.constants.DegreeOfParalleizationConstants;
import de.uniol.inf.is.odysseus.parallelization.interoperator.constants.InterOperatorParallelizationConstants;
import de.uniol.inf.is.odysseus.parallelization.interoperator.strategy.IParallelTransformationStrategy;
import de.uniol.inf.is.odysseus.server.fragmentation.horizontal.logicaloperator.AbstractFragmentAO;
import de.uniol.inf.is.odysseus.server.fragmentation.horizontal.logicaloperator.AbstractStaticFragmentAO;

/**
 * Operator specific configuration which is used in InterOperator Setting. These
 * configurations are created if the #INTEROPERATOR keyword is used in odysseus
 * script
 * 
 * @author ChrisToenjesDeye
 *
 */
public class ParallelOperatorConfiguration {

	private DegreeOfParalleizationConstants degreeConstant = DegreeOfParalleizationConstants.GLOBAL;
	private int degreeOfParallelization = 0;

	private BufferSizeConstants bufferSizeConstant = BufferSizeConstants.GLOBAL;
	private int bufferSize = 0;
	private String multithreadingStrategy = "";
	private String startParallelizationId;
	private String endParallelizationId;
	private boolean useThreadedBuffer;
	private boolean assureSemanticCorrectness = false;
	private boolean useParallelOperators = false;

	private Class<? extends AbstractStaticFragmentAO> fragementationType;

	/**
	 * creates a configuration with default values. this is needed in
	 * transformation if no operator specific configuration is set
	 * 
	 * @param strategy
	 * @param globalDegreeOfParallelization
	 * @param globalBufferSize
	 * @param useThreadedBuffer
	 * @return
	 */
	public static ParallelOperatorConfiguration createDefaultConfiguration(
			IParallelTransformationStrategy<? extends ILogicalOperator> strategy,
			int globalDegreeOfParallelization, int globalBufferSize,
			boolean useThreadedBuffer) {
		ParallelOperatorConfiguration settings = new ParallelOperatorConfiguration();

		settings.doPostCalculationsForConfiguration(strategy,
				globalDegreeOfParallelization, globalBufferSize,
				useThreadedBuffer);
		return settings;
	}

	/**
	 * do post calculations before transformation is done (e.g. get the auto
	 * degree or get the global buffersize if GLOBAL constant is used). also
	 * some validations are done (e.g. if the fragmentation works with the
	 * selected strategy)
	 * 
	 * @param strategy
	 * @param globalDegreeOfParallelization
	 * @param globalBufferSize
	 * @param useThreadedBuffer
	 */
	public void doPostCalculationsForConfiguration(
			IParallelTransformationStrategy<? extends ILogicalOperator> strategy,
			int globalDegreeOfParallelization, int globalBufferSize,
			boolean useThreadedBuffer) {
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
			setBufferSize(InterOperatorParallelizationConstants.DEFAULT_BUFFER_SIZE);
		}

		// use threaded buffer
		this.useThreadedBuffer = useThreadedBuffer;

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
				throw new IllegalArgumentException(
						"Selected strategy "
								+ this.getParallelStrategy()
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
		// if the constant AUTO or GLOABL is set, set the value to zero
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
		// if a value is set, set the constant to userdefined
		if (degreeOfParallelization != 0) {
			degreeConstant = DegreeOfParalleizationConstants.USERDEFINED;
		}
	}

	public BufferSizeConstants getBufferSizeConstant() {
		return bufferSizeConstant;
	}

	public void setBufferSizeConstant(BufferSizeConstants bufferSizeConstant) {
		this.bufferSizeConstant = bufferSizeConstant;
		// if constant AUTO is set, set the value to zero
		if (bufferSizeConstant == BufferSizeConstants.AUTO) {
			bufferSize = 0;
		}
	}

	public int getBufferSize() {
		return bufferSize;
	}

	public void setBufferSize(int bufferSize) {
		this.bufferSize = bufferSize;
		// if a value is set, set the constant to userdefined
		if (bufferSize != 0) {
			bufferSizeConstant = BufferSizeConstants.USERDEFINED;
		}
	}

	public String getParallelStrategy() {
		return multithreadingStrategy;
	}

	public void setMultithreadingStrategy(String multithreadingStrategy) {
		this.multithreadingStrategy = multithreadingStrategy;
	}

	public Class<? extends AbstractStaticFragmentAO> getFragementationType() {
		return fragementationType;
	}

	public void setFragementationType(
			Class<? extends AbstractStaticFragmentAO> fragementationType) {
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

	public boolean hasParallelizationStrategy() {
		return !multithreadingStrategy.isEmpty();
	}

	public String getEndParallelizationId() {
		return endParallelizationId;
	}

	public void setEndParallelizationId(String endParallelizationId) {
		this.endParallelizationId = endParallelizationId;
	}

	public String getStartParallelizationId() {
		return startParallelizationId;
	}

	public void setStartParallelizationId(String startParallelizationId) {
		this.startParallelizationId = startParallelizationId;
	}

	public boolean isAssureSemanticCorrectness() {
		return assureSemanticCorrectness;
	}

	public void setAssureSemanticCorrectness(boolean assureSemanticCorrectness) {
		this.assureSemanticCorrectness = assureSemanticCorrectness;
	}

	public boolean isUseThreadedBuffer() {
		return useThreadedBuffer;
	}

	public void setUseThreadedBuffer(boolean useThreadedBuffer) {
		this.useThreadedBuffer = useThreadedBuffer;
	}

	public boolean isUseParallelOperators() {
		return useParallelOperators;
	}

	public void setUseParallelOperators(boolean useParallelOperators) {
		this.useParallelOperators = useParallelOperators;
	}

}

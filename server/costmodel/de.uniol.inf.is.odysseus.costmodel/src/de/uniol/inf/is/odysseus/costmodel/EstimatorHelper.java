/*******************************************************************************
 * Copyright 2012 The Odysseus Team
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
package de.uniol.inf.is.odysseus.costmodel;

import com.google.common.base.Optional;

import de.uniol.inf.is.odysseus.core.monitoring.IMonitoringData;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.server.monitoring.physicaloperator.MonitoringDataTypes;
import de.uniol.inf.is.odysseus.sweeparea.ISweepArea;

public final class EstimatorHelper {

	// privater Konstruktor
	private EstimatorHelper() {

	}

	public static Optional<Double> getDatarateMetadata(IPhysicalOperator operator) {
		if (operator.isOpen()) {
			IMonitoringData<Double> datarateMonitoringData = operator.getMonitoringData(MonitoringDataTypes.DATARATE.name);
			if (datarateMonitoringData != null) {

				Double datarate = datarateMonitoringData.getValue();
				if (datarate != null && !Double.isNaN(datarate)) {
					return Optional.of(datarate * 1000);
				}
			}
		}
		
		return Optional.absent();
	}

	public static Optional<Double> getSelectivityMetadata(IPhysicalOperator operator) {
		if (operator.isOpen()) {
			IMonitoringData<Double> selectivityMonitoringData = operator.getMonitoringData(MonitoringDataTypes.SELECTIVITY.name);
			if (selectivityMonitoringData != null) {
				Double selectivity = selectivityMonitoringData.getValue();
				if (selectivity != null && !Double.isNaN(selectivity)) {
					return Optional.of(selectivity);
				}
			}
		}

		return Optional.absent();
	}

	public static Optional<Double> getCpuTimeMetadata(IPhysicalOperator operator) {
		if (operator.isOpen()) {
			// measure directly
			IMonitoringData<Double> cpuTime = operator.getMonitoringData(MonitoringDataTypes.MEDIAN_PROCESSING_TIME.name);
			if (cpuTime != null && cpuTime.getValue() != null && !Double.isNaN(cpuTime.getValue())) {
				return Optional.of(cpuTime.getValue() / 1000000000.0);
			}
		}

		return Optional.absent();
	}

	public static int sizeInBytes(SDFSchema schema) {
		if (schema == null)
			return 0;

		int size = 0;
		for (SDFAttribute attribute : schema) {

			SDFDatatype type = attribute.getDatatype();
			if (type.isDouble())
				size += 8;
			else if (type.isFloat())
				size += 8;
			else if (type.isInteger())
				size += 4;
			else if (type.isLong())
				size += 8;
			else if (type.isEndTimestamp())
				size += 8;
			else if (type.isStartTimestamp())
				size += 8;
			else if (type.isTimestamp())
				size += 8;
			else
				size += 4; // default
		}

		return size;

	}

	public static <T> int elementCountOfSweepAreas(ISweepArea<?>[] areas) {
		if (areas == null || areas.length == 0) {
			return 0;
		}

		int sum = 0;
		for (ISweepArea<?> area : areas) {
			sum += area.size();
		}

		return sum;
	}
}

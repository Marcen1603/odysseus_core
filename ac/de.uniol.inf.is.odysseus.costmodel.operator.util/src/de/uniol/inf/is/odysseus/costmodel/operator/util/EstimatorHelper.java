package de.uniol.inf.is.odysseus.costmodel.operator.util;

import de.uniol.inf.is.odysseus.monitoring.IMonitoringData;
import de.uniol.inf.is.odysseus.monitoring.physicaloperator.MonitoringDataTypes;
import de.uniol.inf.is.odysseus.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttributeList;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFDatatype;

public final class EstimatorHelper {

	private EstimatorHelper() {

	}

	public static double getDatarateMetadata(IPhysicalOperator operator) {
		Double datarate = new Double(-1);
		try {
			if (operator.isOpen()) {
				// directly get the datarate
				IMonitoringData<Double> datarateMonitoringData = operator.getMonitoringData(MonitoringDataTypes.DATARATE.name);
				if (datarateMonitoringData != null) {

					datarate = datarateMonitoringData.getValue(); // tuples / ms

					if (datarate == null)
						return -1;

					if (Double.isNaN(datarate))
						return -1;

					if (Math.abs(datarate) < 0.000001)
						return -1;

					datarate *= 1000; // tupes per sec
				}

			}
		} catch (NullPointerException ex) {
		}
		return datarate;
	}

	public static double getSelectivityMetadata(IPhysicalOperator operator) {
		Double selectivity = -1.0;
		try {
			if (operator.isOpen()) {
				IMonitoringData<Double> selectivityMonitoringData = operator.getMonitoringData(MonitoringDataTypes.SELECTIVITY.name);
				if( selectivityMonitoringData != null ) {
					selectivity = selectivityMonitoringData.getValue();
					if (selectivity == null || Double.isNaN(selectivity))
						return -1.0;
					
				}
			}
		} catch (NullPointerException ex) {
		}
		return selectivity;
	}

	// helpers
	public static double getAvgCPUTimeMetadata(IPhysicalOperator operator) {
		double time = -1.0;
		try {
			if (operator.isOpen()) {

				// measure directly
				IMonitoringData<Double> cpuTime = operator.getMonitoringData(MonitoringDataTypes.MEDIAN_PROCESSING_TIME.name);
				if (cpuTime != null && cpuTime.getValue() != null && !Double.isNaN(cpuTime.getValue()))
					time = cpuTime.getValue() / 1000000000.0;

			}
		} catch (NullPointerException ex) {
		}

		return time;
	}

	public static int sizeInBytes(SDFAttributeList schema) {
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
}

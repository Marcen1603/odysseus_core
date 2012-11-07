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
package de.uniol.inf.is.odysseus.costmodel.operator.util;

import de.uniol.inf.is.odysseus.core.monitoring.IMonitoringData;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.server.monitoring.physicaloperator.MonitoringDataTypes;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.sa.ISweepArea;

/**
 * Diese Klasse bietet statische Methoden an, um zu einem gegebenen physischen
 * Operator verschiedenen Metadaten liefern zu können. Es ist nicht möglich, zu
 * dieser Klasse eine Instanz zu erzeugen.
 * 
 * @author Timo Michelsen
 * 
 */
public final class EstimatorHelper {

	// privater Konstruktor
	private EstimatorHelper() {

	}

	/**
	 * Liefert das Metadatum "datarate" zum gegebenen physischen Operator zurück
	 * (in Tupel pro Sekunde). Falls das Metadatum nichr vorhanden ist, oder
	 * dessen Rückgabewert NaN oder <code>null</code> ist, wird -1
	 * zurückgegeben.
	 * 
	 * @param operator
	 *            Physischer Operator, dessen Datenrate zurückgegeben werden
	 *            soll.
	 * @return Datenrate des physischen Operators, oder -1, falls das Metadatum
	 *         nicht existiert oder (noch) ungültig ist
	 */
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

	/**
	 * Liefert das Metadatum "selectivity" zum gegebenen physischen Operator
	 * zurück. Falls das Metadatum nichr vorhanden ist, oder dessen Rückgabewert
	 * NaN oder <code>null</code> ist, wird -1 zurückgegeben.
	 * 
	 * @param operator
	 *            Physischer Operator, dessen Selektivität zurückgegeben werden
	 *            soll.
	 * @return Selektivität des physischen Operators, oder -1, falls das
	 *         Metadatum nicht existiert oder (noch) ungültig ist
	 */
	public static double getSelectivityMetadata(IPhysicalOperator operator) {
		Double selectivity = -1.0;
		try {
			if (operator.isOpen()) {
				IMonitoringData<Double> selectivityMonitoringData = operator.getMonitoringData(MonitoringDataTypes.SELECTIVITY.name);
				if (selectivityMonitoringData != null) {
					selectivity = selectivityMonitoringData.getValue();
					if (selectivity == null || Double.isNaN(selectivity))
						return -1.0;

				}
			}
		} catch (NullPointerException ex) {
		}
		return selectivity;
	}

	/**
	 * Liefert das Metadatum "median_processing_time" zum gegebenen physischen
	 * Operator zurück (in Sekunden). Falls das Metadatum nichr vorhanden ist,
	 * oder dessen Rückgabewert NaN oder <code>null</code> ist, wird -1
	 * zurückgegeben.
	 * 
	 * @param operator
	 *            Physischer Operator, dessen Prozessorzeit zurückgegeben werden
	 *            soll.
	 * @return Median der Prozessorzeiten des physischen Operators, oder -1,
	 *         falls das Metadatum nicht existiert oder (noch) ungültig ist
	 */

	public static double getMedianCPUTimeMetadata(IPhysicalOperator operator) {
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

	/**
	 * Schätzt die ungefähre Größe eines Tupels auf Grundlage des Schemas ab (in
	 * Bytes).
	 * 
	 * @param schema
	 *            Schema der Tupel, dessen Speichergröße abgeschätzt werden
	 *            soll.
	 * @return Größe der Tupel in Bytes
	 */
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
	
	public static <T> int elementCountOfSweepAreas(ISweepArea<T>[] areas ) {
		if( areas == null || areas.length == 0 ) {
			return 0;
		}
		
		int sum = 0;
		for( ISweepArea<T> area : areas ) {
			sum += area.size();
		}
		
		return sum;
	}
}

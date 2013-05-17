/********************************************************************************** 
 * Copyright 2011 The Odysseus Team
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
package de.uniol.inf.is.odysseus.probabilistic;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.server.datadictionary.DataDictionaryException;
import de.uniol.inf.is.odysseus.core.server.datadictionary.IDataDictionary;
import de.uniol.inf.is.odysseus.probabilistic.sdf.schema.SDFProbabilisticDatatype;

/**
 * Datatype provider for probabilistic datatypes.
 * 
 * @author Christian Kuka <christian.kuka@offis.de>
 */
public class ProbabilisticDatatypeProvider {
	/** Logger. */
	private static Logger LOG = LoggerFactory
			.getLogger(ProbabilisticDatatypeProvider.class);
	/** The data dictionary. */
	public static IDataDictionary datadictionary = null;

	/**
	 * Bind data dictionary.
	 * 
	 * @param dataDictionary
	 *            The data dictionary
	 */
	protected void bindDataDictionary(final IDataDictionary dataDictionary) {
		ProbabilisticDatatypeProvider.datadictionary = dataDictionary;
		try {
			ProbabilisticDatatypeProvider.datadictionary.addDatatype(
					SDFProbabilisticDatatype.PROBABILISTIC_DOUBLE.getURI(),
					SDFProbabilisticDatatype.PROBABILISTIC_DOUBLE);
			ProbabilisticDatatypeProvider.datadictionary.addDatatype(
					SDFProbabilisticDatatype.PROBABILISTIC_FLOAT.getURI(),
					SDFProbabilisticDatatype.PROBABILISTIC_FLOAT);
			ProbabilisticDatatypeProvider.datadictionary.addDatatype(
					SDFProbabilisticDatatype.PROBABILISTIC_LONG.getURI(),
					SDFProbabilisticDatatype.PROBABILISTIC_LONG);
			ProbabilisticDatatypeProvider.datadictionary.addDatatype(
					SDFProbabilisticDatatype.PROBABILISTIC_INTEGER.getURI(),
					SDFProbabilisticDatatype.PROBABILISTIC_INTEGER);
			ProbabilisticDatatypeProvider.datadictionary.addDatatype(
					SDFProbabilisticDatatype.PROBABILISTIC_SHORT.getURI(),
					SDFProbabilisticDatatype.PROBABILISTIC_SHORT);
			ProbabilisticDatatypeProvider.datadictionary.addDatatype(
					SDFProbabilisticDatatype.PROBABILISTIC_BYTE.getURI(),
					SDFProbabilisticDatatype.PROBABILISTIC_BYTE);
			ProbabilisticDatatypeProvider.datadictionary.addDatatype(
					SDFProbabilisticDatatype.PROBABILISTIC_STRING.getURI(),
					SDFProbabilisticDatatype.PROBABILISTIC_STRING);
			ProbabilisticDatatypeProvider.datadictionary.addDatatype(
					SDFProbabilisticDatatype.PROBABILISTIC_CONTINUOUS_DOUBLE
							.getURI(),
					SDFProbabilisticDatatype.PROBABILISTIC_CONTINUOUS_DOUBLE);
		} catch (final DataDictionaryException e) {
			LOG.error(e.getMessage(), e);
		}
	}

	/**
	 * Unbind data dictionary.
	 * 
	 * @param dataDictionary
	 *            The data dictionary
	 */
	protected void unbindDataDictionary(final IDataDictionary dataDictionary) {
		try {
			dataDictionary
					.removeDatatype(SDFProbabilisticDatatype.PROBABILISTIC_DOUBLE
							.getURI());
			dataDictionary
					.removeDatatype(SDFProbabilisticDatatype.PROBABILISTIC_FLOAT
							.getURI());
			dataDictionary
					.removeDatatype(SDFProbabilisticDatatype.PROBABILISTIC_LONG
							.getURI());
			dataDictionary
					.removeDatatype(SDFProbabilisticDatatype.PROBABILISTIC_INTEGER
							.getURI());
			dataDictionary
					.removeDatatype(SDFProbabilisticDatatype.PROBABILISTIC_SHORT
							.getURI());
			dataDictionary
					.removeDatatype(SDFProbabilisticDatatype.PROBABILISTIC_BYTE
							.getURI());
			dataDictionary
					.removeDatatype(SDFProbabilisticDatatype.PROBABILISTIC_STRING
							.getURI());
			dataDictionary
					.removeDatatype(SDFProbabilisticDatatype.PROBABILISTIC_CONTINUOUS_DOUBLE
							.getURI());
		} catch (final DataDictionaryException e) {
			LOG.error(e.getMessage(), e);
		} finally {
			ProbabilisticDatatypeProvider.datadictionary = null;
		}
	}
}

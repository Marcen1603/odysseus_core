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
package de.uniol.inf.is.odysseus.interval;

import de.uniol.inf.is.odysseus.core.server.datadictionary.DataDictionaryException;
import de.uniol.inf.is.odysseus.core.server.datadictionary.IDataDictionary;
import de.uniol.inf.is.odysseus.interval.sdf.schema.SDFIntervalDatatype;

/**
 * @author Christian Kuka <christian.kuka@offis.de>
 */
public class IntervalDatatypeProvider {
	public static IDataDictionary datadictionary = null;

	protected void bindDataDictionary(final IDataDictionary dd) {
		IntervalDatatypeProvider.datadictionary = dd;
		try {
			IntervalDatatypeProvider.datadictionary.addDatatype(
					SDFIntervalDatatype.INTERVAL_DOUBLE.getURI(),
					SDFIntervalDatatype.INTERVAL_DOUBLE);
			IntervalDatatypeProvider.datadictionary.addDatatype(
					SDFIntervalDatatype.INTERVAL_FLOAT.getURI(),
					SDFIntervalDatatype.INTERVAL_FLOAT);
			IntervalDatatypeProvider.datadictionary.addDatatype(
					SDFIntervalDatatype.INTERVAL_BYTE.getURI(),
					SDFIntervalDatatype.INTERVAL_BYTE);
			IntervalDatatypeProvider.datadictionary.addDatatype(
					SDFIntervalDatatype.INTERVAL_SHORT.getURI(),
					SDFIntervalDatatype.INTERVAL_SHORT);
			IntervalDatatypeProvider.datadictionary.addDatatype(
					SDFIntervalDatatype.INTERVAL_INTEGER.getURI(),
					SDFIntervalDatatype.INTERVAL_INTEGER);
			IntervalDatatypeProvider.datadictionary.addDatatype(
					SDFIntervalDatatype.INTERVAL_LONG.getURI(),
					SDFIntervalDatatype.INTERVAL_LONG);
		} catch (final DataDictionaryException e) {
			e.printStackTrace();
		}
	}

	protected void unbindDataDictionary(final IDataDictionary dd) {
		try {
			dd.removeDatatype(SDFIntervalDatatype.INTERVAL_DOUBLE.getURI());
			dd.removeDatatype(SDFIntervalDatatype.INTERVAL_FLOAT.getURI());
			dd.removeDatatype(SDFIntervalDatatype.INTERVAL_BYTE.getURI());
			dd.removeDatatype(SDFIntervalDatatype.INTERVAL_SHORT.getURI());
			dd.removeDatatype(SDFIntervalDatatype.INTERVAL_INTEGER.getURI());
			dd.removeDatatype(SDFIntervalDatatype.INTERVAL_LONG.getURI());
		} catch (final DataDictionaryException e) {
			e.printStackTrace();
		}
		IntervalDatatypeProvider.datadictionary = null;
	}
}

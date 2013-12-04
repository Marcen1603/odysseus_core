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
package de.uniol.inf.is.odysseus.datatype.interval;

import java.util.ArrayList;
import java.util.List;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.core.server.datadictionary.IDatatypeProvider;
import de.uniol.inf.is.odysseus.datatype.interval.sdf.schema.SDFIntervalDatatype;

/**
 * @author Marco Grawunder
 */
public class IntervalDatatypeProvider implements IDatatypeProvider{

	@Override
	public List<SDFDatatype> getDatatypes() {
		List<SDFDatatype> ret = new ArrayList<>();
		ret.add(SDFIntervalDatatype.INTERVAL_DOUBLE);
		ret.add(SDFIntervalDatatype.INTERVAL_FLOAT);
		ret.add(SDFIntervalDatatype.INTERVAL_BYTE);
		ret.add(SDFIntervalDatatype.INTERVAL_SHORT);
		ret.add(SDFIntervalDatatype.INTERVAL_INTEGER);
		ret.add(SDFIntervalDatatype.INTERVAL_LONG);
		return ret;
	}	
}

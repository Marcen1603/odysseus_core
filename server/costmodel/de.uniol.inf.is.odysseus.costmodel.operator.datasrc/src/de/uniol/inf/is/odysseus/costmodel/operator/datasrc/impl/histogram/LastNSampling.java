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
package de.uniol.inf.is.odysseus.costmodel.operator.datasrc.impl.histogram;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class LastNSampling implements ISampling {

	private int dataCount;
	private List<Double> values = new LinkedList<Double>();
	
	public LastNSampling( int n ) {
		dataCount = n;
	}
	
	@Override
	public void addValue(double value) {
		values.add(value);
		
		// remove oldest
		if( values.size() > dataCount )
			values.remove(0);
	}

	@Override
	public List<Double> getSampledValues() {
		Collections.sort(values);
		return values;
	}

	@Override
	public int getSampleSize() {
		return dataCount;
	}

}

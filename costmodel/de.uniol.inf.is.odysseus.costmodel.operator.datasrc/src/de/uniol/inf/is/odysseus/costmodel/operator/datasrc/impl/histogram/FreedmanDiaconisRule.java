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

import java.util.List;

public class FreedmanDiaconisRule implements IIntervalCountEstimator {

	@Override
	public int estimateIntervalCount(List<Double> data) {
		int dataSize = data.size();

		// determine number of bins...
		int lowerQuartilePos = (int) (dataSize * 0.25);
		int upperQuartilePos = (int) (dataSize * 0.75);
		double min = data.get(0);
		double max = data.get(dataSize - 1);

		// iqr = interquartile range
		double iqr = data.get(upperQuartilePos) - data.get(lowerQuartilePos);

		// size of bins
		// Quelle: http://en.wikipedia.org/wiki/Freedman–Diaconis_rule
		double binSize = 0;
		if (iqr < 0.00000001) // avoid division by zero...
			binSize = max - min;
		else
			binSize = 2 * iqr * Math.pow(dataSize, -0.3);

		int result = (int) ((max - min) / binSize);
		return result > 0 ? result : 1;
	}

}

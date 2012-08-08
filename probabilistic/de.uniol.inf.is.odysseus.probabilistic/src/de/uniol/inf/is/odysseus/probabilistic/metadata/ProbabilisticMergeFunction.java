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
package de.uniol.inf.is.odysseus.probabilistic.metadata;

import de.uniol.inf.is.odysseus.core.server.metadata.IInlineMetadataMergeFunction;

/**
 * Merge function for probabilistic data streams
 * 
 * @author Christian Kuka <christian.kuka@offis.de>
 */
public class ProbabilisticMergeFunction implements
		IInlineMetadataMergeFunction<IProbabilistic> {

	@Override
	public void mergeInto(IProbabilistic result, IProbabilistic inLeft,
			IProbabilistic inRight) {
		System.arraycopy(inLeft.getProbabilities(), 0,
				result.getProbabilities(), 0, inLeft.getProbabilities().length);
		System.arraycopy(inRight.getProbabilities(), 0,
				result.getProbabilities(), inLeft.getProbabilities().length,
				inRight.getProbabilities().length);
	}

	@Override
	public ProbabilisticMergeFunction clone() {
		return new ProbabilisticMergeFunction();
	}
}

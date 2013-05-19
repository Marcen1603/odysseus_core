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

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.server.metadata.AbstractMetadataUpdater;

/**
 * @author Christian Kuka <christian.kuka@offis.de>
 */
public class ProbabilisticFactory extends AbstractMetadataUpdater<IProbabilistic, Tuple<? extends IProbabilistic>> {
	// FIXME Why do I need the matrix in the attributes? (CK)
	int[] covarianceMatrixPositions;

	public ProbabilisticFactory(final int pos) {
		this.covarianceMatrixPositions = new int[] { pos };
	}

	public ProbabilisticFactory(final int[] pos) {
		this.covarianceMatrixPositions = pos;
	}

	@Override
	public void updateMetadata(final Tuple<? extends IProbabilistic> inElem) {
		final IProbabilistic metadata = inElem.getMetadata();
		metadata.setExistence(1.0);
	}

}

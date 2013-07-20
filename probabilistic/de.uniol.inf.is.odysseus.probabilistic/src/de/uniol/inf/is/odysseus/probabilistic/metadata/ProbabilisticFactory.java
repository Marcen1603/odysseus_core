/**
 * Copyright 2013 The Odysseus Team
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
 * 
 * @author Christian Kuka <christian@kuka.cc>
 * 
 */
public class ProbabilisticFactory extends AbstractMetadataUpdater<IProbabilistic, Tuple<? extends IProbabilistic>> {
	/** The position of the attribute holding the existence probability. */
	private int existenceProbabilityPos;

	/**
	 * Creates a new {@link ProbabilisticFactory}.
	 * 
	 * @param existenceProbabilityPos
	 *            The position of the attribute
	 */
	public ProbabilisticFactory(final int existenceProbabilityPos) {
		this.existenceProbabilityPos = existenceProbabilityPos;
	}

	/*
	 * 
	 * @see de.uniol.inf.is.odysseus.core.server.metadata.IMetadataUpdater#updateMetadata(de.uniol.inf.is.odysseus.core.metadata.IStreamObject)
	 */
	@Override
	public final void updateMetadata(final Tuple<? extends IProbabilistic> inElem) {
		final IProbabilistic metadata = inElem.getMetadata();
		metadata.setExistence(((Number) inElem.getAttribute(existenceProbabilityPos)).doubleValue());
	}

}

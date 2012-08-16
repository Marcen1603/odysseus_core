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
package de.uniol.inf.is.odysseus.probabilistic.logicaloperator;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.core.metadata.PointInTime;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractPipe;
import de.uniol.inf.is.odysseus.probabilistic.metadata.IProbabilistic;

/**
 * 
 * @author Christian Kuka <christian.kuka@offis.de>
 * 
 * @param <T>
 */
public class ProbabilisticProjectPO<T extends IMetaAttribute> extends
		AbstractPipe<Tuple<T>, Tuple<T>> {
	private int[] restrictList;

	public ProbabilisticProjectPO(int[] restrictList) {
		this.restrictList = restrictList;
	}

	public ProbabilisticProjectPO(
			ProbabilisticProjectPO<T> probabilisticProjectPO) {
		super();
		int length = probabilisticProjectPO.restrictList.length;
		restrictList = new int[length];
		System.arraycopy(probabilisticProjectPO.restrictList, 0, restrictList,
				0, length);
	}

	@Override
	public de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractPipe.OutputMode getOutputMode() {
		return OutputMode.MODIFIED_INPUT;
	}

	@Override
	protected void process_next(Tuple<T> object, int port) {
		// TODO Integrate over projected attributes
		IProbabilistic probabilistic = (IProbabilistic) object.getMetadata();

		// TODO integrate/approximate over projected out attributes to calc TEP
		Tuple<T> out = object.restrict(this.restrictList, false);

		transfer(out);
	}

	@Override
	public void processPunctuation(PointInTime timestamp, int port) {
		sendPunctuation(timestamp);

	}

	@Override
	public ProbabilisticProjectPO<T> clone() {
		return new ProbabilisticProjectPO<T>(this);
	}

}

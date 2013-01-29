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
package de.uniol.inf.is.odysseus.probabilistic.physicaloperator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractPipe;
import de.uniol.inf.is.odysseus.probabilistic.base.ProbabilisticTuple;

/**
 * @author Christian Kuka <christian.kuka@offis.de>
 * @param <T>
 */
public class ProbabilisticProjectPO<T extends IMetaAttribute> extends
		AbstractPipe<ProbabilisticTuple<T>, ProbabilisticTuple<T>> {
	Logger logger = LoggerFactory.getLogger(ProbabilisticProjectPO.class);
	private final int[] restrictList;
	private final SDFSchema inputSchema;

	public ProbabilisticProjectPO(SDFSchema inputSchema,
			final int[] restrictList) {
		this.inputSchema = inputSchema;
		this.restrictList = restrictList;
	}

	public ProbabilisticProjectPO(
			final ProbabilisticProjectPO<T> probabilisticProjectPO) {
		super();
		final int length = probabilisticProjectPO.restrictList.length;
		this.inputSchema = probabilisticProjectPO.inputSchema.clone();
		this.restrictList = new int[length];
		System.arraycopy(probabilisticProjectPO.restrictList, 0,
				this.restrictList, 0, length);

	}

	@Override
	public de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractPipe.OutputMode getOutputMode() {
		return OutputMode.MODIFIED_INPUT;
	}

	@Override
	protected void process_next(final ProbabilisticTuple<T> object,
			final int port) {
		final ProbabilisticTuple<T> out = object.restrict(this.restrictList,
				false);
		this.transfer(out);
	}

	@Override
	public ProbabilisticProjectPO<T> clone() {
		return new ProbabilisticProjectPO<T>(this);
	}

}

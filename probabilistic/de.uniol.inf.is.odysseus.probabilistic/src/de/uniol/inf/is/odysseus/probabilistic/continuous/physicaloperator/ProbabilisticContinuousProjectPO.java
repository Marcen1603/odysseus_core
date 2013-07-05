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
package de.uniol.inf.is.odysseus.probabilistic.continuous.physicaloperator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractPipe;
import de.uniol.inf.is.odysseus.probabilistic.base.ProbabilisticTuple;

/**
 * Implementation of a probabilistic Project operator.
 * 
 * @author Christian Kuka <christian.kuka@offis.de>
 * @param <T>
 */
public class ProbabilisticContinuousProjectPO<T extends IMetaAttribute> extends AbstractPipe<ProbabilisticTuple<T>, ProbabilisticTuple<T>> {
	/** Logger. */
	@SuppressWarnings("unused")
	private static final Logger LOG = LoggerFactory.getLogger(ProbabilisticContinuousProjectPO.class);
	/** The positions of attributes to restrict on. */
	private final int[] restrictList;

	/**
	 * Default constructor.
	 * 
	 * @param restrictList
	 *            The positions of attributes to restrict on
	 */
	public ProbabilisticContinuousProjectPO(final int[] restrictList) {
		this.restrictList = restrictList;
	}

	/**
	 * Clone constructor.
	 * 
	 * @param probabilisticProjectPO
	 *            The object
	 */
	public ProbabilisticContinuousProjectPO(final ProbabilisticContinuousProjectPO<T> probabilisticProjectPO) {
		super();
		final int length = probabilisticProjectPO.restrictList.length;
		this.restrictList = new int[length];
		System.arraycopy(probabilisticProjectPO.restrictList, 0, this.restrictList, 0, length);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractPipe# getOutputMode()
	 */
	@Override
	public final de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractPipe.OutputMode getOutputMode() {
		return OutputMode.MODIFIED_INPUT;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractPipe# process_next(de.uniol.inf.is.odysseus.core.metadata.IStreamObject, int)
	 */
	@Override
	protected void process_next(final ProbabilisticTuple<T> object, final int port) {
		final ProbabilisticTuple<T> out = object.restrict(this.restrictList, false);
		this.transfer(out);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractPipe#clone ()
	 */
	@Override
	public final ProbabilisticContinuousProjectPO<T> clone() {
		return new ProbabilisticContinuousProjectPO<T>(this);
	}

}

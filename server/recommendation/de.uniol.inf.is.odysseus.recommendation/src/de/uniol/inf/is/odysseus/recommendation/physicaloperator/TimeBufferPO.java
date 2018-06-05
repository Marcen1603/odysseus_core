/**
 * Copyright 2015 The Odysseus Team
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
package de.uniol.inf.is.odysseus.recommendation.physicaloperator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.core.metadata.PointInTime;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPunctuation;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.TimeValueItem;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractPipe;

/**
 * @author Cornelius Ludmann
 *
 */
public class TimeBufferPO<M extends ITimeInterval> extends
		AbstractPipe<Tuple<M>, Tuple<M>> {

	protected static Logger logger = LoggerFactory
			.getLogger(TimeBufferPO.class);

	private final TimeValueItem bufferSize;
	private PointInTime currentBufferStart = null;

	/**
	 * Default constructor.
	 *
	 * @param bufferSize
	 *            The buffer size.
	 */
	public TimeBufferPO(final TimeValueItem bufferSize) {
		super();
		this.bufferSize = bufferSize;
	}

	/**
	 * Copy constructor.
	 */
	public TimeBufferPO(final TimeBufferPO<M> op) {
		super(op);
		this.bufferSize = op.bufferSize;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractPipe#
	 * getOutputMode()
	 */
	@Override
	public OutputMode getOutputMode() {
		return OutputMode.INPUT;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractPipe#
	 * process_next(de.uniol.inf.is.odysseus.core.metadata.IStreamObject, int)
	 */
	@Override
	protected void process_next(final Tuple<M> object, final int port) {

		if (this.bufferSize == null) {
			transfer(object);
		} else {

			final ITimeInterval tupleTimeInterval = object.getMetadata();

			if (this.currentBufferStart == null) {
				this.currentBufferStart = tupleTimeInterval.getStart();
			}

			final boolean isTupleOlderThanBuffer = isTupleStartOlderThanBuffer(
					tupleTimeInterval, this.currentBufferStart);

			if (isTupleOlderThanBuffer) {
				logger.error("The tuple is older than the buffer.");
			} else {

				// FIXME: time unit
				while (!isTupleStartInBuffer(tupleTimeInterval,
						this.currentBufferStart,
						this.currentBufferStart.plus(this.bufferSize.getTime()))) {
					this.currentBufferStart = this.currentBufferStart
							.plus(this.bufferSize.getTime());
				}

				final PointInTime currentBufferEnd = this.currentBufferStart
						.plus(this.bufferSize.getTime());

				final long timeToAdd = currentBufferEnd.getMainPoint()
						- object.getMetadata().getStart().getMainPoint();

				// move object forward
				object.getMetadata().setEnd(
						object.getMetadata().getEnd().plus(timeToAdd));
				object.getMetadata().setStart(
						object.getMetadata().getStart().plus(timeToAdd));

				transfer(object);
			}
		}
	}

	/**
	 * @param tupleTimeInterval
	 * @param currentBufferStart2
	 * @return
	 */
	private boolean isTupleStartOlderThanBuffer(
			final ITimeInterval tupleTimeInterval, final PointInTime bufferStart) {
		return bufferStart.after(tupleTimeInterval.getEnd());
	}

	/**
	 * @param tupleTimeInterval
	 * @param currentBufferStart2
	 * @param currentBufferEnd
	 * @return
	 */
	private boolean isTupleStartInBuffer(final ITimeInterval tupleTimeInterval,
			final PointInTime bufferStart, final PointInTime bufferEnd) {
		return bufferStart.beforeOrEquals(tupleTimeInterval.getStart())
				&& bufferEnd.after(tupleTimeInterval.getStart());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractPipe#
	 * process_close()
	 */
	@Override
	protected void process_close() {
		super.process_close();
		this.currentBufferStart = null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.uniol.inf.is.odysseus.core.physicaloperator.ISink#processPunctuation
	 * (de.uniol.inf.is.odysseus.core.physicaloperator.IPunctuation, int)
	 */
	@Override
	public void processPunctuation(final IPunctuation punctuation,
			final int port) {
		// TODO Auto-generated method stub

	}
}

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
package de.uniol.inf.is.odysseus.core.physicaloperator;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.core.metadata.PointInTime;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;

/**
 * This punctuation holds a tuple as payload.
 * 
 * @author Cornelius Ludmann
 */
public class TuplePunctuation<T extends Tuple<M>, M extends ITimeInterval>
		extends AbstractPunctuation {

	private static final long serialVersionUID = -8612322431941972753L;

	private final T tuple;

	/**
	 * Creates a new object.
	 * 
	 * @param point
	 *            The point in time as long.
	 * @param tuple
	 *            The tuple.
	 */
	public TuplePunctuation(final long point, final T tuple) {
		super(point);
		this.tuple = tuple;
	}

	/**
	 * Creates a new object.
	 * 
	 * @param p
	 *            The point in time.
	 * @param tuple
	 *            The tuple.
	 */
	public TuplePunctuation(final PointInTime p, final T tuple) {
		super(p);
		this.tuple = tuple;
	}

	/**
	 * Copy constructor.
	 * 
	 * @param tuplePunctuation
	 */
	public TuplePunctuation(final TuplePunctuation<T, M> tuplePunctuation) {
		super(tuplePunctuation.getTime());
		this.tuple = tuplePunctuation.getTuple();
	}

	/**
	 * Returns the tuple of this punctuation.
	 * 
	 * @return the tuple.
	 */
	public T getTuple() {
		return this.tuple;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.uniol.inf.is.odysseus.core.physicaloperator.AbstractPunctuation#clone
	 * ()
	 */
	@Override
	public AbstractPunctuation clone() {
		return new TuplePunctuation<T, M>(this);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.uniol.inf.is.odysseus.core.physicaloperator.AbstractPunctuation#clone
	 * (de.uniol.inf.is.odysseus.core.metadata.PointInTime)
	 */
	@Override
	public AbstractPunctuation clone(final PointInTime newTime) {
		return new TuplePunctuation<T, M>(newTime, getTuple());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("TuplePunctuation [tuple=");
		builder.append(tuple);
		builder.append(", time=");
		builder.append(getTime());
		builder.append("]");
		return builder.toString();
	}

	@Override
	public SDFSchema getSchema() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Tuple<?> getValue() {
		// TODO Auto-generated method stub
		return null;
	}


}

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
package de.uniol.inf.is.odysseus.probabilistic.continuous.datahandler;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import com.google.common.base.Preconditions;

import de.uniol.inf.is.odysseus.core.datahandler.AbstractDataHandler;
import de.uniol.inf.is.odysseus.core.datahandler.IDataHandler;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.probabilistic.continuous.datatype.ProbabilisticContinuousDouble;
import de.uniol.inf.is.odysseus.probabilistic.sdf.schema.SDFProbabilisticDatatype;

/**
 * 
 * @author Christian Kuka <christian@kuka.cc>
 * 
 */
public class ProbabilisticContinuousHandler extends AbstractDataHandler<ProbabilisticContinuousDouble> {

	/** The continuous probabilistic handler handles all distributions. */
	private static final List<String> TYPES = new ArrayList<String>();
	static {
		ProbabilisticContinuousHandler.TYPES.add(SDFProbabilisticDatatype.PROBABILISTIC_CONTINUOUS_BYTE.getURI());
		ProbabilisticContinuousHandler.TYPES.add(SDFProbabilisticDatatype.PROBABILISTIC_CONTINUOUS_SHORT.getURI());
		ProbabilisticContinuousHandler.TYPES.add(SDFProbabilisticDatatype.PROBABILISTIC_CONTINUOUS_INTEGER.getURI());
		ProbabilisticContinuousHandler.TYPES.add(SDFProbabilisticDatatype.PROBABILISTIC_CONTINUOUS_LONG.getURI());
		ProbabilisticContinuousHandler.TYPES.add(SDFProbabilisticDatatype.PROBABILISTIC_CONTINUOUS_FLOAT.getURI());
		ProbabilisticContinuousHandler.TYPES.add(SDFProbabilisticDatatype.PROBABILISTIC_CONTINUOUS_DOUBLE.getURI());
	}

	/*
	 * 
	 * @see de.uniol.inf.is.odysseus.core.datahandler.IDataHandler#readData(java.io.ObjectInputStream)
	 */
	@Override
	public final ProbabilisticContinuousDouble readData(final ObjectInputStream inputStream) throws IOException {
		Objects.requireNonNull(inputStream);
		Preconditions.checkArgument(inputStream.available() >= 4);
		final int distributionIndex = inputStream.readInt();
		return new ProbabilisticContinuousDouble(distributionIndex);
	}

	/*
	 * 
	 * @see de.uniol.inf.is.odysseus.core.datahandler.IDataHandler#readData(java.lang.String)
	 */
	@Override
	public final ProbabilisticContinuousDouble readData(final String string) {
		Objects.requireNonNull(string);
		Preconditions.checkArgument(!string.isEmpty());
		final int distributionIndex = Integer.parseInt(string);
		return new ProbabilisticContinuousDouble(distributionIndex);
	}

	/*
	 * 
	 * @see de.uniol.inf.is.odysseus.core.datahandler.IDataHandler#readData(java.nio.ByteBuffer)
	 */
	@Override
	public final ProbabilisticContinuousDouble readData(final ByteBuffer buffer) {
		Objects.requireNonNull(buffer);
		Preconditions.checkArgument(buffer.remaining() >= 4);
		final int distributionIndex = buffer.getInt();
		return new ProbabilisticContinuousDouble(distributionIndex);
	}

	/*
	 * 
	 * @see de.uniol.inf.is.odysseus.core.datahandler.IDataHandler#writeData(java.nio.ByteBuffer, java.lang.Object)
	 */
	@Override
	public final void writeData(final ByteBuffer buffer, final Object data) {
		Objects.requireNonNull(buffer);
		Objects.requireNonNull(data);
		final ProbabilisticContinuousDouble value = (ProbabilisticContinuousDouble) data;
		buffer.putInt(value.getDistribution());
	}

	/*
	 * 
	 * @see de.uniol.inf.is.odysseus.core.datahandler.IDataHandler#memSize(java.lang.Object)
	 */
	@Override
	public final int memSize(final Object attribute) {
		return Integer.SIZE / 8;
	}

	/*
	 * 
	 * @see de.uniol.inf.is.odysseus.core.datahandler.AbstractDataHandler#getInstance(de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema)
	 */
	@Override
	protected final IDataHandler<ProbabilisticContinuousDouble> getInstance(final SDFSchema schema) {
		return new ProbabilisticContinuousHandler();
	}

	/*
	 * 
	 * @see de.uniol.inf.is.odysseus.core.datahandler.AbstractDataHandler#getSupportedDataTypes()
	 */
	@Override
	public final List<String> getSupportedDataTypes() {
		return Collections.unmodifiableList(ProbabilisticContinuousHandler.TYPES);
	}

	@Override
	public final Class<?> createsType() {
		return ProbabilisticContinuousDouble.class;
	}

}

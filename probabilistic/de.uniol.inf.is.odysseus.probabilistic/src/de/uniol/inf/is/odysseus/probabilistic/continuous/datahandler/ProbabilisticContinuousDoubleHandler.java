/*
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
import java.util.List;

import de.uniol.inf.is.odysseus.core.datahandler.AbstractDataHandler;
import de.uniol.inf.is.odysseus.core.datahandler.IDataHandler;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.probabilistic.continuous.datatype.ProbabilisticContinuousDouble;

public class ProbabilisticContinuousDoubleHandler extends
		AbstractDataHandler<ProbabilisticContinuousDouble> {
	static protected List<String> types = new ArrayList<String>();
	static {
		ProbabilisticContinuousDoubleHandler.types
				.add("ProbabilisticContinuousDouble");
		ProbabilisticContinuousDoubleHandler.types
				.add("ProbabilisticContinuousFloat");
	}

	@Override
	public ProbabilisticContinuousDouble readData(
			final ObjectInputStream inputStream) throws IOException {
		final int distributionIndex = inputStream.readInt();
		return new ProbabilisticContinuousDouble(distributionIndex);
	}

	@Override
	public ProbabilisticContinuousDouble readData(final String string) {
		final int distributionIndex = Integer.parseInt(string);
		return new ProbabilisticContinuousDouble(distributionIndex);
	}

	@Override
	public ProbabilisticContinuousDouble readData(final ByteBuffer buffer) {
		final int distributionIndex = buffer.getInt();
		return new ProbabilisticContinuousDouble(distributionIndex);
	}

	@Override
	public void writeData(final ByteBuffer buffer, final Object data) {
		final ProbabilisticContinuousDouble value = (ProbabilisticContinuousDouble) data;
		buffer.putInt(value.getDistribution());
	}

	@Override
	public int memSize(final Object attribute) {
		return Integer.SIZE / 8;
	}

	@Override
	protected IDataHandler<ProbabilisticContinuousDouble> getInstance(
			final SDFSchema schema) {
		return new ProbabilisticContinuousDoubleHandler();
	}

	@Override
	public List<String> getSupportedDataTypes() {
		return ProbabilisticContinuousDoubleHandler.types;
	}

}

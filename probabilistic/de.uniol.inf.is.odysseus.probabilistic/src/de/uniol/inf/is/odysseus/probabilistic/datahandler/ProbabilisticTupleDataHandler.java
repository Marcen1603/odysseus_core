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
package de.uniol.inf.is.odysseus.probabilistic.datahandler;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import de.uniol.inf.is.odysseus.core.datahandler.AbstractDataHandler;
import de.uniol.inf.is.odysseus.core.datahandler.DataHandlerRegistry;
import de.uniol.inf.is.odysseus.core.datahandler.IDataHandler;
import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.probabilistic.base.ProbabilisticTuple;
import de.uniol.inf.is.odysseus.probabilistic.datatype.NormalDistributionMixture;
import de.uniol.inf.is.odysseus.probabilistic.sdf.schema.SDFProbabilisticDatatype;

/**
 * @author Christian Kuka <christian.kuka@offis.de>
 */
public class ProbabilisticTupleDataHandler extends
		AbstractDataHandler<ProbabilisticTuple<?>> {
	static protected List<String> types = new ArrayList<String>();
	static {
		ProbabilisticTupleDataHandler.types.add("ProbabilisticTuple");
	}

	private IDataHandler<?>[] dataHandlers = null;
	private ProbabilisticDistributionHandler probabilisticDistributionHandler = new ProbabilisticDistributionHandler();
	private int maxDistributions;

	// Default Constructor for declarative Service needed
	public ProbabilisticTupleDataHandler() {
	}

	private ProbabilisticTupleDataHandler(SDFSchema schema) {
		this.createDataHandler(schema);
	}

	public void init(SDFSchema schema) {
		if (dataHandlers == null) {
			createDataHandler(schema);
		} else {
			throw new RuntimeException(
					"ProbabilisticTupleDataHandler is immutable. Values already set");
		}
	}

	public void init(List<String> schema) {
		if (dataHandlers == null) {
			createDataHandler(schema);
		} else {
			throw new RuntimeException(
					"TupleDataHandler is immutable. Values already set");
		}
	}

	@Override
	public ProbabilisticTuple<?> readData(ByteBuffer buffer) {
		ProbabilisticTuple<?> r = null;
		synchronized (buffer) {
			Object[] attributes = new Object[dataHandlers.length];
			for (int i = 0; i < dataHandlers.length; i++) {
				attributes[i] = dataHandlers[i].readData(buffer);
			}
			NormalDistributionMixture[] distribution = new NormalDistributionMixture[this.maxDistributions];
			int distributions = 0;
			try {
			for (int i = 0; i < this.maxDistributions; i++) {
				if (buffer.hasRemaining()) {
					distribution[i] = probabilisticDistributionHandler
							.readData(buffer);
					distributions ++;
				}
			}}catch(Exception e) {
				e.printStackTrace();
			}
			r = new ProbabilisticTuple<IMetaAttribute>(attributes, false);
			r.setDistributions(Arrays.copyOfRange(distribution, 0,
					distributions));
		}
		return r;
	}

	@Override
	public ProbabilisticTuple<?> readData(ObjectInputStream inputStream)
			throws IOException {
		ProbabilisticTuple<?> r = null;
		Object[] attributes = new Object[dataHandlers.length];
		for (int i = 0; i < this.dataHandlers.length; i++) {
			attributes[i] = dataHandlers[i].readData(inputStream);
		}
		NormalDistributionMixture[] distribution = new NormalDistributionMixture[this.maxDistributions];
		int distributions = 0;
		for (int i = 0; i < this.maxDistributions; i++) {
			if (inputStream.available() > 0) {
				distribution[i] = probabilisticDistributionHandler
						.readData(inputStream);
				distributions = i;
			}
		}
		r = new ProbabilisticTuple<IMetaAttribute>(attributes, false);
		r.setDistributions(Arrays.copyOfRange(distribution, 0, distributions));
		return r;
	}

	@Override
	public ProbabilisticTuple<?> readData(String string) {
		throw new RuntimeException("Sorry. Currently not implemented");
	}

	@Override
	public ProbabilisticTuple<?> readData(String[] input) {
		ProbabilisticTuple<?> r = null;
		Object[] attributes = new Object[dataHandlers.length];
		for (int i = 0; i < attributes.length; i++) {
			attributes[i] = dataHandlers[i].readData(input[i]);
		}
		NormalDistributionMixture[] distribution = new NormalDistributionMixture[this.maxDistributions];
		int distributions = 0;
		for (int i = attributes.length; i < input.length; i++) {
			distribution[attributes.length - i] = probabilisticDistributionHandler
					.readData(input[i]);
			distributions = i;
		}
		r = new ProbabilisticTuple<IMetaAttribute>(attributes, false);
		r.setDistributions(Arrays.copyOfRange(distribution, 0, distributions));
		return r;
	}

	@Override
	public ProbabilisticTuple<?> readData(List<String> input) {
		ProbabilisticTuple<?> r = null;
		Object[] attributes = new Object[dataHandlers.length];
		for (int i = 0; i < attributes.length; i++) {
			attributes[i] = dataHandlers[i].readData(input.get(i));
		}
		NormalDistributionMixture[] distribution = new NormalDistributionMixture[this.maxDistributions];
		int distributions = 0;
		for (int i = attributes.length; i < input.size(); i++) {
			distribution[attributes.length - i] = probabilisticDistributionHandler
					.readData(input.get(i));
			distributions = i;
		}
		r = new ProbabilisticTuple<IMetaAttribute>(attributes, false);
		r.setDistributions(Arrays.copyOfRange(distribution, 0, distributions));
		return r;
	}

	@Override
	public void writeData(ByteBuffer buffer, Object data) {
		ProbabilisticTuple<?> r = (ProbabilisticTuple<?>) data;

		int size = memSize(r);

		if (size > buffer.capacity()) {
			buffer = ByteBuffer.allocate(size * 2);
		}

		synchronized (buffer) {
			for (int i = 0; i < dataHandlers.length; i++) {
				dataHandlers[i].writeData(buffer, r.getAttribute(i));
			}
			for (int i = 0; i < r.getDistributions().length; i++) {
				probabilisticDistributionHandler.writeData(buffer,
						r.getDistribution(i));
			}

		}
	}

	@Override
	public int memSize(Object attribute) {
		ProbabilisticTuple<?> r = (ProbabilisticTuple<?>) attribute;
		int size = 0;
		for (int i = 0; i < dataHandlers.length; i++) {
			size += dataHandlers[i].memSize(r.getAttribute(i));
		}
		for (int i = 0; i < r.getDistributions().length; i++) {
			size += probabilisticDistributionHandler.memSize(r
					.getDistribution(i));
		}
		return size;
	}

	@Override
	public List<String> getSupportedDataTypes() {
		return Collections
				.unmodifiableList(ProbabilisticTupleDataHandler.types);
	}

	@Override
	protected IDataHandler<ProbabilisticTuple<?>> getInstance(SDFSchema schema) {
		return new ProbabilisticTupleDataHandler(schema);
	}

	private void createDataHandler(SDFSchema schema) {
		this.dataHandlers = new IDataHandler<?>[schema.size()];
		this.maxDistributions = 0;
		int i = 0;
		for (SDFAttribute attribute : schema) {

			final SDFDatatype type = attribute.getDatatype();
			final SDFProbabilisticDatatype probabilisticType;
			if (type.getClass() == SDFProbabilisticDatatype.class) {
				probabilisticType = (SDFProbabilisticDatatype) attribute
						.getDatatype();
			} else {
				probabilisticType = null;
			}

			String uri = attribute.getDatatype().getURI(false);

			// is this really needed??
			if (type.isTuple()) {
				uri = "TUPLE";
			} else if (type.isMultiValue()) {
				uri = "MULTI_VALUE";
			}
			if (probabilisticType != null) {
				if (probabilisticType.isContinuous()) {
					maxDistributions++;
				}
			}
			if (!DataHandlerRegistry.containsDataHandler(uri)) {
				throw new IllegalArgumentException("Unregistered datatype "
						+ uri);
			}

			dataHandlers[i++] = DataHandlerRegistry.getDataHandler(uri,
					new SDFSchema("", attribute));

		}
	}

	private void createDataHandler(List<String> schema) {
		this.dataHandlers = new IDataHandler<?>[schema.size()];
		this.maxDistributions = 0;
		int i = 0;
		for (String attribute : schema) {

			IDataHandler<?> handler = DataHandlerRegistry.getDataHandler(
					attribute, (SDFSchema) null);

			if (handler == null) {
				throw new IllegalArgumentException("Unregistered datatype "
						+ attribute);
			}
			if (handler.getClass() == ProbabilisticContinuousDoubleHandler.class) {
				maxDistributions++;
			}
			this.dataHandlers[i++] = handler;
		}
	}

}

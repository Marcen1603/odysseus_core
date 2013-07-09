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

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.datahandler.AbstractDataHandler;
import de.uniol.inf.is.odysseus.core.datahandler.DataHandlerRegistry;
import de.uniol.inf.is.odysseus.core.datahandler.IDataHandler;
import de.uniol.inf.is.odysseus.core.datahandler.TupleDataHandler;
import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.probabilistic.base.ProbabilisticTuple;
import de.uniol.inf.is.odysseus.probabilistic.common.SchemaUtils;
import de.uniol.inf.is.odysseus.probabilistic.continuous.datahandler.ProbabilisticContinuousDoubleHandler;
import de.uniol.inf.is.odysseus.probabilistic.continuous.datahandler.ProbabilisticDistributionHandler;
import de.uniol.inf.is.odysseus.probabilistic.continuous.datatype.NormalDistributionMixture;
import de.uniol.inf.is.odysseus.probabilistic.continuous.datatype.ProbabilisticContinuousDouble;
import de.uniol.inf.is.odysseus.probabilistic.sdf.schema.SDFProbabilisticDatatype;

/**
 * @author Christian Kuka <christian.kuka@offis.de>
 */
public class ProbabilisticTupleDataHandler extends AbstractDataHandler<ProbabilisticTuple<?>> {
	static protected List<String> types = new ArrayList<String>();
	static {
		ProbabilisticTupleDataHandler.types.add(SDFProbabilisticDatatype.PROBABILISTIC_TUPLE.getURI());
	}

	private IDataHandler<?>[] dataHandlers = null;
	private final ProbabilisticDistributionHandler probabilisticDistributionHandler = new ProbabilisticDistributionHandler();
	private int maxDistributions;
	private boolean requiresDeepClone = false;
	private final boolean nullMode;

	// Default Constructor for declarative Service needed
	public ProbabilisticTupleDataHandler() {
		nullMode = false;
	}

	protected ProbabilisticTupleDataHandler(boolean nullMode) {
		this.nullMode = nullMode;
	}

	protected ProbabilisticTupleDataHandler(final SDFSchema schema, boolean nullMode) {
		this.nullMode = nullMode;
		this.createDataHandler(schema);
	}

	@Override
	public IDataHandler<ProbabilisticTuple<?>> getInstance(SDFSchema schema) {
		return new ProbabilisticTupleDataHandler(schema, false);
	}

	@Override
	public IDataHandler<ProbabilisticTuple<?>> getInstance(List<String> schema) {
		ProbabilisticTupleDataHandler handler = new ProbabilisticTupleDataHandler(false);
		handler.init(schema);
		return handler;
	}

	public void init(final SDFSchema schema) {
		if (this.dataHandlers == null) {
			this.createDataHandler(schema);
		} else {
			throw new RuntimeException("ProbabilisticTupleDataHandler is immutable. Values already set");
		}
	}

	public void init(final List<String> schema) {
		if (this.dataHandlers == null) {
			this.createDataHandler(schema);
		} else {
			throw new RuntimeException("ProbabilisticTupleDataHandler is immutable. Values already set");
		}
	}

	@Override
	public ProbabilisticTuple<?> readData(final ByteBuffer buffer) {
		ProbabilisticTuple<?> r = null;
		synchronized (buffer) {
			final Object[] attributes = new Object[this.dataHandlers.length];
			for (int i = 0; i < this.dataHandlers.length; i++) {
				byte type = -1;
				if (nullMode) {
					type = buffer.get();
				}
				if (!nullMode || type != 0) {
					attributes[i] = this.dataHandlers[i].readData(buffer);
				}
			}
			final NormalDistributionMixture[] distribution = new NormalDistributionMixture[this.maxDistributions];
			int distributions = 0;
			try {
				for (int i = 0; i < this.maxDistributions; i++) {
					if (buffer.hasRemaining()) {
						distribution[i] = this.probabilisticDistributionHandler.readData(buffer);
						distributions++;
					}
				}
			} catch (final Exception e) {
				e.printStackTrace();
			}
			// Reverse mapping of attribute<->distribution
			final int[] distributionsDimensions = new int[distributions];
			for (final SDFAttribute attr : this.getSchema().getAttributes()) {
				if (SchemaUtils.isContinuousProbabilisticAttribute(attr)) {
					final int attributeIndex = this.getSchema().indexOf(attr);
					final int distributionIndex = ((ProbabilisticContinuousDouble) attributes[attributeIndex]).getDistribution();
					distribution[distributionIndex].setAttribute(distributionsDimensions[distributionIndex], attributeIndex);
					distributionsDimensions[distributionIndex]++;
				}
			}
			r = new ProbabilisticTuple<IMetaAttribute>(attributes, this.requiresDeepClone);
			r.setDistributions(Arrays.copyOfRange(distribution, 0, distributions));
		}
		return r;
	}

	@Override
	public ProbabilisticTuple<?> readData(final ObjectInputStream inputStream) throws IOException {
		ProbabilisticTuple<?> r = null;
		final Object[] attributes = new Object[this.dataHandlers.length];
		for (int i = 0; i < this.dataHandlers.length; i++) {
			attributes[i] = this.dataHandlers[i].readData(inputStream);
		}
		final NormalDistributionMixture[] distribution = new NormalDistributionMixture[this.maxDistributions];
		int distributions = 0;
		for (int i = 0; i < this.maxDistributions; i++) {
			if (inputStream.available() > 0) {
				distribution[i] = this.probabilisticDistributionHandler.readData(inputStream);
				distributions = i;
			}
		}
		r = new ProbabilisticTuple<IMetaAttribute>(attributes, this.requiresDeepClone);
		r.setDistributions(Arrays.copyOfRange(distribution, 0, distributions));
		return r;
	}

	@Override
	public ProbabilisticTuple<?> readData(final String string) {
		throw new RuntimeException("Sorry. Currently not implemented");
	}

	@Override
	public ProbabilisticTuple<?> readData(final String[] input) {
		ProbabilisticTuple<?> r = null;
		final Object[] attributes = new Object[this.dataHandlers.length];
		for (int i = 0; i < attributes.length; i++) {
			attributes[i] = this.dataHandlers[i].readData(input[i]);
		}
		final NormalDistributionMixture[] distribution = new NormalDistributionMixture[this.maxDistributions];
		int distributions = 0;
		for (int i = attributes.length; i < input.length; i++) {
			distribution[attributes.length - i] = this.probabilisticDistributionHandler.readData(input[i]);
			distributions = i;
		}
		r = new ProbabilisticTuple<IMetaAttribute>(attributes, this.requiresDeepClone);
		r.setDistributions(Arrays.copyOfRange(distribution, 0, distributions));
		return r;
	}

	@Override
	public ProbabilisticTuple<?> readData(final List<String> input) {
		ProbabilisticTuple<?> r = null;
		final Object[] attributes = new Object[this.dataHandlers.length];
		for (int i = 0; i < attributes.length; i++) {
			attributes[i] = this.dataHandlers[i].readData(input.get(i));
		}
		final NormalDistributionMixture[] distribution = new NormalDistributionMixture[this.maxDistributions];
		int distributions = 0;
		for (int i = attributes.length; i < input.size(); i++) {
			distribution[attributes.length - i] = this.probabilisticDistributionHandler.readData(input.get(i));
			distributions = i;
		}
		r = new ProbabilisticTuple<IMetaAttribute>(attributes, this.requiresDeepClone);
		r.setDistributions(Arrays.copyOfRange(distribution, 0, distributions));
		return r;
	}

	@Override
	public void writeData(ByteBuffer buffer, final Object data) {
		final ProbabilisticTuple<?> r = (ProbabilisticTuple<?>) data;

		final int size = this.memSize(r);

		if (size > buffer.capacity()) {
			buffer = ByteBuffer.allocate(size * 2);
		}

		synchronized (buffer) {
			for (int i = 0; i < this.dataHandlers.length; i++) {
				this.dataHandlers[i].writeData(buffer, r.getAttribute(i));
			}
			for (int i = 0; i < r.getDistributions().length; i++) {
				this.probabilisticDistributionHandler.writeData(buffer, r.getDistribution(i));
			}

		}
	}

	@Override
	public int memSize(final Object attribute) {
		final ProbabilisticTuple<?> r = (ProbabilisticTuple<?>) attribute;
		int size = 0;
		for (int i = 0; i < this.dataHandlers.length; i++) {
			size += this.dataHandlers[i].memSize(r.getAttribute(i));
		}
		for (int i = 0; i < r.getDistributions().length; i++) {
			size += this.probabilisticDistributionHandler.memSize(r.getDistribution(i));
		}
		return size;
	}

	@Override
	public List<String> getSupportedDataTypes() {
		return Collections.unmodifiableList(ProbabilisticTupleDataHandler.types);
	}

	private void createDataHandler(final SDFSchema schema) {
		this.dataHandlers = new IDataHandler<?>[schema.size()];
		this.maxDistributions = 0;
		int i = 0;
		for (final SDFAttribute attribute : schema) {

			final SDFDatatype type = attribute.getDatatype();
			final SDFProbabilisticDatatype probabilisticType;
			if (type.getClass() == SDFProbabilisticDatatype.class) {
				probabilisticType = (SDFProbabilisticDatatype) attribute.getDatatype();
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
				this.requiresDeepClone = true;
				if (probabilisticType.isContinuous()) {
					this.maxDistributions++;
				}
			}
			if (!DataHandlerRegistry.containsDataHandler(uri)) {
				throw new IllegalArgumentException("Unregistered datatype " + uri);
			}

			this.dataHandlers[i++] = DataHandlerRegistry.getDataHandler(uri, new SDFSchema("", attribute));

		}
	}

	private void createDataHandler(final List<String> schema) {
		this.dataHandlers = new IDataHandler<?>[schema.size()];
		this.requiresDeepClone = true;
		this.maxDistributions = 0;
		int i = 0;
		for (final String attribute : schema) {

			final IDataHandler<?> handler = DataHandlerRegistry.getDataHandler(attribute, (SDFSchema) null);

			if (handler == null) {
				throw new IllegalArgumentException("Unregistered datatype " + attribute);
			}
			if (handler.getClass() == ProbabilisticContinuousDoubleHandler.class) {
				this.maxDistributions++;
			}
			this.dataHandlers[i++] = handler;
		}
	}

}

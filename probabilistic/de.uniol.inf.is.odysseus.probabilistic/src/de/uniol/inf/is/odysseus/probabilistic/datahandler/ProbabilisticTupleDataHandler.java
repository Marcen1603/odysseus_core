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
import java.nio.BufferOverflowException;
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
import de.uniol.inf.is.odysseus.probabilistic.common.SchemaUtils;
import de.uniol.inf.is.odysseus.probabilistic.continuous.datahandler.ProbabilisticContinuousHandler;
import de.uniol.inf.is.odysseus.probabilistic.continuous.datahandler.ProbabilisticDistributionHandler;
import de.uniol.inf.is.odysseus.probabilistic.continuous.datatype.NormalDistributionMixture;
import de.uniol.inf.is.odysseus.probabilistic.continuous.datatype.ProbabilisticContinuousDouble;
import de.uniol.inf.is.odysseus.probabilistic.sdf.schema.SDFProbabilisticDatatype;

/**
 * @author Christian Kuka <christian@kuka.cc>
 */
public class ProbabilisticTupleDataHandler extends AbstractDataHandler<ProbabilisticTuple<?>> {
	/**
	 * Supported data types.
	 */
	protected static final List<String> TYPES = new ArrayList<String>();
	static {
		ProbabilisticTupleDataHandler.TYPES.add(SDFProbabilisticDatatype.PROBABILISTIC_TUPLE.getURI());
	}

	/** The data handlers. */
	private IDataHandler<?>[] dataHandlers = null;
	/** The distribution handler. */
	private final ProbabilisticDistributionHandler probabilisticDistributionHandler = new ProbabilisticDistributionHandler();
	/** Maximum number of distributions in one probabilistic tuple. */
	private int maxDistributions;
	/** Flag indicating whether deep clone is required. */
	private boolean requiresDeepClone = false;
	/** Flag indicating whether null values are supported. */
	private final boolean nullMode;

	/**
	 * Default Constructor for declarative Service needed.
	 */
	public ProbabilisticTupleDataHandler() {
		this.nullMode = false;
	}

	/**
	 * Creates a new {@link ProbabilisticTuple} data handler.
	 * 
	 * @param nullMode
	 *            Flag indicating whether null values should be supported
	 */
	protected ProbabilisticTupleDataHandler(final boolean nullMode) {
		this.nullMode = nullMode;
	}

	/**
	 * Creates a new {@link ProbabilisticTuple} data handler with the given schema.
	 * 
	 * @param schema
	 *            The schema
	 * @param nullMode
	 *            Flag indicating whether null values should be supported
	 */
	protected ProbabilisticTupleDataHandler(final SDFSchema schema, final boolean nullMode) {
		this.nullMode = nullMode;
		this.createDataHandler(schema);
	}

	/*
	 * 
	 * @see de.uniol.inf.is.odysseus.core.datahandler.AbstractDataHandler#getInstance(de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema)
	 */
	@Override
	public final IDataHandler<ProbabilisticTuple<?>> getInstance(final SDFSchema schema) {
		return new ProbabilisticTupleDataHandler(schema, false);
	}

	/*
	 * 
	 * @see de.uniol.inf.is.odysseus.core.datahandler.AbstractDataHandler#getInstance(java.util.List)
	 */
	@Override
	public final IDataHandler<ProbabilisticTuple<?>> getInstance(final List<String> schema) {
		final ProbabilisticTupleDataHandler handler = new ProbabilisticTupleDataHandler(false);
		handler.init(schema);
		return handler;
	}

	/**
	 * Initialize the data handler.
	 * 
	 * @param schema
	 *            The schema
	 */
	public final void init(final SDFSchema schema) {
		if (this.dataHandlers == null) {
			this.createDataHandler(schema);
		} else {
			throw new RuntimeException("ProbabilisticTupleDataHandler is immutable. Values already set");
		}
	}

	/**
	 * Initialize the data handlers.
	 * 
	 * @param schema
	 *            The schema
	 */
	public final void init(final List<String> schema) {
		if (this.dataHandlers == null) {
			this.createDataHandler(schema);
		} else {
			throw new RuntimeException("ProbabilisticTupleDataHandler is immutable. Values already set");
		}
	}

	/*
	 * 
	 * @see de.uniol.inf.is.odysseus.core.datahandler.IDataHandler#readData(java.nio.ByteBuffer)
	 */
	@Override
	public final ProbabilisticTuple<?> readData(final ByteBuffer buffer) {
		ProbabilisticTuple<?> r = null;
		synchronized (buffer) {
			final Object[] attributes = new Object[this.dataHandlers.length];
			for (int i = 0; i < this.dataHandlers.length; i++) {
				byte type = -1;
				if (this.nullMode) {
					type = buffer.get();
				}
				if (!this.nullMode || (type != 0)) {
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

	/*
	 * 
	 * @see de.uniol.inf.is.odysseus.core.datahandler.IDataHandler#readData(java.io.ObjectInputStream)
	 */
	@Override
	public final ProbabilisticTuple<?> readData(final ObjectInputStream inputStream) throws IOException {
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

	/*
	 * 
	 * @see de.uniol.inf.is.odysseus.core.datahandler.IDataHandler#readData(java.lang.String)
	 */
	@Override
	public final ProbabilisticTuple<?> readData(final String string) {
		return readData(new String[] { string });
	}

	/*
	 * 
	 * @see de.uniol.inf.is.odysseus.core.datahandler.AbstractDataHandler#readData(java.lang.String[])
	 */
	@Override
	public final ProbabilisticTuple<?> readData(final String[] input) {
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

	/*
	 * 
	 * @see de.uniol.inf.is.odysseus.core.datahandler.AbstractDataHandler#readData(java.util.List)
	 */
	@Override
	public final ProbabilisticTuple<?> readData(final List<String> input) {
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

	/*
	 * 
	 * @see de.uniol.inf.is.odysseus.core.datahandler.IDataHandler#writeData(java.nio.ByteBuffer, java.lang.Object)
	 */
	@Override
	public final void writeData(final ByteBuffer buffer, final Object data) {
		final ProbabilisticTuple<?> r = (ProbabilisticTuple<?>) data;

		final int size = this.memSize(r);

		if (size > buffer.capacity()) {
			throw new BufferOverflowException();
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

	/*
	 * 
	 * @see de.uniol.inf.is.odysseus.core.datahandler.IDataHandler#memSize(java.lang.Object)
	 */
	@Override
	public final int memSize(final Object attribute) {
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

	/*
	 * 
	 * @see de.uniol.inf.is.odysseus.core.datahandler.AbstractDataHandler#getSupportedDataTypes()
	 */
	@Override
	public final List<String> getSupportedDataTypes() {
		return Collections.unmodifiableList(ProbabilisticTupleDataHandler.TYPES);
	}

	/**
	 * Creates a new probabilistic tuple data handler.
	 * 
	 * @param schema
	 *            The schema
	 */
	private void createDataHandler(final SDFSchema schema) {
		if (schema == null) return;
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

			this.dataHandlers[i++] = DataHandlerRegistry.getDataHandler(uri, new SDFSchema("", ProbabilisticTuple.class, attribute));

		}
	}

	/**
	 * Creates new probabilistic tuple data handlers.
	 * 
	 * @param schema
	 *            The schema
	 */
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
			if (handler.getClass() == ProbabilisticContinuousHandler.class) {
				this.maxDistributions++;
			}
			this.dataHandlers[i++] = handler;
		}
	}

	@Override
	public final Class<?> createsType() {
		return ProbabilisticTuple.class;
	}
}

/********************************************************************************** 
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
package de.uniol.inf.is.odysseus.server.fragmentation.horizontal.physicaloperator;

import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

import com.google.common.hash.HashFunction;
import com.google.common.hash.Hashing;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;
import de.uniol.inf.is.odysseus.core.physicaloperator.AbstractPhysicalSubscription;
import de.uniol.inf.is.odysseus.core.physicaloperator.ControllablePhysicalSubscription;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.core.physicaloperator.ISink;
import de.uniol.inf.is.odysseus.core.physicaloperator.ISource;
import de.uniol.inf.is.odysseus.core.physicaloperator.OpenFailedException;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.server.fragmentation.horizontal.logicaloperator.ConsistentHashFragmentAO;

/**
 * @author Christian Kuka <christian@kuka.cc>
 * @version $Id$
 *
 */
public class ConsistentHashFragmentPO<T extends IStreamObject<IMetaAttribute>> extends AbstractDynamicFragmentPO<T> {

	private final SortedMap<Long, AbstractPhysicalSubscription<?, ISink<IStreamObject<?>>>> circle = new TreeMap<>();
	private final HashFunction hashFunction = Hashing.murmur3_128();
	private final int[] indices;

	/**
	 * 
	 * Class constructor.
	 *
	 * @param fragmentAO
	 */
	public ConsistentHashFragmentPO(ConsistentHashFragmentAO fragmentAO) {
		super(fragmentAO);
		List<SDFAttribute> fragmentAttributes = fragmentAO.getAttributes();
		if (fragmentAttributes != null) {
			indices = new int[fragmentAO.getAttributes().size()];
			int i = 0;
			SDFSchema inputSchema = fragmentAO.getInputSchema();
			for (SDFAttribute restrictAttribute : fragmentAO.getAttributes()) {
				int pos = inputSchema.indexOf(restrictAttribute);
				if (pos == -1) {
					throw new IllegalArgumentException("Attribute " + restrictAttribute + " not found in input schema");
				}
				indices[i++] = pos;
			}
		} else {
			indices = null;
		}

	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void process_open() throws OpenFailedException {
		for (AbstractPhysicalSubscription<?, ISink<IStreamObject<?>>> subscription : getSubscriptions()) {
			circle.put(getHashCode(subscription.getSink().getName() + subscription.getSourceOutPort()), subscription);
		}
		super.process_open();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void disconnectSink(ISink<IStreamObject<?>> sink, int sinkInPort, int sourceOutPort, SDFSchema schema) {
		circle.remove(getHashCode(sink.getName() + sourceOutPort));
		super.disconnectSink(sink, sinkInPort, sourceOutPort, schema);
	}

	/**
     * {@inheritDoc}
     */
    @Override
    public void connectSink(ISink<IStreamObject<?>> sink, int sinkInPort, int sourceOutPort, SDFSchema schema) {
        super.connectSink(sink, sinkInPort, sourceOutPort, schema);
		@SuppressWarnings("unchecked")
		AbstractPhysicalSubscription<ISource<IStreamObject<?>>, ISink<IStreamObject<?>>> sub = new ControllablePhysicalSubscription<ISource<IStreamObject<?>>, ISink<IStreamObject<?>>>(
				(ISource<IStreamObject<?>>) this, sink, sinkInPort, sourceOutPort, schema);
        circle.put(getHashCode(sink.getName() + sourceOutPort), sub);
    }

	/**
	 * 
	 * {@inheritDoc}
	 */
	@Override
	protected int route(IStreamObject<IMetaAttribute> object) {
		return get(object).getSourceOutPort();
	}

	/**
	 * Gets the subscription for the given object
	 * 
	 * @param object
	 * @return The subscription
	 */
	private AbstractPhysicalSubscription<?,ISink<IStreamObject<?>>> get(IStreamObject<IMetaAttribute> object) {
		if (circle.isEmpty()) {
			return getSubscriptions().size() > 0 ? getSubscriptions().get(0) : null;
		}

		long hash = getHashCode(((Tuple<IMetaAttribute>) object).getAttributes());
		if (!circle.containsKey(hash)) {
			SortedMap<Long, AbstractPhysicalSubscription<?,ISink<IStreamObject<?>>>> tailMap = circle.tailMap(hash);
			hash = tailMap.isEmpty() ? circle.firstKey() : tailMap.firstKey();
		}
		return circle.get(hash);
	}

	private long getHashCode(String value) {
		return hashFunction.hashBytes(value.getBytes()).asLong();
	}

	private long getHashCode(Object[] attributes) {
		long ret = 0;
		for (int i = 0; i < indices.length; i++) {
			Object o = attributes[indices[i]];
			if (o != null) {
				ret += hashFunction.hashBytes(o.toString().getBytes()).asLong();
			}
		}
		return ret;
	}

	/**
	 * 
	 * {@inheritDoc}
	 */
	@Override
	public boolean isSemanticallyEqual(IPhysicalOperator ipo) {
		if (!(ipo instanceof ConsistentHashFragmentPO)) {
			return false;
		}

		@SuppressWarnings("unchecked")
		ConsistentHashFragmentPO<T> po = (ConsistentHashFragmentPO<T>) ipo;

		if (this.indices != null && po.indices == null) {
			return false;
		}

		if (this.indices == null && po.indices != null) {
			return false;
		}
		if (this.indices != null && po.indices != null) {
			if (this.indices.length != po.indices.length) {
				return false;
			}
			for (int i = 0; i < this.indices.length; i++) {
				if (this.indices[i] != po.indices[i]) {
					return false;
				}
			}
		}

		return super.isSemanticallyEqual(ipo);
	}
}

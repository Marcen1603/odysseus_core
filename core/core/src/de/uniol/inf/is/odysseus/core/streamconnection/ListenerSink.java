/*******************************************************************************
 * Copyright 2012 The Odysseus Team
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/

package de.uniol.inf.is.odysseus.core.streamconnection;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import com.google.common.collect.Lists;

import sun.awt.util.IdentityArrayList;

import de.uniol.inf.is.odysseus.core.event.IEvent;
import de.uniol.inf.is.odysseus.core.event.IEventListener;
import de.uniol.inf.is.odysseus.core.event.IEventType;
import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;
import de.uniol.inf.is.odysseus.core.monitoring.IMonitoringData;
import de.uniol.inf.is.odysseus.core.monitoring.IPeriodicalMonitoringData;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.core.physicaloperator.ISink;
import de.uniol.inf.is.odysseus.core.physicaloperator.ISource;
import de.uniol.inf.is.odysseus.core.physicaloperator.PhysicalSubscription;
import de.uniol.inf.is.odysseus.core.planmanagement.IOperatorOwner;
import de.uniol.inf.is.odysseus.core.planmanagement.OperatorOwnerComparator;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;

public abstract class ListenerSink<In extends IStreamObject<?>> implements ISink<In>, IStreamConnection<In>{

	final transient protected List<IOperatorOwner> owners = new IdentityArrayList<IOperatorOwner>();

	@Override
	public DefaultStreamConnection<In> clone() {
		throw new RuntimeException("Clone Not implemented for DefaultStreamConnection");
	}
	
	@Override
	public final boolean isSource() {
		return false;
	}

	@Override
	public final boolean isSink() {
		return true;
	}

	@Override
	public final boolean isPipe() {
		return isSink() && isSource();
	}

	@Override
	public boolean isSemanticallyEqual(IPhysicalOperator ipo) {
		return false;
	}

	@Override
	public String getName() {
		return "DefaultStreamConnection";
	}

	@Override
	public void setName(String name) {
	}

	@Override
	public SDFSchema getOutputSchema() {
		return getOutputSchema(0);
	}

	@Override
	public SDFSchema getOutputSchema(int port) {
		return null;
	}

	@Override
	public final Map<Integer, SDFSchema> getOutputSchemas() {
		return null;
	}

	@Override
	public void setOutputSchema(SDFSchema outputSchema) {
	}

	@Override
	public void setOutputSchema(SDFSchema outputSchema, int port) {
	}
	
	// ------------------------------------------------------------------------
	// Owner Management
	// ------------------------------------------------------------------------

	@Override
	public void addOwner(IOperatorOwner owner) {
		if (!this.owners.contains(owner)) {
			this.owners.add(owner);
		}
		Collections.sort(owners, OperatorOwnerComparator.getInstance());

	}

	@Override
	public void addOwner(Collection<IOperatorOwner> owner) {
		this.owners.addAll(owner);
		Collections.sort(owners, OperatorOwnerComparator.getInstance());
	}

	@Override
	public void removeOwner(IOperatorOwner owner) {
		while (this.owners.remove(owner)) {
			// Remove all owners
		}
		Collections.sort(owners, OperatorOwnerComparator.getInstance());
	}

	@Override
	public void removeAllOwners() {
		this.owners.clear();
	}

	@Override
	final public boolean isOwnedBy(IOperatorOwner owner) {
		return this.owners.contains(owner);
	}

	@Override
	final public boolean hasOwner() {
		return !this.owners.isEmpty();
	}

	@Override
	final public List<IOperatorOwner> getOwner() {
		return Collections.unmodifiableList(this.owners);
	}

	/**
	 * Returns a ","-separated string of the owner IDs.
	 * 
	 * @param owner
	 *            Owner which have IDs.
	 * @return ","-separated string of the owner IDs.
	 */
	@Override
	public String getOwnerIDs() {
		StringBuffer result = new StringBuffer();
		for (IOperatorOwner iOperatorOwner : owners) {
			if (result.length() > 0) {
				result.append(", ");
			}
			result.append(iOperatorOwner.getID());
		}
		return result.toString();
	}
	@Override
	public Collection<String> getProvidedMonitoringData() {
		return Lists.newArrayList();
	}

	@Override
	public boolean providesMonitoringData(String type) {
		return false;
	}

	@Override
	public <T> IMonitoringData<T> getMonitoringData(String type) {
		return null;
	}

	@Override
	public void createAndAddMonitoringData(@SuppressWarnings("rawtypes") IPeriodicalMonitoringData item, long period) {
	}

	@Override
	public void addMonitoringData(String type, IMonitoringData<?> item) {
	}

	@Override
	public void removeMonitoringData(String type) {
	}

	@Override
	public void subscribe(IEventListener listener, IEventType type) {
	}

	@Override
	public void unsubscribe(IEventListener listener, IEventType type) {
	}

	@Override
	public void subscribeToAll(IEventListener listener) {
	}

	@Override
	public void unSubscribeFromAll(IEventListener listener) {
	}

	@Override
	public void fire(IEvent<?, ?> event) {
	}

	@Override
	public void subscribeToSource(ISource<? extends In> source, int sinkInPort, int sourceOutPort, SDFSchema schema) {
	}

	@Override
	public void unsubscribeFromSource(PhysicalSubscription<ISource<? extends In>> subscription) {
	}

	@Override
	public void unsubscribeFromAllSources() {
	}

	@Override
	public void unsubscribeFromSource(ISource<? extends In> source, int sinkInPort, int sourceOutPort, SDFSchema schema) {
	}

	@Override
	public Collection<PhysicalSubscription<ISource<? extends In>>> getSubscribedToSource() {
		return Lists.newArrayList();
	}

	@Override
	public PhysicalSubscription<ISource<? extends In>> getSubscribedToSource(int i) {
		return null;
	}
	
	@Override
	public void process(Collection<? extends In> object, int port) {
		for (In obj : object) {
			process(obj, port);
		}
	}

	@Override
	public void done(int port) {
	}
	
}

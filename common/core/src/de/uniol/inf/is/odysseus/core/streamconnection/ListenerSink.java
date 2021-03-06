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
import java.util.Map;

import com.google.common.collect.Lists;

import de.uniol.inf.is.odysseus.core.event.IEvent;
import de.uniol.inf.is.odysseus.core.event.IEventListener;
import de.uniol.inf.is.odysseus.core.event.IEventType;
import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;
import de.uniol.inf.is.odysseus.core.monitoring.IMonitoringData;
import de.uniol.inf.is.odysseus.core.monitoring.IPeriodicalMonitoringData;
import de.uniol.inf.is.odysseus.core.physicaloperator.AbstractPhysicalSubscription;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.core.physicaloperator.ISink;
import de.uniol.inf.is.odysseus.core.physicaloperator.ISource;
import de.uniol.inf.is.odysseus.core.planmanagement.OwnerHandler;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;

public abstract class ListenerSink<In extends IStreamObject<?>> extends OwnerHandler
		implements ISink<In>, IStreamConnection<In> {

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
	public void subscribeToSource(ISource<IStreamObject<?>> source, int sinkInPort, int sourceOutPort,
			SDFSchema schema) {
	}

	@Override
	public void unsubscribeFromSource(AbstractPhysicalSubscription<ISource<IStreamObject<?>>, ?> subscription) {
	}

	@Override
	public void unsubscribeFromAllSources() {
	}

	@Override
	public void unsubscribeFromSource(ISource<IStreamObject<?>> source, int sinkInPort, int sourceOutPort,
			SDFSchema schema) {
	}

	@Override
	public Collection<AbstractPhysicalSubscription<ISource<IStreamObject<?>>, ?>> getSubscribedToSource() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public AbstractPhysicalSubscription<ISource<IStreamObject<?>>, ?> getSubscribedToSource(int i) {
		// TODO Auto-generated method stub
		return null;
	}

	// @Override
	// public void process(Collection<? extends In> object, int port) {
	// for (In obj : object) {
	// process(obj, port);
	// }
	// }

	@Override
	public void done(int port) {
	}

}

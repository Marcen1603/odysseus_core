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
package de.uniol.inf.is.odysseus.costmodel.operator.datasrc.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Optional;

import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPunctuation;
import de.uniol.inf.is.odysseus.core.physicaloperator.ISink;
import de.uniol.inf.is.odysseus.core.physicaloperator.ISource;
import de.uniol.inf.is.odysseus.core.physicaloperator.PhysicalSubscription;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractSink;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.MetadataCreationPO;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.MetadataUpdatePO;

public class DataSourceObserverSink extends AbstractSink<IStreamObject<?>> {
	
	private static Logger _logger = null;

	protected static Logger getLogger() {
		if (_logger == null) {
			_logger = LoggerFactory.getLogger(DataSourceObserverSink.class);
		}
		return _logger;
	}

	private final ISource<? extends IStreamObject<?>> source;
	private final List<IDataSourceObserverListener> listeners = new ArrayList<IDataSourceObserverListener>();

	public DataSourceObserverSink( ISource<? extends IStreamObject<?>> source ) {
		this.source = source;
		connect();
	}
	
	@Override
	public void processPunctuation(IPunctuation punctuation, int port) {
		firePuntuationElementRecieveEvent(punctuation, port);
	}

	@Override
	protected void process_next(IStreamObject<?> object, int port) {
		fireStreamElementRecieveEvent(object, port);
	}

	@Override
	public AbstractSink<IStreamObject<?>> clone() {
		return null;
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void connect() {
		Optional<ISource> connectingSource = getMetadataUpdatePOAsSource(source);
		if( connectingSource.isPresent() ) {
			connectingSource.get().connectSink((ISink) this, 0, 0, connectingSource.get().getOutputSchema());
			getLogger().debug("Source {} connected", source);					
		} else {
			getLogger().error("Could not connect to {}", source);
		}
	}

	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void disconnect() {
		Optional<ISource> connectingSource = getMetadataUpdatePOAsSource(source);
		if( connectingSource.isPresent() ) {
			connectingSource.get().disconnectSink(this, 0, 0, connectingSource.get().getOutputSchema());
			getLogger().debug("Source {} disconnected", source);
		} else {
			getLogger().error("Could not disconnect from {}", source);
		}
	}

	public void addListener( IDataSourceObserverListener listener ) {
		if( listener == null )
			throw new IllegalArgumentException("listener is null");
		
		synchronized( listeners ) {
			if( !listeners.contains(listener)) 
				listeners.add(listener);
		}
	}
	
	@Override
	public SDFSchema getOutputSchema() {
		return source.getOutputSchema();
	}
	
	public void removeListener( IDataSourceObserverListener listener ) {
		synchronized( listeners ) {
			listeners.remove(listener);
		}
	}
	
	protected final void fireStreamElementRecieveEvent( IStreamObject<?> element, int port ) {
		synchronized( listeners ) {
			for( IDataSourceObserverListener listener : listeners ) {
				listener.streamElementRecieved(this, element, port);
			}
		}
	}
	
	protected final void firePuntuationElementRecieveEvent( IPunctuation element, int port ) {
		synchronized( listeners ) {
			for( IDataSourceObserverListener listener : listeners ) {
				listener.punctuationElementRecieved(this, element, port);
			}
		}
	}

	@SuppressWarnings("unchecked")
	private static Optional<IPhysicalOperator> getNextOperator(IPhysicalOperator operator, Class<? extends IPhysicalOperator> opClass) {
		if( operator.isSource() ) {
			ISource<?> source = (ISource<?>)operator;
			Collection<?> subs = source.getSubscriptions();
			for( Object sub : subs ) {
				PhysicalSubscription<? extends ISink<?>> physSub = (PhysicalSubscription<? extends ISink<?>>)sub;
				IPhysicalOperator target = (IPhysicalOperator)physSub.getTarget();
				if( target.getClass().equals(opClass)) {
					return Optional.of(target);
				}
			}
		}
		return Optional.absent();
	}
	
	@SuppressWarnings("rawtypes")
	private static Optional<ISource> getMetadataUpdatePOAsSource(ISource<?> source) {
		Optional<IPhysicalOperator> metadataCreationPO = getNextOperator(source, MetadataCreationPO.class);
		if( metadataCreationPO.isPresent() ) {
			Optional<IPhysicalOperator> metadataUpdatePO = getNextOperator(metadataCreationPO.get(), MetadataUpdatePO.class);
			if( metadataUpdatePO.isPresent() ) {
				IPhysicalOperator op = metadataUpdatePO.get();
				return Optional.of((ISource)op);
			}
		}
		return Optional.absent();
	}
}

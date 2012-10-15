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
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;
import de.uniol.inf.is.odysseus.core.metadata.PointInTime;
import de.uniol.inf.is.odysseus.core.physicaloperator.ISource;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractSink;

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
	public void processPunctuation(PointInTime timestamp, int port) {
		firePuntuationElementRecieveEvent(timestamp, port);
	}

	@Override
	protected void process_next(IStreamObject<?> object, int port) {
		fireStreamElementRecieveEvent(object, port);
	}

	@Override
	public AbstractSink<IStreamObject<?>> clone() {
		return null;
	}
	
	public void connect() {
		source.connectSink(this, 0, 0, source.getOutputSchema());
		getLogger().debug("Source " + source + " connected");
	}
	
	public void disconnect() {
		source.disconnectSink(this, 0, 0, source.getOutputSchema());
		getLogger().debug("Source " + source + " disconnected");
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
	
	protected final void firePuntuationElementRecieveEvent( PointInTime element, int port ) {
		synchronized( listeners ) {
			for( IDataSourceObserverListener listener : listeners ) {
				listener.punctuationElementRecieved(this, element, port);
			}
		}
	}
}

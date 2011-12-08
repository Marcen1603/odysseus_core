package de.uniol.inf.is.odysseus.costmodel.operator.datasrc.impl;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.metadata.PointInTime;
import de.uniol.inf.is.odysseus.physicaloperator.AbstractSink;
import de.uniol.inf.is.odysseus.physicaloperator.ISource;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttributeList;

public class DataSourceObserverSink<In> extends AbstractSink<In> {
	
	private static Logger _logger = null;

	protected static Logger getLogger() {
		if (_logger == null) {
			_logger = LoggerFactory.getLogger(DataSourceObserverSink.class);
		}
		return _logger;
	}

	private final ISource<In> source;
	private final List<IDataSourceObserverListener<In>> listeners = new ArrayList<IDataSourceObserverListener<In>>();

	public DataSourceObserverSink( ISource<In> source ) {
		this.source = source;
		connect();
	}
	
	@Override
	public void processPunctuation(PointInTime timestamp, int port) {
		firePuntuationElementRecieveEvent(timestamp, port);
	}

	@Override
	protected void process_next(In object, int port, boolean isReadOnly) {
		fireStreamElementRecieveEvent(object, port);
	}

	@Override
	public AbstractSink<In> clone() {
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

	public void addListener( IDataSourceObserverListener<In> listener ) {
		if( listener == null )
			throw new IllegalArgumentException("listener is null");
		
		synchronized( listeners ) {
			if( !listeners.contains(listener)) 
				listeners.add(listener);
		}
	}
	
	@Override
	public SDFAttributeList getOutputSchema() {
		return source.getOutputSchema();
	}
	
	public void removeListener( IDataSourceObserverListener<In> listener ) {
		synchronized( listeners ) {
			listeners.remove(listener);
		}
	}
	
	protected final void fireStreamElementRecieveEvent( In element, int port ) {
		synchronized( listeners ) {
			for( IDataSourceObserverListener<In> listener : listeners ) {
				listener.streamElementRecieved(this, element, port);
			}
		}
	}
	
	protected final void firePuntuationElementRecieveEvent( PointInTime element, int port ) {
		synchronized( listeners ) {
			for( IDataSourceObserverListener<In> listener : listeners ) {
				listener.punctuationElementRecieved(this, element, port);
			}
		}
	}
}

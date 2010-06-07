package de.uniol.inf.is.odysseus.rcp.viewer.model.stream.impl;

import java.util.ArrayList;
import java.util.Collection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.base.PointInTime;
import de.uniol.inf.is.odysseus.physicaloperator.base.AbstractSink;
import de.uniol.inf.is.odysseus.physicaloperator.base.ISource;
import de.uniol.inf.is.odysseus.rcp.viewer.model.stream.IStreamConnection;
import de.uniol.inf.is.odysseus.rcp.viewer.model.stream.IStreamElementListener;

public class DefaultStreamConnection<In> extends AbstractSink<In> implements IStreamConnection<In> {

	private static final Logger logger = LoggerFactory.getLogger( DefaultStreamConnection.class );
	
	private Collection<ISource<? extends In>> sources;
	private boolean connected = false;
	private boolean enabled = false;
	
	private ArrayList<In> collectedObjects = new ArrayList<In>();
	private ArrayList<Integer> collectedPorts = new ArrayList<Integer>();
	
	
	private final Collection<IStreamElementListener<In>> listeners = new ArrayList<IStreamElementListener<In>>();
	private boolean hasExceptions = false;
	
	public DefaultStreamConnection(Collection<ISource<? extends In>> source ) {
		if( source == null || source.isEmpty() ) 
			throw new IllegalArgumentException("source is null or empty!");
		
		this.sources = source;
		logger.debug( "StreamManager created" );
	}
	
	@Override
	public final void connect() {
		if(sources != null ) {
			int i = 0;
			for( ISource<? extends In> s : sources ) {
				subscribeToSource( s, i++,0 , s.getOutputSchema());
			}
			connected = true;
			logger.debug("Connected");
		}
	}
	
	@Override
	public final void disconnect() {
		if( sources != null ) {
			int i = 0;
			for(ISource<? extends In> s : sources ) {
				unsubscribeFromSource( s, i++, 0, s.getOutputSchema());
			}
			connected = false;
			logger.debug("Disconnected");
		}
	}
	
	@Override
	public boolean isConnected() {
		return connected;
	}

	@Override
	protected void process_next( In element, int port, boolean isReadOnly ) {
		logger.debug( "Objekt:" + element.toString() );
		if( hasExceptions )
			return;
		
		if( enabled ) {
			// Objekt zwischenspeichern und nicht verarbeiten lassen
			synchronized( collectedObjects ) {
				collectedObjects.add(element);
				collectedPorts.add(port);
			}
				
		} else {
			try {
				notifyListeners( element, port );
			} catch( Exception ex ) {
				logger.error( "Bei der Verarbeitung des Datenelements " + element.toString() + " trat eine Exception auf!", ex);
				hasExceptions = true;
			}
		}
	}
	
	@Override
	public void disable() {
		enabled = true;
	}
	
	@Override
	public void enable() {
		enabled = false;
		if( collectedObjects.size() > 0 ) {
			synchronized( collectedObjects ) {
				// gesammelte Daten nachträglich verarbeiten lassen
				for( int i = 0; i < collectedObjects.size(); i++ ) {
					process_next(collectedObjects.get( i ), collectedPorts.get( i ), true);
				}
				collectedObjects.clear();
				collectedPorts.clear();
			}
		}
	}
	
	@Override
	public boolean isEnabled() {
		return enabled;
	}

	@Override
	public void addStreamElementListener( IStreamElementListener< In > listener ) {
		if( listener == null )
			return;
		
		synchronized( listeners ) {
			if( !listeners.contains( listener ))
				listeners.add( listener );
		}
	}

	@Override
	public void notifyListeners( In element, int port ) {
		synchronized( listeners ) {
			for( IStreamElementListener<In> l : listeners ) {
				if( l != null )
					l.streamElementRecieved( element, port );
			}
		}
	}

	@Override
	public void removeStreamElementListener( IStreamElementListener< In > listener ) {
		if( listener == null )
			return;
		
		synchronized( listeners ) {
			if( listeners.contains( listener ))
				listeners.remove( listener );
		}
		
	}
	
	@Override
	public DefaultStreamConnection<In> clone()  {
		throw new RuntimeException("Clone Not implemented yet");
	}

	@Override
	public void processPunctuation(PointInTime timestamp, int port) {
		// TODO Auto-generated method stub
		
	}


}

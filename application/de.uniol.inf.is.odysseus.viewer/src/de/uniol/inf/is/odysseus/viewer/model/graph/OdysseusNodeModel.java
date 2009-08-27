package de.uniol.inf.is.odysseus.viewer.model.graph;

import java.util.ArrayList;
import java.util.Collection;

import de.uniol.inf.is.odysseus.base.IPhysicalOperator;
import de.uniol.inf.is.odysseus.monitoring.IMonitoringData;
import de.uniol.inf.is.odysseus.monitoring.IMonitoringDataProvider;
import de.uniol.inf.is.odysseus.monitoring.IPeriodicalMonitoringData;
import de.uniol.inf.is.odysseus.monitoring.IPublisher;
import de.uniol.inf.is.odysseus.physicaloperator.base.IObservablePhysicalOperator;
import de.uniol.inf.is.odysseus.viewer.model.meta.IMetadataChangeListener;

public class OdysseusNodeModel extends DefaultNodeModel<IPhysicalOperator> 
			implements IOdysseusNodeModel {

	private IMonitoringDataProvider metadataProvider;
	private Collection<IMetadataChangeListener<IMonitoringData<?>>> listeners = new ArrayList<IMetadataChangeListener<IMonitoringData<?>>>();
	
	public OdysseusNodeModel( IObservablePhysicalOperator content ) {
		super( content );
		metadataProvider = content;
	}

	@SuppressWarnings("unchecked")
	@Override
	public void addMetadataItem( String type, IMonitoringData< ? > metaDataItem ) {
		try {
			metadataProvider.addMonitoringData( type, metaDataItem );
		} catch( IllegalArgumentException ex ) {}
		
		IMonitoringData<?> item = metadataProvider.getMonitoringData( type );
		if( item instanceof IPeriodicalMonitoringData ) {
			((IPeriodicalMonitoringData<Object>)item).subscribe( this );
		}
		
		notifyMetadataChangedListener( type );
	}


	@Override
	public IMonitoringData< ? > getMetadataItem( String metadataType ) {
		return metadataProvider.getMonitoringData( metadataType );
	}


	@Override
	public Collection< String > getProvidedMetadataTypes() {
		return metadataProvider.getProvidedMonitoringData();
	}

	@SuppressWarnings("unchecked")
	@Override
	public void removeMetadataItem( String type ) {
		if( getProvidedMetadataTypes().contains( type )) {
			IMonitoringData<?> item = metadataProvider.getMonitoringData( type );
			if( item instanceof IPeriodicalMonitoringData ) {
				((IPeriodicalMonitoringData<Object>)item).unsubscribe( this );
			}
			metadataProvider.removeMonitoringData( type );
			notifyMetadataChangedListener( type );
		}
	}


	@Override
	public void resetMetadataItem( String type ) {
		if( getProvidedMetadataTypes().contains( type )) {
			IMonitoringData<?> item = getMetadataItem( type );
			item.reset();
			notifyMetadataChangedListener( type );
		}
	}

	@Override
	public void update( IPublisher<Object> publisher, Object value ) {
		IPeriodicalMonitoringData<Object> metadataItem = (IPeriodicalMonitoringData<Object>)publisher;
		notifyMetadataChangedListener( metadataItem.getType() );
	}


	@Override
	public void notifyMetadataChangedListener( String changed ) {
		if( listeners.isEmpty() )
			return;
		
		synchronized( listeners ) {
			for( IMetadataChangeListener<IMonitoringData<?>> l : listeners ) {
				if( l != null ) {
					l.metadataChanged( this, changed );
				}
			}
		}
	}

	@Override
	public void addMetadataChangeListener( IMetadataChangeListener<IMonitoringData<?>> listener ) {
		if( listener == null )
			return ;
		
		if( listeners.contains( listener ))
			return;
		
		synchronized( listeners ) {
			listeners.add( listener );
		}
	}


	@Override
	public void removeMetadataChangeListener( IMetadataChangeListener<IMonitoringData<?>> listener ) {
		if( !listeners.contains( listener ))
			return;
		
		synchronized( listeners ) {
			listeners.remove( listener );
		}
	}

}

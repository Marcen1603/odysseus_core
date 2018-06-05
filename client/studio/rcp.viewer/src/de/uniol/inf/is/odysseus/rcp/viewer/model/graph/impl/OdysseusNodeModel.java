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
package de.uniol.inf.is.odysseus.rcp.viewer.model.graph.impl;

import java.util.ArrayList;
import java.util.Collection;

import de.uniol.inf.is.odysseus.core.monitoring.IMonitoringData;
import de.uniol.inf.is.odysseus.core.monitoring.IMonitoringDataProvider;
import de.uniol.inf.is.odysseus.core.monitoring.IPeriodicalMonitoringData;
import de.uniol.inf.is.odysseus.core.monitoring.IPublisher;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.rcp.viewer.model.graph.IOdysseusNodeModel;
import de.uniol.inf.is.odysseus.rcp.viewer.model.meta.IMetadataChangeListener;

public class OdysseusNodeModel extends DefaultNodeModel<IPhysicalOperator> 
			implements IOdysseusNodeModel {

	private IMonitoringDataProvider metadataProvider;
	private Collection<IMetadataChangeListener<IMonitoringData<?>>> listeners = new ArrayList<IMetadataChangeListener<IMonitoringData<?>>>();
	
	public OdysseusNodeModel( IPhysicalOperator content ) {
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

	@Override
	public String toString() {
		return getName();
	}
}

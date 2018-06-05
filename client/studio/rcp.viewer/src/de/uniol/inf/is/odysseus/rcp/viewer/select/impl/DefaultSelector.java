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
package de.uniol.inf.is.odysseus.rcp.viewer.select.impl;

import java.util.ArrayList;
import java.util.Collection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.rcp.viewer.select.ISelectListener;
import de.uniol.inf.is.odysseus.rcp.viewer.select.ISelector;

public class DefaultSelector<T> implements ISelector<T> {
	
	private static final Logger logger = LoggerFactory.getLogger( DefaultSelector.class );

	private final Collection<ISelectListener<T>> listeners = new ArrayList<ISelectListener<T>>();
	
	private Collection<T> selected = new ArrayList<T>();
	
	@Override
	public void select( Collection<? extends T> selections ) {
		if( selections == null ) {
			if(logger.isWarnEnabled())
				logger.warn( "selections is null!" );
			return;
		}
		for( T n : selections )
			select(n);
	}
	
	@Override
	public int getSelectionCount() {
		return selected.size();
	}

	@Override
	public void select( T node ) {
		if( node == null ) {
			if( logger.isWarnEnabled())
				logger.warn( "node is null!" );
		}
		if( !isSelected(node) )
			selected.add( node );
	}
	
	@Override
	public void unselect( Collection< ? extends T > nodes ) {
		if( nodes == null ) {
			if(logger.isWarnEnabled())
				logger.warn( "nodes is null!" );
			return;
		}
		for( T n : nodes )
			unselect(n);
	}

	@Override
	public void unselect( T node ) {
		selected.remove( node );
	}

	@Override
	public void unselectAll() {
		selected.clear();
	}

	@Override
	public boolean isSelected( T node ) {
		if( node == null ) 
			return false;
		return selected.contains( node );
	}
	
	@Override
	public Collection<T> getSelected() {
		return copyArray(selected);
	}

	private Collection<T> copyArray( Collection<T> array ) {
		Collection<T> clone = new ArrayList<T>();
		for( T obj : array )
			clone.add(  obj  );
		return clone;
	}
	
	@Override
	public void addSelectListener( ISelectListener<T> listener ) {
		if( listener == null && logger.isWarnEnabled() )
			logger.warn( "listener is null!" );
		
		synchronized( listeners ) {
			if( !listeners.contains( listener ))
				listeners.add( listener );
		}
	}

	@Override
	public void notifySelectListener( ISelector<T> sender, Collection<? extends T> selected ) {
		synchronized( listeners ) {
			for( ISelectListener<T> listener : listeners )
				if( listener != null )
					listener.selectObject( sender, selected );
		}
	}


	@Override
	public void notifyUnselectListener( ISelector<T> sender, Collection<? extends T> unselected ) {
		synchronized( listeners ) {
			for( ISelectListener<T> listener : listeners )
				if( listener != null )
					listener.unselectObject( sender, unselected );
		}
	}
	
	@Override
	public void removeSelectListener( ISelectListener<T> listener ) {
		synchronized(listeners) {
			listeners.remove( listener );
		}
	}}

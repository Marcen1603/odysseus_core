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
package de.uniol.inf.is.odysseus.rcp.viewer.view.impl;

import de.uniol.inf.is.odysseus.rcp.viewer.model.graph.IConnectionModel;
import de.uniol.inf.is.odysseus.rcp.viewer.symbol.ConnectionSymbolElementContainer;
import de.uniol.inf.is.odysseus.rcp.viewer.view.IConnectionView;
import de.uniol.inf.is.odysseus.rcp.viewer.view.INodeView;

public class DefaultConnectionView<C> implements IConnectionView<C> {

	private final IConnectionModel<C> data;
	private final INodeView<C> start;
	private final INodeView<C> end;
	private ConnectionSymbolElementContainer<C> symbol = new ConnectionSymbolElementContainer<C>(); 
	
	public DefaultConnectionView( IConnectionModel<C> data, INodeView<C> start, INodeView<C> end ) {
		this.data = data;
		this.start = start;
		this.end = end;
	}

	@Override
	public final IConnectionModel<C> getModelConnection() {
		return data;
	}
	
	@Override
	public final INodeView<C> getViewedStartNode() {
		return start;
	}
	
	@Override
	public final INodeView<C> getViewedEndNode() {
		return end;
	}
	
	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append( "{ " );
		sb.append( start.toString() );
		sb.append( " --> " );
		sb.append(  end.toString()  );
		sb.append( " }" );
		return sb.toString();
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public boolean equals( Object obj ) {
		if( obj == null )
			return false;
		if( !(obj instanceof DefaultConnectionView))
			return false;
		if( obj == this )
			return true;
		
		DefaultConnectionView<C> d = (DefaultConnectionView<C>)obj;
		return d.start.equals(start) && d.end.equals( end );
	}

	@Override
	public ConnectionSymbolElementContainer<C> getSymbolContainer() {
		return symbol;
	}

	@Override
	public int hashCode() {
		return start.hashCode() + end.hashCode();
	}

}

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
package de.uniol.inf.is.odysseus.rcp.viewer.view;

import java.util.Collection;

import de.uniol.inf.is.odysseus.rcp.viewer.model.graph.INodeModel;
import de.uniol.inf.is.odysseus.rcp.viewer.symbol.SymbolElementContainer;

public interface INodeView<C> {
	
	public INodeModel<C> getModelNode();
	
	public Vector getPosition();
	public void setPosition( Vector v );
	
	public int getWidth();
	public int getHeight();
	public void setWidth( int w );
	public void setHeight( int h );
	
	public SymbolElementContainer<C> getSymbolContainer();
	
	public boolean isVisible();
	public boolean isEnabled();
	public void setVisible( boolean visible );
	public void setEnabled( boolean enabled );
	
	public Collection<IConnectionView<C>> getAllConnections();
	public Collection<IConnectionView<C>> getConnectionsAsStart();	
	public Collection<IConnectionView<C>> getConnectionsAsEnd();
	
}
/** Copyright [2011] [The Odysseus Team]
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

import de.uniol.inf.is.odysseus.rcp.viewer.model.graph.IGraphModel;

public interface IGraphView<C>{
	
	public Collection<INodeView<C>> getViewedNodes();	
	public Collection<IConnectionView<C>> getViewedConnections();
	
	public void insertViewedNode(INodeView<C> nodeView );
	public void insertViewedConnection( IConnectionView<C> connView );
	
	public void removeViewedNode( INodeView<C> nodeView );
	public void removeViewedConnection( IConnectionView<C> connView );
	
	public IGraphModel<C> getModelGraph();
}
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
package de.uniol.inf.is.odysseus.rcp.viewer.render;

import de.uniol.inf.is.odysseus.rcp.viewer.select.ISelector;
import de.uniol.inf.is.odysseus.rcp.viewer.view.IGraphView;
import de.uniol.inf.is.odysseus.rcp.viewer.view.INodeView;
import de.uniol.inf.is.odysseus.rcp.viewer.view.Vector;

public interface IRenderManager<C> {

	public void resetPositions();

	public float getZoomFactor();
	public void setZoomFactor(float zoomFactor);
	public void resetZoom();
	public void zoom( float offset );
	public void zoom( int centerX, int centerY, float offset );

	public void setDisplayedGraph(IGraphView<C> graph);
	public IGraphView<C> getDisplayedGraph();

	public void resetGraphOffset();
	public Vector getGraphOffset();
	public void setGraphOffset(Vector offset);

	public void refreshView();
	public ISelector<INodeView<C>> getSelector();
	
	public int getRenderWidth();
	public int getRenderHeight();

}
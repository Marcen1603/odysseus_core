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
package de.uniol.inf.is.odysseus.rcp.viewer.stream.map;

import org.eclipse.ui.PlatformUI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geomgraph.index.SweepLineEvent;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.rcp.viewer.stream.map.layer.VectorLayer;
//import de.uniol.inf.is.odysseus.intervalapproach.*;

/**
 * @author Stephan Jansen
 * @author Kai Pancratz
 * 
 */
public class StreamMapWindowEditor extends StreamMapEditor {

	private static final Logger LOG = LoggerFactory.getLogger(StreamMapWindowEditor.class);
	//private ITimeInterval metadata = null;
	
	//DefaultTISweepArea sweepArea;
	
	public StreamMapWindowEditor() {
		super(100000);
		
		LOG.debug("Window Set to 100.000 Tuple.");
	}

	@Override
	public void streamElementRecieved(Object element, int port) {
		if (!(element instanceof Tuple<?>)) {
			LOG.error("Warning: StreamMap is only for spatial relational tuple!");
			return;
		}
		//LOG.info("Received Element: " + element.toString());
		
		for (Integer key : spatialDataIndex.keySet()) {	
			spatialDataIndex.get(key).addGeometry((Geometry)((Tuple<?>) element).getAttribute(key));	
		}


		tuples.addFirst((Tuple<?>) element);

		
		
		
		if (tuples.size() > getMaxTuplesCount()) {
			tuples.removeLast();
			for (VectorLayer layer : spatialDataIndex.values()) {
				layer.removeLast();
			} 
		}

		if (update == null && screenManager.hasCanvasViewer()
				&& !screenManager.getCanvas().isDisposed()) {
			PlatformUI.getWorkbench().getDisplay()
					.asyncExec(update = new Runnable() {
						@Override
						public void run() {
							if (!screenManager.getCanvas().isDisposed())
								screenManager.getCanvas().redraw();
							update = null;
						}
					});
		}
		
	}
	
}

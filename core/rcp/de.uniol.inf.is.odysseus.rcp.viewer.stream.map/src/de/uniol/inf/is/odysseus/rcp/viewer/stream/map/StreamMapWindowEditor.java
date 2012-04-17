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
package de.uniol.inf.is.odysseus.rcp.viewer.stream.map;

import org.eclipse.ui.PlatformUI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vividsolutions.jts.geom.Geometry;

import de.uniol.inf.is.odysseus.core.server.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.rcp.viewer.stream.map.layer.Layer;
import de.uniol.inf.is.odysseus.relational.base.Tuple;

/**
 * @author Stephan Jansen
 * @author Kai Pancratz
 * 
 */
public class StreamMapWindowEditor extends StreamMapEditor {

	private static final Logger LOG = LoggerFactory.getLogger(StreamMapWindowEditor.class);
	private ITimeInterval metadata = null;
	
	public StreamMapWindowEditor() {
		//BAD!
		super(100);
	}

	@Override
	public void streamElementRecieved(Object element, int port) {
		if (!(element instanceof Tuple<?>)) {
			LOG.error("Warning: StreamMap is only for relational tuple!");
			return;
		}
		for (Integer key : spatialDataIndex.keySet()) {
			spatialDataIndex.get(key).addGeometry(
					(Geometry) ((Tuple<?>) element).getAttribute(key));
		}
		
		
		/*
		 * 
		 * This shouldn't work right!
		 * 
		 */		
		//if(((Tuple<?>)element).getMetadata() instanceof ITimeInterval){
			ITimeInterval metadata = (ITimeInterval)((Tuple<?>)element).getMetadata();

			tuples.add(0, (Tuple<?>) element);
			if(this.metadata != null){
				LOG.error(this.metadata.getEnd().minus(metadata.getStart()).toString());
				
				if(this.metadata.getEnd().after(metadata.getStart())){
					tuples.clear();
					for (Layer layer : spatialDataIndex.values()) {
						//layer.clean();
					}
					this.metadata = metadata;
				}
			}
			else{
				this.metadata = metadata;
			}
			
			if (update == null && hasCanvasViewer()
					&& !getCanvasViewer().isDisposed()) {
				PlatformUI.getWorkbench().getDisplay()
						.asyncExec(update = new Runnable() {
							@Override
							public void run() {
								if (!getCanvasViewer().isDisposed())
									getCanvasViewer().redraw();
								update = null;
							}
						});
			}
			
//		}
//		else{
//			throw new RuntimeException("Windowed Map visualisations must be from type: ITimeInterval");
//		}
		
	}
	
}

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

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.widgets.Display;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.rcp.viewer.stream.map.layer.Layer;

/**
 * @author Stephan Jansen
 * @author Kai Pancratz
 * 
 */
final class GeometryPaintListener implements PaintListener {

	private static final Logger LOG = LoggerFactory
			.getLogger(GeometryPaintListener.class);
	
	/**
	 * 
	 */
	private final StreamMapEditor streamMapEditor;

	public GeometryPaintListener(StreamMapEditor streamMapEditor) {
		this.streamMapEditor = streamMapEditor;
	}

	public void paintControl(PaintEvent e) {
		e.gc.setAntialias(SWT.ON);
		for (Layer layer : this.streamMapEditor.getLayerOrder()) {
			if(layer != null){
				layer.draw(e.gc);
			}
			else{
				LOG.error("Layer in Orderlist is Null");
				throw new RuntimeException("Layer Nullpointer");
			}
		}
		e.gc.setForeground(Display.getCurrent().getSystemColor(SWT.COLOR_BLACK));
		if (this.streamMapEditor.getScreenManager().getMouseSelection() != null){
			e.gc.drawRectangle(this.streamMapEditor.getScreenManager().getMouseSelection());
		}
		e.gc.drawText(this.streamMapEditor.getScreenManager().getMouseLabel().label, this.streamMapEditor.getScreenManager().getMouseLabel().x + 15 , this.streamMapEditor.getScreenManager().getMouseLabel().y,true); 
	}
}
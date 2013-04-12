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
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Display;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vividsolutions.jts.geom.Coordinate;

import de.uniol.inf.is.odysseus.rcp.viewer.stream.map.layer.ILayer;

/** 
 * @author Stephan Jansen
 * @author Kai Pancratz
 * 
 */
final class GeometryPaintListener implements PaintListener {

	@SuppressWarnings("unused")
	private static final Logger LOG = LoggerFactory
			.getLogger(GeometryPaintListener.class);
	
	/**
	 * 
	 */
	private final StreamMapEditorPart streamMapEditor;

	public GeometryPaintListener(StreamMapEditorPart streamMapEditor) {
		this.streamMapEditor = streamMapEditor;
	} 

	@Override
	public void paintControl(PaintEvent e) {
		e.gc.setAntialias(SWT.ON);
		for (ILayer layer : this.streamMapEditor.getMapEditorModel().getLayers()) {
			if(layer != null && layer.isActive()){
				layer.draw(e.gc);
			}
//			else{
//				LOG.error("Layer in Orderlist is Null");
//				throw new RuntimeException("Layer Nullpointer");
//			}
		}
		e.gc.setForeground(ColorManager.getInstance().getColor(new RGB(0,0,0)));
		Canvas canvas = this.streamMapEditor.getScreenManager().getCanvas();
		double scale = this.streamMapEditor.getScreenManager().getScale();
		Point dpi = canvas.getDisplay().getDPI();
		Point screenSize = this.streamMapEditor.getScreenManager().getCanvas().getSize();
		@SuppressWarnings("unused")
		Coordinate sizeInCm = new Coordinate(screenSize.x / dpi.x * 25.4, screenSize.x / dpi.x * 25.4);
		int dpcm = (int) Math.floor(dpi.x / 2.54);
		int ypos = (screenSize.y - 10);
		int xpos = (screenSize.x);
		e.gc.setForeground(Display.getCurrent().getSystemColor(SWT.COLOR_BLACK));
		e.gc.drawLine(xpos - 100, ypos , xpos - 10, ypos);
		e.gc.drawLine(xpos - 100, ypos , xpos - 100, ypos - 10);
		e.gc.drawLine(xpos - 100 + dpcm -1, ypos , xpos - 100 + dpcm -1, ypos - 10);
		e.gc.drawString("Scale: " + (scale * dpcm), xpos - 200, ypos - 80);
		e.gc.drawString("Scale: " + (scale), xpos - 200, ypos - 40);
		if (this.streamMapEditor.getScreenManager().getMouseSelection() != null){
			e.gc.setAlpha(90);
			e.gc.fillRectangle(this.streamMapEditor.getScreenManager().getMouseSelection());
			e.gc.drawRectangle(this.streamMapEditor.getScreenManager().getMouseSelection());
		}
		streamMapEditor.getScreenManager().setRenderComplete(true);
	}
}
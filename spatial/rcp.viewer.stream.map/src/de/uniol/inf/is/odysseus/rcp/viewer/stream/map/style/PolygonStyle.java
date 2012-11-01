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
package de.uniol.inf.is.odysseus.rcp.viewer.stream.map.style;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.PaletteData;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.graphics.Region;
import org.eclipse.swt.widgets.Display;

import de.uniol.inf.is.odysseus.rcp.viewer.stream.map.ColorManager;

/**
 * @author Stephan Jansen
 * @author Kai Pancratz
 * 
 */
public class PolygonStyle extends Style{

	public PolygonStyle(int lineWidth, Color lineColor, Color fillColor) {
		super();
		this.setLineWidth(lineWidth);
		this.setLineColor(lineColor);
		this.setFillColor(fillColor);
	}
	
	@Override
	public void draw(GC gc, int[][] list) {
		draw(gc, list, getLineColor(), getFillColor());
		
		super.draw(gc, list);
	}	
	
	protected void draw(GC gc, int[][] list, Color fcolor, Color bcolor) {
		fill(gc, list, bcolor);
		gc.setLineWidth(this.getLineWidth());
		for (int i = 0; i < list.length; i++) {
			draw(gc, list[i], fcolor, bcolor);
		}
	}	
	
	@Override
	public void draw(GC gc, int[] list) {
		draw(gc, list, getLineColor(), getFillColor());
	}
	
	protected void draw(GC gc, int[] list, Color fcolor, Color bcolor) {
		Color tmp = gc.getForeground();
		gc.setForeground(fcolor);
		gc.drawPolygon(list);
		gc.setForeground(tmp);
		super.draw(gc, list);
	}	

	private void fill(GC gc, int[][] list, Color bcolor) {
		if (getFillColor() != null){
			Region polygon = new Region();
			polygon.add(list[0]);
			for (int i = 1; i < list.length; i++) {
				polygon.subtract(list[i]);
			} 
			gc.setClipping(polygon);
			Color tmp = gc.getBackground();
			gc.setBackground(bcolor);
			gc.fillPolygon(list[0]);
			gc.setForeground(tmp);
			Region nullregion = null;
			gc.setClipping(nullregion);
			polygon.dispose();
		}
	}

	@Override
	public Image getImage() {
		int[][] list = new int[1][];
		int width = getLineWidth();
		int[] l = {width,width, width,DEFAULT_HEIGHT/2, DEFAULT_WIDTH/2,DEFAULT_HEIGHT-width, DEFAULT_WIDTH-width,DEFAULT_HEIGHT/2, DEFAULT_WIDTH/2,DEFAULT_HEIGHT/2, DEFAULT_WIDTH/2,width};
		list[0] = l;
		return getImage(list);
	}
	
	public Image getImage(int[][] list){
		if (!hasChanged())
			return ColorManager.getInstance().getImage(this);
		Display display = Display.getCurrent();
	    Color white = display.getSystemColor(SWT.COLOR_WHITE);
	    Color black = display.getSystemColor(SWT.COLOR_BLACK);
//		ImageData ideaData = new ImageData(size, size, 32, new PaletteData(0,0,0));
//		ideaData.alpha = 0;
//		Image image = new Image(Display.getCurrent(), ideaData);// size, size);
	    Image image = new Image(display, DEFAULT_WIDTH, DEFAULT_HEIGHT);
	    GC gc = new GC(image);
		draw(gc, list);
	    gc.dispose();
	    ImageData imageData = image.getImageData();
	    
	    PaletteData palette = new PaletteData(new RGB[] { new RGB(0, 0, 0),
		        new RGB(0xFF, 0xFF, 0xFF), });
	    ImageData maskData = new ImageData(DEFAULT_WIDTH, DEFAULT_HEIGHT, 1, palette);
	    Image mask = new Image(Display.getCurrent(), maskData);
	    gc = new GC(mask);
	    gc.setBackground(black);
	    gc.fillRectangle(0, 0, DEFAULT_WIDTH, DEFAULT_HEIGHT);
	    draw(gc, list, white, white);
	    gc.dispose();
	    maskData = mask.getImageData();
		Image icon = new Image(display, imageData, maskData);
		ColorManager.getInstance().getImage(this);
		setChanged();
		return icon;
	}
}
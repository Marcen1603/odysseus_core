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
package de.uniol.inf.is.odysseus.rcp.viewer.stream.map.style;

import java.util.LinkedList;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.PaletteData;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.widgets.Display;

import de.uniol.inf.is.odysseus.rcp.viewer.stream.map.ColorManager;

/**
 * @author Stephan Jansen
 * @author Kai Pancratz
 * 
 */
public abstract class Style{
	private boolean changed = true;
	private int lineWidth = 1;
	private Color lineColor = null;
	private Color fillColor = null;
	LinkedList<Style> substyle = null;
	
	public void setLineWidth(int lineWidth) {
		this.lineWidth = lineWidth;
		changed = true;
	}
	
	public void setLineColor(Color color) {
		this.lineColor = color;
		changed = true;
	}

	public void setFillColor(Color fillColor) {
		this.fillColor = fillColor;
		changed = true;
	}

	public int getLineWidth() {
		return lineWidth;
	}
	
	public Color getFillColor() {
		return fillColor;
	}
	
	public Color getLineColor() {
		return lineColor;
	}
	
	public boolean hasChanged(){
		return changed;
	}
	
	public void setChanged(){
		changed = true;
	}
	public void addStyle(Style style){
		if (substyle == null)
			substyle = new LinkedList<Style>();
		substyle.add(style);
		changed = true;
	}

	public void removeStyle(Style style){
		if (substyle != null)
			return;
		substyle.remove(style);
		changed = true;
	}

	public void draw(GC gc, int[] list) {
		if (substyle != null)
		for (Style style : substyle) {
			style.draw(gc, list);
		}
	}

	public void draw(GC gc, int[][] list) {
		if (substyle != null)
		for (Style style : substyle) {
			style.draw(gc, list);
		}
	}
	
	//abstract protected void draw(GC gc, int[] list, Color fcolor, Color bcolor);
	
	abstract public Image getImage();
	
	protected final static int DEFAULT_WIDTH = 20;
    
	protected final static int DEFAULT_HEIGHT = 20;
    
	public Image getImage(int[] list){
		if (!changed)
			return ColorManager.getInstance().getImage(this);
		
		Display display = Display.getCurrent();
//	    Color white = display.getSystemColor(SWT.COLOR_WHITE);
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
	   // draw(gc, list, white, white);
	    draw(gc, list);
	    gc.dispose();
	    maskData = mask.getImageData();

		Image icon = new Image(display, imageData, maskData);
		ColorManager.getInstance().getImage(this);
		changed = false;
		
		return icon;
	}
    
}
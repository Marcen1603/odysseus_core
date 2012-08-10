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
import java.util.ArrayList;
import java.util.LinkedList;

import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.PaletteData;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.widgets.Display;

import de.uniol.inf.is.odysseus.rcp.viewer.stream.map.ColorManager;

import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.LineString;
import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.geom.Polygon;

/**
 * @author Stephan Jansen
 * @author Kai Pancratz
 * 
 */
public class CollectionStyle extends Style{

	private LinkedList<Style> pointStyle = new LinkedList<Style>();
	private LinkedList<Style> lineStyle = new LinkedList<Style>();
	private LinkedList<Style> polygonStyle = new LinkedList<Style>();
	
	@Override
	public void setActiveStyle(Geometry g){
		if (g instanceof Point){
			setActiveStyle((Point) g);
		}
		else if (g instanceof LineString){
				setActiveStyle((LineString) g);
		}
		else if (g instanceof Polygon){
			setActiveStyle((Polygon) g);
		}	
	}
	
	public void setActiveStyle(Point g){
		substyle = pointStyle;
	}

	public void setActiveStyle(LineString g){
		substyle = lineStyle;
	}

	public void setActiveStyle(Polygon g){
		substyle = polygonStyle;
	}
	
	public CollectionStyle() {
		super(); 
	}
	
	@Override
	public void addStyle(Style style) {
		if (style instanceof PointStyle)
			pointStyle.add(style);
		else if (style instanceof LineStyle)
			lineStyle.add(style);
		else if (style instanceof PolygonStyle)
			polygonStyle.add(style);
	}
	
	@Override
	public void removeStyle(Style style) {
		if (style instanceof PointStyle)
			pointStyle.remove(style);
		else if (style instanceof LineStyle)
			lineStyle.remove(style);
		else if (style instanceof PolygonStyle)
			polygonStyle.remove(style);
	}
	
	
	@Override
	public Image getImage() {
		int[][] list = new int[1][];
		int width = getLineWidth();
		int[] l = {width,width, width,DEFAULT_HEIGHT/2, DEFAULT_WIDTH/2,DEFAULT_HEIGHT-width, DEFAULT_WIDTH-width,DEFAULT_HEIGHT/2, DEFAULT_WIDTH/2,DEFAULT_HEIGHT/2, DEFAULT_WIDTH/2,width};
		list[0] = l;
		return getImage(list);
	}

	public void draw(GC gc, int[][] list) {
		draw(gc, list, getLineColor(), getFillColor());
		super.draw(gc, list);
	}	

	protected void draw(GC gc, int[][] list, Color fcolor, Color bcolor) {
		//fill(gc, list, bcolor);
		gc.setLineWidth(this.getLineWidth());
		for (int i = 0; i < list.length; i++) {
			draw(gc, list[i], fcolor, bcolor);
		}
	}	
	
	@Override
	protected void draw(GC gc, int[] list, Color fcolor, Color bcolor) {
			super.draw(gc, list);
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
	
	@Override
	public boolean hasSubstyles() {
		if (!pointStyle.isEmpty())
			return true;
		if (!lineStyle.isEmpty())
			return true;
		if (!polygonStyle.isEmpty())
			return true;
		return false;
	}
	
	@Override
	public boolean contains(Style style) {
		if (style instanceof PointStyle)
			return pointStyle.contains(style);
		else if (style instanceof LineStyle)
			return lineStyle.contains(style);
		else if (style instanceof PolygonStyle)
			return polygonStyle.contains(style);
		return super.contains(style);
	}
	
	public Style[] getSubstyles() {
		if (!hasSubstyles())
			return null;
		LinkedList<Style> tmp = (LinkedList<Style>) pointStyle.clone();
		tmp.addAll(lineStyle);
		tmp.addAll(polygonStyle);
		return tmp.toArray(new Style[tmp.size()]);
	}  
}
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
package de.uniol.inf.is.odysseus.rcp.dashboard.part.map.style;

import java.util.LinkedList;

import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;
import de.uniol.inf.is.odysseus.core.collection.Tuple;

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
	public void setActiveStyle(STYLE s){
		switch (s) {
		case POINT:
			substyle = pointStyle;
			break;
		case LINESTRING:
			substyle = lineStyle;	
			break;
		case POLYGON:
			substyle = polygonStyle;
			break;
		default:
		}
	}
	
	public CollectionStyle() {
		this.setLineColor(null);
		this.setFillColor(null);
		this.setLineWidth(null);
	}
	
	@Override
	public void addStyle(Style style) {
		if (style instanceof PointStyle)
			pointStyle.add(style);
		else if (style instanceof LineStyle)
			lineStyle.add(style);
		else if (style instanceof PolygonStyle)
			polygonStyle.add(style);
		style.parentstyle = this;
		setChanged(true);
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
	public void getImage(GC imgGC, GC maskGC) {
	    for (Style style : this.polygonStyle){
	    	style.getImage(imgGC, maskGC);
	    }
	    for (Style style : this.lineStyle){
	    	style.getImage(imgGC, maskGC);
	    }
	    for (Style style : this.pointStyle){
	    	style.getImage(imgGC, maskGC);
	    }

	}

	@Override
	public void draw(GC gc, int[][] list, Tuple<?> tuple) {
		draw(gc, list, getLineColor(tuple), getFillColor(tuple), tuple);
		super.draw(gc, list, tuple);
	}	

	protected void draw(GC gc, int[][] list, Color fcolor, Color bcolor, Tuple<?> tuple) {
		//fill(gc, list, bcolor);
		gc.setLineWidth(this.getLineWidth(tuple));
		for (int i = 0; i < list.length; i++) {
			draw(gc, list[i], fcolor, bcolor, tuple);
		}
	}	
	
	@Override
	protected void draw(GC gc, int[] list, Color fcolor, Color bcolor, Tuple<?> tuple) {
			super.draw(gc, list, tuple);
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
	
	@Override
	@SuppressWarnings("unchecked")
	public Style[] getSubstyles() {
		if (!hasSubstyles())
			return null;
		LinkedList<Style> tmp =  (LinkedList<Style>) pointStyle.clone();
		tmp.addAll(lineStyle);
		tmp.addAll(polygonStyle);
		return tmp.toArray(new Style[tmp.size()]);
	}  
}
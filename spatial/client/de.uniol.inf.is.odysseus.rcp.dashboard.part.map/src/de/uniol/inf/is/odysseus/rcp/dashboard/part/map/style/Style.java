package de.uniol.inf.is.odysseus.rcp.dashboard.part.map.style;

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

import java.util.LinkedList;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.PaletteData;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.widgets.Display;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.rcp.dashboard.part.map.ColorManager;
import de.uniol.inf.is.odysseus.rcp.dashboard.part.map.model.style.PersistentStyle;
import de.uniol.inf.is.odysseus.rcp.dashboard.part.map.style.expression.ColorCondition;
import de.uniol.inf.is.odysseus.rcp.dashboard.part.map.style.expression.IntegerCondition;


public abstract class Style {
	
	public Style() {
		this.lineWidth = new IntegerCondition(1);
		this.lineColor = new ColorCondition(ColorManager.getInstance().randomColor());
		this.fillColor = new ColorCondition(ColorManager.getInstance().randomColor());
	}
	
	public void init(SDFSchema schema){
		if (this.fillColor != null) this.fillColor.init(schema);
		if (this.lineColor != null) this.lineColor.init(schema);
		if (this.lineWidth != null) this.lineWidth.init(schema);
		Style[] substyles = getSubstyles();
		if (substyles != null){
			for (Style substyle : substyles) {
				substyle.init(schema);
			}
		}
	}
	
	public static Style setStyleByConfig(PersistentStyle pStyle) {
		ColorCondition lineColor = null;
		ColorCondition fillColor = null;
		IntegerCondition lineWidth = null;
		Style style = null;

		if (pStyle.getLineColor() != null)
			lineColor = pStyle.getLineColor();
		if (pStyle.getFillColor() != null)
			fillColor = pStyle.getFillColor();

		lineWidth = pStyle.getLineWidth();

		if (pStyle.getType().equals("point")) {
			style = new PointStyle(pStyle.getShape(), pStyle.getShapeSize(), lineWidth, lineColor, fillColor);
		} else if (pStyle.getType().equals("line")) {
			style = new LineStyle(lineWidth, lineColor);
		} else if (pStyle.getType().equals("polygon")) {
			style = new PolygonStyle(lineWidth, lineColor, fillColor);
		} else if (pStyle.getType().equals("collection")) {
			style = new CollectionStyle();
		} else {
			throw new IllegalArgumentException("styletype " + pStyle.getType() + " not known");
		}
		
		List<PersistentStyle> substyles = pStyle.getSubstyles();
		if (substyles != null){
			for (PersistentStyle substyle : substyles) {
				style.addStyle(setStyleByConfig(substyle));
			}
		}		
		return style;
	}

	public enum STYLE {
		POINT, LINESTRING, POLYGON
	};

	private boolean changed = true;
	private IntegerCondition lineWidth = null;
	private ColorCondition lineColor = null;
	private ColorCondition fillColor = null;
	LinkedList<Style> substyle = null;
	Style parentstyle = null;

	public void setLineWidth(IntegerCondition lineWidth) {
		this.lineWidth = lineWidth;
		setChanged(true);
	}

	public void setLineColor(ColorCondition color) {
		this.lineColor = color;
		setChanged(true);
	}

	public void setFillColor(ColorCondition fillColor) {
		this.fillColor = fillColor;
		setChanged(true);
	}

	public IntegerCondition getLineWidth() {
		return lineWidth;
	}

	public ColorCondition getFillColor() {
		return fillColor;
	}

	public ColorCondition getLineColor() {
		return lineColor;
	}

	public int getLineWidth(Tuple<?> t) {
		return lineWidth.eval(t);
	}

	public Color getFillColor(Tuple<?> t) {
		if (fillColor == null) return null;
		return fillColor.eval(t);
	}

	public Color getLineColor(Tuple<?> t) {
		return lineColor.eval(t);
	}

	
	public boolean hasChanged() {
		return changed;
	}

	void setChanged(boolean b) {
		changed = b;
		if (this.parentstyle != null)
			this.parentstyle.setChanged(b);
	}

	public void addStyle(Style style) {
		if (substyle == null)
			substyle = new LinkedList<Style>();
		substyle.add(style);
		style.parentstyle = this;
		setChanged(true);
	}

	public void removeStyle(Style style) {
		if (substyle != null)
			return;
		substyle.remove(style);
		setChanged(true);
	}

	public void draw(GC gc, int[] list, Tuple<?> tuple) {
		if (substyle != null)
			for (Style style : substyle) {
				style.draw(gc, list, tuple);
			}
	}

	public void draw(GC gc, int[][] list, Tuple<?> tuple) {
		if (substyle != null)
			for (Style style : substyle) {
				style.draw(gc, list, tuple);
			}
	}

	// Double Method: hasSubStyles and hasSubStyle

	public boolean hasSubStyle() {
		if (substyle != null) {
			return true;
		} 
		return false;
	}

	public void setActiveStyle(STYLE s) {
	}

	abstract protected void draw(GC gc, int[] list, Color fcolor, Color bcolor, Tuple<?> tuple);

	public Image getImage() {
		if (!changed)
			return ColorManager.getInstance().getImage(this);

		Display display = Display.getCurrent();
		// Color white = display.getSystemColor(SWT.COLOR_WHITE);
		Color black = display.getSystemColor(SWT.COLOR_BLACK);
		// ImageData ideaData = new ImageData(size, size, 32, new
		// PaletteData(0,0,0));
		// ideaData.alpha = 0;
		// Image image = new Image(Display.getCurrent(), ideaData);// size,
		// size);
		Image image = new Image(display, DEFAULT_WIDTH, DEFAULT_HEIGHT);
		GC imggc = new GC(image);

		PaletteData palette = new PaletteData(new RGB[] { new RGB(0, 0, 0), new RGB(0xFF, 0xFF, 0xFF), });
		ImageData maskData = new ImageData(DEFAULT_WIDTH, DEFAULT_HEIGHT, 1, palette);
		Image mask = new Image(Display.getCurrent(), maskData);
		GC maskgc = new GC(mask);
		maskgc.setBackground(black);
		maskgc.fillRectangle(0, 0, DEFAULT_WIDTH, DEFAULT_HEIGHT);
		// draw(gc, list, white, white);
		getImage(imggc, maskgc);
		maskgc.dispose();
		imggc.dispose();
		ImageData imageData = image.getImageData();
		maskData = mask.getImageData();

		Image icon = new Image(display, imageData, maskData);
		ColorManager.getInstance().addImage(this, icon);
		changed = false;

		return icon;
	}

	protected final static int DEFAULT_WIDTH = 20;

	protected final static int DEFAULT_HEIGHT = 20;

	public abstract void getImage(GC imgGC, GC maskGC);
	
	public boolean hasSubstyles() {
		if (this.substyle == null)
			return false;
		// TODO Auto-generated method stub
		return !this.substyle.isEmpty();
	}

	public boolean contains(Style style) {
		if (hasSubstyles()) {
			return this.substyle.contains(style);
		}
		return false;
	}

	public Style[] getSubstyles() {
		if (!hasSubstyles())
			return null;
		return this.substyle.toArray(new Style[this.substyle.size()]);
	}

	public void setDefaultLineColor(Color color) {
		this.lineColor.setDefault(color);	
		this.setChanged(true);
	}

	public void setDefaultFillColor(Color color) {
		this.fillColor.setDefault(color);
		this.setChanged(true);
	}

}
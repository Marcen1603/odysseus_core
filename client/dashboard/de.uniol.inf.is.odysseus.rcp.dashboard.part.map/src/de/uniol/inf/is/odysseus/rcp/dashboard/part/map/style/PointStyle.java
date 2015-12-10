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

import org.eclipse.nebula.cwt.svg_custom.SvgDocument;
import org.eclipse.nebula.cwt.svg_custom.SvgDocument.VARIABLE;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Rectangle;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.rcp.dashboard.part.map.model.style.PersistentCondition;
import de.uniol.inf.is.odysseus.rcp.dashboard.part.map.style.expression.ColorCondition;
import de.uniol.inf.is.odysseus.rcp.dashboard.part.map.style.expression.IntegerCondition;
import de.uniol.inf.is.odysseus.rcp.dashboard.part.map.style.expression.ShapeCondition;

public class PointStyle extends Style {

	private static final String DEFAULT = "<?xml version=\"1.0\" standalone=\"no\"?>"+
											"<!DOCTYPE svg PUBLIC \"-//W3C//DTD SVG 1.1//EN\"" + 
											"\"http://www.w3.org/Graphics/SVG/1.1/DTD/svg11.dtd\">" +
											"<svg width=\"16\" height=\"16\" xmlns=\"http://www.w3.org/2000/svg\" version=\"1.1\">"+
											"<circle cx=\"8\" cy=\"8\" r=\"7.5\" "+
											"fill=\"$VAR$\" stroke=\"$VAR$\" stroke-width=\"$VAR$\"  />"+
											"</svg>";;
	private ShapeCondition shape = null;
	private IntegerCondition size = null;

	public PointStyle(ShapeCondition shape, IntegerCondition size, IntegerCondition lineWidth, ColorCondition lineColor, ColorCondition fillColor) {
		this.shape = shape;
		this.size = size;
		this.setLineWidth(lineWidth);
		this.setLineColor(lineColor);
		this.setFillColor(fillColor);
	}

	public PointStyle() {
		super();
		this.shape = new ShapeCondition(new PersistentCondition(DEFAULT));
		this.size = new IntegerCondition(5);
	}

	@Override
	public void init(SDFSchema schema) {
		if (this.size != null) this.size.init(schema);
		super.init(schema);
	}
	
	public ShapeCondition getShape() {
		return shape;
	}

	public SvgDocument getShape(Tuple<?> tuple) {
		return shape.eval(tuple);
	}

	
	@Override
	public void draw(GC gc, int[] list, Tuple<?> tuple) {
		draw(gc, list, getLineColor(tuple), getFillColor(tuple), tuple);
		super.draw(gc, list, tuple);
	}

	@Override
	protected void draw(GC gc, int[] list, Color fcolor, Color bcolor, Tuple<?> tuple) {
		Integer size = this.getSize(tuple);
		Rectangle bounds = new Rectangle(list[0]-size/2, list[1]-size/2, size, size);
		@SuppressWarnings("unused")
		Color tmpb = gc.getBackground();
		Color tmp = gc.getForeground();
		gc.setForeground(fcolor);
		SvgDocument svgDoc = getShape(tuple);
		svgDoc.setVariable(VARIABLE.FillColor, bcolor);
		svgDoc.setVariable(VARIABLE.LineColor, fcolor);
		svgDoc.setVariable(VARIABLE.LineWidth, this.getLineWidth(tuple));
		svgDoc.apply(gc, bounds);
		gc.setForeground(tmp);
		super.draw(gc, list, tuple);
	}

	@Override
	public void getImage(GC imgGC, GC maskGC) {
		int[] list = { DEFAULT_WIDTH / 2, DEFAULT_HEIGHT / 2 };
		draw(imgGC, list, null);
		draw(maskGC, list, null);		
	}

	public IntegerCondition getSize() {
		return this.size;
	}

	public Integer getSize(Tuple<?> tuple) {
		return this.size.eval(tuple);
	}
}
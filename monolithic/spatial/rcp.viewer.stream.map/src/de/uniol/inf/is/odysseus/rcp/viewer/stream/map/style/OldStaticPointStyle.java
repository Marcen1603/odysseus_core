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

import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;
import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.rcp.viewer.stream.map.style.expression.ColorCondition;
import de.uniol.inf.is.odysseus.rcp.viewer.stream.map.style.expression.IntegerCondition;

/**
 * @author Stephan Jansen
 * @author Kai Pancratz
 * 
 */
public class OldStaticPointStyle extends Style {

	public static enum SHAPE {
		CIRCLE, TRIANGLE, SQUARE
	}

	private SHAPE shape = null;
	private IntegerCondition size = null;

	public OldStaticPointStyle(SHAPE shape, IntegerCondition size, IntegerCondition lineWidth, ColorCondition lineColor, ColorCondition fillColor) {
		this.shape = shape;
		this.size = size;
		this.setLineWidth(lineWidth);
		this.setLineColor(lineColor);
		this.setFillColor(fillColor);
	}

	public OldStaticPointStyle() {
		super();
		this.shape = SHAPE.CIRCLE;
		this.size = new IntegerCondition(5);
	}

	@Override
	public void init(SDFSchema schema) {
		if (this.size != null) this.size.init(schema);
		super.init(schema);
	}
	
	public SHAPE getShape() {
		return shape;
	}
	
	@Override
	public void draw(GC gc, int[] list, Tuple<?> tuple) {
		draw(gc, list, getLineColor(tuple), getFillColor(tuple), tuple);
		super.draw(gc, list, tuple);
	}

	@Override
	protected void draw(GC gc, int[] list, Color fcolor, Color bcolor, Tuple<?> tuple) {
		Integer size = this.getSize(tuple);
		int halfsize = size / 2;
		Color tmpb = gc.getBackground();
		Color tmp = gc.getForeground();
		gc.setForeground(fcolor);
		switch (this.shape) {
		case CIRCLE:
			if (getFillColor() != null) {
				gc.setBackground(bcolor);
				gc.fillOval(list[0] - halfsize, list[1] - halfsize, size, size);
				gc.setBackground(tmpb);
			}
			gc.drawOval(list[0] - halfsize, list[1] - halfsize, size, size);
			break;
		case TRIANGLE:
			int[] corners = { list[0], list[1] - halfsize, list[0] + halfsize,
					list[1] - halfsize, list[0] + halfsize, list[1] + halfsize };
			if (getFillColor() != null) {
				gc.setBackground(bcolor);
				gc.fillPolygon(corners);
				gc.setBackground(tmpb);
			}
			gc.drawPolygon(corners);
			break;
		case SQUARE:
			if (getFillColor() != null) {
				gc.setBackground(bcolor);
				gc.fillRectangle(list[0] - halfsize, list[1] - halfsize, size,
						size);
				gc.setBackground(tmpb);
			}
			gc.drawRectangle(list[0] - halfsize, list[1] - halfsize, size, size);
			break;
			default:
		}
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
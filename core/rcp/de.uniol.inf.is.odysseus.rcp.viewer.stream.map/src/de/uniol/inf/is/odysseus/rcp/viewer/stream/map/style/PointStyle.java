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

import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;

/**
 * @author Stephan Jansen
 * @author Kai Pancratz
 * 
 */
public class PointStyle extends Style {

	public static enum SHAPE {
		CIRCLE, TRIANGLE, SQUARE
	}

	private SHAPE shape;
	int size = 1;

	public PointStyle(SHAPE shape, int size, int lineWidth, Color lineColor,
			Color fillColor) {
		super();
		this.shape = shape;
		this.size = size;
		this.setLineWidth(lineWidth);
		this.setLineColor(lineColor);
		this.setFillColor(fillColor);
	}

	@Override
	public void draw(GC gc, int[] list) {
		draw(gc, list, getLineColor(), getFillColor());
		super.draw(gc, list);
	}

	protected void draw(GC gc, int[] list, Color fcolor, Color bcolor) {
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

		}
		gc.setForeground(tmp);
		super.draw(gc, list);
	}

	@Override
	public Image getImage() {
		int[] list = { DEFAULT_WIDTH / 2, DEFAULT_HEIGHT / 2 };
		return getImage(list);
	}

}
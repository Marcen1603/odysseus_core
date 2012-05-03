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
public class LineStyle extends Style {

	public LineStyle(int linewidth, Color lineColor) {
		super();
		this.setLineWidth(linewidth);
		this.setLineColor(lineColor);
		this.setFillColor(null);
	}

	@Override
	public void draw(GC gc, int[] list) {
		draw(gc, list,  getLineColor(), getFillColor());
	}

	//@Override
	protected void draw(GC gc, int[] list, Color fcolor, Color bcolor) {
		gc.setLineWidth(this.getLineWidth());
		Color tmp = gc.getForeground();
		gc.setForeground(fcolor);
		gc.drawPolyline(list);
		gc.setForeground(tmp);
		super.draw(gc, list);
	}

	@Override
	public Image getImage() {
		int width = getLineWidth();
		int[] list = {
				width, DEFAULT_HEIGHT/2, 
				DEFAULT_WIDTH/2, width, 
				DEFAULT_WIDTH/2, DEFAULT_HEIGHT-width, 
				DEFAULT_WIDTH-width, DEFAULT_HEIGHT/2
				};
		return getImage(list);
	}

}
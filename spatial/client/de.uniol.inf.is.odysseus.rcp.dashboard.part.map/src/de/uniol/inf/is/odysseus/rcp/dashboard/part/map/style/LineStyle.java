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

import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;
import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.rcp.dashboard.part.map.style.expression.ColorCondition;
import de.uniol.inf.is.odysseus.rcp.dashboard.part.map.style.expression.IntegerCondition;


public class LineStyle extends Style {

	public LineStyle(IntegerCondition linewidth, ColorCondition lineColor) {
		super();
		this.setLineWidth(linewidth);
		this.setLineColor(lineColor);
		this.setFillColor(null);
	}

	public LineStyle() {
		super();
		this.setFillColor(null);
	}

	@Override
	public void draw(GC gc, int[] list, Tuple<?> tuple) {
		draw(gc, list,  getLineColor(tuple), getFillColor(tuple), tuple);
	}

	//@Override
	@Override
	protected void draw(GC gc, int[] list, Color fcolor, Color bcolor, Tuple<?> t) {
		gc.setLineWidth(this.getLineWidth(t));
		Color tmp = gc.getForeground();
		gc.setForeground(fcolor);
		gc.drawPolyline(list);
		gc.setForeground(tmp);
		super.draw(gc, list, t);
	}

	@Override
	public void getImage(GC imgGC, GC maskGC) {
		int width = getLineWidth(null);
		int[] list = {
				width, DEFAULT_HEIGHT/2, 
				DEFAULT_WIDTH/2, DEFAULT_HEIGHT/5*4, 
				DEFAULT_WIDTH/2, DEFAULT_HEIGHT/5, 
				DEFAULT_WIDTH-width, DEFAULT_HEIGHT/2
				};
		draw(imgGC, list, null);
		draw(maskGC, list, null);
	}

}
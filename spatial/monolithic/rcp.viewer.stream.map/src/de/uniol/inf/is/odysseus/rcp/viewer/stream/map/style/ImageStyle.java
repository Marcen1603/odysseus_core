/*******************************************************************************
 * Copyright 2012 The Odysseus Team
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
package de.uniol.inf.is.odysseus.rcp.viewer.stream.map.style;

import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.rcp.viewer.stream.map.model.style.PersistentCondition;
import de.uniol.inf.is.odysseus.rcp.viewer.stream.map.style.expression.ColorCondition;

/**
 * @author Stephan Jansen
 * @author Kai Pancratz
 * 
 */
public class ImageStyle extends Style {

	public ImageStyle() {
		setFillColor(new ColorCondition(new PersistentCondition("#FF00FF")));
		setLineColor(new ColorCondition(new PersistentCondition("#5500FF")));
		setChanged(true);
	}

	@Override
	public void getImage(GC imgGC, GC maskGC) {
		int[] list = new int[(DEFAULT_WIDTH / 5 + DEFAULT_HEIGHT / 5) * 4 - 8];
		int index = 0;
		for (int i = 5; i < DEFAULT_WIDTH; i += 5) {
			list[index++] = i;
			list[index++] = 0;
			list[index++] = i;
			list[index++] = DEFAULT_HEIGHT;
		}
		for (int j = 5; j < DEFAULT_HEIGHT; j += 5) {
			list[index++] = 0;
			list[index++] = j;
			list[index++] = DEFAULT_WIDTH;
			list[index++] = j;
		}
		draw(imgGC, list, null);
		draw(maskGC, list, null);
	}

	@Override
	public void draw(GC gc, int[] list, Tuple<?> tuple) {
		draw(gc, list, getLineColor(tuple), getFillColor(tuple), tuple);
	}

	@Override
	protected void draw(GC gc, int[] list, Color fcolor, Color bcolor, Tuple<?> tuple) {
		int i = 0;
		gc.setForeground(fcolor);
		gc.setForeground(bcolor);
		gc.setLineWidth(1);
		while (i < list.length) {
			gc.drawLine(list[i++], list[i++], list[i++], list[i++]);
		}
	}

}

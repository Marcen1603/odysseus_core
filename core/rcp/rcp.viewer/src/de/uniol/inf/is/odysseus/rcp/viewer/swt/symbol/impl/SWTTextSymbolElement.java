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
package de.uniol.inf.is.odysseus.rcp.viewer.swt.symbol.impl;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.widgets.Display;

import com.google.common.base.Strings;

import de.uniol.inf.is.odysseus.core.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.rcp.viewer.view.INodeView;
import de.uniol.inf.is.odysseus.rcp.viewer.view.Vector;

public class SWTTextSymbolElement<C> extends UnfreezableSWTSymbolElement<C> {

	private Font font;
	
	private int lastWidth;
	private int lastTextWidth;
	private int lastTextHeight;
	
	private Color color;
	
	public SWTTextSymbolElement(Color color){
		this.color = color;
	}
	
	@Override
	public void draw(Vector position, int width, int height, Vector screenShift, float zoomFactor) {
		GC gc = getActualGC();
		INodeView<C> view = getNodeView();
		C content = view.getModelNode().getContent();
		String name = view.getModelNode().getContent().getClass().getSimpleName();
		if( content instanceof IPhysicalOperator ) {
			name = getRealName((IPhysicalOperator)content);
		}

		boolean ok = false;
		int textWidth = 0;
		int textHeight = 0;
		int fontSize = 10;
		
		// calculate ideal font-size
		// save width to cache font...
		while( !ok && lastWidth != width ) {
			
			if( font != null ) {
				font.dispose();
				font = null;
			}
			
			font = new Font(Display.getDefault(), "Arial", fontSize, SWT.BOLD);
			gc.setFont(font);
			textWidth = 0;
			for( char c : name.toCharArray() ) {
				textWidth += gc.getAdvanceWidth(c);
			}
			textHeight = gc.getFontMetrics().getHeight();
			
			if( textWidth > width * 0.9 ) {
				fontSize--;
				if( fontSize < 2 ) {
					font = null;
					return;
				}
			} else {
				ok = true;
				lastWidth = width;
				lastTextWidth = textWidth;
				lastTextHeight = textHeight;
			}
		}
		
		if( font != null ) {
			final int x = ((int)position.getX()) + (width / 2) - (lastTextWidth / 2); 
			final int y = ((int)position.getY()) + (height / 2) - ( lastTextHeight / 2);

			gc.setFont(font);
			gc.setForeground(color);
			gc.drawText(name, x, y, true);
		}
	}

	private static String getRealName(IPhysicalOperator operator) {
		String name = operator.getName();
		if( Strings.isNullOrEmpty(name)) {
			return "[No name]";
		}
		
		int index1 = name.indexOf("(");
		int index2 = name.indexOf(" ");
		if( index1 == -1 && index2 == -1 ) {
			return name;
		}
		
		int pos = index1 == -1 ? index2 : index1;
		return name.substring(0, pos);
	}
}

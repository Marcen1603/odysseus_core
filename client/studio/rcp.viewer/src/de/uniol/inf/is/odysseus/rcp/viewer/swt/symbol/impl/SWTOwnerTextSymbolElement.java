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

import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.widgets.Display;

import de.uniol.inf.is.odysseus.core.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.core.planmanagement.IOperatorOwner;
import de.uniol.inf.is.odysseus.core.planmanagement.IOwnedOperator;
import de.uniol.inf.is.odysseus.rcp.viewer.view.INodeView;
import de.uniol.inf.is.odysseus.rcp.viewer.view.Vector;

public class SWTOwnerTextSymbolElement<C> extends UnfreezableSWTSymbolElement<C> {

	private Font font;
	private Vector offset;
	private boolean useOffsetX;
	private boolean useOffsetY;
	private Color color;

	public SWTOwnerTextSymbolElement(Vector offsetFromParams, boolean useOffsetX, boolean useOffsetY, Color color) {
		this.offset = offsetFromParams;
		this.useOffsetX = useOffsetX;
		this.useOffsetY = useOffsetY;
		this.color = color;
	}

	@Override
	public void draw(Vector position, int width, int height, Vector screenShift, float zoomFactor) {
		GC gc = getActualGC();
		INodeView<C> view = getNodeView();
		C content = view.getModelNode().getContent();

		String name;
		if (content instanceof IPhysicalOperator) {
			name = getRealName((IPhysicalOperator) content);
		} else {
			name = view.getModelNode().getContent().getClass().getSimpleName();
		}

		boolean ok = false;
		int textWidth = 0;
		int textHeight = 0;
		int fontSize = 20;

		int lastWidth = 0;
		int lastTextHeight = 0;

		double availableHeight = useOffsetY ? height - offset.getY() / 100.0 * height : height * 0.9;
		double availableWidth = useOffsetX ? width - offset.getX() / 100.0 * width : width * 0.9;

		// calculate ideal font-size
		// save width to cache font...
		while (!ok && lastWidth != width) {

			if (font != null) {
				font.dispose();
				font = null;
			}

			font = new Font(Display.getDefault(), "Arial", fontSize, SWT.BOLD);
			gc.setFont(font);
//			textWidth = determineTextLength(gc, "someReallyLongText");
			textWidth = determineTextLength(gc, name);
			textHeight = gc.getFontMetrics().getHeight();

			if (textWidth > availableWidth || textHeight > availableHeight) {
				fontSize--;
				if (fontSize < 2) {
					font = null;
					return;
				}
			} else {
				ok = true;
				lastWidth = width;
				lastTextHeight = textHeight;
			}
		}

		if (font != null) {
			final int realTextWidth = determineTextLength(gc, name);

			final int x = useOffsetX ? (int) (position.getX() + offset.getX() / 100.0 * realTextWidth)
					: ((int) position.getX()) + (width / 2) - (realTextWidth / 2);
			final int y = useOffsetY ? (int) (position.getY() + offset.getY() / 100.0 * height)
					: ((int) position.getY()) + (height / 2) - (lastTextHeight / 2);
			final int ownerID = determineFirstOwnerID(getNodeView().getModelNode().getContent());

			gc.setFont(font);
			if (color == null){
				if (isNodeContentOpen()) {
					gc.setForeground(OwnerColorManager.getOwnerTextColor(ownerID));
				} else {
					gc.setForeground(OwnerColorManager.getInactiveOwnerTextColor(ownerID));
				}
			} else {
				gc.setForeground(color);
			}

			gc.drawText(name, x, y, true);
		}
	}

	private boolean isNodeContentOpen() {
		C content = getNodeView().getModelNode().getContent();
		if (content instanceof IPhysicalOperator) {
			IPhysicalOperator operator = (IPhysicalOperator) content;
			if (operator.isOpen()) {
				return true;
			}
		}
		return false;
	}

	private static int determineTextLength(GC gc, String text) {
		int textWidth = 0;
		for (char c : text.toCharArray()) {
			textWidth += gc.getAdvanceWidth(c);
		}
		return textWidth;
	}

	private static int determineFirstOwnerID(Object content) {
		if (!(content instanceof IOwnedOperator)) {
			return 0;
		}

		IOwnedOperator op = (IOwnedOperator) content;
		List<IOperatorOwner> owner = op.getOwner();
		if (!owner.isEmpty()) {
			return owner.get(0).getID();
		}

		return 0;
	}

	private static String getRealName(IPhysicalOperator operator) {
		return operator.getName();
		// String name = operator.getName();
		// if( Strings.isNullOrEmpty(name)) {
		// return "[No name]";
		// }
		//
		// int index1 = name.indexOf("(");
		// int index2 = name.indexOf(" ");
		// if( index1 == -1 && index2 == -1 ) {
		// return name;
		// }
		//
		// int pos = index1 == -1 ? index2 : index1;
		// return name.substring(0, pos);
	}
}

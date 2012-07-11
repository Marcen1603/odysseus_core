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
import org.eclipse.swt.graphics.GC;
import org.eclipse.ui.PlatformUI;

import com.google.common.collect.Lists;

import de.uniol.inf.is.odysseus.core.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.core.planmanagement.IOperatorOwner;
import de.uniol.inf.is.odysseus.rcp.viewer.view.Vector;

public class SWTOwnerSymbolElement<C> extends UnfreezableSWTSymbolElement<C>{

	private static final int OWNER_CIRCLE_RADIUS_PIXELS = 10; 
	private static final int OWNER_CIRCLE_SPACE_PIXELS = 5;
	private static final int OWNER_CIRCLE_STEP_PIXELS = OWNER_CIRCLE_RADIUS_PIXELS + OWNER_CIRCLE_SPACE_PIXELS;
	
	private static final Color OWNER_CIRCLE_BORDER_COLOR = getSystemColor(SWT.COLOR_BLACK);
	private static final Color[] OWNER_COLORS = new Color[] {
		getSystemColor(SWT.COLOR_BLUE),
		getSystemColor(SWT.COLOR_CYAN),
		getSystemColor(SWT.COLOR_GREEN),
		getSystemColor(SWT.COLOR_RED),
		getSystemColor(SWT.COLOR_YELLOW),
		getSystemColor(SWT.COLOR_GRAY),
		getSystemColor(SWT.COLOR_MAGENTA),
		getSystemColor(SWT.COLOR_WHITE),
		getSystemColor(SWT.COLOR_BLACK),
		getSystemColor(SWT.COLOR_DARK_BLUE),
		getSystemColor(SWT.COLOR_DARK_CYAN),
		getSystemColor(SWT.COLOR_DARK_GRAY),
		getSystemColor(SWT.COLOR_DARK_GREEN),
		getSystemColor(SWT.COLOR_DARK_MAGENTA),
		getSystemColor(SWT.COLOR_DARK_YELLOW),
		getSystemColor(SWT.COLOR_DARK_RED),
	};
	
	@Override
	public void draw(Vector position, int width, int height, float zoomFactor) {
		List<Integer> ownerIDs = determineOwnerIDs(getNodeView().getModelNode().getContent());
		drawOwnerCircles(position, width, zoomFactor, ownerIDs);
	}

	private void drawOwnerCircles(Vector position, int width, float zoomFactor, List<Integer> ownerIDs) {
		int x = (int)position.getX();
		int y = (int)position.getY();
		
		int realStepPixels = (int)(OWNER_CIRCLE_STEP_PIXELS * zoomFactor);
		for( Integer ownerID : ownerIDs ) {
			drawOwnerCicle(getActualGC(), x, y, zoomFactor, ownerID);
			
			if( x + realStepPixels > x + width ) {
				x = (int)position.getX();
				y += realStepPixels;
			} else {
				x += realStepPixels;
			}
		}
	}

	private static void drawOwnerCicle(GC actualGC, int x, int y, float zoomFactor, Integer ownerID) {
		actualGC.setBackground(OWNER_COLORS[ownerID % OWNER_COLORS.length]);
		actualGC.setForeground(OWNER_CIRCLE_BORDER_COLOR);
		
		int realRadius = (int)(OWNER_CIRCLE_RADIUS_PIXELS* zoomFactor);
		actualGC.fillOval(x, y, realRadius, realRadius);
		actualGC.drawOval(x, y, realRadius, realRadius);
	}

	private static List<Integer> determineOwnerIDs(Object content) {
		if( !(content instanceof IPhysicalOperator )) {
			return Lists.newArrayList((Integer)0);
		}
		
		IPhysicalOperator operator = (IPhysicalOperator)content;
		
		List<Integer> ownerIDs = Lists.newArrayList();
		for(IOperatorOwner owner : operator.getOwner()) {
			if( !ownerIDs.contains(owner.getID())) {
				ownerIDs.add(owner.getID());
			}
		}
		
		return ownerIDs;
	}
	
	private static Color getSystemColor( int color ) {
		return PlatformUI.getWorkbench().getDisplay().getSystemColor(color);
	}
}

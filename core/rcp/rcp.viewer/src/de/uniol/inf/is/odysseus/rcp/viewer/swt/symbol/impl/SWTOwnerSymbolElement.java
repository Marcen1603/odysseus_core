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

import de.uniol.inf.is.odysseus.core.planmanagement.IOperatorOwner;
import de.uniol.inf.is.odysseus.core.planmanagement.IOwnedOperator;
import de.uniol.inf.is.odysseus.rcp.viewer.view.Vector;

public class SWTOwnerSymbolElement<C> extends UnfreezableSWTSymbolElement<C> {

	private static final int OWNER_CIRCLE_RADIUS_PIXELS = 10;
	private static final int OWNER_CIRCLE_SPACE_PIXELS = 5;
	private static final int OWNER_CIRCLE_STEP_PIXELS = OWNER_CIRCLE_RADIUS_PIXELS + OWNER_CIRCLE_SPACE_PIXELS;
	
	private static final int RECT_ROUND_SIZE_PIXELS = 20;

	private static final Color OWNER_BORDER_COLOR = PlatformUI.getWorkbench().getDisplay().getSystemColor(SWT.COLOR_BLACK);
	private static final Color OWNERLESS_FILL_COLOR = PlatformUI.getWorkbench().getDisplay().getSystemColor(SWT.COLOR_WHITE);
	
	@Override
	public void draw(Vector position, int width, int height, Vector screenShift, float zoomFactor) {
		List<Integer> ownerIDs = determineOwnerIDs(getNodeView().getModelNode().getContent());

		if (!ownerIDs.isEmpty()) {
			drawOwnerRectangle(position, width, height, zoomFactor, ownerIDs.get(0));

			ownerIDs.remove(0);
			drawOwnerCircles(position, width, zoomFactor, ownerIDs);
		} else {
			drawOwnerlessRectangle(position, width, height, zoomFactor);
		}
	}

	private void drawOwnerlessRectangle(Vector pos, int width, int height, float zoomFactor) {
		GC gc = getActualGC();
		int round = (int)(RECT_ROUND_SIZE_PIXELS * zoomFactor);
		
		gc.setBackground(OWNERLESS_FILL_COLOR);
		gc.setForeground(OWNER_BORDER_COLOR);
		gc.fillRoundRectangle((int) pos.getX(), (int) pos.getY(), width, height, round, round);
		gc.drawRoundRectangle((int) pos.getX(), (int) pos.getY(), width, height, round, round);
	}

	private void drawOwnerRectangle(Vector pos, int width, int height, float zoomFactor, int ownerID) {
		GC gc = getActualGC();
		int round = (int)(RECT_ROUND_SIZE_PIXELS * zoomFactor);

		gc.setBackground(OwnerColorManager.getOwnerBackgroundColor(ownerID));
		gc.fillRoundRectangle((int) pos.getX(), (int) pos.getY(), width, height, round, round);
	}

	private void drawOwnerCircles(Vector position, int width, float zoomFactor, List<Integer> ownerIDs) {
		if (ownerIDs.isEmpty()) {
			return;
		}

		int x = (int) position.getX();
		int y = (int) position.getY();

		int realStepPixels = (int) (OWNER_CIRCLE_STEP_PIXELS * zoomFactor);
		for (Integer ownerID : ownerIDs) {
			drawOwnerCicle(getActualGC(), x, y, zoomFactor, ownerID);

			if (x + realStepPixels > x + width) {
				x = (int) position.getX();
				y += realStepPixels;
			} else {
				x += realStepPixels;
			}
		}
	}

	private static void drawOwnerCicle(GC actualGC, int x, int y, float zoomFactor, Integer ownerID) {
		actualGC.setBackground(OwnerColorManager.getOwnerBackgroundColor(ownerID));
		actualGC.setForeground(OWNER_BORDER_COLOR);

		int realRadius = (int) (OWNER_CIRCLE_RADIUS_PIXELS * zoomFactor);
		actualGC.fillOval(x, y, realRadius, realRadius);
		actualGC.drawOval(x, y, realRadius, realRadius);
	}

	private static List<Integer> determineOwnerIDs(Object content) {
		if (!(content instanceof IOwnedOperator)) {
			return Lists.newArrayList();
		}

		IOwnedOperator operator = (IOwnedOperator) content;

		List<Integer> ownerIDs = Lists.newArrayList();
		for (IOperatorOwner owner : operator.getOwner()) {
			if (!ownerIDs.contains(owner.getID())) {
				ownerIDs.add(owner.getID());
			}
		}

		return ownerIDs;
	}

}

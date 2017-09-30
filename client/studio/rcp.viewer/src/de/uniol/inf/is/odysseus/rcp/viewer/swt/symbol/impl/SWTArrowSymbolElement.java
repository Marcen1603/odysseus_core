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
package de.uniol.inf.is.odysseus.rcp.viewer.swt.symbol.impl;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Display;

import de.uniol.inf.is.odysseus.core.physicaloperator.ISink;
import de.uniol.inf.is.odysseus.core.physicaloperator.ISource;
import de.uniol.inf.is.odysseus.core.physicaloperator.AbstractPhysicalSubscription;
import de.uniol.inf.is.odysseus.rcp.viewer.swt.symbol.SWTConnectionSymbolElement;
import de.uniol.inf.is.odysseus.rcp.viewer.view.IConnectionView;
import de.uniol.inf.is.odysseus.rcp.viewer.view.INodeView;
import de.uniol.inf.is.odysseus.rcp.viewer.view.Vector;

public class SWTArrowSymbolElement<C> extends SWTConnectionSymbolElement<C> {

	private static final int ARROW_SIZE = 20;
	
	public SWTArrowSymbolElement(Color activeLineColor, Color inactiveLineColor, Color suspendColor, Color partialColor ) {
		super(activeLineColor, inactiveLineColor, suspendColor, partialColor);
	}	

	@Override
	public void draw(Vector start, Vector end, Vector screenShift, float zoomFactor) {
		GC actualGC = getActualGC();
	
		setContextColor(actualGC);
	
		setContextLineSize(actualGC);
		actualGC.drawLine((int) start.getX(), (int) start.getY(), (int) end.getX(), (int) end.getY());
		actualGC.setLineWidth(1);

		final INodeView<C> node = getConnectionView().getViewedEndNode();
		if (node == null) {
			return;
		}

		final float nx = (float) start.getX();
		final float ny = (float) start.getY();
		final float mx = (float) end.getX();
		final float my = (float) end.getY();

		final float fx = (float) (node.getPosition().getX() + screenShift.getX()) * zoomFactor;
		final float fy = (float) (node.getPosition().getY() + screenShift.getY()) * zoomFactor;
		final float gx = fx + node.getWidth() * zoomFactor;
		final float gy = fy;
		final float hx = fx;
		final float hy = fy + node.getHeight() * zoomFactor;
		final float ix = fx + node.getWidth() * zoomFactor;
		final float iy = fy + node.getHeight() * zoomFactor;

		// Schnittpunktberechnung
		// Quelle: http://www.delphipraxis.net/topic85928.html
		Point cross = null;
		cross = crossingPoint(mx, my, nx, ny, hx, hy, ix, iy); // unten
		if (cross == null) {
			cross = crossingPoint(mx, my, nx, ny, fx, fy, gx, gy); // oben
			if (cross == null) {
				cross = crossingPoint(mx, my, nx, ny, fx, fy, hx, hy); // links
				if (cross == null) {
					cross = crossingPoint(mx, my, nx, ny, gx, gy, ix, iy); // rechts
				}
			}
		}

		if (cross != null) {

			// Pfeil zeichnen
			float ox = nx - mx;
			float oy = ny - my;

			final float length = (float) Math.sqrt(ox * ox + oy * oy);
			ox = (ox / length) * ARROW_SIZE;
			oy = (oy / length) * ARROW_SIZE;

			final int px = (int) (cross.x + (ox - oy * 0.3f) * zoomFactor);
			final int py = (int) (cross.y + (oy + ox * 0.3f) * zoomFactor);
			final int qx = (int) (cross.x + (ox + oy * 0.3f) * zoomFactor);
			final int qy = (int) (cross.y + (oy - ox * 0.3f) * zoomFactor);

			int[] poly = new int[] { cross.x - 1, cross.y, px, py, qx, qy };

			
			setContextBackgroundColor(actualGC);
			actualGC.fillPolygon(poly);

			// // DEBUG
			// getActualGC().setBackground(Display.getCurrent().getSystemColor(SWT.COLOR_RED));
			// getActualGC().fillOval(cross.x - 5, cross.y - 5, 10, 10);
		}

		double distX = end.getX() - start.getX();
		double distY = end.getY() - start.getY();

		int port = getOutputPortNumber(getConnectionView());
		if (port > 0) {
			actualGC.setForeground(Display.getCurrent().getSystemColor(SWT.COLOR_BLACK));
			actualGC.setBackground(Display.getCurrent().getSystemColor(SWT.COLOR_WHITE));
			actualGC.drawString("" + port, (int) (start.getX() + distX / 3), (int) (start.getY() + distY / 3), true);
		}

		port = getInputPortNumber(getConnectionView());
		if (port > 0) {
			actualGC.setForeground(Display.getCurrent().getSystemColor(SWT.COLOR_BLACK));
			actualGC.setBackground(Display.getCurrent().getSystemColor(SWT.COLOR_WHITE));
			actualGC.drawString("" + port, (int) (start.getX() + (distX / 1.2)), (int) (start.getY() + (distY / 1.2)), true);
		}
	}
	
	@Override
	public void update() {
	}

	private static Point crossingPoint(float ax1, float ay1, float ax2, float ay2, float bx1, float by1, float bx2, float by2) {
		float n = ax1 * by2 + bx2 * ay2 + ax2 * by1 + bx1 * ay1 - bx2 * ay1 - ax1 * by1 - bx1 * ay2 - ax2 * by2;
		if (n < 0.0000001f && n > -0.0000001f) {
			// n ist gewissermaßen 0
			// kein Schnittpunkt
			return null;
		}

		float t = (ax1 * ay2 + ax2 * by1 + bx1 * ay1 - ax2 * ay1 - bx1 * ay2 - ax1 * by1) / n;
		if (t < 0 || t > 1)
			return null;

		float s = (ax1 * by2 + bx2 * by1 + bx1 * ay1 - bx2 * ay1 - bx1 * by2 - ax1 * by1) / n;
		if (s < 0 || s > 1)
			return null;

		return new Point((int) (bx1 + t * (bx2 - bx1)), (int) (by1 + t * (by2 - by1)));
	}

	private static int getOutputPortNumber(IConnectionView<?> view) {
		Object startObject = view.getModelConnection().getStartNode().getContent();
		Object endObject = view.getModelConnection().getEndNode().getContent();

		if (!(startObject instanceof ISource)) {
			return -1;
		}

		ISource<?> source = (ISource<?>) startObject;
		for (AbstractPhysicalSubscription<?,?> subscription : source.getSubscriptions()) {
			if (subscription.getSink() == endObject) {
				return subscription.getSourceOutPort();
			}
		}

		return -1;
	}

	private static int getInputPortNumber(IConnectionView<?> view) {
		Object startObject = view.getModelConnection().getStartNode().getContent();
		Object endObject = view.getModelConnection().getEndNode().getContent();

		ISink<?> sink = (ISink<?>) endObject;
		for (AbstractPhysicalSubscription<?,?> subscription : sink.getSubscribedToSource()) {
			if (subscription.getSource() == startObject) {
				return subscription.getSinkInPort();
			}
		}

		return -1;
	}
}

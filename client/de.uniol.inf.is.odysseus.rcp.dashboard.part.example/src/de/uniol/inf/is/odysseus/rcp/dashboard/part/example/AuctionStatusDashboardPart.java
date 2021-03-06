/*******************************************************************************
 * Copyright 2012 The Odysseus Team
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
package de.uniol.inf.is.odysseus.rcp.dashboard.part.example;

import java.util.List;
import java.util.Map;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.ui.PlatformUI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPunctuation;
import de.uniol.inf.is.odysseus.rcp.dashboard.AbstractDashboardPart;

public class AuctionStatusDashboardPart extends AbstractDashboardPart implements PaintListener {

	private static final Logger LOG = LoggerFactory.getLogger(AuctionStatusDashboardPart.class);

	private static final long MAX_LIFETIME_MILLIS = 60 * 1000;
	private static final int MARKER_SIZE_PIXELS = 30;
	private static final int MARKER_SPACE_PIXELS = 15;
	private static final int MARKER_STEP = MARKER_SPACE_PIXELS + MARKER_SIZE_PIXELS;

	private Color[] colorMap;
	private final Map<Double, Double> markerMap = Maps.newHashMap();
	private final Map<Double, Long> timestampMap = Maps.newHashMap();

	private Canvas canvas;

	@Override
	public void createPartControl(Composite parent, ToolBar toolbar) {
		canvas = new Canvas(parent, SWT.BORDER);
		canvas.setLayoutData(new GridData(GridData.FILL_BOTH));
		canvas.setBackground(Display.getCurrent().getSystemColor(SWT.COLOR_WHITE));
		canvas.addPaintListener(this);

		colorMap = new Color[] { PlatformUI.getWorkbench().getDisplay().getSystemColor(SWT.COLOR_RED), PlatformUI.getWorkbench().getDisplay().getSystemColor(SWT.COLOR_YELLOW),
				PlatformUI.getWorkbench().getDisplay().getSystemColor(SWT.COLOR_GREEN), };
	}

	@Override
	public void paintControl(PaintEvent e) {
		synchronized (markerMap) {
			checkTimestamps();

			final int markerCount = markerMap.size();

			if (markerCount > 0) {
				final GC gc = e.gc;

				int x = MARKER_SPACE_PIXELS;
				int y = MARKER_SPACE_PIXELS;
				gc.setForeground(Display.getCurrent().getSystemColor(SWT.COLOR_BLACK));
				for (final Double auctionId : markerMap.keySet()) {
					gc.setBackground(colorMap[markerMap.get(auctionId).intValue()]);
					gc.fillOval(x, y, MARKER_SIZE_PIXELS, MARKER_SIZE_PIXELS);
					gc.drawOval(x, y, MARKER_SIZE_PIXELS, MARKER_SIZE_PIXELS);
					gc.drawText(String.valueOf(auctionId.intValue()), x + (MARKER_SIZE_PIXELS / 4), y + (MARKER_SIZE_PIXELS / 4));

					x += MARKER_STEP;
					if (x >= canvas.getSize().x - MARKER_STEP) {
						x = MARKER_SPACE_PIXELS;
						y += MARKER_STEP;
					}
				}
			}
		}
	}

	@Override
	public void punctuationElementRecieved(IPhysicalOperator senderOperator, IPunctuation punctuation, int port) {

	}

	@Override
	public void streamElementRecieved(IPhysicalOperator senderOperator, IStreamObject<?> element, int port) {
		if (!(element instanceof Tuple)) {
			LOG.error("Could not use stream-objects of class {}. Only {} supported.", element.getClass(), Tuple.class);
			return;
		}

		try {
			final Tuple<?> tuple = (Tuple<?>) element;
			final double status = tuple.getAttribute(0);
			final int auctionId = tuple.getAttribute(2);

			updateStatus(auctionId, status);

		} catch (final Throwable t) {
			LOG.error("Could not process Tuple {}!", element, t);
		}
	}

	private void checkTimestamps() {
		final List<Double> oldAuctionIds = Lists.newArrayList();
		for (final Double auctionId : markerMap.keySet()) {
			final long timestamp = timestampMap.get(auctionId);
			if (System.currentTimeMillis() - timestamp > MAX_LIFETIME_MILLIS) {
				oldAuctionIds.add(auctionId);
			}
		}

		for (final Double oldAuctionId : oldAuctionIds) {
			timestampMap.remove(oldAuctionId);
			markerMap.remove(oldAuctionId);
		}
	}

	private void updateStatus(double auctionId, double status) {
		synchronized (markerMap) {
			markerMap.put(auctionId, status);
			timestampMap.put(auctionId, System.currentTimeMillis());
		}

		PlatformUI.getWorkbench().getDisplay().asyncExec(new Runnable() {

			@Override
			public void run() {
				canvas.redraw();
			}

		});
	}
}

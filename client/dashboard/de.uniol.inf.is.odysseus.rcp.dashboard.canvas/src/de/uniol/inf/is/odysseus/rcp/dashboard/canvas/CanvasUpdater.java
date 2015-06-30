/*******************************************************************************
 * Copyright 2015 The Odysseus Team
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
package de.uniol.inf.is.odysseus.rcp.dashboard.canvas;

import org.eclipse.swt.widgets.Canvas;

/**
 * @author Christian Kuka <christian@kuka.cc>
 * @version $Id$
 * 
 */
public class CanvasUpdater extends Thread {
	final Canvas canvas;
	final long delay;

	/**
	 * Class constructor.
	 * 
	 */
	public CanvasUpdater(final Canvas canvas, long delay) {
		this.canvas = canvas;
		this.setName("Canvas updater");
		this.setDaemon(true);
		this.delay = delay;
	}

	boolean stop = false;

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void run() {
		while (!stop && !this.isInterrupted()) {
			if (this.canvas != null && !this.canvas.isDisposed()) {
				this.canvas.getDisplay().asyncExec(new Runnable() {
					@Override
					public void run() {
						if (!CanvasUpdater.this.canvas.isDisposed()) {
							CanvasUpdater.this.canvas.redraw();
						}
					}
				});

				try {
					Thread.sleep(delay);
				} catch (final InterruptedException e) {
					// Empty block
				}
			}
		}
	}

	public void terminate() {
		stop = true;
	}
}

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

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.opengl.GLCanvas;
import org.eclipse.swt.widgets.Event;

/**
 * @author Christian Kuka <christian@kuka.cc>
 * @version $Id$
 *
 */
public class GLCanvasUpdater extends Thread {
    final GLCanvas canvas;
    final AbstractGLCanvasDashboardPart dashboard;

    /**
     * Class constructor.
     *
     */
    public GLCanvasUpdater(final AbstractGLCanvasDashboardPart dashboard, final GLCanvas canvas) {
        this.canvas = canvas;
        this.dashboard = dashboard;
        this.setName("Canvas updater");
        this.setDaemon(true);

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void run() {
        while (!this.isInterrupted()) {
            if (!this.canvas.isDisposed()) {
                this.canvas.getDisplay().asyncExec(new Runnable() {
                    @Override
                    public void run() {
                        if (!GLCanvasUpdater.this.canvas.isDisposed()) {
                            final Rectangle bounds = GLCanvasUpdater.this.canvas.getBounds();
                            final Event event = new Event();
                            event.display = GLCanvasUpdater.this.canvas.getDisplay();
                            event.type = SWT.Paint;
                            event.x = bounds.x;
                            event.y = bounds.y;
                            event.height = bounds.height;
                            event.width = bounds.width;
                            event.widget = GLCanvasUpdater.this.canvas;
                            final PaintEvent paintEvent = new PaintEvent(event);
                            GLCanvasUpdater.this.dashboard.paintControl(paintEvent);
                        }
                    }
                });

                try {
                    Thread.sleep(10);
                }
                catch (final InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}

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
package de.uniol.inf.is.odysseus.rcp.dashboard.canvas.wheel;

import java.util.Map;
import java.util.Random;

import org.eclipse.swt.graphics.Path;
import org.eclipse.swt.graphics.Transform;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.rcp.dashboard.canvas.colorspace.RGB;

/**
 * @author Christian Kuka <christian@kuka.cc>
 * @version $Id: ArrowWheelDashboardPart.java | Sat Apr 11 02:17:02 2015 +0000 |
 *          ckuka $
 *
 */
public class ArrowWheelDashboardPart extends AbstractWheelDashboardPart {
    private final static String ARROW_COLOR = "arrowColor";

    private RGB arrowColor = new RGB(0, 255, 0);

    /**
     * {@inheritDoc}
     */
    @Override
    public void doPaintZ(final Number z) {
        final double angle = this.normalizeZ(z).doubleValue() * 360.0;
        final Path path = new Path(this.getGC().getDevice());
        path.moveTo((int) ((-(1.0 / 3.0) * this.getRadius()) + 4), 1);
        path.lineTo((int) ((1.0 / 6.0) * this.getRadius()), 1);
        path.lineTo((int) ((1.0 / 6.0) * this.getRadius()), 4);
        path.lineTo((int) (((1.0 / 3.0) * this.getRadius()) - 4), 0);
        path.lineTo((int) ((1.0 / 6.0) * this.getRadius()), -4);
        path.lineTo((int) ((1.0 / 6.0) * this.getRadius()), -1);
        path.lineTo((int) ((-(1.0 / 3.0) * this.getRadius()) + 4), -1);
        path.close();
        final Transform transform = new Transform(this.getGC().getDevice());
        transform.translate((float) this.getCenter().x, (float) this.getCenter().y);
        transform.rotate((float) (angle - 90.0));
        this.getGC().setTransform(transform);
        this.fillPath(path, this.arrowColor);
        this.getGC().setTransform(null);
    }

    /**
     * 
     * {@inheritDoc}
     */
    @Override
    public void onLoad(Map<String, String> saved) {
        super.onLoad(saved);
        arrowColor = RGB.valueOf(saved.get(ARROW_COLOR)!=null?saved.get(ARROW_COLOR):"255,0,0");

    }

    /**
     * 
     * {@inheritDoc}
     */
    @Override
    public Map<String, String> onSave() {
        Map<String, String> toSaveMap = super.onSave();
        toSaveMap.put(ARROW_COLOR, String.valueOf(arrowColor));
        return toSaveMap;
    }

    /**
     * @param arrowColor
     *            the arrowColor to set
     */
    public void setArrowColor(final RGB arrowColor) {
        this.arrowColor = arrowColor;
    }

    /**
     * @return the arrowColor
     */
    public RGB getArrowColor() {
        return this.arrowColor;
    }

    public static void main(final String[] args) {

        final Display display = new Display();
        final Shell shell = new Shell(display);

        shell.setSize(500, 500);

        shell.open();
        final ArrowWheelDashboardPart wheel = new ArrowWheelDashboardPart();
        final Thread generator = new Thread() {
            /**
             * {@inheritDoc}
             */
            //@SuppressWarnings("boxing")
            @Override
            public void run() {
                final Random rnd = new Random();
                long i = 0l;
                while (!this.isInterrupted()) {
                    @SuppressWarnings("rawtypes")
                    final Tuple tuple = new Tuple(3, false);
                    tuple.setAttribute(0, i);
                    tuple.setAttribute(1, rnd.nextDouble() * 100);
                    tuple.setAttribute(2, rnd.nextDouble() * 1000);
                    wheel.streamElementReceived(null, tuple, 0);
                    i++;
                    try {
                        Thread.sleep(100);
                    }
                    catch (final InterruptedException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
            }
        };
        wheel.createPartControl(shell, null);
        generator.start();
        while (!shell.isDisposed()) {
            if (!display.readAndDispatch()) {
                display.sleep();
            }
        }
        generator.interrupt();
        display.dispose();
    }

}

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

import java.text.NumberFormat;
import java.util.Map;
import java.util.Random;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.rcp.dashboard.canvas.Coordinate;
import de.uniol.inf.is.odysseus.rcp.dashboard.canvas.colorspace.RGB;

/**
 * @author Christian Kuka <christian@kuka.cc>
 * @version $Id: GaugeWheelDashboardPart.java | Sat Apr 11 02:17:02 2015 +0000 |
 *          ckuka $
 *
 */
public class GaugeWheelDashboardPart extends AbstractWheelDashboardPart {
    private final static String GAUGE_COLOR = "gaugeColor";

    private RGB gaugeColor = new RGB(0, 255, 0);

    /**
     * {@inheritDoc}
     */
    @Override
    public void doPaintZ(final Number z) {
        final double value = (this.normalizeZ(z).doubleValue() - 0.5) * 360.0;
        final RGB gaugeBackgroundColor = this.getForegroundColor().getComplement();
        final RGB complement = this.getGaugeColor().getComplement();
        this.fillArc(this.getCenter(), (int) ((1.0 / 3.0) * this.getRadius()), 0, 360, gaugeBackgroundColor);
        this.fillArc(this.getCenter(), (int) (((1.0 / 3.0) * this.getRadius()) - 4), 180, -180, complement);
        this.fillArc(this.getCenter(), (int) (((1.0 / 3.0) * this.getRadius()) - 4), 180, (int) (-(value + 180) / 2), this.getGaugeColor());
        this.fillArc(this.getCenter(), (int) (((1.0 / 3.0) * this.getRadius()) - 14), 0, 360, gaugeBackgroundColor);
        final String text = NumberFormat.getNumberInstance().format(z);
        this.setBackground(gaugeBackgroundColor);
        final int fontSize = this.getFontSize(text, this.getFont(), (int) ((1.0 / 3.0) * this.getRadius() - 4), (int) ((1.0 / 3.0) * this.getRadius() - 4));
        this.getGC().setFont(new Font(this.getGC().getDevice(), this.getFont(), fontSize, SWT.NORMAL));
        final Coordinate extent = this.textExtent(text);
        this.drawText(text, new Coordinate(this.getCenter().x - (extent.x / 2), this.getCenter().y + (extent.y / 2)), true);
    }

    /**
     * 
     * {@inheritDoc}
     */
    @Override
    public void onLoad(Map<String, String> saved) {
        super.onLoad(saved);
        gaugeColor = RGB.valueOf(saved.get(GAUGE_COLOR) != null ? saved.get(GAUGE_COLOR) : "255,0,0");
    }

    /**
     * 
     * {@inheritDoc}
     */
    @Override
    public Map<String, String> onSave() {
        Map<String, String> toSaveMap = super.onSave();
        toSaveMap.put(GAUGE_COLOR, String.valueOf(gaugeColor));
        return toSaveMap;
    }

    /**
     * @return the gaugeColor
     */
    public RGB getGaugeColor() {
        return this.gaugeColor;
    }

    /**
     * @param gaugeColor
     *            the gaugeColor to set
     */
    public void setGaugeColor(final RGB gaugeColor) {
        this.gaugeColor = gaugeColor;
    }

    public static void main(final String[] args) {

        final Display display = new Display();
        final Shell shell = new Shell(display);

        shell.setSize(500, 500);

        shell.open();
        final GaugeWheelDashboardPart wheel = new GaugeWheelDashboardPart();
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

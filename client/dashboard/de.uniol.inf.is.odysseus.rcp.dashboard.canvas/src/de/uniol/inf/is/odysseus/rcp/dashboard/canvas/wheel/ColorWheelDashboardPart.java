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

import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.rcp.dashboard.canvas.colorspace.CIELCH;
import de.uniol.inf.is.odysseus.rcp.dashboard.canvas.colorspace.RGB;

/**
 * @author Christian Kuka <christian@kuka.cc>
 * @version $Id: ColorWheelDashboardPart.java | Fri Apr 10 23:28:57 2015 +0000 |
 *          ckuka $
 *
 */
public class ColorWheelDashboardPart extends AbstractWheelDashboardPart {
    private final static String BASE_COLOR = "baseColor";
    private RGB baseColor = new RGB(0, 255, 0);

    /**
     * {@inheritDoc}
     */
    @Override
    public void doPaintZ(final Number z) {
        final double value = (this.normalizeZ(z).doubleValue() - 0.5) * 360.0;
        final CIELCH base = this.getBaseColor().toCIELCH();
        for (double r = 0.0; r < 360; r += 0.5) {
            final RGB color = new CIELCH(base.L, base.C, base.H + r).toCIELab().toXYZ().toRGB();
            this.fillArc(this.getCenter(), (int) ((1.0 / 3.0) * this.getRadius()), (int) (r - 90.0), 1, color);

        }
        this.fillArc(this.getCenter(), (int) (((1.0 / 3.0) * this.getRadius()) - 8), 0, 360, this.getColor(new Double(value)));
    }

    /**
     * 
     * {@inheritDoc}
     */
    @Override
    public void onLoad(Map<String, String> saved) {
        super.onLoad(saved);
        baseColor = RGB.valueOf(saved.get(BASE_COLOR)!=null?saved.get(BASE_COLOR):"255,0,0");
    }

    /**
     * 
     * {@inheritDoc}
     */
    @Override
    public Map<String, String> onSave() {
        Map<String, String> toSaveMap = super.onSave();
        toSaveMap.put(BASE_COLOR, String.valueOf(baseColor));
        return toSaveMap;
    }

    /**
     * @return the baseColor
     */
    public RGB getBaseColor() {
        return this.baseColor;
    }

    /**
     * @param baseColor
     *            the baseColor to set
     */
    public void setBaseColor(final RGB baseColor) {
        this.baseColor = baseColor;
    }

    private RGB getColor(final Number value) {
        final RGB rgb = this.getBaseColor();
        final CIELCH colorspace = rgb.toCIELCH();
        final RGB color = new CIELCH(colorspace.L, colorspace.C, colorspace.H + value.doubleValue()).toCIELab().toXYZ().toRGB();
        return color;
    }

    public static void main(final String[] args) {

        final Display display = new Display();
        final Shell shell = new Shell(display);

        shell.setSize(500, 500);

        shell.open();
        final ColorWheelDashboardPart wheel = new ColorWheelDashboardPart();
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

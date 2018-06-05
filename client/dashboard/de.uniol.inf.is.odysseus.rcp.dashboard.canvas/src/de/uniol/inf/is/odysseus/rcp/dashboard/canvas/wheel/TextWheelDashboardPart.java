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
import java.util.Random;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.rcp.dashboard.canvas.Coordinate;

/**
 * @author Christian Kuka <christian@kuka.cc>
 * @version $Id: TextWheelDashboardPart.java | Sat Apr 11 02:17:02 2015 +0000 |
 *          ckuka $
 *
 */
public class TextWheelDashboardPart extends AbstractWheelDashboardPart {

    /**
     * {@inheritDoc}
     */
    @Override
    public void doPaintZ(final Number z) {
        final String text = NumberFormat.getNumberInstance().format(z);
        final int fontSize = this.getFontSize(text, this.getFont(), (int) ((1.0 / 3.0) * this.getRadius()), (int) ((1.0 / 3.0) * this.getRadius()));
        this.getGC().setFont(new Font(this.getGC().getDevice(), this.getFont(), fontSize, SWT.NORMAL));
        final Coordinate extent = this.textExtent(text);
        this.setBackground(this.getForegroundColor().getComplement());
        this.drawText(text, new Coordinate(this.getCenter().x - (extent.x / 2), this.getCenter().y - (extent.y / 2)), true);
    }

    public static void main(final String[] args) {

        final Display display = new Display();
        final Shell shell = new Shell(display);

        shell.setSize(500, 500);

        shell.open();
        final TextWheelDashboardPart wheel = new TextWheelDashboardPart();
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

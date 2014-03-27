/*******************************************************************************
 * LMS1xx protocol visualization and logging
 * Copyright (C) 2014  Christian Kuka <christian@kuka.cc>
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *******************************************************************************/
package de.uniol.inf.is.odysseus.lms1xx.monitor.ui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

import javax.swing.JPanel;

import de.uniol.inf.is.odysseus.lms1xx.monitor.Settings;
import de.uniol.inf.is.odysseus.lms1xx.monitor.model.Measurement;
import de.uniol.inf.is.odysseus.lms1xx.monitor.model.Sample;

/**
 * @author Christian Kuka <christian@kuka.cc>
 */
public class ScanMap extends JPanel implements KeyListener, MouseListener {

    /**
	 * 
	 */
    private static final long serialVersionUID = 5315446201613125311L;
    private int zoom = 20;
    private final Map<String, Measurement> measurements = new ConcurrentHashMap<String, Measurement>();
    private final Map<String, Double[]> settings = new ConcurrentHashMap<String, Double[]>();
    private String selected;
    private final List<String> devices = new CopyOnWriteArrayList<String>();
    private double mouseX;
    private double mouseY;

    public ScanMap() {
        this.setFocusable(true);
        this.addKeyListener(this);
        this.addMouseListener(this);

    }

    /**
     * 
     * {@inheritDoc}
     */
    @Override
    public void paint(final Graphics graphics) {
        graphics.setColor(Color.BLACK);
        graphics.fillRect(0, 0, 1000, 1000);
        graphics.setColor(Color.WHITE);
        graphics.drawLine(500, 0, 500, 1000);
        graphics.drawLine(0, 500, 1000, 500);
        if (this.measurements.size() > 0) {
            for (final String key : this.measurements.keySet()) {
                final Measurement measurement = this.measurements.get(key);
                final int serial = measurement.getSerial().hashCode();
                final Color color = new Color(((serial) % 255) & 0xFF, ((serial << 2) % 255) & 0xFF, ((serial << 3) % 255) & 0xFF);
                graphics.setColor(color);
                final Sample[] samples = measurement.get16BitSamples();

                final List<Integer> xPoints = new ArrayList<Integer>();
                final List<Integer> yPoints = new ArrayList<Integer>();
                for (int i = 0; i < samples.length; i++) {
                    final Point2D vector = samples[i].getDist1Vector();
                    final double x = vector.getX();
                    final double y = vector.getY();
                    final double angle = Math.toRadians(this.settings.get(key)[2]);
                    vector.setLocation((x * Math.cos(angle)) - (y * Math.sin(angle)), (x * Math.sin(angle)) + (y * Math.cos(angle)));
                    vector.setLocation(vector.getX() - this.settings.get(key)[0].floatValue(), vector.getY() - this.settings.get(key)[1].floatValue());
                    xPoints.add((int) vector.getX() / this.zoom);
                    yPoints.add((int) vector.getY() / this.zoom);
                }
                final int[] tmpXPoints = new int[xPoints.size() + 2];
                tmpXPoints[0] = 500;
                tmpXPoints[xPoints.size() + 1] = 500;
                for (int index = 0; index < xPoints.size(); index++) {
                    tmpXPoints[index + 1] = 500 + xPoints.get(index);
                }
                final int[] tmpYPoints = new int[yPoints.size() + 2];
                tmpYPoints[0] = 500;
                tmpYPoints[yPoints.size() + 1] = 500;
                for (int index = 0; index < yPoints.size(); index++) {
                    tmpYPoints[index + 1] = 500 - yPoints.get(index);
                }
                graphics.drawPolyline(tmpXPoints, tmpYPoints, tmpXPoints.length);

                final int index = this.devices.indexOf(key);
                final double delay = measurement.getDelay();
                graphics.drawString("Key: " + key + " Delay: " + delay + "ms", 5, (index * 35) + 10);
                graphics.drawString("Off: " + this.settings.get(key)[0] + "mm/" + this.settings.get(key)[1] + "mm", 5, (index * 35) + 20);
                graphics.drawString("Angle: " + this.settings.get(key)[2], 5, (index * 35) + 30);

                final double distance = Math.floor(Math.sqrt(Math.pow(this.mouseX, 2) + Math.pow(this.mouseY, 2)));
                if (distance > 0.0) {
                    graphics.drawString(this.mouseX + "mm/" + this.mouseY + "mm | " + distance + "mm", 5, (index * 35) + 40);
                }
                if (Settings.LOGGING) {
                    graphics.drawString("Recording...", 5, (index * 35) + 50);
                }
            }
        }
    }

    public void onMeasurement(final Measurement measurement) {
        final String id = measurement.getSerial();
        this.measurements.put(id, measurement);
        if (!this.devices.contains(id)) {
            this.devices.add(id);
            this.settings.put(id, new Double[] { 0.0, 0.0, 0.0 });
        }
        this.repaint();
    }

    /**
     * 
     * {@inheritDoc}
     */
    @Override
    public void keyPressed(final KeyEvent event) {
        final int keyCode = event.getKeyCode();
        final int number = keyCode - 48;
        if ((number > 0) && (number < 10) && (number <= this.devices.size())) {
            this.selected = this.devices.get(number - 1);
            if (!this.settings.containsKey(this.selected)) {
                this.settings.put(this.selected, new Double[] { 0.0, 0.0, 0.0 });
            }
        }
        else {
            if (this.devices.contains(this.selected)) {
                switch (event.getKeyCode()) {
                    case KeyEvent.VK_RIGHT:
                        if (event.isControlDown()) {

                            this.settings.get(this.selected)[0] -= 10;
                        }
                        else {
                            this.settings.get(this.selected)[0]--;
                        }
                        break;
                    case KeyEvent.VK_LEFT:
                        if (event.isControlDown()) {
                            this.settings.get(this.selected)[0] += 10;
                        }
                        else {
                            this.settings.get(this.selected)[0]++;
                        }
                        break;
                    case KeyEvent.VK_DOWN:
                        if (event.isControlDown()) {
                            this.settings.get(this.selected)[1] += 10;
                        }
                        else {
                            this.settings.get(this.selected)[1]++;
                        }
                        break;
                    case KeyEvent.VK_UP:
                        if (event.isControlDown()) {
                            this.settings.get(this.selected)[1] -= 10;
                        }
                        else {
                            this.settings.get(this.selected)[1]--;
                        }
                        break;
                    case KeyEvent.VK_PAGE_UP:
                        if (event.isControlDown()) {
                            if (this.zoom > 1) {
                                this.zoom--;
                            }
                        }
                        else {
                            this.settings.get(this.selected)[2] += 0.5f;
                        }
                        break;
                    case KeyEvent.VK_PAGE_DOWN:
                        if (event.isControlDown()) {
                            this.zoom++;
                        }
                        else {
                            this.settings.get(this.selected)[2] -= 0.5f;
                        }
                        break;
                    case KeyEvent.VK_L:
                        if (Settings.LOGGING) {
                            Settings.LOGGING = false;
                        }
                        else {
                            Settings.LOGGING = true;
                        }
                        break;
                }
            }
        }
    }

    /**
     * 
     * {@inheritDoc}
     */
    @Override
    public void keyReleased(final KeyEvent e) {

    }

    /**
     * 
     * {@inheritDoc}
     */
    @Override
    public void keyTyped(final KeyEvent e) {

    }

    /**
     * 
     * {@inheritDoc}
     */
    @Override
    public void mouseClicked(final MouseEvent e) {
        this.mouseX = (e.getX() - 500) * this.zoom;
        this.mouseY = (-e.getY() + 500) * this.zoom;
    }

    /**
     * 
     * {@inheritDoc}
     */
    @Override
    public void mouseEntered(final MouseEvent event) {

    }

    /**
     * 
     * {@inheritDoc}
     */
    @Override
    public void mouseExited(final MouseEvent event) {

    }

    /**
     * 
     * {@inheritDoc}
     */
    @Override
    public void mousePressed(final MouseEvent event) {

    }

    /**
     * 
     * {@inheritDoc}
     */
    @Override
    public void mouseReleased(final MouseEvent event) {

    }
}
